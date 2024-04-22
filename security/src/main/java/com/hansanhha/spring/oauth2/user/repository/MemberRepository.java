package com.hansanhha.spring.oauth2.user.repository;

import com.hansanhha.spring.oauth2.user.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String Email);
}
