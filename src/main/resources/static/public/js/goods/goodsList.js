layui.use(['form','layer','table','laytpl'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        laytpl = layui.laytpl,
        table = layui.table;

//	
//    //z-Tree 设置
//	var setting = {
//		view: {
//			selectedMulti: true
//		},
//		data: {
//			simpleData: {
//				enable: true,
//				idKey: "id",
//				pIdKey: "pId",
//				rootPId: 0
//			}
//		},
//		callback:{
//			onClick: zTreeOnClick
//		}
//	};
//    
//	
//	//zTree单击事件，通过传递所点击的节点的id在右侧列表上展示所单击的节点的子菜单
//	//默认未点击时，id应该为1，展示一级目录
//	function zTreeOnClick(event, treeId, treeNode) {
//		table.reload("deptListTable",{
//            page: {
//                curr: 1 //重新从第 1 页开始
//            },
//            where: {
//                "pId": treeNode.id  //搜索的关键字
//            }
//        })
//	};
//	
//	
//	//zTree树展示 
//	$(document).ready(function(){
//		
//		$.ajax({
//			type:"post",
//			url:"baseDept/list.do",
//			dataType:"json",
//			success:function(data){
//				zTreeObj = $.fn.zTree.init($("#tree"), setting, data.data);
//			}
//		})
//	
//	});
	
    //部门列表的展示与渲染   
    var tableIns = table.render({
        elem: '#deptList',
        url : 'baseDept/listTable.do',
        cellMinWidth : 95,
        page : true,
        height : "full-125",
        limits : [10,15,20,25],
        limit : 20, 
        id : "deptListTable",
        cols : [[
            {type: "checkbox", fixed:"left", width:50},
            {field: 'name', title: '部门名称', minWidth:100, align:"center"},
            {field: 'isValid', title: '是否启用',  align:'center',templet:function(d){
                return d.isValid == "1" ? "启用" : "禁用";
            }},
            {field: 'description', title: '描述', align:'center'},
            {title: '操作', minWidth:175, templet:'#deptListBar',fixed:"right",align:"center"}
        ]]
    });

    //搜索【此功能需要后台配合，所以暂时没有动态效果演示】
    $(".search_btn").on("click",function(){
        if($(".searchVal").val() != ''){
        	//重新生成表格
            table.reload("deptListTable",{
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

    //添加菜单
    function addUser(){
    	//layer 弹出层
        var index = layui.layer.open({
            title : "添加部门",
            type : 2,
            //跳转到添加页面
            content : "baseDept/toAdd.do",
            success : function(layero, index){
                setTimeout(function(){
                    layui.layer.tips('点击此处返回菜单列表', '.layui-layer-setwin .layui-layer-close', {
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
    
    
    //修改部门  从添加部门的方法拷贝
    function toUpdateUser(id){
    	//layer 弹出层
        var index = layui.layer.open({
            title : "修改员工",
            type : 2,
            content : "baseDept/toUpdate.do?id="+id,
            success : function(layero, index){
                setTimeout(function(){
                    layui.layer.tips('点击此处返回部门列表', '.layui-layer-setwin .layui-layer-close', {
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
        var checkStatus = table.checkStatus('deptListTable'),
            data = checkStatus.data,
            newsId = [];
        if(data.length > 0) {
            for (var i in data) {
                newsId.push(data[i].newsId);
            }
            layer.confirm('确定删除选中的部门？', {icon: 3, title: '提示信息'}, function (index) {
                // $.get("删除文章接口",{
                //     newsId : newsId  //将需要删除的newsId作为参数传入
                // },function(data){
                tableIns.reload();
                layer.close(index);
                // })
            })
        }else{
            layer.msg("请选择需要删除的部门");
        }
    })

    /**
     * 列表操作
     */  
    //tabel.on使用了数据表格中的事件监听，监听tool的userList
    table.on('tool(deptList)', function(obj){
        var layEvent = obj.event,
            data = obj.data;
//一个  =  赋值   两个  比较值但不比较类型
        if(layEvent === 'edit'){ //编辑
        	//从要修改的数据中获取id，以便回显和修改后更新数据库
            toUpdateUser(data.id);
        }else if(layEvent === 'del'){ //删除
            layer.confirm('确定删除此部门？',{icon:3, title:'提示信息'},function(index){
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
