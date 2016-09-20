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
    <title>定时任务</title>
    <style>
        /*#contacts{
            padding:10px;
            background-color:#003300;
            color:#FFFFFF;
            width:600px;
            height:400px;
            display:block;
            position: absolute;
            margin-left:-300px;
            margin-top:-200px;
        }*/
    </style>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <!-- 引入 Bootstrap -->
    <link href="../../resources/bootstrap-3.3.7-dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="../../resources/bootstrap-3.3.7-dist/js/uiscript.js"></script>
</head>
<body>
<h3>定时任务操作</h3>

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

    <div id="timertaskEditDiv"><%--编辑定时任务的表格--%>
        <table class="table table-hover table-bordered text-center">
            <thead>
            <tr class="info">
                <th class="text-center">序号</th>
                <th class="text-center">功能</th>
                <th class="text-center">描述</th>
                <th class="text-center">触发规则</th>
                <th class="text-center">接收者们</th>
                <th class="text-center">状态</th>
                <th class="text-center">操作</th>
            </tr>
            </thead>
            <tbody>
                <tr>
                    <td width="5%" id="ttIdEdit"></td>
                    <td width="10%">
                        <label id="ttfunctionsEdit"></label><br>
                        <button type="button" class="btn btn-success btn-sm" onclick="">编辑</button>
                    </td>
                    <td width="20%">
                        <textarea id="ttdescriptionEdit"></textarea>
                    </td>
                    <td width="15%">
                        <label id="ttfirerulesEdit"></label><br>
                        <button type="button" class="btn btn-success btn-sm" onclick="">编辑</button>
                    </td>
                    <td width="35%">
                        <textarea id="ttcontactsEdit"></textarea><br>
                        <button type="button" class="btn btn-success btn-sm" onclick="queryContactsDetailsWithIds()">查看已选</button>
                        <button type="button" class="btn btn-success btn-sm" onclick="">新增人员</button>
                    </td>
                    <td width="7%">
                        <select id="ttstateEdit">
                            <option value="1">1</option>
                            <option value="2">2</option>
                            <option value="3">3</option>
                            <option value="4">4</option>
                        </select>
                    </td>
                    <td width="8%">
                        <button type="button" class="btn btn-success btn-sm" onclick="">保存</button>
                        <button type="button" class="btn btn-warning btn-sm" onclick="">删除</button>
                        <button type="button" class="btn btn-primary btn-sm" onclick="">取消</button>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>


    <div id="contactsDiv" class="bg-warning" style="padding: 5px;display: none;text-align:center">
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
                    <tr>
                        <td>
                            <button type="button" class="btn btn-danger btn-sm" onclick="queryContactsDetailsWithIds()">已选人员</button>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
        <div id="persons" style="width:80%;float:left;overflow:scroll; height:400px;">

            <table class="table table-hover table-bordered text-center">
                <thead>
                <tr class="info">
                    <th class="text-center">id(<input type="checkbox" id="selectAllContacts"
                                                      onclick="changeSelectAllContacts()">全选)
                    </th>
                    <th class="text-center">所在分组</th>
                    <th class="text-center">姓名</th>
                    <th class="text-center">职务</th>
                    <th class="text-center">手机号</th>
                </tr>
                </thead>
                <tbody id="contactsBody">
                </tbody>
            </table>
        </div>
    </div>

    <button type="button" class="btn btn-warning btn-sm" onclick="createTimerTask()">新建任务</button>
    <div id="tasks" style="width:100%;float:left;overflow:scroll; height:400px;">
        <table class="table table-hover table-bordered text-center">
            <thead>
            <tr class="info">
                <th class="text-center">序号(<input type="checkbox" id="selectAll" onclick="changeSelectAll()">全选)</th>
                <th class="text-center">功能</th>
                <th class="text-center">描述</th>
                <th class="text-center">触发规则</th>
                <th class="text-center">接收者们</th>
                <th class="text-center">状态</th>
                <th class="text-center">操作</th>
            </tr>
            </thead>
            <tbody id="tasksBody">
            </tbody>
        </table>
    </div>

</div>

<script type="application/javascript">
    var bp = '<%=basePath%>';


    // 使用联系人json数据组合成联系人表格内容
    function initTbodyOfTasks(timetasks) {
        var htmlStr = '';
        for (var i = 0; i < timetasks.length; i++) {
            htmlStr = htmlStr + '<tr><td>' + '<input type="checkbox" name="contactsCheckbox" value="' + timetasks[i].id + '"> ' + timetasks[i].id
                    + '</td><td>' + timetasks[i].groupname
                    + '</td><td>' + timetasks[i].name
                    + '</td><td>' + timetasks[i].title
                    + '</td><td>' + timetasks[i].phone
                    + '</td><td>' + timetasks[i].description
                    + '</td><td>' + '<button type="button" class="btn btn-primary btn-sm" onclick="initEditContact(\'' + timetasks[i].id
                    + '\',\'' + timetasks[i].groupname + '\',\'' + timetasks[i].name + '\',\'' + timetasks[i].title + '\',\'' + timetasks[i].phone + '\',\'' + timetasks[i].description + '\')">编辑</button>'
                    + '</td></tr>';
        }
        $("#contactBody").html(htmlStr);
        // 取消全选的勾选
        $("#selectAll").prop("checked", false);
    }



    // 通过ids查询联系人详情
    function queryContactsDetailsWithIds() {
        var ids = $("#ttcontactsEdit").val();
        // 判断不为空
        if (ids == '' || name == undefined) {
            showEditFail("当前还没有选择联系人！", $("#ttcontactsEdit"));
            hideEditFail();
            return;
        }
        var jsonStr = "{\"ids\":\""+ids+"\"}";
        $.ajax({
            type: 'POST',
            url: bp + 'Smserver/contacts/searchbyids',
            data: jsonStr,
            dataType: "json",
            contentType: "application/json",
            success: function (data) {
                showEditDone();
                hideEditFail();
                // 显示联系人信息表格div
                showContactsDiv();
                // 分组信息
                initTbodyOfContacts(data['contacts']);

            }
        });
    }

    // 初始化联系人详情UI控件
    function initTbodyOfContacts(contacts) {
        var htmlStr = '';
        for (var i = 0; i < contacts.length; i++) {
            htmlStr = htmlStr + '<tr><td>' + '<input type="checkbox" name="contactsCheckbox" value="' + contacts[i].id + '"> ' + contacts[i].id
                    + '</td><td>' + contacts[i].groupname
                    + '</td><td>' + contacts[i].name
                    + '</td><td>' + contacts[i].title
                    + '</td><td>' + contacts[i].phone
                    + '</td></tr>';
        }
        console.log("htmlStr:" + htmlStr);
        $("#contactsBody").html(htmlStr);
        // 取消全选的勾选
        $("#selectAllContacts").prop("checked", false);
    }

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



    function createTimerTask() {
        $.ajax({
            type: 'POST',
            url: bp + 'Smserver/timertask/insert',
            data: '{}',
            dataType: "json",
            contentType: "application/json",
            success: function (data) {
                showEditDone();
                hideEditFail();

            }
        });
    }

    function showContactsDiv() {
        $("#contactsDiv").show(2000);
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

<!-- jQuery (Bootstrap 的 JavaScript 插件需要引入 jQuery) -->
<script src="../../resources/bootstrap-3.3.7-dist/js/jquery-3.1.0.min.js"></script>
<!-- 包括所有已编译的插件 -->
<script src="../../resources/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>
</body>
</html>
