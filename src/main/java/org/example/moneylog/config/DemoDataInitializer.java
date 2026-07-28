package org.example.moneylog.config;

import lombok.RequiredArgsConstructor;
import org.example.moneylog.domain.User;
import org.example.moneylog.repository.UserRepository;
import org.example.moneylog.service.CategoryService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

// 아직 회원가입 API가 없는 2일차 테스트용 임시 사용자 생성기입니다.
// 3일차에 회원가입 API가 생기면 이 클래스는 지워도 됩니다.
@Component
@RequiredArgsConstructor
public class DemoDataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CategoryService categoryService;

    @Override
    public void run(String... args) {
        if (userRepository.findByEmail("demo@moneylog.com").isPresent()) {
            return;
        }
        User demoUser = new User("demo@moneylog.com", "demo-password-placeholder", "데모유저");
        userRepository.save(demoUser);
        categoryService.createDefaultCategories(demoUser);
    }
}
