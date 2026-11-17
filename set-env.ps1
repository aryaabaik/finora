# =============================================================
# Finora - Script Set Environment Variables Google OAuth2
# Jalankan: .\set-env.ps1 -ClientId "ID_ANDA" -ClientSecret "SECRET_ANDA"
# =============================================================

param(
    [Parameter(Mandatory=$true)]
    [string]$ClientId,

    [Parameter(Mandatory=$true)]
    [string]$ClientSecret,

    [switch]$Permanent  # -Permanent => simpan ke registry User (permanen)
)

if ($ClientId -notmatch '\.apps\.googleusercontent\.com$') {
    Write-Warning "PERINGATAN: Client ID tidak terlihat seperti Google OAuth Client ID yang valid."
    Write-Warning "Format seharusnya: xxxxx.apps.googleusercontent.com"
    $confirm = Read-Host "Lanjutkan? (y/n)"
    if ($confirm -ne 'y') { exit 1 }
}

if ($Permanent) {
    [System.Environment]::SetEnvironmentVariable("GOOGLE_CLIENT_ID", $ClientId, "User")
    [System.Environment]::SetEnvironmentVariable("GOOGLE_CLIENT_SECRET", $ClientSecret, "User")
    Write-Host "✅ GOOGLE_CLIENT_ID dan GOOGLE_CLIENT_SECRET disimpan PERMANEN (User registry)." -ForegroundColor Green
    Write-Host "   Restart terminal/IDE agar terbaca oleh Maven/Spring Boot." -ForegroundColor Yellow
} else {
    # Set hanya untuk sesi PowerShell ini
    $env:GOOGLE_CLIENT_ID = $ClientId
    $env:GOOGLE_CLIENT_SECRET = $ClientSecret
    Write-Host "✅ GOOGLE_CLIENT_ID dan GOOGLE_CLIENT_SECRET diset untuk sesi ini." -ForegroundColor Green
    Write-Host "   Jalankan aplikasi dari terminal ini: .\mvnw.cmd spring-boot:run" -ForegroundColor Cyan
}

Write-Host ""
Write-Host "Client ID prefix: $($ClientId.Substring(0, [Math]::Min(20, $ClientId.Length)))..." -ForegroundColor DarkGray
Write-Host "Secret: [TERSEMBUNYI - $($ClientSecret.Length) karakter]" -ForegroundColor DarkGray
