/*
 * ajax请求不成功（如：对此请求没有权限，后台会重定向至无权限页面）
 * 但是默认ajax请求是局部刷新，不重新加载页面，因此不支持重定向
 * 此方法为利用ajax请求的$.ajaxSetup方法，每当ajax请求完成便执行complete方法，
 * 在后台的配合下，后台发送指定参数：
 * REDIRECT：告诉前端我要重定向
 * CONTENT-PATH：告诉前端我要重定向的地址
 * 然后前端根据这2个参数判断是否需要重定向
 */

var jqxhr;
//设置ajax请求完成后运行的函数,
$.ajaxSetup({
    complete:function(){
        //若HEADER中含有REDIRECT说明后端想重定向，
        if("REDIRECT" == jqxhr.getResponseHeader("REDIRECT")){
            var win = window;
            while(win != win.top){
                win = win.top;
            }
            //将后端重定向的地址取出来,使用win.location.href去实现重定向的要求
            win.location.href = jqxhr.getResponseHeader("CONTENT-PATH");
        }
    }
});