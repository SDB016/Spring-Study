package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.Entity.Member;
import study.datajpa.Entity.Team;
import study.datajpa.dto.MemberDto;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;
    @Autowired TeamRepository teamRepository;
    @PersistenceContext EntityManager em;


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

    @Test
    void testQuery() {
        Member memberA = new Member("dongbin", 10);
        Member memberB = new Member("dongbin", 30);

        memberRepository.save(memberA);
        memberRepository.save(memberB);

        List<Member> memberList = memberRepository.findMember("dongbin", 10);

        assertThat(memberList.size()).isEqualTo(1);
        assertThat(memberList.get(0)).isEqualTo(memberA);
    }

    @Test
    void testFindUsernameList() {
        Member memberA = new Member("dongbin", 10);
        Member memberB = new Member("ehdqls", 30);

        memberRepository.save(memberA);
        memberRepository.save(memberB);

        List<String> usernameList = memberRepository.findUsernameList();

        assertThat(usernameList.size()).isEqualTo(2);
        assertThat(usernameList.get(0)).isEqualTo("dongbin");
    }

    @Test
    void testFindMemberDto() {
        Team team = new Team("teamA");

        teamRepository.save(team);

        Member memberA = new Member("dongbin", 10, team);
        Member memberB = new Member("ehdqls", 30, team);

        memberRepository.save(memberA);
        memberRepository.save(memberB);

        List<MemberDto> memberDtoList = memberRepository.findMemberDto();

        for (int i = 0; i < 2; i++) {
            assertThat(memberDtoList.get(i).getTeamname()).isEqualTo(team.getName());
        }
    }

    @Test
    void testFindMembersByNames() {
        Member memberA = new Member("dongbin");
        Member memberB = new Member("ehdqls");

        memberRepository.save(memberA);
        memberRepository.save(memberB);

        List<Member> memberList = memberRepository.findMembersByNames(Arrays.asList("dongbin", "ehdqls"));

        assertThat(memberList.size()).isEqualTo(2);
        assertThat(memberList.get(0)).isEqualTo(memberA);
        assertThat(memberList.get(1)).isEqualTo(memberB);
    }

    @Test
    void testReturnType() {
        Member memberA = new Member("dongbin",10);
        Member memberB = new Member("dongbin",20);
        Member memberC = new Member("ehdqls",20);

        memberRepository.save(memberA);
        memberRepository.save(memberB);
        memberRepository.save(memberC);

        List<Member> memberList = memberRepository.findListByUsername("dongbin");
        assertThat(memberList.size()).isEqualTo(2);

        Member member = memberRepository.findMemberByUsername("ehdqls");
        assertThat(member).isEqualTo(memberC);

        Optional<Member> optionalMember = memberRepository.findOptionalByUsername("ehdqls");
        if (optionalMember.isPresent()) {
            Member optionalOne = optionalMember.get();
            assertThat(optionalOne).isEqualTo(memberC);
        }
    }

    @Test
    void testReturnType_abnormal_input(){
        Member memberA = new Member("dongbin",10);
        Member memberB = new Member("dongbin",20);
        Member memberC = new Member("ehdqls",20);

        memberRepository.save(memberA);
        memberRepository.save(memberB);
        memberRepository.save(memberC);

        List<Member> memberList = memberRepository.findListByUsername("1234");
        assertNotNull(memberList);
        assertThat(memberList.size()).isEqualTo(0);

        Member member = memberRepository.findMemberByUsername("1234");
        assertNull(member);

        assertThrows(IncorrectResultSizeDataAccessException.class, () -> {
            Optional<Member> memberOptional = memberRepository.findOptionalByUsername("dongbin");
        });
    }

    @Test
    void Paging() {

        for (int i = 0; i < 5; i++) {
            memberRepository.save(new Member("member" + i, 20));
        }

        int age = 20;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        Page<Member> page = memberRepository.findPageByAge(age, pageRequest);

        //Page<MemberDto> dtoPage = page.map(m -> new MemberDto(m.getId(), m.getUsername(), null)); // api 반환용

        List<Member> content = page.getContent();
        long totalElements = page.getTotalElements();

        assertEquals(3, content.size());
        assertEquals(5, totalElements);
        assertEquals(2, page.getTotalPages());
        assertTrue(page.isFirst());
        assertTrue(page.hasNext());
        assertFalse(page.hasPrevious());
    }

    @Test
    void Slicing() {
        memberRepository.save(new Member("member1", 20));
        memberRepository.save(new Member("member2", 20));
        memberRepository.save(new Member("member3", 20));
        memberRepository.save(new Member("member4", 20));
        memberRepository.save(new Member("member5", 20));

        int age = 20;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        Slice<Member> page = memberRepository.findSliceByAge(age, pageRequest);

        List<Member> content = page.getContent();

        for (Member member : content) {
            System.out.println("member.getUsername() = " + member.getUsername());
        }

        assertEquals(3, content.size());
        assertTrue(page.isFirst());
        assertTrue(page.hasNext());
        assertFalse(page.hasPrevious());
    }

    @Test
    void bulkUpdate() {
        for (int i = 0; i < 5; i++) {
            memberRepository.save(new Member("member" + i, 17 + i));
        }
        int resultCount = memberRepository.bulkAgePlus(20);

        assertEquals(2, resultCount);
        assertEquals(21, memberRepository.findByUsername("member3").get(0).getAge());
        assertEquals(22, memberRepository.findByUsername("member4").get(0).getAge());
    }

    @Test
    void testEntityGraph() {
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        memberRepository.save(new Member("member1", 10, teamA));
        memberRepository.save(new Member("member2", 10, teamB));

        em.flush();
        em.clear();

        List<Member> members = memberRepository.findEntityGraphByUsername("member1");

        for (Member member : members) {
            System.out.println("member = " + member.getUsername());
            System.out.println("member.teamClass = " + member.getTeam().getClass());
            System.out.println("member.team = " + member.getTeam().getName());
        }
    }

    @Test
    void queryHint() {
        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);
        em.flush();
        em.clear();

        Member member = memberRepository.findReadOnlyByUsername("member1");
        member.setUsername("member22"); //스냅샷 생성 X, 변경 X

        em.flush();

        assertEquals(0, memberRepository.findByUsername("member22").size());
    }

    @Test
    void testLock() {
        Member member = new Member("member1", 10);
        memberRepository.save(member);
        em.flush();
        em.clear();

        List<Member> result = memberRepository.findLockByUsername("member1");
    }

    @Test
    void MemberRepositoryCustomTest() {
        memberRepository.save(new Member("member1"));
        List<Member> members = memberRepository.findMemberCustom();

        assertThat(members.size()).isEqualTo(1);
        assertThat(members.get(0).getUsername()).isEqualTo("member1");

    }
}