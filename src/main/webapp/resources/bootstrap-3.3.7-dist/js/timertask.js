/**
 * Created by Administrator on 2016-10-27.
 * timertask.jsp 页面的 js支持
 */
var pager = new Pager(null, null, null);

function queryTimerTasks() {
    var jsonStr = "{\"pageSize\":\"" + pager.pageSize
        + "\",\"targetPage\":\"" + pager.targetPage
        + "\"}";
    $.when(myAjaxPost(bp + 'Smserver/timertask/getall', jsonStr)).done(function (data) {
        if (data != null) {
            pager = new Pager(data['timerTasks'], queryTimerTasks, initTbodyOfTasks);
            pager.uiDisplay();
            // 初始化timertasks相关的控件
            // initTbodyOfTasks(data['timerTasks']);// 选择控件
        }
    });
}

// 使用联系人json数据组合成联系人表格内容
function initTbodyOfTasks(timertasks) {
    var htmlStr = '';
    for (var i = 0; i < timertasks.length; i++) {
        htmlStr = htmlStr + '<tr><td>' + '<input type="checkbox" name="taskCheckbox" value="' + timertasks[i].id + '"> ' + timertasks[i].id
            + '</td><td>' + timertasks[i].functions
            + '</td><td>' + parseToAbbr(timertasks[i].description, 10, null)
            + '</td><td>' + timertasks[i].firerules
            + '</td><td>' + parseToAbbr(timertasks[i].receivers, 15, null) + '</td>';
        switch (timertasks[i].receivetype) {
            case 0:
                htmlStr = htmlStr + '<td>短信</td>';
                break;
            case 1:
                htmlStr = htmlStr + '<td>微信</td>';
                break;
            case 2:
                htmlStr = htmlStr + '<td>短信和微信</td>';
                break;
        }
        var stateDesc = '';
        if (timertasks[i].state == 'run') {
            stateDesc = '正常运行';
        } else if (timertasks[i].state == 'pause') {
            stateDesc = '暂停运行';
        }
        htmlStr = htmlStr + '<td>' + stateDesc
            + '</td><td>' + '<button type="button" class="btn btn-primary btn-sm" onclick="initEditTimerTask(\'' + timertasks[i].id
            + '\',\'' + timertasks[i].functions + '\',\'' + timertasks[i].description + '\',\'' + timertasks[i].firerules + '\',\'' + timertasks[i].receivers + '\',' + timertasks[i].receivetype + ',\'' + timertasks[i].state + '\')">编辑</button>'
            + '</td></tr>';
    }

    $("#tasksBody").html(htmlStr);
    // 取消全选的勾选
    $("#selectAllTimerTasks").prop("checked", false);
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
    for (var i = 0; i < currentSelectIds.length; i++) {
        console.log("abc:" + currentSelectIds[i]);
    }
    if (currentSelectIds.length == 0) {// 没有选择任何联系人
        return;
    }
    // 原有的ids
    var formerIds = null;
    var a = ('' + $("#selectedContactsIds").val()).trim();
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
    console.log("tempStr:" + tempStr);
    $("#selectedContactsIds").val(tempStr);
}

function initEditTimerTask(id, functions, description, firerules, receivers, receivetype, state) {
    // 先将编辑框展示出来
    $("#timertaskEditDiv").show(2000);
    if (id != -1) {
        $("#ttIdEdit").html(id);
        $("#ttfunctionsEdit").html(functions);
        $("#ttdescriptionEdit").val(description);
        $("#ttcronEdit").html(firerules);
        $("#selectedContactsIds").val(receivers);
        console.log("type:" + receivetype);
        switch (receivetype) {
            case 0:
                $("#sendTypeSmsEdit").prop("checked", true);
                break;
            case 1:
                $("#sendTypeWxEdit").prop("checked", true);
                break;
            case 2:
                console.log("what the fuck! " + receivetype);
                $("#sendTypeSmsEdit").prop("checked", true);
                $("#sendTypeWxEdit").prop("checked", true);
                break;
        }
        $("#ttstateEdit").val(state);
    } else {
        $("#ttIdEdit").html('新建');
        $("#ttfunctionsEdit").html('未指定');
        $("#ttdescriptionEdit").val('');
        $("#ttcronEdit").html('未指定');
        $("#selectedContactsIds").val('');
        $("#sendTypeSmsEdit").prop("checked", true);
        $("#sendTypeWxEdit").prop("checked", false);
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
    $.when(myAjaxGet(bp + 'Smserver/functions/getall')).done(function (data) {
        var htmlStr = '';
        if (data != null) {
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
                if (functions[i].usable == "no") {
                    htmlStr = htmlStr + ' disabled> ' + parseToAbbr(functions[i].id, 10, '此功能暂时不能使用，请联系管理员解决。') + '</input>';
                } else {
                    htmlStr = htmlStr + '> ' + functions[i].id + '</input>';
                }
                htmlStr = htmlStr
                    + '</td><td>' + parseToAbbr(functions[i].name, 10, null)
                    + '</td><td>' + parseToAbbr(functions[i].description, 40, null)
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
        var contacts = $("#selectedContactsIds").val();
        var sendTypeSms = $("#sendTypeSmsEdit").prop("checked");
        var sendTypeWx = $("#sendTypeWxEdit").prop("checked");
        var type = 0;
        if (sendTypeSms) {
            if (sendTypeWx) {
                type = 2;
            }
        } else {
            if (sendTypeWx) {
                type = 1;
            }
        }
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
            + "\",\"type\":\"" + type
            + "\",\"state\":\"" + state + "\"}";
        doAjaxHandleTimerTask('update', jsonStr);
    }
}

// 处理TimerTask操作提交给服务器部分
function doAjaxHandleTimerTask(action, jsonStr) {
    $.when(myAjaxPost(bp + 'Smserver/timertask/' + action, jsonStr)).done(function (data) {
        var htmlStr = '';
        if (data != null) {
            // 初始化timertasks相关的控件
            initTbodyOfTasks(data['timerTasks']);// 选择控件
            // 先将编辑框隐藏
            $("#timertaskEditDiv").hide(2000);
            showEditDone();
            hideEditFail();
        }
    });
}
function gotoPage(page){
    pager.gotoPage(page);
}

function changePageSize() {
    pager.changePageSize();
}