<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html>
<html>
<base href="<%=basePath%>" />
<head>
	<meta charset="utf-8">
	<title>添加菜单</title>
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


<form class="layui-form" id="baseMenuForm" style="width:80%;">
	<div class="layui-form-item layui-row layui-col-xs12">
		<label class="layui-form-label">菜单标题</label>
		<div class="layui-input-block">
			<input type="text" name="name" class="layui-input name" lay-verify="required" placeholder="请输入姓名">
		</div>
	</div>
	
	<div class="layui-form-item layui-row layui-col-xs12">
		<label class="layui-form-label">上级菜单</label>
		<div class="layui-input-block">
			<!--上级菜单不允许输入，只读  -->
			<input type="text" readonly="true" id="parentName" name="parentName" class="layui-input phone" placeholder="默认为一级菜单">
			<!-- 默认为0  表示一级菜单 -->
			<input type="hidden" id="parentId" name="parentId" value="0">
		</div>
	</div>
	
	<div class="layui-form-item layui-row layui-col-xs12">
		<label class="layui-form-label">联系电话</label>
		<div class="layui-input-block">
			<input type="text" name="phone" class="layui-input phone" lay-verify="phone" placeholder="请输入手机号">
		</div>
	</div>
		
	<div class="layui-form-item layui-row layui-col-xs12">
		<label class="layui-form-label">排序</label>
		<div class="layui-input-block">
			<input type="text" name="sort" class="layui-input sort" lay-verify="sort" placeholder="请输入顺序">
		</div>
	</div>
	<div class="layui-form-item layui-row layui-col-xs12">
		<label class="layui-form-label">访问路径</label>
		<div class="layui-input-block">
			<input type="text" name="resUrl" class="layui-input resUrl" lay-verify="resUrl" placeholder="请输入访问路径">
		</div>
	</div>
	
	<div class="layui-form-item layui-row layui-col-xs12">
		<label class="layui-form-label">描述</label>
		<div class="layui-input-block">
			<input type="text" name="description" class="layui-input description" lay-verify="description" placeholder="请输入描述">
		</div>
	</div>
	
	<div class="layui-row">
		<div class="magb15 layui-col-md4 layui-col-xs12">
			<label class="layui-form-label">是否启用</label>
			<div class="layui-input-block isValid">
				<input type="radio" name="isValid" value="1" title="启用" checked>
				<input type="radio" name="isValid" value="0" title="禁用">
			</div>
		</div>
 		<div class="magb15 layui-col-md4 layui-col-xs12">
			<label class="layui-form-label">角色</label>
			<div class="layui-input-block">
				<select name="type" class="type" lay-filter="type">
					<option value="1">菜单</option>
					<option value="2">功能</option>
					<option value="3">按钮</option>
				</select>
			</div>
		</div>

	</div>
	<div class="layui-form-item layui-row layui-col-xs12">
		<div class="layui-input-block">
		<!-- button不给type属性默认是submit -->
		<!--此处的lay-filter为事件过滤器，可通过设定其名字来在js中为该名字绑定事件，事件会对该标签生效  -->
			<button class="layui-btn layui-btn-sm" lay-submit="" lay-filter="addMenu">立即添加</button>
			<button type="reset" class="layui-btn layui-btn-sm layui-btn-primary">取消</button>
		</div>
	</div>
	<div id="treeData" style="display:none">
		<ul id="tree" class="ztree" style="width:230px; overflow:auto;"></ul>
	</div>
	
	<!--操作
	<script type="text/html" id="menuListBar">
		<a class="layui-btn layui-btn-xs" lay-event="edit">编辑</a>
		<a class="layui-btn layui-btn-xs layui-btn-warm" lay-event="usable">已启用</a>
		<a class="layui-btn layui-btn-xs layui-btn-danger" lay-event="del">删除</a>
	</script>-->
	
</form>
<script type="text/javascript" src="public/layui/layui.js"></script>
<script type="text/javascript" src="public/jquery-1.8.3.js"></script>
<script type="text/javascript" src="static/zTree_v3/js/jquery.ztree.all.js"></script>
<script type="text/javascript" src="public/js/baseMenu/menuAdd.js"></script>
</body>
</html>