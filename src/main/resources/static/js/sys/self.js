$(function () {
    //加载数据
    load_self(user.id);
});

/**
 * 加载用户数据
 * @param id
 */
function load_self(id) {
    jqxhr = $.ajax({
        url: '/v/sys/user/'+id,
        type: 'get',
        success: function (data) {
            if(data.code == 200){
                load_self_do(data.data);
            }else{
                pop("加载用户信息失败！提示信息："+data.data.err_msg);
            }
        },
        error: function (e) {
            console.log(e);
            pop("网络错误。请稍后再试。",1);
        }
    });
}

/**
 * 请求数据放入桌面
 * @param user 用户数据
 */
function load_self_do(user) {
    sessionStorage.setItem("user",JSON.stringify(user));
    $("#self-name").html(user.user_name);
    if(user.locked){
        $("#self-locked").addClass("warn").html("锁定");
    }else {
        $("#self-locked").addClass("green").html("正常");
    }
    $("#self-role").html(user.role_name);
    $("#self-dept").html(user.dept);
    $("#self-company").html(user.company);
    $("#self-image").attr("src",user.image);
    $("#self-login").val(user.user_login);
    $("#self-note").val(user.note);
    $("#self-id-card").html(user.identity_code);
    if(null != user.address)
        $("#self-address").val(user.address);
    $("#self-phone").val(user.phone);
    $("#self-email").val(user.email);
    if(null != user.qq)
        $("#self-qq").val(user.qq);
    if(null != user.wx)
        $("#self-wx").val(user.wx);
}

/**
 * 修改按钮的点击操作
 * 隐藏修改按钮、显示保存、取消按钮
 * input或textarea可编辑
 * 将原值储存在data-src上
 * @param e 编辑框对象
 */
function self_edit(e) {
    //编辑按钮隐藏
    $(e).siblings("i").css("visibility","hidden");
    //保存取消按钮显示
    $(e).siblings(".self-edit-yes").css("visibility","visible");
    $(e).siblings(".self-edit-cancel").css("visibility","visible");
    //编辑框可编辑
    $(e).prop("disabled","");
    $(e).addClass("self-can-edit");
    //保存原始值
    $(e).attr("data-src",$(e).val());
}

/**
 * 保存编辑数据
 * @param e 编辑框对象
 * @param name 参数名
 */
function self_edit_save(e,name) {
    //获取原始值
    var src = $(e).data("src");
    //获取新值
    var _new = $(e).val();
    if(src == _new){
        pop("数据没有改变，不执行更新操作。",1);
    }else{
        var _id = user.id;
        var data = '{"id":'+_id+',"'+name+'":"'+_new+'"}',
        jqxhr = $.ajax({
            url: '/v/sys/self',
            type: 'POST',
            data: data,
            headers: {'Content-Type':'application/json','Accept':'*/*'},
            success: function (data) {
                if(data.code == 200){
                    self_edit_cancel(e,_new);
                    tips("更新成功");
                }else{
                    pop("更新失败。提示信息："+data.data.err_msg,1);
                }
            },
            error: function () {
                pop("网络错误。请稍后再试。");
            }
        });
    }
}

/**
 * 取消编辑
 * @param e 编辑框对象
 * @param m 编辑框赋值的数据，用于成功后的“取消编辑”时的赋值
 */
function self_edit_cancel(e,m) {
    //按钮隐藏
    $(e).siblings(".self-edit-cancel").css("visibility","hidden");
    $(e).siblings(".self-edit-yes").css("visibility","hidden");
    //编辑按钮可展现
    $(e).siblings("i").removeAttr("style");
    //编辑框不可编辑
    $(e).prop("disabled","disabled");
    $(e).removeClass("self-can-edit");
    //赋值原来的值
    var src = $(e).data("src");
    if(undefined != m && null != m){
        //由于保存成功后的操作执行的一样的程序。但是赋值不一样，因此这里加个判断，与方法合用
        $(e).val(m);
    }else{
        $(e).val(src);
    }
    //编辑框删除原值
    $(e).attr("data-src","");
}

/**
 * 修改图片按钮功能
 * 再次确认
 */
function self_edit_image() {
    pop("确定要更换此图片为头像吗？",2,'onclick="self_edit_image_do()"');
}

/**
 * 修改头像图片
 */
function self_edit_image_do() {
    var fileObj = document.querySelector("#self-image-file").files[0];
    console.log(fileObj);
    var formData = new FormData();
    formData.append("file",fileObj);
    jqxhr = $.ajax({
        url: '/v/sys/self/image',
        type: 'post',
        dataType: 'json',
        data: formData,
        async: false,
        cache: false,
        contentType: false,
        processData: false,
        headers: {"Accept":"*/*"},
        success: function (data) {
            if(data.code == 200){
                console.log(data.data);
                user.image = data.data;
                sessionStorage.setItem("user",JSON.stringify(user));
                pop("更换成功",1);
            }else{
                pop("更换头像失败：提示信息："+data.data.err_msg,1);
            }
        },
        error: function () {
            tips("网络错误。请稍后再试。");
        }
    });
    //保存之后的事：
    $("#self-image-file").val(null);
    $("#self-image").attr("src",user.image);
    $("#self-image-save").css("visibility","hidden");
}

/**
 * 图片的改变事件
 * 用于回显。没有选择图片
 * @param e
 * @param name
 */
function changImg(e,name) {
    if (e.target.files.length <= 0){
        console.log(user.image);
        $("#self-image").attr("src",user.image);
        $("#self-image-save").css("visibility","hidden");
    } else{
        $("#self-image-save").css("visibility","visible");
        for (var i = 0; i < e.target.files.length; i++) {
            var file = e.target.files.item(i);
            if (!(/^image\/.*$/i.test(file.type))) {
                continue; //不是图片 就跳出这一次循环
            }
            //实例化FileReader API
            var freader = new FileReader();
            freader.readAsDataURL(file);
            freader.onload = function (e) {
                console.log(e.target.result);
                $("#"+ name).attr("src", e.target.result);
            }
        }
    }
}