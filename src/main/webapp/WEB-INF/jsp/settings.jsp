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
    <title>系统设置</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <!-- 引入 Bootstrap -->
    <link href="../../resources/bootstrap-3.3.7-dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="../../resources/bootstrap-3.3.7-dist/js/uiscript.js"></script>
    <script src="../../resources/bootstrap-3.3.7-dist/js/askserver.js"></script>
    <script src="../../resources/bootstrap-3.3.7-dist/js/settings.js"></script>
</head>
<body>
<h3>系统设置</h3>

<div class="danger" style="padding: 5px;">
    <div id="editFailDiv" class="alert alert-danger" role="alert"
         style="padding:5px;display: none;width: 30%;margin:0 auto;text-align:center"><%--操作失败--%>
        <h5>操作失败！</h5>
        <p class="text-danger" id="failCause"></p>
    </div>

    <div class="well">
        <div class="row text-center">
            <div class="col-md-3 text-center">
                <img src="../../resources/bootstrap-3.3.7-dist/img/cry.png">
                <h3 id="authorizestate" style="color: red"> 您尚未获得授权 </h3>
            </div>
            <div class="col-md-5 text-left">
                <div class="row">
                    <div class="col-md-6 text-right">
                        您的名字
                    </div>
                    <div class="col-md-6 text-left">
                        <input id="username" class="form-control">
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6 text-right">
                        授权手机号码(<a href="http://www.baidu.com">哪里找？</a>)
                    </div>
                    <div class="col-md-6 text-left">
                        <input id="authorizerphone" class="form-control">
                    </div>
                </div>
                <div class="text-center">
                    <button id="getAuthorized" onclick="authorizationRequest()">获取授权</button>
                </div>
            </div>
            <div class="col-md-3 text-left">
                <a href="<%=basePath%>Smserver/download/app"><img
                        src="../../resources/bootstrap-3.3.7-dist/img/app.png">下载App(仅限Android手机) </a>
                <br>
                <a href="<%=basePath%>Smserver/download/guidebook"><img
                        src="../../resources/bootstrap-3.3.7-dist/img/help.png">帮助 </a>
            </div>
        </div>

    </div>
    <div class="well">
        <div id="dbSettings">
            <div class="row bg-primary">
                <div class="col-md-4 text-left">
                    <img src="../../resources/bootstrap-3.3.7-dist/img/database.png">数据库配置
                </div>
                <div class="col-md-6 text-center">

                </div>
            </div>
            <div class="row">
                <div class="col-md-4 text-right">
                    用户名
                </div>
                <div class="col-md-6 text-left">
                    <input class="form-control">
                </div>
            </div>
            <div class="row">
                <div class="col-md-4 text-right">
                    密码
                </div>
                <div class="col-md-6 text-left">
                    <input class="form-control">
                </div>
            </div>
            <div class="row">
                <div class="col-md-4 text-right">
                    端口号
                </div>
                <div class="col-md-6 text-left">
                    <input class="form-control">
                </div>
            </div>

        </div>
    </div>
    <div class="well">
        <div>
            <input type="checkbox" id="isuseWx">是否有微信公众号
        </div>
        <div id="wxSettings">
            <div class="row bg-success">
                <div class="col-md-4 text-left">
                    <img src="../../resources/bootstrap-3.3.7-dist/img/wechat.png">服务器配置
                </div>
                <div class="col-md-6 text-left">

                </div>
            </div>
            <div class="row">
                <div class="col-md-4 text-right">
                    AppID(应用ID)
                </div>
                <div class="col-md-6 text-left">
                    <input class="form-control">
                </div>
            </div>
            <div class="row">
                <div class="col-md-4 text-right">
                    AppSecret(应用密钥)
                </div>
                <div class="col-md-6 text-left">
                    <input class="form-control">
                </div>
            </div>
            <div class="row">
                <div class="col-md-4 text-right">
                    URL(服务器地址)
                </div>
                <div class="col-md-6 text-left">
                    <input class="form-control">
                </div>
            </div>
            <div class="row">
                <div class="col-md-4 text-right">
                    Token(令牌)
                </div>
                <div class="col-md-6 text-left">
                    <input class="form-control">
                </div>
            </div>
            <div class="row">
                <div class="col-md-4 text-right">
                    EncodingAESKey(消息加解密密钥)
                </div>
                <div class="col-md-6 text-left">
                    <input class="form-control">
                </div>
            </div>
        </div>
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
//        queryAskrecords();// 默认查询一小时之内的
    });


</script>
</body>
</html>