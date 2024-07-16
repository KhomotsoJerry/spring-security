package com.security.Spring_Security.authenticationController;

import com.security.Spring_Security.configAuth.JwtService;
import com.security.Spring_Security.student.Role;
import com.security.Spring_Security.student.Student;
import com.security.Spring_Security.studentRepo.StudentRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

@Data
@AllArgsConstructor
@Builder
public class AuthenticationService {
   private final PasswordEncoder passwordEncoder;
   private final StudentRepository repository;
   private final JwtService jwtService;
   private final AuthenticationManager authenticationManager;
    public AuthenticationResponse register(Register request) {
        var user = Student.builder()
                .email(request.getEmail())
                .lastname(request.getLastname())
                .firstname(request.getFirstname())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        repository.save(user);
        var jwt = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwt)
                .build();
    }

    public AuthenticationResponse authenticate(Authenticate response) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(response.getPassword(),response.getEmail())
        );
        var user = repository.findStudentByEmail(response.getEmail())
                .orElseThrow(()-> new UsernameNotFoundException("user not found"));
        var jwt = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwt)
                .build();
    }
}
