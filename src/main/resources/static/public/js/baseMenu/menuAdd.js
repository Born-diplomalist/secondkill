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
		callback:{
			onDblClick: zTreeOnDblClick
		}
		
		//将查到的数据存到变量中，以免每次都操作数据库
	},zTreeObj,treeData;

	
	//双击事件，双击之后赋值并关闭弹框
	function zTreeOnDblClick(ecent,treeId,treeNode) {
		$("#parentName").val(treeNode.name);
		$("#parentId").val(treeNode.id);
		layui.layer.close(layui.layer.index);
	}
	
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
    
    $('#parentName').click(function(){
    	//弹框
    	 layui.layer.open({
    	        type: 1 //默认水平居中
    	        ,offset: 'auto' //坐标  auto为垂直水平居中
    	        ,content: $("#treeData")
    	        ,btn: '关闭'
    	        ,shadeClose: true 
    	        ,area:['300px','400px'] //设定宽高
    	        ,yes: function(){
    	          layer.closeAll();
    	        }
    	      });
    })
    
    //对添加菜单的按钮的提交事件进行监听
    form.on("submit(addMenu)",function(data){
        //弹出loading  显示指定信息和图标
        var index = top.layer.msg('数据提交中，请稍候',{icon: 16,time:false,shade:0.8});
        //实际使用时的提交信息
        $.ajax({
        	type:"post",
        	url:"baseMenu/add.do",
        	//取出id为baseMenuForm的数据（ 是baseMenuAdd.jsp的表单id）
        	data:$('#baseMenuForm').serialize(),
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