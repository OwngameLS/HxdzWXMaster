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
    <title>短信群发</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <!-- 引入 Bootstrap -->
    <link href="../../resources/bootstrap-3.3.7-dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="../../resources/bootstrap-3.3.7-dist/js/uiscript.js"></script>
    <script src="../../resources/bootstrap-3.3.7-dist/js/askserver.js"></script>
    <script src="../../resources/bootstrap-3.3.7-dist/js/msg.js"></script>
    <script src="../../resources/bootstrap-3.3.7-dist/js/contact.js"></script>
</head>
<body>
<h3>信息群发</h3>

<div class="danger" style="padding: 5px;"><%--通讯录操作部分--%>
    <div id="editDoneDiv" class="alert alert-success" role="alert"
         style="display: none;width: 30%;margin:0 auto;text-align:center"><%--操作完成--%>
        操作完成！
    </div>

    <div id="editFailDiv" class="alert alert-danger alert-dismissible" style="display:none" role="alert"><%--操作失败--%>
        <div style="width: 100%;" class="text-right">
            <a href="javascript:void(0)" onclick="hideEditFail();return false;" title="关闭"><img src="../../resources/bootstrap-3.3.7-dist/img/cross-red.png"></a>
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

    <div id="contactsDiv" class="bg-success" style="display: none;text-align:center">
        <div style="width: 20%;float:left;">
            <button type="button" class="btn btn-warning btn-sm" onclick="hideContactsDiv()">收起</button>
        </div>
        <div style="width:80%;float:left;">
            <button type="button" class="btn btn-success btn-sm" onclick="editContacts('add')">添加选择的人员</button>
            <button type="button" class="btn btn-warning btn-sm" onclick="editContacts('remove')">删除选择的人员</button>
        </div>
        <div id="groups" style="width: 20%;float:left;">
            <table class="table table-striped text-center table-bordered">
                <thead>
                <tr class="warning">
                    <th class="text-center">
                        点击下列分组查看组员
                    </th>
                </tr>
                </thead>
                <tbody id="groupsBody">
                <tr>
                    <td>
                        <button type="button" class="btn btn-danger btn-sm" onclick="queryContactsDetailsWithIds()">
                            已选人员
                        </button>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
        <div id="persons" style="width:80%;float:left;overflow:scroll; height:400px;">
            <table class="table table-hover table-bordered text-center">
                <thead>
                <tr class="info">
                    <th width="15%" class="text-center">ID(<input type="checkbox" id="selectAllContacts"
                                                                  onclick="changeSelectAllContacts()">全选)
                    </th>
                    <th width="15%" class="text-center">所在分组</th>
                    <th width="15%" class="text-center">姓名</th>
                    <th width="10%" class="text-center">职务</th>
                    <th width="20%" class="text-center">手机号</th>
                    <th width="10%" class="text-center">等级</th>
                    <th width="15%" class="text-center">备注</th>
                </tr>
                </thead>
                <tbody id="contactsBody">
                </tbody>
            </table>
        </div>
    </div>


    <div class="well">
        <div class="row">
            <div id="titleOfcontents" class="col-md-2">
                发送内容：
            </div>
            <div class="col-md-6">
            </div>
            <div class="col-md-2 text-center">
                <button type="button" class="btn btn-danger btn-sm" onclick="emptyContents()">清空内容</button>
            </div>
            <div class="col-md-2 text-center">
                <button type="button" class="btn btn-primary btn-sm" onclick="useFunction()">使用功能的结果</button>
            </div>
        </div>
        <div class="row">
            <div style="height: 100px;overflow:scroll;" class="col-md-12 text-center">
                <textarea id="message" style="height: 100%;width: 100%" placeholder="自行输入内容或者使用“功能”得到其结果。"></textarea>
            </div>
        </div>
    </div>

    <div class="well">
        <div class="row">
            <div id="titleOfReceivers" class="col-md-2">
                发送人员：
            </div>
            <div class="col-md-2">
                <input class="form-control" id="newphone" placeholder="填写合法的手机号...">
            </div>
            <div class="col-md-2">
                <button type="button" class="btn btn-primary btn-sm" onclick="addAnonymous()">添加</button>
            </div>
            <div class="col-md-2">
            </div>
            <div class="col-md-2 text-center">
                <button type="button" class="btn btn-danger btn-sm" onclick="removeAllContacts()">清空人员</button>
            </div>
            <div class="col-md-2 text-center">
                <button type="button" class="btn btn-primary btn-sm" onclick="showContactsUI()">添加人员</button>
            </div>
        </div>
        <div class="row">
            <div id="receivers" style="height: 180px;overflow:scroll;" class="col-md-12 text-center">
            </div>
        </div>
    </div>

    <div class="well">
        <div class="row">
            <div class="col-md-2">
                发送方式：
            </div>
            <div class="col-md-1">
                <input type="checkbox" id="sendTypeSms" checked>短信
            </div>
            <div class="col-md-2">
                <input type="checkbox" id="sendTypeWx"><abbr title="如果这个手机号对应的有微信号的话，就会发送">微信</abbr>
            </div>
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

   /* // 文档被加载完成时
    $(document).ready(function () {
        queryTimerTasks();
    });
*/

</script>


</body>
</html>
