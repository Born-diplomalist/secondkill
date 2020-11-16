layui.use(['form','layer'],function(){
    var form = layui.form
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;

    form.on("submit(updateUser)",function(data){
        //弹出loading  显示指定信息和图标
        var index = top.layer.msg('数据提交中，请稍候',{icon: 16,time:false,shade:0.8});
        // 实际使用时的提交信息
        // $.post("上传路径",{
        //     userName : $(".userName").val(),  //登录名
        //     userEmail : $(".userEmail").val(),  //邮箱
        //     userSex : data.field.sex,  //性别
        //     userGrade : data.field.userGrade,  //会员等级
        //     userStatus : data.field.userStatus,    //用户状态
        //     newsTime : submitTime,    //添加时间
        //     userDesc : $(".userDesc").text(),    //用户简介
        // },function(res){
        //
        // })
        $.ajax({
        	type:"post",
        	url:"baseUser/updateBaseUser.do",
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