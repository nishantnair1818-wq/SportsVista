$ErrorActionPreference = "Stop"
Write-Host "Downloading Maven locally for this project to run..."
Invoke-WebRequest -Uri "https://archive.apache.org/dist/maven/maven-3/3.9.6/binaries/apache-maven-3.9.6-bin.zip" -OutFile "maven.zip"
Write-Host "Extracting Maven..."
Expand-Archive -Path "maven.zip" -DestinationPath "." -Force
$mvn = ".\apache-maven-3.9.6\bin\mvn.cmd"
Write-Host "Using Maven to configure your MySQL database..."
& $mvn sql:execute
Write-Host "Starting Jetty Web Server..."
& $mvn clean jetty:run
