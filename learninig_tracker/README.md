# Learning Tracker Application

A comprehensive application for tracking your learning progress across various subjects, projects, and tutorials. Built with Spring Boot 3+ and React 18+.

## Project Structure

This project is organized as follows:

- `/backend` - Contains the Spring Boot backend application
  - Spring Boot 3.2.3 with Java 17
  - JPA for database access
  - PostgreSQL database
  - RESTful API endpoints
  
- `/frontend` - Contains the React frontend application
  - React 18 with TypeScript
  - Tailwind CSS for styling
  - Vite for the build system

- Root directory - Contains project-wide files such as README, Docker configuration, etc.

**NOTE:** The duplicate backend code that was in the root `/src` directory has been renamed to `/src_outdated` and should not be used. All development should be done in the `/backend` and `/frontend` directories.

## Features

- Track your learning items, progress, and goals
- Organize items by categories
- Add detailed notes and resource links
- Visualize your progress with analytics
- Export your data
- Dark/light theme support

## Tech Stack

### Backend
- Spring Boot 3+
- Java 17+
- PostgreSQL
- Flyway for migrations
- Spring Data JPA
- OpenAPI/Swagger for API documentation

### Frontend
- React 18+
- TailwindCSS + shadcn/ui
- React Query for data fetching
- Recharts for visualizations
- Framer Motion for animations

## Getting Started

### Prerequisites
- Java 17+
- Node.js 16+
- Docker (for running PostgreSQL)

### Running the Backend

1. Navigate to the backend directory:
   ```bash
   cd backend
   ```

2. Start the database:
   ```bash
   docker-compose up -d
   ```

3. Run the Spring Boot application:
   ```bash
   ./gradlew bootRun
   ```

4. Access the API documentation at: http://localhost:8080/swagger-ui.html

### Running the Frontend

1. Navigate to the frontend directory:
   ```bash
   cd frontend
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

3. Start the development server:
   ```bash
   npm run dev
   ```

4. Access the app at: http://localhost:5173

## Database Schema

### Category
- `id` (UUID, PK)
- `name` (string, required, unique)
- `description` (string, optional)

### LearningItem
- `id` (UUID, PK)
- `title` (string, required)
- `description` (string, required)
- `category_id` (FK → Category)
- `created_date` (timestamp)
- `target_date` (timestamp, optional)
- `status` (enum: in-progress, completed, paused)
- `total_hours` (float, default 0)
- `progress_percentage` (int, 0–100)
- `resource_url` (string, optional → link to tutorial/course/resource)
- `notes` (string, up to 500 chars → revision notes)
- `github_url` (string, optional → link to repo for code/progress)

### ProgressEntry
- `id` (UUID, PK)
- `learning_item_id` (FK → LearningItem)
- `date` (date)
- `duration` (float, hours spent)
- `notes` (string, up to 250 chars)
- `progress_added` (int, percentage points)

### Goal
- `id` (UUID, PK)
- `title` (string, required)
- `description` (string, optional)
- `target_date` (timestamp)
- `status` (enum: active, completed, abandoned)
- `progress_percentage` (int, 0–100)

## API Endpoints

- `/api/categories` - CRUD operations for categories
- `/api/learning-items` - CRUD operations for learning items
- `/api/progress-entries` - CRUD operations for progress entries
- `/api/goals` - CRUD operations for goals
- `/api/analytics/*` - Analytics endpoints
