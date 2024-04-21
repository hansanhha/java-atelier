package com.hansanhha.spring.oauth2.security;

import com.hansanhha.spring.oauth2.user.Member;
import com.hansanhha.spring.oauth2.user.MemberRepository;
import com.hansanhha.spring.oauth2.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Optional;
import java.util.UUID;

@UserService
@RequiredArgsConstructor
@Slf4j
public class ClientOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2Attributes oAuth2Attributes;
        OAuth2User oauth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        
        log.info("registrationId :" + registrationId);
        log.info("===user attributes===");
        oauth2User.getAttributes().forEach((k, v) -> {
            log.info(k + " : " + v);
        });

        if (registrationId.equals("kakao")) {
            oAuth2Attributes = KaKaoOAuth2Attributes.create(oauth2User.getAttributes());
        } else {
            throw new IllegalArgumentException("지원하지 않는 OAuth2 인증입니다.");
        }

        Optional<Member> findUser = memberRepository.findByEmail(oAuth2Attributes.getEmail());

        if (findUser.isEmpty()) {
            String nickname = UUID.randomUUID().toString().substring(0, 12);
            Member member = Member.createNormalUser(nickname, oAuth2Attributes.getEmail(), userRequest.getClientRegistration().getRegistrationId(), oAuth2Attributes.getUserNumber());
            memberRepository.save(member);
            return OAuth2UserDetails.create(member);
        }

        return OAuth2UserDetails.create(findUser.get());
    }
}
