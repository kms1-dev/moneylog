package org.example.moneylog.service;

import lombok.RequiredArgsConstructor;
import org.example.moneylog.domain.User;
import org.example.moneylog.dto.LoginRequest;
import org.example.moneylog.dto.LoginResponse;
import org.example.moneylog.dto.SignupRequest;
import org.example.moneylog.dto.SignupResponse;
import org.example.moneylog.exception.DuplicateEmailException;
import org.example.moneylog.exception.InvalidCredentialsException;
import org.example.moneylog.repository.UserRepository;
import org.example.moneylog.security.JwtTokenProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final CategoryService categoryService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public SignupResponse signup(SignupRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new DuplicateEmailException();
        }
        User user = new User(
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getNickname());
        userRepository.save(user);
        categoryService.createDefaultCategories(user);
        return new SignupResponse(user);
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(InvalidCredentialsException::new);
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }
        String token = jwtTokenProvider.createToken(user.getId());
        return new LoginResponse(token);
    }
}
