# Configuration Setup Guide

## Before Testing the POC

You need to update the configuration in `src/main/resources/application.properties` with your actual paths:

### 1. Update Browser Path
Find this line in `application.properties`:
```properties
rpa.browser.path=C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe
```

**Verify this path exists on your system.** If Chrome is installed elsewhere, update it.

### 2. Update ui.vision.html Path
Find this line in `application.properties`:
```properties
rpa.uivision.url=file:///C:/path/to/your/ui.vision.html
```

**Replace with your actual ui.vision.html file path.** For example:
- If your file is at `C:\Users\SahilPandey\Desktop\ui.vision.html`
- Update to: `rpa.uivision.url=file:///C:/Users/SahilPandey/Desktop/ui.vision.html`

**Important Notes:**
- Use forward slashes (`/`) in the file:/// URL, not backslashes
- The path must be absolute (full path from C: drive)
- Make sure the file exists and is accessible

### 3. Restart the Application
After updating the configuration:
1. Stop the current application (Ctrl+C in the terminal)
2. Restart with: `.\mvnw.cmd spring-boot:run`

### 4. Verify Configuration
Test the config endpoint to confirm your paths:
```bash
curl http://localhost:8080/api/v1/rpa/config
```

## Example Configuration
Here's what your configuration should look like (replace with your actual paths):

```properties
# Your actual Chrome installation path
rpa.browser.path=C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe

# Your actual ui.vision.html file path
rpa.uivision.url=file:///C:/Users/SahilPandey/Desktop/ui.vision.html

# Timeout in minutes
rpa.execution.timeout.minutes=10
```

## Testing the Macro Execution
Once configured correctly, you can test with:

```bash
curl -X POST http://localhost:8080/api/v1/rpa/run \
  -H "Content-Type: application/json" \
  -d '{"macroName": "YourMacroName"}'
```

Replace `YourMacroName` with an actual macro name that exists in your ui.vision installation.
