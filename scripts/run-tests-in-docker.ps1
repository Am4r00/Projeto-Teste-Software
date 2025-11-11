Param(
    [string]$ComposeFile = "src/test/docker-compose.ci.yml"
)

$cwd = Get-Location
Write-Host "Using compose file: $ComposeFile" -ForegroundColor Cyan

# Start compose and run until tests container exits
$proc = Start-Process -FilePath "docker" -ArgumentList "compose -f $ComposeFile up --build --abort-on-container-exit --exit-code-from tests" -NoNewWindow -Wait -PassThru
$exit = $proc.ExitCode

# Tear down and remove volumes
docker compose -f $ComposeFile down -v

if ($exit -ne 0) {
    Write-Host "Tests failed with exit code $exit" -ForegroundColor Red
    exit $exit
} else {
    Write-Host "Tests completed successfully" -ForegroundColor Green
    exit 0
}
