<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html>
<html>
<base href="<%=basePath%>" />
	<meta charset="utf-8">
	<title>修改员工信息</title>
	<meta name="renderer" content="webkit">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
	<meta name="apple-mobile-web-app-status-bar-style" content="black">
	<meta name="apple-mobile-web-app-capable" content="yes">
	<meta name="format-detection" content="telephone=no">
	<link rel="stylesheet" href="public/layui/css/layui.css" media="all" />
	<link rel="stylesheet" href="public/css/public.css" media="all" />
</head>
<body class="childrenBody">


<form class="layui-form" id="baseUserForm" style="width:80%;">
	<div class="layui-form-item layui-row layui-col-xs12">
		<label class="layui-form-label">姓名</label>
		<div class="layui-input-block">
			<input type="text" name="name"  value="${baseUser.name}" class="layui-input name" lay-verify="required" placeholder="请输入姓名">
			<input type="hidden" name="id" value="${baseUser.id}" >
		</div>
	</div>
		<div class="layui-form-item layui-row layui-col-xs12">
		<label class="layui-form-label">联系电话</label>
		<div class="layui-input-block">
			<input type="text" name="phone"  value="${baseUser.phone}" class="layui-input phone" lay-verify="phone" placeholder="请输入手机号">
		</div>
	</div>
	<div class="layui-form-item layui-row layui-col-xs12">
		<label class="layui-form-label">邮箱</label>
		<div class="layui-input-block">
			<input type="text" name="email"  value="${baseUser.email}" class="layui-input userEmail" lay-verify="email" placeholder="请输入邮箱">
		</div>
	</div>
	<div class="layui-row">
		<div class="magb15 layui-col-md4 layui-col-xs12">
			<label class="layui-form-label">性别</label>
			<div class="layui-input-block userSex">
				<input type="radio" name="sex" value="1" title="男" <c:if test="${baseUser.sex==1}">checked</c:if>>
				<input type="radio" name="sex" value="0" title="女" <c:if test="${baseUser.sex==0}">checked</c:if>>
			</div>
		</div>
 		<div class="magb15 layui-col-md4 layui-col-xs12">
			<label class="layui-form-label">角色</label>
			<div class="layui-input-block">
				<select name="roleId" class="roleId" lay-filter="roleId">
				<!-- 遍历展示角色，和用户的角色id一致的角色被选中 -->
					<c:forEach items="${baseRole}" var="role">
						<option value="${role.id }"<c:if test="${baseUser.roleId==role.id}">selected</c:if>>${role.name }</option>
					</c:forEach>
				</select>
			</div>
		</div>
	</div>
	<div class="layui-form-item layui-row layui-col-xs12">
		<div class="layui-input-block">
		<!-- button不给type属性默认是submit -->
			<button class="layui-btn layui-btn-sm" lay-submit="" lay-filter="updateUser">立即修改</button>
			<button type="reset" class="layui-btn layui-btn-sm layui-btn-primary">取消</button>
		</div>
	</div>
</form>
<script type="text/javascript" src="public/layui/layui.js"></script>
<script type="text/javascript" src="public/js/baseUser/userUpdate.js"></script>
</body>
</html>