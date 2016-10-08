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
<h2>当前任务</h2>

<div class="danger" style="padding: 5px;"><%--通讯录操作部分--%>


    <div style="width:30%;float: left">
        <label for="hours">选择查询时段：</label>
        <select id="hours" onchange="changeQueryHours()">
            <option value="1">最近1小时</option>
            <option value="3">最近3小时</option>
            <option value="5">最近5小时</option>
            <option value="10">最近10小时</option>
            <option value="24">最近1天</option>
        </select>
    </div>
    <div style="width:30%;float: left">
        <label for="hours">自动刷新时间：</label>
        <select id="refreshtime" onchange="changeRefreshTime()">
            <option value="0.5" selected>30秒</option>
            <option value="1">1分钟</option>
            <option value="5">5分钟</option>
            <option value="10">10分钟</option>
            <option value="30">30分钟</option>
        </select>
    </div>
    <div id="tasks" style="width:100%;float:left;overflow:scroll; height:400px;">
        <table class="table table-hover table-bordered text-center">
            <thead>
            <tr class="info">
                <th width="20%" class="text-center">任务名称</th>
                <th width="50%" class="text-center">描述</th>
                <th width="20%" class="text-center">时间</th>
                <th width="10%" class="text-center">状态</th>
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
    var refreshTime = 30000;// 30秒
    var lasthours = 1;// 1小时
    var intervalId;
    // 文档被加载完成时
    $(document).ready(function () {
        queryTasks();// 默认查询一小时之内的
    });

    function queryTasks() {
        $.ajax({
            url: bp + 'Smserver/tasks/' + lasthours,
            type: 'GET',
            success: function (data) {
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
        console.log("at here : " + new Date().Format("yyyy-MM-dd HH:mm:ss"));
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
        clearInterval(intervalId);
        intervalId = setInterval(queryTasks, refreshTime);// 自动刷新
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

</script>
</body>
</html>
