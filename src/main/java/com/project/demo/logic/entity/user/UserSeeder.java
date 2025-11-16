package com.project.demo.logic.entity.user;



import com.project.demo.logic.entity.rol.Role;
import com.project.demo.logic.entity.rol.RoleEnum;
import com.project.demo.logic.entity.rol.RoleRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserSeeder implements ApplicationListener<ContextRefreshedEvent> {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public UserSeeder(
            UserRepository userRepository
            , PasswordEncoder passwordEncoder,

            RoleRepository roleRepository){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        this.createSuperAdmin();
        this.createAdmin();
        this.createUser();
    }

    private void createSuperAdmin() {

        User admin = new User();
        admin.setName("SuperAdmin01");
        admin.setLastname("System");
        admin.setEmail("SuperAdmin01@gmail.com");
        admin.setPassword("SuperAdmin");

        Optional<Role> optionalAdminRole = roleRepository.findByName(RoleEnum.SUPER_ADMIN);
        Optional<User> optionalAdminUser = userRepository.findByEmail(admin.getEmail());

        if (optionalAdminRole.isEmpty() || optionalAdminUser.isPresent())  {
            return;
        }

        var newASuperAdmin = new User();
        newASuperAdmin.setName(admin.getName());
        newASuperAdmin.setLastname(admin.getLastname());
        newASuperAdmin.setEmail(admin.getEmail());
        newASuperAdmin.setPassword(passwordEncoder.encode(admin.getPassword()));
        newASuperAdmin.setRole(optionalAdminRole.get());

        userRepository.save(newASuperAdmin);
    }

    private void createAdmin() {

        User admin = new User();
        admin.setName("Admin01");
        admin.setLastname("System");
        admin.setEmail("Admin01@gmail.com");
        admin.setPassword("Admin");

        Optional<Role> optionalAdminRole = roleRepository.findByName(RoleEnum.ADMIN);
        Optional<User> optionalAdminUser = userRepository.findByEmail(admin.getEmail());

        if (optionalAdminRole.isEmpty() || optionalAdminUser.isPresent())  {
            return;
        }

        var newAdmin = new User();
        newAdmin.setName(admin.getName());
        newAdmin.setLastname(admin.getLastname());
        newAdmin.setEmail(admin.getEmail());
        newAdmin.setPassword(passwordEncoder.encode(admin.getPassword()));
        newAdmin.setRole(optionalAdminRole.get());

        userRepository.save(newAdmin);
    }

    private void createUser() {

        User user = new User();
        user.setName("User01");
        user.setLastname("System");
        user.setEmail("User01@gmail.com");
        user.setPassword("User");

        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.USER);
        Optional<User> optionalUser = userRepository.findByEmail(user.getEmail());

        if (optionalRole.isEmpty() || optionalUser.isPresent())  {
            return;
        }

        var newUser = new User();
        newUser.setName(user.getName());
        newUser.setLastname(user.getLastname());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setRole(optionalRole.get());

        userRepository.save(newUser);
    }
}
