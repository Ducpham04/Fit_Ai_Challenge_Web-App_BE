package com.example.fitchallenge.service.impl;

import com.example.fitchallenge.DTO.user.JwtResponse;
import com.example.fitchallenge.DTO.user.LoginRequest;
import com.example.fitchallenge.DTO.user.RegisterRequestAdmin;
import com.example.fitchallenge.DTO.user.UserDTO;
import com.example.fitchallenge.DTO.user.userProfile.*;
import com.example.fitchallenge.Entity.*;
import com.example.fitchallenge.Security.JWT.JwtTokenProvider;
import com.example.fitchallenge.config.NotificationResponse;
import com.example.fitchallenge.repository.*;
import com.example.fitchallenge.repository.User.UserRepository;
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
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
    @Override
    public void register(RegisterRequestAdmin registerRequestAdmin) {
        // 1. Check email tồn tại
        if(userRepository.existsByEmail(registerRequestAdmin.getEmail())) {
            throw new RuntimeException("Email đã tồn tại!");
        }


        // 3. Lấy role theo roleId từ request, default = 1 nếu null
        Role role = roleRepository.findById(registerRequestAdmin.getRoleId() != null ? registerRequestAdmin.getRoleId() : 2)
                .orElseThrow(() -> new RuntimeException("Role không tồn tại"));

        // 4. Tạo user mới
        User user = new User();
        user.setUserName(registerRequestAdmin.getFullName()); // bắt buộc không trùng
        System.out.println(registerRequestAdmin.getFullName());
        user.setEmail(registerRequestAdmin.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequestAdmin.getPassword()));
        user.setRole(role);
       // user.setLinkImage(registerRequestAdmin.getLinkImage());
        user.setCreateAt(new java.util.Date());

        // 5. Save vào DB
        userRepository.save(user);
    }


    @Override
    public JwtResponse login(LoginRequest loginRequest) {
        Optional<User> user  = userRepository.findByEmail((loginRequest.getEmail())) ;
        if(!passwordEncoder.matches(loginRequest.getPassword(), user.get().getPassword())){
            throw new RuntimeException("Invalid password");
        }
        String token = jwtTokenProvider.generateToken(user.get().getEmail(), user.get().getRole().getRoleName());
        System.out.println(token);
        return new JwtResponse(token);
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
            dto.setCreatedAt(user.getCreateAt());
            dto.setRole(user.getRole().getRoleName());
            dto.setStatus(user.getStatus());

    return dto ;
        }).collect(Collectors.toList()); // ẩn password trước khi trả về

        return new NotificationResponse(true, "Success", users);


    }

    @Override
    public UserDTO getUserById(Long id) {
        return userRepository.findById(id).map(user -> {;
            UserDTO dto = new UserDTO();
            dto.setId(user.getId());
            dto.setEmail(user.getEmail());
            dto.setFullName(user.getUserName());
            dto.setRole(user.getRole().getRoleName());
            dto.setStatus(user.getStatus());
            return dto;
        }).orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new UserDTO(
                user.getId(),
                user.getEmail(),
                user.getUserName(),
                user.getRole().getRoleName(),
                user.getLinkImage(),
                user.getCreateAt(),
                user.getStatus()
        );

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
        // Lấy goals từ UserNutrition (nếu có)
        List<UserNutrition> userNutritions = userNutritionRepository.findAll()
                .stream()
                .filter(un -> un.getUser().getId().equals(userId) && "active".equals(un.getStatus()))
                .collect(Collectors.toList());

        Integer dailyCalories = null;
        if (!userNutritions.isEmpty()) {
            UserNutrition activeNutrition = userNutritions.get(0);
            if (activeNutrition.getNutritionPlan() != null && 
                activeNutrition.getNutritionPlan().getCaloriesPerDay() != null) {
                dailyCalories = activeNutrition.getNutritionPlan().getCaloriesPerDay();
            }
        }

        // Tính weeklyWorkouts từ UserTraining
        LocalDate weekStart = LocalDate.now().with(DayOfWeek.MONDAY);
        long weeklyWorkouts = userTrainingRepository.findUserTrainingDetailsByUserId(userId)
                .stream()
                .filter(ut -> ut.getStartDate() != null && 
                             !ut.getStartDate().isBefore(weekStart))
                .count();

        // monthlyDistance có thể tính từ training plans (nếu có thông tin distance)
        Integer monthlyDistance = null; // Cần thêm logic tính toán nếu có field distance

        return UserGoalsDTO.builder()
                .weeklyWorkouts((int) weeklyWorkouts)
                .dailyCalories(dailyCalories)
                .monthlyDistance(monthlyDistance)
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

}
