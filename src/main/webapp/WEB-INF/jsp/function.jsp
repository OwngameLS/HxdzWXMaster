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
    <title>功能编辑</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <style type="text/css">
        /*设置单元格内容超长，用省略号代替的效果，前提是每一列的宽度都要指定*/
        table {
            table-layout: fixed;
        }

        td {
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
        }
    </style>
    <!-- 引入 Bootstrap -->
    <link href="../../resources/bootstrap-3.3.7-dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="../../resources/bootstrap-3.3.7-dist/js/uiscript.js"></script>
</head>
<body>
<h3>定时任务操作</h3>

<div class="danger" style="padding: 5px;"><%--通讯录操作部分--%>
    <dl>
        <dt>编辑“功能”</dt>
        <dd>
            <p class="text-warning">
                在下面进行您自定义功能的编辑
            </p>
        </dd>
    </dl>
    <div id="editDoneDiv" class="alert alert-success" role="alert"
         style="display: none;width: 30%;margin:0 auto;text-align:center"><%--操作完成--%>
        操作完成！
    </div>

    <div id="editFailDiv" class="alert alert-danger" role="alert"
         style="padding:5px;display: none;width: 30%;margin:0 auto;text-align:center"><%--操作失败--%>
        <h5>操作失败！</h5>
        <p class="text-danger" id="failCause"></p>
    </div>

    <div id="functionEditDiv" style="display: none" class="well"><%--编辑定时任务的表格--%>
        <table class="table table-hover table-bordered text-center">
            <tbody>
                ---功能描述性设置-------------------------
                <tr class="info">
                    <th width="20%" class="text-center">名称</th>
                    <th width="30%" class="text-center">关键字</th>
                    <th width="50%" class="text-center">描述</th>
                </tr>
                <tr class="info">
                    <td width="20%" class="text-center">
                        <input class="form-control" id="editName" placeholder="给功能起一个名字...">
                    </td>
                    <td width="30%" class="text-center">
                        <input class="form-control" id="editKeywords" placeholder="关键字，区别于其他功能的关键字..." onchange="">
                    </td>
                    <td width="50%" class="text-center">
                        <input class="form-control" id="editContactName" placeholder="描述这个功能，它将作为返回信息的起始部分...">
                    </td>
                </tr>
                ---数据库连接属性设置---------------------
                <tr>
                    <th width="30%" class="text-center">IP地址</th>
                    <th width="20%" class="text-center">端口号</th>
                    <th width="25%" class="text-center">数据库类型</th>
                    <th width="25%" class="text-center">数据库名称</th>
                </tr>
                <tr>
                    <td width="30%" class="text-center">
                        <input class="form-control" id="editIP" placeholder="欲读取数据库所在机器的IP地址...">
                    </td>
                    <td width="20%" class="text-center">
                        <input class="form-control" id="editPort" placeholder="其数据库开放的端口...">
                    </td>
                    <td width="25%" class="text-center">
                        <select id="editDbtype">
                            <option value="MySQL">MySQL</option>
                            <option value="Microsoft SQL Server">Microsoft SQL Server</option>
                            <option value="Oracle">Oracle</option>
                            <option value="PostgreSQL">PostgreSQL</option>
                            <option value="DB2">DB2</option>
                            <option value="Informix">Informix</option>
                            <option value="JDBC-ODBC">JDBC-ODBC</option>
                        </select>
                    </td>
                    <td width="25%" class="text-center">
                        <input class="form-control" id="editDbname" placeholder="数据库名...">
                    </td>
                </tr>
                <tr>
                    <th width="25%" class="text-center">用户名</th>
                    <th width="25%" class="text-center">密码</th>
                    <th width="30%" class="text-center">表名</th>
                    <th width="20%" class="text-center" id="connectResult"></th>
                </tr>
                <tr>
                    <td width="25%" class="text-center">
                        <input class="form-control" id="editUsername" placeholder="数据库用户名...">
                    </td>
                    <td width="25%" class="text-center">
                        <input class="form-control" id="editPassword" placeholder="数据库密码...">
                    </td>
                    <td width="30%" class="text-center">
                        <input class="form-control" id="editTablename" placeholder="读取的表名...">
                    </td>
                    <td width="20%" class="text-center">
                        <button type="button" class="btn btn-success btn-sm" onclick="testConnect()">连接测试</button>
                    </td>
                </tr>
                ---读取字段的设置-------------------------
                <tr>
                    <td width="15%" class="text-center">是否排序</td>
                    <td width="15%" class="text-center">是否使用</td>
                    <td width="23%" class="text-center">字段名</td>
                    <td width="12%" class="text-center">自定义名称</td>
                    <td width="15%" class="text-center">是否使用规则</td>
                    <td width="15%" class="text-center">标准值</td>
                    <td width="15%" class="text-center">规则</td>
                </tr>
            </tbody>
        </table>
        <table>
            ---读取字段-------------------------------
            <tbody id="colsTR"><%--在这里列出所有字段--%>
            </tbody>
        </table>
    </div>

    <button type="button" class="btn btn-warning btn-sm" onclick="initEditTimerTask(-1)">新建任务</button>
    <div id="tasks" style="width:100%;float:left;overflow:scroll; height:400px;" class="well">
        <table class="table table-hover table-bordered text-center">
            <thead>
            <tr class="info">
                <th width="12%" class="text-center">序号(<input type="checkbox" id="selectAllTimerTasks"
                                                              onclick="changeSelectAllTimerTasks()">全选)
                </th>
                <th width="12%" class="text-center">功能</th>
                <th width="12%" class="text-center">描述</th>
                <th width="14%" class="text-center">触发规则</th>
                <th width="27%" class="text-center">接收者们</th>
                <th width="15%" class="text-center">状态</th>
                <th width="8%" class="text-center">操作</th>
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
    var collength = 0;
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
                    + '</td><td>' + timertasks[i].receivers;
            var stateDesc = '';
            if (timertasks[i].state == 'run') {
                stateDesc = '正常运行';
            } else if (timertasks[i].state == 'pause') {
                stateDesc = '暂停运行';
            }
            htmlStr = htmlStr + '</td><td>' + stateDesc
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
        var jsonStr = "{\"ids\":\"" + ids + "\"}";
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

    // 初始化表字段UI控件 用于编辑字段规则
    function initTbodyOfCols(colNames) {
        var htmlStr = '';
        for (var i = 0; i < colNames.length; i++) {
            htmlStr = htmlStr
                    + '<tr>'
                    + '<td><input type="checkbox" name="isSort'+i+'"><br>'
                    + '<input type="radio" name=""></td>'
                    + '<td><input type="checkbox" name="isused" value="' + colNames[i] + '"></td>'
                    + '<td>' + contacts[i].groupname
                    + '</td><td>' + contacts[i].name
                    + '</td><td>' + contacts[i].title
                    + '</td><td>' + contacts[i].phone
                    + '</td>'
                    + '</tr>';
        }
        $("#contactsBody").html(htmlStr);
        // 取消全选的勾选
        $("#selectAllContacts").prop("checked", false);
    }

    // 初始化联系人组UI控件
    function initTbodyOfGroups() {
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

    // 全选或者反选 功能
    function changeSelectAllFunctions() {
        if ($("#selectAllFunctions").is(':checked')) {
            $("input[name='functionsCheckbox']").prop("checked", true);// 放弃了attr
        } else {
            $("input[name='functionsCheckbox']").prop("checked", false);
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
        if (currentSelectIds.length == 0) {// 没有选择任何联系人
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
            if (index != -1) {// 找到了
                if (action == 'add') {
                    // 不用处理
                } else if (action == 'remove') {
                    // 删除找到的元素
                    formerIds.splice(index, 1);
                }
            } else {// 没找到
                if (action == 'add') {
                    // 添加元素
                    formerIds.push(currentSelectIds[i]);
                } else if (action == 'remove') {
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

    function initEditTimerTask(id, functions, description, firerules, receivers, state) {
        // 先将编辑框展示出来
        $("#timertaskEditDiv").show(2000);
        if (id != -1) {
            $("#ttIdEdit").html(id);
            $("#ttfunctionsEdit").html(functions);
            $("#ttdescriptionEdit").val(description);
            $("#ttcronEdit").html(firerules);
            $("#ttcontactsEdit").val(receivers);
            $("#ttstateEdit").val(state);
        } else {
            $("#ttIdEdit").html('新建');
            $("#ttfunctionsEdit").html('未指定');
            $("#ttdescriptionEdit").val('');
            $("#ttcronEdit").html('未指定');
            $("#ttcontactsEdit").val('');
            $("#ttstateEdit").val('run');
        }
    }

    // 编辑定时任务的功能
    function editFunctions() {
        // 显示编辑UIs
        $("#functionsEditDiv").show(2000);
        // 读取当前已经选择的功能
        var selectedFunctions = ($("#ttfunctionsEdit").html() + '').split(",");
        // 从服务器端获取可用的功能
        $.ajax({
            type: 'GET',
            url: bp + 'Smserver/functions',
            success: function (data) {
                var functions = (data['functions']);
                // 整理成表格展示
                var htmlStr = '';
                for (var i = 0; i < functions.length; i++) {
                    htmlStr = htmlStr + '<tr><td>' + '<input type="checkbox" name="functionsCheckbox" value="' + functions[i].name + '"';
                    var isInSelected = false;
                    for (var j = 0; j < selectedFunctions.length; j++) {
                        if (selectedFunctions[j] == functions[i].name) {
                            isInSelected = true;
                            break;
                        }
                    }
                    if (isInSelected) {
                        htmlStr = htmlStr + 'checked';
                    }
                    htmlStr = htmlStr + '> ' + functions[i].id + '</input>'
                            + '</td><td>' + functions[i].name
                            + '</td><td>' + functions[i].description
                            + '</td></tr>';
                }
                $("#functionsBody").html(htmlStr);
                // 取消全选的勾选
                $("#selectAllFunctions").prop("checked", false);
            }
        });
    }

    // 完成或者取消编辑功能
    function editFunctionsDone(action) {
        if (action == 'done') {
            // 检查勾选的功能
            // 当前选中的ids
            var selectIds = new Array();
            var selectNames = new Array();
            var aaa = $("input[name='functionsCheckbox']");
            aaa.each(function () {
                if ($(this).prop("checked")) {
                    selectNames.push($(this).val());
                    selectIds.push($(this).html());
                }
            });
            // 改变展示
            if (selectNames.length == 0) {// 没有选中
                $("#ttfunctionsEdit").html('未指定');
            } else {
                var tt = '';
                for (var i = 0; i < selectNames.length; i++) {
                    if ((i + 1) < selectNames.length) {
                        tt = tt + selectNames[i] + ',';
                    } else {
                        tt = tt + selectNames[i];
                    }
                }
                $("#ttfunctionsEdit").html(tt);
            }
            $("#functionsEditDiv").hide(2000);
        } else if (action == 'cancle') {
            $("#functionsEditDiv").hide(2000);
        }
    }

    // 展示定时任务触发规则的UI
    function editCron() {
        // 显示按钮
        $("#editCronDoneDiv").show(2000);
        // 显示操作页面
        $("#cronExpressionDiv").show(2000);
        // 将原来的cron表达式在UI界面上显示出来
        var formerCron = $("#ttcronEdit").html();
        $("iframe").contents().find("#cron").val(formerCron);
        $("iframe").contents().find("#btnFan").click();
    }

    // 完成编辑定时任务触发规则
    function doneCronEdit() {
        // 获得编辑好的表达式
        var a = $("iframe").contents().find("#cron").val();
        // 设置
        $("#ttcronEdit").html(a);
        // 提示操作完成并隐藏编辑UI
        showEditDone();
        hideCronEdit();
    }

    // 隐藏展示定时任务触发规则的UI
    function hideCronEdit() {
        // 显示按钮
        $("#editCronDoneDiv").hide(2000);
        // 显示操作页面
        $("#cronExpressionDiv").hide(2000);
    }

    // 取消编辑定时任务
    function cancelEditTimerTask() {
        // 编辑框
        $("#timertaskEditDiv").hide(2000);
        hideEditFail();
        hideCronEdit();
    }


    // 处理TimerTask操作UI逻辑部分
    function handleTimerTask(action) {
        var id = $("#ttIdEdit").html();
        if (action == 'delete') {
            if (id == '新建') {// 新建情况删除个屁啊
                showEditFail("这是新建呢，不能执行删除操作！", $("#ttIdEdit"));
            } else {
                doAjaxHandleTimerTask('delete', '{\"id\":\"' + id + '\"}');
            }
        } else if (action == 'save') {
            // 获取其他的值
            var functions = $("#ttfunctionsEdit").html();
            var description = $("#ttdescriptionEdit").val();
            var cron = $("#ttcronEdit").html();
            var contacts = $("#ttcontactsEdit").val();
            var state = $("#ttstateEdit  option:selected").val();
            // 判断合理值
            // 整理成JsonStr
            if (id == '新建') {
                id = 0;// 新建
            }
            var jsonStr = "{\"id\":\"" + id
                    + "\",\"functions\":\"" + functions
                    + "\",\"description\":\"" + description
                    + "\",\"cron\":\"" + cron
                    + "\",\"contacts\":\"" + contacts
                    + "\",\"state\":\"" + state + "\"}";
            doAjaxHandleTimerTask('update', jsonStr);
        }
    }

    // 处理TimerTask操作提交给服务器部分
    function doAjaxHandleTimerTask(action, jsonData) {
        $.ajax({
            type: 'POST',
            url: bp + 'Smserver/timertask/' + action,
            data: jsonData,
            dataType: "json",
            contentType: "application/json",
            success: function (data) {
                // 初始化timertasks相关的控件
                initTbodyOfTasks(data['timerTasks']);// 选择控件
                // 先将编辑框隐藏
                $("#timertaskEditDiv").hide(2000);
                showEditDone();
                hideEditFail();
            }
        });
    }

    function showContactsDiv() {
        $("#contactsDiv").show(2000);
    }

    function hideContactsDiv() {
        $("#contactsDiv").hide(2000);
    }

    function showEditDone() {
        $("#editDoneDiv").show(2000);
        $("#editDoneDiv").hide(2000);
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
