var user = JSON.parse(sessionStorage.getItem("user"));

/**
 * 展示用户便签
 */
function show_user_note() {
    if(null != user.note && '' != user.note){
        if(user.note.length > 24)
            $("#user-note-title")
                .append('<marquee class="user-note" behavior="scroll" direction="left">' +
                    user.note + '</marquee>');
        else
            $("#user-note-title").append('<b class="user-note">'+user.note+'</b>');
    }else{
        $("#user-note-title").append('<b class="user-note" style="font-size: 14px">' +
            '您暂时还没有设置说说。</b>');
    }
}

show_user_note();

$("#user-function").append('<img src="'+user.image+'" onclick="sys_function()" >');

/**
 * 功能块展开
 */
function sys_function() {
    $("#sys-function-zzc").show();
}

/**
 * 功能块收起
 */
function sys_function_cancel() {
    $("#sys-function-zzc").hide();
}

/**
 * 更新用户便签：窗口打开
 */
function note_set() {
    $("#user-note-data").val(user.note);
    $("#user-note-edit").show();
}

/**
 * 去往个人资料页面
 */
function trans_user_intro() {
    window.location.href = "/sys/self";
}

/**
 * 退出登录
 */
function logout() {
    window.location.href = "/logout";
}

/**
 * 用户便签修改窗口关闭
 */
function user_note_close() {
    $("#user-note-data").val('');
    $("#user-note-edit").hide();
}

/**
 * 用户便签的更新
 */
function user_note_update() {
    var id = user.id;
    var old_note = user.note;
    var new_note = $("#user-note-data").val();
    if(null == new_note || '' == new_note){
        pop("请填写便签内容再保存！",1);
    }else if(old_note == new_note){
        pop("便签内容未作改变！",1);
    }else{
        jqxhr = $.ajax({
            url: '/v/sys/self/note',
            type: 'POST',
            data: {'id':id,'note':new_note},
            success: function (data) {
                if(data.code == 200){
                    user_note_close();
                    tips("更新成功");
                    //更新成功后改变用户session便签值和便签显示
                    user.note = new_note;
                    sessionStorage.setItem("user",JSON.stringify(user));
                    user = JSON.parse(sessionStorage.getItem("user"));
                    var title = $("#user-note-title");
                    $(title).find("marquee").remove();
                    $(title).find("b").remove();
                    show_user_note();
                }else{
                    pop("更新失败。错误提示："+data.data.err_msg);
                }
            },
            error: function () {
                pop("网络错误！请稍后再试。",1);
            }
        });
    }
}