package com.project.study.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.Principal;
import java.sql.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.UUID;

import org.springframework.boot.web.servlet.server.Session;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.nimbusds.oauth2.sdk.util.StringUtils;
import com.project.domain.Board;
import com.project.domain.BoardSaveForm;
import com.project.domain.BoardView;
import com.project.domain.Criteria;
import com.project.domain.Page;
import com.project.domain.ReplyPage;
import com.project.domain.Users;
import com.project.domain.Smarteditor;
import com.project.service.BoardService;
import com.project.service.UsersService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

	private final BoardService boardService;
	private final UsersService usersService;
	
//	보드 리스트 페이지 이동
	@GetMapping("/list")
	public String boardList(Model model, Criteria cri) {
		log.info("보드리스트 확인하기 : {}", boardService.boardList(cri));
		log.info("스킬리스트 확인하기 : {}", usersService.skill());
		model.addAttribute("pageMaker", new Page(boardService.getTotal(cri), cri, boardService.boardList(cri)));
		model.addAttribute("skillList", usersService.skill());
		return "board/boardList";
	}
	
//	게시글 리스트 받아오기(ajax, 첫 로딩, 필터)
	@PostMapping(value = "/getList", consumes = "application/json")
	public String getList(Model model, @RequestBody Criteria cri) {
		log.info("보드리스트 확인하기 : {}",boardService.boardList(cri));
		log.info("cri : {}",cri);
		model.addAttribute("pageMaker", new Page(boardService.getTotal(cri), cri, boardService.boardList(cri)));
		return "board/boardReplace";
	}
	
//	게시글 리스트 받아오기(ajax, 스크롤 이벤트)
	@PostMapping(value = "/getScroll", consumes = "application/json")
	public String getScroll(Model model, @RequestBody Criteria cri) {
		log.info("보드리스트 확인하기 : {}",boardService.boardList(cri));
		log.info("cri : {}",cri);
		model.addAttribute("pageMaker", new Page(boardService.getTotal(cri), cri, boardService.boardList(cri)));
		return "board/boardScroll";
	}

//	게시글 상세보기
	@GetMapping("/view")
	public String boardView(Model model, String boardnumber, HttpSession session) {		
		Users users = (Users) session.getAttribute("users");
		BoardView boardView = boardService.getBoard(boardnumber);
		
		// 접속자 닉네임과 게시글 닉네임이 같은지 확인
		if(!(users.getUsersnickname().equals(boardService.getBoard(boardnumber).getUsersnickname()))) {
			// 같지 않으면 게시글 조회수 1증가
			boardService.updateViewCnt(boardnumber);
			boardView.setBoardview(boardView.getBoardview()+1);
		}
		log.info("게시글 확인"+boardView);
		model.addAttribute("board", boardView);
		return "board/boardView";
	}

//	게시글 작성하기 페이지 이동
	@GetMapping("/write")
	public String write(@ModelAttribute Board board, Model model) {
		model.addAttribute("skillList", usersService.skill());
		return "board/boardWrite";
	}
	
//	게시글 작성 완료
	@PostMapping("/write")
	public String writeFinish(BoardSaveForm form, Principal principal, Model model) {
		form.setBoardwriter(principal.getName());
		log.info("BoardSaveForm을 확인해보자"+form);
		boardService.writeBoard(form);
		return "redirect:/board/list";
	}
	
//	게시글 수정 페이지 이동
	@GetMapping("/modify")
	public String boardModify(String boardnumber, Model model) {
		model.addAttribute("board", boardService.getBoardDetail(boardnumber));
		model.addAttribute("skillList", usersService.skill());
		log.info("게시글 확인 {}", boardService.getBoardDetail(boardnumber));
		log.info("게시글 수정 페이지 이동");
		return "board/boardmodify";
	}
	
//	게시글 수정 완료 후 게시글 상세보기 페이지 이동
	@PostMapping("/modify")
	public String boardModify(Board board) {
		boolean check = boardService.updateBoard(board);
		log.info("게시글 수정 완료 했음");
		return "redirect:/board/view?boardnumber="+board.getBoardnumber();
	}
	
//	게시글 삭제 후 게시글 리스트 페이지 이동
	@GetMapping("/remove")
	public String boardRemove(String boardnumber) {
		boolean check = boardService.removeBoard(boardnumber);
		log.info("게시글 삭제 완료 했음");
		return "redirect:/board/list";
	}
	
//	나의 게시글 리스트 페이지 이동
	@GetMapping("/myBoardList")
	public String myBoardList(Model model, HttpServletRequest req) {
		Users users = (Users) req.getSession().getAttribute("users");
		model.addAttribute("myBoardList", boardService.myBoardList(users.getUsername()));
		return "board/myBoardList";
	}
}
