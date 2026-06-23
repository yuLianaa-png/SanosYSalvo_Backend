$ErrorActionPreference = "Stop"

$REGION = "us-east-1"
$ACCOUNT_ID = aws sts get-caller-identity --query Account --output text
$ECR = "$ACCOUNT_ID.dkr.ecr.$REGION.amazonaws.com"

Write-Host "AWS Account ID: $ACCOUNT_ID"
Write-Host "ECR Registry: $ECR"

Write-Host "Login en ECR..."
aws ecr get-login-password --region $REGION | docker login --username AWS --password-stdin $ECR

$services = @(
    @{ Name = "sanos-salvos-auth"; Path = "./auth-service" },
    @{ Name = "sanos-salvos-petserv"; Path = "./pet-service" },
    @{ Name = "sanos-salvos-match"; Path = "./match-service" },
    @{ Name = "sanos-salvos-notif"; Path = "./notification-service" },
    @{ Name = "sanos-salvos-nginx"; Path = "./nginx" }
)

foreach ($service in $services) {
    $imageName = $service.Name
    $path = $service.Path
    $remoteImage = "$ECR/$imageName:latest"

    Write-Host "Construyendo imagen $imageName..."
    docker build -t "$imageName:latest" $path

    Write-Host "Etiquetando imagen $imageName..."
    docker tag "$imageName:latest" $remoteImage

    Write-Host "Subiendo imagen $imageName a ECR..."
    docker push $remoteImage
}

Write-Host "Todas las imágenes fueron subidas correctamente a ECR."