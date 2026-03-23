$ErrorActionPreference = "Stop"
$mvn = ".\apache-maven-3.9.6\bin\mvn.cmd"
Write-Host "Using Maven to configure your MySQL database..."
& $mvn sql:execute
Write-Host "Starting Jetty Web Server..."
& $mvn clean jetty:run
