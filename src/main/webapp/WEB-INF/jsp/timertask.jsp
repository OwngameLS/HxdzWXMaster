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

    <div id="timertaskEditDiv" style="display: none"><%--编辑定时任务的表格--%>
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
                    <td width="5%" id="ttIdEdit">新建</td>
                    <td width="10%">
                        <label id="ttfunctionsEdit"></label><br>
                        <button type="button" class="btn btn-success btn-sm" onclick="">编辑</button>
                    </td>
                    <td width="20%">
                        <textarea id="ttdescriptionEdit"></textarea>
                    </td>
                    <td width="15%">
                        <label id="ttcronEdit">还没有指定</label><br>
                        <button type="button" class="btn btn-primary btn-sm" onclick="editCron()">编辑</button>
                        <div id="editCronDoneDiv" style="display: none">
                            <button type="button" class="btn btn-success btn-sm" onclick="doneCronEdit()">确定</button>
                            <button type="button" class="btn btn-warning btn-sm" onclick="hideCronEdit()">取消</button>
                        </div>
                    </td>
                    <td width="35%">
                        <textarea id="ttcontactsEdit"></textarea><br>
                        <button type="button" class="btn btn-success btn-sm" onclick="queryContactsDetailsWithIds()">查看已选</button>
                        <button type="button" class="btn btn-success btn-sm" onclick="showContactsUI()">新增人员</button>
                    </td>
                    <td width="7%">
                        <select id="ttstateEdit">
                            <option value="run">正常</option>
                            <option value="pause">暂停</option>
                        </select>
                    </td>
                    <td width="8%">
                        <button type="button" class="btn btn-success btn-sm" onclick="">保存</button>
                        <button type="button" class="btn btn-warning btn-sm" onclick="">删除</button>
                        <button type="button" class="btn btn-primary btn-sm" onclick="cancelEditTimerTask()">取消</button>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>

    <div id="cronExpressionDiv" class="embed-responsive embed-responsive-16by9" style="display:none">
        <iframe class="embed-responsive-item" src="../../resources/cronpage/cronpage.htm" ></iframe>
    </div>

    <div id="contactsDiv" class="bg-success" style="display: none;text-align:center">
        <div style="width: 20%;float:left;">
            <button type="button" class="btn btn-warning btn-sm" onclick="hideContactsDiv()">取消编辑</button>
        </div>
        <div style="width:80%;float:left;">
            <button type="button" class="btn btn-success btn-sm" onclick="editContacts('add')">添加选择的人员</button>
            <button type="button" class="btn btn-warning btn-sm" onclick="editContacts('remove')">删除选择的人员</button>
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
                    <th class="text-center">序号（<input type="checkbox" id="selectAllContacts"
                                                      onclick="changeSelectAllContacts()">全选）
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
                <th class="text-center">序号(<input type="checkbox" id="selectAllTimerTasks" onclick="changeSelectAllTimerTasks()">全选)</th>
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
<!-- jQuery (Bootstrap 的 JavaScript 插件需要引入 jQuery) -->
<script src="/resources/bootstrap-3.3.7-dist/js/jquery-3.1.0.min.js"></script>
<!-- 包括所有已编译的插件 -->
<script src="../../resources/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>

<script type="application/javascript">
    var bp = '<%=basePath%>';

    // 文档被加载完成时
    $(document).ready(function () {
        queryTimerTasks();
    });

    function queryTimerTasks() {
        $.ajax({
            url: bp + 'Smserver/timertask/getall',
            type: 'GET',
            success: function (data) {
                // 初始化timertasks相关的控件
                initTbodyOfTasks(data['timerTasks']);// 选择控件
            }
        });
    }

    // 使用联系人json数据组合成联系人表格内容
    function initTbodyOfTasks(timertasks) {
        var htmlStr = '';
        for (var i = 0; i < timertasks.length; i++) {
            htmlStr = htmlStr + '<tr><td>' + '<input type="checkbox" name="contactsCheckbox" value="' + timertasks[i].id + '"> ' + timertasks[i].id
                    + '</td><td>' + timertasks[i].functions
                    + '</td><td>' + timertasks[i].description
                    + '</td><td>' + timertasks[i].firerules
                    + '</td><td>' + timertasks[i].receivers
                    + '</td><td>' + timertasks[i].state
                    + '</td><td>' + '<button type="button" class="btn btn-primary btn-sm" onclick="initEditTimerTask(\'' + timertasks[i].id
                    + '\',\'' + timertasks[i].functions + '\',\'' + timertasks[i].description + '\',\'' + timertasks[i].firerules + '\',\'' + timertasks[i].receivers + '\',\'' + timertasks[i].state + '\')">编辑</button>'
                    + '</td></tr>';
        }

        $("#tasksBody").html(htmlStr);
        // 取消全选的勾选
        $("#selectAllTimerTasks").prop("checked", false);
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

    // 展示联系人选择
    function showContactsUI() {
        showEditDone();
        hideEditFail();
        // 显示联系人信息表格div
        showContactsDiv();
        // 初始化分组表格
        initTbodyOfGroups();
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
        $("#contactsBody").html(htmlStr);
        // 取消全选的勾选
        $("#selectAllContacts").prop("checked", false);
    }

    // 初始化联系人组UI控件
    function initTbodyOfGroups(){
        var htmlStr = '<tr><td><button type="button" class="btn btn-danger btn-sm" onclick="queryContactsDetailsWithIds()">已选人员</button></td></tr>';
        $.ajax({
            url: bp + 'Smserver/contacts/groups',
            type: 'GET',
            success: function (data) {
                var groups = data['groups'];
                for (var i = 0; i < groups.length; i++) {
                    htmlStr = htmlStr + '<tr><td><button type="button" class="btn btn-danger btn-sm" onclick="getContactsByGroups(\'' + groups[i] + '\')">' + groups[i] + '</button>';
                }
                $("#groupsBody").html(htmlStr);
                // 展示第一组
                getContactsByGroups(groups[0]);
            }
        });
    }

    // 向服务器请求联系人信息 通过分组名称
    function getContactsByGroups(groupname) {
        $.ajax({
            type: 'GET',
            url: bp + 'Smserver/contacts/' + groupname,
            success: function (data) {
                initTbodyOfContacts(data['contacts']);
            }
        });
    }


    //全选或者反选 联系人
    function changeSelectAllContacts() {
        if ($("#selectAllContacts").is(':checked')) {
            $("input[name='contactsCheckbox']").prop("checked", true);// 放弃了attr
        } else {
            $("input[name='contactsCheckbox']").prop("checked", false);
        }
    }

    //全选或者反选 定时任务
    function changeSelectAllTimerTasks() {
        if ($("#selectAllTimerTasks").is(':checked')) {
            $("input[name='timerTasksCheckbox']").prop("checked", true);// 放弃了attr
        } else {
            $("input[name='timerTasksCheckbox']").prop("checked", false);
        }
    }

    // 添加联系人ids action : add ;remove
    function editContacts(action) {
        // 当前选中的ids
        var currentSelectIds = new Array();
        var aaa = $("input[name='contactsCheckbox']");
        aaa.each(function () {
            if ($(this).prop("checked")) {
                currentSelectIds.push($(this).val());
            }
        });
        if(currentSelectIds.length == 0){// 没有选择任何联系人
            return;
        }

        // 原有的ids
        var formerIds = null;
        var a = ('' + $("#ttcontactsEdit").val()).trim();
        if (a != '') {
            formerIds = a.split(",");
        } else {
            formerIds = new Array();
        }

        for (var i = 0; i < currentSelectIds.length; i++) {
            // 在原来的ids中查找
            var index = formerIds.indexOf(currentSelectIds[i]);
            if(index != -1){// 找到了
                if(action == 'add'){
                    // 不用处理
                }else if(action == 'remove'){
                    // 删除找到的元素
                    formerIds.splice(index,1);
                }
            }else{// 没找到
                if(action == 'add'){
                    // 添加元素
                    formerIds.push(currentSelectIds[i]);
                }else if(action == 'remove'){
                    // 不用处理
                }
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
        $("#ttcontactsEdit").val(tempStr);
    }

    function initEditTimerTask(id, functions, description, firerules, receivers, state){
        // 先将编辑框展示出来
        $("#timertaskEditDiv").show(1500);
        $("#ttIdEdit").html(id);
        $("#ttfunctionsEdit").val(functions);
        $("#ttdescriptionEdit").val(description);
        $("#ttcronEdit").html(firerules);
        $("#ttcontactsEdit").val(receivers);
        $("#ttstateEdit").val(state);
    }


    function editCron() {
        // 显示按钮
        $("#editCronDoneDiv").show(1000);

        // 显示操作页面
        $("#cronExpressionDiv").show(1000);
        // 将原来的cron表达式在UI界面上显示出来
        var formerCron = $("#ttcronEdit").html();
        $("iframe").contents().find("#cron").val(formerCron);
        $("iframe").contents().find("#btnFan").click();
    }

    function doneCronEdit(){
        // 获得编辑好的表达式
        var a = $("iframe").contents().find("#cron").val();
        // 设置
        $("#ttcronEdit").html(a);
        // 提示操作完成并隐藏编辑UI
        showEditDone();
        hideCronEdit();
    }

    function hideCronEdit() {
        // 显示按钮
        $("#editCronDoneDiv").hide(1000);
        // 显示操作页面
        $("#cronExpressionDiv").hide(1000);
    }

    // 取消编辑定时任务
    function cancelEditTimerTask() {
        // 编辑框
        $("#timertaskEditDiv").hide(1500);
        hideEditFail();
        hideCronEdit();
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

    function hideContactsDiv(){
        $("#contactsDiv").hide(1500);
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
