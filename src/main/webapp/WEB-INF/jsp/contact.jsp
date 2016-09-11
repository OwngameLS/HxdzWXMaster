<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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

<div class="bg-success" style="padding: 20px"><%--文件上传部分--%>
    <dl>
        <dt>上传通讯录</dt>
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
        <table class="table table-striped text-center">
            <thead>
            <tr>
                <th>
                    点击下列分组查看组员
                </th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${groups}" var="group">
                <tr>
                    <td>
                        <button type="button" class="btn btn-primary" onclick="getContactsByGroups('<%=basePath%>','${group}')">${group}</button>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
    <div id="persons" style="width:70%;float:left;">
        <table class="table table-hover table-bordered text-center">
            <thead>
                <tr>
                    <th>id</th>
                    <th>所在分组</th>
                    <th>姓名</th>
                    <th>职务</th>
                    <th>手机号</th>
                    <th>备注</th>
                </tr>
            </thead>
            <tbody id="contactBody">
            <c:forEach items="${contacts}" var="contact">
                <tr>
                    <td>
                            ${contact.id}
                    </td>
                    <td>
                            ${contact.groupname}
                    </td>
                    <td>
                            ${contact.name}
                    </td>
                    <td>
                            ${contact.title}
                    </td>
                    <td>
                            ${contact.phone}
                    </td>
                    <td>
                            ${contact.description}
                    </td>
                </tr>
            </c:forEach>
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
    function uploadSuccess() {
        $("#uploadResult").attr("style", "");
        $("#uploadResult").text("上传的通讯录已经处理完成。");
    }
    function uploadFailed(msg) {
        var a = UrlDecode(msg);
        $("#uploadResult").attr("style", "background:#F55");
        $("#uploadResult").html(a);
    }

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


    function getContactsByGroups(bathPath, groupname) {
        $.ajax({
            type: 'GET',
            url: bathPath+'Smserver/contacts/'+groupname ,
            success: function (data) {
                var contacts = data['contacts'];
                console.log(contacts);
                console.log(contacts.length);
                var htmlStr = '';
                for(var i=0; i<contacts.length;i++){
                htmlStr = htmlStr +  '<tr><td>'+contacts[i].id
                        +'</td><td>'+contacts[i].groupname
                        +'</td><td>'+contacts[i].name
                        +'</td><td>'+contacts[i].title
                        +'</td><td>'+contacts[i].phone
                        +'</td><td>'+contacts[i].description
                        +'</td></tr>';
                }
                console.log(htmlStr);
                $("#contactBody").html(htmlStr);
            }
        });
    }

</script>
</body>
</html>