/**
 * Created by Administrator on 2016-10-27.
 * taskstate.jsp 页面的 js支持
 */
var refreshTime = 30000;// 30秒
var lasthours = 1;// 1小时
var askers = "all";// 全部
var type = -1; // 全部
var functions = "all";// 全部
var issuccess = -1;// 全部
var intervalId;

function queryAskrecords() {
    var jsonStr = "{\"lasthours\":" + lasthours
        + ",\"type\":\"" + type
        + "\",\"askers\":\"" + askers
        + "\",\"functions\":\"" + functions
        + "\",\"issuccess\":\"" + issuccess + "\"}";
    console.log("jsonStr:" + jsonStr);
    $.when(myAjaxPost(bp + 'Smserver/askrecords/query', jsonStr)).done(function (data) {
        if (data != null) {
            // 初始化tasks相关的控件
            initTbodyOfAskrecords(data['askrecords']);// 选择控件
        }
    });
}

function doQuery() {
    refreshTime = $("#refreshtime option:selected").val() * 60 * 1000;
    lasthours = $("#hours option:selected").val();
    askers = $("#askers").val();
    if (isEmpty(askers)) {
        askers = "all";
    }
    functions = $("#functions").val();
    if (isEmpty(functions)) {
        functions = "all";
    }
    issuccess = $("#issuccess option:selected").val();
    type = $("#type option:selected").val();
    queryAskrecords();
}

// 当发生选择查询时段变化时
function changeQueryHours() {
    // 得到查询时间段
    lasthours = $("#hours option:selected").val();
    queryAskrecords();
}
// 当发生选择刷新时间间隔变化时
function changeRefreshTime() {
    // 得到查询时间段
    refreshTime = $("#refreshtime option:selected").val() * 60 * 1000;
    queryAskrecords();
}
// 当发生选择查询成功与否变化时
function changeQuerySuccess() {
    // 得到查询时间段
    issuccess = $("#issuccess option:selected").val();
    queryAskrecords();
}

// 当发生选择查询类型变化时
function changeQueryType() {
    // 得到查询时间段
    type = $("#type option:selected").val();
    queryAskrecords();
}

// 初始化表格内容
function initTbodyOfAskrecords(askrecords) {
    var htmlStr = '';
    var stateDesc = '';
    for (var i = 0; i < askrecords.length; i++) {
        if (askrecords[i].issuccess == 0) {
            htmlStr = htmlStr + '<tr class="danger">';
            stateDesc = '查询失败';
        } else if (askrecords[i].issuccess == 1) {
            htmlStr = htmlStr + '<tr class="warning">';
            stateDesc = '查询成功';
        }
        // 转换时间
        var time = new Date(askrecords[i].time).Format("yyyy-MM-dd HH:mm:ss");
        htmlStr = htmlStr + '<td>' + time + "</td><td>"
            + parseToAbbr(askrecords[i].name, 0, askrecords[i].phone) + "</td>";
        if (askrecords[i].type == 0) {
            htmlStr = htmlStr + "<td>短信</td>";
        } else if (askrecords[i].type == 1) {
            htmlStr = htmlStr + "<td>微信</td>";
        } else if (askrecords[i].type == 2) {
            htmlStr = htmlStr + "<td>管理员</td>";
        }
        htmlStr = htmlStr + "<td>" + parseToAbbr(askrecords[i].functions, 5, null) + "</td>";
        if (askrecords[i].issuccess == 0) {
            htmlStr = htmlStr + "<td>失败</td>";
        } else if (askrecords[i].issuccess == 1) {
            htmlStr = htmlStr + "<td>成功</td>";
        }
        htmlStr = htmlStr + "<td>" + parseToAbbr(askrecords[i].description, 10, null) + "</td></tr>";
    }
    $("#askrecordsBody").html(htmlStr);
    $("#myModal").modal("show");
    clearInterval(intervalId);
    intervalId = setInterval(queryAskrecords, refreshTime);// 自动刷新
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

