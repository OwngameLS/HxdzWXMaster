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
//        console.log("at here : " + new Date().Format("yyyy-MM-dd HH:mm:ss"));
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
        }
        // 转换时间

        var time = new Date(tasks[i].createTime).Format("yyyy-MM-dd HH:mm:ss");
        htmlStr = htmlStr + '<td>' + tasks[i].name
            + '</td><td>' + tasks[i].description
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
