layui.use(['form','layer'],function(){
    var form = layui.form
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;
    
    
    //z-Tree展示
	var setting = {
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
		},
		check: {
			enable: true,
			chkStyle: "checkbox",
		}
		
		//将查到的数据存到变量中，以免每次都操作数据库
	},zTreeObj,treeData;
	
    $('#auths').click(function(){
    	//弹框
    	 layui.layer.open({
    	        type: 1 //默认水平居中
    	        ,offset: 'auto' //坐标  auto为垂直水平居中
    	        ,content: $("#treeData")
    	        ,shadeClose: true 
    	        ,area:['300px','400px'] //设定宽高
    	 		,btn: ['确定','取消']
    	        ,yes: function(){
    	        	var treeObj=$.fn.zTree.getZTreeObj("tree");
    	        	var nodes= treeObj.getCheckedNodes(true);
    	        	var ids="";
    	        	var names="";
    	        	for(var i=0 ;i<nodes.length;i++){
    	        		ids +=nodes[i].id+",";
    	        		names +=nodes[i].name+",";
    	        	}
    	        	ids.substring(0,nodes.length-1)
    	        	names.substring(0,nodes.length-1)
    	        	$('#auths').val(names);
    	        	$('#mId').val(ids);
    	        	//关闭
    	        	layui.layer.close(layui.layer.index);
    	        }
    	 		,btn2:function(index,layero){
    	 			layer.closeAll();
    	 		}
    	 })
    });
	
	//zTree树展示
	$(document).ready(function(){
		
		$.ajax({
			type:"post",
			url:"baseMenu/list.do",
			dataType:"json",
			success:function(data){
				//将数据赋给一个变量treeData   zTreeObj为树节点对象
				treeData=data.data;
				zTreeObj = $.fn.zTree.init($("#tree"), setting, data.data);
				$.fn.zTree.getZTreeObj("tree").expandAll(true);
			}
		})
	});

    form.on("submit(addRole)",function(data){
    	alert($('#baseRoleForm').serialize());
        //弹出loading  显示指定信息和图标
        var index = top.layer.msg('数据提交中，请稍候',{icon: 16,time:false,shade:0.8});
        $.ajax({
        	type:"post",
        	url:"baseRole/add.do",
        	data:$('#baseRoleForm').serialize(),
        	dataType:"json",
        	success:function(data){
        		if(data.code==0){
        			//成功
        			setTimeout(function(){
        	            top.layer.close(index);
        	            top.layer.msg(data.msg);
        	            layer.closeAll("iframe");
        	            //刷新父页面
        	            parent.location.reload();
        	        },2000);
        		}else{
        			//关闭提示
        			top.layer.close(index);
        			//提示出我所提示出的信息
        			top.layer.msg(data.msg);
        		}
        	}
        	
        })
        
        return false;
    })

    //格式化时间
    function filterTime(val){
        if(val < 10){
            return "0" + val;
        }else{
            return val;
        }
    }
    //定时发布
    var time = new Date();
    var submitTime = time.getFullYear()+'-'+filterTime(time.getMonth()+1)+'-'+filterTime(time.getDate())+' '+filterTime(time.getHours())+':'+filterTime(time.getMinutes())+':'+filterTime(time.getSeconds());

})