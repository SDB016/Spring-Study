package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.Entity.Member;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;


    @Test
    void testMember() {
        Member member = new Member("dongbin");
        Member savedMember = memberRepository.save(member);

        Member findMember = memberRepository.findById(savedMember.getId()).get();

        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
    }

    @Test
    void basicCRUD() {
        Member memberA = new Member("dongbin");
        Member memberB = new Member("ehdqls");

        memberRepository.save(memberA);
        memberRepository.save(memberB);

        //단건 조회 검증
        Member findA = memberRepository.findById(memberA.getId()).get();
        Member findB = memberRepository.findById(memberB.getId()).get();
        assertThat(findA).isEqualTo(memberA);
        assertThat(findB).isEqualTo(memberB);

        //리스트 조회 검증
        List<Member> all = memberRepository.findAll();
        assertTrue(all.contains(memberA));
        assertTrue(all.contains(memberB));
        assertThat(all.size()).isEqualTo(2);

        //카운트 검증
        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        //삭제 검증
        memberRepository.delete(memberA);
        long deletedCount = memberRepository.count();
        assertThat(deletedCount).isEqualTo(1);
    }

    @Test
    void findByUsernameAndAgeGreaterThen() {
        Member m1 = new Member("aaa", 10);
        Member m2 = new Member("aaa", 20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> memberList = memberRepository.findByUsernameAndAgeGreaterThan("aaa", 15);

        assertThat(memberList.get(0).getUsername()).isEqualTo("aaa");
        assertThat(memberList.get(0).getAge()).isEqualTo(20);
        assertThat(memberList.size()).isEqualTo(1);
    }
    @Test
    void testNamedQuery() {
        Member memberA = new Member("dongbin", 10);
        Member memberB = new Member("dongbin", 30);

        memberRepository.save(memberA);
        memberRepository.save(memberB);

        List<Member> memberList = memberRepository.findByUsername("dongbin");

        assertThat(memberList.size()).isEqualTo(2);
        assertThat(memberList.get(0).getAge()).isEqualTo(10);
    }
}