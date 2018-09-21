 var callbackdata = function () {
            var data=$('#newid').val();
            return data;
        }
 $(function(){
	 $('#add').on('click',function(){
		 var id=$('#newid').val();
		 kq.ajax_url(kq.getRequestPath("/user/add"),
					{"id": id},
					function(data){
						if(data.code !== 1){
								return;
							}
					})
	 })
})