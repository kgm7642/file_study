package com.project.study.domain;

import lombok.Data;

@Data
public class Users {
	private String usersnumber; //DB에서 PK 값
	private String username; // 로그인용 ID 값
	private String userspw; // 비밀번호
	private String usersnickname; // 닉네임
	private String usersemail; // 이메일
	private String Role; // 유저 역할
	private String createdate; // 계정 생성 날짜
	private String modifydate; // 계정 수정 날짜
	private String provider; // google
	private String providerid; // sub값
	private String[] skillarry;	// 기술 스택
	private String skill;
	private String imagename; // 이미지 파일 이름

}
