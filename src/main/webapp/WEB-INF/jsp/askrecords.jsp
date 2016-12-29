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
    <title>用户查询详情</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <!-- 引入 Bootstrap -->
    <link href="../../resources/bootstrap-3.3.7-dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="../../resources/bootstrap-3.3.7-dist/js/uiscript.js"></script>
    <script src="../../resources/bootstrap-3.3.7-dist/js/askserver.js"></script>
    <script src="../../resources/bootstrap-3.3.7-dist/js/askrecords.js"></script>
</head>
<body>
<h3>查询记录</h3>

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

    <div class="row bg-success">
        <div class="col-md-2 text-center">刷新间隔</div>
        <div class="col-md-1 text-center">查询时段</div>
        <div class="col-md-2 text-center"><abbr title="多个人请用英文逗号分隔">请求人</abbr></div>
        <div class="col-md-1 text-center">查询方式</div>
        <div class="col-md-3 text-center"><abbr title="多个请用英文逗号分隔">查询功能</abbr></div>
        <div class="col-md-1 text-center">是否成功</div>
        <div class="col-md-2 text-center">操作</div>
    </div>
    <div class="row">
        <div class="row bg-success">
            <div class="col-md-2 text-center">
                <select id="refreshtime" onchange="changeRefreshTime()">
                    <option value="0.5" selected>30秒</option>
                    <option value="1">1分钟</option>
                    <option value="5">5分钟</option>
                    <option value="10">10分钟</option>
                    <option value="30">30分钟</option>
                </select>
            </div>
            <div class="col-md-1 text-center">
                <select id="hours" onchange="changeQueryHours()">
                    <option value="1" selected>最近1小时</option>
                    <option value="3">最近3小时</option>
                    <option value="12">最近12小时</option>
                    <option value="24">最近1天</option>
                    <option value="-1">全部</option>
                </select>
            </div>
            <div class="col-md-2 text-center">
                <input class="form-control" id="askers" placeholder="查询人姓名..">
            </div>
            <div class="col-md-1 text-center">
                <select id="type" onchange="changeQueryType()">
                    <option value="-1" selected>全部</option>
                    <option value="0">短信</option>
                    <option value="1">微信</option>
                    <option value="2">管理员</option>
                </select>
            </div>
            <div class="col-md-3 text-center">
                <input class="form-control" id="functions" placeholder="查询功能..">
            </div>
            <div class="col-md-1 text-center">
                <select id="issuccess" onchange="changeQuerySuccess()">
                    <option value="-1" selected>全部</option>
                    <option value="0">失败</option>
                    <option value="1">成功</option>
                </select>
            </div>
            <div class="col-md-2 text-center">
                <button type="button" class="btn btn-success btn-sm" onclick="doQuery()">搜索</button>
            </div>
        </div>
    </div>


    <div id="askrecords" style="width:100%;float:left;overflow:scroll; height:400px;" class="well">
        <table class="table table-hover table-bordered text-center">
            <thead>
            <tr class="info">
                <th width="20%" class="text-center">询问时间</th>
                <th width="15%" class="text-center">请求人</th>
                <th width="10%" class="text-center">查询方式</th>
                <th width="20%" class="text-center">查询内容</th>
                <th width="10%" class="text-center">是否成功</th>
                <th width="25%" class="text-center">描述</th>
            </tr>
            </thead>
            <tbody id="askrecordsBody">
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
        queryAskrecords();// 默认查询一小时之内的
    });

</script>
</body>
</html>
