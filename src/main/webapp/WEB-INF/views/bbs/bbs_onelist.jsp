<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style type="text/css">
#bbs table {
	width: 580px;
	margin: 0 auto;
	margin-top: 20px;
	border: 1px solid black;
	border-collapse: collapse;
	font-size: 14px;
}

#bbs table caption {
	font-size: 20px;
	font-weight: bold;
	margin-bottom: 10px;
}

#bbs table th {
	text-align: center;
	border: 1px solid black;
	padding: 4px 10px;
}

#bbs table td {
	text-align: left;
	border: 1px solid black;
	padding: 4px 10px;
}

.title {
	background: lightsteelblue;
}

.div1 {
	border: 1px solid blue;
	width: 60%;
	margin: auto;
	padding: 5px 20px;
}
</style>
<script type="text/javascript">
	function list_go(f) {
		f.action = "bbs_list.do";
		f.submit();
	}
	function update_go(f) {
		f.action = "bbs_update.do";
		f.submit();
	}
	function delete_go(f) {
		f.action = "bbs_delete.do";
		f.submit();
	}
	function comment_go(f) {
		f.action = "bbs_c_write.do";
		f.submit();
	}
	// 댓글 삭제 시 비밀번호 치게 해보자
	function comment_del(f) {
		f.action = "bbs_c_delete.do";
		f.submit();
	}
</script>
</head>
<body>
	<div id="bbs">
		<form method="post">
			<table summary="게시판">
				<caption>게시판</caption>
				<tbody>
					<tr>
						<th style="width: 15%" class="title">제목:</th>
						<td>${bvo.subject}</td>
					</tr>
					<tr>
						<th class="title">이름:</th>
						<td>${bvo.writer}</td>
					</tr>
					<tr>
						<th class="title">내용:</th>
						<td>${bvo.content}</td>
					</tr>
					<tr>
						<th class="title">첨부파일:</th>
						<c:choose>
							<c:when test="${empty bvo.f_name}">
								<td><b>첨부파일없음</b></td>
							</c:when>
							<c:otherwise>
								<td><img
									src="<c:url value='/resources/upload/${bvo.f_name}'/>"
									width="80px"> <br> <a
									href="bbs_down.do?f_name=${bvo.f_name}">${bvo.f_name} </a>
							</c:otherwise>
						</c:choose>
					</tr>
					<tr>
						<td colspan="2" style="text-align: center;"><input
							type="hidden" value="${cPage}" name="cPage"> <input
							type="hidden" value="${bvo.b_idx}" name="b_idx"> <input
							type="button" value="수정" onclick="update_go(this.form)">
							<input type="button" value="삭제" onclick="delete_go(this.form)">
							<input type="button" value="목록" onclick="list_go(this.form)">
						</td>
					</tr>
				</tbody>
			</table>
		</form>
	</div>

	<%-- 댓글 처리 --%>
	<div class="div1">
		<form method="post">
			<p>
				이름 : <input type="text" name="writer" size="15">
			</p>
			<p>
				내용 : <br>
				<textarea rows="4" cols="40" name="content"></textarea>
			</p>
			<p>
				비밀번호 : <input type="password" name="pwd" size="15">
			</p>
			<input type="hidden" name="b_idx" value="${bvo.b_idx}"> <input
				type="hidden" value="${cPage}" name="cPage"> <input
				type="button" value="댓글저장" onclick="comment_go(this.form)">
		</form>
	</div>
	<br>
	<br>
	<br>
	<div style="display: table; margin-left: 100px;">
		<c:forEach var="k" items="${c_list}">
			<div
				style="border: 1px solid #cc00cc; width: 400px; margin: 20px; padding: 20px;">
				<form method="post">
					<p>이름 : ${k.writer}</p>
					<p>날짜 : ${k.write_date.substring(0,10)}</p>
					<p>내용 : ${k.content}</p>
					<input type="hidden" name="c_idx" value="${k.c_idx}"> <input
						type="hidden" name="b_idx" value="${k.b_idx}"> <input
						type="hidden" value="${cPage}" name="cPage"> <input
						type="button" value="댓글삭제" onclick="comment_del(this.form)">
				</form>
			</div>
			<hr>
		</c:forEach>
	</div>
</body>
</html>
