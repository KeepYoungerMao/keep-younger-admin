# keep-younger-admin
一个简单的后台管理系统 \

主要使用： \

spring、spring boot、mybatis、thymeleaf、shiro

*** 

## IDEA 生成模板文件方法（Mapper.xml）举例

`File` -- `Settings` -- `Editor` -- `File and Code Templates` -- \
面板中选择：`+`(添加) -- `name` 填写：`mapper`， `extension` 填写 `xml`， \
内容填写：

    <?xml version="1.0" encoding="UTF-8" ?>
    <!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
            "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
    <mapper namespace="">
    
    </mapper>
保存 \
新建文件选项便会有`mapper`这个选项，直接可创建`Mapper.xml`文件

*** 

## IDEA 设置去除mapper.xml文件中SQL语句警告

`File` -- `Settings` -- `Editor` -- `Inspections` -- `SQL ` -- \
找到其中的 `No data sources configure` 、 \
`SQL dialect datection` 和 `Unresolved reference` 选项 \
去掉勾选，保存。\
黄色警告解除，但会有淡绿色底色，有洁癖的可以找： \
`settings` -- `Editor` -- `Color Scheme` -- `General` \
面板中的 `Folded text with highlighting`，将右边的`Background`勾选去掉。（我试的绿色去不掉。。）

***

## AJAX请求无法重定向的问题

#### 1.问题
在本项目中，所有请求都是ajax方式请求，请求使用shiro配置权限，在没有权限的时候后台拦截权限异常 \
并重定向至无权限页面。但是当无权限时后台虽然会正常返回无权限页面，但前端则不会跳转。 \
前端请求中可以看到：原请求被302；无权限页面200，response data未无权限页面内容。
#### 2.原因
ajax请求默认不支持重定向。ajax请求是局部刷新，不会重新刷新页面。
#### 3.解决办法
1.后台需要重定向的时候，首先判断是否为ajax请求，如果是，则向header中传入指定参数， \
告诉前端我要重定向,并且我要重定向到哪。 \
2.前端利用ajax的ajaxSetup()方法，在每次ajax请求完成的时候执行此方法。 \
此方法中判断header中有没有指定参数，如果有，则重定向。
###### 代码
1.后台部分

    private void redirect(HttpServletRequest request,
                              HttpServletResponse response) throws IOException{
        //获取源url
        String basePath = request.getScheme() + "://"
                + request.getServerName() + ":"
                + request.getServerPort()+request.getContextPath();
                
        //判断是否是ajax
        if("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))){
        
            //告诉ajax我是重定向
            response.setHeader("REDIRECT", "REDIRECT");
            
            //告诉ajax我重定向的路径
            response.setHeader("CONTENT-PATH", basePath+"/unAuth");
            
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            
        }else{
        
            response.sendRedirect("/unAuth");
        }
    }
2.前端部分

    //使用ajax请求时，使用此参数接收
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
3.前端ajax使用：每次使用ajax请求，让他的返回值为jqxhr。如：

    var jqxhr = $.ajax({
        
        url: '/a/b',
        type: 'get',
        success: function(data){
            . . .
        }
        
    });