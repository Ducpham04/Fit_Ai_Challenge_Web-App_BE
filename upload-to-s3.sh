#!/bin/bash

# Script để upload JAR lên S3 cho AWS Lambda
# Sử dụng: ./upload-to-s3.sh <bucket-name> [s3-key]

JAR_FILE="target/FIT_Challenge-0.0.1-SNAPSHOT.jar"
BUCKET_NAME=$1
S3_KEY=${2:-"lambda/fit-challenge.jar"}

if [ -z "$BUCKET_NAME" ]; then
    echo "❌ Thiếu bucket name!"
    echo ""
    echo "Cách sử dụng:"
    echo "  ./upload-to-s3.sh <bucket-name> [s3-key]"
    echo ""
    echo "Ví dụ:"
    echo "  ./upload-to-s3.sh my-lambda-bucket"
    echo "  ./upload-to-s3.sh my-lambda-bucket lambda/fit-challenge.jar"
    exit 1
fi

# Kiểm tra JAR
if [ ! -f "$JAR_FILE" ]; then
    echo "❌ File JAR không tồn tại: $JAR_FILE"
    echo "   Hãy chạy: ./build-lambda.sh hoặc mvn clean package -Plambda -DskipTests"
    exit 1
fi

# Kiểm tra JAR có đúng không
echo "🔍 Kiểm tra JAR..."
if jar tf "$JAR_FILE" | grep -q "^BOOT-INF"; then
    echo "❌ JAR có cấu trúc BOOT-INF - KHÔNG phù hợp với Lambda"
    echo "   Hãy build lại với: mvn clean package -Plambda -DskipTests"
    exit 1
fi

if ! jar tf "$JAR_FILE" | grep -q "com/example/fitchallenge/config/StreamLambdaHandler.class"; then
    echo "❌ StreamLambdaHandler.class không có trong JAR"
    echo "   Hãy build lại với: mvn clean package -Plambda -DskipTests"
    exit 1
fi

echo "✅ JAR đã được kiểm tra và đúng"
echo ""

# Upload lên S3
echo "📤 Uploading JAR lên S3..."
echo "   Bucket: $BUCKET_NAME"
echo "   Key: $S3_KEY"
echo "   File: $JAR_FILE"
echo ""

aws s3 cp "$JAR_FILE" "s3://$BUCKET_NAME/$S3_KEY"

if [ $? -eq 0 ]; then
    echo ""
    echo "✅ Upload thành công!"
    echo ""
    echo "📝 S3 URI: s3://$BUCKET_NAME/$S3_KEY"
    echo ""
    echo "📝 Cấu hình trên Lambda Console:"
    echo "   1. Vào Lambda function → Code → Upload from"
    echo "   2. Chọn 'Amazon S3 location'"
    echo "   3. Nhập S3 link: s3://$BUCKET_NAME/$S3_KEY"
    echo "   4. Handler: com.example.fitchallenge.config.StreamLambdaHandler"
    echo "   5. Runtime: Java 17"
    echo "   6. Memory: 1024MB (khuyến nghị)"
    echo "   7. Timeout: 60 giây (khuyến nghị)"
else
    echo ""
    echo "❌ Upload thất bại!"
    echo "   Kiểm tra lại AWS credentials và bucket permissions"
    exit 1
fi





