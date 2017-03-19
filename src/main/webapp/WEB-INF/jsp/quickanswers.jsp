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
    <script src="../../resources/bootstrap-3.3.7-dist/js/pager.js"></script>
    <script src="../../resources/bootstrap-3.3.7-dist/js/askserver.js"></script>
    <script src="../../resources/bootstrap-3.3.7-dist/js/quickanswers.js"></script>
</head>
<body>
<h3>关键字设置</h3>

<div class="danger" style="padding: 5px;">
    <div id="editFailDiv" class="alert alert-danger alert-dismissible" style="display:none" role="alert"><%--操作失败--%>
        <div style="width: 100%;" class="text-right">
            <a href="javascript:void(0)" onclick="hideEditFail();return false;" title="关闭"><img
                    src="../../resources/bootstrap-3.3.7-dist/img/cross-red.png"></a>
        </div>
        <h5>操作失败！</h5>
        <p class="text-danger" id="failCause"></p>
    </div>

    <div class="well">
        <div id="addKeywords">
            <div id="add" class="row bg-warning">
                <div class="col-md-4 text-left">
                    <img src="../../resources/bootstrap-3.3.7-dist/img/add.png">新增关键字
                </div>
                <div class="col-md-6 text-left">
                </div>
            </div>
            <div class="row" id="addSettingEditDiv">
                <div class="row">
                    <div class="col-md-1 text-right">关键字:</div>
                    <div class="col-md-2 text-left"><input id="addKeyname"></div>
                    <div class="col-md-1 text-right">返回结果:</div>
                    <div class="col-md-2 text-left"><input id="addResult"></div>
                    <div class="col-md-1 text-right">描述:</div>
                    <div class="col-md-2 text-left"><input id="addDescription"></div>
                    <div class="col-md-1 text-center">
                        <button class="btn btn-success btn-sm" id="addStBtn" onclick="addQuickanswer()"> 确定添加</button>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="well">
        <div id="other" class="row bg-info">
            <div class="col-md-4 text-left">
                <img src="../../resources/bootstrap-3.3.7-dist/img/other_settings.png">在下面修改关键字配置
            </div>
            <div class="col-md-6 text-left">
            </div>
        </div>
        <div id="keywordsSettings" style="width:100%;float:left;overflow:scroll; height:320px;"></div>
        <div id="pageSelectDivKeywords" style="text-align:center;width:100%;">
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
        if (checkAuthorizationStateByUI()) {
            queryQuickAnswers();// 获得所有关键字信息
        }
    });

</script>
</body>
</html>
