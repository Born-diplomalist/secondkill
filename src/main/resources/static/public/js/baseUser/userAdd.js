layui.use(['form','layer'],function(){
    var form = layui.form
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;

    form.on("submit(addUser)",function(data){
        //弹出loading  显示指定信息和图标
        var index = top.layer.msg('数据提交中，请稍候',{icon: 16,time:false,shade:0.8});
        $.ajax({
        	type:"post",
        	url:"baseUser/addBaseUser.do",
        	data:$('#baseUserForm').serialize(),
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