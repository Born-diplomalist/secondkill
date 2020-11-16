<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html>
<html>
<!-- 想让zTree默认选中，需要数据有checked属性且属性为true，因此在数据库中做出checked字段 -->
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
	<link rel="stylesheet" href="static/zTree_v3/css/zTreeStyle/zTreeStyle.css" type="text/css">
</head>
<body class="childrenBody">


<form class="layui-form" id="baseRoleForm" style="width:80%;">
	<div class="layui-form-item layui-row layui-col-xs12">
		<label class="layui-form-label">角色</label>
		<div class="layui-input-block">
			<input type="text" name="name" class="layui-input name" value="${baseRole.name }" lay-verify="required" placeholder="请输入角色名">
			<input type="hidden" id="roleId" name="id" value="${baseRole.id}">
		</div>
	</div>
	
	<div class="layui-form-item layui-row layui-col-xs12">
		<label class="layui-form-label">所拥有权限</label>
		<div class="layui-input-block">
			<input type="text" readonly="readonly" id="auths" class="layui-input auths" lay-verify="required" placeholder="请输入角色名">
			<input type="text" id="mId" name="ids" >
		</div>
	</div>
	
	<div class="layui-form-item layui-row layui-col-xs12">
		<label class="layui-form-label">描述</label>
		<div class="layui-input-block">
			<input type="text" name="description" value="${baseRole.description }"  class="layui-input description" placeholder="请输入描述">
		</div>
	</div>

	<div class="layui-row">
		<div class="magb15 layui-col-md4 layui-col-xs12">
			<label class="layui-form-label">是否启用</label>
			<div class="layui-input-block isValid">
				<input type="radio" name="isValid" value="1" title="启用" <c:if test="${baseRole.isValid==1}">checked</c:if>>
				<input type="radio" name="isValid" value="0" title="禁用" <c:if test="${baseRole.isValid==0}">checked</c:if>>
			</div>
		</div>
	</div>
	
		
	<div id="treeData" style="display:none">
		<ul id="tree" class="ztree" style="width:230px; overflow:auto;"></ul>
	</div>
	
	
	
	<div class="layui-form-item layui-row layui-col-xs12">
		<div class="layui-input-block">
		<!-- button不给type属性默认是submit -->
			<button class="layui-btn layui-btn-sm" lay-submit="" lay-filter="updateRole">立即修改</button>
			<button type="reset" class="layui-btn layui-btn-sm layui-btn-primary">取消</button>
		</div>
	</div>
</form>
<script type="text/javascript" src="public/layui/layui.js"></script>
<script type="text/javascript" src="public/jquery-1.8.3.js"></script>
<script type="text/javascript" src="static/zTree_v3/js/jquery.ztree.all.js"></script>
<script type="text/javascript" src="public/js/baseRole/roleUpdate.js"></script>
</body>
</html>