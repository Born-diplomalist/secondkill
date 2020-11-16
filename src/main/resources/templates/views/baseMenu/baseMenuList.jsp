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
	<title>菜单管理</title>
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
<form class="layui-form">
	<blockquote class="layui-elem-quote quoteBox">
		<form class="layui-form">
			<div class="layui-inline">
				<div class="layui-input-inline">
					<input type="text" class="layui-input searchVal" placeholder="请输入搜索的内容" />
				</div>
				<a class="layui-btn search_btn" data-type="reload">搜索</a>
			</div>
			<div class="layui-inline">
				<a class="layui-btn layui-btn-normal addNews_btn">添加菜单</a>
			</div>
			<div class="layui-inline">
				<a class="layui-btn layui-btn-danger layui-btn-normal delAll_btn">批量删除</a>
			</div>
		</form>
	</blockquote>
	<!--菜单树 此处目录有限，不要将右侧空白留的太多
		应该让左树右表产生关联
	-->
 	<div style="float:left;width:18%">
		<ul id="tree" class="ztree" style="width:230px; overflow:auto;"></ul>
	</div>
	<div style="float:left;width:82%">
		<!-- 此处会被Js加入数据表格 ，我们需要在此之前加上一个菜单树 -->
		<table id="menuList" lay-filter="menuList"></table>
				<!--操作-->
	</div>
	
	
	<script type="text/html" id="menuListBar">
		<a class="layui-btn layui-btn-xs" lay-event="edit">编辑</a>
		<a class="layui-btn layui-btn-xs layui-btn-warm" lay-event="usable">已启用</a>
		<a class="layui-btn layui-btn-xs layui-btn-danger" lay-event="del">删除</a>
	</script>


</form>
<script type="text/javascript" src="public/layui/layui.js"></script>
<script type="text/javascript" src="public/jquery-1.8.3.js"></script>
<script type="text/javascript" src="static/zTree_v3/js/jquery.ztree.all.js"></script>
<script type="text/javascript" src="public/js/baseMenu/menuList.js"></script>
</body>
