# Maven Installation Guide for Windows

## Quick Installation (Using Chocolatey)

If you have Chocolatey installed:
```bash
choco install maven
```

## Manual Installation (Windows - Recommended)

### Step 1: Check Java is Installed
```bash
java -version
```
If not installed, download Java 17 from: https://www.oracle.com/java/technologies/downloads/

### Step 2: Download Maven
1. Go to: https://maven.apache.org/download.cgi
2. Download **apache-maven-3.9.6-bin.zip** (or latest version)
3. Extract to a location like: `C:\Program Files\apache-maven-3.9.6`

### Step 3: Set Environment Variables

**Windows:**
1. Right-click **This PC** or **My Computer** → Properties
2. Click **Advanced system settings**
3. Click **Environment Variables**
4. Under "System variables", click **New**
5. Create variable:
   - Variable name: `MAVEN_HOME`
   - Variable value: `C:\Program Files\apache-maven-3.9.6` (adjust path)
6. Click OK
7. Find **Path** variable → Click **Edit**
8. Click **New** → Add: `%MAVEN_HOME%\bin`
9. Click OK → OK → OK

### Step 4: Verify Installation
Restart your terminal/Git Bash and run:
```bash
mvn -v
```

You should see:
```
Apache Maven 3.9.6
Java version: 17.x.x
```

## Troubleshooting

### "mvn: command not found" on Git Bash
Try running:
```bash
source ~/.bashrc
```

Or restart Git Bash completely.

### Still not working?
Use full path:
```bash
"C:\Program Files\apache-maven-3.9.6\bin\mvn" clean package -DskipTests
```

---

## Alternative: Using Docker (No Installation Required)

If you want to avoid installation, you can run Maven inside Docker:

```bash
cd C:\dev-shop
docker run --rm -v "%cd%":/opt/maven -w /opt/maven maven:3.9-eclipse-temurin-17 mvn clean package -DskipTests
```

This downloads Maven Docker image and runs it without installing anything on your system.

---

## Next Steps

After Maven is installed, run:
```bash
cd C:\dev-shop
mvn clean package -DskipTests
docker-compose up
```

This will build and start all services!

