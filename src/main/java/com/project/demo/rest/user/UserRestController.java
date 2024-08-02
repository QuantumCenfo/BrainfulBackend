package com.project.demo.rest.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.demo.logic.entity.Azure.AzureBlobService;
import com.project.demo.logic.entity.rol.Role;
import com.project.demo.logic.entity.rol.RoleEnum;
import com.project.demo.logic.entity.rol.RoleRepository;
import com.project.demo.logic.entity.user.User;
import com.project.demo.logic.entity.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserRestController {
    @Autowired
    private UserRepository UserRepository;

    @Autowired
    private AzureBlobService azureBlobAdapter;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public List<User> getAllUsers() {
        return UserRepository.findAll();
    }

    @PostMapping
    public User addUser(@RequestPart("user") String userJson, @RequestPart(value = "image", required = false) MultipartFile imageUser) throws IOException {
        System.out.println(userJson);
        ObjectMapper objectMapper = new ObjectMapper();
        User user = objectMapper.readValue(userJson, User.class);

        String image = azureBlobAdapter.upload(imageUser);
        if(imageUser!=null && !imageUser.isEmpty()){
            user.setImage(image);
        }else{
            user.setImage(image);
        }


        return UserRepository.save(user);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return UserRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    @GetMapping("/filterByName/{name}")
    public List<User> getUserById(@PathVariable String name) {
        return UserRepository.findUsersWithCharacterInName(name);
    }

    @PutMapping("/{id}")
    public User updateUser(
            @PathVariable Long id,
            @RequestPart("user") String userJson,
            @RequestPart(value = "image", required = false) MultipartFile imageUser) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        User user = objectMapper.readValue(userJson, User.class);



        String image = imageUser != null ? azureBlobAdapter.upload(imageUser) : null;
        if (image != null) {
            user.setImage(image);
        }

        return UserRepository.findById(id)
                .map(existingUser -> {
                    existingUser.setName(user.getName());
                    existingUser.setLastname(user.getLastname());
                    existingUser.setEmail(user.getEmail());
                    existingUser.setImage(user.getImage());
                    existingUser.setRole(user.getRole());

                    return UserRepository.save(existingUser);
                })
                .orElseGet(() -> {
                    user.setId(id);
                    return UserRepository.save(user);
                });
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        UserRepository.deleteById(id);
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public User authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }

}