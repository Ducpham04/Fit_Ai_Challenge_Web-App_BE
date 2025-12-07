# 🚀 Hướng dẫn Deploy lên AWS Lambda

## 📋 Tổng quan

Dự án đã được cấu hình để deploy lên AWS Lambda sử dụng:
- **Handler**: `com.example.fitchallenge.config.StreamLambdaHandler`
- **Runtime**: Java 17
- **Framework**: Spring Boot 3 với AWS Serverless Java Container

## 🔧 Chuẩn bị

### 1. Kiểm tra Handler Class

Handler class đã được tạo tại:
```
src/main/java/com/example/fitchallenge/config/StreamLambdaHandler.java
```

Class này implement `RequestStreamHandler` và sử dụng `SpringBootLambdaContainerHandler` để xử lý requests từ API Gateway.

### 2. Build JAR cho Lambda

**Sử dụng script (Khuyến nghị):**
```bash
./build-lambda.sh
```

**Hoặc build thủ công:**
```bash
mvn clean package -Plambda -DskipTests
```

**Kiểm tra JAR:**
```bash
./verify-lambda-jar.sh
```

File JAR sẽ được tạo tại: `target/FIT_Challenge-0.0.1-SNAPSHOT.jar`

### 3. Cấu hình Environment Variables

Tạo file `.env.lambda` hoặc cấu hình trực tiếp trên Lambda Console với các biến sau:

```bash
# Database Configuration
SPRING_DATASOURCE_URL=jdbc:mysql://your-rds-endpoint:3306/fit_challenge?useSSL=true&requireSSL=false&serverTimezone=UTC
SPRING_DATASOURCE_USERNAME=admin
SPRING_DATASOURCE_PASSWORD=your-password

# JWT Configuration
JWT_SECRET=your-jwt-secret-key-here
JWT_EXPIRATION=8640000

# CORS Configuration (comma-separated)
CORS_ALLOWED_ORIGINS=https://your-frontend-domain.com,https://www.your-frontend-domain.com

# JPA Configuration
SPRING_JPA_HIBERNATE_DDL_AUTO=update
SPRING_JPA_SHOW_SQL=false

# Upload Path (optional)
UPLOAD_PATH=uploads/
```

## 📤 Upload lên AWS Lambda

### Cách 1: Upload trực tiếp (JAR < 50MB)

1. Vào AWS Lambda Console
2. Chọn function hoặc tạo mới
3. Code → Upload from → Upload a .zip or .jar file
4. Chọn file: `target/FIT_Challenge-0.0.1-SNAPSHOT.jar`

### Cách 2: Upload lên S3 (Khuyến nghị cho JAR > 50MB)

**Sử dụng script:**
```bash
./upload-to-s3.sh your-bucket-name [s3-key]
```

**Hoặc upload thủ công:**
```bash
aws s3 cp target/FIT_Challenge-0.0.1-SNAPSHOT.jar s3://your-bucket-name/lambda/fit-challenge.jar
```

Sau đó trên Lambda Console:
1. Code → Upload from → Amazon S3 location
2. Nhập: `s3://your-bucket-name/lambda/fit-challenge.jar`

## ⚙️ Cấu hình Lambda Function

### Basic Settings

- **Handler**: `com.example.fitchallenge.config.StreamLambdaHandler`
- **Runtime**: `Java 17` hoặc `Java 21`
- **Architecture**: `x86_64`

### Memory & Timeout

- **Memory**: `1024 MB` (khuyến nghị tối thiểu)
- **Timeout**: `60 seconds` (khuyến nghị tối thiểu)
  - Có thể tăng lên `300 seconds` (5 phút) nếu cần

### Environment Variables

Thêm tất cả các biến môi trường từ phần "Cấu hình Environment Variables" ở trên.

### VPC Configuration (Nếu RDS trong VPC)

Nếu RDS database nằm trong VPC, Lambda cần:
1. **VPC**: Chọn VPC chứa RDS
2. **Subnets**: Chọn ít nhất 2 subnets (public hoặc private)
3. **Security Groups**: 
   - Cho phép outbound traffic đến RDS port (3306)
   - Cho phép outbound HTTPS (443) cho AWS services

**Lưu ý**: Lambda trong VPC sẽ có cold start lâu hơn và cần ENI (Elastic Network Interface).

### IAM Role Permissions

Lambda execution role cần các permissions:

```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": [
        "logs:CreateLogGroup",
        "logs:CreateLogStream",
        "logs:PutLogEvents"
      ],
      "Resource": "arn:aws:logs:*:*:*"
    },
    {
      "Effect": "Allow",
      "Action": [
        "ec2:CreateNetworkInterface",
        "ec2:DescribeNetworkInterfaces",
        "ec2:DeleteNetworkInterface"
      ],
      "Resource": "*"
    }
  ]
}
```

## 🌐 Cấu hình API Gateway

### Tạo API Gateway

1. Tạo REST API hoặc HTTP API
2. Tạo resource và method (GET, POST, PUT, DELETE, OPTIONS)
3. Integrate với Lambda function
4. Enable CORS nếu cần

### CORS Configuration

Nếu sử dụng API Gateway, có thể cấu hình CORS ở đây hoặc để Spring Boot xử lý.

**API Gateway CORS Headers:**
```
Access-Control-Allow-Origin: *
Access-Control-Allow-Headers: Content-Type,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token
Access-Control-Allow-Methods: GET,POST,PUT,DELETE,OPTIONS
```

### Custom Domain (Optional)

1. Tạo custom domain trong API Gateway
2. Cấu hình SSL certificate
3. Map domain đến API
4. Cập nhật CORS_ALLOWED_ORIGINS với domain mới

## 🔍 Testing

### Test từ Lambda Console

1. Vào Lambda function → Test
2. Tạo test event với format API Gateway:

```json
{
  "httpMethod": "GET",
  "path": "/api/challenges",
  "headers": {
    "Content-Type": "application/json"
  },
  "body": null
}
```

### Test từ API Gateway

1. Deploy API Gateway
2. Test từ API Gateway console
3. Hoặc test từ Postman/curl với API Gateway URL

### Test từ Frontend

Cập nhật FE API base URL:
```typescript
// src/api/client.ts
const API_BASE_URL = import.meta.env.VITE_API_URL || "https://your-api-gateway-url.execute-api.region.amazonaws.com/api";
```

## 🐛 Troubleshooting

### Lỗi: ClassNotFoundException: StreamLambdaHandler

**Nguyên nhân**: JAR không được build đúng với profile lambda

**Giải pháp**:
```bash
rm -rf target
mvn clean package -Plambda -DskipTests
./verify-lambda-jar.sh
```

### Lỗi: Cannot connect to database

**Nguyên nhân**: 
- Lambda không có quyền truy cập RDS
- RDS security group không cho phép Lambda
- Lambda không trong cùng VPC với RDS

**Giải pháp**:
1. Kiểm tra RDS security group cho phép Lambda security group
2. Nếu RDS trong VPC, cấu hình Lambda trong cùng VPC
3. Kiểm tra environment variables: SPRING_DATASOURCE_URL, USERNAME, PASSWORD

### Lỗi: CORS error từ frontend

**Nguyên nhân**: CORS không được cấu hình đúng

**Giải pháp**:
1. Kiểm tra CORS_ALLOWED_ORIGINS environment variable
2. Đảm bảo frontend URL được thêm vào allowed origins
3. Kiểm tra API Gateway CORS settings

### Cold Start quá lâu

**Nguyên nhân**: Spring Boot initialization mất thời gian

**Giải pháp**:
1. Tăng memory allocation (1024MB → 2048MB)
2. Sử dụng Lambda SnapStart (Java 17+)
3. Giảm số lượng dependencies nếu có thể
4. Sử dụng Provisioned Concurrency

### Out of Memory

**Nguyên nhân**: Memory không đủ

**Giải pháp**:
1. Tăng memory allocation
2. Kiểm tra CloudWatch logs để xem memory usage
3. Optimize code và dependencies

## 📊 Monitoring

### CloudWatch Logs

Lambda tự động ghi logs vào CloudWatch:
- Log Group: `/aws/lambda/your-function-name`
- Xem logs real-time: Lambda Console → Monitor → View CloudWatch logs

### CloudWatch Metrics

Monitor các metrics:
- Invocations
- Duration
- Errors
- Throttles
- Memory usage

### Alarms

Tạo CloudWatch alarms cho:
- Error rate > threshold
- Duration > threshold
- Memory usage > threshold

## 🔄 CI/CD (Optional)

### GitHub Actions Example

```yaml
name: Deploy to Lambda

on:
  push:
    branches: [main]

jobs:
  deploy:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - uses: actions/setup-java@v2
        with:
          java-version: '17'
      - name: Build JAR
        run: mvn clean package -Plambda -DskipTests
      - name: Upload to S3
        run: |
          aws s3 cp target/FIT_Challenge-0.0.1-SNAPSHOT.jar s3://your-bucket/lambda/fit-challenge.jar
      - name: Update Lambda
        run: |
          aws lambda update-function-code \
            --function-name your-function-name \
            --s3-bucket your-bucket \
            --s3-key lambda/fit-challenge.jar
```

## 📝 Checklist trước khi Deploy

- [ ] Build JAR với profile lambda: `mvn clean package -Plambda -DskipTests`
- [ ] Verify JAR: `./verify-lambda-jar.sh`
- [ ] Cấu hình tất cả environment variables
- [ ] Kiểm tra RDS security group cho phép Lambda
- [ ] Cấu hình VPC nếu cần
- [ ] Test Lambda function từ console
- [ ] Cấu hình API Gateway
- [ ] Test API từ Postman/curl
- [ ] Cập nhật FE API base URL
- [ ] Test end-to-end từ frontend
- [ ] Setup CloudWatch alarms
- [ ] Document API Gateway URL

## 📚 Tài liệu tham khảo

- [AWS Lambda Java Handler](https://docs.aws.amazon.com/lambda/latest/dg/java-handler.html)
- [AWS Serverless Java Container](https://github.com/awslabs/aws-serverless-java-container)
- [Spring Boot on AWS Lambda](https://spring.io/guides/gs/spring-boot-for-aws-lambda/)



