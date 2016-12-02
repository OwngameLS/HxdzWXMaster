/**
 * Created by Administrator on 2016-10-27.
 * taskstate.jsp 页面的 js支持
 */
var refreshTime = 30000;// 30秒
var lasthours = 1;// 1小时
var intervalId;

function queryTasks() {
    $.when(myAjaxGet(bp + 'Smserver/tasks/' + lasthours)).done(function (data) {
        var htmlStr = '';
        if (data != null) {
            // 初始化tasks相关的控件
            initTbodyOfTasks(data['tasks']);// 选择控件
        }
    });
}

// 当发生选择查询时段变化时
function changeQueryHours() {
    // 得到查询时间段
    lasthours = $("#hours option:selected").val();
    queryTasks();
}
// 当发生选择刷新时间间隔变化时
function changeRefreshTime() {
    // 得到查询时间段
    refreshTime = $("#refreshtime option:selected").val() * 60 * 1000;
    queryTasks();
}

// 初始化表格内容
function initTbodyOfTasks(tasks) {
    var htmlStr = '';
    var stateDesc = '';
    for (var i = 0; i < tasks.length; i++) {
        if (tasks[i].state == 0) {
            htmlStr = htmlStr + '<tr class="danger">';
            stateDesc = '新任务';
        } else if (tasks[i].state == 1) {
            htmlStr = htmlStr + '<tr class="warning">';
            stateDesc = '正在处理';
        } else if (tasks[i].state == 2) {
            htmlStr = htmlStr + '<tr class="success">';
            stateDesc = '已完成';
        } else if (tasks[i].state == -1) {
            htmlStr = htmlStr + '<tr">';
            stateDesc = '已取消';
        }

        // 转换时间
        var time = new Date(tasks[i].createTime).Format("yyyy-MM-dd HH:mm:ss");
        htmlStr = htmlStr + '<td>' + tasks[i].name;
        if (tasks[i].state == 0 || tasks[i].state == 1) {// 尚未发送成功，可以停止
            htmlStr = htmlStr + parseToAbbr('<img src="../../resources/bootstrap-3.3.7-dist/img/stop.png" onclick="changeState(' + tasks[i].id + ', -1)"/>', null, "取消发送");
        } else if (tasks[i].state == -1 || tasks[i].state == 2) {// 取消发送或者发送完成 需要重发
            htmlStr = htmlStr + parseToAbbr('<img src="../../resources/bootstrap-3.3.7-dist/img/redo.png" onclick="changeState(' + tasks[i].id + ', -2)"/>', null, "再次发送");
        }
        htmlStr = htmlStr + '</td><td>' + parseToAbbr(tasks[i].description, 30, null)
            + '</td><td>' + time + '</td><td>'
            + stateDesc + '</td></tr>';
    }
    $("#tasksBody").html(htmlStr);
    $("#myModal").modal("show");
    clearInterval(intervalId);
    intervalId = setInterval(queryTasks, refreshTime);// 自动刷新
    setTimeout(hideModal, 2000);
}

// 隐藏Modal
function hideModal() {
    $("#myModal").modal("hide");
}


// 时间转换
Date.prototype.Format = function (fmt) { //author: meizz
    var o = {
        "M+": this.getMonth() + 1,                 //月份
        "d+": this.getDate(),                    //日
        "H+": this.getHours(),                   //小时
        "m+": this.getMinutes(),                 //分
        "s+": this.getSeconds(),                 //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds()             //毫秒
    };
    if (/(y+)/.test(fmt))
        fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt))
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}

// 取消发送，修改状态即可
function changeState(id, state) {
    var actionName = "";
    if (state == -1) {
        actionName = "取消发送";
    } else if (state == -2) {
        actionName = "再次发送"
    }
    if (confirm("确认‘" + actionName + "’吗？")) {
        // 访问服务器
        $.when(myAjaxGet(bp + 'Smserver/tasks/commitTask/' + id + '/' + state)).done(function (data) {
            if (data != null) {
                var colsNames = data['type'];
                if (colsNames == "GOON") {//修改成功
                    queryTasks();
                }
            }
        }).fail(function () {// 连接失败
            queryTasks();
        });
    } else {
        return;
    }
}