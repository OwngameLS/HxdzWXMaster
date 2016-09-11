<%@page language="java" contentType="text/html; charset=utf-8"
        pageEncoding="utf-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
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
            注意！！！
            <p class="text-warning">
                1.每次上传的文件将会覆盖原有数据，请仔细操作！<br>
                2.如果你不知道上传什么文件，请下载模板文件进行修改后再上传。
                <a href="Smserver/download">点击下载《通讯录模板文件》</a>
            </p>
        </dd>
    </dl>
    <form role="form" action="<%=basePath%>Smserver/doUpload" method="post" enctype="multipart/form-data" target="hidden_frame">
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
<div><%--通讯录操作部分--%>
    <table class="table table-hover">

    </table>


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


</script>
</body>
</html>