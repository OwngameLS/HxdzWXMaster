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
    <title>任务状态</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <!-- 引入 Bootstrap -->
    <link href="../../resources/bootstrap-3.3.7-dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="../../resources/bootstrap-3.3.7-dist/js/uiscript.js"></script>
    <script src="../../resources/bootstrap-3.3.7-dist/js/askserver.js"></script>
    <script src="../../resources/bootstrap-3.3.7-dist/js/taskstate.js"></script>
</head>
<body>
<h3>任务状态</h3>

<div class="danger" style="padding: 5px;">
    <!-- 模态框（Modal） -->
    <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                </div>
                <div id="mbody" class="modal-body"><img src="../../resources/bootstrap-3.3.7-dist/img/success.png"/>刷新成功
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal -->
    </div>


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
    <div id="tasks" style="width:100%;float:left;overflow:scroll; height:400px;" class="well">
        <table class="table table-hover table-bordered text-center">
            <thead>
            <tr class="info">
                <th width="20%" class="text-center">任务名称</th>
                <th width="50%" class="text-center">任务描述</th>
                <th width="20%" class="text-center">生成时间</th>
                <th width="10%" class="text-center">当前状态</th>
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
        if(checkAuthorizationStateByUI()) {
            queryTasks();// 默认查询一小时之内的
        }
    });


</script>
</body>
</html>
