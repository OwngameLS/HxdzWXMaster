/**
 * Created by Administrator on 2016-10-27.
 */

var updateContactId;
var originalGroupName;//被编辑的原来的组名

// 初始化联系人控件
function initContactsUIs(displayGroup) {
    $.when(myAjaxGet(bp + 'Smserver/contacts/groups')).done(function (data) {
        if (data != null) {
            var groups = data['groups'];
            // 初始化groups相关的控件
            initSelect(groups);// 选择控件
            initGroupsBody(groups);// 分组链接
            if (displayGroup == null) {
                getContactsByGroups(groups[0]);
            } else {
                getContactsByGroups(displayGroup);
            }
        }
    });
}


// 向服务器请求联系人信息 通过分组名称
function getContactsByGroups(groupname) {
    $.when(myAjaxGet(bp + 'Smserver/contacts/' + groupname)).done(function (data) {
        if (data != null) {
            var contacts = data['contacts'];
            initTbodyOfContacts(contacts);
        }
    });

}

// 初始化单独编辑联系人信息的分组选项
function initSelect(groups) {
    var html = '';
    for (var i = 0; i < groups.length; i++) {
        html = html + '<option value ="' + groups[i] + '">' + groups[i] + '</option>';
    }
    $("#editContactGroup").html(html);
}

// 初始化groups选择链接
function initGroupsBody(groups) {
    var html = '';
    for (var i = 0; i < groups.length; i++) {
        html = html + '<tr><td>'
            + '<button type="button" class="btn btn-danger btn-sm" onclick="getContactsByGroups(\'' + groups[i] + '\')">' + parseToAbbr(groups[i], 5, null) + '</button>  '
            + '<button type="button" class="btn btn-primary btn-sm" onclick="initEditGroup(\'' + groups[i] + '\')">编辑</button></td></tr>';
    }
    $("#groupsBody").html(html);
}

// 当上传通讯录成功时调用
function uploadSuccess() {
    $("#uploadResult").attr("style", "");
    $("#uploadResult").text("上传的通讯录已经处理完成。");
}
// 当上传通讯录出现错误时调用
function uploadFailed(msg) {
    var a = UrlDecode(msg);
    $("#uploadResult").attr("style", "background:#F55");
    $("#uploadResult").html(a);
}

// 将utf-8形式编码的内容进行解码
function UrlDecode(zipStr) {
    var uzipStr = "";
    for (var i = 0; i < zipStr.length; i++) {
        var chr = zipStr.charAt(i);
        if (chr == "+") {
            uzipStr += " ";
        } else if (chr == "%") {
            var asc = zipStr.substring(i + 1, i + 3);
            if (parseInt("0x" + asc) > 0x7f) {
                uzipStr += decodeURI("%" + asc.toString() + zipStr.substring(i + 3, i + 9).toString());
                i += 8;
            } else {
                uzipStr += AsciiToString(parseInt("0x" + asc));
                i += 2;
            }
        } else {
            uzipStr += chr;
        }
    }
    return uzipStr;
}

function StringToAscii(str) {
    return str.charCodeAt(0).toString(16);
}
function AsciiToString(asccode) {
    return String.fromCharCode(asccode);
}

// 显示或隐藏上传通讯录div
function showOrHideUpload() {
    $("#uploadDiv").toggle(2000);
}


// 使用联系人json数据组合成联系人表格内容
function initTbodyOfContacts(contacts) {
    var htmlStr = '';
    for (var i = 0; i < contacts.length; i++) {
        htmlStr = htmlStr + '<tr><td>' + '<input type="checkbox" name="contactsCheckbox" value="' + contacts[i].id + '"> ' + contacts[i].id
            + '</td><td>' + contacts[i].groupname
            + '</td><td>' + contacts[i].name
            + '</td><td>' + parseToAbbr(contacts[i].title, 5, null)
            + '</td><td>' + contacts[i].phone
            + '</td><td>' + parseToAbbr(contacts[i].description, 10, null)
            + '</td><td>' + '<button type="button" class="btn btn-primary btn-sm" onclick="initEditContact(\'' + contacts[i].id
            + '\',\'' + contacts[i].groupname + '\',\'' + contacts[i].name + '\',\'' + contacts[i].title + '\',\'' + contacts[i].phone + '\',\'' + contacts[i].description + '\')">编辑</button>'
            + '</td></tr>';
    }
    $("#contactsBody").html(htmlStr);
    // 取消全选的勾选
    $("#selectAll").prop("checked", false);
}

//全选或者反选
function changeSelectAll() {
//        console.log("changeSelectAll....." + $("#selectAll").is(':checked'));
    if ($("#selectAll").is(':checked')) {
        $("input[name='contactsCheckbox']").prop("checked", true);// 放弃了attr
    } else {
        $("input[name='contactsCheckbox']").prop("checked", false);
    }
}

// 将选中的联系人Id添加到TextArea中
function addContactIdsToArea() {
    // 原有的ids
    var formerIds = null;
    var a = ('' + $("#GroupContactsIds").val()).trim();
    if (a != '') {
        formerIds = a.split(",");
    } else {
        formerIds = new Array();
    }
    // 当前选中的ids
    var currentSelectIds = new Array();
    var aaa = $("input[name='contactsCheckbox']");
    aaa.each(function () {
        if ($(this).prop("checked")) {
            currentSelectIds.push($(this).val());
        }
    });
    // 合并
    for (var i = 0; i < currentSelectIds.length; i++) {
        var isFound = false;
        for (var j = 0; j < formerIds.length; j++) {
            if (formerIds[j] == currentSelectIds[i]) {
                isFound = true;
                break;
            }
        }
        if (isFound == false) {// 没找到
            formerIds.push(currentSelectIds[i]);
        }
    }
    // 返回字符串
    var tempStr = '';
    for (var j = 0; j < formerIds.length; j++) {
        if (j == 0) {
            tempStr = '' + formerIds[j];
        } else {
            tempStr = tempStr + ',' + formerIds[j];
        }
    }
    $("#GroupContactsIds").val(tempStr);
}
// 清空选择
function emptyContactIds() {
    $("#GroupContactsIds").val("");
}

// 准备好创建新分组的控件
function initCreateGroup(type) {
    $("#createGroupDiv").show(2000);
    if (type == null) {//新建分组
        // 将原有添加到联系人idtextarea的内容清空
        $("#newGroupName").val("");
        $("#GroupContactsIds").val("");
    } else {// 需要集体编辑联系人的分组
        // 获得当前选择的联系人id，然后添加到textarea中
        addContactIdsToArea();
    }
}

// 创建group
function createGroup() {
    // 检查分组名称是否填写
    var groupname = $("#newGroupName").val().trim();
    if (isEmpty(groupname)) {
        // 错误信息
        showEditFail("必须输入组名！", $("#newGroupName"));
        return;
    }
    // 获取分组人员ids
    var pattern = /\d+(,\d+)*/;
    var ids = $("#GroupContactsIds").val().trim();
    if (isEmpty(ids) == false) {
        if (pattern.test(ids) == false) {
            showEditFail("联系人中存在非法字符，两个id之间必须用英文逗号  ','  分隔！", $("#GroupContactsIds"));
            return;
        }
    }
    if (isEmpty(ids)) {
        ids = "empty";
    }
    // 获取联系人操作方式
    var addContactsType = $('input[name="addContactsType"]:checked ').val();
    hideEditFail();
    // 调用ajax方法去更新
    // 将上述数据整理成json对象
    var jsonStr = "{\"groupname\":\"" + groupname
        + "\",\"ids\":\"" + ids
        + "\",\"addContactsType\":\"" + addContactsType + "\"}";
    commitEditGroup('insert', jsonStr, groupname);
}


// 准备好编辑分组信息的控件
function initEditGroup(groupname) {
    // 标记编辑的组名
    originalGroupName = groupname;
    // 显示编辑部分
    $("#editGroupDiv").show(2000);
    // 初始化数据
    $("#editGroupOriginalName").text(groupname);
    $("#editGroupName").val(groupname);
}

// 确认编辑组名
function doEditGroup() {
    var groupname = $("#editGroupName").val();
    // 判断新名称不为空
    if (isEmpty(groupname)) {
        showEditFail("必须输入组名！", $("#editGroupName"));
        return;
    }
    // 将上述数据整理成json对象
    var jsonStr = "{\"originalGroupName\":\"" + originalGroupName
        + "\",\"groupname\":\"" + groupname + "\"}";
    commitEditGroup('update', jsonStr, groupname);
}

// 确认删除该组
function deleteGroup() {
    if (confirm("确认删除？该组下的所有信息将会被删除！")) {
        var jsonStr = "{\"originalGroupName\":\"" + originalGroupName + "\"}";
        commitEditGroup('delete', jsonStr, null);
    } else {
        return;
    }
}

// 提交操作，然后更新页面组组件
function commitEditGroup(action, jsonStr, groupname) {
    $.when(myAjaxPost(bp + 'Smserver/group/' + action, jsonStr)).done(function (data) {
        var htmlStr = '';
        if (data != null) {
            showEditDone();
            hideEditFail();
            if (action == "insert") {
                $("#createGroupDiv").hide(2000);
            } else {
                $("#editGroupDiv").hide(2000);
            }
            initContactsUIs(groupname);
        }
    });
}

// 取消编辑组
function cancleEditGroup() {
    $("#editGroupDiv").hide(2000);
    hideEditFail();
}

// 准备好编辑联系人的控件
function initEditContact(id, groupname, name, title, phone, description) {
    // 标记编辑的id
    updateContactId = id;
    // 显示编辑部分
    $("#editContactDiv").show(2000);
    // 初始化数据
    // 分组是select editContactGroup
    if (groupname == '') {
        // 默认设置为第一个
        var a = $("#editContactGroup option:first").val();
        $("#editContactGroup").val(a);
    } else {
        $("#editContactGroup").val(groupname);
    }
    $("#editContactName").val(name);
    $("#editContactTitle").val(title);
    $("#editContactPhone").val(phone);
    $("#editContactDescription").val(description);
}


// 取消编辑联系人
function cancleEditContact() {
    $("#editContactDiv").hide(2000);
    hideEditFail();
}

// 确实需要更新编辑联系人信息
function doEditContact() {
    var id = updateContactId;
    var groupname = $("#editContactGroup").val();
    var name = $("#editContactName").val();
    var title = $("#editContactTitle").val();
    var phone = $("#editContactPhone").val();
    var description = $("#editContactDescription").val();
    // 判断不为空
    if (name == '' || name == undefined) {
        showEditFail("必须输入姓名！", $("#editContactName"));
        return;
    }
    if (phone == '' || phone == undefined) {
        showEditFail("必须输入手机号！", $("#editContactPhone"));
        return;
    }
    // 判断手机号格式
    if (false == (phone && /^1[3|4|5|8]\d{9}$/.test(phone))) {
        //不对
        showEditFail("手机号格式不对！", $("#editContactPhone"));
        return;
    }
    // 将上述数据整理成json对象
    var jsonStr = "{\"id\":" + id
        + ",\"groupname\":\"" + groupname
        + "\",\"name\":\"" + name
        + "\",\"title\":\"" + title
        + "\",\"phone\":\"" + phone
        + "\",\"description\":\"" + description + "\"}";
    console.log("jsonStr:" + jsonStr);
    commitEditContact('update', jsonStr);
}

// 删除联系人
function deleteContact() {
    var id = updateContactId;
    // 弹出确认对话框
    if (confirm("确认删除？")) {
        var jsonStr = "{\"id\":" + id + "}";
        commitEditContact('delete', jsonStr);
    } else {
        return;
    }
}

// 提交操作，然后更新页面联系人组件
function commitEditContact(action, jsonStr, type) {
    var url = bp + 'Smserver/contacts/' + action;
    $.when(myAjaxPost(url, jsonStr)).done(function (data) {
        var htmlStr = '';
        if (data != null) {
            showEditDone();
            hideEditFail();
            $("#editContactDiv").hide(2000);
            var contacts = data['contacts'];
            initTbodyOfContacts(contacts);
        }
    });
}

// 查询联系人
function searchContact() {
    var name = $("#searchContact").val().trim();
    if (isEmpty(name)) {
        myAnimate($("#searchContact"), 8, $("#searchContact").attr("style"));
        return;
    } else {
        var jsonStr = "{\"name\":\"" + name + "\"}";
        commitEditContact('searchbyname', jsonStr);
    }
}

function cancleCreateGroup() {
    $("#createGroupDiv").hide(2000);
    hideEditFail();
}

