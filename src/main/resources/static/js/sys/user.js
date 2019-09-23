//页码，总页数
var page = 1;
var total = 1;

//角色，名称，登录名关键字
var _role = null;
var _name = null;
var _login = null;

$(function () {
    //加载角色选择列表
    $.ajax({
        url: '/v/sys/role',
        type: 'GET',
        success: function (data) {
            if(data.code == 200){
                //加载数据
                var roles = data.data;
                for(var i=0; i < roles.length; i++){
                    var _option = '<option value="'+roles[i].id+'">'+roles[i].name+'</option>';
                    $("#role").append(_option);
                    $("#user_role").append(_option);
                }
                //加载数据列表
                search();
            }
        }
    });
});

/**
 * 查询用户数据总页数
 * @param role 角色id
 * @param name 名字关键字
 * @param login 登录名关键字
 */
function count(role,name,login) {
    $.ajax({
        url: '/v/sys/users',
        type: 'GET',
        data:{'role':role,'name':name,'login':login},
        success: function (data) {
            if(data.code == 200){
                total = data.data;
                $("#total_page").html(total);
            }else{
                pop("用户数据加载失败："+data.err_msg);
            }
        }
    });
}

/**
 * 查询用户数据列表
 * @param role 角色id
 * @param name 名字关键字
 * @param login 登录名关键字
 */
function list(role,name,login) {
    $.ajax({
        url: '/v/sys/user',
        type: 'GET',
        data:{'role':role,'name':name,'login':login,'page':page},
        success: function (data) {
            if(data.code == 200){
                load(data.data);
            }else{
                pop("用户数据加载失败："+data.err_msg);
            }
        }
    });
}

/**
 * 加载数据列表
 * @param data 数据列表
 */
function load(data) {
    var table = $("#user-data");
    //清空数据
    $(table).find(".user-item").remove();
    if(data.length <= 0){
        pop("用户当前没有数据",1);
    }else{
        //组合数据
        for(var i=0; i < data.length; i++){
            var id = '<td>'+data[i].id+'</td>';
            var name = '<td>'+data[i].user_name+'</td>';
            var login = '<td>'+data[i].user_login+'</td>';
            var locked = data[i].locked ? '<td><span class="warn">锁定</span></td>' :
                '<td><span class="green">正常</span></td>';
            var role = '<td>'+data[i].role_name+'</td>';
            var bar = '<td>'+
                '<a class="green" href="javascript:intro('+data[i].id+')">详情</a>'+
                '<a class="warn" href="javascript:update('+data[i].id+')">修改</a>'+
                '<a class="danger" href="javascript:del('+data[i].id+')">删除</a>'+
                '</td>';
            var html = '<tr class="user-item">'+id+name+login+locked+role+bar+'</tr>';
            $(table).append(html);
        }
        $("#current_page").html(page);
    }
}

/**
 * 加载上一页数据
 */
function pre_page() {
    if(page == 1){
        tips("当前已经是第一页");
    }else{
        page --;
        list(_role,_name,_login);
    }
}

/**
 * 加载下一页数据
 */
function next_page() {
    if(page >= total){
        tips("已经是最后一页了！");
    }else{
        page ++;
        list(_role,_name,_login);
    }
}

/**
 * 跳转到某一页
 */
function to_page() {
    var to_page = $("#to_page").val();
    if(undefined != to_page && null != to_page && '' != to_page){
        to_page = parseInt(to_page);
        if(to_page > 0 && to_page <= total){
            page = to_page;
            list(_role,_name,_login);
        }
    }
}

/***
 * 检索
 */
function search() {
    //loading层缓冲
    $("#loading").show();
    setTimeout(function () {
        //loading撤销
        $("#loading").hide();

        _role = $("#role").find("option:selected").val();
        var name = $("#name").val();
        var login = $("#login").val();
        if(undefined != name && null != name)
            _name = name;
        else
            _name = null;
        if(undefined != login && null != login)
            _login = login;
        else
            _login = null;
        //检索一次后page重新回到1，total重新查询
        page = 1;
        count(_role,_name,_login);
        list(_role,_name,_login);
    },500);
}

/**
 * 查看用户数据详情
 * div：#user-intro
 * @param id 用户id
 */
function intro(id) {
    //打开intro box
    var intro = $("#user-intro");
    $(intro).show();
    //清空数据
    $(intro).find(".mao-box-content").empty();
    //加载loading
    //请求数据
    $.ajax({
        url: '/v/sys/user/'+id,
        type: 'get',
        success: function (data) {
            if (data.code == 200){
                var user = data.data;
                var note = (null != user.note && '' != user.note) ? user.note :
                    '这个人很懒，什么也没有留下';
                var html = '<div class="user-intro-title">'+
                '<div class="user-intro-img">'+
                '<img src="'+user.image+'" alt="">'+
                '</div>'+
                '<div class="user-intro-names">'+
                '<div class="user-intro-names-item">'+
                '<span class="user-intro-name warn" title="姓名">'+user.user_name+'</span>'+
                '<span class="user-intro-role" title="角色">'+user.role_name+'</span>'+
                '</div>'+
                '<div class="user-intro-names-item">'+
                '<span class="user-intro-company" title="所属公司">'+user.company+'</span>'+
                '<span class="user-intro-dept" title="所属部门">'+user.dept+'</span>'+
                '</div>'+
                '</div>'+
                '</div>'+
                '<div class="user-ss">'+
                '<p>'+
                '<span>简介：</span>'+note+
                '</p>'+
                '</div>'+
                '<div class="user-ss">'+
                '<span><i class="fa fa-user-circle-o"></i>登陆账号：</span>'+
                '<b>'+user.user_login+'</b>'+
                '</div>'+
                '<div class="user-ss">'+
                '<span><i class="fa fa-credit-card"></i>身份证号：</span>'+
                '<b>'+user.identity_code+'</b>'+
                '</div>'+
                '<div class="user-ss">'+
                '<span><i class="fa fa-university"></i>现住地址：</span>'+
                '<b>'+user.address+'</b>'+
                '</div>'+
                '<div class="user-ss">'+
                '<span><i class="fa fa-qq"></i>QQ号码：</span>'+
                '<b>'+user.qq+'</b>'+
                '</div>'+
                '<div class="user-ss">'+
                '<span><i class="fa fa-weixin"></i>微信号码：</span>'+
                '<b>'+user.wx+'</b>'+
                '</div>'+
                '<div class="user-ss">'+
                '<span><i class="fa fa-volume-control-phone"></i>手机号码：</span>'+
                '<b>'+user.phone+'</b>'+
                '</div>'+
                '<div class="user-ss">'+
                '<span><i class="fa fa-envelope-o"></i>邮箱地址：</span>'+
                '<b>'+user.email+'</b>'+
                '</div>';
                $(intro).find(".mao-box-content").append(html);
            }else{
                $(intro).find(".mao-box-content")
                    .append('<div class="user-intro-error">加载用户信息失败</div>');
            }
        },
        error: function (e) {
            console.log(e);
            $(intro).find(".mao-box-content")
                .append('<div class="user-intro-error">加载用户信息失败</div>');
        }
    });
}

/**
 * 用户信息弹框关闭
 */
function intro_close() {
    $("#user-intro").hide();
}

/**
 * 新建或更新弹框关闭
 */
function edit_close() {
    $("#user-edit").hide();
}

/**
 * 新增一个用户数据
 * 新增时候的编辑框打开
 */
function save() {
    data_clear();
    //密码编辑框显示
    $("#user_pass").parent().parent().show();
    //清空id
    $("#id").val(null);
    $("#user-edit").show();
}

/**
 * 更新一个用户数据方法
 * 打开编辑框，赋值id
 * 更新不更新密码
 * @param id 用户id
 */
function update(id) {
    $("#user_pass").parent().parent().hide();
    if(undefined == id || null == id || id <= 0)
        tips("unknown this id : "+id);
    else
    //查询数据
    $.ajax({
        url: '/v/sys/user/' + id,
        type: 'get',
        success: function (data) {
            if(data.code == 200){
                var user = data.data;
                //赋值id
                $("#id").val(id);
                //赋值其它
                $("#user_name").val(user.user_name);
                $("#user_login").val(user.user_login);
                //$("#user_pass").val();
                $("#user_role").find("option:selected").val();
                $("#company").val(user.company);
                $("#dept").val(user.dept);
                $("#identity_code").val(user.identity_code);
                $("#address").val(user.address);
                $("#phone").val(user.phone);
                $("#email").val(user.email);
                $("#qq").val(user.qq);
                $("#wx").val(user.wx);
                //打开编辑框
                $("#user-edit").show();
            }else{
                pop("无法编辑，由于请求数据失败。",1);
            }
        }
    });
}

/**
 * 更新或保存一个用户数据
 * 数据的提交
 */
function update_save_do() {
    var id = $("#id").val();
    var user_name = $("#user_name").val();
    var user_login = $("#user_login").val();
    var user_pass = $("#user_pass").val();
    var user_role = $("#user_role").find("option:selected").val();
    var company = $("#company").val();
    var dept = $("#dept").val();
    var identity_code = $("#identity_code").val();
    var address = $("#address").val();
    var phone= $("#phone").val();
    var email = $("#email").val();
    var qq = $("#qq").val();
    var wx = $("#wx").val();
    var image = '/static/img/user.jpg';
    if(user_name && user_login && user_role &&
        company && dept && identity_code && address && phone && email){
        var user;
        if(undefined != id && null != id && id > 0){
            //更新操作(pass 不操作)
            user = new User(id,user_name,user_login,'',user_role,company,dept,
            image,identity_code,address,qq,wx,phone,email);
            $.ajax({
                url: '/v/sys/user',
                type: 'POST',
                data: JSON.stringify(user),
                headers: {'Content-Type':'application/json','Accept':'*/*'},
                success: function (data) {
                    if (data.code == 200){
                        edit_close();
                        tips("更新成功");
                        //刷新
                        search();
                    }else{
                        pop("更新失败。错误提示："+data.data.err_msg,1);
                    }
                },
                error: function () {
                    pop("网络错误，请稍后重试。");
                }
            });
        }else{
            //保存操作
            user = new User(0,user_name,user_login,user_pass,user_role,company,dept,
                image,identity_code,address,qq,wx,phone,email);
            $.ajax({
                url: '/v/sys/user',
                type: 'PUT',
                data: JSON.stringify(user),
                headers: {'Content-Type':'application/json','Accept':'*/*'},
                success: function (data) {
                    if (data.code == 200){
                        edit_close();
                        tips("保存成功");
                        //刷新
                        search();
                    }else{
                        pop("保存失败。错误提示："+data.data.err_msg,1);
                    }
                },
                error: function () {
                    pop("网络错误，请稍后重试。");
                }
            });
        }
    }else{
        tips("请检查数据完整性");
    }
}

/**
 * 清空
 */
function data_clear() {
    //除id之外清空，
    var _id = $("#id");
    var box = $("#user-edit");
    var id = $(_id).val();
    $(box).find("input").val('');
    $(box).find("textarea").val('');
    $(_id).val(id);
}

/**
 * 刷新主界面列表
 * 清除关键字数据
 */
function flush() {
    document.getElementById("role").options[0].selected = true;
    $("#name").val('');
    $("#login").val('');
    search();
}

/**
 * 删除一个用户数据
 * @param id 用户id
 */
function del(id) {
    pop('你确定要删除这个数据么？',2,'onclick="del_do('+id+')"');
}
//删除要做的工作
function del_do(id) {
    tips("当前无法删除数据");
}

/**
 * 用户对象
 * @param id id
 * @param user_name 用户名
 * @param user_login 登录名
 * @param user_pass 密码
 * @param user_role 角色id
 * @param company 公司
 * @param dept 部门
 * @param image 图片地址
 * @param identity_code 身份证号
 * @param address 现住地址
 * @param qq QQ
 * @param wx 微信
 * @param phone 手机号
 * @param email 邮箱
 */
function User(id,user_name,user_login,user_pass,user_role,company,
              dept,image,identity_code,address,qq,wx,phone,email) {
    this.id = id;
    this.user_name = user_name;
    this.user_login = user_login;
    this.user_pass = user_pass;
    this.user_role = user_role;
    this.company = company;
    this.dept = dept;
    this.image = image;
    this.identity_code = identity_code;
    this.address = address;
    this.qq = qq;
    this.wx = wx;
    this.phone = phone;
    this.email = email;
}