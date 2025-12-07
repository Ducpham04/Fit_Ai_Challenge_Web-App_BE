# API Endpoints Mapping - FE Requirements vs BE Implementation

## ✅ Đã hoàn thành

### 1. Authentication APIs

| FE Requirement | BE Endpoint | Status | Notes |
|---------------|-------------|--------|-------|
| `POST /auth/login` | `POST /api/auth/login` | ✅ Updated | Now returns `{token, refreshToken, user}` |
| `POST /auth/register` | `POST /api/auth/register` | ✅ Updated | Now returns `{token, refreshToken, user}` |
| `GET /auth/user` | `GET /api/auth/user` | ✅ Added | Returns full UserDTO |
| `GET /auth/me` | `GET /api/auth/me` | ✅ Exists | Returns UserDTO |

### 2. User Profile APIs

| FE Requirement | BE Endpoint | Status | Notes |
|---------------|-------------|--------|-------|
| `GET /users/{userId}/profile` | `GET /api/v1/users/{userId}/profile` | ✅ Exists | Returns UserProfileDTO |
| `GET /users/{userId}/profile/full` | `GET /api/v1/users/{userId}/profile/full` | ✅ Exists | Returns FullUserProfileDTO |

## 🔄 Cần cập nhật/Cần tạo mới

### 3. Challenge APIs

| FE Requirement | BE Endpoint | Status | Action Needed |
|---------------|-------------|--------|---------------|
| `GET /challenges` | `GET /api/challenges` | ❌ Missing | Create new endpoint with pagination, filters |
| `GET /challenges/{id}` | `GET /api/challenges/{id}` | ❌ Missing | Create with participants list |
| `POST /challenges/{id}/join` | `POST /api/challenges/{id}/join` | ❌ Missing | Create join endpoint |

**Current BE:**
- `GET /api/admin/challenges` - Admin only, returns NotificationResponse
- `GET /api/admin/challenges/{id}` - Admin only

**DTOs Created:**
- ✅ `ChallengeResponseDTO` - với video array, participants count
- ✅ `ParticipantDTO` - cho participants list

**Next Steps:**
1. Create `ChallengeController` (non-admin) với endpoints trên
2. Update `ChallengeService` để có methods:
   - `getAllChallengesForUser()` với pagination và filters
   - `getChallengeByIdForUser()` với participants list
   - `joinChallenge()`

### 4. Training Plan APIs

| FE Requirement | BE Endpoint | Status | Action Needed |
|---------------|-------------|--------|---------------|
| `GET /training-plans` | `GET /api/training-plans` | ❌ Missing | Create with exercises array |
| `GET /training-plans/{id}` | `GET /api/training-plans/{id}` | ❌ Missing | Create with full details |
| `POST /training-plans/{id}/start` | `POST /api/training-plans/{id}/start` | ⚠️ Partial | Exists as `POST /api/user/training` but different format |

**Current BE:**
- `GET /api/admin/training-plans` - Admin only
- `POST /api/user/training` - Create user training

**DTOs Created:**
- ✅ `TrainingPlanResponseDTO` - với exercises array
- ✅ `ExerciseDTO` - cho exercise details

**Entity Updates:**
- ✅ Added fields to `TrainingPlanDetail`: duration, restTime, instructions

**Next Steps:**
1. Create `TrainingPlanController` (non-admin)
2. Update `TrainingPlanService` để map TrainingPlanDetail → ExerciseDTO
3. Update `UserTrainingService` để có `startTrainingPlan()` method

### 5. Admin User Management APIs

| FE Requirement | BE Endpoint | Status | Action Needed |
|---------------|-------------|--------|---------------|
| `GET /admin/users` | `GET /api/admin/users` | ✅ Exists | ⚠️ Add pagination |
| `POST /admin/users` | `POST /api/admin/users` | ❌ Missing | Create |
| `PUT /admin/users/{id}` | `PUT /api/admin/users/{id}` | ❌ Missing | Create |
| `DELETE /admin/users/{id}` | `DELETE /api/admin/users/{id}` | ❌ Missing | Create |

**Current BE:**
- `GET /api/admin/users` - Returns NotificationResponse with UserDTO list
- `GET /api/users/{id}` - Returns NotificationResponse (not admin-specific)

**Next Steps:**
1. Add pagination to `getAllUsers()`
2. Create CRUD methods in `UserService`
3. Create `AdminUserController` hoặc update `AuthController`

### 6. Admin Challenge Management APIs

| FE Requirement | BE Endpoint | Status | Notes |
|---------------|-------------|--------|-------|
| `GET /admin/challenges` | `GET /api/admin/challenges` | ✅ Exists | Returns NotificationResponse |
| `POST /admin/challenges` | `POST /api/admin/challenges` | ✅ Exists | Multipart form-data |
| `PUT /admin/challenges/{id}` | `PUT /api/admin/challenges/{id}` | ✅ Exists | Multipart form-data |
| `DELETE /admin/challenges/{id}` | `DELETE /api/admin/challenges/{id}` | ✅ Exists | |

**Action Needed:**
- Update response DTOs để match FE format (video array, participants count)

### 7. Admin Reward Management APIs

| FE Requirement | BE Endpoint | Status | Action Needed |
|---------------|-------------|--------|---------------|
| `GET /admin/rewards` | `GET /api/admin/rewards` | ✅ Exists | ⚠️ Update DTO format |
| `POST /admin/rewards` | `POST /api/admin/rewards` | ✅ Exists | |
| `PUT /admin/rewards/{id}` | `PUT /api/admin/rewards/{id}` | ✅ Exists | |

**Entity Updates:**
- ✅ Added `status` field to Reward entity

**Next Steps:**
- Update Reward DTOs để include `status`, `expiresAt` format

### 8. Admin Meal Management APIs

| FE Requirement | BE Endpoint | Status | Notes |
|---------------|-------------|--------|-------|
| `GET /admin/meals` | `GET /api/admin/meals` | ✅ Exists | Returns NotificationResponse |
| `POST /admin/meals` | `POST /api/admin/meals` | ✅ Exists | |

**DTOs:**
- ✅ `MealResponse` - có foods array
- ✅ `MealFoodResponse` - có đầy đủ fields (mfId, foodId, foodName, quantityG, totalCalories, totalProtein, totalCarbs, totalFat)

**Status:** ✅ DTOs đã đúng format FE cần

### 9. Admin Food Management APIs

| FE Requirement | BE Endpoint | Status | Notes |
|---------------|-------------|--------|-------|
| `GET /admin/foods` | `GET /api/admin/foods` | ✅ Exists | |
| `POST /admin/foods` | `POST /api/admin/foods` | ✅ Exists | |

**Status:** ✅ Có thể cần kiểm tra DTO format

### 10. Admin Training Plan Management APIs

| FE Requirement | BE Endpoint | Status | Action Needed |
|---------------|-------------|--------|---------------|
| `GET /admin/training-plans` | `GET /api/admin/training-plans` | ✅ Exists | ⚠️ Update DTO format |
| `POST /admin/training-plans` | `POST /api/admin/training-plans` | ✅ Exists | |

**Next Steps:**
- Update response để include subscribers count, price, focusArea

### 11. Admin Nutrition Plan Management APIs

| FE Requirement | BE Endpoint | Status | Action Needed |
|---------------|-------------|--------|---------------|
| `GET /admin/nutrition-plans` | `GET /api/admin/nutrition-plans` | ✅ Exists | ⚠️ Update DTO format |
| `POST /admin/nutrition-plans` | `POST /api/admin/nutrition-plans` | ✅ Exists | |

**Next Steps:**
- Update DTO để include subscribers count, price, target

### 12. Admin Transaction Management APIs

| FE Requirement | BE Endpoint | Status | Notes |
|---------------|-------------|--------|-------|
| `GET /admin/transactions` | `GET /api/admin/transactions` | ✅ Exists | |

**Status:** ✅ Cần kiểm tra DTO format

### 13. Admin Goal Management APIs

| FE Requirement | BE Endpoint | Status | Notes |
|---------------|-------------|--------|-------|
| `GET /admin/goals` | `GET /api/admin/goals` | ✅ Exists | |

**Note:** FE yêu cầu goals có userId, nhưng Goals entity hiện tại không có userId. 
Có thể FE đang nhầm với UserGoals (mục tiêu cá nhân của user) vs Goals (mục tiêu chung của hệ thống).

---

## Summary of Changes Made

### Entity Updates:
1. ✅ **User**: Added `updatedAt`, `lastLoginAt` fields
2. ✅ **Challenges**: Added `reward` field, updated Status enum
3. ✅ **TrainingPlanDetail**: Added `duration`, `restTime`, `instructions` fields
4. ✅ **Reward**: Added `status` field

### DTO Updates:
1. ✅ **JwtResponse**: Added `refreshToken`, `user` object
2. ✅ **UserDTO**: Added `updatedAt`, `lastLoginAt`, `profileImage` (alias for linkImage)
3. ✅ **ChallengeResponseDTO**: New DTO với video array, participants
4. ✅ **ParticipantDTO**: New DTO cho participants list
5. ✅ **TrainingPlanResponseDTO**: New DTO với exercises array
6. ✅ **ExerciseDTO**: New DTO cho exercise details

### Service Updates:
1. ✅ **UserService**: 
   - `register()` now returns `JwtResponse`
   - `login()` returns `JwtResponse` with refreshToken and user
   - Updated all methods to populate new UserDTO fields

### Controller Updates:
1. ✅ **AuthController**: 
   - `register()` returns `JwtResponse`
   - Added `GET /api/auth/user` endpoint

---

## Next Steps (Priority Order)

### Priority 1: Critical APIs
1. **Create Challenge APIs for users** (`/api/challenges`)
   - GET all with pagination
   - GET by ID with participants
   - POST join challenge

2. **Create Training Plan APIs for users** (`/api/training-plans`)
   - GET all with exercises
   - GET by ID with full details
   - POST start training plan

3. **Add pagination to Admin User Management**
   - Update `getAllUsers()` method

### Priority 2: Admin CRUD
4. **Create Admin User CRUD endpoints**
   - POST, PUT, DELETE for users

5. **Update Admin DTOs to match FE format**
   - Challenge, Reward, Training Plan, Nutrition Plan responses

### Priority 3: Enhancements
6. **Add refresh token endpoint**
   - POST /api/auth/refresh

7. **Add pagination to all list endpoints**

8. **Update error handling**
   - Standardize error responses
   - Add ControllerAdvice


