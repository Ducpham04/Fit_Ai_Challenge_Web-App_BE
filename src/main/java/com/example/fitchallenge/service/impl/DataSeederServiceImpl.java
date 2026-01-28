package com.example.fitchallenge.service.impl;

import com.example.fitchallenge.entity.TrainingPlan;
import com.example.fitchallenge.entity.TrainingPlanDetail;
import com.example.fitchallenge.entity.UserChallenge;
import com.example.fitchallenge.entity.User;
import com.example.fitchallenge.entity.Role;
import com.example.fitchallenge.entity.Goals;
import com.example.fitchallenge.entity.DailyTrainingLog;
import com.example.fitchallenge.entity.Challenges;
import com.example.fitchallenge.repository.*;
import com.example.fitchallenge.repository.GoalRepository;
import com.example.fitchallenge.repository.user.UserRepository;
import com.example.fitchallenge.service.DataSeederService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Service để import dữ liệu mẫu vào database
 */
@Service
@RequiredArgsConstructor
public class DataSeederServiceImpl implements DataSeederService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final GoalRepository goalRepository;
    private final ChallengeRepository challengeRepository;
    private final TrainingPlanRepository trainingPlanRepository;
    private final TrainingPlanDetailRepository trainingPlanDetailRepository;
    private final DailyTrainingLogRepository dailyTrainingLogRepository;
    private final UserChallengeRepository userChallengeRepository;
    private final PasswordEncoder passwordEncoder;

    private static final String[] FIRST_NAMES = {
        "Nguyễn", "Trần", "Lê", "Phạm", "Hoàng", "Huỳnh", "Phan", "Vũ", "Võ", "Đặng",
        "Bùi", "Đỗ", "Hồ", "Ngô", "Dương", "Lý", "Đinh", "Đào", "Mai", "Tô"
    };

    private static final String[] LAST_NAMES = {
        "Văn", "Thị", "Minh", "Anh", "Hùng", "Dũng", "Hoa", "Lan", "Hương", "Linh",
        "Nam", "Bình", "Hạnh", "Phúc", "Thành", "Tài", "Đức", "Khang", "Tuấn", "Quang"
    };

    private static final String[] CHALLENGE_TITLES = {
        "Push-up Challenge", "Squat Challenge", "Plank Challenge", "Pull-up Challenge",
        "Sit-up Challenge", "Burpee Challenge", "Jump Rope Challenge", "Mountain Climber Challenge",
        "Lunges Challenge", "Deadlift Challenge", "Bench Press Challenge", "Running Challenge"
    };

    private static final String[] TRAINING_PLAN_TITLES = {
        "Beginner Full Body", "Intermediate Strength", "Advanced Power", "Cardio Blast",
        "Muscle Building", "Weight Loss", "Endurance Training", "Flexibility & Mobility"
    };

    @Override
    @Transactional
    public DataSeederResult importAllData() {
        DataSeederResult result = new DataSeederResult();
        
        System.out.println("🚀 Bắt đầu import dữ liệu mẫu...");
        
        // 1. Import Goals (nếu chưa có)
        if (goalRepository.count() == 0) {
            importGoals();
        }
        
        // 2. Import Users
        int usersCount = importUsers(50);
        result.setUsersCreated(usersCount);
        
        // 3. Import Challenges
        int challengesCount = importChallenges(20);
        result.setChallengesCreated(challengesCount);
        
        // 4. Import Training Plans
        int plansCount = importTrainingPlans(10);
        result.setTrainingPlansCreated(plansCount);
        
        // 5. Import Daily Training Logs
        int logsCount = importDailyTrainingLogs(200);
        result.setDailyLogsCreated(logsCount);
        
        // 6. Import User Challenges
        int userChallengesCount = importUserChallenges(100);
        result.setUserChallengesCreated(userChallengesCount);
        
        result.setTotalRecords(usersCount + challengesCount + plansCount + logsCount + userChallengesCount);
        
        System.out.println("✅ Hoàn thành import dữ liệu!");
        System.out.println("   - Users: " + usersCount);
        System.out.println("   - Challenges: " + challengesCount);
        System.out.println("   - Training Plans: " + plansCount);
        System.out.println("   - Daily Logs: " + logsCount);
        System.out.println("   - User Challenges: " + userChallengesCount);
        System.out.println("   - Tổng: " + result.getTotalRecords() + " records");
        
        return result;
    }

    @Override
    @Transactional
    public int importUsers(int count) {
        List<Role> roles = roleRepository.findAll();
        if (roles.isEmpty()) {
            throw new RuntimeException("Không tìm thấy Role nào. Vui lòng tạo Role trước.");
        }
        
        Role userRole = roles.stream()
            .filter(r -> "USER".equalsIgnoreCase(r.getRoleName()) || "user".equalsIgnoreCase(r.getRoleName()))
            .findFirst()
            .orElse(roles.get(0));
        
        List<User> users = new ArrayList<>();
        Random random = new Random();
        
        for (int i = 0; i < count; i++) {
            String firstName = FIRST_NAMES[random.nextInt(FIRST_NAMES.length)];
            String lastName = LAST_NAMES[random.nextInt(LAST_NAMES.length)];
            String fullName = firstName + " " + lastName;
            String email = "user" + (i + 1) + "@example.com";
            
            // Kiểm tra email đã tồn tại chưa
            if (userRepository.existsByEmail(email)) {
                email = "user" + System.currentTimeMillis() + i + "@example.com";
            }
            
            User user = new User();
            user.setUserName(fullName.replace(" ", "")); // Remove space for username
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode("123456")); // Default password
            user.setRole(userRole);
            user.setLinkImage("https://api.dicebear.com/7.x/avataaars/svg?seed=" + fullName);
            user.setCreateAt(new Date());
            user.setUpdatedAt(new Date());
            user.setPoints(random.nextInt(10000)); // Random points 0-10000
            user.setStatus("active");
            
            users.add(user);
        }
        
        userRepository.saveAll(users);
        return users.size();
    }

    @Override
    @Transactional
    public int importChallenges(int count) {
        List<Goals> goals = goalRepository.findAll();
        if (goals.isEmpty()) {
            // Tạo goals mẫu nếu chưa có
            importGoals();
            goals = goalRepository.findAll();
        }
        
        Random random = new Random();
        List<Challenges> challenges = new ArrayList<>();
        
        String[] exerciseTypes = {"push-up", "squat", "plank", "pull-up", "sit-up", "burpee"};
        Challenges.DifficultLevel[] difficulties = Challenges.DifficultLevel.values();
        Challenges.Status[] statuses = {Challenges.Status.ACTIVE, Challenges.Status.ACTIVE, Challenges.Status.ACTIVE, Challenges.Status.INACTIVE};
        
        for (int i = 0; i < count; i++) {
            Goals goal = goals.get(random.nextInt(goals.size()));
            String title = CHALLENGE_TITLES[random.nextInt(CHALLENGE_TITLES.length)] + " #" + (i + 1);
            String exerciseType = exerciseTypes[random.nextInt(exerciseTypes.length)];
            
            Challenges challenge = new Challenges();
            challenge.setGoal(goal);
            challenge.setTitle(title);
            challenge.setDescription("Complete " + title + " with proper form. AI will analyze your performance.");
            challenge.setDifficult(difficulties[random.nextInt(difficulties.length)]);
            challenge.setExerciseType(exerciseType);
            challenge.setLinkVideos("https://example.com/video1.mp4,https://example.com/video2.mp4");
            challenge.setReward((random.nextInt(500) + 100) + " AI Points");
            challenge.setStatus(statuses[random.nextInt(statuses.length)]);
            
            challenges.add(challenge);
        }
        
        challengeRepository.saveAll(challenges);
        return challenges.size();
    }

    @Override
    @Transactional
    public int importTrainingPlans(int count) {
        List<Goals> goals = goalRepository.findAll();
        if (goals.isEmpty()) {
            importGoals();
            goals = goalRepository.findAll();
        }
        
        Random random = new Random();
        List<TrainingPlan> plans = new ArrayList<>();
        
        String[] difficulties = {"beginner", "intermediate", "advanced"};
        
        for (int i = 0; i < count; i++) {
            Goals goal = goals.get(random.nextInt(goals.size()));
            String title = TRAINING_PLAN_TITLES[random.nextInt(TRAINING_PLAN_TITLES.length)] + " #" + (i + 1);
            
            TrainingPlan plan = new TrainingPlan();
            plan.setGoal(goal);
            plan.setTitle(title);
            plan.setDescription("A comprehensive " + title.toLowerCase() + " program designed for all fitness levels.");
            plan.setDifficultyLevel(difficulties[random.nextInt(difficulties.length)]);
            plan.setDurationWeeks(random.nextInt(8) + 4); // 4-12 weeks
            
            plans.add(plan);
        }
        
        trainingPlanRepository.saveAll(plans);
        
        // Tạo training plan details cho mỗi plan
        List<Challenges> challenges = challengeRepository.findAll();
        if (!challenges.isEmpty()) {
            for (TrainingPlan plan : plans) {
                createTrainingPlanDetails(plan, challenges, random);
            }
        }
        
        return plans.size();
    }

    private void createTrainingPlanDetails(TrainingPlan plan, List<Challenges> challenges, Random random) {
        int days = plan.getDurationWeeks() * 7; // Total days
        List<TrainingPlanDetail> details = new ArrayList<>();
        
        for (int day = 1; day <= Math.min(days, 30); day++) { // Limit to 30 days max
            Challenges challenge = challenges.get(random.nextInt(challenges.size()));
            
            TrainingPlanDetail detail = new TrainingPlanDetail();
            detail.setTrainingPlan(plan);
            detail.setDayNumber(day);
            detail.setChallenge(challenge);
            detail.setSets(random.nextInt(3) + 2); // 2-4 sets
            detail.setReps(random.nextInt(20) + 10); // 10-30 reps
            
            details.add(detail);
        }
        
        trainingPlanDetailRepository.saveAll(details);
    }

    @Override
    @Transactional
    public int importDailyTrainingLogs(int count) {
        List<User> users = userRepository.findAll();
        List<TrainingPlan> plans = trainingPlanRepository.findAll();
        
        if (users.isEmpty() || plans.isEmpty()) {
            System.out.println("⚠️ Không có users hoặc training plans. Bỏ qua import daily logs.");
            return 0;
        }
        
        Random random = new Random();
        List<DailyTrainingLog> logs = new ArrayList<>();
        
        String[] statuses = {"completed", "in_progress", "not_started", "completed", "completed"}; // More completed
        
        for (int i = 0; i < count; i++) {
            User user = users.get(random.nextInt(users.size()));
            TrainingPlan plan = plans.get(random.nextInt(plans.size()));
            
            // Get challenges for this plan
            List<TrainingPlanDetail> details = trainingPlanDetailRepository.findByTrainingPlan_TpId(plan.getTpId());
            if (details.isEmpty()) {
                continue;
            }
            
            TrainingPlanDetail detail = details.get(random.nextInt(details.size()));
            Challenges challenge = detail.getChallenge();
            String status = statuses[random.nextInt(statuses.length)];
            
            LocalDate trainingDate = LocalDate.now().minusDays(random.nextInt(30)); // Last 30 days
            ZonedDateTime completedAt = null;
            
            if ("completed".equals(status)) {
                completedAt = ZonedDateTime.now().minusDays(random.nextInt(30));
            }
            
            DailyTrainingLog log = DailyTrainingLog.builder()
                .user(user)
                .trainingPlan(plan)
                .trainingDate(trainingDate)
                .dayNumber(detail.getDayNumber())
                .challenge(challenge)
                .status(status)
                .actualDurationMinutes(random.nextInt(60) + 15) // 15-75 minutes
                .caloriesBurned(random.nextInt(500) + 100) // 100-600 calories
                .setsCompleted(status.equals("completed") ? detail.getSets() : random.nextInt(detail.getSets()))
                .repsCompleted(status.equals("completed") ? detail.getReps() : random.nextInt(detail.getReps()))
                .score(status.equals("completed") ? random.nextInt(40) + 60 : null) // 60-100
                .confidence(status.equals("completed") ? 0.7 + random.nextDouble() * 0.3 : null) // 0.7-1.0
                .startedAt(completedAt != null ? completedAt.minusMinutes(random.nextInt(60) + 15) : null)
                .completedAt(completedAt)
                .createdAt(ZonedDateTime.now().minusDays(random.nextInt(30)))
                .build();
            
            logs.add(log);
        }
        
        dailyTrainingLogRepository.saveAll(logs);
        return logs.size();
    }

    @Override
    @Transactional
    public int importUserChallenges(int count) {
        List<User> users = userRepository.findAll();
        List<Challenges> challenges = challengeRepository.findAll();
        
        if (users.isEmpty() || challenges.isEmpty()) {
            System.out.println("⚠️ Không có users hoặc challenges. Bỏ qua import user challenges.");
            return 0;
        }
        
        Random random = new Random();
        List<UserChallenge> userChallenges = new ArrayList<>();
        
        String[] statuses = {"success", "pending", "failed", "success", "success"}; // More success
        
        for (int i = 0; i < count; i++) {
            User user = users.get(random.nextInt(users.size()));
            Challenges challenge = challenges.get(random.nextInt(challenges.size()));
            
            String status = statuses[random.nextInt(statuses.length)];
            ZonedDateTime submittedAt = ZonedDateTime.now().minusDays(random.nextInt(60));
            ZonedDateTime completedAt = null;
            
            if ("success".equals(status)) {
                completedAt = submittedAt.plusHours(random.nextInt(24) + 1);
            }
            
            UserChallenge userChallenge = UserChallenge.builder()
                .user(user)
                .challenge(challenge)
                .status(status)
                .videoUrl("https://example.com/video" + i + ".mp4")
                .score(status.equals("success") ? random.nextInt(40) + 60 : null)
                .confidence(status.equals("success") ? 0.7 + random.nextDouble() * 0.3 : null)
                .submittedAt(submittedAt)
                .completedAt(completedAt)
                .build();
            
            userChallenges.add(userChallenge);
        }
        
        userChallengeRepository.saveAll(userChallenges);
        
        // Update user points based on completed challenges
        updateUserPoints();
        
        return userChallenges.size();
    }

    private void importGoals() {
        String[] goalNames = {
            "Lose Weight", "Build Muscle", "Improve Endurance", 
            "Increase Flexibility", "General Fitness", "Athletic Performance"
        };
        
        String[] descriptions = {
            "Achieve your weight loss goals through structured training",
            "Build lean muscle mass and strength",
            "Improve cardiovascular endurance and stamina",
            "Enhance flexibility and mobility",
            "Maintain overall health and fitness",
            "Reach peak athletic performance"
        };
        
        List<Goals> goals = new ArrayList<>();
        for (int i = 0; i < goalNames.length; i++) {
            Goals goal = new Goals();
            goal.setName(goalNames[i]);
            goal.setDescription(descriptions[i]);
            goal.setImageLink("https://example.com/goal" + i + ".jpg");
            goals.add(goal);
        }
        
        goalRepository.saveAll(goals);
    }

    private void updateUserPoints() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            long completedChallenges = userChallengeRepository.findAll().stream()
                .filter(uc -> uc.getUser().getId().equals(user.getId()) && "success".equals(uc.getStatus()))
                .count();
            
            // Update points: 100 points per completed challenge
            user.setPoints((int) (completedChallenges * 100));
            userRepository.save(user);
        }
    }
}

