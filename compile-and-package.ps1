param(
    [switch]$Clean,
    [switch]$OnlyPackage
)

$projectRoot = "."
$srcDir = "frontend\src"
$buildDir = "build"
$classesDir = "$buildDir\classes"
$filesDir = "files"
$jarName = "blockbuster-sistema.jar"
$jarExe = "C:\Program Files\Android\Android Studio\jbr\bin\jar.exe"

Write-Host "=== Blockbuster Sistema - Compilacion y Empaquetado (Fat JAR) ===" -ForegroundColor Cyan

if ($Clean) {
    Write-Host "[1/5] Limpiando directorios..." -ForegroundColor Yellow
    if (Test-Path $buildDir) {
        Remove-Item -Path $buildDir -Recurse -Force
        Write-Host "OK - Directorio eliminado" -ForegroundColor Green
    }
}

Write-Host "[1/5] Preparando estructura de directorios..." -ForegroundColor Yellow
if (-not (Test-Path $classesDir)) {
    New-Item -Path $classesDir -ItemType Directory -Force | Out-Null
}

if (-not $OnlyPackage) {
    Write-Host "[2/5] Compilando codigo Java..." -ForegroundColor Yellow
    
    $javaFiles = @(
        "$srcDir\controller\*.java",
        "$srcDir\dao\*.java",
        "$srcDir\model\*.java",
        "$srcDir\service\*.java",
        "$srcDir\view\*.java"
    )
    
    $allJavaFiles = @()
    foreach ($pattern in $javaFiles) {
        $files = Get-ChildItem -Path $pattern -ErrorAction SilentlyContinue
        if ($files) {
            $allJavaFiles += $files.FullName
        }
    }
    
    if ($allJavaFiles.Count -eq 0) {
        Write-Host "ERROR - No se encontraron archivos Java" -ForegroundColor Red
        exit 1
    }
    
    Write-Host "Compilando $($allJavaFiles.Count) archivos..."
    
    & javac -d $classesDir -cp "$filesDir\mysql-connector-j-9.3.0.jar" $allJavaFiles 2>&1
    
    if ($LASTEXITCODE -ne 0) {
        Write-Host "ERROR - Compilacion fallida" -ForegroundColor Red
        exit 1
    }
    
    Write-Host "OK - Compilacion completada" -ForegroundColor Green
}

Write-Host "[2/5] Empaquetando MySQL Connector dentro del JAR..." -ForegroundColor Yellow

$tempExtractDir = "$buildDir\temp-mysql"
if (Test-Path $tempExtractDir) {
    Remove-Item -Path $tempExtractDir -Recurse -Force
}
New-Item -Path $tempExtractDir -ItemType Directory -Force | Out-Null

# Extraer mysql-connector.jar
$currentDir = Get-Location
try {
    Set-Location -Path $tempExtractDir
    & $jarExe -xf "..\..\$filesDir\mysql-connector-j-9.3.0.jar"
    if ($LASTEXITCODE -ne 0) {
        Set-Location -Path $currentDir
        Write-Host "ERROR - No se pudo extraer mysql-connector.jar" -ForegroundColor Red
        exit 1
    }
} finally {
    Set-Location -Path $currentDir
}

# Copiar contenido extraído a build/classes (pero no el MANIFEST)
$comSourceDir = "$tempExtractDir\com"
$comDestDir = "$classesDir\com"
if (Test-Path $comSourceDir) {
    Copy-Item -Path $comSourceDir -Destination $comDestDir -Recurse -Force
    Write-Host "OK - Clases de MySQL Connector copiadas" -ForegroundColor Green
} else {
    Write-Host "ADVERTENCIA - No se encontro directorio 'com' en mysql-connector" -ForegroundColor Yellow
}

# Copiar services
$servicesSourceDir = "$tempExtractDir\META-INF\services"
$servicesDestDir = "$classesDir\META-INF\services"
if (Test-Path $servicesSourceDir) {
    New-Item -Path $servicesDestDir -ItemType Directory -Force | Out-Null
    Copy-Item -Path "$servicesSourceDir\*" -Destination $servicesDestDir -Force
    Write-Host "OK - Servicios de MySQL Connector copiados" -ForegroundColor Green
}

# Limpiar directorio temporal
Remove-Item -Path $tempExtractDir -Recurse -Force

Write-Host "[3/5] Copiando recursos (imagenes)..." -ForegroundColor Yellow

$srcImagesDir = "$srcDir\images"
$destImagesDir = "$classesDir\frontend\src\images"

if (Test-Path $srcImagesDir) {
    if (-not (Test-Path $destImagesDir)) {
        New-Item -Path $destImagesDir -ItemType Directory -Force | Out-Null
    }
    
    Copy-Item -Path "$srcImagesDir\*" -Destination $destImagesDir -Force
    
    $imageCount = (Get-ChildItem -Path $destImagesDir).Count
    Write-Host "OK - $imageCount imagenes copiadas" -ForegroundColor Green
} else {
    Write-Host "ADVERTENCIA - No se encontro directorio de imagenes" -ForegroundColor Yellow
}

Write-Host "[4/5] Generando archivo JAR (Fat JAR sin dependencias externas)..." -ForegroundColor Yellow

$manifestDir = "$buildDir\manifest"
if (-not (Test-Path $manifestDir)) {
    New-Item -Path $manifestDir -ItemType Directory -Force | Out-Null
}

# IMPORTANTE: Sin Class-Path porque todo está empaquetado dentro del JAR
# Usar encoding ASCII para evitar BOM que causa errores en jar.exe
$manifestContent = "Manifest-Version: 1.0`nMain-Class: frontend.src.controller.Main`n`n"

$manifestFile = "$manifestDir\MANIFEST.MF"
[System.IO.File]::WriteAllText($manifestFile, $manifestContent, [System.Text.Encoding]::ASCII)

$jarPath = "$buildDir\$jarName"
Push-Location -Path $classesDir
& $jarExe -cfm "$jarPath" "..\..\$manifestFile" .
Pop-Location

if ($LASTEXITCODE -ne 0) {
    Write-Host "ERROR - No se pudo crear el JAR" -ForegroundColor Red
    exit 1
}

Write-Host "OK - JAR generado" -ForegroundColor Green

Write-Host ""
Write-Host "[5/5] Verificando contenido del JAR..." -ForegroundColor Cyan
$jarContents = & $jarExe -tf $jarPath 2>&1

$hasImages = $jarContents | Select-String -Pattern "images/.*\.(png|jpg|jpeg)" | Measure-Object | Select-Object -ExpandProperty Count
$hasClasses = $jarContents | Select-String -Pattern "Main\.class" | Measure-Object | Select-Object -ExpandProperty Count
$hasMysql = $jarContents | Select-String -Pattern "com/mysql/cj/jdbc/Driver\.class" | Measure-Object | Select-Object -ExpandProperty Count

Write-Host ""
Write-Host "Contenido verificado:"
if ($hasClasses -gt 0) {
    Write-Host "  [OK] Clases de la aplicacion incluidas" -ForegroundColor Green
} else {
    Write-Host "  [ERROR] Clases NO encontradas" -ForegroundColor Red
}

if ($hasImages -gt 0) {
    Write-Host "  [OK] $hasImages imagenes incluidas en el JAR" -ForegroundColor Green
} else {
    Write-Host "  [ERROR] No se encontraron imagenes en el JAR" -ForegroundColor Red
}

if ($hasMysql -gt 0) {
    Write-Host "  [OK] MySQL Connector (com.mysql.cj.jdbc.Driver) incluido" -ForegroundColor Green
} else {
    Write-Host "  [ERROR] MySQL Connector NO encontrado en el JAR" -ForegroundColor Red
}

$jarSize = (Get-Item $jarPath).Length / 1MB
Write-Host ""
Write-Host "=== Compilacion completada ===" -ForegroundColor Cyan
Write-Host "Archivo JAR: $jarPath" -ForegroundColor Cyan
Write-Host "Tamaño: $([math]::Round($jarSize, 2)) MB" -ForegroundColor Cyan
Write-Host "Comando: java -jar $jarPath" -ForegroundColor Cyan
Write-Host ""
Write-Host "NOTA: Este JAR es completamente portable." -ForegroundColor Green
Write-Host "      No requiere dependencias externas." -ForegroundColor Green
Write-Host "      Puede ejecutarse en cualquier computadora con Java instalado." -ForegroundColor Green
