package com.hansanhha.spring.security.oauth2.client;

import com.hansanhha.spring.security.oauth2.client.vo.KaKaoOAuth2Attributes;
import com.hansanhha.spring.security.oauth2.client.vo.OAuth2Attributes;
import com.hansanhha.spring.security.user.entity.Member;
import com.hansanhha.spring.security.user.repository.MemberRepository;
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

        if (registrationId.equals("kakao")) {
            oAuth2Attributes = KaKaoOAuth2Attributes.create(oauth2User.getAttributes());
        } else {
            throw new IllegalArgumentException("지원하지 않는 OAuth2 인증입니다.");
        }

        Optional<Member> findUser = memberRepository.findByEmail(oAuth2Attributes.getEmail());

        OAuth2User loginUser = convertAndCreateIfFirst(findUser, oAuth2Attributes, userRequest);

        return loginUser;
    }

    private OAuth2User convertAndCreateIfFirst(Optional<Member> findUser, OAuth2Attributes oAuth2Attributes, OAuth2UserRequest userRequest) {
        if (findUser.isPresent()) {
            return ClientOAuth2User.create(findUser.get());
        }

        String nickname = UUID.randomUUID().toString().substring(0, 12);
        Member member = Member.createNormalUser(nickname, oAuth2Attributes.getEmail(), userRequest.getClientRegistration().getRegistrationId(), oAuth2Attributes.getUserNumber());
        memberRepository.save(member);
        return ClientOAuth2User.create(member);
    }
}
