<%@page language="java" contentType="text/html; charset=utf-8"
        pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <title>Bootstrap 模板</title>
    <!-- 引入 Bootstrap -->
    <link href="resources/bootstrap-3.3.7-dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body style="padding-left: 20px">
<div class="page-header">
    <h1>Smserver
        <small>如此简单</small>
    </h1>
    <ul class="nav nav-pills">
        <li class="active"><a href="#">首 页</a></li>
        <li><a href="#">定时任务</a></li>
        <li><a href="#">通讯录</a></li>
        <li><a href="#">关 于</a></li>
    </ul>
</div>

<div class="jumbotron">
<table class="table">
    <thead>
    <tr>
        <th>Student-ID</th>
        <th>First Name</th>
        <th>Last Name</th>
        <th>Grade</th>
    </tr>
    </thead>
    <tbody>
    <tr>
        <td>001</td>
        <td>Rammohan </td>
        <td>Reddy</td>
        <td>A+</td>
    </tr>
    <tr>
        <td>002</td>
        <td>Smita</td>
        <td>Pallod</td>
        <td>A</td>
    </tr>
    <tr>
        <td>003</td>
        <td>Rabindranath</td>
        <td>Sen</td>
        <td>A+</td>
    </tr>
    </tbody>
</table>
</div>

<!-- jQuery (Bootstrap 的 JavaScript 插件需要引入 jQuery) -->
<script src="resources/bootstrap-3.3.7-dist/js/jquery-3.1.0.min.js"></script>
<!-- 包括所有已编译的插件 -->
<script src="resources/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>
</body>
</html>
