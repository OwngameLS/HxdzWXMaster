<%@page language="java" contentType="text/html; charset=utf-8"
        pageEncoding="utf-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <title>Smserver</title>
    <!-- 引入 Bootstrap -->
    <link href="resources/bootstrap-3.3.7-dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="resources/bootstrap-3.3.7-dist/js/settings.js"></script>
    <script src="resources/bootstrap-3.3.7-dist/js/askserver.js"></script>
    <script src="resources/bootstrap-3.3.7-dist/js/authorization.js"></script>
</head>
<body>
<div class="container">
    <nav class="navbar navbar-inverse" role="navigation">
        <div class="container-fluid">
            <!-- Brand and toggle get grouped for better mobile display -->
            <div class="navbar-header">
                <button type="button" class="navbar-toggle collapsed" data-toggle="collapse"
                        data-target="#bs-example-navbar-collapse-1">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand">Smserver</a>
            </div>

            <div class="collapse navbar-collapse">
                <ul class="nav navbar-nav nav-pills">
                    <li><a href="<%=basePath%>Smserver/view/taskstate" target="display">首 页</a></li>
                    <li><a href="<%=basePath%>Smserver/view/askrecords" target="display">查询记录</a></li>
                    <li><a href="<%=basePath%>Smserver/view/timertask" target="display">定时任务</a></li>
                    <li><a href="<%=basePath%>Smserver/view/contact" target="display">通讯录</a></li>
                    <li><a href="<%=basePath%>Smserver/view/function" target="display">功 能</a></li>
                    <%--target指的是name--%>
                    <li><a href="<%=basePath%>Smserver/view/msg" target="display">信息群发</a></li>
                    <li><a id="authorize" href="<%=basePath%>Smserver/view/settings" target="display"><p>系统设置</p></a>
                    </li>
                    <li id="unauth" style="display: none"><a><img
                            src="resources/bootstrap-3.3.7-dist/img/cross-red.png">未授权</a></li>
                </ul>
            </div><!-- /.navbar-collapse -->
        </div><!-- /.container-fluid -->
    </nav>

    <!-- 模态框（Modal） -->
    <div class="modal fade" id="authorizedModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
         aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title" id="myModalLabel">授权失败</h4>
                </div>
                <div class="modal-body">
                    <h1 style="color: red">授权失败！</h1>
                    <h3>失败原因：</h3>
                    <h3 id="invalidReason"></h3>
                    <h5 id="countdowndesc"></h5>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-warning" data-dismiss="modal">关闭</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal -->
    </div>

    <!-- 16:9 aspect ratio -->
    <div class="embed-responsive embed-responsive-16by9">
        <iframe class="embed-responsive-item" name="display" src="<%=basePath%>Smserver/view/taskstate"></iframe>
    </div>
    <footer class="center-block container text-center">
        <p>------技术支持------</p>
        <p>手 机：18107436127 QQ：1003919353</p>
        <p>Copyright © All Rights Reserved By 龙生(Owngame).</p>
    </footer>
</div>


<!-- jQuery (Bootstrap 的 JavaScript 插件需要引入 jQuery) -->
<script src="resources/bootstrap-3.3.7-dist/js/jquery-3.1.0.min.js"></script>
<!-- 包括所有已编译的插件 -->
<script src="resources/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>

<script type="application/javascript">
    var bp = '<%=basePath%>';
    // 文档被加载完成时
    $(document).ready(function () {
        getAuthorizedState();
    });
</script>

</body>
</html>
