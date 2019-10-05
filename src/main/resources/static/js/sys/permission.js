$(function () {
    load();
});

/**
 * 加载数据
 */
function load() {
    var table = $("#role-data");
    //清空数据
    $(table).find(".role-item").remove();
    //加载角色列表
    jqxhr = $.ajax({
        url: '/v/sys/role',
        type: 'GET',
        success: function (data) {
            if(data.code === 200){
                //加载数据
                var roles = data.data;
                for(var i=0; i < roles.length; i++){
                    var bar = '<a class="green" href="javascript:intro('+roles[i].id+')">详情</a>' +
                        '<a class="warn" href="javascript:update('+roles[i].id+')">修改</a>' +
                        '<a class="danger" href="javascript:del('+roles[i].id+')">删除</a>';
                    var tr = '<tr class="role-item">' +
                        '<td>'+roles[i].id+'</td>' +
                        '<td>'+roles[i].name+'</td>' +
                        '<td>'+roles[i].intro+'</td>' +
                        '<td>'+bar+'</td>' +
                        '</tr>';
                    $(table).append(tr);
                }
            }
        }
    });
}

function intro(id) {

}

function update(id) {

}

function del(id) {

}

/**
 * 新增数据
 */
function save() {

}

/**
 * 刷新数据
 */
function flush() {

}