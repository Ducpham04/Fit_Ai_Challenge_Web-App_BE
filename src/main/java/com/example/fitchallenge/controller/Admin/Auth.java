package com.example.fitchallenge.controller.Admin;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class Auth {

    @PostMapping
    public String auth(@RequestBody String user){
        return user;
    }
}
