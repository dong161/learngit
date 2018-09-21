var common = {
    context: "/" + window.location.pathname.split("/")[1]
};
JSLoader.loadJavaScript(common.context + "/js/jquery/jquery-1.11.0.min.js");
JSLoader.loadJavaScript(common.context + "/js/json2.js");
JSLoader.loadJavaScript(common.context + "/js/kq-core.js");
JSLoader.loadStyleSheet(common.context + "/js/layui2.4/css/layui.css");
JSLoader.loadJavaScript(common.context + "/js/layui2.4/layui.js");
