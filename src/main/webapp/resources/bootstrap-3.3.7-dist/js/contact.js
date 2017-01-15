/**
 * Created by Administrator on 2016-10-27.
 */

var updateContactId;
var originalGroupName;//被编辑的原来的组名

// ---START---展示通讯录信息的逻辑

/**
 * 初始化联系人控件
 * @param displayGroup 初始化后立即展示的组
 * @param selectedIds 初始化后立即展示的已选人员
 * @param isEdit 是否需要编辑功能
 */
function initContactsUIs(displayGroup, selectedIds, isEdit) {
    $.when(myAjaxGet(bp + 'Smserver/contacts/groups')).done(function (data) {
        if (data != null) {
            var groups = data['groups'];
            // 初始化groups相关的控件
            if (isEdit) {// 需要编辑 才需要初始化这个控件
                initSelect(groups);// 选择控件
            }
            initGroupsBody(groups, selectedIds, isEdit);// 人员分组部分的UI初始化
            // 立即展示哪一组
            if (selectedIds == null) {// 不需要展示已选人员
                if (displayGroup == null) {
                    showGroupContacts(groups[0], isEdit);
                } else {
                    showGroupContacts(displayGroup, isEdit);
                }
            } else {// 需要展示已选人员
                showSelectedContacts(isEdit);
            }
        }
        showContactsDiv();
    });
}

/**
 * 初始化groups选择链接
 * @param groups 所有的组名
 * @param selectedIds 选择的联系人ids
 * @param isEdit 是否可以编辑
 */
function initGroupsBody(groups, selectedIds, isEdit) {
    var html = '';
    if (selectedIds != null) {// 需要添加已选人员
        html = html + '<tr><td>'
            + '<button type="button" class="btn btn-danger btn-sm" onclick="showSelectedContacts(' + isEdit + ')">' + parseToAbbr('已选人员', 5, null) + '</button>  '
            + '</td></tr>';
    }
    for (var i = 0; i < groups.length; i++) {
        html = html + '<tr><td>'
            + '<button type="button" class="btn btn-danger btn-sm" onclick="showGroupContacts(\'' + groups[i] + '\',' + isEdit + ')">' + parseToAbbr(groups[i], 5, null) + '</button>  ';
        if (isEdit) {
            html = html + '<button type="button" class="btn btn-primary btn-sm" onclick="initEditGroup(\'' + groups[i] + '\')">编辑</button>';
        }
        html = html + '</td></tr>';
    }
    $("#groupsBody").html(html);
}


/**
 * 展示某一组联系人的信息
 * @param groupname 展示的组的名称
 * @param isEdit 是否可以编辑
 */
function showGroupContacts(groupname, isEdit) {
    $.when(getContactsByGroupname(groupname)).done(function (data) {
        if (data != null) {
            initTbodyOfContacts(data['contacts'], isEdit);
        }
    }).fail(function (error) {
        showEditFail("获取" + groupname + "这一组的联系人信息失败。");
    });
}

/**
 * 展示已选人员的信息
 * @param isEdit 是否可以编辑
 */
function showSelectedContacts(isEdit) {
    // 检查是否有选中人员的id
    var selectedIds = $("#selectedContactsIds").val();
    // 判断不为空
    if (isEmpty(selectedIds)) {
        showEditFail("当前还没有选择联系人！", $("#selectedContactsIds"));
        return;
    }
    $.when(getContactsByIds(selectedIds)).done(function (data) {
        if (data != null) {
            initTbodyOfContacts(data['contacts'], isEdit);
        }
    }).fail(function (error) {
        showEditFail("获取已选人员信息失败。");
    });
}

/**
 * 使用联系人json数据组合成联系人表格内容
 * @param contacts 联系人信息队列
 * @param isEdit 是否可以编辑
 */
function initTbodyOfContacts(contacts, isEdit) {
    var htmlStr = '';
    for (var i = 0; i < contacts.length; i++) {
        htmlStr = htmlStr + '<tr><td>' + '<input type="checkbox" name="contactsCheckbox" value="' + contacts[i].base_id + '"> ' + contacts[i].base_id
            + '</td><td>' + contacts[i].groupname
            + '</td><td>' + contacts[i].name
            + '</td><td>' + parseToAbbr(contacts[i].title, 5, null)
            + '</td><td>' + contacts[i].phone
            + '</td><td>' + contacts[i].grade
            + '</td><td>' + parseToAbbr(contacts[i].description, 10, null);
        if (isEdit) {
            htmlStr = htmlStr + '</td><td>' + '<button type="button" class="btn btn-primary btn-sm" onclick="initEditContact(\'' + contacts[i].base_id
                + '\',\'' + contacts[i].groupname + '\',\'' + contacts[i].name + '\',\'' + contacts[i].title + '\',\'' + contacts[i].phone + '\',\'' + contacts[i].grade + '\',\'' + contacts[i].description + '\')">编辑</button>'
                + '</td></tr>';
        } else {
            htmlStr = htmlStr + '</td></tr>';
        }
    }
    $("#contactsBody").html(htmlStr);
    // 取消全选的勾选
    $("#selectAllContacts").prop("checked", false);
}

/**
 * 向服务器请求联系人信息 通过分组名称
 * @param groupname 分组名称
 * @returns {*}
 */
function getContactsByGroupname(groupname) {
    var defer = $.Deferred();
    $.when(myAjaxGet(bp + 'Smserver/contacts/' + groupname)).done(function (data) {
        if (data != null) {
            defer.resolve(data);
        }
    }).fail(function (error) {
        defer.reject(error);
    });
    return defer.promise();
}
/**
 * 向服务器请求联系人信息 通过联系人ids
 * @param ids 联系人ids
 * @returns {*}
 */
function getContactsByIds(ids) {
    var defer = $.Deferred();
    var jsonStr = "{\"ids\":\"" + ids + "\"}";
    $.when(myAjaxPost(bp + 'Smserver/contacts/searchbyids', jsonStr)).done(function (data) {
        if (data != null) {
            defer.resolve(data);
        }
    }).fail(function (error) {
        defer.reject(error);
    });
    return defer.promise();
}


//全选或者反选
function changeSelectAllContacts() {
    if ($("#selectAllContacts").is(':checked')) {
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

// 展示联系人的UI
function showContactsUI() {
    var ids = $("#selectedContactsIds").val();
    showEditDone();
    hideEditFail();
    // 初始化联系人表格
    initContactsUIs(null, ids, false);
    // 显示联系人信息表格div
    showContactsDiv();
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
    $.when(myAjaxPost(bp + 'Smserver/contacts/group/' + action, jsonStr)).done(function (data) {
        var htmlStr = '';
        if (data != null) {
            showEditDone();
            hideEditFail();
            if (action == "insert") {
                $("#createGroupDiv").hide(2000);
            } else {
                $("#editGroupDiv").hide(2000);
            }
            initContactsUIs(groupname, null, true);
        }
    });
}

// 取消编辑组
function cancleEditGroup() {
    $("#editGroupDiv").hide(2000);
    hideEditFail();
}

// 准备好编辑联系人的控件
function initEditContact(id, groupname, name, title, phone, grade, description) {
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
    $("#editContactGrade").val(grade);
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
    var grade = $("#editContactGrade  option:selected").val();
    var description = $("#editContactDescription").val();
    // 判断不为空
    if (isEmpty(name)) {
        showEditFail("必须输入姓名！", $("#editContactName"));
        return;
    }
    if (isEmpty(phone)) {
        showEditFail("必须输入手机号！", $("#editContactPhone"));
        return;
    }
    // 判断手机号格式
    if (false == isValidPhone(phone)) {
        //不对
        showEditFail("手机号格式不对！", $("#editContactPhone"));
        return;
    }
    // 将上述数据整理成json对象
    var jsonStr = "{\"base_id\":" + id
        + ",\"groupname\":\"" + groupname
        + "\",\"name\":\"" + name
        + "\",\"title\":\"" + title
        + "\",\"phone\":\"" + phone
        + "\",\"grade\":\"" + grade
        + "\",\"description\":\"" + description + "\"}";
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
            initTbodyOfContacts(contacts, true);
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

// 初始化单独编辑联系人信息的分组选项
function initSelect(groups) {
    var html = '';
    for (var i = 0; i < groups.length; i++) {
        html = html + '<option value ="' + groups[i] + '">' + groups[i] + '</option>';
    }
    $("#editContactGroup").html(html);
}

// ---START---上传通讯录的逻辑

// 当上传通讯录成功时调用
function uploadSuccess() {
    $("#uploadResult").attr("style", "");
    $("#uploadResult").text("上传的通讯录已经处理完成。");
    // 刷新显示
    initContactsUIs(null, null, true);
}
// 当上传通讯录出现错误时调用
function uploadFailed(msg) {
    var a = UrlDecode(msg);
    $("#uploadResult").attr("style", "background:#F55");
    $("#uploadResult").html(a);
}


// 显示或隐藏上传通讯录div
function showOrHideUpload() {
    $("#uploadDiv").toggle(2000);
}

// ---END---上传通讯录的逻辑


function showContactsDiv() {
    $("#contactsDiv").show(2000);
}

function hideContactsDiv() {
    $("#contactsDiv").hide(2000);
}
