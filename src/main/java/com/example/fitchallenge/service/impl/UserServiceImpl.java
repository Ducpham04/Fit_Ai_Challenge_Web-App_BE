package com.example.fitchallenge.service.impl;

import com.example.fitchallenge.dto.user.userprofile.UserGoalsDTO;
import com.example.fitchallenge.dto.user.userprofile.FullUserProfileDTO;
import com.example.fitchallenge.dto.user.userprofile.UserStatsDTO;
import com.example.fitchallenge.dto.user.userprofile.WeeklyStatsDTO;
import com.example.fitchallenge.dto.user.userprofile.UserProfileDTO;
import com.example.fitchallenge.dto.user.userprofile.ActivitySummaryDTO;
import com.example.fitchallenge.dto.user.userprofile.AchievementDTO;
import com.example.fitchallenge.entity.UserTraining;
import com.example.fitchallenge.entity.UserNutrition;
import com.example.fitchallenge.entity.TrainingPlanDetail;
import com.example.fitchallenge.entity.UserChallenge;
import com.example.fitchallenge.entity.User;
import com.example.fitchallenge.entity.InformationBodyUser;
import com.example.fitchallenge.entity.Role;
import com.example.fitchallenge.dto.users.JwtResponse;
import com.example.fitchallenge.dto.users.LoginRequest;
import com.example.fitchallenge.dto.users.RegisterRequestAdmin;
import com.example.fitchallenge.dto.users.UserDTO;
import com.example.fitchallenge.security.jwt.JwtTokenProvider;
import com.example.fitchallenge.config.NotificationResponse;
import com.example.fitchallenge.repository.*;
import com.example.fitchallenge.repository.user.UserRepository;
import com.example.fitchallenge.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RoleRepository roleRepository;
    private final UserChallengeRepository userChallengeRepository;
    private final UserTrainingRepository userTrainingRepository;
    private final UserNutritionRepository userNutritionRepository;
    private final TrainingPlanDetailRepository trainingPlanDetailRepository;
    private final InformationBodyUserRepository informationBodyUserRepository;
    private final DailyTrainingLogRepository dailyTrainingLogRepository;
    @Override
    public JwtResponse register(RegisterRequestAdmin registerRequestAdmin) {
        // 1. Validate input
        if (registerRequestAdmin.getEmail() == null || registerRequestAdmin.getEmail().trim().isEmpty()) {
            throw new RuntimeException("Email is required");
        }
        if (registerRequestAdmin.getFullName() == null || registerRequestAdmin.getFullName().trim().isEmpty()) {
            throw new RuntimeException("Full name is required");
        }
        if (registerRequestAdmin.getPassword() == null || registerRequestAdmin.getPassword().trim().isEmpty()) {
            throw new RuntimeException("Password is required");
        }
        if (registerRequestAdmin.getPassword().length() < 6) {
            throw new RuntimeException("Password must be at least 6 characters");
        }
        
        // 2. Check email tồn tại
        if(userRepository.existsByEmail(registerRequestAdmin.getEmail().trim())) {
            throw new RuntimeException("Email đã tồn tại!");
        }

        // 3. Lấy role theo roleId từ request, default = 2 (USER) nếu null
        Long roleId = registerRequestAdmin.getRoleId() != null ? registerRequestAdmin.getRoleId() : 2L;
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role không tồn tại (ID: " + roleId + "). Vui lòng đảm bảo role USER (ID=2) đã được tạo trong database."));

        // 4. Tạo user mới
        User user = new User();
        user.setUserName(registerRequestAdmin.getFullName().trim());
        user.setEmail(registerRequestAdmin.getEmail().trim().toLowerCase());
        user.setPassword(passwordEncoder.encode(registerRequestAdmin.getPassword()));
        user.setRole(role);
        user.setStatus("active"); // Set default status
        user.setPoints(0); // Set default points
        Date now = new java.util.Date();
        user.setCreateAt(now);
        user.setUpdatedAt(now);
        user.setLastLoginAt(now);

        // 5. Save vào DB
        try {
            User savedUser = userRepository.save(user);
            
            // 6. Generate tokens and return response
            String token = jwtTokenProvider.generateToken(savedUser.getEmail(), savedUser.getRole().getRoleName());
            String refreshToken = jwtTokenProvider.generateRefreshToken(savedUser.getEmail());
            
            JwtResponse.UserInfoDTO userInfo = new JwtResponse.UserInfoDTO(
                    savedUser.getId(),
                    savedUser.getEmail(),
                    savedUser.getUserName(),
                    savedUser.getRole().getRoleName()
            );
            
            return new JwtResponse(token, refreshToken, userInfo);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to save user: " + e.getMessage());
        }
    }


    @Override
    public JwtResponse login(LoginRequest loginRequest) {
        Optional<User> userOpt = userRepository.findByEmail(loginRequest.getEmail());
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        
        User user = userOpt.get();
        if(!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())){
            throw new RuntimeException("Invalid password");
        }
        
        // Update last login time
        user.setLastLoginAt(new java.util.Date());
        userRepository.save(user);
        
        // Generate tokens
        String token = jwtTokenProvider.generateToken(user.getEmail(), user.getRole().getRoleName());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getEmail());
        
        // Create user info DTO
        JwtResponse.UserInfoDTO userInfo = new JwtResponse.UserInfoDTO(
                user.getId(),
                user.getEmail(),
                user.getUserName(),
                user.getRole().getRoleName()
        );
        
        return new JwtResponse(token, refreshToken, userInfo);
    }


    @Override
    public UserDetails loadUserByEmail(String email)  {
        // Tìm user trong DB
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + email));

        // Trả về một đối tượng UserDetails
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities((GrantedAuthority) user.getRole()) // có thể lấy từ role của user
                .build();
    }

    @Override
    public NotificationResponse getAllUsers() {
        List<UserDTO> users = userRepository.findAll()
        .stream().map(user ->  {
            UserDTO dto = new UserDTO();
            dto.setId(user.getId());
            dto.setEmail(user.getEmail());
            dto.setFullName(user.getUserName());
            dto.setLinkImage(user.getLinkImage());
            dto.setProfileImage(user.getLinkImage()); // Map to profileImage for FE
            dto.setCreatedAt(user.getCreateAt());
            dto.setUpdatedAt(user.getUpdatedAt());
            dto.setLastLoginAt(user.getLastLoginAt());
            dto.setRole(user.getRole().getRoleName());
            dto.setStatus(user.getStatus());
            return dto;
        }).collect(Collectors.toList()); // ẩn password trước khi trả về

        return new NotificationResponse(true, "Success", users);


    }

    @Override
    public UserDTO getUserById(Long id) {
        return userRepository.findById(id).map(user -> {
            UserDTO dto = new UserDTO();
            dto.setId(user.getId());
            dto.setEmail(user.getEmail());
            dto.setFullName(user.getUserName());
            dto.setRole(user.getRole().getRoleName());
            dto.setLinkImage(user.getLinkImage());
            dto.setProfileImage(user.getLinkImage()); // Map to profileImage for FE
            dto.setCreatedAt(user.getCreateAt());
            dto.setUpdatedAt(user.getUpdatedAt());
            dto.setLastLoginAt(user.getLastLoginAt());
            dto.setStatus(user.getStatus());
            return dto;
        }).orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setFullName(user.getUserName());
        dto.setRole(user.getRole().getRoleName());
        dto.setLinkImage(user.getLinkImage());
        dto.setProfileImage(user.getLinkImage()); // Map to profileImage for FE
        dto.setCreatedAt(user.getCreateAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        dto.setLastLoginAt(user.getLastLoginAt());
        dto.setStatus(user.getStatus());
        return dto;
    }

    @Override
    public UserProfileDTO getUserProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Tính currentStreak (số ngày liên tục hoàn thành challenge)
        int currentStreak = calculateCurrentStreak(userId);

        // Format joinDate
        String joinDate = user.getCreateAt() != null
                ? user.getCreateAt().toInstant().atZone(ZoneId.systemDefault())
                .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
                : null;

        return UserProfileDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUserName())
                .avatar(user.getLinkImage())
                .joinDate(joinDate)
                .currentStreak(currentStreak)
                .build();
    }

    @Override
    public FullUserProfileDTO getFullUserProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // UserProfileDTO
        UserProfileDTO profile = getUserProfile(userId);

        // UserStatsDTO
        UserStatsDTO stats = buildUserStatsDTO(userId, user);

        // ActivitySummaryDTO
        ActivitySummaryDTO activity = buildActivitySummaryDTO(userId);

        // List<AchievementDTO>
        List<AchievementDTO> achievements = buildAchievementsDTO(userId, user);

        // UserGoalsDTO
        UserGoalsDTO goals = buildUserGoalsDTO(userId);

        // WeeklyStatsDTO
        WeeklyStatsDTO weeklyStats = buildWeeklyStatsDTO(userId);

        return FullUserProfileDTO.builder()
                .profile(profile)
                .stats(stats)
                .activity(activity)
                .achievements(achievements)
                .goals(goals)
                .weeklyStats(weeklyStats)
                .build();
    }

    private int calculateCurrentStreak(Long userId) {
        List<UserChallenge> challenges = userChallengeRepository.findAll()
                .stream()
                .filter(uc -> uc.getUser().getId().equals(userId) && "success".equals(uc.getStatus()))
                .filter(uc -> uc.getCompletedAt() != null)
                .sorted((a, b) -> b.getCompletedAt().compareTo(a.getCompletedAt()))
                .collect(Collectors.toList());

        if (challenges.isEmpty()) {
            return 0;
        }

        int streak = 0;
        LocalDate lastDate = null;
        LocalDate today = LocalDate.now();

        for (UserChallenge challenge : challenges) {
            LocalDate completedDate = challenge.getCompletedAt().toLocalDate();
            
            if (lastDate == null) {
                // Ngày đầu tiên
                if (completedDate.equals(today) || completedDate.equals(today.minusDays(1))) {
                    streak = 1;
                    lastDate = completedDate;
                } else {
                    break;
                }
            } else {
                // Kiểm tra ngày liên tiếp
                if (completedDate.equals(lastDate.minusDays(1))) {
                    streak++;
                    lastDate = completedDate;
                } else {
                    break;
                }
            }
        }

        return streak;
    }

    private UserStatsDTO buildUserStatsDTO(Long userId, User user) {
        // Tính challengesCompleted
        long challengesCompleted = userChallengeRepository.findAll()
                .stream()
                .filter(uc -> uc.getUser().getId().equals(userId) && "success".equals(uc.getStatus()))
                .count();

        // Tính totalWorkouts
        long totalWorkouts = userTrainingRepository.findUserTrainingDetailsByUserId(userId)
                .stream()
                .filter(ut -> "completed".equals(ut.getStatus()))
                .count();

        // Tính currentStreak
        int currentStreak = calculateCurrentStreak(userId);

        // AI Score từ points
        Integer aiScore = user.getPoints() != null ? user.getPoints() : 0;

        return UserStatsDTO.builder()
                .aiScore(aiScore)
                .challengesCompleted((int) challengesCompleted)
                .totalWorkouts((int) totalWorkouts)
                .currentStreak(currentStreak)
                .build();
    }

    private ActivitySummaryDTO buildActivitySummaryDTO(Long userId) {
        List<UserTraining> userTrainings = userTrainingRepository.findUserTrainingDetailsByUserId(userId);
        
        int totalCaloriesBurned = 0;
        int totalMinutes = 0;
        Map<String, Long> workoutCounts = userTrainings.stream()
                .filter(ut -> ut.getTrainingPlan() != null && ut.getTrainingPlan().getTitle() != null)
                .collect(Collectors.groupingBy(
                        ut -> ut.getTrainingPlan().getTitle(),
                        Collectors.counting()
                ));

        String favoriteWorkout = workoutCounts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("N/A");

        // Tính calories và minutes từ training plan details
        for (UserTraining ut : userTrainings) {
            if (ut.getTrainingPlan() != null) {
                List<TrainingPlanDetail> details = trainingPlanDetailRepository
                        .findByTrainingPlan_TpId(ut.getTrainingPlan().getTpId());
                
                // Giả sử mỗi detail có duration và calories (cần điều chỉnh theo entity thực tế)
                int detailCount = details.size();
                totalMinutes += detailCount * 30; // Default 30 minutes per detail
                totalCaloriesBurned += detailCount * 200; // Default 200 calories per detail
            }
        }

        return ActivitySummaryDTO.builder()
                .totalCaloriesBurned(totalCaloriesBurned)
                .totalMinutes(totalMinutes)
                .favoriteWorkout(favoriteWorkout)
                .build();
    }

    private List<AchievementDTO> buildAchievementsDTO(Long userId, User user) {
        List<AchievementDTO> achievements = new ArrayList<>();

        // Tính số challenge hoàn thành
        long challengesCompleted = userChallengeRepository.findAll()
                .stream()
                .filter(uc -> uc.getUser().getId().equals(userId) && "success".equals(uc.getStatus()))
                .count();

        // Tính số workout hoàn thành
        long workoutsCompleted = userTrainingRepository.findUserTrainingDetailsByUserId(userId)
                .stream()
                .filter(ut -> "completed".equals(ut.getStatus()))
                .count();

        // Achievement: First Challenge
        if (challengesCompleted >= 1) {
            achievements.add(AchievementDTO.builder()
                    .name("First Challenge")
                    .icon("🏆")
                    .color("from-yellow-400 to-orange-500")
                    .build());
        }

        // Achievement: 10 Challenges
        if (challengesCompleted >= 10) {
            achievements.add(AchievementDTO.builder()
                    .name("Challenge Master")
                    .icon("👑")
                    .color("from-purple-400 to-pink-500")
                    .build());
        }

        // Achievement: First Workout
        if (workoutsCompleted >= 1) {
            achievements.add(AchievementDTO.builder()
                    .name("First Workout")
                    .icon("💪")
                    .color("from-blue-400 to-cyan-500")
                    .build());
        }

        // Achievement: 7 Day Streak
        int currentStreak = calculateCurrentStreak(userId);
        if (currentStreak >= 7) {
            achievements.add(AchievementDTO.builder()
                    .name("7 Day Streak")
                    .icon("🔥")
                    .color("from-red-400 to-orange-500")
                    .build());
        }

        // Achievement: Points milestone
        if (user.getPoints() != null && user.getPoints() >= 1000) {
            achievements.add(AchievementDTO.builder()
                    .name("High Scorer")
                    .icon("⭐")
                    .color("from-yellow-400 to-amber-500")
                    .build());
        }

        return achievements;
    }

    private UserGoalsDTO buildUserGoalsDTO(Long userId) {
        // Lấy InformationBodyUser để lấy goals và recommended calories
        List<InformationBodyUser> bodyInfoList = informationBodyUserRepository.findByUserId(userId);
        InformationBodyUser bodyInfo = bodyInfoList.isEmpty() ? null : bodyInfoList.get(0);

        // Lấy dailyCalories từ InformationBodyUser (recommendedCalories) hoặc UserNutrition
        Integer dailyCalories = null;
        if (bodyInfo != null && bodyInfo.getRecommendedCalories() != null) {
            dailyCalories = bodyInfo.getRecommendedCalories().intValue();
        } else {
            // Fallback: Lấy từ UserNutrition
            List<UserNutrition> userNutritions = userNutritionRepository.findAll()
                    .stream()
                    .filter(un -> un.getUser().getId().equals(userId) && "active".equals(un.getStatus()))
                    .collect(Collectors.toList());

            if (!userNutritions.isEmpty()) {
                UserNutrition activeNutrition = userNutritions.get(0);
                if (activeNutrition.getNutritionPlan() != null && 
                    activeNutrition.getNutritionPlan().getCaloriesPerDay() != null) {
                    dailyCalories = activeNutrition.getNutritionPlan().getCaloriesPerDay();
                }
            }
        }

        // Tính weeklyWorkouts từ DailyTrainingLog (completed challenges trong tuần này)
        LocalDate weekStart = LocalDate.now().with(DayOfWeek.MONDAY);
        LocalDate weekEnd = weekStart.plusDays(6);
        
        long weeklyWorkouts = dailyTrainingLogRepository.findByUser_Id(userId)
                .stream()
                .filter(log -> log.getTrainingDate() != null &&
                             !log.getTrainingDate().isBefore(weekStart) &&
                             !log.getTrainingDate().isAfter(weekEnd) &&
                             "completed".equalsIgnoreCase(log.getStatus()))
                .map(log -> log.getTrainingDate()) // Get unique dates
                .distinct()
                .count();

        // Tính monthlyDistance từ DailyTrainingLog (nếu có actualDurationMinutes, có thể estimate)
        // For now, set to null as we don't have distance data
        Integer monthlyDistance = null;

        // Lấy goal name từ InformationBodyUser
        String goalName = null;
        Long goalId = null;
        if (bodyInfo != null && bodyInfo.getGoals() != null) {
            goalName = bodyInfo.getGoals().getName();
            goalId = bodyInfo.getGoals().getId();
        }

        // Set default weekly workouts target
        Integer weeklyWorkoutsTarget = 5; // Default target: 5 workouts per week

        return UserGoalsDTO.builder()
                .weeklyWorkouts((int) weeklyWorkouts)
                .weeklyWorkoutsTarget(weeklyWorkoutsTarget)
                .dailyCalories(dailyCalories)
                .monthlyDistance(monthlyDistance)
                .goalName(goalName)
                .goalId(goalId)
                .build();
    }

    private WeeklyStatsDTO buildWeeklyStatsDTO(Long userId) {
        LocalDate weekStart = LocalDate.now().with(DayOfWeek.MONDAY);
        LocalDate weekEnd = weekStart.plusDays(6);

        List<UserTraining> weeklyTrainings = userTrainingRepository.findUserTrainingDetailsByUserId(userId)
                .stream()
                .filter(ut -> ut.getStartDate() != null && 
                             !ut.getStartDate().isBefore(weekStart) &&
                             !ut.getStartDate().isAfter(weekEnd))
                .collect(Collectors.toList());

        int workouts = weeklyTrainings.size();
        int calories = 0;
        int minutes = 0;

        for (UserTraining ut : weeklyTrainings) {
            if (ut.getTrainingPlan() != null) {
                List<TrainingPlanDetail> details = trainingPlanDetailRepository
                        .findByTrainingPlan_TpId(ut.getTrainingPlan().getTpId());
                
                int detailCount = details.size();
                calories += detailCount * 200; // Default calories per detail
                minutes += detailCount * 30; // Default minutes per detail
            }
        }

        return WeeklyStatsDTO.builder()
                .workouts(workouts)
                .calories(calories)
                .minutes(minutes)
                .build();
    }
    
    @Override
    public Page<UserDTO> getAllUsersPaginated(String status, String role, String search, Pageable pageable) {
        List<User> allUsers = userRepository.findAll();
        
        // Search by email or username
        if (search != null && !search.isEmpty()) {
            String searchLower = search.toLowerCase();
            allUsers = allUsers.stream()
                    .filter(u -> (u.getEmail() != null && u.getEmail().toLowerCase().contains(searchLower)) ||
                            (u.getUserName() != null && u.getUserName().toLowerCase().contains(searchLower)))
                    .collect(Collectors.toList());
        }
        
        // Filter by status
        if (status != null && !status.isEmpty()) {
            allUsers = allUsers.stream()
                    .filter(u -> u.getStatus() != null && u.getStatus().equalsIgnoreCase(status))
                    .collect(Collectors.toList());
        }
        
        // Filter by role
        if (role != null && !role.isEmpty()) {
            allUsers = allUsers.stream()
                    .filter(u -> u.getRole() != null && 
                            u.getRole().getRoleName().equalsIgnoreCase(role))
                    .collect(Collectors.toList());
        }
        
        // Map to DTOs
        List<UserDTO> dtos = allUsers.stream()
                .map(user -> {
                    UserDTO dto = new UserDTO();
                    dto.setId(user.getId());
                    dto.setEmail(user.getEmail());
                    dto.setFullName(user.getUserName());
                    dto.setLinkImage(user.getLinkImage());
                    dto.setProfileImage(user.getLinkImage());
                    dto.setCreatedAt(user.getCreateAt());
                    dto.setUpdatedAt(user.getUpdatedAt());
                    dto.setLastLoginAt(user.getLastLoginAt());
                    dto.setRole(user.getRole().getRoleName());
                    dto.setStatus(user.getStatus());
                    return dto;
                })
                .collect(Collectors.toList());
        
        // Apply pagination
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), dtos.size());
        List<UserDTO> pagedDtos = dtos.subList(start, end);
        
        return new PageImpl<>(pagedDtos, pageable, dtos.size());
    }
    
    @Override
    public UserDTO createUser(RegisterRequestAdmin request) {
        // Check email exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        
        // Get role
        Role role = roleRepository.findById(request.getRoleId() != null ? request.getRoleId() : 2)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        
        // Create user
        User user = new User();
        user.setUserName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(role);
        Date now = new Date();
        user.setCreateAt(now);
        user.setUpdatedAt(now);
        
        User savedUser = userRepository.save(user);
        
        // Return DTO
        return getUserById(savedUser.getId());
    }
    
    @Override
    public UserDTO updateUser(Long id, RegisterRequestAdmin request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Update fields
        if (request.getFullName() != null) {
            user.setUserName(request.getFullName());
        }
        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new RuntimeException("Email already exists");
            }
            user.setEmail(request.getEmail());
        }
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        if (request.getRoleId() != null) {
            Role role = roleRepository.findById(request.getRoleId())
                    .orElseThrow(() -> new RuntimeException("Role not found"));
            user.setRole(role);
        }
        
        user.setUpdatedAt(new Date());
        User savedUser = userRepository.save(user);
        
        // Return DTO
        return getUserById(savedUser.getId());
    }
    
    @Override
    public UserDTO updateUserAvatar(Long id, String avatarUrl) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        user.setLinkImage(avatarUrl);
        user.setUpdatedAt(new Date());
        User savedUser = userRepository.save(user);
        
        return getUserById(savedUser.getId());
    }

    @Override
    public NotificationResponse deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            return new NotificationResponse(false, "User not found");
        }
        
        userRepository.deleteById(id);
        return new NotificationResponse(true, "User deleted successfully");
    }

}
