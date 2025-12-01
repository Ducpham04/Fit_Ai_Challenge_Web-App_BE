package com.example.FIT_Challenge.controller.Admin;



import com.example.FIT_Challenge.DTO.FoodDTO.FoodRequest;
import com.example.FIT_Challenge.service.FoodService;
import com.example.FIT_Challenge.config.NotificationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FoodController {

    private final FoodService foodService;

    @PostMapping("/admin/foods")
    public NotificationResponse createFood(@RequestBody FoodRequest request){
        System.out.println("Received Food Request: " + request);
        return foodService.createFood(request);
    }

    @PutMapping("/admin/foods/{id}")
    public NotificationResponse updateFood(@PathVariable Long id, @RequestBody FoodRequest request){
        return foodService.updateFood(id, request);
    }

    @DeleteMapping("/admin/foods/{id}")
    public NotificationResponse deleteFood(@PathVariable Long id){
        return foodService.deleteFood(id);
    }

    @GetMapping("/admin/foods/{id}")
    public NotificationResponse getFoodById(@PathVariable Long id){
        return foodService.getFoodById(id);
    }

    @GetMapping("/foods")
    public NotificationResponse getAllFoods(){
        return foodService.getAllFoods();
    }
}
