package com.db.dsg.controller;

import com.db.dsg.dtos.AuthRequest;
import com.db.dsg.model.Member;
import com.db.dsg.model.MemberUser;
import com.db.dsg.repository.MemberRepository;
import com.db.dsg.repository.MemberUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final MemberUserRepository userRepo;
    private final MemberRepository memberRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @PostMapping("/signup")
    public ResponseEntity<?> register(@RequestBody AuthRequest req) {
        if (userRepo.existsByUsername(req.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
        }

        // Create member profile
        Member member = new Member();
        member.setName(req.getFullName());
        member.setPhone(req.getPhone());
        member = memberRepo.save(member);

        // Create user account linked to member
        MemberUser user = new MemberUser();
        user.setUsername(req.getUsername());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setRole(req.getRole());
        user.setMember(member);

        member.setUser(user); // bi-directional sync
        userRepo.save(user);

        return ResponseEntity.ok(new AuthResponse(jwtService.generateToken(user)));
    }
}