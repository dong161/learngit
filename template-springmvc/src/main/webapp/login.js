var login = {};
layui.use(['form', 'layer'], function () {
    login.layer = layui.layer;
    login.form = layui.form;
    if (window.top.location.href.indexOf("login.html") < 0) {
        window.top.location.href = common.context + "/login.html";
    }

    $("#login_username").val("Admin");
    $("#login_password").val("888888");

    $(document).keydown(function (event) {
        if (event.keyCode == 13) {
            login();
        }
    });
    login.form.on('submit(go)', function (data) {
        login();
        return false;
    });
    
    function login() {
        var isremember = false;
        var username = $("#login_username").val();
        var password = $("#login_password").val();
        if (username == "" || password == "") {
            kq.dialog.alert("用户名或密码不能为空！");
            return;
        }
        forceLogin(username, password, isremember);
    }

    function forceLogin(username, password, isremember) {

        kq.ajax_url("login/login.do", { "username": username, "password": $.md5(password), "isremember": isremember },
            function (data) {
        		if(data.code === 0){
        			kq.dialog.alert(data.msg);
        			return;
        		}

                afterLoginSuccess();
            }
        );
    }
    function afterLoginSuccess() {
        window.location.href = "page/test/test.html";
    }
});