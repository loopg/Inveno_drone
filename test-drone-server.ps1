# Test Script for Inveno Drone Server
Write-Host "=== Inveno Drone Server Test Script ===" -ForegroundColor Green
Write-Host ""

# Test 1: Health Check
Write-Host "1. Testing Health Check..." -ForegroundColor Yellow
try {
    $healthResponse = Invoke-RestMethod -Uri "http://localhost:8080/api/v1/rpa/health" -Method Get
    Write-Host "   ✓ Health Check: $($healthResponse.status) - $($healthResponse.message)" -ForegroundColor Green
} catch {
    Write-Host "   ✗ Health Check Failed: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host ""

# Test 2: Configuration
Write-Host "2. Testing Configuration..." -ForegroundColor Yellow
try {
    $configResponse = Invoke-RestMethod -Uri "http://localhost:8080/api/v1/rpa/config" -Method Get
    Write-Host "   ✓ Configuration: $($configResponse.status)" -ForegroundColor Green
    Write-Host "   Details: $($configResponse.message)" -ForegroundColor Cyan
} catch {
    Write-Host "   ✗ Configuration Failed: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host ""

# Test 3: Macro Execution
Write-Host "3. Testing Macro Execution..." -ForegroundColor Yellow
Write-Host "   Note: Enter an actual macro name from your ui.vision installation" -ForegroundColor Cyan

$macroName = Read-Host "   Enter macro name to test (or press Enter to skip)"

if ($macroName) {
    try {
        $body = @{
            macroName = $macroName
        } | ConvertTo-Json

        Write-Host "   Executing macro: $macroName" -ForegroundColor Cyan
        
        $executionResponse = Invoke-RestMethod -Uri "http://localhost:8080/api/v1/rpa/run" -Method Post -Body $body -ContentType "application/json"
        
        Write-Host "   ✓ Macro Execution: $($executionResponse.status)" -ForegroundColor Green
        Write-Host "   Message: $($executionResponse.message)" -ForegroundColor Cyan
        Write-Host "   Timestamp: $($executionResponse.timestamp)" -ForegroundColor Cyan
        
    } catch {
        Write-Host "   ✗ Macro Execution Failed: $($_.Exception.Message)" -ForegroundColor Red
    }
} else {
    Write-Host "   Skipping macro execution test" -ForegroundColor Yellow
}

Write-Host ""
Write-Host "=== Test Complete ===" -ForegroundColor Green
Write-Host ""
Write-Host "Next Steps:" -ForegroundColor Cyan
Write-Host "1. Check the application logs for execution details" -ForegroundColor White
Write-Host "2. Verify the browser opened and macro executed" -ForegroundColor White
Write-Host "3. Check if browser closed automatically after completion" -ForegroundColor White
