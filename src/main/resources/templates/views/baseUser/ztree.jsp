<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
  <link rel="stylesheet" href="static/zTree_v3/css/zTreeStyle/zTreeStyle.css" type="text/css">
  <!--jQuery必须在该插件之上， 因为ztree是基于jquery  -->
  <script type="text/javascript" src="public/jquery-1.8.3.js"></script>
  <script type="text/javascript" src="static/zTree_v3/js/jquery.ztree.all.js"></script>
  
</head>
<body>
	<ul id="tree" class="ztree" style="width:230px; overflow:auto;"></ul>
</body>
<script type="text/javascript">
	
	var zTreeObj,
	setting = {
		view: {
			selectedMulti: true
		},
		data: {
			simpleData: {
				enable: true,
				idKey: "id",
				pIdKey: "parentId",
				rootPId: 0
			}
		}
	};
	
	$(document).ready(function(){
		$.ajax({
			type:"post",
			url:"baseMenu/list.do",
			data:"json",
			success:function(data){
				zTreeObj = $.fn.zTree.init($("#tree"), setting, data.data);
			}
		})
	
	});
	
</script>
</html>