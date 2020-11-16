layui.use(['form','layer','table','laytpl'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        laytpl = layui.laytpl,
        table = layui.table;

    //角色列表
    var tableIns = table.render({
        elem: '#roleList',
        url : 'baseRole/list.do',
        cellMinWidth : 95,
        page : true,
        height : "full-125",
        limits : [10,15,20,25],
        limit : 20, 
        id : "roleListTable",
        cols : [[
            {type: "checkbox", fixed:"left", width:50},
            {field: 'name', title: '角色名称', minWidth:100, align:"center"},
            {field: 'isValid', title: '角色状态',  align:'center',templet:function(d){
                return d.isValid == "1" ? "启用" : "禁用";
            }},
            {field: 'description', title: '描述', align:'center'},
            {title: '操作', minWidth:175, templet:'#roleListBar',fixed:"right",align:"center"}
        ]]
    });

    //搜索【此功能需要后台配合，所以暂时没有动态效果演示】
    $(".search_btn").on("click",function(){
        if($(".searchVal").val() != ''){
            table.reload("newsListTable",{
                page: {
                    curr: 1 //重新从第 1 页开始
                },
                where: {
                    key: $(".searchVal").val()  //搜索的关键字
                }
            })
        }else{
            layer.msg("请输入搜索的内容");
        }
    });

    //添加角色
    function addUser(){
    	//layer 弹出层
        var index = layui.layer.open({
            title : "添加角色",
            type : 2,
            content : "baseRole/toAdd.do",
            success : function(layero, index){
                setTimeout(function(){
                    layui.layer.tips('点击此处返回角色列表', '.layui-layer-setwin .layui-layer-close', {
                        tips: 3
                    });
                },500)
            }
        })
        layui.layer.full(index);
        window.sessionStorage.setItem("index",index);
        //改变窗口大小时，重置弹窗的宽高，防止超出可视区域（如F12调出debug的操作）
        $(window).on("resize",function(){
            layui.layer.full(window.sessionStorage.getItem("index"));
        })
    }
    
    
    $(".addNews_btn").click(function(){
        addUser();
    })
    
    
    //修改角色  从添加角色的方法拷贝
    function toUpdateUser(id){
    	//layer 弹出层
        var index = layui.layer.open({
            title : "修改员工",
            type : 2,
            content : "baseRole/toUpdate.do?id="+id,
            success : function(layero, index){
                setTimeout(function(){
                    layui.layer.tips('点击此处返回角色列表', '.layui-layer-setwin .layui-layer-close', {
                        tips: 3
                    });
                },500)
            }
        })
        layui.layer.full(index);
        window.sessionStorage.setItem("index",index);
        //改变窗口大小时，重置弹窗的宽高，防止超出可视区域（如F12调出debug的操作）
        $(window).on("resize",function(){
            layui.layer.full(window.sessionStorage.getItem("index"));
        })
    }


   /**
    *  批量删除
    */
    $(".delAll_btn").click(function(){
        var checkStatus = table.checkStatus('roleListTable'),
            data = checkStatus.data,
            newsId = [];
        if(data.length > 0) {
            for (var i in data) {
                newsId.push(data[i].newsId);
            }
            layer.confirm('确定删除选中的角色？', {icon: 3, title: '提示信息'}, function (index) {
                // $.get("删除文章接口",{
                //     newsId : newsId  //将需要删除的newsId作为参数传入
                // },function(data){
                tableIns.reload();
                layer.close(index);
                // })
            })
        }else{
            layer.msg("请选择需要删除的角色");
        }
    })

    /**
     * 列表操作
     */  
    //tabel.on使用了数据表格中的事件监听，监听tool的userList
    table.on('tool(roleList)', function(obj){
        var layEvent = obj.event,
            data = obj.data;
//一个  =  赋值   两个  比较值但不比较类型
        if(layEvent === 'edit'){ //编辑
        	//从要修改的数据中获取id，以便回显和修改后更新数据库
            toUpdateUser(data.id);
        }else if(layEvent === 'del'){ //删除
            layer.confirm('确定删除此角色？',{icon:3, title:'提示信息'},function(index){
                // $.get("删除文章接口",{
                //     newsId : data.newsId  //将需要删除的newsId作为参数传入
                // },function(data){
                    tableIns.reload();
                    layer.close(index);
                // })
            });
        }
    });

})
