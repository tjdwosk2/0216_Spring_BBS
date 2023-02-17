package com.ict.bbs.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.ict.bbs.model.service.BBS_Service;
import com.ict.bbs.model.vo.BBS_VO;
import com.ict.bbs.model.vo.Comment_VO;
import com.ict.common.FileReName;
import com.ict.common.Paging;

@Controller
public class BBS_Controller {
	// 0217 수정 후 목록을 누르면 안됨

	@Autowired
	private BBS_Service bbsService;

	// common 에 있는 paging 을 쓰기위함
	@Autowired
	private Paging paging;

	// common 에 있는 fileReName 을 쓰기위함
	@Autowired
	private FileReName fileReName;

	// cPage 전역변수화
	String cPage;

	private static final Logger logger = LoggerFactory.getLogger(BBS_Controller.class);

	public void setBbsService(BBS_Service bbsService) {
		this.bbsService = bbsService;
	}

	public void setPaging(Paging paging) {
		this.paging = paging;
	}

	public void setFileReName(FileReName fileReName) {
		this.fileReName = fileReName;
	}
	
	// 첫 페이지 List 정보 가져오기
	@RequestMapping("bbs_list.do")
	public ModelAndView getList(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("bbs/bbs_list");
		// 전체 게시물의 수
		int count = bbsService.getTotalCount();
		paging.setTotalRecord(count);

		// 전체 페이지의 수
		// 간단한 수학, 어려워하지말자
		if (paging.getTotalRecord() <= paging.getNumPerPage()) {
			paging.setTotalPage(1);
		} else {
			paging.setTotalPage(paging.getTotalRecord() / paging.getNumPerPage());
			if (paging.getTotalRecord() % paging.getNumPerPage() != 0) {
				paging.setTotalPage(paging.getTotalPage() + 1);
			}
		}
		// 현재 페이지
		cPage = request.getParameter("cPage");
		if (cPage == null) {
			paging.setNowPage(1);
		} else {
			paging.setNowPage(Integer.parseInt(cPage));
		}

		// 시작 번호와 끝 번호 구하기
		paging.setBegin((paging.getNowPage() - 1) * paging.getNumPerPage() + 1);
		paging.setEnd((paging.getBegin() - 1) + paging.getNumPerPage());

		// 시작 블록과 끝 블록 구하기
		paging.setBeginBlock(
				(int) ((paging.getNowPage() - 1) / paging.getPagePerBlock()) * paging.getPagePerBlock() + 1);
		paging.setEndBlock(paging.getBeginBlock() + paging.getPagePerBlock() - 1);

		// 주의 사항
		if (paging.getEndBlock() > paging.getTotalPage()) {
			paging.setEndBlock(paging.getTotalPage());
		}

		List<BBS_VO> bbs_list = bbsService.getList(paging.getBegin(), paging.getEnd());
		mv.addObject("bbs_list", bbs_list);
		mv.addObject("paging", paging);

		return mv;
	}

	// OneList 정보 가져오기
	@RequestMapping("bbs_onelist.do")
	public ModelAndView getOneList(@RequestParam("b_idx") String b_idx, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("bbs/bbs_onelist");
		cPage = request.getParameter("cPage");

		// 조회수 업데이트
		bbsService.getHitUpdate(b_idx);

		// 상세보기
		BBS_VO bvo = bbsService.getOneList(b_idx);

		// 댓글 가져오기
		List<Comment_VO> c_list = bbsService.getCommList(b_idx);

		mv.addObject("bvo", bvo);
		mv.addObject("c_list", c_list);
		mv.addObject("cPage", cPage);
		return mv;
	}

	// 게시글 쓰기 화면단 들어가기
	@RequestMapping("bbs_write.do")
	public ModelAndView getWrite() {
		ModelAndView mv = new ModelAndView("bbs/bbs_write");
		return mv;
	}
	
	// 게시글 쓰기
	@RequestMapping("bbs_write_ok.do")
	public ModelAndView getWriteOK(BBS_VO bvo, HttpSession session) {
		ModelAndView mv = new ModelAndView("redirect:bbs_list.do");
		logger.info("게시글 작성 페이지 진입");
		try {
			String path = session.getServletContext().getRealPath("/resources/upload");
			MultipartFile file_name = bvo.getF_param()[0];
			if (file_name.isEmpty()) {
				bvo.setF_name("");
			} else {
				// 이름 중복 여부
				String str = fileReName.exec(path, bvo.getF_param()[0].getOriginalFilename());
				bvo.setF_name(str);

			}
			mv.addObject("cPage", "1");
			int result = bbsService.getInsert(bvo);
			if (result > 0) {
				file_name.transferTo(new File(path + "/" + bvo.getF_name()));
			}

		} catch (Exception e) {
		}
		return mv;
	}

	// 댓글 쓰기
	@RequestMapping("bbs_c_write.do")
	public ModelAndView commWrite(Comment_VO cvo, @ModelAttribute("cPage") String cPage,
			@ModelAttribute("b_idx") String b_idx) {
		ModelAndView mv = new ModelAndView("redirect:bbs_onelist.do");
		bbsService.getCommWrite(cvo);
		return mv;
	}

	// 파일 다운로드
	@RequestMapping("bbs_down.do")
	public void getFileDonw(@RequestParam("f_name") String f_name, HttpSession session, HttpServletResponse response) {
		String path = session.getServletContext().getRealPath("/resources/upload/" + f_name);
		try {
			String realpath = URLEncoder.encode(path, "utf-8");
			response.setContentType("application/x-msdownload");
			response.setHeader("Content-Disposition", "attachment; filename=" + realpath);
			File file = new File(new String(path.getBytes()));
			FileInputStream in = new FileInputStream(file);
			OutputStream out = response.getOutputStream();
			FileCopyUtils.copy(in, out);
		} catch (Exception e) {
		}
	}

	// 게시글 수정창 들어가기
	@RequestMapping("bbs_update.do")
	public ModelAndView getUpdate(@RequestParam("b_idx") String b_idx) {
		ModelAndView mv = new ModelAndView("bbs/bbs_update");
		BBS_VO bvo = bbsService.getOneList(b_idx);
		mv.addObject("bvo", bvo);
		return mv;
	}

	// 게시글 수정하기
	@RequestMapping("bbs_update_ok.do")
	public ModelAndView getUpdateOK(BBS_VO bvo, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();
		String path = request.getSession().getServletContext().getRealPath("/resources/upload");
		try {
			MultipartFile f_param = bvo.getF_param()[0];
			String old_f_name = request.getParameter("old_f_name");

			File dir = new File(path);
			String[] arr = dir.list();
			List<String> k = Arrays.asList(arr);
			boolean res = k.contains(f_param.getOriginalFilename());
			String ori_filename = f_param.getOriginalFilename();
			if (ori_filename.equals("") || ori_filename == null) {
				bvo.setF_name(old_f_name);
			} else {
				bvo.setF_name(f_param.getOriginalFilename());
				if (!res) {
					f_param.transferTo(new File(path + "/" + bvo.getF_name()));
				}
			}
			bbsService.getUpdate(bvo);
		} catch (Exception e) {
		}
		mv.setViewName("redirect:bbs_onelist.do?b_idx=" + bvo.getB_idx());
		return mv;
	}

	// 게시글 삭제창 들어가기
	@RequestMapping("bbs_delete.do")
	public ModelAndView getDelete(@RequestParam("b_idx") String b_idx) {
		ModelAndView mv = new ModelAndView("bbs/bbs_delete");
		BBS_VO bvo = bbsService.getOneList(b_idx);
		mv.addObject("bvo", bvo);
		return mv;
	}

	// 게시글 삭제 하기
	@RequestMapping("bbs_delete_ok.do")
	public ModelAndView getDeleteOK(@RequestParam("b_idx") String b_idx) {
		ModelAndView mv = new ModelAndView("redirect:bbs_list.do");
		bbsService.getDelete(b_idx);
		return mv;
	}

	// 댓글 삭제창 들어가기
	@RequestMapping("bbs_c_delete.do")
	public ModelAndView commDelete(@ModelAttribute("cPage") String cPage, @ModelAttribute("b_idx") String b_idx,
			@RequestParam("c_idx") String c_idx) {
		ModelAndView mv = new ModelAndView("bbs/bbs_comm_delete");
		Comment_VO cvo = bbsService.getCommOneList(c_idx);
		BBS_VO bvo = bbsService.getOneList(cvo.getB_idx());
		mv.addObject("cvo", cvo);
		mv.addObject("bvo", bvo);
		mv.addObject("cPage", cPage);
		return mv;
	}

	// 댓글 삭제하기
	@RequestMapping("bbs_c_delete_ok.do")
	public ModelAndView commDeleteOK(@ModelAttribute("cPage") String cPage, @ModelAttribute("b_idx") String b_idx,
			@RequestParam("c_idx") String c_idx) {
		ModelAndView mv = new ModelAndView("redirect:bbs_onelist.do");
		bbsService.getCommDelete(c_idx);
		return mv;
	}

	// 삭제 및 수정 목록 버튼 활성화
	@RequestMapping("bbs_list_delete_update.do")
	public ModelAndView getListDelete(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("bbs/bbs_list");
		// 전체 게시물의 수
		logger.info("list_delete 진입" + cPage);
		int count = bbsService.getTotalCount();
		paging.setTotalRecord(count);

		// 전체 페이지의 수
		// 간단한 수학, 어려워하지말자
		if (paging.getTotalRecord() <= paging.getNumPerPage()) {
			paging.setTotalPage(1);
		} else {
			paging.setTotalPage(paging.getTotalRecord() / paging.getNumPerPage());
			if (paging.getTotalRecord() % paging.getNumPerPage() != 0) {
				paging.setTotalPage(paging.getTotalPage() + 1);
			}
		}
		// 현재 페이지
		logger.info("list_delete 진입" + cPage);
		if (cPage == null) {
			paging.setNowPage(1);
		} else {
			paging.setNowPage(Integer.parseInt(cPage));
		}

		// 시작 번호와 끝 번호 구하기
		paging.setBegin((paging.getNowPage() - 1) * paging.getNumPerPage() + 1);
		paging.setEnd((paging.getBegin() - 1) + paging.getNumPerPage());

		// 시작 블록과 끝 블록 구하기
		paging.setBeginBlock(
				(int) ((paging.getNowPage() - 1) / paging.getPagePerBlock()) * paging.getPagePerBlock() + 1);
		paging.setEndBlock(paging.getBeginBlock() + paging.getPagePerBlock() - 1);

		// 주의 사항
		if (paging.getEndBlock() > paging.getTotalPage()) {
			paging.setEndBlock(paging.getTotalPage());
		}

		List<BBS_VO> bbs_list = bbsService.getList(paging.getBegin(), paging.getEnd());
		mv.addObject("bbs_list", bbs_list);
		mv.addObject("paging", paging);

		return mv;
	}

}
