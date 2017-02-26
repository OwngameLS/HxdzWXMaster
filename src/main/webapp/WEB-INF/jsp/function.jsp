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
    <!-- 引入 Bootstrap -->
    <link href="../../resources/bootstrap-3.3.7-dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="../../resources/bootstrap-3.3.7-dist/js/uiscript.js"></script>
    <script src="../../resources/bootstrap-3.3.7-dist/js/askserver.js"></script>
    <script src="../../resources/bootstrap-3.3.7-dist/js/pager.js"></script>
    <script src="../../resources/bootstrap-3.3.7-dist/js/function.js"></script>
</head>
<body>
<h3>功能设置</h3>

<div class="danger" style="padding: 5px;"><%--通讯录操作部分--%>
    <dl>
        <dt>编辑“功能”</dt>
        <dd>
            <p class="text-warning">
                在下面进行您自定义功能的编辑
            </p>
        </dd>
    </dl>

    <!-- 模态框（Modal） -->
    <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title" id="myModalLabel">功能详情</h4>
                </div>
                <div id="mbody" class="modal-body"></div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-warning" data-dismiss="modal">关闭</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal -->
    </div>

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

    <div id="functionEditDiv" style="display:none" class="well"><%--编辑定时任务的表格 style="display:none"--%>
        <div class="row">
            <div id="infos" class="col-md-4 text-center"></div>
            <div class="col-md-2 text-center">
                <b>功能属性编辑</b>
            </div>
            <div id="isUsable" class="col-md-2 text-center"></div>
            <div class="col-md-4 text-center">
                <button type="button" class="btn btn-primary btn-sm" onclick="copyFunction()">复 制</button>
                <button type="button" class="btn btn-danger btn-sm" onclick="saveFunctionAnyway()">仅保存</button>
                <button type="button" class="btn btn-success btn-sm" onclick="saveFunction(1)">验证并保存</button>
                <button type="button" class="btn btn-warning btn-sm" onclick="hideEditDiv()">取 消</button>
            </div>
        </div>
        ---功能描述性设置-------------------------
        <div class="row bg-success">
            <div class="col-md-2 text-center">名称</div>
            <div class="col-md-3 text-center">关键字</div>
            <div class="col-md-2 text-center">等级</div>
            <div class="col-md-5 text-center">描述</div>
        </div>
        <div class="row">
            <div class="col-md-2 text-center">
                <input class="form-control" id="editName" placeholder="给功能起一个名字..."></div>
            <div class="col-md-3 text-center">
                <input class="form-control" id="editKeywords" placeholder="关键字，区别于其他功能的关键字..."></div>
            <div class="col-md-2 text-center">
                <select class="form-control" id="editGrade">
                    <option value="0">0</option>
                    <option value="1">1</option>
                    <option value="2">2</option>
                    <option value="3">3</option>
                    <option value="4">4</option>
                    <option value="5">5</option>
                    <option value="6">6</option>
                    <option value="7">7</option>
                </select>
            </div>
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
                <button type="button" class="btn btn-success btn-sm" onclick="askServer4Cols2Edit()">连接测试</button>
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
                    <button type="button" class="btn btn-success btn-sm" onclick="testSQLPart(false)">测试</button>
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
                <input type="radio" name="whichType" value="rules" checked>使用规则（读取字段的设置，注意：所有的规则都满足才能查询到结果）
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

    <button type="button" class="btn btn-warning btn-sm" onclick="edit(-1)">新建功能</button>
    <div id="functionsDIV" style="width:100%;float:left;overflow:scroll; height:400px;" class="well">
        <table class="table table-hover table-bordered text-center">
            <thead>
            <tr class="info">
                <th width="10%" class="text-center">序号</th>
                <th width="14%" class="text-center">名称</th>
                <th width="12%" class="text-center">关键字</th>
                <th width="30%" class="text-center">描述</th>
                <th width="10%" class="text-center">等级</th>
                <th width="24%" class="text-center">操作</th>
            </tr>
            </thead>
            <tbody id="functionsBody">
            </tbody>
        </table>
    </div>
    <div id="pageSelectDiv" style="text-align:center;width:100%;" >
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
        if (checkAuthorizationStateByUI()) {
            getFunctions();
        }
    });

</script>


</body>
</html>
