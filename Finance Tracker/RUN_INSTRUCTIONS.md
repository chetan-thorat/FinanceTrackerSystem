# How to Run the Intelligent Expense Tracker

Follow these steps to get the application running on your Windows machine.

## Prerequisites Check

Before starting, make sure you have these installed:

1. **Java JDK 17 or higher**
   - Check: Open PowerShell and run `java -version`
   - If not installed: Download from [Oracle](https://www.oracle.com/java/technologies/downloads/) or [OpenJDK](https://adoptium.net/)

2. **Maven 3.6+**
   - Check: Run `mvn -version` in PowerShell
   - If not installed: Download from [Maven](https://maven.apache.org/download.cgi)

3. **Node.js 16+ and npm**
   - Check: Run `node -v` and `npm -v`
   - If not installed: Download from [Node.js](https://nodejs.org/)

4. **MySQL 8.0+**
   - Check: Run `mysql --version`
   - If not installed: Download from [MySQL](https://dev.mysql.com/downloads/mysql/)

---

## Step 1: Start MySQL Database

1. **Start MySQL Service:**
   ```powershell
   # Option 1: Using Services (GUI)
   # Press Win+R, type "services.msc", find MySQL80, right-click and Start
   
   # Option 2: Using Command Line
   net start MySQL80
   ```

2. **Login to MySQL:**
   ```powershell
   mysql -u root -p
   # Enter your MySQL root password when prompted
   ```

3. **Create Database and Tables:**
   ```sql
   -- Option 1: Run the SQL file directly
   SOURCE E:/Finance Tracker/database/schema.sql;
   
   -- Option 2: Or copy-paste the SQL commands manually
   CREATE DATABASE IF NOT EXISTS expense_tracker;
   USE expense_tracker;
   -- Then copy all SQL from database/schema.sql file
   ```

   **Or use MySQL Workbench:**
   - Open MySQL Workbench
   - Connect to your MySQL server
   - File → Open SQL Script → Select `database/schema.sql`
   - Click Execute (⚡ icon)

---

## Step 2: Configure Backend

1. **Navigate to backend folder:**
   ```powershell
   cd "E:\Finance Tracker\backend"
   ```

2. **Edit `src/main/resources/application.properties`:**
   
   Open the file and update these lines with your MySQL credentials:
   ```properties
   spring.datasource.username=root          # Your MySQL username
   spring.datasource.password=your_password  # Your MySQL password
   ```
   
   **Important:** Replace `your_password` with your actual MySQL root password.

3. **Verify the database URL is correct:**
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/expense_tracker?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
   ```

---

## Step 3: Build and Run Backend

1. **Open PowerShell in the backend folder:**
   ```powershell
   cd "E:\Finance Tracker\backend"
   ```

2. **Build the project:**
   ```powershell
   mvn clean install
   ```
   This will download dependencies and compile the project. It may take a few minutes the first time.

3. **Run the Spring Boot application:**
   ```powershell
   mvn spring-boot:run
   ```

4. **Wait for startup:**
   You should see output like:
   ```
   Started ExpenseTrackerApplication in X.XXX seconds
   ```

5. **Verify backend is running:**
   - Open browser: http://localhost:8080/api/categories
   - You should see a JSON response with categories

   **Keep this terminal window open!** The backend must stay running.

---

## Step 4: Setup and Run Frontend

1. **Open a NEW PowerShell window** (keep backend running in the first one)

2. **Navigate to frontend folder:**
   ```powershell
   cd "E:\Finance Tracker\frontend"
   ```

3. **Install dependencies:**
   ```powershell
   npm install
   ```
   This will download all React packages. Wait for it to complete.

4. **Start the development server:**
   ```powershell
   npm run dev
   ```

5. **Wait for startup:**
   You should see:
   ```
   VITE v5.x.x  ready in XXX ms
   ➜  Local:   http://localhost:3000/
   ```

   **Keep this terminal window open too!**

---

## Step 5: Access the Application

1. **Open your web browser** (Chrome, Firefox, Edge, etc.)

2. **Navigate to:** http://localhost:3000

3. **You should see the login page!**

---

## Step 6: Create Your First Account

1. **Click "Register"** on the login page

2. **Fill in the registration form:**
   - Username: Choose a username (e.g., "john_doe")
   - Email: Your email address
   - Password: At least 6 characters
   - First Name & Last Name: Optional

3. **Click "Register"**

4. **You'll be automatically logged in and redirected to the Dashboard!**

---

## Step 7: Test the Application

1. **Add an Expense:**
   - Click "Expenses" in the navigation bar
   - Click "+ Add Expense"
   - Fill in:
     - Category: Select one (e.g., Food 🍔)
     - Amount: Enter a number (e.g., 25.50)
     - Date: Select today's date
     - Description: "Test expense"
   - Click "Create"

2. **View Analytics:**
   - Click "Analytics" in the navigation bar
   - You should see charts showing your expense data

3. **View Dashboard:**
   - Click "Dashboard" to see summary statistics

---

## Troubleshooting

### Backend Won't Start

**Error: "Port 8080 already in use"**
```powershell
# Find what's using port 8080
netstat -ano | findstr :8080

# Kill the process (replace PID with actual process ID)
taskkill /PID <PID> /F

# Or change port in application.properties:
server.port=8081
```

**Error: "Cannot connect to database"**
- Verify MySQL is running: `net start MySQL80`
- Check username/password in `application.properties`
- Verify database exists: `mysql -u root -p` then `SHOW DATABASES;`

**Error: "JAVA_HOME not set"**
```powershell
# Set JAVA_HOME (replace with your Java path)
setx JAVA_HOME "C:\Program Files\Java\jdk-17"
```

### Frontend Won't Start

**Error: "Port 3000 already in use"**
```powershell
# Change port in vite.config.js:
server: { port: 3001 }
```

**Error: "npm install fails"**
```powershell
# Clear cache and retry
npm cache clean --force
rm -r node_modules
rm package-lock.json
npm install
```

**Error: "Cannot connect to backend"**
- Verify backend is running on http://localhost:8080
- Check browser console (F12) for errors
- Verify CORS settings in `SecurityConfig.java`

### Database Issues

**Tables not created:**
- Run `database/schema.sql` manually in MySQL
- Or let Spring Boot create them (set `spring.jpa.hibernate.ddl-auto=create` temporarily)

**Categories missing:**
- The schema.sql includes INSERT statements for categories
- If missing, run the INSERT statements manually

---

## Quick Commands Reference

### Start Everything (in order):

**Terminal 1 - Backend:**
```powershell
cd "E:\Finance Tracker\backend"
mvn spring-boot:run
```

**Terminal 2 - Frontend:**
```powershell
cd "E:\Finance Tracker\frontend"
npm run dev
```

### Stop Everything:

- **Backend:** Press `Ctrl+C` in backend terminal
- **Frontend:** Press `Ctrl+C` in frontend terminal
- **MySQL:** `net stop MySQL80` (if you want to stop MySQL)

---

## What's Running Where?

- **Backend API:** http://localhost:8080
- **Frontend App:** http://localhost:3000
- **MySQL Database:** localhost:3306

---

## Next Steps

- Read the full [README.md](README.md) for detailed documentation
- Explore API endpoints using Postman or browser
- Customize categories and features
- Deploy to production when ready

**Happy Expense Tracking! 💰**

