#!/usr/bin/env bash

set -euo pipefail

AWS_REGION="${AWS_REGION:-us-east-1}"
SQS_QUEUE_NAME="${SQS_QUEUE_NAME:-match-notifications-queue}"
LAMBDA_FUNCTION_NAME="${LAMBDA_FUNCTION_NAME:-process-match-notification}"
LAMBDA_SOURCE_DIR="${LAMBDA_SOURCE_DIR:-lambda/process-match-notification}"

if [[ -z "${API_BASE_URL:-}" ]]; then
  echo "ERROR: Missing API_BASE_URL environment variable"
  exit 1
fi

if [[ -z "${INTERNAL_SERVICE_SECRET:-}" ]]; then
  echo "ERROR: Missing INTERNAL_SERVICE_SECRET environment variable"
  exit 1
fi

echo "Configuring AWS cloud resources..."
echo "Region: $AWS_REGION"
echo "SQS queue: $SQS_QUEUE_NAME"
echo "Lambda function: $LAMBDA_FUNCTION_NAME"

echo "Checking SQS queue..."

QUEUE_URL=$(aws sqs get-queue-url \
  --queue-name "$SQS_QUEUE_NAME" \
  --region "$AWS_REGION" \
  --query "QueueUrl" \
  --output text 2>/dev/null || true)

if [[ -z "$QUEUE_URL" || "$QUEUE_URL" == "None" ]]; then
  echo "SQS queue does not exist. Creating queue..."

  QUEUE_URL=$(aws sqs create-queue \
    --queue-name "$SQS_QUEUE_NAME" \
    --region "$AWS_REGION" \
    --query "QueueUrl" \
    --output text)
else
  echo "SQS queue already exists."
fi

QUEUE_ARN=$(aws sqs get-queue-attributes \
  --queue-url "$QUEUE_URL" \
  --attribute-names QueueArn \
  --region "$AWS_REGION" \
  --query "Attributes.QueueArn" \
  --output text)

echo "SQS queue URL: $QUEUE_URL"
echo "SQS queue ARN: $QUEUE_ARN"

echo "Checking Lambda function..."

if ! aws lambda get-function \
  --function-name "$LAMBDA_FUNCTION_NAME" \
  --region "$AWS_REGION" >/dev/null 2>&1; then

  echo "ERROR: Lambda function '$LAMBDA_FUNCTION_NAME' does not exist."
  echo "Create the Lambda function once in AWS Academy, then run this script again."
  exit 1
fi

echo "Lambda function exists."

echo "Packaging Lambda source code..."

TMP_ZIP="$(mktemp -t lambda-package.XXXXXX.zip)"

(
  cd "$LAMBDA_SOURCE_DIR"
  zip -q "$TMP_ZIP" lambda_function.py
)

echo "Updating Lambda code..."

aws lambda update-function-code \
  --function-name "$LAMBDA_FUNCTION_NAME" \
  --zip-file "fileb://$TMP_ZIP" \
  --region "$AWS_REGION" >/dev/null

aws lambda wait function-updated \
  --function-name "$LAMBDA_FUNCTION_NAME" \
  --region "$AWS_REGION"

echo "Updating Lambda environment variables..."

aws lambda update-function-configuration \
  --function-name "$LAMBDA_FUNCTION_NAME" \
  --region "$AWS_REGION" \
  --environment "Variables={API_BASE_URL=$API_BASE_URL,INTERNAL_SERVICE_SECRET=$INTERNAL_SERVICE_SECRET}" >/dev/null

aws lambda wait function-updated \
  --function-name "$LAMBDA_FUNCTION_NAME" \
  --region "$AWS_REGION"

echo "Checking SQS trigger for Lambda..."

MAPPING_UUID=$(aws lambda list-event-source-mappings \
  --function-name "$LAMBDA_FUNCTION_NAME" \
  --event-source-arn "$QUEUE_ARN" \
  --region "$AWS_REGION" \
  --query "EventSourceMappings[0].UUID" \
  --output text 2>/dev/null || true)

if [[ -z "$MAPPING_UUID" || "$MAPPING_UUID" == "None" ]]; then
  echo "SQS trigger does not exist. Creating trigger..."

  aws lambda create-event-source-mapping \
    --function-name "$LAMBDA_FUNCTION_NAME" \
    --event-source-arn "$QUEUE_ARN" \
    --batch-size 10 \
    --enabled \
    --region "$AWS_REGION" >/dev/null
else
  echo "SQS trigger already exists. Updating trigger..."

  aws lambda update-event-source-mapping \
    --uuid "$MAPPING_UUID" \
    --batch-size 10 \
    --enabled \
    --region "$AWS_REGION" >/dev/null
fi

echo "Cloud resources configured successfully."