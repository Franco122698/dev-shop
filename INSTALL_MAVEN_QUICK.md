# 🚀 QUICKEST WAY TO FIX "mvn: command not found"

## Choose Your Installation Method

### ⚡ METHOD 1: PowerShell Script (Easiest - Recommended)

**Step 1:** Open PowerShell as Administrator
- Press `Win + X` → Select "Windows PowerShell (Admin)"

**Step 2:** Run the installation script
```powershell
Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope CurrentUser
C:\dev-shop\install-maven.ps1
```

**Step 3:** Close Git Bash and reopen it
- The script modifies environment variables that require a restart

**Step 4:** Verify Maven is installed
```bash
mvn -v
```

---

### 📝 METHOD 2: Batch Script

**Step 1:** Open Command Prompt as Administrator
- Press `Win + R` → Type `cmd` → Press `Ctrl + Shift + Enter`

**Step 2:** Run the installation script
```cmd
C:\dev-shop\install-maven.bat
```

**Step 3:** Close Git Bash and reopen

**Step 4:** Verify
```bash
mvn -v
```

---

### 🔧 METHOD 3: Manual Installation (Most Control)

**Step 1: Check Java**
```bash
java -version
```
If not installed, download Java 17 from: https://www.oracle.com/java/technologies/downloads/

**Step 2: Download Maven**
1. Go to: https://maven.apache.org/release-notes-3.9.6.html
2. Under "Binaries" section, download: `apache-maven-3.9.6-bin.zip`
3. Extract to: `C:\Maven\apache-maven-3.9.6`

**Step 3: Set Environment Variables**

On Windows:
1. Press `Win + Pause` (or right-click This PC → Properties)
2. Click "Advanced system settings"
3. Click "Environment Variables"
4. Under "User variables", click "New"
5. Variable name: `MAVEN_HOME`
6. Variable value: `C:\Maven\apache-maven-3.9.6`
7. Click OK
8. Find "Path" variable → Click "Edit"
9. Click "New" → Add: `C:\Maven\apache-maven-3.9.6\bin`
10. Click OK three times

**Step 4: Restart Git Bash**
- Close completely and reopen

**Step 5: Verify**
```bash
mvn -v
```

---

## Testing After Installation

Once Maven is installed and you've restarted your terminal:

```bash
# Navigate to project
cd C:\dev-shop

# Build without running tests (fast)
mvn clean package -DskipTests

# This should download dependencies and build all modules
# It will take 2-3 minutes on first run
```

---

## ✅ Success Indicators

After running `mvn -v`, you should see:
```
Apache Maven 3.9.6 (ecf0f13) 
Java version: 17.0.x
Java home: C:\Program Files\Java\jdk-17.x.x
Default locale: en_US, platform encoding: UTF-8
```

If you see this, Maven is installed correctly!

---

## 🆘 Still Having Issues?

### "mvn: command not found" after installation
1. **Definitely close Git Bash completely** (don't just use exit)
2. Reopen Git Bash fresh
3. Try: `mvn -v`

### Environment variables not updating
- Log out of Windows and log back in
- Or restart your computer

### Stuck? Use Docker (if available)
If you have Docker installed on your system:
```bash
cd C:\dev-shop
docker run --rm -v "$(pwd)":/opt/maven -w /opt/maven maven:3.9-eclipse-temurin-17 mvn clean package -DskipTests
```

---

## 📦 What Maven Will Do

Once installed, this command:
```bash
mvn clean package -DskipTests
```

Will:
1. ✓ Download all dependencies (libraries)
2. ✓ Compile all 4 services
3. ✓ Package them into JAR files
4. ✓ Create build directories in each service folder

Takes ~2-3 minutes first time, ~30 seconds after that.

---

## Next Step After Maven Works

```bash
# Build
mvn clean package -DskipTests

# Start services
docker-compose up

# Test
curl http://localhost:8080/api/v1/inventory/products -H "X-Store-Id: 1"
```

---

**Recommended:** Try **METHOD 1 (PowerShell Script)** - it's the fastest!

