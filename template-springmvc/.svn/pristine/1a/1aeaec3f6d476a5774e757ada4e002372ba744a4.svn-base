/**
 * 依赖于jquery,JSON2, layer弹出框
 * 
 * ------------------------------------------- assert断言 ----------------------------------------------------
 * kq.assert.isEmpty      是否为空
 * 	
 * kq.assert.isNotEmpty   是否不为空
 * kq.assert.isBoolean(g) 判断是否为boolean值
 * isNumber、isString、isFunction、isArray、isObject、isDefined、isNotDefined
 * ------------------------------------------- 远程请求 ----------------------------------------------------
 * 
	kq.ajax(serviceId, param, success, fail, options)
	 发起AJAX请求，执行指定的serviceId ; 
	 默认同步 , 可在options对象中重写设置为异步{async : true ;}
	 sample
	 	kq.ajax("serviceId",
	  			{},
	  			function(data){
	  			}
	 			)
 * * 
	kq.ajax_url(url, param, success, fail, options)
	 向指定的url发起AJAX请求 ; 默认同步 ;
	 sample
	 	kq.ajax_url(kq.getRequestPath("/a/xxx.do"),
	  				{},
	  				function(data){
	  					// 如果本次请求没有被成功处理
   	  					if(data.code !== 1){
   	  						return;
   	  					}	
   	  					
   	  					// 自定义逻辑
	  				}
	 			   )
 * 
 * kq.ajaxSubmit($obj, params)
 *  发起ajaxSubmit请求的ajax对象
 *  params : 
 *      第三方框架jquery.form的$.ajaxSubmit方法的所有支持的参数 
 *  
 * ------------------------------------------- 公用请求 ----------------------------------------------------
 * 
 * kq.getRequestPath(relativePath)
 *  获取请求路径
 *  sample
 *  	kq.getRequestPath("/a/xxx.do") => http://localhost:9090/contextpath/a/xxx.do
 * 
 * kq.getUrlParam(name) 
 *  获取请求参数; name为参数名称
 * 
 * kq.guid()
 *  生成随机码 
 * 
 * kq.strFormat()
 *  C#样式的字符串格式化
 *  exsample：
 *      kq.strFormat("this is a {0}","test") => "this is a test" 
 * ------------------------------------------- 对话框相关(暂未启用) ----------------------------------------------------
 * kq.dialog.alert(msg, callback)
 *  警告对话框; 调用了kq.alert; 默认了icon为0
 * 
 * kq.dialog.success(msg, callback)
 *  成功对话框; 调用了kq.alert; 默认了icon为1
 * 
 * kq.dialog.fail(msg, callback)
 *  失败对话框; 调用了kq.alert; 默认了icon为2
 * 
 * kq.dialog.confirm(msg, yesCallback, cancelCallback)
 *  确认对话框; 调用了kq.confirm; 
 * 
 * --------------------------------------------------------------
 * 
 */

var kq = kq || {};
kq = $.extend(kq, {
    context: ""
    , path: {}
    , url: ""
    , init: null
    , pageSize: 25
    , pageList: [10, 20, 25, 30, 40]
    , loadMsg: "正在加载数据..."
    , timeout_message: "登陆超时或者账号已被其他人登陆！"
});

kq.init = function () {	
	kq.context = "/" + window.location.pathname.split("/")[1];
    kq.url = kq.context + "/controller/core/request.do";
};

kq.getRequestPath = function(relativePath){
	// 1. 可参考群主的 Url.js中的combine (https://github.com/blqw/blqw.JsTools/blob/master/WebSite/js/Url.js)
	// 2. 或者 ： http://medialize.github.io/URI.js/docs.html#normalize
	if(relativePath.startsWith("/")){
		return kq.context + relativePath;
	}else{
		return kq.context +"/" + relativePath;
	}
		
}

$(function () {
    kq.init();
})
/*----------------------------------------------------------------------------------------------------------*/
/*-------------------------------------------- assert 断言 判断类型 -----------------------------------------*/
/*----------------------------------------------------------------------------------------------------------*/

kq.assert = {
    isBoolean: function (G) {
        return (typeof G === "boolean");
    },
    // 判断字符串是否由数字组成,则使用RegexHelper的digitMatch
    // 或使用isFinite
    isNumber: function (G) {
        return (typeof G === "number" && isFinite(G));
    },
    isString: function (G) {
        return (typeof G === "string" || G.constructor == String);
    },
    isFunction: function (G) {
        var A = {};
        return (A.toString.apply(G) === A.toString.apply(Function));
    },
    isArray: function (G) {
        return Object.prototype.toString.call(G) === "[object Array]";
    },
    isObject: function (G) {
        return (G && (typeof G === "object" || this.isFunction(G)) || false);
    },
    isDefined: function (v) {
        return typeof v !== 'undefined';
    },
    isNotDefined: function (v) {
        return (!kq.assert.isDefined(v));
    },
    /* 无法判断对象或数组为空,可以用来判断变量是否为空 */
    isEmpty: function (v, allowBlank) {
        return v === null || v === undefined || ((kq.assert.isArray(v) && !v.length)) || (!allowBlank ? v === '' : false);
    },
    isNotEmpty: function (v, allowBlank) {
        return !kq.assert.isEmpty(v, allowBlank);
    }
    , isPositiveInteger: function (v) {
        return /(^[1-9]\d*$)/.test(v)
    }
};

/*----------------------------------------------------------------------------------------------------------*/
/*-------------------------------------------- 远程请求 -----------------------------------------------------*/
/*----------------------------------------------------------------------------------------------------------*/

$(function _remote_request($, win) {

    /**
     *  默认error函数 
     *  注：与 jeasyui.datagrid.js 覆盖的 onLoadError重复了 !!!
     * 
     *  this; 调用本次AJAX请求时传递的options参数
     *  request: XMLHttpRequest 对象
     *  textStatus :错误信息、 除了得到null之外，还可能是"timeout", "error","notmodified" 和 "parsererror"
     *  errorThrown: :（可选）捕获的异常对象
     * 
     *  通常 textStatus 和 errorThrown 之中只有一个会包含信息
     */
    var defaultErrorFunc = function (request, status, ex) {
        if (kq.assert.isEmpty(request.responseText)) {
            alert("未知错误, 错误信息为: [ " + ex + " ]");
            return;
        }

        var rv = JSON2.parse(request.responseText);     //  {code: 1, data: "/common/page/login.html", msg: "fail"}
        
        // 待完善, 其实更应该在后台统一掉
//        if(kq.assert.isEmpty(rv.status)){
//        	alert(rv.error);
//        	return;
//        }
        
        
        switch (rv.code) {
            case -1:  // NO_LOGIN
            case -2:  // NO_PERMISSION
                // 当前页面不属于登录界面时
                if (window.top.location.href.indexOf(rv.data) < 0) {
                	alert.alert("超期!");
                    window.top.location.href = common.context + rv.data;
                }
                break;
            default:
                alert("未知错误, 错误码为: [ " + rv.code + " ]");
                break;
        }
    };

    kq._defaultErrorFunc = defaultErrorFunc;

    /**
     * 向指定的url发起AJAX请求
     * 默认同步 , 可在options对象中重写设置为异步{async : true ;}
     */
    kq.ajax = function (serviceId, param, success, fail, options) {
        var queryParam = param || {}
            , op = options || { async: false };
        queryParam.serviceId = serviceId;

        var params = {
            type: "POST"
            , url: kq.url
            , dataType: "JSON"
            , cache: false
            , async: op.async
            , data: queryParam
            , success: success
            , fail: fail
        };

        kq._doAjax(params);
    };

    /**
     * ajax请求; 默认是同步的, 可以显式设置为异步; async : true ;
     * 默认同步 , 可在options对象中重写设置为异步{async : true } 即: kq.ajax_url(url,param,success,null,{async : true})
     */
    kq.ajax_url = function (url, param, success, fail, options) {
        var queryParam = param || {}
            , op = options || { async: false };
        if (kq.assert.isEmpty(op.async)) {
            op.async = false;
        }
        var params = {
            type: "POST"
            , url: url
            , dataType: "JSON"
            , cache: false
            , async: op.async
            , data: queryParam
            , success: success
            , fail: fail
        };

        kq._doAjax(params);

    };

    /**
     * ajax请求; 默认是同步的, 可以显式设置为异步; async : true ; 
     */
    kq.ajax_html = function (url, param, success, fail, options) {
        var queryParam = param || {}
            , op = options || { async: false };
        if (kq.assert.isEmpty(op.async)) {
            op.async = false;
        }
        var params = {
            type: "GET"
            , url: url
            , dataType: "html"
            , cache: false
            , async: op.async
            , data: queryParam
            , success: success
            , fail: fail
        };

        kq._doAjax(params);

    };

    /**
     * 发起ajaxSubmit请求的ajax对象
     * params : 第三方框架jquery.form的$.ajaxSubmit方法的所有支持的参数
     */
    kq.ajaxSubmit = function ($obj, params) {
        var customSuccessFunc = params.success
            , customErrorFunc = params.error;
        params.success = function (data) {
            customSuccessFunc(this, arguments);
        };
        params.error = function (data) {
            if (kq.assert.isNotEmpty(customErrorFunc)) {
                customErrorFunc(this, arguments);
                return;
            }
            defaultErrorFunc(this, arguments);
        };
        $obj.ajaxSubmit(params);
    };

    /**
     * ajax 统一处理函数
     */
    kq._doAjax = function (params) {
        var defaultParams = {
				                success: function (data, status, jqXHR) {
				                    if (typeof data == String) {
				                        data = JSON2.parse(data);
				                    }
				                    params.success(data);
				                }
				                , error: function (request, status, ex) {
				                    if (kq.assert.isNotEmpty(params.fail)) {
				                        params.fail(status + ";" + ex);
				                    } else {
				                        defaultErrorFunc.apply(this, arguments);
				                    }
				                }
				            };

        kq.lq.apply(defaultParams, params);

        /* 解决重复请求问题 */
        // var currentRequests = {};
        // $.ajaxPrefilter(function (options, originalOptions, jqXHR) {
        //     if (currentRequests[options.url]) { currentRequests[options.url].abort(); }
        //     currentRequests[options.url] = jqXHR;
        // });

        $.ajax(defaultParams);
    };

}(jQuery, window));


/*----------------------------------------------------------------------------------------------------------*/
/*-------------------------------------------- 公用方法和请求 -----------------------------------------------------*/
/*----------------------------------------------------------------------------------------------------------*/

/**
 * 获取url参数
 */
kq.getUrlParam = function (name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i"); // 构造一个含有目标参数的正则表达式对象
    var r = window.location.search.substr(1).match(reg);  // 匹配目标参数
    if (r != null) {
        return unescape(r[2]);
    }
    return ""; // 返回参数值
    // return $.query.get(name); 
}

/** 生成随机码 */
kq.guid = function () {
    return 'xxxxxxxxxxxx4xxxyxxxxxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
        var r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
        return v.toString(16);
    });
}

/**
 * C#样式的字符串格式化
 *  exsample：
 *      kq.strFormat("this is a {0}","test") => "this is a test"
 */
kq.strFormat = function () {
    if (arguments.length == 0)
        return null;

    var str = arguments[0];
    for (var i = 1; i < arguments.length; i++) {
        var re = new RegExp('\\{' + (i - 1) + '\\}', 'gm');
        str = str.replace(re, arguments[i]);
    }
    return str;
};

/*----------------------------------------------------------------------------------------------------------*/
/*-------------------------------------------- kq.lq -----------------------------------------------------*/
/*----------------------------------------------------------------------------------------------------------*/
(function _init_lq($, kq, win) {
    if (kq.lq === undefined) {
        kq.lq = {};
    }
    var lq = kq.lq;

    /*
	 * 空实现
	 */
    lq.emptyFn = function () {
        // 仿 Ext.emptyFn
    };

    /*
	 * 将 c 中属性复制到 o 中;已有的会被覆盖.
	 * 
	 * defaults : A different object that will also be applied for default
	 * values
	 */
    lq.apply = function (o, c, defaults) {
        // no "this" reference for friendly out of scope calls
        if (defaults) {
            lq.apply(o, defaults);
        }
        if (o && c && typeof c == 'object') {
            for (var p in c) {
                o[p] = c[p];
            }
        }
        return o;
    };

    /* 将 c 中属性复制到 o 中;已存在于o中的属性不处理. */
    lq.applyIf = function (o, c) {
        if (o) {
            for (var p in c) {
                if (!kq.assert.isDefined(o[p])) {
                    o[p] = c[p];
                }
            }
        }
        return o;
    };

}(jQuery, kq, window));






