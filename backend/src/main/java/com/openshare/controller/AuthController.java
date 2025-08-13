package com.openshare.controller;


import org.springframework.web.bind.annotation.*;

import com.openshare.models.User;
import com.openshare.repos.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import jakarta.servlet.http.HttpSession;
import java.util.Map;



@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class AuthController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public AuthController(UserRepository userRepository) { this.userRepository = userRepository; }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String,String> body) {
        String username = body.get("username");
        String password = body.get("password");
        
        if (username == null || password == null) 
        	return ResponseEntity.badRequest().body("Missing fields!");
        
        if (userRepository.existsByUsername(username)) 
        	return ResponseEntity.badRequest().body("Username exists!");
        
        User u = new User();
        u.setUsername(username);
        u.setPasswordHash(encoder.encode(password));
        userRepository.save(u);
        return ResponseEntity.ok(Map.of("status","ok"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String,String> body, HttpSession session) {
        String username = body.get("username");
        String password = body.get("password");
        if (username == null || password == null)
        	return ResponseEntity.badRequest().body("Missing fields");
        
        var opt = userRepository.findByUsername(username);
        if (opt.isEmpty())
        	return ResponseEntity.status(403).body("Invalid credentials");
        
        User u = opt.get();
        if (!encoder.matches(password, u.getPasswordHash()))
        	return ResponseEntity.status(403).body("Invalid credentials");
        
        session.setAttribute("userId", u.getId());
        session.setAttribute("username", u.getUsername());
        
        return ResponseEntity.ok(Map.of("status","ok","username",u.getUsername(),"userId",u.getId()));
    }
    
    @GetMapping("/check-session")
    public ResponseEntity<?> checkSession(HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        // System.out.println(userId);
        
        if (userId == null || userId == "") {
        	return ResponseEntity.ok(Map.of("loggedIn", false));
        }
        
    	var opt = userRepository.findById(userId);
    	// System.out.println(opt);
    	if (opt.isEmpty())
    		return ResponseEntity.ok(Map.of("loggedIn", false));
    	
		User u = opt.get();
    	return ResponseEntity.ok(Map.of(
			"loggedIn", true,
			"username", u.getUsername()
		));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok(Map.of("status","ok"));
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(HttpSession session) {
        Object uid = session.getAttribute("userId");
        Object uname = session.getAttribute("username");
        if (uid==null) return ResponseEntity.status(401).body(Map.of("authenticated", false));
        return ResponseEntity.ok(Map.of("authenticated", true, "userId", uid, "username", uname));
    }
}
