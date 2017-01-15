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
    <title>Smserver短信群发</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <!-- 引入 Bootstrap -->
    <link href="../../resources/bootstrap-3.3.7-dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="../../resources/bootstrap-3.3.7-dist/js/uiscript.js"></script>
    <script src="../../resources/bootstrap-3.3.7-dist/js/askserver.js"></script>
    <script src="../../resources/bootstrap-3.3.7-dist/js/wxmsg.js"></script>
</head>
<body>
<h3>Smserver短信群发</h3>
<div class="danger" style="padding: 5px;"><%--通讯录操作部分--%>
    <div id="editDoneDiv" class="alert alert-success" role="alert"
         style="display: none;width: 30%;margin:0 auto;text-align:center"><%--操作完成--%>
        操作完成！
    </div>

    <div id="editFailDiv" class="alert alert-danger alert-dismissible" style="display:none" role="alert"><%--操作失败--%>
        <div style="width: 100%;" class="text-right">
            <a href="javascript:void(0)" onclick="hideEditFail();return false;" title="关闭"><img
                    src="../../resources/bootstrap-3.3.7-dist/img/cross-red.png"></a>
        </div>
        <h5>操作失败！</h5>
        <p class="text-danger" id="failCause"></p>
    </div>

    <!-- 模态框（Modal） -->
    <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title" id="myModalLabel">选择你将使用功能</h4>
                </div>
                <div id="mbody" class="modal-body"></div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-success" onclick="getResults()">确定</button>
                    <button type="button" class="btn btn-warning" data-dismiss="modal">关闭</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal -->
    </div>


    <div class="well">
        <div>
            发送内容：
            <button type="button" class="btn btn-danger btn-sm" onclick="emptyContents()">清空内容</button>
        </div>
        <div>
            <textarea id="message" style="width: 100%" placeholder="自行输入内容或者使用“功能”得到其结果。"></textarea>
        </div>
    </div>
    <div class="well">
        <div>
            功能查询：
        </div>
        <div id="functions">
            <table class="table table-striped text-center table-bordered">
                <tbody id="functionsBody">
                </tbody>
            </table>
        </div>
        <div>
            发送群组：
        </div>
        <div id="groups">
            <table class="table table-hover table-bordered text-center">
                <tbody id="groupsBody">
                </tbody>
            </table>
        </div>
    </div>

    <div class="well">
        <div class="row">
            <div class="col-md-2">
                发送方式：
            </div>
            <div class="col-md-1 text-center">
                <input type="checkbox" id="sendTypeSms" checked>短信 <input type="checkbox" id="sendTypeWx">微信
            </div>
            <br>
            <div class="col-md-2 text-center">
                <button type="button" class="btn btn-primary btn-sm" onclick="createTask()">发 送</button>
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
        if(checkAuthorizationStateByUI()) {
            // 得到可用功能和群组信息
            getUsableFunctionsAndGroups();
        }
    });

</script>


</body>
</html>
