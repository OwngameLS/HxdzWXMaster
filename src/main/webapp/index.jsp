<%@page language="java" contentType="text/html; charset=utf-8"
        pageEncoding="utf-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <title>Bootstrap 模板</title>
    <!-- 引入 Bootstrap -->
    <link href="resources/bootstrap-3.3.7-dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body style="padding-left: 20px">
<div class="page-header">
    <h1>Smserver
        <small>如此简单</small>
    </h1>
    <ul class="nav nav-pills">
        <li class="active"><a href="#">首 页</a></li>
        <li><a href="#">定时任务</a></li>
        <li><a href="#">通讯录</a></li>
        <li><a href="#">关 于</a></li>
    </ul>
</div>

<div class="jumbotron">
    <table class="table">
        <thead>
        <tr>
            <th>Student-ID</th>
            <th>First Name</th>
            <th>Last Name</th>
            <th>Grade</th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td>001</td>
            <td>Rammohan</td>
            <td>Reddy</td>
            <td>A+</td>
        </tr>
        <tr>
            <td>002</td>
            <td>Smita</td>
            <td>Pallod</td>
            <td>A</td>
        </tr>
        <tr>
            <td>003</td>
            <td>Rabindranath</td>
            <td>Sen</td>
            <td>A+</td>
        </tr>
        </tbody>
    </table>
</div>

<div>
    文件上传
    <form role="form" action="Smserver/doUpload" method="post" enctype="multipart/form-data" target="hidden_frame">
        <div class="form-group">
            <label for="inputfile">选择文件</label>
            <input type="file" id="inputfile" name="file" accept=".xls">
        </div>
        <button id="submit" type="submit" class="btn btn-default">提交</button>
    </form>

    <p style="color: #c9302c">
        如果你不知道上传什么文件，请下载模板文件进行修改后再上传。
        <a href="Smserver/download">点击下载《通讯录模板文件》</a>
    </p>
    <br>
    <div id="uploadResult"></div>
    <iframe name='hidden_frame' id="hidden_frame" style='display:none'></iframe>
</div>
<%--参考：http://www.codingyun.com/article/50.html--%>
<!-- jQuery (Bootstrap 的 JavaScript 插件需要引入 jQuery) -->
<script src="resources/bootstrap-3.3.7-dist/js/jquery-3.1.0.min.js"></script>
<!-- 包括所有已编译的插件 -->
<script src="resources/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>
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


</script>
</body>
</html>
