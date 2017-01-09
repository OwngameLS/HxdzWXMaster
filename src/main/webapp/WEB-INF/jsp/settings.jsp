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
    <div id="editFailDiv" class="alert alert-danger alert-dismissible" style="display:none" role="alert"><%--操作失败--%>
        <div style="width: 100%;" class="text-right">
            <a href="javascript:void(0)" onclick="hideEditFail();return false;" title="关闭"><img src="../../resources/bootstrap-3.3.7-dist/img/cross-red.png"></a>
        </div>
        <h5>操作失败！</h5>
        <p class="text-danger" id="failCause"></p>
    </div>

    <div class="well">
        <div class="row text-center">
            <div class="col-md-3 text-center">
                <img id="authorizestateimg" src="../../resources/bootstrap-3.3.7-dist/img/smiley.png">
                <h3 id="authorizestatedes"> 您已获得授权。 </h3>
                <h5 id="invalidReason"></h5>
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
            <div id="db" class="row bg-primary">
                <div class="col-md-4 text-left">
                    <img src="../../resources/bootstrap-3.3.7-dist/img/database.png">数据库配置
                </div>
                <div class="col-md-6 text-center">
                </div>
            </div>
        </div>
    </div>
    <div class="well">
        <div>
            <input type="checkbox" id="isUseWXMP" onclick="usingWXMP()">是否使用微信服务号
        </div>
        <div id="wxSettings">
            <div id="wx" class="row bg-success">
                <div class="col-md-4 text-left">
                    <img src="../../resources/bootstrap-3.3.7-dist/img/wechat.png">服务器配置
                </div>
                <div class="col-md-6 text-left">

                </div>
            </div>
        </div>
    </div>

    <div class="well">
        <div id="otherSettings">
            <div id="other" class="row bg-info">
                <div class="col-md-4 text-left">
                    <img src="../../resources/bootstrap-3.3.7-dist/img/other_settings.png">其他配置
                </div>
                <div class="col-md-6 text-left">
                </div>
            </div>
        </div>
    </div>

    <div class="well">
        <div id="addSettings">
            <div id="add" class="row bg-warning">
                <div class="col-md-4 text-left">
                    <img src="../../resources/bootstrap-3.3.7-dist/img/add.png">新增配置
                </div>
                <div class="col-md-6 text-left">
                </div>
            </div>
            <div class="row" id="addSettingEditDiv">
                <div class="row">
                    <div class="col-md-1 text-right"></div>
                    <div class="col-md-1 text-right">描述</div>
                    <div class="col-md-2 text-left"><input id="addDescription" class="form-control"></div>
                    <div class="col-md-1 text-right"><abbr title="在数据库中的英文名">name</abbr></div>
                    <div class="col-md-2 text-left"><input id="addName" class="form-control"></div>
                    <div class="col-md-1 text-right">值</div>
                    <div class="col-md-2 text-left"><input id="addValue" class="form-control"></div>
                </div>
                <div class="row">
                    <div class="col-md-1 text-right"></div>
                    <div class="col-md-1 text-right">依赖于</div>
                    <div class="col-md-2 text-left"><select class="form-control" id="addReferto"></select></div>
                    <div class="col-md-2 text-right">是否作为被依赖的属性</div>
                    <div class="col-md-2 text-left"><input type="checkbox" id="addBerefered">（勾选）</div>
                    <div class="col-md-2 text-left"><button class="btn btn-success btn-sm" id="addStBtn" onclick="addSettings()"> 确定添加</button></div>
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
        getSettings();// 获得所有设置信息
    });

</script>
</body>
</html>
