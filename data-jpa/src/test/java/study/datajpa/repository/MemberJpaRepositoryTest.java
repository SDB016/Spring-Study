package study.datajpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.Entity.Member;
import study.datajpa.Entity.Team;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {

    @Autowired MemberJpaRepository memberJpaRepository;
    @Autowired TeamRepository teamRepository;

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
    void findByUsernameAndAgeGreaterThen() {
        Member m1 = new Member("aaa", 10);
        Member m2 = new Member("aaa", 20);

        memberJpaRepository.save(m1);
        memberJpaRepository.save(m2);

        List<Member> memberList = memberJpaRepository.findByUsernameAndAgeGreaterThan("aaa", 15);

        assertThat(memberList.get(0).getUsername()).isEqualTo("aaa");
        assertThat(memberList.get(0).getAge()).isEqualTo(20);
        assertThat(memberList.size()).isEqualTo(1);
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

    @Test
    void Paging() {
        memberJpaRepository.save(new Member("member1", 20));
        memberJpaRepository.save(new Member("member2", 20));
        memberJpaRepository.save(new Member("member3", 20));
        memberJpaRepository.save(new Member("member4", 20));
        memberJpaRepository.save(new Member("member5", 20));

        int age = 20;
        int offset = 0;
        int limit = 3;

        List<Member> memberList = memberJpaRepository.findByPage(age, offset, limit);
        long totalCount = memberJpaRepository.totalCount(age);

        assertThat(memberList.size()).isEqualTo(3);
        assertEquals(5, totalCount);
    }

    @Test
    void bulkUpdate() {
        for (int i = 0; i < 5; i++) {
            memberJpaRepository.save(new Member("member" + i, 17 + i));
        }
        int resultCount = memberJpaRepository.bulkAgePlus(20);

        assertEquals(2, resultCount);
    }
}