package com.geekbrains.springms.user.controllers;

import com.geekbrains.springms.api.AuthRequest;
import com.geekbrains.springms.api.AuthResponse;
import com.geekbrains.springms.api.UserDto;
import com.geekbrains.springms.api.UserRegisterRequest;
import com.geekbrains.springms.user.entities.User;
import com.geekbrains.springms.user.mappers.UserMapper;
import com.geekbrains.springms.user.services.UserService;
import com.geekbrains.springms.user.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Enumeration;

@RestController
public class UserController {

    private AuthenticationManager authenticationManager;

    private UserService userService;

    private JwtUtils jwtUtils;

    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setJwtUtils(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }



    @PostMapping("/auth")
    public AuthResponse authenticate(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getUsername(),
                        authRequest.getPassword()
                )
        );
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtUtils.generateToken(userDetails);
        return new AuthResponse(token);
    }



    @PostMapping("/register")
    public void registerUser(@RequestBody UserRegisterRequest registerRequest, HttpServletRequest request)
            throws Exception {

        boolean specialAuthority = hasSpecialAuthority(request);
        //admin must be logged in
        if (specialAuthority) {
            String username = checkAuthorizationHeaderOrThrowException(request);
        }
        userService.registerUser(registerRequest, specialAuthority);
    }



    @PostMapping("/update")
    public UserDto updateUserInfo(@RequestBody UserDto userDto, HttpServletRequest request) {
        String username = checkAuthorizationHeaderOrThrowException(request);
        boolean specialAuthority = hasSpecialAuthority(request);
        //user can change only himself
        if (!specialAuthority && !userDto.getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized access.");
        }
        User user = userService.updateUserInfo(userDto, specialAuthority);
        return UserMapper.MAPPER.toDto(user);
    }

    @GetMapping("/profile")
    public UserDto getUserInfo(HttpServletRequest request) {
        String username = checkAuthorizationHeaderOrThrowException(request);
        User user = userService.getUserInfoByName(username);
        return UserMapper.MAPPER.toDto(user);
    }

    @DeleteMapping("/payment")
    public void deleteUserBilling(@RequestParam Long id, HttpServletRequest request) {
        String username = checkAuthorizationHeaderOrThrowException(request);
        userService.deleteUserBilling(id, username);
    }

    @PostMapping("/change-password")
    public void changePassword(
            @RequestParam(required = false) String usernameToModify,
            @RequestParam(name = "old_password") String oldPassword,
            @RequestParam(name = "new_password") String newPassword,
            HttpServletRequest request
    )
    {
        String authorizedUsername = checkAuthorizationHeaderOrThrowException(request);
        boolean specialAuthority = hasSpecialAuthority(request);

        //user can change only his own password
        //admin and user can let field be null, username will be extracted from header anyway
        if (usernameToModify == null || !specialAuthority) {
            usernameToModify = authorizedUsername;
        }
        userService.changePassword(
                authorizedUsername, usernameToModify, oldPassword, newPassword, specialAuthority);
    }

    private boolean hasSpecialAuthority(HttpServletRequest request) {
        if (request.getHeaders("roles").hasMoreElements()) {
            Enumeration<String> roles = request.getHeaders("roles");
            while (roles.hasMoreElements()) {
                if (roles.nextElement().equals("ROLE_ADMIN")) {
                    return true;
                }
            }
        }
        return false;
    }

    private String checkAuthorizationHeaderOrThrowException(HttpServletRequest request) {
        String username = request.getHeader("username");
        if (username != null) {
            return username;
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized access.");
    }

}
