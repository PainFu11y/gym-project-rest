package com.gym_project.controller;

import com.gym_project.constants.RoutConstants;
import com.gym_project.security.LoginService;

import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(RoutConstants.BASE_URL + RoutConstants.AUTH)
@Api(tags = "Authentication")
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping("/login")
    @ApiOperation(value = "Login", notes = "Authenticates a user by username and password")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Login successful"),
            @ApiResponse(code = 401, message = "Invalid username or password")
    })
    public ResponseEntity<Void> login(
            @ApiParam(required = true)
            @RequestParam String username,
            @ApiParam(required = true)
            @RequestParam String password
    ) {
        loginService.login(username, password);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/change-password")
    @ApiOperation(value = "Change password", notes = "Changes the password for an existing user")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Password changed successfully"),
            @ApiResponse(code = 401, message = "Invalid username or old password")
    })
    public ResponseEntity<Void> changePassword(
            @ApiParam(required = true)
            @RequestParam String username,
            @ApiParam(required = true)
            @RequestParam String oldPassword,
            @ApiParam(required = true)
            @RequestParam String newPassword
    ) {
        loginService.changePassword(username, oldPassword, newPassword);
        return ResponseEntity.ok().build();
    }
}