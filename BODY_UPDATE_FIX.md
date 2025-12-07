# Fix cho vấn đề cập nhật Body

## Vấn đề có thể gặp:

1. **Database schema thiếu columns mới:**
   - `information_body_user` table cần có các columns:
     - `activity_level` VARCHAR(50)
     - `bmr` DECIMAL(8,2)
     - `recommended_calories` DECIMAL(8,0)

2. **SQL Migration cần chạy:**
```sql
ALTER TABLE information_body_user 
ADD COLUMN IF NOT EXISTS activity_level VARCHAR(50),
ADD COLUMN IF NOT EXISTS bmr DECIMAL(8,2),
ADD COLUMN IF NOT EXISTS recommended_calories DECIMAL(8,0);
```

3. **Kiểm tra logs:**
   - Xem console backend để biết lỗi cụ thể
   - Kiểm tra response từ API trong Network tab của browser

4. **Các endpoint cần authentication:**
   - `/api/user/info` - GET, PUT
   - `/api/user/body-metric` - POST, GET
   - Đảm bảo JWT token được gửi trong header: `Authorization: Bearer <token>`

## Cách test:

1. **Test API trực tiếp với Postman:**
   ```
   GET /api/user/info
   Headers: Authorization: Bearer <your-token>
   
   PUT /api/user/info
   Headers: Authorization: Bearer <your-token>
   Body: {
     "weightKg": 70.5,
     "heightCm": 175.0,
     "age": 25,
     "gender": "male",
     "activityLevel": "moderately active"
   }
   ```

2. **Kiểm tra frontend console:**
   - Mở DevTools > Console
   - Xem error message chi tiết
   - Kiểm tra Network tab để xem request/response


