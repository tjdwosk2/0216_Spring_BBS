package com.ict.bbs.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ict.bbs.model.dao.BBS_DAO;
import com.ict.bbs.model.vo.BBS_VO;
import com.ict.bbs.model.vo.Comment_VO;

@Service
public class BBS_ServiceImpl implements BBS_Service {
	@Autowired
	private BBS_DAO bbsDAO;

	public void setBbsDAO(BBS_DAO bbsDAO) {
		this.bbsDAO = bbsDAO;
	}

	@Override
	public List<BBS_VO> getList(int begin, int end) {
		return bbsDAO.getList(begin, end);
	}

	// 보통은 주석처리를 한다. 근거를 남기기위해서
//	@Override
//	public List<BBS_VO> getList() {
//		return bbsDAO.getList();
//	}

	@Override
	public int getHitUpdate(String b_idx) {
		return bbsDAO.getHitUpdate(b_idx);
	}

	@Override
	public int getTotalCount() {
		return bbsDAO.getTotalCount();
	}

	@Override
	public BBS_VO getOneList(String b_idx) {
		return bbsDAO.getOneList(b_idx);
	}

	@Override
	public List<Comment_VO> getCommList(String b_idx) {
		return bbsDAO.getCommList(b_idx);
	}

	@Override
	public int getInsert(BBS_VO bvo) {
		return bbsDAO.getInsert(bvo);
	}

	@Override
	public int getDelete(String b_idx) {
		return bbsDAO.getDelete(b_idx);
	}

	@Override
	public int getUpdate(BBS_VO bvo) {
		return bbsDAO.getUpdate(bvo);
	}

	@Override
	public int getCommWrite(Comment_VO cvo) {
		return bbsDAO.getCommWrite(cvo);
	}

	@Override
	public int getCommDelete(String c_idx) {
		return bbsDAO.getCommDelete(c_idx);
	}

	@Override
	public Comment_VO getCommOneList(String c_idx) {
		return bbsDAO.getCommOneList(c_idx);
	}

}
