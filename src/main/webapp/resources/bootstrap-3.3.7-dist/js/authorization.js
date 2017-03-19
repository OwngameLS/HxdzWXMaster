/**
 * Created by Administrator on 2017/1/11.
 */


function getAuthorizedState() {
    // 先获得状态
    $.when(myAjaxGet(bp + 'Smserver/authorization/authorizedState')).done(function (data) {
        if (data != null) {
            var authorizedState = data['authorizedState'];
            if (authorizedState == 'invalid') {
                var invalidReason = data['invalidReason'];
                $("#invalidReason").html(invalidReason + "<br>");
                $("#authorizedModal").modal("show");
                // 添加未授权标识
                $("#unauth").show();
                countDown(5);
            } else {
                // 删除未授权标识
                $("#unauth").hide();
            }
        }
    });
}

function countDown(counts) {
    if (counts == 0) {
        gotoAuthorizePage();
        return;
    }
    var htmlStr = counts + " 秒后将跳转到设置页面...";
    $("#countdowndesc").html(htmlStr);
    counts = counts - 1;
    setTimeout("countDown(" + counts + ")", 1000);
}


// 跳转到设置页面
function gotoAuthorizePage() {
    $('#authorize>p').trigger('click');
    $("#authorizedModal").modal("hide");
}


// 询问服务器是否有同名
function checkUsername() {
    $("#registerResult").html('');
    var defer = $.Deferred();
    var username = $("#username").val();
    if (isEmpty(username)) {
        showEditFail("请先输入内容！", $("#username"));
        defer.reject();
        return;
    }
    // 先检查不要含有特殊字符
    var reg = /^[0-9a-zA-Z]*$/g;
    if (reg.test(username) == false) {
        showEditFail("请输入英文、数字组合构成的名称。", $("#username"));
        defer.reject();
        return;
    }
    var jsonStr = "{\"username\":\"" + username + "\"}";
    $.when(myAjaxPost(bp + 'Smserver/authorization/checkusername', jsonStr)).done(function (data) {
        if (data != null) {
            var success = data['success'];
            if (success == 'success') {
                $("#usernameCheckResult").html('<img src="' + bp + 'resources/bootstrap-3.3.7-dist/img/settings_done.png">');
                defer.resolve();
            } else {
                $("#usernameCheckResult").html('<img src="' + bp + 'resources/bootstrap-3.3.7-dist/img/settings_undeletable.png">');
                showEditFail("【用户名已存在】<br>" + data['similarNames'], $("#username"));
                defer.reject();
            }
        }
    });
    return defer.promise();
}

function changeInputUsername() {
    $("#usernameCheckResult").html('');
}

function register() {
    // 先检查用户名的唯一性
    $.when(checkUsername()).done(function (data) {
        var username = $("#username").val();
        // 通过用户名检查了，检查手机号
        var phone = $("#phone").val();
        if (isEmpty(phone)) {
            showEditFail("请你输入你即将使用的手机号码。", $("#phone"));
            return;
        }
        if (isValidPhone(phone) == false) {
            showEditFail("你输入的手机号码有误，请再次检查。", $("#phone"));
            return;
        }
        $("#registerResult").html('');
        // 提交给服务器
        var jsonStr = "{\"phone\":\"" + phone
            + "\",\"username\":\"" + username + "\"}";
        $.when(myAjaxPost(bp + "Smserver/authorization/regist", jsonStr)).done(function (data) {
            if (data != null) {
                var success = data['success'];
                if (success == 'success') {
                    $("#registerResult").html("注册成功！请尽快缴费已获得使用权限吧。");
                } else {
                    $("#registerResult").html("注册失败！请再次尝试，如多次尝试均失败，试试联系技术支持吧。");
                }
            }
        });
    });

}

function loginAdmin() {
    var password = $("#password").val();
    if (isEmpty(password)) {
        showEditFail("您还没有输入密码。", $("#password"));
        return;
    }
    var jsonStr = "{\"password\":\"" + password + "\"}";
    $.when(myAjaxPost(bp + 'Smserver/authorization/loginAdmin', jsonStr)).done(function (data) {
        if (data != null) {
            var success = data['success'];
            if (success == 'success') {
                var htmlStr = data['htmlStr'];
                htmlStr = UrlDecode(htmlStr);
                $("#admin").html(htmlStr);
                var actionName = data['action'];
                grantAuthorizationActionName = bp + actionName;
            } else {

            }
        }
    });
}

// 获得验证 请求服务器
function authorizationRequest() {
    // 获得验证手机号码
    var phone = $("#authorizerphone").val();
    if (isValidPhone(phone) == false) {
        showEditFail("你必须输入正确的手机号码！", $("#authorizerphone"));
        return;
    }
    var username = $("#username").val();
    if (isEmpty(username)) {
        showEditFail("你必须输入你的用户名！", $("#username"));
        return;
    }
    var userphone = $("#userphone").val();
    if (isEmpty(userphone)) {
        showEditFail("你必须输入你的手机号！", $("#userphone"));
        return;
    }
    if (isValidPhone(userphone) == false) {
        showEditFail("你必须输入正确的手机号码！", $("#userphone"));
        return;
    }

    var jsonStr = "{\"phone\":\"" + phone
        + "\",\"userphone\":\"" + userphone
        + "\",\"username\":\"" + username + "\"}";
    // 创建后台任务
    $.when(myAjaxPost(bp + 'Smserver/authorization/requestAuthorization', jsonStr)).done(function (data) {
        if (data != null) {
            var success = data['success'];
            if (success == 'success') {
                var invalidReason = data['invalidReason'];
                alert(invalidReason);
                $("#invalidReason").html(invalidReason);
            }
        }
    });
    // 如果创建后台任务成功了，就定时刷新，展示授权结果
}

var grantAuthorizationActionName = '';
function authorizeAdmin() {
// 先将action进行替换
    $("#grantfrom").attr("action", grantAuthorizationActionName);
    // 然后提交
    var jsonStr = formToJson($('#grantfrom'));  //json字符串
    $.when(myAjaxPost(grantAuthorizationActionName, jsonStr)).done(function (data) {
        if (data != null) {
            var success = data['success'];
            if (success == 'success') {
                $("#grantresult").html("授权已成功！");
            } else {
                var failedObject = data['failedObject'];
                var failedReason = data['failedReason'];
                showEditFail(failedReason, $("#" + failedObject));
                $("#grantresult").html(failedReason);
            }
        }
    });
}


function formToJson(formObj) {
    var o = {};
    var a = formObj.serializeArray();
    $.each(a, function () {
        this.value = this.value.trim();
        if (o[this.name]) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || "null");
        } else {
            if ($("[name='" + this.name + "']:checkbox", formObj).length) {
                o[this.name] = [this.value];
            } else {
                o[this.name] = this.value || "null";
            }
        }
    });
    return JSON.stringify(o);
};