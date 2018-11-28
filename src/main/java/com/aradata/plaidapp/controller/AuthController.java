package com.aradata.plaidapp.controller;

import com.aradata.plaidapp.exception.EmailAlreadyExistsException;
import com.aradata.plaidapp.exception.UsernameAlreadyExistsException;
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
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.ValidationException;
import javax.xml.ws.Response;
import java.net.URI;
import java.util.Collections;
import java.util.List;

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
			throw new UsernameAlreadyExistsException();
		}

		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			throw new EmailAlreadyExistsException();
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

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ResponseEntity validationError(MethodArgumentNotValidException ex) {
		BindingResult result = ex.getBindingResult();
		final List<FieldError> fieldErrors = result.getFieldErrors();

		throw new ValidationException((fieldErrors.get(0).getField() + " " +
				fieldErrors.get(0).getDefaultMessage()));
	}
}
