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
    <style type="text/css">
        /*设置单元格内容超长，用省略号代替的效果，前提是每一列的宽度都要指定*/
        table{
            table-layout: fixed;
        }
        td{
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
                <th width="5%" class="text-center">序号</th>
                <th width="10%" class="text-center">功能</th>
                <th width="20%" class="text-center">描述</th>
                <th width="15%" class="text-center">触发规则</th>
                <th width="23%" class="text-center">接收者们</th>
                <th width="12%" class="text-center">状态</th>
                <th width="15%" class="text-center">操作</th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td width="5%" id="ttIdEdit">新建</td>
                <td width="10%">
                    <label id="ttfunctionsEdit">未指定</label><br>
                    <button type="button" class="btn btn-success btn-sm" onclick="editFunctions()">编辑</button>
                </td>
                <td width="20%">
                    <textarea id="ttdescriptionEdit" placeholder="对这个定时任务用你能理解的方式描述"></textarea>
                </td>
                <td width="15%">
                    <label id="ttcronEdit">未指定</label><br>
                    <button type="button" class="btn btn-primary btn-sm" onclick="editCron()">编辑</button>
                    <div id="editCronDoneDiv" style="display: none">
                        <button type="button" class="btn btn-success btn-sm" onclick="doneCronEdit()">确定</button>
                        <button type="button" class="btn btn-warning btn-sm" onclick="hideCronEdit()">取消</button>
                    </div>
                </td>
                <td width="35%">
                    <textarea id="ttcontactsEdit" placeholder="这里将展示选中人员的id"></textarea><br>
                    <button type="button" class="btn btn-success btn-sm" onclick="queryContactsDetailsWithIds()">查看已选
                    </button>
                    <button type="button" class="btn btn-success btn-sm" onclick="showContactsUI()">新增人员</button>
                </td>
                <td width="7%">
                    <select id="ttstateEdit">
                        <option value="run">正常运行</option>
                        <option value="pause">暂停运行</option>
                    </select>
                </td>
                <td width="8%">
                    <button type="button" class="btn btn-success btn-sm" onclick="handleTimerTask('save')">保存</button>
                    <button type="button" class="btn btn-warning btn-sm" onclick="handleTimerTask('delete')">删除</button>
                    <button type="button" class="btn btn-primary btn-sm" onclick="cancelEditTimerTask()">取消</button>
                </td>
            </tr>
            </tbody>
        </table>
    </div>

    <div id="cronExpressionDiv" class="embed-responsive embed-responsive-16by9" style="display:none">
        <iframe class="embed-responsive-item" src="../../resources/cronpage/cronpage.htm"></iframe>
    </div>

    <div id="functionsEditDiv" class="bg-success" style="display: none;text-align:center"> <!--编辑定时任务功能的方法-->
        <h4>在下面勾选你需要的功能</h4>
        <div style="float:left;">
            <button type="button" class="btn btn-success btn-sm" onclick="editFunctionsDone('done')">完成</button>
            <button type="button" class="btn btn-warning btn-sm" onclick="editFunctionsDone('cancle')">取消</button>
        </div>
        <table class="table table-hover table-bordered text-center">
            <thead>
            <tr class="info">
                <th width="15%" class="text-center">序号（<input type="checkbox" id="selectAllFunctions"
                                                              onclick="changeSelectAllFunctions()">全选）
                </th>
                <th width="25%" class="text-center">
                    功能名称
                </th>
                <th width="60%" class="text-center">
                    描述
                </th>
            </tr>
            </thead>
            <tbody id="functionsBody">
            </tbody>
        </table>
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
                        <button type="button" class="btn btn-danger btn-sm" onclick="queryContactsDetailsWithIds()">
                            已选人员
                        </button>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
        <div id="persons" style="width:80%;float:left;overflow:scroll; height:400px;">

            <table class="table table-hover table-bordered text-center">
                <thead>
                <tr class="info">
                    <th width="15%" class="text-center">序号（<input type="checkbox" id="selectAllContacts"
                                                      onclick="changeSelectAllContacts()">全选）
                    </th>
                    <th width="15%" class="text-center">所在分组</th>
                    <th width="15%" class="text-center">姓名</th>
                    <th width="25%" class="text-center">职务</th>
                    <th width="30%"class="text-center">手机号</th>
                </tr>
                </thead>
                <tbody id="contactsBody">
                </tbody>
            </table>
        </div>
    </div>

    <button type="button" class="btn btn-warning btn-sm" onclick="initEditTimerTask(-1)">新建任务</button>
    <div id="tasks" style="width:100%;float:left;overflow:scroll; height:400px;">
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
        $("#timertaskEditDiv").show(1500);
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
            $("#functionsEditDiv").hide(1500);
        } else if (action == 'cancle') {
            $("#functionsEditDiv").hide(1500);
        }
    }

    // 展示定时任务触发规则的UI
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
                $("#timertaskEditDiv").hide(1500);
                showEditDone();
                hideEditFail();
            }
        });
    }

    function showContactsDiv() {
        $("#contactsDiv").show(2000);
    }

    function hideContactsDiv() {
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
