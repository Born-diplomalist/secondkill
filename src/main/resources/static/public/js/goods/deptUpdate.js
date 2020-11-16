layui.use(['form','layer'],function(){
    var form = layui.form
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;
    
    

    
    //对修改部门的按钮的提交事件进行监听
    form.on("submit(updateDept)",function(data){
        //弹出loading  显示指定信息和图标
        var index = top.layer.msg('数据提交中，请稍候',{icon: 16,time:false,shade:0.8});
        //实际使用时的提交信息
        $.ajax({
        	type:"post",
        	url:"baseDept/update.do",
        	//取出id为baseDeptForm的数据（ 是baseDeptAdd.jsp的表单id）
        	data:$('#baseDeptForm').serialize(),
        	dataType:"json",
        	success:function(data){
        		if(data.code==0){
        			//成功
        			setTimeout(function(){
        	            top.layer.close(index);
        	            top.layer.msg(data.msg);
        	            layer.closeAll("iframe");
        	            //刷新父页面,父页面就是mapList.jsp的页面，添加和更新是弹出来的
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