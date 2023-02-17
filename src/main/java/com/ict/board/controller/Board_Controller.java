package com.ict.board.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ict.board.model.service.Board_Service;
import com.ict.board.model.vo.Board_VO;

@Controller
public class Board_Controller {
	@Autowired
	private Board_Service board_Service;
	
	private static final Logger logger = LoggerFactory.getLogger(Board_Controller.class);

	public void setBoard_Service(Board_Service board_Service) {
		this.board_Service = board_Service;
	}
	
	@RequestMapping("board_list.do")
	public ModelAndView getBoardList() {
		logger.info("게시글 진입");
		//ModelAndView mv = new ModelAndView("board/board_list");
		//List<Board_VO> list = board_Service.getList();
		//mv.addObject("list", list);
		return new ModelAndView("board/board_list");
	}
	
}
