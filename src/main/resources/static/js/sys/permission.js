//权限数据：用于zTree
var permission_json;
//树对象
var zTreeObj;
//树这是
var setting = {
    data:{
        simpleData:{
            enable: true,
            idKey: "id",
            pIdKey: "pId",
            rootPId: 0
        }
    },
    check:{
        enable: true,           //启用勾选框
        nocheckInherit: true,   //父节点取消勾线时可联动子节点
        chkboxType: { "Y": "ps", "N": "ps" }
    }
};

$(function () {
    jqxhr = $.ajax({
        url: '/static/data/permission.json',
        success: function (data) {
            permission_json = data;
            //初始化树
            $(document).ready(function () {
                //初始化树
                zTreeObj = $.fn.zTree.init($("#permission-tree"),setting,permission_json);
                //设置全部展开
                zTreeObj.expandAll(true);
            });
        }
    });
    flush();
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
            //loading撤销
            $("#loading").hide();
            if(data.code === 200){
                //加载数据
                var roles = data.data;
                for(var i=0; i < roles.length; i++){
                    var _name = roles[i].name == null ? '' : roles[i].name;
                    var _intro = roles[i].intro == null ? '' : roles[i].intro;
                    var bar = '<a class="green" href="javascript:intro('+roles[i].id+')">详情</a>' +
                        '<a class="warn" href="javascript:update('+roles[i].id+',\''+_name+'\',\''+_intro+'\')">修改</a>' +
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
        },
        error: function () {
            //loading撤销
            $("#loading").hide();
            pop("网络错误",1);
        }
    });
}

/**
 * 查看该角色权限详情
 * @param id 角色id
 */
function intro(id) {
    //显示详情框
    $("#permission-intro").show();
}

/**
 * 详情信息窗口关闭
 */
function intro_close() {
    $("#permission-intro").hide();
}

/**
 * 修改或新增窗口的关闭
 */
function edit_close() {
    $("#permission-edit").hide();
}

/**
 * 修改该角色的权限
 * @param id 角色id
 * @param name
 * @param intro
 */
function update(id,name,intro) {
    jqxhr = $.ajax({
        url: '/v/sys/permission',
        type: 'get',
        data: {'role':id},
        success: function (data) {
            if (data.code == 200){
                //打开编辑窗口
                $("#permission-edit").show();
                //赋值数据
                $("#role-name").val(name);
                $("#role-intro").val(intro);
                var list = data.data;
                for(var i = 0,len = list.length; i < len; i++){
                    var node = zTreeObj.getNodeByParam("id",list[i]);
                    //这是点击选中的方法，并非复选框选中的方法
                    //zTreeObj.selectNode(node,true);
                    //这是复选框选中的方法
                    zTreeObj.checkNode(node,true,true);
                }
            }else{
                pop("数据请求错误。提示信息："+data.data.err_msg,1);
            }
        }
    });
}

/**
 * 保存编辑的数据
 */
function edit_save() {
    var checked = zTreeObj.getCheckedNodes();
    var ids = [];
    for(var i = 0,len = checked.length; i < len; i++){
        if(checked[i].id > 0)
            ids.push(checked[i].id);
    }
    console.log(ids);
}

/**
 * 删除该角色：最后提示
 * @param id 角色id
 */
function del(id) {
    pop("您确定要删除这个角色么？",2,'onclick=del_do('+id+')');
}

/**
 * 删除该角色：具体操作
 * @param id 角色id
 */
function del_do(id) {
    tips("功能暂未开放");
}

/**
 * 新增数据
 */
function save() {
    $("#permission-edit").show();
    //取消所有选中
    zTreeObj.checkAllNodes(false);
    //清空角色数据
    $("#role-name").val('');
    $("#role-intro").val('');
}

/**
 * 刷新数据
 */
function flush() {
    //loading层缓冲
    $("#loading").show();
    setTimeout(function () {
        load();
    },500);
}