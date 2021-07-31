package study.datajpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.EventPublishingRunListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.Entity.Member;
import study.datajpa.dto.MemberDto;
import study.datajpa.repository.MemberRepository;

import javax.annotation.PostConstruct;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("/member/{id}")
    public String getMember(@PathVariable("id") Long id) {
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }

    @GetMapping("/member2/{id}")
    public String getMemberConvertor(@PathVariable("id") Member member) {
        return member.getUsername();
    }

    @PostConstruct
    public void init() {
        for (int i = 1; i < 101; i++) {
            memberRepository.save(new Member("member" + i, 80 - i / 2));
        }
    }

    @GetMapping("/members")
    public Page<Member> list(@PageableDefault(size = 4, sort = "username") Pageable pageable) {
        Page<Member> pageList = memberRepository.findAll(pageable);
        return pageList;
    }

    @GetMapping("/membersDto")
    public Page<MemberDto> dtoList(@PageableDefault(size = 4, sort = "username") Pageable pageable) {
        Page<MemberDto> memberDtos = memberRepository.findAll(pageable).map(m -> new MemberDto(m.getId(), m.getUsername(), null));
        return memberDtos;
    }
}

