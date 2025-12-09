package com.example.fitchallenge.service;

/**
 * Service để import dữ liệu mẫu vào database
 */
public interface DataSeederService {
    
    /**
     * Import toàn bộ dữ liệu mẫu
     * @return Số lượng records đã tạo
     */
    DataSeederResult importAllData();
    
    /**
     * Import chỉ users
     */
    int importUsers(int count);
    
    /**
     * Import chỉ challenges
     */
    int importChallenges(int count);
    
    /**
     * Import chỉ training plans
     */
    int importTrainingPlans(int count);
    
    /**
     * Import daily training logs
     */
    int importDailyTrainingLogs(int count);
    
    /**
     * Import user challenges
     */
    int importUserChallenges(int count);
    
    /**
     * Result class cho import operation
     */
    class DataSeederResult {
        private int usersCreated;
        private int challengesCreated;
        private int trainingPlansCreated;
        private int dailyLogsCreated;
        private int userChallengesCreated;
        private int totalRecords;
        
        // Getters and setters
        public int getUsersCreated() { return usersCreated; }
        public void setUsersCreated(int usersCreated) { this.usersCreated = usersCreated; }
        
        public int getChallengesCreated() { return challengesCreated; }
        public void setChallengesCreated(int challengesCreated) { this.challengesCreated = challengesCreated; }
        
        public int getTrainingPlansCreated() { return trainingPlansCreated; }
        public void setTrainingPlansCreated(int trainingPlansCreated) { this.trainingPlansCreated = trainingPlansCreated; }
        
        public int getDailyLogsCreated() { return dailyLogsCreated; }
        public void setDailyLogsCreated(int dailyLogsCreated) { this.dailyLogsCreated = dailyLogsCreated; }
        
        public int getUserChallengesCreated() { return userChallengesCreated; }
        public void setUserChallengesCreated(int userChallengesCreated) { this.userChallengesCreated = userChallengesCreated; }
        
        public int getTotalRecords() { return totalRecords; }
        public void setTotalRecords(int totalRecords) { this.totalRecords = totalRecords; }
    }
}

