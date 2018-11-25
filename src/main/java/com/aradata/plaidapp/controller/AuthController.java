package com.aradata.plaidapp.controller;

import com.aradata.plaidapp.model.payloads.*;
import com.aradata.plaidapp.model.user.AppUser;
import com.aradata.plaidapp.model.user.Role;
import com.aradata.plaidapp.repository.AppUserRepository;
import com.aradata.plaidapp.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.net.URI;
import java.util.Collections;
import java.util.HashSet;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	AppUserRepository userRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	JwtTokenProvider tokenProvider;

//	@PostConstruct
//	public void init() {
//		userRepository.deleteAll();
//		AppUser admin = new AppUser("admin", "admin", "test@mail.ru", "rodnyan");
//		admin.setPassword(passwordEncoder.encode("rodnyan"));
//		HashSet<Role> roles = new HashSet<>();
//		roles.add(Role.ROLE_USER);
//		roles.add(Role.ROLE_ADMIN);
//		admin.setRoles(roles);
//		userRepository.save(admin);
//	}

	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						loginRequest.getUsernameOrEmail(),
						loginRequest.getPassword()
				)
		);

		SecurityContextHolder.getContext().setAuthentication(authentication);

		String jwt = tokenProvider.generateToken(authentication);
		return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
	}


	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return new ResponseEntity(new ErrorResponse(new ErrorObject(400, "Username is already taken")),
					HttpStatus.BAD_REQUEST);
		}

		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return new ResponseEntity(new ErrorResponse(new ErrorObject(400, "Email Address already in use!")),
					HttpStatus.BAD_REQUEST);
		}

		// Creating user's account
		AppUser user = new AppUser(signUpRequest.getName(), signUpRequest.getUsername(),
				signUpRequest.getEmail(), signUpRequest.getPassword());

		user.setPassword(passwordEncoder.encode(user.getPassword()));

		Role userRole = Role.ROLE_USER;
		user.setRoles(Collections.singleton(userRole));

		AppUser result = userRepository.save(user);

		URI location = ServletUriComponentsBuilder
				.fromCurrentContextPath().path("/api/users/{username}")
				.buildAndExpand(result.getUsername()).toUri();

		return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully", 201));
	}
}
