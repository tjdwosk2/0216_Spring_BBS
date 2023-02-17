<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet"
	href="resources/css/summernote-lite.css">
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
</style>
<script type="text/javascript">
	function list_go(f) {
		f.action = "bbs_list_delete_update.do";
		f.submit();
	}
	function update_ok_go(f) {
		var k = "${bvo.pwd}";
		if (f.pwd.value == k) {
			f.action = "bbs_update_ok.do";
			f.submit();
		} else {
			alert("비밀번호가 틀립니다.");
			f.pwd.value = "";
			f.pwd.focus();
			return;
		}
	}
</script>
</head>
<body>
	<div id="bbs">
		<form method="post" encType="multipart/form-data">
			<table summary="게시판 글쓰기">
				<caption>게시판 글쓰기</caption>
				<tbody>
					<tr>
						<th style="width: 15%" class="title">제목:</th>
						<td><input type="text" name="subject" value="${bvo.subject}"></td>
					</tr>
					<tr>
						<th class="title">이름:</th>
						<td><input type="text" name="writer" value="${bvo.writer}"></td>
					</tr>
					<tr>
						<th class="title">내용:</th>
						<td><textarea name="content" id="content" cols="50" rows="8">${bvo.content}</textarea></td>
					</tr>
					<tr>
						<th class="title">첨부파일:</th>
						<td><c:choose>
								<c:when test="${empty bvo.f_name}">
									<input type="file" name="f_param">
									<b>이전 파일 없음</b>
									<input type="hidden" name="old_f_name" value="">
								</c:when>
								<c:otherwise>
									<input type="file" name="f_param">
									<b>이전 파일 (${bvo.f_name})</b>
									<input type="hidden" name="old_f_name" value="${bvo.f_name}">
								</c:otherwise>
							</c:choose></td>
					</tr>
					<tr>
						<th class="title">비밀번호:</th>
						<td><input type="password" name="pwd" size="12" /></td>
					</tr>
					<tr>
						<td colspan="2" style="text-align: center;"><input
							type="hidden" name="cPage" value="${cPage}"> <input
							type="hidden" name="b_idx" value="${bvo.b_idx}"> <input
							type="button" value="수정" onclick="update_ok_go(this.form)" /> <input
							type="reset" value="다시" /> <input type="button" value="목록"
							onclick="list_go(this.form)" /></td>
					</tr>
				</tbody>
			</table>
		</form>
	</div>
	<script src="https://code.jquery.com/jquery-3.6.0.min.js"
		crossorigin="anonymous"></script>
	<script src="resources/js/summernote-lite.js"></script>
	<script
		src="resources/js/lang/summernote-ko-kr.js"></script>
	<script type="text/javascript">
		$(function() {
			$("#content").summernote({
				lang : "ko-kr",
				height : 300,
				focus : true,
				callbacks : {
					onImageUpload : function(files, editor) {
						// 이미지가 에디터에 추가 될 때마다 수행 하는 곳이다.
						// 이미지를 첨부하면 배열 인식된다.
						// 이것을 서버로 비동기식 통신을 수행하면 
						// 서버에 업로드를 시킬 수 있다.
						for (var i = 0; i < files.length; i++) {
							sendImage(files[i], editor);
						}
					}
				}
			});
		});
		function sendImage(file, editor) {
			// 서버로 이미지 파일을 보내기 위해 폼 객체 준비!
			var frm = new FormData(); // <form></form>

			// 폼에 파라미터값이 등록 (폼 객체 안에 upload라는 이름으로 파일이 등록된다.)
			frm.append("upload", file);

			// 비동기식 통신
			$.ajax({
				url : "${pageContext.request.contextPath}/view/saveImage.jsp", // 전달 하고자 하는 서버 위치
				data : frm, // 전달 하고자 하는 파라마터
				type : "post", // 전송 방식
				contentType : false,
				processData : false,
				dataType : "json",
			}).done(function(data) {
				// 이미지가 서버의 upload 폴더에 저장 성공시
				// 이미지 경로를 img_url이라는 이름으로 data에 저장되어 넘어온다.
				// 이것을 에디터에 넣어주면 된다.
				$("#content").summernote("editor.insertImage", data.img_url);
			});
		}
	</script>
</body>
</html>

