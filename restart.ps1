$ErrorActionPreference = "Stop"
$connections = Get-NetTCPConnection -LocalPort 8080 -ErrorAction SilentlyContinue
if ($connections) {
    $connections.OwningProcess | Select-Object -Unique | ForEach-Object { Stop-Process -Id $_ -Force -ErrorAction SilentlyContinue }
}
Start-Sleep -Seconds 2
$mvn = ".\apache-maven-3.9.6\bin\mvn.cmd"
Write-Host "Re-initializing Database..."
& $mvn sql:execute
Write-Host "Starting Jetty..."
& $mvn clean jetty:run
