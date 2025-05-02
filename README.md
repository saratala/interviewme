# interviewme
I'll help you update the `README.md` file with project information and deployment instructions:

```markdown
# InterviewMe

A real-time interview practice application built with Spring Boot and React that helps users prepare for job interviews through simulated interview sessions.

## Technology Stack

### Backend
- Java 17
- Spring Boot
- Gradle
- WebSocket for real-time communication

### Frontend
- React
- Material-UI
- React Router DOM
- WebSocket client

## Project Setup

### Prerequisites
- Java 17 or higher
- Node.js 14 or higher
- Gradle 7.x
- IDE (IntelliJ IDEA recommended)

### Backend Setup
1. Clone the repository:
```bash
git clone <repository-url>
cd interviewme
```

2. Build the backend:
```bash
./gradlew clean build
```

3. Run the Spring Boot application:
```bash
./gradlew bootRun
```
The backend will start on `http://localhost:8080`

### Frontend Setup
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
npm start
```
The frontend will be available at `http://localhost:3000`

## Development

### Backend Structure
- `config/` - Configuration files
- `controller/` - REST API endpoints
- `service/` - Business logic
- `domain/` - Data models
- `repository/` - Data access layer

### Frontend Structure
- `src/components/` - React components
- `src/context/` - State management
- `src/services/` - API calls
- `src/utils/` - Utility functions

## Deployment

### Backend Deployment
1. Build the production JAR:
```bash
./gradlew build
```

2. Run the JAR:
```bash
java -jar build/libs/interviewme-0.0.1-SNAPSHOT.jar
```

### Frontend Deployment
1. Create production build:
```bash
npm run build
```

2. The build folder can be deployed to any static hosting service

## Features
- Real-time interview sessions
- Audio/video processing
- Session management
- User authentication
- Interview feedback

## Contributing
1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Open a Pull Request
```

This README provides:
- Overview of the application
- Technology stack details
- Setup instructions
- Development guidelines
- Deployment procedures
- Project structure
- Main features
- Contributing guidelines