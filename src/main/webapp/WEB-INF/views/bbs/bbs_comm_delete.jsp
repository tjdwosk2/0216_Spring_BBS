<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>댓글 삭제 비번 창</title>
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
		history.go(-1);
	}
	function delete_go(f) {
		var k = "${cvo.pwd}"
		if (f.pwd.value == k) {
			var chk = confirm("정말 삭제할까요?");
			if (chk) {
				f.action = "bbs_c_delete_ok.do";
				f.submit();
			} else {
				history.go(-1);
			}
		} else {
			alert("비밀번호가 틀립니다.\n다시 입력해 주세요");
			f.pwd.value = "";
			f.pw.focus();
			return;
		}
	}
</script>
</head>
<body>
	<div id="bbs">
		<form method="post">
			<table summary="댓글 삭제하기">
				<caption>댓글 삭제하기</caption>
				<tbody>
					<tr>
						<th style="width: 15%" class="title">비밀번호 확인:</th>
						<td><input type="password" name="pwd" size="20" /></td>
					</tr>
				</tbody>
				<tfoot>
					<tr>
						<td colspan="2">
							<input type="button" value="삭제" onclick="delete_go(this.form)">
							<input type="hidden" name="cPage" value="${cPage}">
							<input type="hidden" name="b_idx" value="${bvo.b_idx}">
							<input type="hidden" name="c_idx" value="${cvo.c_idx}">
							<input type="button" value="목록" onclick="list_go(this.form)">
							</td>
					</tr>
				</tfoot>
			</table>
		</form>
	</div>
</body>
</html>