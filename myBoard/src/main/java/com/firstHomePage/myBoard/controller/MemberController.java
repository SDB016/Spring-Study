package com.firstHomePage.myBoard.controller;

import com.firstHomePage.myBoard.domain.Member;
import com.firstHomePage.myBoard.service.MemberService;
import com.firstHomePage.myBoard.util.Session;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/member")
    public CreateMemberResponse join(@RequestBody @Valid CreateMemberRequest request){

        Member member = new Member(request.userId, request.userPwd, request.name, request.nickname, request.age);
        Long memberId = memberService.join(member);

        return new CreateMemberResponse(memberId);
    }

    @PostMapping("/member/login")
    public LoginMemberResponse login(@RequestBody @Valid LoginMemberRequest loginRequest, HttpServletRequest httpRequest) {

        Optional<Member> optionalMember = Optional.ofNullable(memberService.login(loginRequest.getUserId(), loginRequest.getUserPwd()));
        if(optionalMember.isPresent()) {
            HttpSession session = httpRequest.getSession();
            session.setAttribute(Session.SESSION_ID, optionalMember.get().getNickname());

            return new LoginMemberResponse("SUCCESS");
        }else{
            return new LoginMemberResponse("FAIL");
        }
    }

    @PatchMapping("/member/{id}")
    public String updateMember(
            @PathVariable Long id,
            @RequestBody @Valid UpdateMemberRequest request){

        memberService.update(id, request.userId, request.userPwd, request.nickname);

        return "Update Done!";
    }

    @GetMapping("/member/id")
    public findMemberIdResponse findMemberIdByName(@RequestBody @Valid findMemberIdRequest request){

        String id = memberService.findIdByName(request.getName());
        return new findMemberIdResponse(id);
    }

    @GetMapping("/member/pwd")
    public findMemberPwdResponse findMemberPwdByNameId(@RequestBody @Valid findMemberPwdRequest request){

        String pwd = memberService.findPwdByNameId(request.name, request.userId);
        return new findMemberPwdResponse(pwd);
    }

    @DeleteMapping("/member/{id}")
    public String deleteMember(@PathVariable Long id){

        Member member = memberService.findOne(id);
        memberService.delete(member);

        return "Delete Done!";
    }


    //=====static class========//
    @Data
    @AllArgsConstructor
    static class CreateMemberResponse{
        private Long id;
    }

    @Data
    static class CreateMemberRequest{
        private String userId;
        private String userPwd;
        private String name;
        private String nickname;
        private int age;
    }

    @Data
    static class UpdateMemberRequest{
        private String userId;
        private String userPwd;
        private String nickname;
    }

    @Data
    @AllArgsConstructor
    static class findMemberIdResponse{
        private String userId;
    }

    @Data
    static class findMemberIdRequest{
        private String name;
    }

    @Data
    @AllArgsConstructor
    static class findMemberPwdResponse{
        private String userPwd;
    }

    @Data
    static class findMemberPwdRequest{

        private String name;
        private String userId;
    }

    @Data
    static class LoginMemberRequest{

        private String userId;
        private String userPwd;
    }

    @Data
    @AllArgsConstructor
    static class LoginMemberResponse{

        private String loginResult;
    }
}
