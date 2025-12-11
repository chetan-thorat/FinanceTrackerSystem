# Quick Start Guide

## Prerequisites Check

Ensure you have installed:
- ✅ Java JDK 17+
- ✅ Maven 3.6+
- ✅ Node.js 16+ and npm
- ✅ MySQL 8.0+

## Step-by-Step Setup

### 1. Database Setup (5 minutes)

```bash
# Start MySQL service
# Windows: net start MySQL80
# Linux/Mac: sudo systemctl start mysql

# Login to MySQL
mysql -u root -p

# Create database and run schema
CREATE DATABASE expense_tracker;
USE expense_tracker;
SOURCE database/schema.sql;
# Or manually copy-paste the SQL from database/schema.sql
```

### 2. Backend Setup (5 minutes)

```bash
cd backend

# Edit application.properties
# Update these lines:
# spring.datasource.username=your_username
# spring.datasource.password=your_password

# Build and run
mvn clean install
mvn spring-boot:run
```

Backend should be running at: http://localhost:8080

### 3. Frontend Setup (3 minutes)

```bash
cd frontend

# Install dependencies
npm install

# Start development server
npm run dev
```

Frontend should be running at: http://localhost:3000

### 4. Test the Application

1. Open browser: http://localhost:3000
2. Click "Register" and create an account
3. Login with your credentials
4. Add your first expense!
5. View analytics dashboard

## Common Issues

**Backend won't start:**
- Check MySQL is running
- Verify database credentials in `application.properties`
- Check port 8080 is available

**Frontend won't start:**
- Run `npm install` again
- Check port 3000 is available
- Verify backend is running first

**Can't connect to backend:**
- Check backend is running on port 8080
- Verify CORS settings in `SecurityConfig.java`
- Check browser console for errors

## Next Steps

- Read the full [README.md](README.md) for detailed documentation
- Explore the API endpoints
- Customize categories and features

Happy tracking! 💰

