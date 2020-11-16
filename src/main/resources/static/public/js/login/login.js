layui.use(['form','layer','jquery'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer
        $ = layui.jquery;

    
    /**
     * 每次点击验证码图片，去重新获取验证码
     */
    $('#codeImg').click(function(){
    	//后面跟一个随机id，让每一次请求都不一样，以免触发缓存，导致验证码不更换
    	$('#codeImg').attr("src","code.do?id="+Math.random())
    });
      
    

    //登录按钮
    form.on("submit(login)",function(data){
        $(this).text("登录中...").attr("disabled","disabled").addClass("layui-disabled");
/*        console.log($("#code").val()+"----");
        console.log($("#loginName").val()+"++++");
        console.log($("#password").val()+"####");
        console.log(hex_md5($("#password").val())+"||#||");*/
        $.ajax({
        	type:"post",
        	url:"login.do",
        	//将密码用md5加密
    		data:{"userName":$("#loginName").val(),"password":hex_md5($("#password").val()),"code":$("#code").val()},
    		dataType:"json",
    		success:function(data){
    			if(data.code!=0){
    				layer.msg(data.msg);
    				$("#login").text("登录").removeAttr("disabled").removeClass("layui-disabled");
    			}
    			else if(data.code ==0){
    				window.location.href = "index.do";
    			}
    			layer.msg(data.code+"--||--"+data.msg)
    		}
        })

    })

    //表单输入效果
    $(".loginBody .input-item").click(function(e){
        e.stopPropagation();
        $(this).addClass("layui-input-focus").find(".layui-input").focus();
    })
    $(".loginBody .layui-form-item .layui-input").focus(function(){
        $(this).parent().addClass("layui-input-focus");
    })
    $(".loginBody .layui-form-item .layui-input").blur(function(){
        $(this).parent().removeClass("layui-input-focus");
        if($(this).val() != ''){
            $(this).parent().addClass("layui-input-active");
        }else{
            $(this).parent().removeClass("layui-input-active");
        }
    })
})
