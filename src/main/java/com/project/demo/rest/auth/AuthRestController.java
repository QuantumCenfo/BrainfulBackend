package com.project.demo.rest.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.demo.logic.entity.Azure.AzureBlobService;
import com.project.demo.logic.entity.auth.AuthenticationService;
import com.project.demo.logic.entity.auth.JwtService;
import com.project.demo.logic.entity.rol.Role;
import com.project.demo.logic.entity.rol.RoleEnum;
import com.project.demo.logic.entity.rol.RoleRepository;
import com.project.demo.logic.entity.user.LoginResponse;
import com.project.demo.logic.entity.user.User;
import com.project.demo.logic.entity.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RequestMapping("/auth")
@RestController
public class AuthRestController {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private AzureBlobService azureBlobAdapter;



    private final AuthenticationService authenticationService;
    private final JwtService jwtService;

    public AuthRestController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody User user) {
        User authenticatedUser = authenticationService.authenticate(user);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(jwtToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());

        Optional<User> foundedUser = userRepository.findByEmail(user.getEmail());

        foundedUser.ifPresent(loginResponse::setAuthUser);

        return ResponseEntity.ok(loginResponse);
    }

//    @PostMapping("/signup")
//    public ResponseEntity<?> registerUser(@RequestBody User user) {
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.USER);
//
//        if (optionalRole.isEmpty()) {
//            return null;
//        }
//        user.setRole(optionalRole.get());
//        User savedUser = userRepository.save(user);
//        return ResponseEntity.ok(savedUser);
//    }

    @PostMapping(
            value = "/signup",
            consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE }
    )
    public ResponseEntity<?> registerUser(
            // used when Content-Type is application/json
            @RequestBody(required = false) User userBody,

            // used when Content-Type is multipart/form-data
            @RequestPart(value = "user", required = false) String userJson,
            @RequestPart(value = "image", required = false) MultipartFile imageUser
    ) throws IOException {

        User user;
        if (userBody != null) {
            user = userBody; // JSON flow
        } else if (userJson != null) {
            user = new ObjectMapper().readValue(userJson, User.class); // multipart flow
        } else {
            return ResponseEntity.badRequest().body("Missing user payload");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role role = roleRepository.findByName(RoleEnum.USER)
                .orElseThrow(() -> new IllegalStateException("Role USER not found"));
        user.setRole(role);

        if (imageUser != null && !imageUser.isEmpty()) {
            String imageUrl = azureBlobAdapter.upload(imageUser);
            user.setImage(imageUrl);
        }

        return ResponseEntity.ok(userRepository.save(user));
    }

}