.PHONY: dev up down restart logs clean rebuild

# Start development environment
dev:
@echo "🚀 Starting Fit AI Challenge - Development Mode..."
docker-compose up -d --build
@echo "✅ Started! Waiting for app to be ready..."
@sleep 10
@echo "📱 App: http://localhost:8080"
@echo "🗄️  Adminer: http://localhost:8081"
@echo "🔍 Debug port: 5005"
@echo "📝 View logs: make logs"

# Start without rebuild
up:
docker-compose up -d

# Stop all containers
down:
docker-compose down

# Restart ONLY app (when you add new entity)
restart:
@echo "🔄 Restarting app..."
docker-compose restart app
@echo "✅ Done! Check logs: make logs"

# View app logs
logs:
docker-compose logs -f app

# View all logs
logs-all:
docker-compose logs -f

# Clean everything
clean:
@echo "🧹 Cleaning..."
docker-compose down -v
docker system prune -f
@echo "✅ Cleaned!"

# Rebuild only when changing dependencies
rebuild:
@echo "🔨 Rebuilding app..."
docker-compose build --no-cache app
docker-compose up -d app
@echo "✅ Done!"

# Database commands
db-reset:
docker-compose down db
docker volume rm fit_ai_challenge_web-app_be_db_data
docker-compose up -d db

# Check status
status:
docker-compose ps

## 🚀 Build cho AWS Lambda

### Vấn đề
Spring Boot Maven Plugin tạo ra JAR với cấu trúc `BOOT-INF/` mà AWS Lambda không thể đọc trực tiếp. Lambda cần một JAR với cấu trúc phẳng (flat structure) để có thể tìm thấy class `StreamLambdaHandler`.

### Giải pháp
Dự án đã được cấu hình với Maven profile `lambda` để tạo uber JAR phù hợp với Lambda.

### Cách build cho Lambda:

**Cách 1: Sử dụng script (Khuyến nghị)**
```bash
./build-lambda.sh
# Script sẽ build và tạo file: target/fit-challenge-lambda.jar
```

**Cách 2: Build thủ công**
```bash
# Build JAR cho Lambda (sử dụng profile lambda)
mvn clean package -Plambda -DskipTests

# File JAR sẽ được tạo tại:
# target/FIT_Challenge-0.0.1-SNAPSHOT.jar
```

**Sau khi build, kiểm tra JAR:**
```bash
./verify-lambda-jar.sh
```

### Upload JAR lên AWS:

**Cách 1: Upload trực tiếp (nếu JAR < 50MB)**
- Vào Lambda Console → Code → Upload from → Upload a .zip or .jar file
- Chọn file: `target/FIT_Challenge-0.0.1-SNAPSHOT.jar`

**Cách 2: Upload lên S3 (khuyến nghị cho JAR > 50MB)**
```bash
# Sử dụng script tự động
./upload-to-s3.sh <bucket-name> [s3-key]

# Hoặc upload thủ công
aws s3 cp target/FIT_Challenge-0.0.1-SNAPSHOT.jar s3://<bucket-name>/lambda/fit-challenge.jar
```

Sau khi upload lên S3:
1. Vào Lambda Console → Code → Upload from → Amazon S3 location
2. Nhập S3 link: `s3://<bucket-name>/lambda/fit-challenge.jar`

### Cấu hình Lambda trên AWS:

1. **Handler**: `com.example.fitchallenge.config.StreamLambdaHandler`
   - Lambda sẽ tự động gọi method `handleRequest` vì class implement `RequestStreamHandler`
   - **QUAN TRỌNG**: Đảm bảo bạn upload đúng JAR file: `target/FIT_Challenge-0.0.1-SNAPSHOT.jar` (file này được tạo sau khi build với profile lambda)
   - **KHÔNG** upload file `original-FIT_Challenge-0.0.1-SNAPSHOT.jar` (file này có thể có cấu trúc BOOT-INF)
2. **Runtime**: Java 17 (hoặc Java 21)
3. **Memory**: Tối thiểu 512MB (khuyến nghị 1024MB cho Spring Boot)
4. **Timeout**: Tối thiểu 30 giây (khuyến nghị 60 giây cho cold start)
5. **Environment Variables**: Cấu hình các biến môi trường cần thiết (database URL, JWT secret, etc.)

### Kiểm tra JAR trước khi upload:

```bash
# Kiểm tra xem StreamLambdaHandler có trong JAR không
jar tf target/FIT_Challenge-0.0.1-SNAPSHOT.jar | grep StreamLambdaHandler

# Kiểm tra xem JAR có cấu trúc BOOT-INF không (không nên có)
jar tf target/FIT_Challenge-0.0.1-SNAPSHOT.jar | grep BOOT-INF

# Kiểm tra Main-Class trong MANIFEST
jar xf target/FIT_Challenge-0.0.1-SNAPSHOT.jar META-INF/MANIFEST.MF && cat META-INF/MANIFEST.MF && rm -rf META-INF
```

### Troubleshooting:

Nếu gặp lỗi `ClassNotFoundException: com.example.fitchallenge.config.StreamLambdaHandler`:

1. **Đảm bảo build với profile lambda**:
   ```bash
   rm -rf target
   mvn clean package -Plambda -DskipTests
   # Hoặc sử dụng script
   ./build-lambda.sh
   ```

2. **Kiểm tra JAR đúng trước khi upload**:
   ```bash
   ./verify-lambda-jar.sh
   ```
   - File phải là: `target/FIT_Challenge-0.0.1-SNAPSHOT.jar` hoặc `target/fit-challenge-lambda.jar`
   - File này phải có `StreamLambdaHandler.class` ở root (không có BOOT-INF)
   - Kích thước thường khoảng 60-70MB

3. **Kiểm tra Handler name trên Lambda Console**:
   - Phải chính xác: `com.example.fitchallenge.config.StreamLambdaHandler`
   - **KHÔNG** có method name (không phải `::handleRequest`)
   - **KHÔNG** có dấu cách hoặc ký tự đặc biệt

4. **Kiểm tra Runtime trên Lambda Console**:
   - Phải là: `Java 17` hoặc `Java 21`
   - **KHÔNG** dùng Java 8 hoặc Java 11

5. **Đảm bảo upload đúng file**:
   - **QUAN TRỌNG**: Phải build lại với profile lambda trước khi upload:
     ```bash
     rm -rf target
     mvn clean package -Plambda -DskipTests
     ./verify-lambda-jar.sh  # Kiểm tra JAR đúng
     ```
   - Upload file: `target/FIT_Challenge-0.0.1-SNAPSHOT.jar` (sau khi build với profile lambda)
   - **KHÔNG** upload: `target/original-FIT_Challenge-0.0.1-SNAPSHOT.jar`
   - **KHÔNG** upload JAR được build không có profile lambda
   - Nếu upload lên S3, đảm bảo S3 link đúng và Lambda có quyền truy cập S3 bucket

6. **Nếu vẫn lỗi, kiểm tra CloudWatch Logs**:
   - Vào CloudWatch → Log groups → tìm log group của Lambda function
   - Xem log chi tiết để biết lỗi cụ thể
   - Kiểm tra xem có lỗi khác ngoài ClassNotFoundException không

7. **Thử xóa và tạo lại Lambda function**:
   - Đôi khi Lambda cache có thể gây vấn đề
   - Xóa function và tạo lại với JAR mới

### Lưu ý:
- JAR được tạo với profile `lambda` sẽ có tất cả dependencies được giải nén vào root, không có cấu trúc BOOT-INF
- Class `StreamLambdaHandler` sẽ có thể được tìm thấy ở root classpath
- Spring Boot repackage được tắt trong profile lambda để tránh tạo cấu trúc BOOT-INF

### Build cho local development (không dùng profile):
```bash
mvn clean package
# Tạo JAR với cấu trúc Spring Boot chuẩn (có BOOT-INF)
```

## 🧪 Postman Testing - User Profile APIs

**Các endpoint mới:**
- `GET /api/v1/users/{userId}/profile` → Trả về `UserProfileDTO`
- `GET /api/v1/users/{userId}/profile/full` → Trả về `FullUserProfileDTO`

**Cách test với Postman:**
1. Import môi trường chứa `baseUrl` (ví dụ: `http://localhost:8080`)
2. Tạo request mới:
   - Method: `GET`
   - URL: `{{baseUrl}}/api/v1/users/1/profile`
3. Nếu cần token (khi security bật), thêm header:
   - `Authorization: Bearer <JWT_TOKEN>`
4. Gửi request và kiểm tra JSON response:
   ```json
   {
     "id": 1,
     "email": "user@example.com",
     "username": "User Name",
     "avatar": "https://cdn.fitnit.ai/avatar.png",
     "joinDate": "2025-12-03T00:00:00+07:00",
     "currentStreak": 4
   }
   ```

**Collection gợi ý:**
```json
{
  "info": {
    "name": "Fit Challenge - User Profile",
    "_postman_id": "auto-generated",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "item": [
    {
      "name": "Get User Profile",
      "request": {
        "method": "GET",
        "header": [
          {
            "key": "Authorization",
            "value": "Bearer {{token}}",
            "type": "text",
            "disabled": true
          }
        ],
        "url": {
          "raw": "{{baseUrl}}/api/v1/users/{{userId}}/profile",
          "host": ["{{baseUrl}}"],
          "path": ["api", "v1", "users", "{{userId}}", "profile"]
        }
      }
    },
    {
      "name": "Get Full User Profile",
      "request": {
        "method": "GET",
        "header": [
          {
            "key": "Authorization",
            "value": "Bearer {{token}}",
            "type": "text",
            "disabled": true
          }
        ],
        "url": {
          "raw": "{{baseUrl}}/api/v1/users/{{userId}}/profile/full",
          "host": ["{{baseUrl}}"],
          "path": ["api", "v1", "users", "{{userId}}", "profile", "full"]
        }
      }
    }
  ]
}
```

> Gợi ý: lưu JSON trên vào file `postman/user-profile-collection.json` để import nhanh vào Postman.