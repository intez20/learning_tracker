-- Learning Tracker PostgreSQL Database Schema
-- Run this script to create the complete database structure

-- Create database (run this separately as superuser)
-- CREATE DATABASE learning_tracker;

-- Connect to the learning_tracker database and run the following:

-- Enable UUID extension for better ID generation
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Categories table for organizing learning areas
CREATE TABLE categories (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    color VARCHAR(7) DEFAULT '#4f46e5', -- Hex color for UI
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Projects/Tutorials table for tracking different learning paths
CREATE TABLE projects (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    title VARCHAR(200) NOT NULL,
    description TEXT,
    category_id UUID REFERENCES categories(id) ON DELETE SET NULL,
    status VARCHAR(20) DEFAULT 'active' CHECK (status IN ('active', 'completed', 'paused', 'abandoned')),
    start_date DATE,
    target_completion_date DATE,
    actual_completion_date DATE,
    difficulty_level INTEGER CHECK (difficulty_level BETWEEN 1 AND 5),
    estimated_hours DECIMAL(5,2),
    actual_hours DECIMAL(5,2) DEFAULT 0,
    progress_percentage INTEGER DEFAULT 0 CHECK (progress_percentage BETWEEN 0 AND 100),
    github_url TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Learning resources table for tracking where you learn from
CREATE TABLE learning_resources (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    project_id UUID REFERENCES projects(id) ON DELETE CASCADE,
    title VARCHAR(200) NOT NULL,
    url TEXT NOT NULL,
    resource_type VARCHAR(50) CHECK (resource_type IN ('course', 'tutorial', 'documentation', 'video', 'book', 'article', 'practice')),
    platform VARCHAR(100), -- e.g., 'Udemy', 'YouTube', 'LeetCode', 'Official Docs'
    instructor_author VARCHAR(100),
    is_primary BOOLEAN DEFAULT false, -- Mark the main resource for a project
    completion_percentage INTEGER DEFAULT 0 CHECK (completion_percentage BETWEEN 0 AND 100),
    rating INTEGER CHECK (rating BETWEEN 1 AND 5),
    notes TEXT CHECK (LENGTH(notes) <= 500),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Daily progress entries table (enhanced version of your current structure)
CREATE TABLE progress_entries (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    project_id UUID REFERENCES projects(id) ON DELETE CASCADE,
    entry_date DATE NOT NULL,
    day_of_week VARCHAR(20) NOT NULL,
    focus_area VARCHAR(100) NOT NULL,
    what_studied TEXT NOT NULL,
    hours_spent DECIMAL(4,2) NOT NULL CHECK (hours_spent > 0 AND hours_spent <= 24),
    confidence_level INTEGER NOT NULL CHECK (confidence_level BETWEEN 1 AND 5),
    session_notes TEXT CHECK (LENGTH(session_notes) <= 500),
    next_steps TEXT CHECK (LENGTH(next_steps) <= 500),
    github_commit_url TEXT, -- Link to specific commits made during this session
    resource_id UUID REFERENCES learning_resources(id) ON DELETE SET NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Learning goals table for setting and tracking objectives
CREATE TABLE learning_goals (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    project_id UUID REFERENCES projects(id) ON DELETE CASCADE,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    goal_type VARCHAR(20) CHECK (goal_type IN ('skill', 'hours', 'project', 'certification')),
    target_value DECIMAL(10,2), -- Could be hours, percentage, etc.
    current_value DECIMAL(10,2) DEFAULT 0,
    unit VARCHAR(20), -- 'hours', 'percentage', 'problems solved', etc.
    deadline DATE,
    is_completed BOOLEAN DEFAULT false,
    completed_at TIMESTAMP WITH TIME ZONE,
    priority INTEGER DEFAULT 3 CHECK (priority BETWEEN 1 AND 5), -- 1 = highest priority
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Code snippets table for saving important code examples
CREATE TABLE code_snippets (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    project_id UUID REFERENCES projects(id) ON DELETE CASCADE,
    progress_entry_id UUID REFERENCES progress_entries(id) ON DELETE CASCADE,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    code_content TEXT NOT NULL,
    language VARCHAR(50) NOT NULL,
    tags TEXT[], -- Array of tags for better organization
    github_link TEXT,
    is_working BOOLEAN DEFAULT true,
    notes TEXT CHECK (LENGTH(notes) <= 500),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Weekly milestones table for tracking roadmap progress
CREATE TABLE weekly_milestones (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    week_number INTEGER NOT NULL CHECK (week_number BETWEEN 1 AND 52),
    focus_area VARCHAR(100) NOT NULL,
    topics TEXT NOT NULL,
    target_hours_per_day DECIMAL(3,2),
    actual_hours_completed DECIMAL(5,2) DEFAULT 0,
    completion_percentage INTEGER DEFAULT 0 CHECK (completion_percentage BETWEEN 0 AND 100),
    is_completed BOOLEAN DEFAULT false,
    completed_at TIMESTAMP WITH TIME ZONE,
    notes TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for better performance
CREATE INDEX idx_progress_entries_date ON progress_entries(entry_date DESC);
CREATE INDEX idx_progress_entries_project ON progress_entries(project_id);
CREATE INDEX idx_progress_entries_focus ON progress_entries(focus_area);
CREATE INDEX idx_projects_status ON projects(status);
CREATE INDEX idx_projects_category ON projects(category_id);
CREATE INDEX idx_learning_resources_project ON learning_resources(project_id);
CREATE INDEX idx_learning_goals_project ON learning_goals(project_id);
CREATE INDEX idx_code_snippets_project ON code_snippets(project_id);
CREATE INDEX idx_weekly_milestones_week ON weekly_milestones(week_number);

-- Create triggers for automatic timestamp updates
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Apply trigger to all tables with updated_at column
CREATE TRIGGER update_categories_updated_at BEFORE UPDATE ON categories FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_projects_updated_at BEFORE UPDATE ON projects FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_learning_resources_updated_at BEFORE UPDATE ON learning_resources FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_progress_entries_updated_at BEFORE UPDATE ON progress_entries FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_learning_goals_updated_at BEFORE UPDATE ON learning_goals FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_code_snippets_updated_at BEFORE UPDATE ON code_snippets FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_weekly_milestones_updated_at BEFORE UPDATE ON weekly_milestones FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- Insert default categories
INSERT INTO categories (name, description, color) VALUES 
('Data Engineering', 'Big data processing, ETL, pipelines', '#4f46e5'),
('Apache Spark', 'Spark framework and ecosystem', '#f59e0b'),
('SQL & Databases', 'SQL practice and database concepts', '#059669'),
('Programming', 'General programming concepts', '#dc2626'),
('DevOps & Tools', 'Development tools and practices', '#7c3aed'),
('Machine Learning', 'ML concepts and implementations', '#0891b2');

-- Insert initial roadmap as weekly milestones
INSERT INTO weekly_milestones (week_number, focus_area, topics, target_hours_per_day, notes) VALUES 
(1, 'Spark Foundation', 'Complete remaining 7 hrs of Taming Big Data with Apache Spark - RDDs, DataFrames, basic operations', 2.25, 'Focus on hands-on coding examples and practice'),
(2, 'Spark Advanced', 'Start Data Engineering Essentials course - Spark SQL, joins, aggregations, performance tuning', 2.25, 'Build small projects to reinforce learning'),
(3, 'SQL Practice', 'LeetCode Easy SQL problems (20-30 problems) + DataLemur basics', 2.25, 'Focus on SELECT, WHERE, GROUP BY, basic JOINs'),
(4, 'SQL Intermediate', 'LeetCode Medium SQL problems + DataExpert.io questions - Window functions, CTEs', 2.25, 'Practice complex queries and optimization'),
(5, 'Data Engineering', 'YouTube playlist (Darshil Parmar) - ETL concepts, data pipelines, architecture basics', 2.25, 'Take notes on key concepts and tools'),
(6, 'Data Engineering', 'Continue YouTube playlist - Apache Kafka, Airflow, data warehousing concepts', 2.25, 'Set up local environments for hands-on practice'),
(7, 'Spark Deep Dive', 'Advanced Spark concepts from course - streaming, MLlib basics, cluster optimization', 2.25, 'Work on a comprehensive Spark project'),
(8, 'SQL Mastery', 'LeetCode Hard problems + DataExpert.io advanced + DataLemur company-specific questions', 2.25, 'Focus on interview-style problems'),
(9, 'Integration Project', 'Build end-to-end data pipeline using Spark + SQL + concepts from YouTube playlist', 2.25, 'Document the project for portfolio'),
(10, 'Review & Polish', 'Review all materials, complete any unfinished sections, polish projects', 2.25, 'Prepare for interviews, update LinkedIn/resume');

-- Create some sample projects based on your roadmap
INSERT INTO projects (title, description, category_id, status, difficulty_level, estimated_hours) VALUES 
('Apache Spark Fundamentals', 'Complete Taming Big Data with Apache Spark course', 
 (SELECT id FROM categories WHERE name = 'Apache Spark'), 'active', 3, 20),
('Data Engineering Essentials', 'Advanced Spark concepts and data engineering patterns', 
 (SELECT id FROM categories WHERE name = 'Data Engineering'), 'active', 4, 25),
('SQL Mastery Path', 'LeetCode SQL problems from Easy to Hard', 
 (SELECT id FROM categories WHERE name = 'SQL & Databases'), 'active', 3, 30),
('End-to-End Data Pipeline', 'Capstone project integrating all learned concepts', 
 (SELECT id FROM categories WHERE name = 'Data Engineering'), 'active', 5, 35);

-- Create views for common queries
CREATE VIEW project_progress_summary AS
SELECT 
    p.id,
    p.title,
    p.status,
    c.name as category_name,
    p.progress_percentage,
    p.actual_hours,
    p.estimated_hours,
    COUNT(pe.id) as total_sessions,
    AVG(pe.confidence_level) as avg_confidence,
    MAX(pe.entry_date) as last_session_date,
    SUM(pe.hours_spent) as total_logged_hours
FROM projects p
LEFT JOIN categories c ON p.category_id = c.id
LEFT JOIN progress_entries pe ON p.id = pe.project_id
GROUP BY p.id, p.title, p.status, c.name, p.progress_percentage, p.actual_hours, p.estimated_hours;

CREATE VIEW daily_progress_summary AS
SELECT 
    entry_date,
    COUNT(*) as sessions_count,
    SUM(hours_spent) as total_hours,
    AVG(confidence_level) as avg_confidence,
    STRING_AGG(DISTINCT focus_area, ', ') as focus_areas
FROM progress_entries
GROUP BY entry_date
ORDER BY entry_date DESC;

CREATE VIEW resource_effectiveness AS
SELECT 
    lr.id,
    lr.title,
    lr.resource_type,
    lr.platform,
    lr.completion_percentage,
    lr.rating,
    COUNT(pe.id) as sessions_using_resource,
    AVG(pe.confidence_level) as avg_confidence_gain,
    SUM(pe.hours_spent) as total_hours_spent
FROM learning_resources lr
LEFT JOIN progress_entries pe ON lr.id = pe.resource_id
GROUP BY lr.id, lr.title, lr.resource_type, lr.platform, lr.completion_percentage, lr.rating;

-- Sample data for learning resources
INSERT INTO learning_resources (project_id, title, url, resource_type, platform, instructor_author, is_primary) VALUES 
((SELECT id FROM projects WHERE title = 'Apache Spark Fundamentals'), 
 'Taming Big Data with Apache Spark', 
 'https://www.udemy.com/course/taming-big-data-with-apache-spark-hands-on/', 
 'course', 'Udemy', 'Frank Kane', true),

((SELECT id FROM projects WHERE title = 'Data Engineering Essentials'), 
 'Data Engineering Essentials Course', 
 'https://example-platform.com/data-engineering', 
 'course', 'Online Platform', 'Various', true),

((SELECT id FROM projects WHERE title = 'SQL Mastery Path'), 
 'LeetCode SQL Problems', 
 'https://leetcode.com/problemset/database/', 
 'practice', 'LeetCode', 'LeetCode', true),

((SELECT id FROM projects WHERE title = 'SQL Mastery Path'), 
 'DataLemur SQL Practice', 
 'https://datalemur.com/', 
 'practice', 'DataLemur', 'DataLemur', false),

((SELECT id FROM projects WHERE title = 'Data Engineering Essentials'), 
 'Darshil Parmar Data Engineering Playlist', 
 'https://youtube.com/playlist?list=PLBJe2dFI4sguF2nU6Z3Od7BX8eALZN3mU', 
 'video', 'YouTube', 'Darshil Parmar', false);

-- Functions for common operations

-- Function to update project progress based on entries
CREATE OR REPLACE FUNCTION update_project_progress(project_uuid UUID)
RETURNS VOID AS $$
DECLARE
    total_hours DECIMAL(5,2);
    avg_confidence DECIMAL(3,2);
    latest_entry_date DATE;
BEGIN
    -- Calculate totals from progress entries
    SELECT 
        COALESCE(SUM(hours_spent), 0),
        COALESCE(AVG(confidence_level), 0),
        MAX(entry_date)
    INTO total_hours, avg_confidence, latest_entry_date
    FROM progress_entries 
    WHERE project_id = project_uuid;
    
    -- Update project
    UPDATE projects 
    SET 
        actual_hours = total_hours,
        progress_percentage = CASE 
            WHEN estimated_hours > 0 THEN LEAST(ROUND((total_hours / estimated_hours) * 100), 100)
            ELSE CASE 
                WHEN avg_confidence >= 4.5 THEN 90
                WHEN avg_confidence >= 4.0 THEN 75
                WHEN avg_confidence >= 3.0 THEN 50
                WHEN avg_confidence >= 2.0 THEN 25
                ELSE 10
            END
        END,
        status = CASE 
            WHEN progress_percentage >= 100 OR avg_confidence >= 4.8 THEN 'completed'
            WHEN latest_entry_date < CURRENT_DATE - INTERVAL '14 days' THEN 'paused'
            ELSE status
        END,
        actual_completion_date = CASE 
            WHEN progress_percentage >= 100 AND actual_completion_date IS NULL THEN latest_entry_date
            ELSE actual_completion_date
        END,
        updated_at = CURRENT_TIMESTAMP
    WHERE id = project_uuid;
END;
$$ LANGUAGE plpgsql;

-- Trigger to automatically update project progress when entries are added/updated/deleted
CREATE OR REPLACE FUNCTION trigger_update_project_progress()
RETURNS TRIGGER AS $$
BEGIN
    -- Handle INSERT and UPDATE
    IF TG_OP = 'INSERT' OR TG_OP = 'UPDATE' THEN
        PERFORM update_project_progress(NEW.project_id);
        RETURN NEW;
    END IF;
    
    -- Handle DELETE
    IF TG_OP = 'DELETE' THEN
        PERFORM update_project_progress(OLD.project_id);
        RETURN OLD;
    END IF;
    
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER progress_entries_update_project 
    AFTER INSERT OR UPDATE OR DELETE ON progress_entries
    FOR EACH ROW EXECUTE FUNCTION trigger_update_project_progress();

-- Useful queries for the application

-- Get dashboard statistics
CREATE OR REPLACE VIEW dashboard_stats AS
SELECT 
    (SELECT COUNT(*) FROM projects WHERE status = 'active') as active_projects,
    (SELECT COUNT(*) FROM projects WHERE status = 'completed') as completed_projects,
    (SELECT COALESCE(SUM(actual_hours), 0) FROM projects) as total_hours_all_projects,
    (SELECT COUNT(*) FROM progress_entries WHERE entry_date >= CURRENT_DATE - INTERVAL '7 days') as sessions_this_week,
    (SELECT COALESCE(AVG(confidence_level), 0) FROM progress_entries WHERE entry_date >= CURRENT_DATE - INTERVAL '30 days') as avg_confidence_30days,
    (SELECT COUNT(DISTINCT entry_date) FROM progress_entries WHERE entry_date >= CURRENT_DATE - INTERVAL '30 days') as study_days_30days;

-- Get learning streak (consecutive days with entries)
CREATE OR REPLACE FUNCTION get_current_streak()
RETURNS INTEGER AS $$
DECLARE
    streak_count INTEGER := 0;
    check_date DATE := CURRENT_DATE;
    found_entry BOOLEAN;
BEGIN
    -- Check if there's an entry for today, if not start from yesterday
    SELECT EXISTS(SELECT 1 FROM progress_entries WHERE entry_date = CURRENT_DATE) INTO found_entry;
    IF NOT found_entry THEN
        check_date := CURRENT_DATE - INTERVAL '1 day';
    END IF;
    
    -- Count consecutive days with entries
    LOOP
        SELECT EXISTS(SELECT 1 FROM progress_entries WHERE entry_date = check_date) INTO found_entry;
        IF found_entry THEN
            streak_count := streak_count + 1;
            check_date := check_date - INTERVAL '1 day';
        ELSE
            EXIT;
        END IF;
    END LOOP;
    
    RETURN streak_count;
END;
$$ LANGUAGE plpgsql;

-- Common queries that your frontend will use:

-- 1. Get all projects with their progress
/*
SELECT 
    p.*,
    c.name as category_name,
    c.color as category_color,
    COUNT(pe.id) as total_sessions,
    COALESCE(AVG(pe.confidence_level), 0) as avg_confidence
FROM projects p
LEFT JOIN categories c ON p.category_id = c.id
LEFT JOIN progress_entries pe ON p.id = pe.project_id
GROUP BY p.id, c.name, c.color
ORDER BY p.created_at DESC;
*/

-- 2. Get recent progress entries with project info
/*
SELECT 
    pe.*,
    p.title as project_title,
    c.name as category_name,
    lr.title as resource_title,
    lr.url as resource_url
FROM progress_entries pe
LEFT JOIN projects p ON pe.project_id = p.id
LEFT JOIN categories c ON p.category_id = c.id
LEFT JOIN learning_resources lr ON pe.resource_id = lr.id
ORDER BY pe.entry_date DESC, pe.created_at DESC
LIMIT 50;
*/

-- 3. Get project resources
/*
SELECT lr.*, p.title as project_title
FROM learning_resources lr
JOIN projects p ON lr.project_id = p.id
WHERE lr.project_id = $1
ORDER BY lr.is_primary DESC, lr.created_at;
*/

-- 4. Analytics queries
/*
-- Hours by focus area (last 30 days)
SELECT 
    focus_area,
    SUM(hours_spent) as total_hours,
    COUNT(*) as session_count,
    AVG(confidence_level) as avg_confidence
FROM progress_entries 
WHERE entry_date >= CURRENT_DATE - INTERVAL '30 days'
GROUP BY focus_area
ORDER BY total_hours DESC;

-- Daily progress timeline
SELECT 
    entry_date,
    SUM(hours_spent) as daily_hours,
    AVG(confidence_level) as daily_avg_confidence,
    COUNT(*) as session_count
FROM progress_entries 
WHERE entry_date >= CURRENT_DATE - INTERVAL '90 days'
GROUP BY entry_date
ORDER BY entry_date;

-- Weekly progress summary
SELECT 
    DATE_TRUNC('week', entry_date) as week_start,
    SUM(hours_spent) as weekly_hours,
    AVG(confidence_level) as weekly_avg_confidence,
    COUNT(DISTINCT entry_date) as study_days_in_week
FROM progress_entries 
WHERE entry_date >= CURRENT_DATE - INTERVAL '12 weeks'
GROUP BY DATE_TRUNC('week', entry_date)
ORDER BY week_start;
*/

-- Create user for the application (run as superuser)
-- CREATE USER learning_tracker_app WITH PASSWORD 'your_secure_password_here';
-- GRANT ALL PRIVILEGES ON DATABASE learning_tracker TO learning_tracker_app;
-- GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO learning_tracker_app;
-- GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO learning_tracker_app;
-- GRANT EXECUTE ON ALL FUNCTIONS IN SCHEMA public TO learning_tracker_app;