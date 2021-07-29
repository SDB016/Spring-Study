package study.datajpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.Entity.Member;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {

    @Autowired MemberJpaRepository memberJpaRepository;

    @Test
    void testMember() {
        Member member = new Member("dongbin");
        Member savedMember = memberJpaRepository.save(member);

        Member findMember = memberJpaRepository.find(savedMember.getId());

        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
    }

    @Test
    void basicCRUD() {
        Member memberA = new Member("dongbin");
        Member memberB = new Member("ehdqls");

        memberJpaRepository.save(memberA);
        memberJpaRepository.save(memberB);

        //단건 조회 검증
        Member findA = memberJpaRepository.findById(memberA.getId()).get();
        Member findB = memberJpaRepository.findById(memberB.getId()).get();
        assertThat(findA).isEqualTo(memberA);
        assertThat(findB).isEqualTo(memberB);

        //리스트 조회 검증
        List<Member> all = memberJpaRepository.findAll();
        assertTrue(all.contains(memberA));
        assertTrue(all.contains(memberB));
        assertThat(all.size()).isEqualTo(2);

        //카운트 검증
        long count = memberJpaRepository.count();
        assertThat(count).isEqualTo(2);

        //삭제 검증
        memberJpaRepository.delete(memberA);
        long deletedCount = memberJpaRepository.count();
        assertThat(deletedCount).isEqualTo(1);
    }

    @Test
    void testNamedQuery() {
        Member memberA = new Member("dongbin", 10);
        Member memberB = new Member("dongbin", 30);

        memberJpaRepository.save(memberA);
        memberJpaRepository.save(memberB);

        List<Member> memberList = memberJpaRepository.findByUsername("dongbin");

        assertThat(memberList.size()).isEqualTo(2);
        assertThat(memberList.get(0).getAge()).isEqualTo(10);
    }
}