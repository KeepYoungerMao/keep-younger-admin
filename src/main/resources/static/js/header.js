var user = JSON.parse(sessionStorage.getItem("user"));
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
 * 更新用户便签
 */
function note_set() {
    console.log(1);
    pop('功能即将上线！',1);
}

/**
 * 更换头像
 */
function trans_user_image() {
    pop('功能即将上线！',1);
}

function logout() {
    window.location.href = "/logout";
}