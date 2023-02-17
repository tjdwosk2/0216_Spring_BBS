package com.ict.bbs.model.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ict.bbs.model.vo.BBS_VO;
import com.ict.bbs.model.vo.Comment_VO;

@Repository
public class BBS_DAO {
	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

	// 주석 처리한 이유 : 근거를 남기기 위해서
//	public List<BBS_VO> getList() {
//		return sqlSessionTemplate.selectList("bbs.list");
//	}

	public int getTotalCount() {
		return sqlSessionTemplate.selectOne("bbs.count");
	}

	public List<BBS_VO> getList(int begin, int end) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("begin", begin);
		map.put("end", end);
		return sqlSessionTemplate.selectList("bbs.list", map);
	}

	public BBS_VO getOneList(String b_idx) {
		return sqlSessionTemplate.selectOne("bbs.onelist", b_idx);
	}

	public int getInsert(BBS_VO bvo) {
		return sqlSessionTemplate.insert("bbs.insert", bvo);
	}

	public int getDelete(String b_idx) {
		return sqlSessionTemplate.delete("bbs.delete", b_idx);
	}

	public int getHitUpdate(String b_idx) {
		return sqlSessionTemplate.update("bbs.hitup", b_idx);
	}

	public int getUpdate(BBS_VO bvo) {
		return sqlSessionTemplate.update("bbs.update", bvo);
	}

	public List<Comment_VO> getCommList(String b_idx) {
		return sqlSessionTemplate.selectList("bbs.comm_list", b_idx);
	}

	public int getCommWrite(Comment_VO cvo) {
		return sqlSessionTemplate.insert("bbs.comm_insert", cvo);
	}

	public int getCommDelete(String c_idx) {
		return sqlSessionTemplate.delete("bbs.comm_delete", c_idx);
	}

	//
	public Comment_VO getCommOneList(String c_idx) {
		return sqlSessionTemplate.selectOne("bbs.comm_onelist", c_idx);
	}

}
