package com.ankit.BloodDonorFinder.service;

import com.ankit.BloodDonorFinder.dto.AuthResponse;
import com.ankit.BloodDonorFinder.dto.LoginRequest;
import com.ankit.BloodDonorFinder.dto.RegisterRequest;
import com.ankit.BloodDonorFinder.exception.BadRequestException;
import com.ankit.BloodDonorFinder.exception.ResourceNotFoundException;
import com.ankit.BloodDonorFinder.exception.UnauthorizedException;
import com.ankit.BloodDonorFinder.model.User;
import com.ankit.BloodDonorFinder.repository.UserRepository;
import com.ankit.BloodDonorFinder.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    //register
    public String register(RegisterRequest request) {

        // check email already exist
        if(userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already Registered!");
        }

        // check phone already exist
        if(userRepository.existsByPhone(request.getPhone())) {
            throw new RuntimeException("Phone already Registered!");
        }

        // create user object
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );
        user.setRole(User.Role.valueOf(request.getRole()));
        user.setCity(request.getCity());
        user.setState(request.getState());
        user.setPincode(request.getPincode());

        // save user to database
        userRepository.save(user);

        return "Registration successful!";
    }

    // login
    public AuthResponse login(LoginRequest request) {

        //find user by email
        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not Found!"));

        // check password
        if(!passwordEncoder.matches(
                request.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Wrong Password!");
        }

        // generate jwt token
        String token = jwtService.generateToken(user.getEmail());

        // return token and user details
        return new AuthResponse(
                token,
                user.getName(),
                user.getEmail(),
                user.getRole().name()
        );
    }
}
