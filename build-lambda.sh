#!/bin/bash

# Script để build JAR cho AWS Lambda

echo "🔨 Building JAR for AWS Lambda..."
echo ""

# Clean và build
mvn clean package -Plambda -DskipTests

if [ $? -ne 0 ]; then
    echo "❌ Build failed!"
    exit 1
fi

echo ""
echo "✅ Build successful!"
echo ""

# Kiểm tra JAR
JAR_FILE="target/FIT_Challenge-0.0.1-SNAPSHOT.jar"

if [ ! -f "$JAR_FILE" ]; then
    echo "❌ JAR file not found: $JAR_FILE"
    exit 1
fi

# Tạo bản sao với tên rõ ràng hơn
LAMBDA_JAR="target/fit-challenge-lambda.jar"
cp "$JAR_FILE" "$LAMBDA_JAR"

echo "📦 JAR files created:"
echo "   - $JAR_FILE (original)"
echo "   - $LAMBDA_JAR (for Lambda upload)"
echo ""

# Kiểm tra JAR
echo "🔍 Verifying JAR..."
if jar tf "$LAMBDA_JAR" | grep -q "com/example/fitchallenge/config/StreamLambdaHandler.class"; then
    echo "✅ StreamLambdaHandler.class found in JAR"
else
    echo "❌ StreamLambdaHandler.class NOT found in JAR"
    exit 1
fi

if jar tf "$LAMBDA_JAR" | grep -q "^BOOT-INF"; then
    echo "❌ JAR contains BOOT-INF structure - NOT suitable for Lambda"
    exit 1
else
    echo "✅ JAR does NOT contain BOOT-INF structure - Suitable for Lambda"
fi

echo ""
echo "📝 File to upload to Lambda:"
echo "   $LAMBDA_JAR"
echo ""
echo "📝 Lambda Configuration:"
echo "   Handler: com.example.fitchallenge.config.StreamLambdaHandler"
echo "   Runtime: Java 17"
echo "   Memory: 1024MB (recommended)"
echo "   Timeout: 60 seconds (recommended)"
echo ""



