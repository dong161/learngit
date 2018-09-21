var user={};
$(function(){
	user.init();
	
})
user.del=function(id){
	
	kq.ajax_url(kq.getRequestPath("/user/delbyid"),
				{"id":id},
				function(data){
						if(data.code !== 1){
								return;}
				}
	)
}
user.add=function(id,name,code,password,altertime,createtime){
	kq.ajax_url(kq.getRequestPath("/user/add"),
			{"id":id,"name":name,"code":code,"password":password,"altertime":altertime,"createtime":createtime},
			function(data){
					if(data.code !== 1){
							return;}
			}
)
}

user.updatebyid=function(id,field,value){
	
	kq.ajax_url(kq.getRequestPath("/user/updatebyid"),
				{"id":id,"field":field,"value":value},
				function(data){
						if(data.code !== 1){
								return;}
				}
	)
}
user.init=function(){
	kq.ajax_url(kq.getRequestPath("/user/getall"),
				{},
				function(data){
 					if(data.code !== 1){
 						return;
 					}
 					var newid;
 					var currPageNo;
 					layui.use('table', function(){
 						  var table = layui.table;
 						  var layer = layui.layer;
 						  var addrow=function(){
 							
 							  var totalRecord  =data.data.length;
 							  var pageSize = $(".layui-laypage-limits").find("select").val();
	 						  if($.type(pageSize) == "string"){
	 							  pageSize = parseInt(pageSize);
	 						  }
	 						  currPageNo = Math.ceil(totalRecord / pageSize);
	 						  console.log("每页显示的记录数："+pageSize+"  类型："+$.type(pageSize)+"   总的记录数："+totalRecord+"  类型："+$.type(totalRecord)
	 						     +"   新增行所在页码："+currPageNo+"  类型："+$.type(currPageNo));
					          
	 						 layer.open({
	 							  type:2,
					    		  width:200,
					    		  height:200,
					    		  btn:['确定','取消'],
					    		  shadeClose: false, //点击遮罩关闭层
					    		  anim:1,//弹出框动画效果
					    		  content: 'layer.html' ,             //$('#getid') ,
					    		  
					    		  yes: function(index, layero){
					    			    newid=$(layero).find('iframe')[0].contentWindow.callbackdata();
//					    			  	var body = layer.getChildFrame('body', index);
//					    		        var iframeWin = window[layero.find('iframe')[0]['name']]; 
//					    		        newid=iframeWin.callbackdata();
				                        layer.close(index);
				                        var data1={"US_IDENT":newid,"US_NAME":"","US_CODE":"","US_PWORD":"","修改时间":"","创建时间":"","操作":""};
	 			 						data.data.push(data1);
	 			 						  
	 							          table.reload('LAY_table_user',{
	 							        	  page : {
	 							        	         curr : currPageNo
	 							        	      },
	 							              data : data.data
	 							          })
					    			  },
					    		
	 						 });
	 						
	 						  
					          }
							          
 						  
 						  
 						  //监听表格复选框选择
 						  table.on('checkbox(demo)', function(obj){
 						    console.log(obj)
 						  });
 						  table.on('toolbar(demo)', function(obj){
 							  var checkStatus = table.checkStatus(obj.config.id);
 							  switch(obj.event){
 							    case 'add':
 							    	
 							    	addrow();
 							    	
 							        //layer.msg('添加');
 							    break;
 							    case 'delete':
 							      layer.msg('删除');
 							    break;
 							    case 'update':
 							      layer.msg('编辑');
 							    break;
 							  };
 							});
 						  //监听工具条
 						  table.on('tool(demo)', function(obj){
 						    var data = obj.data;
 						    var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
 						    var tr = obj.tr; //获得当前行 tr 的DOM对象
 						    if(layEvent=== 'detail'){
 						      layer.msg('ID：'+ data.US_IDENT + ' 的查看操作');
 						    } else if(layEvent === 'del'){
 						      layer.confirm('真的删除行么', function(index){
 						    	  user.del(data.US_IDENT)
 						    	  obj.del();
 						    	  layer.close(index);
 						      });
 						    } else if(layEvent === 'edit'){
 						      layer.alert('编辑行：<br>'+ JSON.stringify(data))
	 						  obj.update({
	 						        US_NAME: '123'
	 						        ,title: 'xxx'
	 						   });
 						    }
 						  });
 						  //监听单元格编辑
 						  table.on('edit(demo)', function(obj){
 						    var value = obj.value //得到修改后的值
 						    ,data = obj.data //得到所在行所有键值
 						    ,field = obj.field; //得到字段
 						    user.updatebyid(data.US_IDENT,field,value)
 						    layer.msg('[ID: '+ data.US_IDENT +'] ' + field + ' 字段更改为：'+ value);
 						  });
 						  
 						  //第一个实例
 						  table.render({
 						    elem: '#LAY_table_user'
 						    ,toolbar: '#toolbarDemo'
 						    ,height: 600
// 						    ,width:1200
 						    ,data: data.data //数据接口
 						    ,page: true //开启分页
 						    ,cols: [[ //表头
 						      {checkbox: true, fixed: true}
 						      ,{field: 'US_IDENT', title: 'ID', edit: 'text',sort: true, fixed: 'left'}
 						      ,{field: 'US_NAME', title: '名称',edit: 'text'}
 						      ,{field: 'US_CODE', title: '登录code', edit: 'text',sort: true,}
 						      ,{field: 'US_PWORD', title: '密码', edit: 'text'} 
 						      ,{field: 'US_DROP', title: '修改时间',edit: 'text'}
 						      ,{field: 'US_DATE', title: '创建时间',edit: 'text', sort: true}
 						      ,{fixed: 'right', title:'操作', toolbar: '#barDemo'}
 						    ]]
 						
 						  });
 						 $('.demoTable #search').on('click',function(){
 							var username=$('.demoTable #username').val();
 							var code=$('.demoTable #code').val();
 							kq.ajax_url(kq.getRequestPath("/user/selectbyname"),
 									{"username": username,"code":code},
 									function(data){
 										if(data.code !== 1){
 												return;
 											}
 										alert(data.data);
 										table.reload('LAY_table_user',{
 								              data : data.data
 								          });
// 										table.render({
// 				 						    elem: '#LAY_table_user'
// 				 						    ,toolbar: '#toolbarDemo'
// 				 						    ,height: 500
// 				 						    ,width:1200
// 				 						    ,data: data.data //数据接口
// 				 						    ,page: true //开启分页
// 				 						    ,cols: [[ //表头
// 				 						      {checkbox: true, fixed: true}
// 				 						      ,{field: 'US_IDENT', title: 'ID', sort: true, fixed: 'left'}
// 				 						      ,{field: 'US_NAME', title: '名称'}
// 				 						      ,{field: 'US_CODE', title: '登录code', sort: true}
// 				 						      ,{field: 'US_PWORD', title: '密码'} 
// 				 						      ,{field: 'US_DROP', title: '修改时间'}
// 				 						      ,{field: 'US_DATE', title: '创建时间',  sort: true}
// 				 						      ,{fixed: 'right', title:'操作', toolbar: '#barDemo',}
// 				 						    ]]
// 				 						
// 				 						  });
 											 
 								
 									}
 							)
 						})
 					})
})
}
					
