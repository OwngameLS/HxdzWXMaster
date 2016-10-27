/**
 * Created by Administrator on 2016-10-27.
 * function.jsp 页面的 js支持
 */

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
var sqlstmt;//sql语句
var sqlfields;// sql查询的字段属性，按照顺序来a,aName#b,bName
var usable;// 方法是否可用
var isConnectSuccess = false;// 设置的数据库连接是否成功的实际情况
var formerSqlFieldsHTML = "";//前一次编辑的Sql字段结果

// 因为访问服务器的过程中会得到一些数据，而这些数据并不是每次访问都需要用上，故先保存代用
var tableDefaultCols = null;// 某一张表的所有字段（服务器返回的）
var sqlSelectedFields = null;// 自定义sql语句筛选的字段（服务器返回的）


// 向服务器请求所有方法的信息
function getFunctions() {
    $.when(myAjaxGet(bp + 'Smserver/functions')).done(function (data) {
        if (data != null) {
            initTbodyOfFunctions(data['functions']);
        }
    });
}

// 保存function
function saveFunction() {
    hideEditFail();
    // 利用新的Deferred来做多个同步操作

    $.when(testConnect()).done(function () {// 先验证与数据库的链接可用
        $.when(testFunctionDescPart(),// 验证功能描述性设置
            testSQLPart(),// 验证SQL语句
            testRules()// 验证规则
        ).done(function (data) {
            // 提交
            var jsonStr = "{\"id\":\"" + id
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
            doAjaxHandleFunction('update', jsonStr, 'POST');
        });
    }).fail(function (data2) {
        showEditFail("保存失败！由于未能连接上你设置的数据库，此次保存无法生效。", $("#functionEditDiv"));
    });
}

// 处理Functions操作提交给服务器部分
function doAjaxHandleFunction(action, jsonStr, type) {
    console.log("doAjaxHandleFunction action:" + action);
    $.when(myAjaxPost(bp + 'Smserver/functions/' + action, jsonStr)).done(function (data) {
        var htmlStr = '';
        if (data != null) {
            // 先将编辑框隐藏
            $("#functionEditDiv").hide(2000);
            showEditDone();
            hideEditFail();
            // 刷新功能展示列表
            initTbodyOfFunctions(data['functions']);
        }
    });
}

// 本地检查SQL语句
function checkSQLLocally() {// 检查sql语句，排除非法操作
    var errorinfo = '';
    if (sqlstmt == '' || sqlstmt == null) {
        errorinfo = errorinfo + "您还没有输入SQL语句呢;<br>"
        showEditFail(errorinfo, $("#editSQL"));
    }
    if (sqlstmt.indexOf("remove") >= 0 || sqlstmt.indexOf("delete") >= 0 || sqlstmt.indexOf("update") >= 0) {
        errorinfo = errorinfo + "您输入的SQL语句不是查询语句，请检查！<br><b>注意:</b>只能是查询语句！<br>"
        showEditFail(errorinfo, $("#editSQL"));
    }
    return errorinfo;
}


// 询问服务器 SQL
function askServer4SQL() {// 每次都是保证与数据库连接正常
    var defer = $.Deferred();
    var jsonStr = "{\"ip\":\"" + ip
        + "\",\"port\":\"" + port
        + "\",\"dbtype\":\"" + dbtype
        + "\",\"dbname\":\"" + dbname
        + "\",\"username\":\"" + username
        + "\",\"password\":\"" + password
        + "\",\"tablename\":\"" + tablename
        + "\",\"sqlstmt\":\"" + sqlstmt + "\"}";

    //暂时没错了，交给后台检查吧
    $.when(myAjaxPost(bp + 'Smserver/functions/sql/', jsonStr)).done(function (data) {
        if (data != null) {
            var result = data['sqlResult'];
            if (result.isSuccess < 0) {// 有错误信息
                // 会返回错误信息
                var errorMsg = "您输入的sql语句存在错误：<br>" + result.fields[0];
                sqlSelectedFields = null;// 置空
                defer.reject(errorMsg);
            } else {// 返回的是列表信息
                // 先保存起来就行
                sqlSelectedFields = result.fields;
                console.log("sqlSelectedFields:" + sqlSelectedFields);
                defer.resolve();
            }
        } else {
            var errorMsg = "与服务器通讯失败，请稍后再试。<br>";
            defer.reject(errorMsg);
        }
    }).fail(function () {
        var errorMsg = "与服务器通讯失败，请稍后再试。<br>";
        defer.reject(errorMsg);
    });
    return defer.promise();
}

// 测试SQL语句设置部分
function testSQLPart(isSaveSql) {
    var defer = $.Deferred();
    var errorinfo = '';
    if (isSaveSql == false) {// 按钮触发，必须要测试SQL
        sqlstmt = $("#editSQL").val();
        errorinfo = checkSQLLocally();
        if (errorinfo != '') {
            defer.reject();
            return defer.promise();
        }
        $.when(testConnect(),askServer4SQL()).done(function () {
            console.log("done here...");
            initTbodyOfSQL(sqlSelectedFields, false);
            defer.resolve();
        }).fail(function (error) {
            showEditFail(error, $("#editSQL"));
            defer.reject();
        });

    } else {// 保存function 带来的保存意图
        usetype = getUseType();
        if (usetype == 'sql') {// 是要保存sql规则，就需要检查
            sqlstmt = $("#editSQL").val();
            errorinfo = checkSQLLocally();
            if (errorinfo != '') {
                defer.reject();
            }
            $.when(testConnect(),askServer4SQL()).done(function () {
                // 返回的信息与已设置的进行对比
                if (saveSQL(sqlSelectedFields)) {
                    defer.resolve();
                } else {
                    defer.reject();
                }
            }).fail(function (error) {
                showEditFail(error, $("#editSQL"));
                defer.reject();
            });
        } else {// 不需要保存sql
            // 置空
            sqlstmt = '';
            sqlfields = '';
            defer.resolve();
        }
    }
    return defer.promise();
}

// 保存SQL规则
function saveSQL(sqlSelectedFields) {
    // 获得已经设置的字段集合
    var settedFields = new Array();
    var fieldsHtml = $("[id*='sqlFieldCol']");
    for (var i = 0; i < fieldsHtml.length; i++) {
        settedFields.push($(fieldsHtml[i]).text());
    }
    var errorMsg = '';
    // 与查询得到的字段进行对比
    if (sqlSelectedFields.length != settedFields.length) {
        // 新旧长度不统一
        // 重新初始化并告知
        initTbodyOfSQL(sqlSelectedFields, false);
        if (settedFields.length == 0) {
            errorMsg = "您必须对您的查询字段做相应的设置。";
        } else {
            errorMsg = "您查询的字段与您设置的字段不一样，请重新设置。";
        }
        showEditFail(errorMsg, $("#editSQL"));
        return false;
    }
    var hasNotFound = false;
    for (var i = 0; i < sqlSelectedFields.length; i++) {
        if (settedFields.indexOf(sqlSelectedFields[i]) == -1) {
            hasNotFound = true;
        }
    }

    if (hasNotFound) {
        // 重新初始化并告知
        initTbodyOfSQL(sqlSelectedFields, false);
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
            errorInfo = errorInfo + "字段(" + settedFields[i] + ")必须设置名称哦！<br>";
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
            errorInfo = errorInfo + "字段(" + settedFields[i] + ")必须设置排列顺序哦！<br>";
            myAnimate($(sortHtml[i]), 8, $(sortHtml[i]).attr("style"));
        } else if (isInteger(sort) == false) {
            hasError = true;
            errorInfo = errorInfo + "字段(" + settedFields[i] + ")的排列顺序必须为正整数哦！<br>";
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
            errorInfo = errorInfo + "字段(" + settedFields[index1] + ")和(" + settedFields[index2] + ")的排列顺序不能相同！<br>";
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
        sqlfields = sqlfields + settedFields[index] + "," + names[index];
        if ((i + 1) < s.length) {
            sqlfields = sqlfields + "#";
        }
    }
    hideEditFail();
    return true;
}


// 验证功能描述性设置部分
function testFunctionDescPart() {
    var defer = $.Deferred();
    var errorinfo = '';
    // 功能名称
    name = $("#editName").val();
    if (name == '' || name == null) {
        errorinfo = errorinfo + "必须输入功能名称；<br>";
        showEditFail(errorinfo, $("#editName"));
    }
    // 关键字
    keywords = $("#editKeywords").val();
    if (keywords == '' || keywords == null) {
        errorinfo = errorinfo + "必须输入关键字；<br>";
        showEditFail(errorinfo, $("#editKeywords"));
    }
    // 检查描述
    description = $("#editDescription").val();
    if (description == '' || description == null) {
        errorinfo = errorinfo + "必须输入描述；<br>";
        showEditFail(errorinfo, $("#editDescription"));
    }
    if (errorinfo != '') {
        defer.reject(errorinfo);
        return defer.promise();
    }

    // keywords不为空，还要检测它的唯一性
    var jsonStr = "{\"id\":\"" + id + "\",\"keywords\":\"" + keywords + "\"}";
    $.when(myAjaxPost(bp + 'Smserver/functions/keywords/', jsonStr)).done(function (data) {
        if (data != null) {
            var result = data['keywordResult'];
            if (result.isSuccess < 0) {
                // 会返回类似的关键字，将他们罗列出来
                var errorMsg = "关键字重复！<br>";
                for (var i = 0; i < result.similarKeys.length; i++) {
                    errorMsg = errorMsg + result.similarKeys[i];
                    if ((i + 1) < result.similarKeys.length) {
                        errorMsg = errorMsg + "<br>";
                    }
                }
                showEditFail(errorMsg, $("#editKeywords"));
                defer.reject(errorMsg);
            } else {
                defer.resolve(true);
            }
        } else {
            showEditFail("读取服务器信息失败，请稍后再试...", $("#editKeywords"));
            defer.reject("读取服务器信息失败，请稍后再试...");
        }
    }).fail(function () {
        showEditFail("与服务器通讯失败，请稍后再试...", $("#editKeywords"));
        defer.reject("与服务器通讯失败，请稍后再试...");
    });
    return defer.promise();
}

// 请求查询表格的所有字段用来编辑
function queryTableCols4Edit(){
    $.when(testConnect()).done(function () {
        isConnectSuccess = true;
        var htmlStr = '<p style="color: #0000FF">连接成功!</p>';
        $("#connectResult").html(htmlStr);
        myAnimate($("#connectResult"), 8, $("#connectResult").attr("style"));
        initTbodyOfCols(tableDefaultCols);
    }).fail(function () {
        isConnectSuccess = false;
        var htmlStr = '<p style="color: #c9302c">连接失败!</p>';
        $("#connectResult").html(htmlStr);
        myAnimate($("#connectResult"), 8, $("#connectResult").attr("style"));
    });
}

// 检查数据库连通性 isShowCols 是否需要将查询得到的字段展示出来 true:展示，fasle:不展示
function testConnect() {
    var defer = $.Deferred();
    var errorinfo = '';
    ip = $("#editIP").val();
    if (ip == '' || ip == null) {
        errorinfo = errorinfo + "必须输入IP地址；<br>";
        showEditFail(errorinfo, $("#editIP"));
    }
    var ignoreIp = false;
    if (checkIpisHost(ip)) {
        // 发现填写的可能是主机名
        if (confirm("发现你在Ip地址填写的不是合理的Ip,是否继续？")) {
            ignoreIp = true;
        } else {
            errorinfo = errorinfo + "不是合理的IP地址；<br>";
            showEditFail(errorinfo, $("#editIP"));
            defer.reject(errorinfo);
            return defer.promise();
        }
    }
    if (ignoreIp == false) {// 需要Ip检查
        if (checkIP(ip) == false) {
            errorinfo = errorinfo + "必须输入IP地址；<br>";
            showEditFail(errorinfo, $("#editIP"));
        }
    }
    port = $("#editPort").val();
    if (port == '' || port == null) {
        errorinfo = errorinfo + "必须输入端口号；<br>";
        showEditFail(errorinfo, $("#editPort"));
    }
    if (isInteger(port) == false) {
        errorinfo = errorinfo + "端口号必须输入正整数；<br>";
        showEditFail(errorinfo, $("#editPort"));
    }

    dbtype = $("#editDbtype  option:selected").val();

    dbname = $("#editDbname").val();
    if (dbname == '' || dbname == null) {
        errorinfo = errorinfo + "必须输入数据库名；<br>";
        showEditFail(errorinfo, $("#editDbname"));
    }
    username = $("#editUsername").val();
    if (username == '' || username == null) {
        errorinfo = errorinfo + "必须输入用户名；<br>";
        showEditFail(errorinfo, $("#editUsername"));
    }
    password = $("#editPassword").val();
    if (password == '' || password == null) {
        errorinfo = errorinfo + "必须输入密码；<br>";
        showEditFail(errorinfo, $("#editPassword"));
    }
    tablename = $("#editTablename").val();
    if (tablename == '' || tablename == null) {
        errorinfo = errorinfo + "必须输入表名；<br>";
        showEditFail(errorinfo, $("#editTablename"));

    }
    if (errorinfo != '') {
        defer.reject(errorinfo);
        return defer.promise();
    }

    var jsonStr = "{\"ip\":\"" + ip
        + "\",\"port\":\"" + port
        + "\",\"dbtype\":\"" + dbtype
        + "\",\"dbname\":\"" + dbname
        + "\",\"username\":\"" + username
        + "\",\"password\":\"" + password
        + "\",\"tablename\":\"" + tablename + "\"}";
    // 访问服务器
    $.when(myAjaxPost(bp + 'Smserver/functions/testconnect', jsonStr)).done(function (data) {
        if (data != null) {
            var colsNames = data['colNames'];
            if (colsNames != null) {// 获得了字段信息
                tableDefaultCols = colsNames;// 保存到全局变量中
                isConnectSuccess = true;
                defer.resolve();
            } else {// 没有获得字段信息
                defer.reject();
                tableDefaultCols = null;
//                    if (isShowCols == false) {// 只需告知结果
//                        if (confirm("你设置的数据库连接没有成功，确认继续操作？")) {
//                            result = true;// 当保存时数据库出现问题，设置没有问题时
//                        }
//                    }
//                    $("#colsDIV").hide(2000);
            }
        } else {
            isConnectSuccess = false;
            defer.reject();
        }
    }).fail(function () {// 连接失败
        defer.reject();
    });
    return defer.promise();
}

// 获得规则使用类型
function getUseType() {
    usetype = $('input[name="whichType"]:checked ').val();
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


// sql字段类
function sqlField(name, selfName, sort) {
    this.name = name;
    this.selfName = selfName;
    this.sort = sort;
}

// 当使用sql语句规则时的相关设置
function initTbodyOfSQL(fields, isEdit) { // isEdit 是否是编辑，true，则 fields为字符串，false 为数组
    var arr = new Array();
    $("#sqlFields").html("");
    if (isEdit) {// 是编辑功能，说明原来有内容
        // id,序号#count_type,统计类型
        var t = fields.split("#");
        for (var i = 0; i < t.length; i++) {
            var temp = t[i].split(",");
            var a = new sqlField(temp[0], temp[1], i + 1);
            arr.push(a);
        }
    } else {
        for (var i = 0; i < fields.length; i++) {
            var a = new sqlField(fields[i], '', i + 1);
            arr.push(a);
        }
    }
    var htmlStr = '<div class="row"><div class="col-md-3 text-center">字段名</div><div class="col-md-3 text-center">名称</div><div class="col-md-3 text-center">排序序号</div></div>';
    for (var i = 0; i < arr.length; i++) {
        htmlStr = htmlStr + '<div class="row">';
        htmlStr = htmlStr + '<div class="col-md-3 text-center"><b id="sqlFieldCol' + i + '" >' + arr[i].name + '</b></div>';
        htmlStr = htmlStr + '<div class="col-md-3 text-center"><input class="form-control" id="sqlFieldName' + i + '" placeholder="名称" value="' + arr[i].selfName + '"></div>';
        htmlStr = htmlStr + '<div class="col-md-3 text-center"><input class="form-control" id="sqlFieldSort' + i + '" placeholder="序号" value="' + arr[i].sort + '"></div>';
        htmlStr = htmlStr + '</div>';
    }
    $("#sqlFields").html(htmlStr);
    $("#sqlresultDiv").show(2000);
}


// 检查字段规则
function testRules() {
    var defer = $.Deferred();
    var errorinfo = '';
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
                defer.reject();
            }
            // 添加进读取字段数组中
            readArray.push(queryfields[i] + "," + selfColName);
        }
        // 检查是否使用规则
        var isusedRule = $("#isusedRule" + i).prop("checked");
        if (isusedRule) {// 使用规则 进一步判断
            // 为方便告知规则情况，需要了解字段自定义名称
            if (selfColName == null || selfColName == '') {
                showEditFail("你选择使用规则，则字段<b>" + queryfields[i] + "</b>必须填写它的自定义名称。", $("#selfColName" + i));
                defer.reject();
            }
            var ruleType = $('input[name="rule' + i + '"]:checked ').val();
            if (ruleType == 'EQ' || ruleType == 'NE') {// 等于或者不等于的规则
                // 检查参照值为合法输入 非空即可
                var compareValue = $("#compareValue" + i).val();
                if (compareValue == null || compareValue == '') {
                    showEditFail("字段<b>" + queryfields[i] + "</b>使用规则，其参照值不能为空！", $("#compareValue" + i));
                    defer.reject();
                } else {
                    rulesArray.push(queryfields[i] + "," + selfColName + "," + compareValue + "," + ruleType);
                }
            } else if (ruleType == 'BB') {// 大于
                var aboveValue = $("#above" + i).val();
                if (aboveValue == null || aboveValue == '') {
                    showEditFail("字段<b>" + queryfields[i] + "</b>使用‘大于’规则，其参照值不能为空！", $("#above" + i));
                    defer.reject();
                } else {
                    rulesArray.push(queryfields[i] + "," + selfColName + "," + aboveValue + "," + ruleType);
                }
            } else if (ruleType == 'LL') {// 小于
                var belowValue = $("#below" + i).val();
                if (belowValue == null || belowValue == '') {
                    showEditFail("字段<b>" + queryfields[i] + "</b>使用‘小于’规则，其参照值不能为空！", $("#below" + i));
                    defer.reject();
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
                    defer.reject();
                }
                var rangeup = $("#rangeup" + i).val();
                if (rangeup == null || rangeup == '') {
                    showEditFail("字段<b>" + queryfields[i] + "</b>使用‘范围’规则，其上限值不能为空！", $("#rangeup" + i));
                    defer.reject();
                }
                // 检查大小、不要颠倒
                if (rangedown == rangeup) {// 大小相同
                    showEditFail("字段<b>" + queryfields[i] + "</b>使用‘范围’规则，其上限值和下限值不能相等！", $("#rangeup" + i));
                    defer.reject();
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
    if (fieldrules == "") {// 没有定义规则，就是查询到了就返回
        isreturn = "anyway";
    } else {
        isreturn = "oncase";
    }
    console.log("readfields:" + readfields + ";sortfields:" + sortfields + "; fieldrules:" + fieldrules);
    defer.resolve();
    return defer.promise();
}


// 使用联系人json数据组合成联系人表格内容
function initTbodyOfFunctions(functions) {
    var htmlStr = '';
    for (var i = 0; i < functions.length; i++) {
        htmlStr = htmlStr + '<tr><td>' + functions[i].id
            + '</td><td>' + functions[i].name
            + '</td><td>' + functions[i].keywords
            + '</td><td>' + parseToAbbr(functions[i].description, 10, null)
            + '</td><td>'
            + '<button type="button" class="btn btn-warning btn-sm" onclick="detail(\'' + functions[i].id + '\')">详情</button> '
            + '<button type="button" class="btn btn-primary btn-sm" onclick="edit(\'' + functions[i].id + '\')">编辑</button> '
            + '<button type="button" class="btn btn-danger btn-sm" onclick="deleteFunction(\'' + functions[i].id + '\')">删除</button> '
            + '</td></tr>';
    }
    $("#functionsBody").html(htmlStr);

}


// 查看某个功能的信息
function detail(id) {
    $("#mbody").html('<img src="/resources/bootstrap-3.3.7-dist/img/loading.gif" style="width: 100px;height: 100px"/> 请稍后...');
    $("#myModal").modal("show");
    $.when(myAjaxGet(bp + 'Smserver/functions/get/' + id)).done(function (data) {
        var htmlStr = '';
        if (data != null) {
            var func = data['function'];
            htmlStr = ''
                + '<b>名称: </b>' + func.name + '<br>'
                + '<b>描述: </b>' + parseToAbbr(func.description, 20, null) + '<br>'
                + '<b>关键词: </b>' + func.keywords + '<br>'// 关键字，当用户自主查询时，通过关键字匹配
                + '<b>IP: </b>' + func.ip + '<br>'
                + '<b>端口: </b>' + func.port + '<br>'
                + '<b>数据库类型: </b>' + func.dbtype + '<br>'
                + '<b>数据库名称: </b>' + func.dbname + '<br>'
                + '<b>用户名: </b>' + func.username + '<br>'
                + '<b>密码: </b>' + func.password + '<br>'
                + '<b>表名: </b>' + func.tablename + '<br>'
                + '<b>规则类型: </b>' + func.usetype + '<br>'// 由于加入了SQL语句查询，需要确定使用哪个 sql或rules
                + '<b>读取字段: </b>' + func.readfields + '<br>'// 要读取的字段 用户查询所需的字段a,aName#b,bName
                + '<b>排序字段: </b>' + func.sortfields + '<br>'// 排序字段，根据这个字段才能查询到最新的数据 A ASC,B DESC 默认为降序排列
                + '<b>筛选规则: </b>' + func.fieldrules + '<br>'// 规则字段，字段名，值，规则 根据规则来判断 a,aName,-1,NN#b,bName,5,BB#c,cName,200,LL#d,dName,abcd,EQ#e,eName,bcde,NE#f,fName,xxxx,RG@12BT34
                + '<b>是否返回: </b>' + func.isreturn + '<br>'// 读取结果是否返回的规则（由于需要涉及到预警功能，所以需要定义规则）
                + '<b>sql语句: </b>' + func.sqlstmt + '<br>'//sql语句
                + '<b>sql读取字段: </b>' + func.sqlfields + '<br>';// sql查询的字段属性，按照顺序来a,aName#b,bName

        } else {
            htmlStr = '获取失败！<img src="../../resources/bootstrap-3.3.7-dist/img/error.png" />';
        }
        $("#mbody").html(htmlStr);
    });
}
// 编辑某个功能
function edit(tempId) {
    id = tempId;
    $.when(myAjaxGet(bp + 'Smserver/functions/get/' + tempId)).done(function (data) {
        var htmlStr = '';
        if (data != null) {
            var func = data['function'];
            // tempId = func.id;
            // 依次初始化相关控件
            $("#editName").val(func.name);
            $("#editKeywords").val(func.keywords);
            $("#editDescription").val(func.description);
            $("#editIP").val(func.ip);
            $("#editPort").val(func.port);
            $("#editDbtype").val(func.dbtype);
            $("#editDbname").val(func.dbname);
            $("#editUsername").val(func.username);
            $("#editPassword").val(func.password);
            $("#editTablename").val(func.tablename);
            $("input[name='whichType'][value='" + func.usetype + "']").attr("checked", true);  //根据Value值设置Radio为选中状态
            if (func.sqlstmt == 'null' || func.sqlstmt == 'undefined' || func.sqlstmt == null) {
                $("#editSQL").val('');
            } else {
                $("#editSQL").val(func.sqlstmt);
                // 展示字段
                initTbodyOfSQL(func.sqlfields, true);
            }
            if (func.readfields == 'null' || func.readfields == 'undefined' || func.readfields == null) {
                $("#colsTR").html("");
            } else {
                initColFromDB(initRuleFieldArray(func.readfields, func.sortfields, func.fieldrules));
            }
            $("#functionEditDiv").show(3000);
        }
    });
}

// 用来初始化每一个规则字段控件的类
function ruleField() {
    this.name;// 字段在数据库中的字段名
    this.selfname = null;// 用户自定义名
    this.issort = null;// 是否排序 有值就是要排序
    this.isread = null;// 是否读取
    this.comparevalue = null;// 对比值
    this.rule = null;// 判断条件
}

// 初始化规则字段类对象数组
function initRuleFieldArray(readfields, sortfields, fieldrules) {
    var ruleFields = new Array();
    // 先分析 readfields : id,序号#count_type,计时类型
    var rf = readfields.split("#");
    for (var i = 0; i < rf.length; i++) {
        var t = rf[i].split(",");
        var temp = new ruleField();
        temp.name = t[0];
        temp.selfname = t[1];
        temp.isread = true;
        ruleFields.push(temp);
    }
    // 再分析 sortfields : a desc,b asc
    var sf = sortfields.split(",");
    for (var i = 0; i < sf.length; i++) {
        var t = sf[i].split(" ");
        var name = t[0];
        var issort = t[1];
        var index = indexInRuleFields(name, ruleFields);
        if (index == -1) {// 不存在
            // 需要新增加
            var temp = new ruleField();
            temp.name = name;
            temp.issort = issort;
            ruleFields.push(temp);
        } else {// 存在
            ruleFields[index].issort = issort;
        }
    }
    // 最后分析 fieldrules : player_statids_home,home,-1,NN#player_statids_guest,guest,-1,NN
    var fr = fieldrules.split("#");
    for (var i = 0; i < fr.length; i++) {
        var t = fr[i].split(",");
        var name = t[0];
        var selfname = t[1];
        var comparevalue = t[2];
        var rule = t[3];
        var index = indexInRuleFields(name, ruleFields);
        if (index == -1) {// 不存在
            // 需要新增加
            var temp = new ruleField();
            temp.name = name;
            temp.selfname = selfname;
            temp.comparevalue = comparevalue;
            temp.rule = rule;
            ruleFields.push(temp);
        } else {// 存在
            ruleFields[index].name = name;
            ruleFields[index].selfname = selfname;
            ruleFields[index].comparevalue = comparevalue;
            ruleFields[index].rule = rule;
        }
    }

    for (var i = 0; i < ruleFields.length; i++) {
        console.log(i + ":" + ruleFields[i].name + "," + ruleFields[i].selfname + "," + ruleFields[i].issort + "," + ruleFields[i].isread + "," + ruleFields[i].comparevalue + "," + ruleFields[i].rule);
    }

    return ruleFields;
}

// 检查某个字段是否在规则对象数组中
function indexInRuleFields(name, ruleFields) {
    for (var i = 0; i < ruleFields.length; i++) {
        if (ruleFields[i].name == name) {
            return i;
        }
    }
    return -1;
}

// 初始化字段规则设置UI（从数据库读出的字段）
function initColFromDB(ruleFields) {
    var htmlStr = '';
    for (var i = 0; i < ruleFields.length; i++) {
        if (i % 2 == 0) {
            htmlStr = htmlStr
                + '<div class="row bg-warning">';
        } else {
            htmlStr = htmlStr
                + '<div class="row">';
        }
        htmlStr = htmlStr + '<div class="col-md-2 text-center"><b id="colName' + i + '">' + ruleFields[i].name + '</b></div>';

        if (ruleFields[i].isread != null || ruleFields[i].rule != null) {
            htmlStr = htmlStr + '<div class="col-md-3 text-center"><input class="form-control" id="selfColName' + i + '" placeholder="名称" value="' + ruleFields[i].selfname + '"></div>'
        } else {
            htmlStr = htmlStr + '<div class="col-md-3 text-center"><input class="form-control" id="selfColName' + i + '" placeholder="名称"></div>'
        }
        htmlStr = htmlStr + '<div class="col-md-3 text-center">';

        if (ruleFields[i].issort != null) {// 需要排序
            htmlStr = htmlStr + '<input type="checkbox" id="isSort' + i + '" checked>是';
            if (ruleFields[i].issort == 'desc') {
                htmlStr = htmlStr + '<input type="radio" name="sort' + i + '" value="desc" checked>降序 <input type="radio" name="sort' + i + '" value="asc">升序</div>';
            } else {
                htmlStr = htmlStr + '<input type="radio" name="sort' + i + '" value="desc">降序 <input type="radio" name="sort' + i + '" value="asc" checked>升序</div>';
            }
        } else {
            htmlStr = htmlStr
                + '<input type="checkbox" id="isSort' + i + '">是'
                + '<input type="radio" name="sort' + i + '" value="desc" checked>降序 <input type="radio" name="sort' + i + '" value="asc">升序</div>';
        }

        if (ruleFields[i].isread != null) {// 是否读取
            htmlStr = htmlStr + '<div class="col-md-2 text-center"><input type="checkbox" id="isread' + i + '" value="' + ruleFields[i].name + '" checked>读取</div>';
        } else {
            htmlStr = htmlStr + '<div class="col-md-2 text-center"><input type="checkbox" id="isread' + i + '" value="' + ruleFields[i].name + '">读取</div>';
        }
        if (ruleFields[i].rule != null) {// 是否使用规则
            htmlStr = htmlStr + '<div class="col-md-2 text-center"><input type="checkbox" id="isusedRule' + i + '" checked>使用规则</div>'
                + '</div>';
        } else {
            htmlStr = htmlStr + '<div class="col-md-2 text-center"><input type="checkbox" id="isusedRule' + i + '">使用规则</div>'
                + '</div>';
        }
        if (i % 2 == 0) {
            htmlStr = htmlStr
                + '<div class="row bg-warning" style="border-bottom:1px solid #222222;">';
        } else {
            htmlStr = htmlStr
                + '<div class="row" style="border-bottom:1px solid #245580;">';
        }
        if (ruleFields[i].rule == null) {
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
        } else {
            htmlStr = htmlStr + '<div class="col-md-2 text-center">设置规则</div>' + '<div class="col-md-2 text-center">';
            var htmlStrEQ = '', htmlStrNE = '', htmlStrCompareValue = '', htmlStrBB = '', htmlStrLL = '', htmlStrRG = '';
            var isEQorNE = false; // 规则是大于或者小于
            if (ruleFields[i].rule.indexOf("EQ") == 0) {
                isEQorNE = true;
                htmlStrEQ = '<input type="radio" name="rule' + i + '" value="EQ" checked>等于 ';
            } else {
                htmlStrEQ = '<input type="radio" name="rule' + i + '" value="EQ">等于 ';
            }
            if (ruleFields[i].rule.indexOf("NE") == 0) {
                isEQorNE = true;
                htmlStrNE = '<input type="radio" name="rule' + i + '" value="NE" checked>不等于<br>';
            } else {
                htmlStrNE = '<input type="radio" name="rule' + i + '" value="NE">不等于<br>';
            }
            if (isEQorNE) {// 等于或者不等于 的参照值
                htmlStrCompareValue = '参照值<input class="form-control" id="compareValue' + i + '" placeholder="该字段的合理值" value="' + ruleFields[i].comparevalue + '"></div>';
            } else {
                htmlStrCompareValue = '参照值<input class="form-control" id="compareValue' + i + '" placeholder="该字段的合理值"></div>';
            }

            if (ruleFields[i].rule.indexOf("BB") == 0) {
                htmlStrBB = '<div class="col-md-1 text-center">'
                    + '<input type="radio" name="rule' + i + '" value="BB" checked>大于'
                    + '<input class="form-control" id="above' + i + '" placeholder="大于" value="' + ruleFields[i].comparevalue + '"></div>';
            } else {
                htmlStrBB = '<div class="col-md-1 text-center">'
                    + '<input type="radio" name="rule' + i + '" value="BB">大于'
                    + '<input class="form-control" id="above' + i + '" placeholder="大于"></div>';
            }

            if (ruleFields[i].rule.indexOf("LL") == 0) {
                htmlStrLL = '<div class="col-md-1 text-center">'
                    + '<input type="radio" name="rule' + i + '" value="LL" checked>小于'
                    + '<input class="form-control" id="below' + i + '" placeholder="小于" value="' + ruleFields[i].comparevalue + '"></div>';
            } else {
                htmlStrLL = '<div class="col-md-1 text-center">'
                    + '<input type="radio" name="rule' + i + '" value="LL">小于'
                    + '<input class="form-control" id="below' + i + '" placeholder="小于"></div>';
            }

            if (ruleFields[i].rule.indexOf("RG") == 0) {
                htmlStrRG = '<div class="col-md-6 text-center" style="border-style: groove">'
                    + '<input type="radio" name="rule' + i + '" value="RG" checked>范围<br>';
                // 分析RG
                var rangedown, rangeup;
                var indexAt = ruleFields[i].rule.indexOf("@");
                if (ruleFields[i].rule.indexOf("BT") != -1) {// 中间between
                    var indexBT = ruleFields[i].rule.indexOf("BT");
                    rangedown = ruleFields[i].rule.substring(indexAt + 1, indexBT);
                    rangeup = ruleFields[i].rule.substring(indexBT + 2);
                    htmlStrRG = htmlStrRG + '<div class="col-md-4 text-center"><input type="radio" name="range' + i + '" value="BT" checked>在内 <input type="radio" name="range' + i + '" value="OUT">在外 </div>';
                } else {// 两端 out
                    var indexOUT = ruleFields[i].rule.indexOf("OUT");
                    rangedown = ruleFields[i].rule.substring(indexAt + 1, indexOUT);
                    rangeup = ruleFields[i].rule.substring(indexOUT + 3);
                    htmlStrRG = htmlStrRG + '<div class="col-md-4 text-center"><input type="radio" name="range' + i + '" value="BT">在内 <input type="radio" name="range' + i + '" value="OUT"  checked>在外 </div>';
                }
                htmlStrRG = htmlStrRG + '<div class="col-md-4 text-center">' +
                    '下限值:<input class="form-control" id="rangedown' + i + '" value="' + rangedown + '"></div>'
                    + '<div class="col-md-4 text-center">' +
                    '上限值:<input class="form-control" id="rangeup' + i + '" value="' + rangeup + '"></div>'
                    + '</div>'
                    + '</div><br>';
            } else {
                htmlStrRG = '<div class="col-md-6 text-center" style="border-style: groove">'
                    + '<input type="radio" name="rule' + i + '" value="RG">范围<br>'
                    + '<div class="col-md-4 text-center"><input type="radio" name="range' + i + '" value="BT" checked>在内 <input type="radio" name="range' + i + '" value="OUT">在外 </div>'
                    + '<div class="col-md-4 text-center">下限值:<input class="form-control" id="rangedown' + i + '"></div>'
                    + '<div class="col-md-4 text-center">上限值:<input class="form-control" id="rangeup' + i + '"></div>'
                    + '</div>'
                    + '</div><br>';
            }

            htmlStr = htmlStr + htmlStrEQ + htmlStrNE + htmlStrCompareValue + htmlStrBB + htmlStrLL + htmlStrRG;
        }

    }

    $("#colsTR").html(htmlStr);
    $("#colsDIV").show(2000);
}


// 删除某个功能
function deleteFunction(id) {
    // 弹出确认对话框
    if (confirm("确认删除？")) {
        var jsonStr = "{\"id\":" + id + "}";
        doAjaxHandleFunction('delete', jsonStr, 'POST');
    } else {
        return;
    }
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

function hideEditDiv(){
    $("#functionEditDiv").hide(2000);
}


