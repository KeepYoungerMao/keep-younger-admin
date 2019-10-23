# keep-younger-admin
一个简单的后台管理系统。

主要使用：

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
面板中的 `code`中的`injected language fragment`，将右边的`Background`勾选去掉。

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

*** 

## Mapper.xml中返回值`List<T>`,当T为基本类型时的写法

列表查询时，如返回值为`List<User>`，则SQL写法为：

    <select id="getUser" resultType="com.mao.entity.User">
        SELECT * FROM user
    </select>
    
当返回值为`List<String>`接收值为基本类型时，则SQL写法为：

    <select id="getUserName" resultType="Java.lang.String">
        SELECT `name` FROM user
    </select>
    
`List<Integer>`、`List<Double>`等如上一样。

***

## Mapper.xml中插入`List<T>`时，SQL的写法

插入列表时，`mapper`接口写法：

    void saveUser(@Param("users") List<User> users);
    
`Mapper.xml`写法：
`collection`参数（"users"）与接口`Param`参数（"users"）一致；
`item`参数（"user"）与`#{}`中参数（"user"）一致，具体写法如下：

    <insert id="saveUser" parameterType="com.mao.entity.User">
        INSERT INTO user(name,phone) VALUES
        <foreach collection="users" item="user" separator=",">
            (#{user.name},#{user.phone})
        </foreach>
    </insert>

***

## MySQL 启动错误：CLIENT_PLUGIN_AUTH is required

使用spring boot 2.0之后，spring boot的mysql Driver变成了：

    com.mysql.cj.jdbc.Driver
在使用老MySQL的时候就会出现此类错误。因此需要更换以下MySQL驱动：
由（2.0版本的MySQL驱动都是8.* 的）：

    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <scope>runtime</scope>
    </dependency>
改为：

    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>5.1.34</version>
    </dependency>
驱动改完后，检查Driver是否需要变成原来的：

    com.mysql.jdbc.Driver
    
***
    
## spring boot连接 Mysql8.0 需要注意的一些问题
#### 1.密码加密方式
MySQL8.0的密码加密方式做了改变。默认使用的是caching_sha2_password的加密方式。
这种加密方式是sha256_password加密方式的升级版。加密性能很好，所以8.0后就默认了这种加密方式。
但是现在的客户端（如：SqlYog、navicat等）都没有实现这种密码解密，因此不想更换大部件的话需要改回到以前。
以前的加密方式是：mysql_native_password。
###### Linux登录MySQL执行（更改密码、刷新权限）：
    ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'your pass';
    FLUSH PRIVILEGES;
###### Windows修改配置文件并重启服务：
    [mysqld]
    default_authentication_plugin=mysql_native_password
客户端可以正常访问MySQL
#### 2.远程连接（跨服务器连接）
MySQL安装完毕后默认的账户是root，默认root绑定的host是‘localhost’，因此root是不被远程连接的。
想要root远程连接就需要更改绑定的host地址，通常情况会改为‘%’（Linux服务器情况）以便任何地方都可以访问。
我通常使用新建用户的方法，避免改动root。
###### Linux登录MySQL执行：
    # 创建新用户（如：test），host指定：%
    create user 'test'@'%' identified by '密码';
    # 更改此用户密码加密方式
    alter user 'test'@'%' identified with mysql_native_password by '密码';
    # 设置远程登陆
    grant all privileges on *.* to 'test'@'%';
    # 刷新权限
    FLUSH PRIVILEGES;
###### Windows修改配置文件并重启服务：
    [mysqld]
    bind-address=0.0.0.0
Windows修改后还是不行可根据Linux方式执行。
#### 3.Maven引入MySQL驱动
spring boot项目的MySQL驱动引入都是由parent自动配置。

    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <scope>runtime</scope>
    </dependency>
spring boot版本改动：
###### 1.* 版本时期：
    Driver： com.mysql.jdbc.Driver
    Maven版本： 5.*
###### 2.* 版本时期：
    Driver： com.mysql.cj.jdbc.Driver
    Maven版本： 5.* - 8.*
都是自动选择版本，因此也不需要担心太多。
一般情况下：5.* 驱动支持MySQL 5.* 及以下版本；8.* 驱动不支持MySQL 5.1.* 以下版本。
#### 4.配置文件配置MySQL参数
除Driver需要注意之外，url的配置参数需要注意：
1. useUnicode
    > 是否使用Unicode字符集，如参数characterEncoding设置为gb2312或gbk，必须设置为true。
      一般不用设置。
2. characterEncoding
    > 指定字符编码：默认utf-8
3. useSSL
    > MySQL5.5以上提示：不建议用户在没有身份验证的情况下使用SSL连接，可设置true、false。
      在不加此参数的情况下，如果报SSL类错误则添加此参数；如果加了此参数会报错则别加了。
4. serverTimezone
    > 设置时区。MySQL驱动6.0以上必须配置。测试过spring boot2.0版本启动都会包timezone问题。
      默认时区为UTC。但与中国标准时区差8小时。建议设置：serverTimezone=Asia/Shanghai