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
    <script src="../../resources/bootstrap-3.3.7-dist/js/askserver.js"></script>
    <script src="../../resources/bootstrap-3.3.7-dist/js/timertask.js"></script>
    <script src="../../resources/bootstrap-3.3.7-dist/js/contact.js"></script>
</head>
<body>
<h3>定时任务设置</h3>

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

    <div id="timertaskEditDiv" style="display: none" class="well"><%--编辑定时任务的表格--%>
        <table class="table table-hover table-bordered text-center">
            <thead>
            <tr class="info">
                <th width="5%" class="text-center">ID</th>
                <th width="10%" class="text-center">功能</th>
                <th width="20%" class="text-center">描述</th>
                <th width="15%" class="text-center">触发规则</th>
                <th width="15%" class="text-center">接收者们</th>
                <th width="10%" class="text-center">接收方式</th>
                <th width="7%" class="text-center">状态</th>
                <th width="15%" class="text-center">操作</th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td width="5%" id="ttIdEdit">新建</td>
                <td width="10%">
                    <label id="ttfunctionsEdit">未指定</label><br>
                    <button type="button" class="btn btn-success btn-sm" onclick="editFunctions()">编辑</button>
                </td>
                <td width="20%">
                    <textarea id="ttdescriptionEdit" placeholder="对这个定时任务用你能理解的方式描述"></textarea>
                </td>
                <td width="15%">
                    <label id="ttcronEdit">未指定</label><br>
                    <button type="button" class="btn btn-primary btn-sm" onclick="editCron()">编辑</button>
                    <div id="editCronDoneDiv" style="display: none">
                        <button type="button" class="btn btn-success btn-sm" onclick="doneCronEdit()">确定</button>
                        <button type="button" class="btn btn-warning btn-sm" onclick="hideCronEdit()">取消</button>
                    </div>
                </td>
                <td width="15%">
                    <textarea id="selectedContactsIds" placeholder="这里将展示选中人员的id"></textarea><br>
                    <button type="button" class="btn btn-success btn-sm" onclick="showContactsUI()">编辑人员</button>
                </td>
                <td width="15%">
                    <input type="checkbox" id="sendTypeSmsEdit">短信<br>
                    <input type="checkbox" id="sendTypeWxEdit">微信
                </td>
                <td width="7%">
                    <select id="ttstateEdit">
                        <option value="run">运行</option>
                        <option value="pause">暂停</option>
                    </select>
                </td>
                <td width="15%">
                    <button type="button" class="btn btn-success btn-sm" onclick="handleTimerTask('save')">保存</button>
                    <button type="button" class="btn btn-warning btn-sm" onclick="handleTimerTask('delete')">删除</button>
                    <button type="button" class="btn btn-primary btn-sm" onclick="cancelEditTimerTask()">取消</button>
                </td>
            </tr>
            </tbody>
        </table>
    </div>

    <div id="cronExpressionDiv" class="embed-responsive embed-responsive-16by9" style="display:none">
        <iframe class="embed-responsive-item well" src="../../resources/cronpage/cronpage.htm"></iframe>
    </div>

    <div id="functionsEditDiv" class="well" style="display: none;text-align:center"> <!--编辑定时任务功能的方法-->
        <h4>在下面勾选你需要的功能</h4>
        <div style="float:left;">
            <button type="button" class="btn btn-success btn-sm" onclick="editFunctionsDone('done')">完成</button>
            <button type="button" class="btn btn-warning btn-sm" onclick="editFunctionsDone('cancle')">取消</button>
        </div>
        <table class="table table-hover table-bordered text-center">
            <thead>
            <tr class="info">
                <th width="15%" class="text-center">ID(<input type="checkbox" id="selectAllFunctions"
                                                              onclick="changeSelectAllFunctions()">全选)
                </th>
                <th width="25%" class="text-center">
                    功能名称
                </th>
                <th width="60%" class="text-center">
                    描述
                </th>
            </tr>
            </thead>
            <tbody id="functionsBody">
            </tbody>
        </table>
    </div>

    <div id="contactsDiv" class="well" style="display: none;text-align:center">
        <div style="width: 20%;float:left;">
            <button type="button" class="btn btn-warning btn-sm" onclick="hideContactsDiv()">取消编辑</button>
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
                    <th width="10%" class="text-center">所在分组</th>
                    <th width="10%" class="text-center">姓名</th>
                    <th width="15%" class="text-center">职务</th>
                    <th width="20%" class="text-center">手机号</th>
                    <th width="10%" class="text-center">等级</th>
                    <th width="20%" class="text-center">备注</th>
                </tr>
                </thead>
                <tbody id="contactsBody">
                </tbody>
            </table>
        </div>
    </div>

    <button type="button" class="btn btn-warning btn-sm" onclick="initEditTimerTask(-1)">新建任务</button>
    <div id="tasks" style="width:100%;float:left;overflow:scroll; height:400px;" class="well">
        <table class="table table-hover table-bordered text-center">
            <thead>
            <tr class="info">
                <th width="12%" class="text-center">ID(<input type="checkbox" id="selectAllTimerTasks"
                                                              onclick="changeSelectAllTimerTasks()">全选)
                </th>
                <th width="12%" class="text-center">功能</th>
                <th width="12%" class="text-center">描述</th>
                <th width="14%" class="text-center">触发规则</th>
                <th width="20%" class="text-center">接收者们</th>
                <th width="14%" class="text-center">接收方式</th>
                <th width="8%" class="text-center">状态</th>
                <th width="8%" class="text-center">操作</th>
            </tr>
            </thead>
            <tbody id="tasksBody">
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
        if(checkAuthorizationStateByUI()) {
            queryTimerTasks();
        }
    });


</script>


</body>
</html>
