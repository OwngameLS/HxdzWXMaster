<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@page language="java" contentType="text/html; charset=utf-8"
        pageEncoding="utf-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<html>
<head>
    <title>Bootstrap 模板</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <!-- 引入 Bootstrap -->
    <link href="../../resources/bootstrap-3.3.7-dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="../../resources/bootstrap-3.3.7-dist/js/uiscript.js"></script>
</head>
<body>
<h3>通讯录操作</h3>
<button type="button" class="btn btn-primary btn-sm" onclick="showOrHideUpload()">上传通讯录</button>
<div id="uploadDiv" class="bg-success" style="padding: 5px;display: none"><%--文件上传部分--%>
    <dl>
        <dd>
            <p class="text-warning">
                注意：<br>
                1.每次上传的文件将会覆盖原有数据，请仔细操作！
                2.如果你不知道上传什么文件，请下载模板文件进行修改后再上传。
                <a href="<%=basePath%>Smserver/download">点击下载《通讯录模板文件》</a>
            </p>
        </dd>
    </dl>
    <form role="form" action="<%=basePath%>Smserver/doUpload" method="post" enctype="multipart/form-data"
          target="hidden_frame">
        <div class="form-group">
            <label for="inputfile">选择文件</label>
            <input type="file" id="inputfile" name="file" accept=".xls">
        </div>
        <button id="submit" type="submit" class="btn btn-default">提交</button>
    </form>
    <br>
    <div id="uploadResult"></div>
    <iframe name='hidden_frame' id="hidden_frame" style='display:none'></iframe>
</div>

<div class="danger" style="padding: 5px;"><%--通讯录操作部分--%>
    <dl>
        <dt>编辑通讯录信息</dt>
        <dd>
            <p class="text-warning">
                1.每次上传的文件将会覆盖原有数据，请仔细操作！
            </p>
        </dd>
    </dl>
    <div id="editDoneDiv" class="bg-success"
         style="display: none;width: 30%;margin:0 auto;text-align:center"><%--操作完成--%>
        操作完成！
    </div>

    <div id="editFailDiv" class="bg-danger"
         style="padding:5px;display: none;width: 30%;margin:0 auto;text-align:center"><%--操作失败--%>
        <h5>操作失败！</h5>
        <p class="text-danger" id="failCause"></p>
    </div>


    <div id="editContactDiv" class="bg-warning" style="padding: 5px;display: none;text-align:center"><%--单个联系人信息修改部分--%>
        <table class="text-center">
            <thead>
            <tr class="warning">
                <th class="text-center">所在分组</th>
                <th class="text-center">姓名</th>
                <th class="text-center">职务</th>
                <th class="text-center">手机号</th>
                <th class="text-center">备注</th>
                <th class="text-center">操作</th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td>
                    <select id="editContactGroup"></select>
                </td>
                <td><input class="form-control" id="editContactName" placeholder="姓名"></td>
                <td><input class="form-control" id="editContactTitle" placeholder="职务"></td>
                <td><input class="form-control" id="editContactPhone" placeholder="手机号"></td>
                <td><input class="form-control" id="editContactDescription" placeholder="备注"></td>
                <td>
                    <button type="button" class="btn btn-primary btn-sm" onclick="doEditContact()">完成</button>
                    <button type="button" class="btn btn-danger btn-sm" onclick="deleteContact()">删除</button>
                    <button type="button" class="btn btn-warning btn-sm" onclick="cancleEditContact()">取消</button>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
    <div id="editGroupDiv" class="bg-warning" style="padding: 5px;display: none;text-align:center"><%--单个组信息修改部分--%>
        <table class="text-center">
            <thead>
            <tr class="warning">
                <th class="text-center">原名称</th>
                <th class="text-center">新名称</th>
                <th class="text-center">操作</th>
                <th class="text-center">说明</th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td id="editGroupOriginalName"></td>
                <td><input class="form-control" id="editGroupName" placeholder="新组的名称"></td>
                <td>
                    <button type="button" class="btn btn-primary btn-sm" onclick="doEditGroup()">修改</button>
                    <button type="button" class="btn btn-danger btn-sm" onclick="deleteGroup()">删除</button>
                    <button type="button" class="btn btn-warning btn-sm" onclick="cancleEditGroup()">取消</button>
                </td>
                <td><h6>操作后该组的所有数据都会被更新</h6></td>
            </tr>
            </tbody>
        </table>
    </div>

    <div id="createGroupDiv" class="bg-warning" style="padding: 5px;display: none;text-align:center"><%--创建新分组部分--%>
        <table class="text-center table-condensed">
            <thead>
            <tr class="warning">
                <th class="text-center">分组名称</th>
                <th class="text-center">分组人员</th>
                <th class="text-center">选项</th>
                <th class="text-center">操作</th>
                <th class="text-center">说明</th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td width="10%"><input class="form-control" id="newGroupName" placeholder="新组的名称"></td>
                <td width="30%">
                    <textarea class="form-control" id="GroupContactsIds" placeholder="这里将展示出添加至该组的人员名单"></textarea>
                    <button type="button" class="btn btn-primary btn-sm" onclick="emptyContactIds()">清空</button>
                </td>
                <td width="10%">
                    <input type="radio" name="addContactsType" id="addContactsType1" value="copy" checked>复制<br>
                    <input type="radio" name="addContactsType" id="addContactsType2" value="move">迁移
                </td>
                <td width="10%">
                    <button type="button" class="btn btn-primary btn-sm" onclick="createGroup()">确定</button>
                    <button type="button" class="btn btn-warning btn-sm" onclick="cancleCreateGroup()">取消</button>
                </td>
                <td width="30%">分组名如果为已有分组名，则会将选中的联系人分组进行操作；如果为新分组，则根据“选项”来确定如何操作联系人</td>
            </tr>
            </tbody>
        </table>
    </div>
    <div id="groups" style="width: 20%;float:left;">
        <button type="button" class="btn btn-warning btn-sm" onclick="initCreateGroup(null)">新建分组</button>
    </div>
    <div id="persons" style="width:80%;float:left;">
        <button type="button" class="btn btn-warning btn-sm" onclick="initEditContact(-1,'','','','','')">新建联系人</button>
        <button type="button" class="btn btn-success btn-sm" onclick="initCreateGroup('abc')">集体编辑</button>
        <input id="searchContact" placeholder="输入人员名字">
        <button type="button" class="btn btn-primary btn-sm" onclick="searchContact()">搜索</button>
    </div>
    <div id="groups" style="width: 20%;float:left;">
        <table class="table table-striped text-center table-bordered">
            <thead>
            <tr class="warning">
                <th class="text-center">
                    点击下列分组查看组员
                </th>
            </tr>
            </thead>
            <tbody id="groupsBody">
            </tbody>
        </table>
    </div>
    <div id="persons" style="width:80%;float:left;overflow:scroll; height:400px;">
        <table class="table table-hover table-bordered text-center">
            <thead>
            <tr class="info">
                <th class="text-center">序号（<input type="checkbox" id="selectAll" onclick="changeSelectAll()">全选）</th>
                <th class="text-center">所在分组</th>
                <th class="text-center">姓名</th>
                <th class="text-center">职务</th>
                <th class="text-center">手机号</th>
                <th class="text-center">备注</th>
                <th class="text-center">操作</th>
            </tr>
            </thead>
            <tbody id="contactsBody">
            </tbody>
        </table>
    </div>
</div>

<%--参考：http://www.codingyun.com/article/50.html--%>

<!-- jQuery (Bootstrap 的 JavaScript 插件需要引入 jQuery) -->
<script src="../../resources/bootstrap-3.3.7-dist/js/jquery-3.1.0.min.js"></script>
<!-- 包括所有已编译的插件 -->
<script src="../../resources/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>

<script type="application/javascript">
    var bp = '<%=basePath%>';
    var updateContactId;
    var originalGroupName;//被编辑的原来的组名

    // 文档被加载完成时
    $(document).ready(function () {
        initContactsUIs(null);
    });
    // 初始化联系人控件
    function initContactsUIs(displayGroup) {
        $.ajax({
            url: bp + 'Smserver/contacts/groups',
            type: 'GET',
            success: function (data) {
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
        $.ajax({
            type: 'GET',
            url: bp + 'Smserver/contacts/' + groupname,
            success: function (data) {
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
            html = html + '<tr><td><button type="button" class="btn btn-danger btn-sm" onclick="getContactsByGroups(\'' + groups[i] + '\')">' + groups[i] + '</button>  ' +
                    '<button type="button" class="btn btn-primary btn-sm" onclick="initEditGroup(\'' + groups[i] + '\')">编辑</button></td></tr>';
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
        $("#uploadDiv").toggle(1000);
    }


    // 使用联系人json数据组合成联系人表格内容
    function initTbodyOfContacts(contacts) {
        var htmlStr = '';
        for (var i = 0; i < contacts.length; i++) {
            htmlStr = htmlStr + '<tr><td>' + '<input type="checkbox" name="contactsCheckbox" value="' + contacts[i].id + '"> ' + contacts[i].id
                    + '</td><td>' + contacts[i].groupname
                    + '</td><td>' + contacts[i].name
                    + '</td><td>' + contacts[i].title
                    + '</td><td>' + contacts[i].phone
                    + '</td><td>' + contacts[i].description
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
                console.log("youyisima :" + tempStr);
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
        $("#createGroupDiv").show(1000);
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
        if (groupname == '' || groupname == null) {
            // 错误信息
            showEditFail("必须输入组名！", $("#newGroupName"));
            return;
        }
        // 获取分组人员ids
        var pattern = /\d+(,\d+)*/;
        var ids = $("#GroupContactsIds").val().trim();
        if (ids != "" || ids != undefined) {
            if (pattern.test(ids) == false) {
                showEditFail("联系人中存在非法字符，两个id之间必须用英文逗号  ','  分隔！", $("#GroupContactsIds"));
                return;
            }
        }
        if (ids == undefined || ids == "") {
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
        $("#editGroupDiv").show(1000);
        // 初始化数据
        $("#editGroupOriginalName").text(groupname);
        $("#editGroupName").val(groupname);
    }

    // 确认编辑组名
    function doEditGroup() {
        var groupname = $("#editGroupName").val();
        // 判断新名称不为空
        if (groupname == "") {
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
        $.ajax({
            type: 'POST',
            url: bp + 'Smserver/group/' + action,
            data: jsonStr,
            dataType: "json",
            contentType: "application/json",
            success: function (data) {
                showEditDone();
                hideEditFail();
                if (action == "insert") {
                    $("#createGroupDiv").hide(1000);
                } else {
                    $("#editGroupDiv").hide(1000);
                }
                initContactsUIs(groupname);
            }
        });
    }

    // 取消编辑组
    function cancleEditGroup() {
        $("#editGroupDiv").hide(1000);
        hideEditFail();
    }

    // 准备好编辑联系人的控件
    function initEditContact(id, groupname, name, title, phone, description) {
        // 标记编辑的id
        updateContactId = id;
        // 显示编辑部分
        $("#editContactDiv").show(1000);
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
        $("#editContactDiv").hide(1000);
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
        commitEditContact('update', jsonStr, "POST");
    }

    // 删除联系人
    function deleteContact() {
        var id = updateContactId;
        // 弹出确认对话框
        if (confirm("确认删除？")) {
            var jsonStr = "{\"id\":" + id + "}";
            commitEditContact('delete', jsonStr, "POST");
        } else {
            return;
        }
    }

    // 提交操作，然后更新页面联系人组件
    function commitEditContact(action, jsonStr, type) {
        $.ajax({
            type: type,
            url: bp + 'Smserver/contacts/' + action,
            data: jsonStr,
            dataType: "json",
            contentType: "application/json",
            success: function (data) {
                showEditDone();
                hideEditFail();
                $("#editContactDiv").hide(1000);
                var contacts = data['contacts'];
                initTbodyOfContacts(contacts);
            },
            error: function (errdata) {
//                showEditFail("abc");
            }
        });
    }

    // 查询联系人
    function searchContact() {
        var name = $("#searchContact").val().trim();
        if (name == "" || name == undefined) {
            myAnimate($("#searchContact"), 8, $("#searchContact").attr("style"));
            return;
        } else {
            var jsonStr = "{\"name\":\"" + name + "\"}";
            commitEditContact('searchbyname', jsonStr, "POST");
        }
    }

    function cancleCreateGroup() {
        $("#createGroupDiv").hide(1000);
        hideEditFail();
    }

    function showEditDone() {
        $("#editDoneDiv").show(2200);
        $("#editDoneDiv").hide(1000);
    }
    function showEditFail(msg, el) {
        myAnimate(el, 8, el.attr("style"));
        $("#failCause").text(msg);
        $("#editFailDiv").show(2000);

    }
    function hideEditFail() {
        $("#editFailDiv").hide(2000);
    }

</script>
</body>
</html>