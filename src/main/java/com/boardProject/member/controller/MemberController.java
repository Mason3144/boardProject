package com.boardProject.member.controller;

import com.boardProject.dto.SingleResponseDto;
import com.boardProject.member.dto.MemberDto;
import com.boardProject.member.entity.Member;
import com.boardProject.member.mapper.MemberMapper;
import com.boardProject.member.service.MemberService;
import com.boardProject.utils.UriCreator;
import org.mapstruct.control.MappingControl;
import org.mapstruct.control.MappingControls;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.EntityResponse;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.net.URI;

@RestController
@RequestMapping("/v1/members")
@Validated
public class MemberController {
    private final static String MEMBER_DEFAULT_URL = "/v1/members";
    private final MemberService service;
    private final MemberMapper mapper;

    public MemberController(MemberService service, MemberMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity postMember(@RequestBody @Valid MemberDto.Post requestBody){
        // email verification needed
        // social login create member needed
        Member memberCreated = service.createMember(mapper.memberPostToMember(requestBody));

        URI location = UriCreator.createUri(MEMBER_DEFAULT_URL,memberCreated.getMemberId());

        return ResponseEntity.created(location).build();
    }

    @PatchMapping("/{member-id}")
    public ResponseEntity patchMember(@PathVariable("member-id") @Positive long memberId,
                                      @RequestBody @Valid MemberDto.Patch requestBody){
        // profile photo update needed
        requestBody.setMemberId(memberId);

        Member updatedMember = service.updateMember(mapper.memberPatchToMember(requestBody));

        return new ResponseEntity<>(
                new SingleResponseDto<>(mapper.memberToMemberResponse(updatedMember)), HttpStatus.OK
        );
    }
    @GetMapping("/{member-id}")
    public ResponseEntity getMember(@PathVariable("member-id") @Positive long memberId){
        Member foundMember = service.findMember(memberId);
        return new ResponseEntity<>(
                new SingleResponseDto<>(mapper.memberToMemberResponse(foundMember)),HttpStatus.OK
        );
    }
    @DeleteMapping("/{member-id}")
    public ResponseEntity deleteMember(@PathVariable("member-id") @Positive long memberId){
        Member deletedMember = service.deleteMember(memberId);
        return new ResponseEntity<>(
                new SingleResponseDto<>(mapper.memberToMemberResponse(deletedMember)), HttpStatus.OK
        );
    }
}
