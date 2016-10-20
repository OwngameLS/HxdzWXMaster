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
    <title>功能编辑</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <style type="text/css">
        /*设置单元格内容超长，用省略号代替的效果，前提是每一列的宽度都要指定*/
        table {
            table-layout: fixed;
        }

        td {
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
        }
    </style>
    <!-- 引入 Bootstrap -->
    <link href="../../resources/bootstrap-3.3.7-dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="../../resources/bootstrap-3.3.7-dist/js/uiscript.js"></script>
</head>
<body>
<h3>定时任务操作</h3>

<div class="danger" style="padding: 5px;"><%--通讯录操作部分--%>
    <dl>
        <dt>编辑“功能”</dt>
        <dd>
            <p class="text-warning">
                在下面进行您自定义功能的编辑
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

    <div id="functionEditDiv" style="" class="well"><%--编辑定时任务的表格--%>
        <div class="row">
            <div class="col-md-4 text-center"></div>
            <div class="col-md-4 text-center">
                <h4>功能属性编辑</h4>
            </div>
            <div class="col-md-2 text-center"></div>
            <div class="col-md-2 text-center">
                <button type="button" class="btn btn-success btn-sm" onclick="saveFunction()">保 存</button>
                <button type="button" class="btn btn-warning btn-sm" onclick="initEditTimerTask(-1)">取 消</button>
            </div>
        </div>
        ---功能描述性设置-------------------------
        <div class="row bg-success">
            <div class="col-md-3 text-center">名称</div>
            <div class="col-md-4 text-center">关键字</div>
            <div class="col-md-5 text-center">描述</div>
        </div>
        <div class="row">
            <div class="col-md-3 text-center">
                <input class="form-control" id="editName" placeholder="给功能起一个名字..."></div>
            <div class="col-md-4 text-center">
                <input class="form-control" id="editKeywords" placeholder="关键字，区别于其他功能的关键字..." onchange=""></div>
            <div class="col-md-5 text-center">
                <input class="form-control" id="editDescription" placeholder="描述这个功能，它将作为返回信息的起始部分..."></div>
        </div>
        ---数据库连接属性设置---------------------
        <div class="row bg-success">
            <div class="col-md-4 text-center">IP地址/主机名</div>
            <div class="col-md-2 text-center">端口号</div>
            <div class="col-md-3 text-center">数据库类型</div>
            <div class="col-md-3 text-center">数据库名称</div>
        </div>
        <div class="row">
            <div class="col-md-4 text-center">
                <input class="form-control" id="editIP" placeholder="欲读取数据库所在机器的IP地址...">
            </div>
            <div class="col-md-2 text-center">
                <input class="form-control" id="editPort" placeholder="其数据库开放的端口...">
            </div>
            <div class="col-md-3 text-center">
                <select id="editDbtype">
                    <option value="MySQL">MySQL</option>
                    <option value="Microsoft SQL Server">Microsoft SQL Server</option>
                    <option value="Oracle">Oracle</option>
                    <option value="PostgreSQL">PostgreSQL</option>
                    <option value="DB2">DB2</option>
                    <option value="Informix">Informix</option>
                    <option value="JDBC-ODBC">JDBC-ODBC</option>
                </select>
            </div>
            <div class="col-md-3 text-center">
                <input class="form-control" id="editDbname" placeholder="数据库名...">
            </div>
        </div>
        <div class="row bg-success">
            <div class="col-md-4 text-center">用户名</div>
            <div class="col-md-2 text-center">密码</div>
            <div class="col-md-3 text-center">表名</div>
            <div class="col-md-3 text-center" id="connectResult"></div>
        </div>
        <div class="row">
            <div class="col-md-4 text-center">
                <input class="form-control" id="editUsername" placeholder="数据库用户名...">
            </div>
            <div class="col-md-2 text-center">
                <input class="form-control" id="editPassword" placeholder="数据库密码...">
            </div>
            <div class="col-md-3 text-center">
                <input class="form-control" id="editTablename" placeholder="读取的表名...">
            </div>
            <div class="col-md-3 text-center">
                <button type="button" class="btn btn-success btn-sm" onclick="testConnect(true)">连接测试</button>
            </div>
        </div>

        <div id="colsDIV">
            <%--<div id="colsDIV" style="display:none">--%>
            ---对数据库字段的读取规则-------------------------
            <div class="row bg-success">
                <input type="radio" name="whichType" value="sql">使用SQL语句（如果你够专业，您可以在这里编写SQL语句）
            </div>
            <div class="row">
                <div class="col-md-3 text-center">
                    Sql语句(只支持查询语句)：
                </div>
                <div class="col-md-7 text-center">
                    <input id="editSQL" class="form-control">
                </div>
                <div class="col-md-2 text-center">
                    <button type="button" class="btn btn-success btn-sm" onclick="testSQL(false)">测试</button>
                </div>
            </div>
            <div id="sqlresultDiv" style="display: none">
                <div class="row bg-warning">
                    <div class="col-md-1 text-center"></div>
                    <div class="col-md-10 text-center">
                        根据你的sql语句，得知你要查询以下几个字段的值，为了让你的查询结果更容易理解，
                        请将字段进行命名，并按照你的需求给出字段的顺序，便于整理结果。<br>
                        <em>
                            例如，你查询了A,B,C三个字段，分别命名为 AAA,BBB,CCC，顺序分别为2,1,3，则查询结果为：<br>
                            【功能XXX的查询结果如下：BBB的值为bbb,AAA的值为aaa,CCC的值为ccc。】
                        </em>
                    </div>
                </div>
                <div id="sqlFields">

                </div>

            </div>
            <br>
            <div class="row bg-success">
                <input type="radio" name="whichType" value="rule" checked>使用规则（读取字段的设置，注意：所有的规则都满足才能查询到结果）
            </div>
            <div class="row bg-success">
                <div class="col-md-2 text-center">数据库字段名</div>
                <div class="col-md-3 text-center">自定义名称</div>
                <div class="col-md-3 text-center">是否参与排序</div>
                <div class="col-md-2 text-center">是否读取该字段的值</div>
                <div class="col-md-2 text-center">是否使用规则</div>
            </div>
            <div class="row" id="colsTR"><%--在这里列出所有字段--%>
            </div>
        </div>
    </div>

    <button type="button" class="btn btn-warning btn-sm" onclick="initEditTimerTask(-1)">新建任务</button>
    <div id="tasks" style="width:100%;float:left;overflow:scroll; height:400px;" class="well">
        <table class="table table-hover table-bordered text-center">
            <thead>
            <tr class="info">
                <th width="12%" class="text-center">序号(<input type="checkbox" id="selectAllTimerTasks"
                                                              onclick="changeSelectAllTimerTasks()">全选)
                </th>
                <th width="12%" class="text-center">功能</th>
                <th width="12%" class="text-center">描述</th>
                <th width="14%" class="text-center">触发规则</th>
                <th width="27%" class="text-center">接收者们</th>
                <th width="15%" class="text-center">状态</th>
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
    var colLength = 0;
    // 数据库链接相关变量
    var id = -1;// 新建
    var name;
    var description;
    var keywords;// 关键字，当用户自主查询时，通过关键字匹配
    var ip;
    var port;
    var dbtype;
    var dbname;
    var username;
    var password;
    var tablename;
    var usetype;// 由于加入了SQL语句查询，需要确定使用哪个 sql或rules
    var readfields;// 要读取的字段 用户查询所需的字段a,aName#b,bName
    var sortfields;// 排序字段，根据这个字段才能查询到最新的数据 A ASC,B DESC 默认为降序排列
    var fieldrules;// 规则字段，字段名，值，规则 根据规则来判断 a,aName,-1,NN#b,bName,5,BB#c,cName,200,LL#d,dName,abcd,EQ#e,eName,bcde,NE#f,fName,xxxx,RG@12BT34
    var isreturn;// 读取结果是否返回的规则（由于需要涉及到预警功能，所以需要定义规则）
    // 由于功能是定时执行，因此不一定是每次都读取到数据就需要告知
    // anyway: 不论如何都返回
    // oncase: 监听几个字段，当其中一个字段达到报警要求时就需要告知
    var sqlstmt;//sql语句
    var sqlfields;// sql查询的字段属性，按照顺序来a,aName#b,bName
    var isConnectSuccess = false;// 设置的数据库连接是否成功的实际情况
    var formerSqlFieldsHTML = "";//前一次编辑的Sql字段结果

    // 文档被加载完成时
    $(document).ready(function () {
//        queryTimerTasks();
    });

    function queryTimerTasks() {
        $.ajax({
            url: bp + 'Smserver/timertask/getall',
            type: 'GET',
            success: function (data) {
                // 初始化timertasks相关的控件
                initTbodyOfTasks(data['timerTasks']);// 选择控件
            }
        });
    }

    // 保存function
    function saveFunction() {
        hideEditFail();
        // 验证所填写的字段非空且符合要求
        // 验证功能描述性设置
        var result1 = false, result2 = false, result3 = false;
        console.log("check testFunctionDescPart...");
        var result1 = testFunctionDescPart();

        if(result1){
            console.log("check testConnect...");
            result2 = testConnect(false);
        }
        if(isConnectSuccess){
            console.log("check settings...");
            // 检查读取规则类型
            usetype = $('input[name="whichType"]:checked ').val();
            if(usetype == 'sql'){
                result3 = testSQL(true);
            }else{
                result3 = testRules();
            }
        }
        if(result3){
            // 提交
            doAjaxHandleFunction('update');
        }
    }


    // 处理TimerTask操作提交给服务器部分
    function doAjaxHandleFunction(action) {
        console.log("doAjaxHandleFunction...");
        var jsonData = "{\"id\":\"" + id
                + "\",\"name\":\"" + name
                + "\",\"description\":\"" + description
                + "\",\"keywords\":\"" + keywords
                + "\",\"ip\":\"" + ip
                + "\",\"port\":\"" + port
                + "\",\"dbtype\":\"" + dbtype
                + "\",\"dbname\":\"" + dbname
                + "\",\"username\":\"" + username
                + "\",\"password\":\"" + password
                + "\",\"tablename\":\"" + tablename
                + "\",\"usetype\":\"" + usetype
                + "\",\"readfields\":\"" + readfields
                + "\",\"sortfields\":\"" + sortfields
                + "\",\"fieldrules\":\"" + fieldrules
                + "\",\"isreturn\":\"" + isreturn
                + "\",\"sqlstmt\":\"" + sqlstmt
                + "\",\"sqlfields\":\"" + sqlfields
                + "\"}";
        $.ajax({
            type: 'POST',
            url: bp + 'Smserver/function/' + action,
            data: jsonData,
            dataType: "json",
            contentType: "application/json",
            success: function (data) {
                console.log(data['updateResult']);
                // 先将编辑框隐藏
                $("#timertaskEditDiv").hide(2000);
                showEditDone();
                hideEditFail();
            }
        });
    }


    // 测试SQL语句
    function testSQL(isSaveSQL) {
        // 先检查sql语句，排除非法操作
        sql = $("#editSQL").val();
        if (sql == '' || sql == null) {
            showEditFail("您还没有输入SQL语句呢！", $("#editSQL"));
            return false;
        }
        if (sql.indexOf("remove") >= 0 || sql.indexOf("delete") >= 0 || sql.indexOf("update") >= 0) {
            showEditFail("您输入的SQL语句不是查询语句，请检查！<br><b>注意:</b>只能是查询语句！", $("#editSQL"));
            return false;
        }
        // 再检查数据库连通性
        if(isSaveSQL == false){// 不是保存阶段，就要检查连通性
            testConnect(false);
        }
        if (isConnectSuccess == false) {
            // 数据库不可连通，建议放弃操作
            if (confirm("数据库连接没有成功，确认继续操作？")) {
                // 保存sql语句供下次编辑就好了
            } else {
                return false;
            }
        }
        var jsonData = "{\"ip\":\"" + ip
                + "\",\"port\":\"" + port
                + "\",\"dbtype\":\"" + dbtype
                + "\",\"dbname\":\"" + dbname
                + "\",\"username\":\"" + username
                + "\",\"password\":\"" + password
                + "\",\"tablename\":\"" + tablename
                + "\",\"sql\":\"" + sql + "\"}";

        //暂时没错了，交给后台检查吧
        $.ajax({
            url: bp + 'Smserver/functions/sql/',
            type: 'POST',
            async: false,
            data: jsonData,
            dataType: "json",
            contentType: "application/json",
            success: function (data) {
                var result = data['sqlResult'];
                if (result.isSuccess < 0) {// 有错误信息
                    // 会返回类似的关键字，将他们罗列出来
                    var errorMsg = "您输入的sql语句存在错误：<br>" + result.fields[0];
                    showEditFail(errorMsg, $("#editSQL"));
                    return false;
                } else {// 返回的是列表信息
                    if(isSaveSQL){
                        // 返回的列表信息与设置的是否一致？
                        console.log("saving sql....");
                        return saveSQL(result.fields);
                    }else{
                        initTbodyOfSQL(result.fields);
                    }
                }
            }
        });
    }




    // 验证功能描述性设置
    function testFunctionDescPart() {
        // 功能名称
        name = $("#editName").val();
        if (name == '' || name == null) {
            // 错误信息
            showEditFail("必须输入功能名称！", $("#editName"));
            return false;
        }
        // 关键字
        keywords = $("#editKeywords").val();
        if (keywords == '' || keywords == null) {
            // 错误信息
            showEditFail("必须输入关键字！", $("#editKeywords"));
            return false;
        }
        // keywords不为空，还要检测它的唯一性
        $.ajax({
            url: bp + 'Smserver/functions/keywords/',
            type: 'POST',
            async: false,
            data: "{\"id\":\"" + id + "\",\"keywords\":\"" + keywords + "\"}",
            dataType: "json",
            contentType: "application/json",
            success: function (data) {
                var result = data['keywordResult'];
                if (result.isSuccess < 0) {
                    // 会返回类似的关键字，将他们罗列出来
                    var errorMsg = "关键字重复！<br>";
                    for (var i = 0; i < result.simlarKeys.length; i++) {
                        errorMsg = errorMsg + result.simlarKeys[i];
                        if ((i + 1) < result.simlarKeys.length) {
                            errorMsg = errorMsg + "<br>";
                        }
                    }
                    showEditFail(errorMsg, $("#editKeywords"));
                    return false;
                }
            }
        });
        // 检查描述
        description = $("#editDescription").val();
        if (description == '' || description == null) {
            // 错误信息
            showEditFail("必须输入描述！", $("#editDescription"));
            return false;
        }
        return true;
    }

    // 检查数据库连通性 isShowCols 是否需要将查询得到的字段展示出来 true:展示，fasle:不展示
    function testConnect(isShowCols) {
        ip = $("#editIP").val();
        if (ip == '' || ip == null) {
            // 错误信息
            showEditFail("必须输入IP地址！", $("#editIP"));
            return false;
        }
        var ignoreIp = false;
        if (checkIpisHost(ip)) {
            // 发现填写的可能是主机名
            if (confirm("发现你在Ip地址填写的不是合理的Ip,是否继续？")) {
                ignoreIp = true;
            } else {
                myAnimate($("#editIP"), 8, $("#editIP").attr("style"));
                return false;
            }
        }
        if (ignoreIp == false) {// 需要Ip检查
            if (checkIP(ip) == false) {
                showEditFail("必须输入IP地址！", $("#editIP"));
                return false;
            }
        }
        port = $("#editPort").val();
        if (port == '' || port == null) {
            // 错误信息
            showEditFail("必须输入端口号！", $("#editPort"));
            return false;
        }
        if (isInteger(port) == false) {
            showEditFail("端口号必须输入正整数！", $("#editPort"));
            return false;
        }

        dbtype = $("#editDbtype  option:selected").val();

        dbname = $("#editDbname").val();
        if (dbname == '' || dbname == null) {
            // 错误信息
            showEditFail("必须输入数据库名！", $("#editDbname"));
            return false;
        }
        username = $("#editUsername").val();
        if (username == '' || username == null) {
            // 错误信息
            showEditFail("必须输入用户名！", $("#editUsername"));
            return false;
        }
        password = $("#editPassword").val();
        if (password == '' || password == null) {
            // 错误信息
            showEditFail("必须输入密码！", $("#editPassword"));
            return false;
        }
        tablename = $("#editTablename").val();
        if (tablename == '' || tablename == null) {
            // 错误信息
            showEditFail("必须输入表名！", $("#editTablename"));
            return false;
        }
        var jsonData = "{\"ip\":\"" + ip
                + "\",\"port\":\"" + port
                + "\",\"dbtype\":\"" + dbtype
                + "\",\"dbname\":\"" + dbname
                + "\",\"username\":\"" + username
                + "\",\"password\":\"" + password
                + "\",\"tablename\":\"" + tablename + "\"}";
//        console.log("jsonData:" + jsonData);
        // 访问服务器
        $.ajax({
            url: bp + 'Smserver/functions/testconnect',
            type: 'POST',
            async: false,
            data: jsonData,
            dataType: "json",
            contentType: "application/json",
            success: function (data) {
                var colsNames = data['colNames'];
                if (colsNames != null) {
                    isConnectSuccess = true;
                    var htmlStr = '<p style="color: #0000FF">连接成功!</p>';
                    $("#connectResult").html(htmlStr);
                    if (isShowCols == false) {// 只需告知结果
                        return true;
                    }
                    // 初始化colNames相关的控件
                    initTbodyOfCols(data['colNames']);// 选择控件
                } else {
                    isConnectSuccess = false;
                    var htmlStr = '<p style="color: #c9302c">连接失败!</p>';
                    $("#connectResult").html(htmlStr);
                    if (isShowCols == false) {// 只需告知结果
                        if (confirm("数据库连接没有成功，确认继续操作？")) {
                            return true;// 当保存时数据库出现问题，设置没有问题时
                        } else {
                            return false;
                        }
                    }
                    $("#colsDIV").hide(2000);
                }
                if (isShowCols == true) {
                    myAnimate($("#connectResult"), 8, $("#connectResult").attr("style"));
                }
            },
            error: function () {
                isConnectSuccess = false;
            }
        });
    }


    // 初始化表字段UI控件 用于编辑字段规则
    function initTbodyOfCols(colNames) {
        colLength = colNames.length;
        var htmlStr = '';
        for (var i = 0; i < colNames.length; i++) {
            if (i % 2 == 0) {
                htmlStr = htmlStr
                        + '<div class="row bg-warning">';
            } else {
                htmlStr = htmlStr
                        + '<div class="row">';
            }

            htmlStr = htmlStr
                    + '<div class="col-md-2 text-center"><b id="colName' + i + '">' + colNames[i] + '</b></div>'
                    + '<div class="col-md-3 text-center"><input class="form-control" id="selfColName' + i + '" placeholder="名称"></div>'
                    + '<div class="col-md-3 text-center">'
                    + '<input type="checkbox" id="isSort' + i + '">是'
                    + '<input type="radio" name="sort' + i + '" value="desc" checked>降序 <input type="radio" name="sort' + i + '" value="asc">升序</div>'
                    + '<div class="col-md-2 text-center"><input type="checkbox" id="isread' + i + '" value="' + colNames[i] + '">读取</div>'
                    + '<div class="col-md-2 text-center"><input type="checkbox" id="isusedRule' + i + '">使用规则</div>'
                    + '</div>';
            if (i % 2 == 0) {
                htmlStr = htmlStr
                        + '<div class="row bg-warning" style="border-bottom:1px solid #222222;">';
            } else {
                htmlStr = htmlStr
                        + '<div class="row" style="border-bottom:1px solid #245580;">';
            }
            htmlStr = htmlStr
                    + '<div class="col-md-2 text-center">设置规则</div>'
                    + '<div class="col-md-2 text-center"><input type="radio" name="rule' + i + '" value="EQ" checked>等于 <input type="radio" name="rule' + i + '" value="NE">不等于<br>'
                    + '参照值<input class="form-control" id="compareValue' + i + '" placeholder="该字段的合理值"></div>'
                    + '<div class="col-md-1 text-center"><input type="radio" name="rule' + i + '" value="BB">大于<input class="form-control" id="above' + i + '" placeholder="大于"></div>'
                    + '<div class="col-md-1 text-center"><input type="radio" name="rule' + i + '" value="LL">小于<input class="form-control" id="below' + i + '" placeholder="小于"></div>'
                    + '<div class="col-md-6 text-center" style="border-style: groove">'
                    + '<input type="radio" name="rule' + i + '" value="RG">范围<br>'
                    + '<div class="col-md-4 text-center"><input type="radio" name="range' + i + '" value="BT" checked>在内 <input type="radio" name="range' + i + '" value="OUT">在外 </div>'
                    + '<div class="col-md-4 text-center">下限值:<input class="form-control" id="rangedown' + i + '"></div>'
                    + '<div class="col-md-4 text-center">上限值:<input class="form-control" id="rangeup' + i + '"></div>'
                    + '</div>'
                    + '</div><br>';
        }
        $("#colsTR").html(htmlStr);
        $("#colsDIV").show(2000);
    }

    // 当使用sql语句规则时的相关设置
    function initTbodyOfSQL(fields) {
//        formerSqlFieldsHTML = $("#sqlFields").html();
        $("#sqlFields").html("");
        var htmlStr = '<div class="row"><div class="col-md-3 text-center">字段名</div><div class="col-md-3 text-center">名称</div><div class="col-md-3 text-center">排序序号</div></div>';
        for (var i = 0; i < fields.length; i++) {
            htmlStr = htmlStr + '<div class="row">';
            htmlStr = htmlStr + '<div class="col-md-3 text-center"><b id="sqlFieldCol' + i + '" >' + fields[i] + '</b></div>';
            htmlStr = htmlStr + '<div class="col-md-3 text-center"><input class="form-control" id="sqlFieldName' + i + '" placeholder="名称"></div>';
            htmlStr = htmlStr + '<div class="col-md-3 text-center"><input class="form-control" id="sqlFieldSort' + i + '" value="' + (i + 1) + '"></div>';
            htmlStr = htmlStr + '</div>';
        }
        $("#sqlFields").html(htmlStr);
        $("#sqlresultDiv").show(2000);
    }


    // 检查字段规则
    function testRules() {
        // 先获得被选中的要求添加规则的字段
        var sortArray = new Array();// 排序字段
        var readArray = new Array();// 读取字段
        var rulesArray = new Array();// 读取字段的规则
        var queryfields = new Array();// 查询得到的所有字段
        var fieldsHtml = $("[id^='colName']");
        for (var i = 0; i < fieldsHtml.length; i++) {
            queryfields.push($(fieldsHtml[i]).text());
        }
        for (var i = 0; i < queryfields.length; i++) {
            // 是否参与排序
            var isSort = $("#isSort" + i).prop("checked");
            if (isSort) {
                var order = $('input[name="sort' + i + '"]:checked ').val();
                sortArray.push(queryfields[i] + " " + order);// 添加到排序数组里
            }
            // 是否读取字段
            var isRead = $("#isread" + i).prop("checked");
            var selfColName = $("#selfColName" + i).val();
            if (isRead) {
                // 检查其自定义名称是否填写
                if (selfColName == null || selfColName == '') {
                    showEditFail("你选择读取字段<b>" + queryfields[i] + "</b>,所以必须填写它的自定义名称。", $("#selfColName" + i));
                    return false;
                }
                // 添加进读取字段数组中
                readArray.push(queryfields[i] + "," + selfColName);
            }
            // 检查是否使用规则
            var isusedRule = $("#isusedRule" + i).prop("checked");
            if (isusedRule){// 使用规则 进一步判断
                // 为方便告知规则情况，需要了解字段自定义名称
                if (selfColName == null || selfColName == '') {
                    showEditFail("你选择使用规则，则字段<b>" + queryfields[i] + "</b>必须填写它的自定义名称。", $("#selfColName" + i));
                    return false;
                }
                var ruleType = $('input[name="rule' + i + '"]:checked ').val();
                if (ruleType == 'EQ' || ruleType == 'NE') {// 等于或者不等于的规则
                    // 检查参照值为合法输入 非空即可
                    var compareValue = $("#compareValue" + i).val();
                    if (compareValue == null || compareValue == '') {
                        showEditFail("字段<b>" + queryfields[i] + "</b>使用规则，其参照值不能为空！", $("#compareValue" + i));
                        return false;
                    } else {
                        rulesArray.push(queryfields[i] + "," + selfColName + "," + compareValue + "," + ruleType);
                    }
                } else if (ruleType == 'BB') {// 大于
                    var aboveValue = $("#above" + i).val();
                    if (aboveValue == null || aboveValue == '') {
                        showEditFail("字段<b>" + queryfields[i] + "</b>使用‘大于’规则，其参照值不能为空！", $("#above" + i));
                        return false;
                    } else {
                        rulesArray.push(queryfields[i] + "," + selfColName + "," + aboveValue + "," + ruleType);
                    }
                } else if (ruleType == 'LL') {// 小于
                    var belowValue = $("#below" + i).val();
                    if (belowValue == null || belowValue == '') {
                        showEditFail("字段<b>" + queryfields[i] + "</b>使用‘小于’规则，其参照值不能为空！", $("#below" + i));
                        return false;
                    } else {
                        rulesArray.push(queryfields[i] + "," + selfColName + "," + belowValue + "," + ruleType);
                    }
                } else if (ruleType == 'RG') {// 范围
                    // f,fName,cdef,RG@12BT34
                    // 范围区域（在内，在外）
                    var rangeType = $('input[name="range' + i + '"]:checked ').val();
                    // 两个标值
                    // 检查不能为空
                    var rangedown = $("#rangedown" + i).val();
                    if (rangedown == null || rangedown == '') {
                        showEditFail("字段<b>" + queryfields[i] + "</b>使用‘范围’规则，其下限值不能为空！", $("#rangedown" + i));
                        return false;
                    }
                    var rangeup = $("#rangeup" + i).val();
                    if (rangeup == null || rangeup == '') {
                        showEditFail("字段<b>" + queryfields[i] + "</b>使用‘范围’规则，其上限值不能为空！", $("#rangeup" + i));
                        return false;
                    }
                    // 检查大小、不要颠倒
                    if (rangedown == rangeup) {// 大小相同
                        showEditFail("字段<b>" + queryfields[i] + "</b>使用‘范围’规则，其上限值和下限值不能相等！", $("#rangeup" + i));
                        return false;
                    }
                    if (rangedown > rangeup) {// 自动调整颠倒
                        var t = rangedown;
                        rangedown = rangeup;
                        rangeup = t;
                    }
                    rulesArray.push(queryfields[i] + "," + selfColName + ",xxx," + ruleType + "@" + rangedown + rangeType + rangeup);
                }
            }
        }
        // 循环完了 整理成规则字符串
        readfields = "";
        for (var i = 0; i < readArray.length; i++) {
            readfields = readfields + readArray[i];
            if ((i + 1) < readArray.length) {
                readfields = readfields + "#";
            }
        }
        sortfields = "";
        for (var i = 0; i < sortArray.length; i++) {
            sortfields = sortfields + sortArray[i];
            if ((i + 1) < sortArray.length) {
                sortfields = sortfields + ",";
            }
        }
        fieldrules = "";
        for (var i = 0; i < rulesArray.length; i++) {
            fieldrules = fieldrules + rulesArray[i];
            if ((i + 1) < rulesArray.length) {
                fieldrules = fieldrules + "#";
            }
        }
        if(fieldrules == ""){// 没有定义规则，就是查询到了就返回
            isreturn = "anyway";
        }else{
            isreturn = "oncase";
        }
//        console.log("readfields:" + readfields+";sortfields:" + sortfields + "; fieldrules:" + fieldrules);
    }


    // 保存SQL规则
    function saveSQL(queryFileds) {
        // 获得字段集合
        var fields = new Array();
        var fieldsHtml = $("[id*='sqlFieldCol']");
        for (var i = 0; i < fieldsHtml.length; i++) {
            fields.push($(fieldsHtml[i]).text());
        }
        var errorMsg = '';
        // 与查询得到的字段进行对比
        if(queryFileds.length != fields.length){
            // 新旧长度不统一
            // 重新初始化并告知
            initTbodyOfSQL(queryFileds);
            if(fields.length == 0){
                errorMsg = "您必须对您的查询字段做相应的设置。";
            }else{
                errorMsg = "您查询的字段与您设置的字段不一样，请重新设置。";
            }
            showEditFail(errorMsg, $("#editSQL"));
            return false;
        }
        var hasNotFound = false;
        for(var i=0;i<queryFileds.length;i++){
            if(fields.indexOf(queryFileds[i]) == -1){
                hasNotFound  = true;
            }
        }

        if(hasNotFound) {
            // 重新初始化并告知
            initTbodyOfSQL(queryFileds);
            errorMsg = "您查询的字段与您设置的字段不一样，请重新设置。";
            showEditFail(errorMsg, $("#editSQL"));
            return false;
        }

        // 检查非空
        var nameHtml = $("[id*='sqlFieldName']");
        var names = new Array();
        var hasError = false;
        var errorInfo = '';
        for (var i = 0; i < nameHtml.length; i++) {
            var value = $(nameHtml[i]).val();
            if (value == null || value == '') {
                hasError = true;
                errorInfo = errorInfo + "字段(" + fields[i] + ")必须设置名称哦！<br>";
                myAnimate($(nameHtml[i]), 8, $(nameHtml[i]).attr("style"));
            } else {
                names.push(value);
            }
        }
        if (hasError) {
            showEditFail(errorInfo, null);
            return false;
        }
        // 检查不重复？用户是傻逼吧

        // 顺序
        var sortHtml = $("[id*='sqlFieldSort']");
        var sortsValue = new Array();
        // 检查顺序非空
        for (var i = 0; i < sortHtml.length; i++) {
            var sort = $(sortHtml[i]).val();
            if (sort == null || sort == '') {
                hasError = true;
                errorInfo = errorInfo + "字段(" + fields[i] + ")必须设置排列顺序哦！<br>";
                myAnimate($(sortHtml[i]), 8, $(sortHtml[i]).attr("style"));
            } else if (isInteger(sort) == false) {
                hasError = true;
                errorInfo = errorInfo + "字段(" + fields[i] + ")的排列顺序必须为正整数哦！<br>";
                myAnimate($(sortHtml[i]), 8, $(sortHtml[i]).attr("style"));
            } else {
                sortsValue.push(sort);
            }
        }
        if (hasError) {
            showEditFail(errorInfo, null);
            return false;
        }
        // 检查顺寻顺序
        // 不能有一样的顺序
        // 新建个数组
        var s = sortsValue;
        s = s.sort();
        for (var i = 0; i < s.length; i++) {
            if (s[i] == s[i + 1]) {
                hasError = true;
                var index1 = sortsValue.indexOf(s[i]);
                sortsValue[index1] = (-sortsValue[index1]);// 置负数 避免下面被找到 我觉得我真牛^_^
                var index2 = sortsValue.indexOf(s[i + 1]);
                errorInfo = errorInfo + "字段(" + fields[index1] + ")和(" + fields[index2] + ")的排列顺序不能相同！<br>";
                myAnimate($(sortHtml[index1]), 8, $(sortHtml[index1]).attr("style"));
                myAnimate($(sortHtml[index2]), 8, $(sortHtml[index2]).attr("style"));
            }
        }
        if (hasError) {
            showEditFail(errorInfo, null);
            return false;
        }
//        没有错误，则根据排序结果整理成所需要的数据吧
        sqlstmt = $("#editSQL").val();
        sqlfields = "";//qingk
        for (var i = 0; i < s.length; i++) {
            var index = sortsValue.indexOf(s[i]);
            sqlfields = sqlfields + fields[index] + "," + names[index];
            if ((i + 1) < s.length) {
                sqlfields = sqlfields + "#";
            }
        }
        hideEditFail();
        return true;
    }


    // 使用联系人json数据组合成联系人表格内容
    function initTbodyOfTasks(timertasks) {
        var htmlStr = '';
        for (var i = 0; i < timertasks.length; i++) {
            htmlStr = htmlStr + '<tr><td>' + '<input type="checkbox" name="contactsCheckbox" value="' + timertasks[i].id + '"> ' + timertasks[i].id
                    + '</td><td>' + timertasks[i].functions
                    + '</td><td>' + timertasks[i].description
                    + '</td><td>' + timertasks[i].firerules
                    + '</td><td>' + timertasks[i].receivers;
            var stateDesc = '';
            if (timertasks[i].state == 'run') {
                stateDesc = '正常运行';
            } else if (timertasks[i].state == 'pause') {
                stateDesc = '暂停运行';
            }
            htmlStr = htmlStr + '</td><td>' + stateDesc
                    + '</td><td>' + '<button type="button" class="btn btn-primary btn-sm" onclick="initEditTimerTask(\'' + timertasks[i].id
                    + '\',\'' + timertasks[i].functions + '\',\'' + timertasks[i].description + '\',\'' + timertasks[i].firerules + '\',\'' + timertasks[i].receivers + '\',\'' + timertasks[i].state + '\')">编辑</button>'
                    + '</td></tr>';
        }

        $("#tasksBody").html(htmlStr);
        // 取消全选的勾选
        $("#selectAllTimerTasks").prop("checked", false);
    }

    // 通过ids查询联系人详情
    function queryContactsDetailsWithIds() {
        var ids = $("#ttcontactsEdit").val();
        // 判断不为空
        if (ids == '' || name == undefined) {
            showEditFail("当前还没有选择联系人！", $("#ttcontactsEdit"));
            hideEditFail();
            return;
        }
        var jsonStr = "{\"ids\":\"" + ids + "\"}";
        $.ajax({
            type: 'POST',
            url: bp + 'Smserver/contacts/searchbyids',
            data: jsonStr,
            dataType: "json",
            contentType: "application/json",
            success: function (data) {
                showEditDone();
                hideEditFail();
                // 显示联系人信息表格div
                showContactsDiv();
                // 分组信息
                initTbodyOfContacts(data['contacts']);
            }
        });
    }

    // 展示联系人选择
    function showContactsUI() {
        showEditDone();
        hideEditFail();
        // 显示联系人信息表格div
        showContactsDiv();
        // 初始化分组表格
        initTbodyOfGroups();
    }


    // 初始化联系人组UI控件
    function initTbodyOfGroups() {
        var htmlStr = '<tr><td><button type="button" class="btn btn-danger btn-sm" onclick="queryContactsDetailsWithIds()">已选人员</button></td></tr>';
        $.ajax({
            url: bp + 'Smserver/contacts/groups',
            type: 'GET',
            success: function (data) {
                var groups = data['groups'];
                for (var i = 0; i < groups.length; i++) {
                    htmlStr = htmlStr + '<tr><td><button type="button" class="btn btn-danger btn-sm" onclick="getContactsByGroups(\'' + groups[i] + '\')">' + groups[i] + '</button>';
                }
                $("#groupsBody").html(htmlStr);
                // 展示第一组
                getContactsByGroups(groups[0]);
            }
        });
    }

    // 向服务器请求联系人信息 通过分组名称
    function getContactsByGroups(groupname) {
        $.ajax({
            type: 'GET',
            url: bp + 'Smserver/contacts/' + groupname,
            success: function (data) {
                initTbodyOfContacts(data['contacts']);
            }
        });
    }


    //全选或者反选 联系人
    function changeSelectAllContacts() {
        if ($("#selectAllContacts").is(':checked')) {
            $("input[name='contactsCheckbox']").prop("checked", true);// 放弃了attr
        } else {
            $("input[name='contactsCheckbox']").prop("checked", false);
        }
    }

    //全选或者反选 定时任务
    function changeSelectAllTimerTasks() {
        if ($("#selectAllTimerTasks").is(':checked')) {
            $("input[name='timerTasksCheckbox']").prop("checked", true);// 放弃了attr
        } else {
            $("input[name='timerTasksCheckbox']").prop("checked", false);
        }
    }

    // 全选或者反选 功能
    function changeSelectAllFunctions() {
        if ($("#selectAllFunctions").is(':checked')) {
            $("input[name='functionsCheckbox']").prop("checked", true);// 放弃了attr
        } else {
            $("input[name='functionsCheckbox']").prop("checked", false);
        }
    }

    // 添加联系人ids action : add ;remove
    function editContacts(action) {
        // 当前选中的ids
        var currentSelectIds = new Array();
        var aaa = $("input[name='contactsCheckbox']");
        aaa.each(function () {
            if ($(this).prop("checked")) {
                currentSelectIds.push($(this).val());
            }
        });
        if (currentSelectIds.length == 0) {// 没有选择任何联系人
            return;
        }
        // 原有的ids
        var formerIds = null;
        var a = ('' + $("#ttcontactsEdit").val()).trim();
        if (a != '') {
            formerIds = a.split(",");
        } else {
            formerIds = new Array();
        }
        for (var i = 0; i < currentSelectIds.length; i++) {
            // 在原来的ids中查找
            var index = formerIds.indexOf(currentSelectIds[i]);
            if (index != -1) {// 找到了
                if (action == 'add') {
                    // 不用处理
                } else if (action == 'remove') {
                    // 删除找到的元素
                    formerIds.splice(index, 1);
                }
            } else {// 没找到
                if (action == 'add') {
                    // 添加元素
                    formerIds.push(currentSelectIds[i]);
                } else if (action == 'remove') {
                    // 不用处理
                }
            }
        }
        // 返回字符串
        var tempStr = '';
        for (var j = 0; j < formerIds.length; j++) {
            if (j == 0) {
                tempStr = '' + formerIds[j];
            } else {
                tempStr = tempStr + ',' + formerIds[j];
            }
        }
        $("#ttcontactsEdit").val(tempStr);
    }

    function initEditTimerTask(id, functions, description, firerules, receivers, state) {
        // 先将编辑框展示出来
        $("#timertaskEditDiv").show(2000);
        if (id != -1) {
            $("#ttIdEdit").html(id);
            $("#ttfunctionsEdit").html(functions);
            $("#ttdescriptionEdit").val(description);
            $("#ttcronEdit").html(firerules);
            $("#ttcontactsEdit").val(receivers);
            $("#ttstateEdit").val(state);
        } else {
            $("#ttIdEdit").html('新建');
            $("#ttfunctionsEdit").html('未指定');
            $("#ttdescriptionEdit").val('');
            $("#ttcronEdit").html('未指定');
            $("#ttcontactsEdit").val('');
            $("#ttstateEdit").val('run');
        }
    }

    // 编辑定时任务的功能
    function editFunctions() {
        // 显示编辑UIs
        $("#functionsEditDiv").show(2000);
        // 读取当前已经选择的功能
        var selectedFunctions = ($("#ttfunctionsEdit").html() + '').split(",");
        // 从服务器端获取可用的功能
        $.ajax({
            type: 'GET',
            url: bp + 'Smserver/functions',
            success: function (data) {
                var functions = (data['functions']);
                // 整理成表格展示
                var htmlStr = '';
                for (var i = 0; i < functions.length; i++) {
                    htmlStr = htmlStr + '<tr><td>' + '<input type="checkbox" name="functionsCheckbox" value="' + functions[i].name + '"';
                    var isInSelected = false;
                    for (var j = 0; j < selectedFunctions.length; j++) {
                        if (selectedFunctions[j] == functions[i].name) {
                            isInSelected = true;
                            break;
                        }
                    }
                    if (isInSelected) {
                        htmlStr = htmlStr + 'checked';
                    }
                    htmlStr = htmlStr + '> ' + functions[i].id + '</input>'
                            + '</td><td>' + functions[i].name
                            + '</td><td>' + functions[i].description
                            + '</td></tr>';
                }
                $("#functionsBody").html(htmlStr);
                // 取消全选的勾选
                $("#selectAllFunctions").prop("checked", false);
            }
        });
    }

    // 完成或者取消编辑功能
    function editFunctionsDone(action) {
        if (action == 'done') {
            // 检查勾选的功能
            // 当前选中的ids
            var selectIds = new Array();
            var selectNames = new Array();
            var aaa = $("input[name='functionsCheckbox']");
            aaa.each(function () {
                if ($(this).prop("checked")) {
                    selectNames.push($(this).val());
                    selectIds.push($(this).html());
                }
            });
            // 改变展示
            if (selectNames.length == 0) {// 没有选中
                $("#ttfunctionsEdit").html('未指定');
            } else {
                var tt = '';
                for (var i = 0; i < selectNames.length; i++) {
                    if ((i + 1) < selectNames.length) {
                        tt = tt + selectNames[i] + ',';
                    } else {
                        tt = tt + selectNames[i];
                    }
                }
                $("#ttfunctionsEdit").html(tt);
            }
            $("#functionsEditDiv").hide(2000);
        } else if (action == 'cancle') {
            $("#functionsEditDiv").hide(2000);
        }
    }

    // 展示定时任务触发规则的UI
    function editCron() {
        // 显示按钮
        $("#editCronDoneDiv").show(2000);
        // 显示操作页面
        $("#cronExpressionDiv").show(2000);
        // 将原来的cron表达式在UI界面上显示出来
        var formerCron = $("#ttcronEdit").html();
        $("iframe").contents().find("#cron").val(formerCron);
        $("iframe").contents().find("#btnFan").click();
    }

    // 完成编辑定时任务触发规则
    function doneCronEdit() {
        // 获得编辑好的表达式
        var a = $("iframe").contents().find("#cron").val();
        // 设置
        $("#ttcronEdit").html(a);
        // 提示操作完成并隐藏编辑UI
        showEditDone();
        hideCronEdit();
    }

    // 隐藏展示定时任务触发规则的UI
    function hideCronEdit() {
        // 显示按钮
        $("#editCronDoneDiv").hide(2000);
        // 显示操作页面
        $("#cronExpressionDiv").hide(2000);
    }

    // 取消编辑定时任务
    function cancelEditTimerTask() {
        // 编辑框
        $("#timertaskEditDiv").hide(2000);
        hideEditFail();
        hideCronEdit();
    }




    // 判断是不是大于0的整数
    function isInteger(obj) {
        var o = Math.floor(obj);
        if (o == obj) { // ==== 就不行
            if (o >= 0) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    // 检查ip地址填写的是主机名
    function checkIpisHost(value) {
        var exp = /^[a-zA-Z]/;
        var reg = value.match(exp);
        if (reg == null) {
            return false;
        } else {
            return true;
        }
    }

    // 检查IP地址
    function checkIP(value) {
        var exp = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;
        var reg = value.match(exp);
        if (reg == null) {
            return false;
        } else {
            return true;
        }
    }

    function showContactsDiv() {
        $("#contactsDiv").show(2000);
    }

    function hideContactsDiv() {
        $("#contactsDiv").hide(2000);
    }

    function showEditDone() {
        $("#editDoneDiv").show(2000);
        $("#editDoneDiv").hide(2000);
    }
    function showEditFail(msg, el) {
        if (el != null) {
            myAnimate(el, 8, el.attr("style"));
        }
        $("#failCause").html(msg);
        $("#editFailDiv").show(2000);

    }
    function hideEditFail() {
        $("#editFailDiv").hide(2000);
    }
</script>


</body>
</html>
