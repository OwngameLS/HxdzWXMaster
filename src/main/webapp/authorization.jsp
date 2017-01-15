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
    <title>用户授权</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <!-- 引入 Bootstrap -->
    <link href="resources/bootstrap-3.3.7-dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="resources/bootstrap-3.3.7-dist/js/uiscript.js"></script>
    <script src="resources/bootstrap-3.3.7-dist/js/askserver.js"></script>
    <script src="resources/bootstrap-3.3.7-dist/js/authorization.js"></script>
</head>
<body>
<h3>用户授权</h3>

<div class="danger" style="padding: 5px;">
    <div id="editFailDiv" class="alert alert-danger alert-dismissible" style="display:none" role="alert"><%--操作失败--%>
        <div style="width: 100%;" class="text-right">
            <a href="javascript:void(0)" onclick="hideEditFail();return false;" title="关闭"><img
                    src="resources/bootstrap-3.3.7-dist/img/cross-red.png"></a>
        </div>
        <h5>操作失败！</h5>
        <p class="text-danger" id="failCause"></p>
    </div>

    <div class="well">
        用户申请：
        <div id="user">
            <div class="row bg-primary">
                <div class="col-md-2 text-left">

                </div>
                <div class="col-md-4 text-left">
                    <img src="resources/bootstrap-3.3.7-dist/img/user.png">
                </div>
                <div class="col-md-6 text-center">
                </div>
            </div>
            <br>
            <div class="row">
                <div class="col-md-4 text-right">自定义你的用户名</div>
                <div class="col-md-2 text-left"><input class="form-control" id="username"
                                                       oninput="changeInputUsername()"/></div>
                <div class="col-md-1 text-left" id="usernameCheckResult"></div>
                <div class="col-md-2 text-left">
                    <button type="button" class="btn bg-success" id="checkUsername" onclick="checkUsername()">检测用户名
                    </button>
                </div>

            </div>
            <div class="row">
                <div class="col-md-4 text-right">您将使用的手机号码</div>
                <div class="col-md-2 text-left"><input class="form-control" id="phone"/></div>
                <div class="col-md-1 text-left" id="phoneCheckResult"></div>
            </div>
            <div class="row">
                <div class="col-md-4 text-right"></div>
                <div class="col-md-4 text-right">
                    <button type="button" class="btn bg-danger" id="register" onclick="register()">注册</button>
                </div>
            </div>
            <div>
                <div class="col-md-2 text-right"></div>
                <div id="registerResult" class="col-md-8 text-center"></div>
            </div>
        </div>
    </div>
    <div class="well">
        管理员：
        <div>
            <div class="row bg-success">
                <div class="col-md-2 text-left">
                </div>
                <div class="col-md-4 text-left">
                    <img src="resources/bootstrap-3.3.7-dist/img/admin.png">
                </div>
                <div class="col-md-6 text-center">
                </div>
            </div>
        </div>
        <br>
        <div id="admin">
            <div class="row">
                <div class="col-md-4 text-right">密码</div>
                <div class="col-md-2 text-left"><input type="password" class="form-control" id="password"/></div>
            </div>
            <div class="row">
                <div class="col-md-4 text-right"></div>
                <div class="col-md-4 text-right">
                    <button type="button" class="btn bg-primary" id="loginAdmin" onclick="loginAdmin()">登录</button>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- jQuery (Bootstrap 的 JavaScript 插件需要引入 jQuery) -->
<script src="resources/bootstrap-3.3.7-dist/js/jquery-3.1.0.min.js"></script>
<!-- 包括所有已编译的插件 -->
<script src="resources/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>

<script type="application/javascript">
    var bp = '<%=basePath%>';

</script>
</body>
</html>
