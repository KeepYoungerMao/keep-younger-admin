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
    //初始化权限详情信息
    permission_intro();
    //加载角色列表
    flush();
});

/**
 * 初始化权限详情信息
 */
function permission_intro() {
    jqxhr = $.ajax({
        url: '/static/data/permission-intro.json',
        success: function (data) {
            var table = $(".permission-intro-table");
            for(var i = 0,len = data.length; i < len; i++){
                var x = getX(data[i].level);
                var tr = '<tr><td data-id="'+data[i].id+'" class="'+x+'">'+
                    '<i class="l fa fa-'+data[i].i+'"></i><span>'+data[i].name+'</span>'+
                    '</td></tr>';
                $(table).append(tr);
            }
        }
    });
}

function getX(level) {
    switch(level){
        case 1:
            return "x";
        case 2:
            return "xx";
        case 3:
            return "xxx";
        default:
            return "x";
    }
}

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
    //请求权限数据
    jqxhr = $.ajax({
        url: '/v/sys/permission',
        type: 'GET',
        data: {'role':id},
        success: function (data) {
            if (data.code == 200){
                var ids = data.data;
                for(var i = 0,len = ids.length; i < len; i++){
                    //点亮
                    var td = document.querySelector('.permission-intro-table .xxx[data-id="'+ids[i]+'"]');
                    $(td).find("i").addClass("has");
                    $(td).find("span").addClass("has");
                    $(td).append('<b class="gou fa fa-check-circle-o"></b>');
                }
            }else{
                pop("请求数据失败。提示信息："+data.data.err_msg,1);
            }
        },
        error: function () {
            pop("请求数据失败，网络错误",1);
        }
    });
}

/**
 * 详情信息窗口关闭
 */
function intro_close() {
    $("#permission-intro").hide();
    //去除选中样式
    var table = $(".permission-intro-table");
    $(table).find("td i").removeClass("has");
    $(table).find("td span").removeClass("has");
    $(table).find("td b").remove();
}

/**
 * 修改或新增窗口的关闭
 */
function edit_close() {
    $("#permission-edit").hide();
}

/**
 * 修改该角色的权限：回显角色权限数据
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
                //赋值id
                $("#role-id").val(id);
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
 * 保存编辑的数据，更新或保存
 */
function edit_save() {
    //获取角色数据
    var id = $("#role-id").val();
    var name = $("#role-name").val();
    var intro = $("#role-intro").val();
    if(undefined == name || null == name || '' == name)
        tips("角色名称不能为空");
    else {
        if (undefined == intro || null == intro || '' == intro){
            tips("角色描述不能为空");
        }else{
            var checked = zTreeObj.getCheckedNodes();
            var ids = [];
            for(var i = 0,len = checked.length; i < len; i++){
                if(checked[i].id > 1000)
                    ids.push(checked[i].id);
            }
            var role;
            if(undefined == id || null == id || '' == id){
                //新增
                role = {
                    name: name,
                    intro: intro,
                    permissions: ids
                };
                jqxhr = $.ajax({
                    url: '/v/sys/role',
                    type: 'put',
                    data: JSON.stringify(role),
                    headers: {'Content-Type':'application/json','Accept':'*/*'},
                    success: function (data) {
                        if (data.code == 200){
                            //隐藏编辑框
                            $("#permission-edit").hide();
                            //提示成功
                            tips("保存成功");
                            //刷新列表
                            flush();
                        }else{
                            //提示错误
                            pop("保存失败。提示信息："+data.data.err_msg,1);
                        }
                    },
                    error: function () {
                        pop("网络错误。保存失败！",1);
                    }
                });
            }else{
                //更新
                role = {
                    id: parseInt(id),
                    name: name,
                    intro: intro,
                    permissions: ids
                };
                jqxhr = $.ajax({
                    url: '/v/sys/role',
                    type: 'post',
                    data: JSON.stringify(role),
                    headers: {'Content-Type':'application/json','Accept':'*/*'},
                    success: function (data) {
                        if (data.code == 200){
                            //隐藏编辑框
                            $("#permission-edit").hide();
                            //提示成功
                            tips("更新成功");
                            //刷新列表
                            flush();
                        }else{
                            //提示错误
                            pop("更新失败。提示信息："+data.data.err_msg,1);
                        }
                    },
                    error: function () {
                        pop("网络错误。更新失败！",1);
                    }
                });
            }
        }
    }
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
 * 新增数据：开启编辑框，清空数据
 * 新增数据的保存见：edit_save()
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