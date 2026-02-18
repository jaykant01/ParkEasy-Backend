package com.jay.ParkEasy_Backend.oauth;

import com.jay.ParkEasy_Backend.entity.Role;
import com.jay.ParkEasy_Backend.entity.UserEntity;
import com.jay.ParkEasy_Backend.jwt.JwtService;
import com.jay.ParkEasy_Backend.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException {

        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        Map<String, Object> attributes = oauthToken.getPrincipal().getAttributes();

        String email = attributes.get("email").toString();
        String name = attributes.get("name").toString();

        UserEntity user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            user = UserEntity.builder()
                    .email(email)
                    .name(name)
                    .role(Role.USER)
                    .password(null)
                    .build();
        } else {
            if (!user.getName().equals(name)) {
                user.setName(name);
            }
        }

        user = userRepository.save(user);

        String jwtToken = jwtService.generateToken(user);

        // Always use new redirect
        String redirectUrl = "http://localhost:4200/home-page?token=" + jwtToken;

        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}
