# Kế hoạch điều chỉnh API theo yêu cầu Frontend

## Phân tích sự khác biệt giữa FE requirements và BE hiện tại

### 1. Authentication APIs

#### 1.1 Login Response
**FE cần:**
```json
{
  "token": "string",
  "refreshToken": "string",
  "user": {
    "id": "string",
    "email": "string",
    "fullName": "string",
    "role": "string"
  }
}
```

**BE hiện tại:**
```json
{
  "token": "string",
  "type": "Bearer"
}
```

**Cần thay đổi:**
- ✅ Thêm `refreshToken` vào JwtResponse
- ✅ Thêm `user` object vào JwtResponse
- ✅ Implement refresh token mechanism trong JwtTokenProvider

#### 1.2 Register Response
**FE cần:** Same as login response
**BE hiện tại:** Chỉ trả về String message
**Cần thay đổi:**
- ✅ Trả về JwtResponse với token, refreshToken, user object

#### 1.3 Get Profile (`GET /auth/user`)
**FE cần:**
```json
{
  "id": "string",
  "email": "string",
  "fullName": "string",
  "role": "string",
  "status": "active|inactive|banned",
  "createdAt": "string",
  "updatedAt": "string",
  "lastLoginAt": "string",
  "profileImage": "string"
}
```

**BE hiện tại:** UserDTO thiếu `updatedAt`, `lastLoginAt`, `profileImage`
**Cần thay đổi:**
- ✅ Thêm fields vào User entity: `updatedAt`, `lastLoginAt`
- ✅ Đổi `linkImage` → `profileImage` trong DTO (hoặc map cả hai)
- ✅ Cập nhật UserDTO

### 2. Challenge APIs

#### 2.1 Get All Challenges
**FE cần:**
- `video`: array of strings (hiện tại là `linkVideos` string)
- `participants`: number
- `reward`: string
- Pagination support

**BE hiện tại:**
- `linkVideos`: string (single video)
- Không có participants count
- Không có reward field

**Cần thay đổi:**
- ✅ Thêm method tính participants count từ UserChallenge
- ✅ Thêm reward field vào Challenges entity (hoặc tính từ related data)
- ✅ Hỗ trợ video array (split linkVideos hoặc thêm field mới)

#### 2.2 Get Challenge by ID
**FE cần:**
- `participants`: array với userId, userName, joinedAt, progress, completed

**BE hiện tại:** Chưa có endpoint này với participants list
**Cần thay đổi:**
- ✅ Tạo ChallengeDetailDTO với participants array
- ✅ Query UserChallenge để lấy participants list

### 3. Training Plan APIs

#### 3.1 Get All Training Plans
**FE cần:**
- `exercises`: array với id, name, sets, reps, duration, restTime, instructions, videoUrl

**BE hiện tại:**
- TrainingPlanDetail có: sets, reps, challenge reference
- Thiếu: duration, restTime, instructions, videoUrl

**Cần thay đổi:**
- ✅ Thêm fields vào TrainingPlanDetail: duration, restTime, instructions
- ✅ Lấy videoUrl từ Challenges entity
- ✅ Tạo ExerciseDTO cho response

### 4. User Management APIs

#### 4.1 Get All Users (Admin)
**FE cần:**
- Pagination support
- `lastLoginAt`, `updatedAt`, `profileImage`

**BE hiện tại:**
- Không có pagination
- Thiếu `lastLoginAt`, `updatedAt`

**Cần thay đổi:**
- ✅ Thêm pagination vào UserService
- ✅ Cập nhật User entity và DTO

### 5. Các API khác

#### 5.1 Reward Management
**FE cần:**
- `status`: "active|inactive"
- `expiresAt`: string

**BE hiện tại:**
- Có `expireAt` (Date)
- Không có `status` field

**Cần thay đổi:**
- ✅ Thêm status field vào Reward entity
- ✅ Đảm bảo expiresAt format đúng

#### 5.2 Meal Management
**FE cần:**
- `foods`: array với mfId, foodId, foodName, quantityG, totalCalories, totalProtein, totalCarbs, totalFat

**BE hiện tại:**
- Có MealFood entity nhưng cần kiểm tra DTO response

**Cần thay đổi:**
- ✅ Tạo MealFoodResponseDTO với đầy đủ fields

---

## Thứ tự ưu tiên triển khai

1. **Priority 1 (Critical):**
   - Update User entity: thêm updatedAt, lastLoginAt
   - Update JwtResponse: thêm refreshToken, user object
   - Update Auth endpoints: login, register trả về đúng format

2. **Priority 2 (High):**
   - Challenge APIs: thêm participants count và list
   - Training Plan APIs: thêm exercises array với đầy đủ fields
   - User Management: thêm pagination

3. **Priority 3 (Medium):**
   - Reward: thêm status field
   - Meal: cập nhật DTO response
   - Các API khác theo yêu cầu FE


