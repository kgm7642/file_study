package com.project.study.domain;

import lombok.Data;

@Data
public class Users {
	private String usernumber; //DB에서 PK 값
	private String useremail; // 이메일
	private String username; // 로그인용 ID 값
	private String usersnickname; // 닉네임
	private String usermessage; // 상태메세지
	private String userblog; // 블로그명
	private String useraddress; // 블로그주소
	private String role;
	private String userpw;

}
