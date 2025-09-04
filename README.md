# Inveno Drone Server - POC ✅

This is the **successfully tested** Proof of Concept (POC) for the Inveno RPA Drone Server, implementing **Milestone 1** of the RPA platform architecture.

## 🎯 Overview

The Drone Server is a Spring Boot microservice that provides a REST API to execute ui.vision macros on the host machine. It serves as the foundation for the distributed RPA platform.

**✅ SUCCESS: Macro execution is working with automated browser control!**

## 🏗️ Architecture

- **Controller Layer**: `RpaController` - Handles HTTP requests and responses
- **Service Layer**: `RpaExecutionService` - Core business logic for macro execution
- **DTO Layer**: Request/Response objects for API communication
- **Configuration**: Externalized properties for browser paths and timeouts

## ✅ Prerequisites (Verified Working)

1. **Java 17** or higher ✅
2. **Maven 3.6** or higher ✅ (Using Maven Wrapper)
3. **Google Chrome** installed ✅
4. **ui.vision** with XModules installed ✅
5. **ui.vision.html** file accessible ✅

## 🚀 Setup Instructions

### 1. Configure Browser Path

Edit `src/main/resources/application.properties` and update:

```properties
# Update this to your actual Chrome installation path
rpa.browser.path=C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe

# Update this to your actual ui.vision.html file path
rpa.uivision.url=file:///C:/Users/SahilPandey/Downloads/ui.vision.html
```

**Important**: Use forward slashes (`/`) in the file:/// URL, not backslashes.

### 2. Build and Run

```bash
# Build the project
.\mvnw.cmd clean compile

# Run the application
.\mvnw.cmd spring-boot:run
```

The server will start on `http://localhost:8080`

### 3. Verify Installation

Test the health endpoint:
```bash
curl http://localhost:8080/api/v1/rpa/health
```

## 🔌 API Endpoints

### Execute Macro
- **POST** `/api/v1/rpa/run`
- **Body**: `{"macroName": "YourMacroName"}`
- **Response**: Success/failure status with timestamp

### Health Check
- **GET** `/api/v1/rpa/health`
- **Response**: Service health status

### Configuration
- **GET** `/api/v1/rpa/config`
- **Response**: Current configuration details

## 🧪 Testing the POC (Proven Working)

### 1. Start the Server
```bash
.\mvnw.cmd spring-boot:run
```

### 2. Test Health Check
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/v1/rpa/health" -Method Get
```

### 3. Test Configuration
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/v1/rpa/config" -Method Get
```

### 4. Execute a Macro (Working Example)
```powershell
$body = @{ macroName = "initializ" } | ConvertTo-Json
Invoke-RestMethod -Uri "http://localhost:8080/api/v1/rpa/run" -Method Post -Body $body -ContentType "application/json"
```

**Alternative using curl (if available):**
```bash
curl -X POST http://localhost:8080/api/v1/rpa/run \
  -H "Content-Type: application/json" \
  -d '{"macroName": "initializ"}'
```

## ✅ Expected Behavior (Verified)

1. **Successful Execution**: 
   - Browser opens with ui.vision
   - Macro runs automatically (no confirmation dialog)
   - Browser closes automatically after completion
2. **API Response**: Returns `SUCCESS` status with confirmation message
3. **Logging**: Detailed logs in console showing execution progress

## 🔧 Key Technical Solutions

### UI.Vision Automation Fix
The service automatically adds `?direct=1` to the URL to bypass the confirmation dialog:
- **Before**: `file:///path/ui.vision.html?macro=initializ`
- **After**: `file:///path/ui.vision.html?macro=initializ&direct=1`

This enables true automation without user interaction.

### ProcessBuilder Command
The service constructs the command correctly:
```java
processBuilder.command(browserPath, fullUrl);
// Example: "C:\Program Files\Google\Chrome\Application\chrome.exe" "file:///C:/path/ui.vision.html?macro=initializ&direct=1"
```

## 🐛 Troubleshooting

### Common Issues (Resolved)

1. **500 Internal Server Error** ✅ **FIXED**
   - **Cause**: Missing `?direct=1` parameter for UI.Vision
   - **Solution**: Service now automatically adds this parameter

2. **Browser Path Not Found**
   - Verify Chrome installation path in `application.properties`
   - Use double backslashes for Windows paths

3. **ui.vision.html Not Found**
   - Verify file path in `application.properties`
   - Use `file:///` protocol for local files

4. **Permission Denied**
   - Ensure the application has permission to launch browser processes
   - Check Windows User Account Control (UAC) settings

### Debug Mode

Enable debug logging by setting in `application.properties`:
```properties
logging.level.com.inveno.drone=DEBUG
```

## 🚀 Next Steps

After successful POC validation:

1. **Milestone 2**: Build Command Center backend with database
2. **Milestone 3**: Create Hilla UI for task management
3. **Milestone 4**: Add Temporal orchestration and real-time status

## 💡 Development Notes

- The service uses `ProcessBuilder` to launch browser processes
- Windows command handling is implemented for cross-platform compatibility
- Timeout protection prevents hung processes
- Comprehensive error handling and logging throughout
- **UI.Vision integration is fully automated** with `?direct=1` parameter

## 🎉 Success Criteria Met

✅ **A successful API call to the Drone Server executes the macro on the host machine**

The POC is **fully functional** and ready for the next milestone!

## 📞 Support

For issues or questions:
1. Check the console logs for detailed error messages
2. Verify all prerequisites are installed correctly
3. Ensure configuration paths are accurate for your environment
4. The `?direct=1` parameter is automatically handled by the service
