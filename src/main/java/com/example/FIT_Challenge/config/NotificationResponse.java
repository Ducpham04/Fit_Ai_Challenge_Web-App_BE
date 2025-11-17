package com.example.FIT_Challenge.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponse {

    private boolean success  ;
    private String message ;
    private Object data ;

    public NotificationResponse(boolean b, String goalDataCannotBeNull) {
        this.success = b ;
        this.message = goalDataCannotBeNull ;
    }
}
