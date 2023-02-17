package com.ict.board.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ict.board.model.dao.Board_DAO;
import com.ict.board.model.vo.Board_VO;

@Service
public class Borad_ServiceImpl implements Board_Service {

	@Autowired
	private Board_DAO board_DAO;

	public void setBoard_DAO(Board_DAO board_DAO) {
		this.board_DAO = board_DAO;
	}

	@Override
	public List<Board_VO> getList() {
		return board_DAO.getList();
	}
}
