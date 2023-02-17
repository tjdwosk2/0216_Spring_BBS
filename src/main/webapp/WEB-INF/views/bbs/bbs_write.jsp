<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="resources/css/summernote-lite.css">
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
		f.action = "bbs_list.do";
		f.submit();
	}
	function sendData_go(f) {
		if (f.subject.value.trim().length <= 0) {
			alert("제목을 입력하세요");
			f.subject.focus();
			return;
		}
		if (f.writer.value.trim().length <= 0) {
			alert("작성자를 입력하세요");
			f.writer.focus();
			return;
		}
		if (f.content.value.trim().length <= 0) {
			alert("내용을 입력하세요");
			f.content.focus();
			return;
		}
		if (f.pwd.value.trim().length <= 0) {
			alert("비밀번호를 입력하세요");
			f.pwd.focus();
			return;
		}
		f.action = "bbs_write_ok.do";
		f.submit();
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
						<td><input type="text" name="subject" size="45" /></td>
					</tr>
					<tr>
						<th class="title">이름:</th>
						<td><input type="text" name="writer" size="12" /></td>
					</tr>
					<tr>
						<th class="title">내용:</th>
						<td><textarea name="content" id="content" cols="50" rows="8"></textarea></td>
					</tr>
					<tr>
						<th class="title">첨부파일:</th>
						<!-- 파일 명 잊지말자  -->
						<td><input type="file" name="f_param" /></td>
					</tr>
					<tr>
						<th class="title">비밀번호:</th>
						<td><input type="password" name="pwd" size="12" /></td>
					</tr>
					<tr>
						<td colspan="2" style="text-align: center;">
							<input type="button" value="글쓰기" onclick="sendData_go(this.form)" />
							<!-- 화면에 보이는 글 모두 빈글로 초기화  -->
							<input type="reset" value="다시" />
							<input type="button" value="목록" onclick="list_go(this.form)" /></td>
					</tr>
				</tbody>
			</table>
		</form>
	</div>
	<script src="https://code.jquery.com/jquery-3.6.0.min.js"
		crossorigin="anonymous"></script>
	<script src="resources/js/summernote-lite.js"></script>
	<script src="resources/js/lang/summernote-ko-kr.js"></script>
	<script type="text/javascript">
		$(function() {
			$("#content").summernote({
				lang : "ko-KR",
				height : 300,
				focus : true,
				callbacks : {
					onImageUpload : function(files, editor) {
						for (var i = 0; i < files.length; i++) {
							sendImage(files[i], editor)
						}
					}
				}
			});
		});

		function sendImage(file, editor) {
			var frm = new FormData();
			frm.append("upload", file);

			// 비동기 통신
			$.ajax({
				url : "saveImg.inc",
				data : frm, // 전달하고자 하는 파라미터 값
				type : "post", // 전송 방식
				contentType : false,
				processData : false,
				dataType : "json",
			}).done(
					function(data) {
						$("#content").summernote("editor.insertImage",
								data.url + "/" + data.fname);
					});
		}
	</script>
</body>
</html>

