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
    <title>Bootstrap 模板</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">

    <!-- 引入 Bootstrap -->
    <link href="../../resources/bootstrap-3.3.7-dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="../../resources/bootstrap-3.3.7-dist/js/uiscript.js"></script>
    <script src="../../resources/bootstrap-3.3.7-dist/js/askserver.js"></script>
    <script src="../../resources/bootstrap-3.3.7-dist/js/contact.js"></script>
</head>
<body>
<h3>通讯录操作</h3><h5 style="color: red">请仔细操作，操作结果无法撤销！</h5>
<button type="button" class="btn btn-primary btn-sm" onclick="showOrHideUpload()">通过上传文件编辑通讯录</button>
<div id="uploadDiv" class="bg-success well" style="padding: 5px;display: none"><%--文件上传部分--%>
    <dl>
        <dd>
            <p class="text-warning">
                注意：<br>
                1.每次上传的文件将会覆盖原有数据，请仔细操作。
                <br> ------如果你只编辑文件中的某一部分，其他需要的信息也一定要保留。即，如果原内容包含A、B、C，你仅需要编辑更新A，请将B、C保留，更新后为a、B、C。
                <br>2.如果你不知道上传什么文件，请下载模板文件进行修改后再上传。
                <a href="<%=basePath%>Smserver/download">点击下载《通讯录模板文件》</a>
            </p>
        </dd>
    </dl>
    <form role="form" action="<%=basePath%>Smserver/doUpload" method="post" enctype="multipart/form-data"
          target="hidden_frame">
        <div class="form-group">
            <label for="inputfile">选择文件</label>
            <input type="file" id="inputfile" name="file" accept=".xls">
        </div>
        <button id="submit" type="submit" class="btn btn-default">提交</button>
    </form>
    <br>
    <div id="uploadResult"></div>
    <iframe name='hidden_frame' id="hidden_frame" style='display:none'></iframe>
</div>

<div class="danger" style="padding: 5px;"><%--通讯录操作部分--%>
    <dl>
        <dt>编辑通讯录信息</dt>
        <dd>
            <p class="text-warning">
            </p>
        </dd>
    </dl>
    <div id="editDoneDiv" class="alert alert-success" role="alert"
         style="display: none;width: 30%;margin:0 auto;text-align:center"><%--操作完成--%>
        操作完成！
    </div>

    <div id="editFailDiv" class="alert alert-danger" role="alert"
         style="padding:5px;display: none;width: 30%;margin:0 auto;text-align:center"><%--操作失败--%>
        <h5>操作失败！</h5>
        <p class="text-danger" id="failCause"></p>
    </div>

    <div id="editContactDiv" class="well" style="padding: 5px;display: none;text-align:center"><%--单个联系人信息修改部分--%>
        <table class="text-center">
            <thead>
            <tr class="warning">
                <th width="10%" class="text-center">所在分组</th>
                <th width="10%" class="text-center">姓名</th>
                <th width="15%" class="text-center">职务</th>
                <th width="20%" class="text-center">手机号</th>
                <th width="20%" class="text-center">备注</th>
                <th width="15%" class="text-center">操作</th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td>
                    <select id="editContactGroup"></select>
                </td>
                <td><input class="form-control" id="editContactName" placeholder="姓名"></td>
                <td><input class="form-control" id="editContactTitle" placeholder="职务"></td>
                <td><input class="form-control" id="editContactPhone" placeholder="手机号"></td>
                <td><input class="form-control" id="editContactDescription" placeholder="备注"></td>
                <td>
                    <button type="button" class="btn btn-primary btn-sm" onclick="doEditContact()">完成</button>
                    <button type="button" class="btn btn-danger btn-sm" onclick="deleteContact()">删除</button>
                    <button type="button" class="btn btn-warning btn-sm" onclick="cancleEditContact()">取消</button>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
    <div id="editGroupDiv" class="well" style="padding: 5px;display: none;text-align:center"><%--单个组信息修改部分--%>
        <table class="text-center">
            <thead>
            <tr class="warning">
                <th width="25%" class="text-center">原名称</th>
                <th width="25%" class="text-center">新名称</th>
                <th width="25%" class="text-center">操作</th>
                <th width="25%" class="text-center">说明</th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td id="editGroupOriginalName"></td>
                <td><input class="form-control" id="editGroupName" placeholder="新组的名称"></td>
                <td>
                    <button type="button" class="btn btn-primary btn-sm" onclick="doEditGroup()">修改</button>
                    <button type="button" class="btn btn-danger btn-sm" onclick="deleteGroup()">删除</button>
                    <button type="button" class="btn btn-warning btn-sm" onclick="cancleEditGroup()">取消</button>
                </td>
                <td><h6>操作后该组的所有数据都会被更新</h6></td>
            </tr>
            </tbody>
        </table>
    </div>

    <div id="createGroupDiv" class="well" style="padding: 5px;display: none;text-align:center"><%--创建新分组部分--%>
        <table class="text-center table-condensed">
            <thead>
            <tr class="warning">
                <th width="20%" class="text-center">分组名称</th>
                <th width="40%" class="text-center">分组人员</th>
                <th width="10%" class="text-center">选项</th>
                <th width="10%" class="text-center">操作</th>
                <th width="20%" class="text-center">说明</th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td><input class="form-control" id="newGroupName" placeholder="新组的名称"></td>
                <td>
                    <textarea class="form-control" id="GroupContactsIds" placeholder="这里将展示出添加至该组的人员名单"></textarea>
                    <button type="button" class="btn btn-primary btn-sm" onclick="emptyContactIds()">清空</button>
                </td>
                <td>
                    <input type="radio" name="addContactsType" id="addContactsType1" value="copy" checked>复制<br>
                    <input type="radio" name="addContactsType" id="addContactsType2" value="move">迁移
                </td>
                <td>
                    <button type="button" class="btn btn-primary btn-sm" onclick="createGroup()">确定</button>
                    <button type="button" class="btn btn-warning btn-sm" onclick="cancleCreateGroup()">取消</button>
                </td>
                <td>分组名如果为已有分组名，则会将选中的联系人分组进行操作；<br>
                    如果为新分组，则根据“选项”来确定如何操作联系人
                </td>
            </tr>
            </tbody>
        </table>
    </div>
    <div id="groups" style="width: 20%;float:left;">
        <button type="button" class="btn btn-warning btn-sm" onclick="initCreateGroup(null)">新建分组</button>
    </div>
    <div id="persons" style="width:80%;float:left;">
        <button type="button" class="btn btn-warning btn-sm" onclick="initEditContact(-1,'','','','','')">新建联系人</button>
        <button type="button" class="btn btn-success btn-sm" onclick="initCreateGroup('abc')">集体编辑</button>
        <input id="searchContact" placeholder="输入人员名字">
        <button type="button" class="btn btn-primary btn-sm" onclick="searchContact()">搜索</button>
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
                <th width="15%" class="text-center">序号（<input type="checkbox" id="selectAll"
                                                              onclick="changeSelectAll()">全选）
                </th>
                <th width="15%" class="text-center">所在分组</th>
                <th width="15%" class="text-center">姓名</th>
                <th width="10%" class="text-center">职务</th>
                <th width="15%" class="text-center">手机号</th>
                <th width="15%" class="text-center">备注</th>
                <th width="15%" class="text-center">操作</th>
            </tr>
            </thead>
            <tbody id="contactsBody">
            </tbody>
        </table>
    </div>
</div>

<%--参考：http://www.codingyun.com/article/50.html--%>

<!-- jQuery (Bootstrap 的 JavaScript 插件需要引入 jQuery) -->
<script src="../../resources/bootstrap-3.3.7-dist/js/jquery-3.1.0.min.js"></script>
<!-- 包括所有已编译的插件 -->
<script src="../../resources/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>

<script type="application/javascript">
    var bp = '<%=basePath%>';


    // 文档被加载完成时
    $(document).ready(function () {
        initContactsUIs(null);
    });

</script>
</body>
</html>