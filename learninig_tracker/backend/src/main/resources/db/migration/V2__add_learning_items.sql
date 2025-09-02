-- Add Learning Items table (V2__add_learning_items.sql)

CREATE TABLE learning_items (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    title VARCHAR(200) NOT NULL,
    description TEXT,
    status VARCHAR(20) NOT NULL CHECK (status IN ('NOT_STARTED', 'IN_PROGRESS', 'COMPLETED', 'ON_HOLD')),
    priority VARCHAR(20) NOT NULL CHECK (priority IN ('LOW', 'MEDIUM', 'HIGH')),
    category_id UUID REFERENCES categories(id) ON DELETE SET NULL,
    progress_percentage INTEGER DEFAULT 0 CHECK (progress_percentage BETWEEN 0 AND 100),
    total_hours DECIMAL(10,2) DEFAULT 0,
    target_date DATE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    notes TEXT
);

-- Create tables for goals related to learning items
CREATE TABLE goals (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    title VARCHAR(200) NOT NULL,
    description TEXT,
    learning_item_id UUID REFERENCES learning_items(id) ON DELETE CASCADE,
    due_date DATE,
    completed BOOLEAN DEFAULT false,
    completed_date TIMESTAMP WITH TIME ZONE,
    priority VARCHAR(20) NOT NULL CHECK (priority IN ('LOW', 'MEDIUM', 'HIGH')),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Create tables for progress entries related to learning items
CREATE TABLE item_progress_entries (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    learning_item_id UUID REFERENCES learning_items(id) ON DELETE CASCADE,
    date DATE NOT NULL,
    hours_spent DECIMAL(4,2) NOT NULL CHECK (hours_spent > 0 AND hours_spent <= 24),
    progress INTEGER CHECK (progress BETWEEN 0 AND 100),
    notes TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Create triggers for learning items
CREATE TRIGGER update_learning_items_updated_at BEFORE UPDATE ON learning_items FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_goals_updated_at BEFORE UPDATE ON goals FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_item_progress_entries_updated_at BEFORE UPDATE ON item_progress_entries FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- Function to update learning item progress based on entries
CREATE OR REPLACE FUNCTION update_learning_item_progress(item_uuid UUID)
RETURNS VOID AS $$
DECLARE
    total_hours DECIMAL(10,2);
    total_progress INTEGER;
    progress_entries_count INTEGER;
BEGIN
    -- Calculate totals from progress entries
    SELECT 
        COALESCE(SUM(hours_spent), 0),
        COALESCE(SUM(progress), 0),
        COUNT(*)
    INTO total_hours, total_progress, progress_entries_count
    FROM item_progress_entries 
    WHERE learning_item_id = item_uuid;
    
    -- Update learning item
    UPDATE learning_items 
    SET 
        total_hours = total_hours,
        progress_percentage = CASE 
            WHEN progress_entries_count > 0 THEN LEAST(total_progress, 100)
            ELSE 0
        END,
        status = CASE
            WHEN progress_percentage >= 100 THEN 'COMPLETED'
            WHEN progress_percentage > 0 THEN 'IN_PROGRESS'
            ELSE status
        END,
        updated_at = CURRENT_TIMESTAMP
    WHERE id = item_uuid;
END;
$$ LANGUAGE plpgsql;

-- Trigger to automatically update learning item progress when entries are added/updated/deleted
CREATE OR REPLACE FUNCTION trigger_update_learning_item_progress()
RETURNS TRIGGER AS $$
BEGIN
    -- Handle INSERT and UPDATE
    IF TG_OP = 'INSERT' OR TG_OP = 'UPDATE' THEN
        PERFORM update_learning_item_progress(NEW.learning_item_id);
        RETURN NEW;
    END IF;
    
    -- Handle DELETE
    IF TG_OP = 'DELETE' THEN
        PERFORM update_learning_item_progress(OLD.learning_item_id);
        RETURN OLD;
    END IF;
    
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER item_progress_entries_update_learning_item
    AFTER INSERT OR UPDATE OR DELETE ON item_progress_entries
    FOR EACH ROW EXECUTE FUNCTION trigger_update_learning_item_progress();

-- Create indexes for better performance
CREATE INDEX idx_learning_items_category ON learning_items(category_id);
CREATE INDEX idx_learning_items_status ON learning_items(status);
CREATE INDEX idx_goals_learning_item ON goals(learning_item_id);
CREATE INDEX idx_item_progress_entries_learning_item ON item_progress_entries(learning_item_id);
CREATE INDEX idx_item_progress_entries_date ON item_progress_entries(date);
