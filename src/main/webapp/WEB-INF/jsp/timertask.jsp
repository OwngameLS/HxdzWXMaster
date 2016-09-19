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

    <div id="tasks" style="width:100%;float:left;overflow:scroll; height:400px;">
        <table class="table table-hover table-bordered text-center">
            <thead>
            <tr class="info">
                <th class="text-center">序号(<input type="checkbox" id="selectAll" onclick="changeSelectAll()">全选)</th>
                <th class="text-center">任务分类</th>
                <th class="text-center">任务描述</th>
                <th class="text-center">接收者们</th>
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
</script>

<!-- jQuery (Bootstrap 的 JavaScript 插件需要引入 jQuery) -->
<script src="../../resources/bootstrap-3.3.7-dist/js/jquery-3.1.0.min.js"></script>
<!-- 包括所有已编译的插件 -->
<script src="../../resources/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>
</body>
</html>
