package com.example.fitchallenge.controller.admin;



import com.example.fitchallenge.dto.fooddto.FoodRequest;
import com.example.fitchallenge.service.FoodService;
import com.example.fitchallenge.config.NotificationResponse;
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
