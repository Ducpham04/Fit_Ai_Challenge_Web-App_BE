#!/bin/bash

# Script để kiểm tra JAR có đúng cho Lambda không

JAR_FILE="target/FIT_Challenge-0.0.1-SNAPSHOT.jar"

echo "🔍 Kiểm tra JAR cho AWS Lambda..."
echo ""

if [ ! -f "$JAR_FILE" ]; then
    echo "❌ File JAR không tồn tại: $JAR_FILE"
    echo "   Hãy chạy: mvn clean package -Plambda -DskipTests"
    exit 1
fi

echo "✅ File JAR tồn tại: $JAR_FILE"
echo ""

# Kiểm tra StreamLambdaHandler
echo "📦 Kiểm tra StreamLambdaHandler..."
if jar tf "$JAR_FILE" | grep -q "com/example/fitchallenge/config/StreamLambdaHandler.class"; then
    echo "✅ StreamLambdaHandler.class có trong JAR"
else
    echo "❌ StreamLambdaHandler.class KHÔNG có trong JAR"
    exit 1
fi

# Kiểm tra BOOT-INF (không nên có)
echo ""
echo "📦 Kiểm tra cấu trúc BOOT-INF..."
if jar tf "$JAR_FILE" | grep -q "^BOOT-INF"; then
    echo "❌ JAR có cấu trúc BOOT-INF - KHÔNG phù hợp với Lambda"
    echo "   Hãy build lại với: mvn clean package -Plambda -DskipTests"
    exit 1
else
    echo "✅ JAR không có cấu trúc BOOT-INF - Phù hợp với Lambda"
fi

# Kiểm tra Main-Class trong MANIFEST
echo ""
echo "📦 Kiểm tra Main-Class trong MANIFEST..."
jar xf "$JAR_FILE" META-INF/MANIFEST.MF 2>/dev/null
if grep -q "Main-Class: com.example.fitchallenge.config.StreamLambdaHandler" META-INF/MANIFEST.MF 2>/dev/null; then
    echo "✅ Main-Class đúng trong MANIFEST"
    cat META-INF/MANIFEST.MF | grep Main-Class
else
    echo "❌ Main-Class không đúng trong MANIFEST"
    rm -rf META-INF
    exit 1
fi
rm -rf META-INF

# Kiểm tra kích thước
echo ""
echo "📦 Thông tin JAR:"
ls -lh "$JAR_FILE" | awk '{print "   Kích thước: " $5}'

echo ""
echo "✅ JAR đã sẵn sàng để upload lên AWS Lambda!"
echo ""
echo "📝 Cấu hình trên Lambda:"
echo "   Handler: com.example.fitchallenge.config.StreamLambdaHandler"
echo "   Runtime: Java 17"
echo "   Memory: 1024MB (khuyến nghị)"
echo "   Timeout: 60 giây (khuyến nghị)"




