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
</head>
<body>
<h3>通讯录操作</h3>
<button type="button" class="btn btn-primary btn-sm" onclick="showOrHideUpload()">上传通讯录</button>
<div id="uploadDiv" class="bg-success" style="padding: 20px;display: none"><%--文件上传部分--%>
    <dl>
        <dd>
            <p class="text-warning">
                注意：<br>
                1.每次上传的文件将会覆盖原有数据，请仔细操作！
                2.如果你不知道上传什么文件，请下载模板文件进行修改后再上传。
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
<div id="editContactDiv" class="bg-warning" style="padding: 20px;display: none"><%--单个联系人信息修改部分--%>
    <table class="text-center">
        <thead>
        <tr class="warning">
            <th class="text-center">所在分组</th>
            <th class="text-center">姓名</th>
            <th class="text-center">职务</th>
            <th class="text-center">手机号</th>
            <th class="text-center">备注</th>
            <th class="text-center">操作</th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td>
                <select id="editContactGroup">

                </select>
            </td>
            <td><input class="form-control" id="editContactName" placeholder="姓名"></td>
            <td><input class="form-control" id="editContactTitle" placeholder="职务"></td>
            <td><input class="form-control" id="editContactPhone" placeholder="手机号"></td>
            <td><input class="form-control" id="editContactDescription" placeholder="备注"></td>
            <td>
                <button type="button" class="btn btn-primary btn-sm" onclick="doEditContact()">修改</button>
                <button type="button" class="btn btn-danger btn-sm" onclick="deleteContact()">删除</button>
                <button type="button" class="btn btn-warning btn-sm" onclick="cancleEditContact()">取消</button>
            </td>
        </tr>
        </tbody>
    </table>
</div>
<div class="danger" style="padding: 20px;"><%--通讯录操作部分--%>
    <dl>
        <dt>编辑通讯录信息</dt>
        <dd>
            <p class="text-warning">
                1.每次上传的文件将会覆盖原有数据，请仔细操作！
            </p>
        </dd>
    </dl>
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
                <th class="text-center">id(<input type="checkbox" id="selectAll" onclick="changeSelectAll()">全选)</th>
                <th class="text-center">所在分组</th>
                <th class="text-center">姓名</th>
                <th class="text-center">职务</th>
                <th class="text-center">手机号</th>
                <th class="text-center">备注</th>
                <th class="text-center">操作</th>
            </tr>
            </thead>
            <tbody id="contactBody">
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
    var updateContactId;

    // 文档被加载完成时
    $(document).ready(function () {
        $.ajax({
            url: bp + 'Smserver/contacts/groups',
            type: 'GET',
            success: function (data) {
                var groups = data['groups'];
                // 初始化groups相关的控件
                initSelect(groups);// 选择控件
                initGroupsBody(groups);// 分组链接
                getContactsByGroups(groups[0]);
            }
        });
    });

    // 向服务器请求联系人信息 通过分组名称
    function getContactsByGroups(groupname) {
        $.ajax({
            type: 'GET',
            url: bp + 'Smserver/contacts/' + groupname,
            success: function (data) {
                var contacts = data['contacts'];
                initTbodyOfContacts(contacts);

            }
        });
    }

    // 初始化单独编辑联系人信息的分组选项
    function initSelect(groups) {
        var html = '';
        for (var i = 0; i < groups.length; i++) {
            html = html + '<option value ="' + groups[i] + '">' + groups[i] + '</option>';
        }
        $("#editContactGroup").html(html);
    }

    // 初始化groups选择链接
    function initGroupsBody(groups) {
        var html = '';
        for (var i = 0; i < groups.length; i++) {
            html = html + '<tr><td><button type="button" class="btn btn-danger btn-sm" onclick="getContactsByGroups(\'' + groups[i] + '\')">' + groups[i] + '</button></td></tr>';
        }
        $("#groupsBody").html(html);
    }

    // 当上传通讯录成功时调用
    function uploadSuccess() {
        $("#uploadResult").attr("style", "");
        $("#uploadResult").text("上传的通讯录已经处理完成。");
    }
    // 当上传通讯录出现错误时调用
    function uploadFailed(msg) {
        var a = UrlDecode(msg);
        $("#uploadResult").attr("style", "background:#F55");
        $("#uploadResult").html(a);
    }

    // 将utf-8形式编码的内容进行解码
    function UrlDecode(zipStr) {
        var uzipStr = "";
        for (var i = 0; i < zipStr.length; i++) {
            var chr = zipStr.charAt(i);
            if (chr == "+") {
                uzipStr += " ";
            } else if (chr == "%") {
                var asc = zipStr.substring(i + 1, i + 3);
                if (parseInt("0x" + asc) > 0x7f) {
                    uzipStr += decodeURI("%" + asc.toString() + zipStr.substring(i + 3, i + 9).toString());
                    i += 8;
                } else {
                    uzipStr += AsciiToString(parseInt("0x" + asc));
                    i += 2;
                }
            } else {
                uzipStr += chr;
            }
        }
        return uzipStr;
    }

    function StringToAscii(str) {
        return str.charCodeAt(0).toString(16);
    }
    function AsciiToString(asccode) {
        return String.fromCharCode(asccode);
    }

    // 显示或隐藏上传通讯录div
    function showOrHideUpload() {
        $("#uploadDiv").toggle(1000);
    }


    // 使用联系人json数据组合成联系人表格内容
    function initTbodyOfContacts(contacts) {
        var htmlStr = '';
        for (var i = 0; i < contacts.length; i++) {
            htmlStr = htmlStr + '<tr><td>' + '<input type="checkbox" value="' + contacts[i].id + '"> ' + contacts[i].id
                    + '</td><td>' + contacts[i].groupname
                    + '</td><td>' + contacts[i].name
                    + '</td><td>' + contacts[i].title
                    + '</td><td>' + contacts[i].phone
                    + '</td><td>' + contacts[i].description
                    + '</td><td>' + '<button type="button" class="btn btn-primary btn-sm" onclick="initEditContact(\'' + contacts[i].id
                    + '\',\'' + contacts[i].groupname + '\',\'' + contacts[i].name + '\',\'' + contacts[i].title + '\',\'' + contacts[i].phone + '\',\'' + contacts[i].description + '\')">编辑</button>'
                    + '</td></tr>';
        }
        $("#contactBody").html(htmlStr);
    }

    //全选或者反选
    function changeSelectAll() {
        console.log("changeSelectAll....." + $("#selectAll").is(':checked'));
        if ($("#selectAll").is(':checked')) {
            $("table input[type=checkbox]").attr("checked", true);
        } else {
            $("table input[type=checkbox]").attr("checked", false);
        }
    }

    function initEditContact(id, groupname, name, title, phone, description) {
        updateContactId = id;
        // 显示编辑部分
        $("#editContactDiv").show(1000);
        // 初始化数据
        // 分组是select editContactGroup
//        $("#editContactGroup option[value='" + groupname + "']").attr("selected", true);
        $("#editContactGroup").val(groupname);
        $("#editContactName").val(name);
        $("#editContactTitle").val(title);
        $("#editContactPhone").val(phone);
        $("#editContactDescription").val(description);
    }

    function cancleEditContact() {
        $("#editContactDiv").hide(1000);
    }

    // 确实需要更新编辑联系人信息
    function doEditContact(){
        var id = updateContactId;
        var groupname = $("#editContactGroup").val();
        var name = $("#editContactName").val();
        var title = $("#editContactTitle").val();
        var phone = $("#editContactPhone").val();
        var description = $("#editContactDescription").val();
        // 判断不为空
        // 判断手机号格式
        $.ajax({
            type: 'POST',
            url: bp + 'Smserver/contacts/update',
            data : {id:id,groupname:groupname,name:name,title:title,phone:phone,description:description},
            dataType : "json",
            success: function (data) {
                var contacts = data['contacts'];
                initTbodyOfContacts(contacts);

            }
        });

    }

    // 删除联系人
    function deleteContact() {
        var id = updateContactId;
    }


</script>
</body>
</html>