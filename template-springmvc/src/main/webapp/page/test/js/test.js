$(function(){
	/* 访问地址: 
 			http://localhost:9999/kanq-template-project-springmvc/page/test/test.html
	 * */
	
	
	
	$(document).css("background-color","red");
	
	$("#requestControllerBtn").on("click",function(){
		 	kq.ajax_url(kq.getRequestPath("/test/helloWorld.do"),
		  				{},
		  				function(data){
	   	  					if(data.code !== 1){
	   	  						return;
	   	  					}
	   	  					
	   	  					alert(data.data);
		  				}
		 	)		
	});
  
	$("#requestServiceBtn").on("click",function(){
	    kq.ajax("TEST_SERVICE",
				{ Condition: JSON2.stringify({YWID:"123"}) },
				function (data) {
				    alert(data.data.row["'LQ'"]);
				},
				null,
				{ async: false }
			);		
	});
	
})