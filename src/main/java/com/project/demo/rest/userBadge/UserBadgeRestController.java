package com.project.demo.rest.userBadge;


import com.project.demo.logic.entity.badge.Badge;
import com.project.demo.logic.entity.userbadge.UserBadge;
import com.project.demo.logic.entity.userbadge.UserBadgeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/userbadge")
public class UserBadgeRestController {

    @Autowired
    private UserBadgeRepository userBadgeRepository;


    @GetMapping("/{id}")
    public List<Badge> getUserBadgeById(@RequestParam Long userID) {
        return userBadgeRepository.findByUserId(userID);
    }

    @GetMapping
    public List<UserBadge> getAllUserBadge() {
        return userBadgeRepository.findAll();
    }
}
