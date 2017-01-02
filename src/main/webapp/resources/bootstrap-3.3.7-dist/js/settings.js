/**
 * Created by Administrator on 2016-12-28.
 */

// 获得验证
function authorizationRequest() {
    // 获得验证手机号码
    var phone = $("#authorizerphone").val();
    if(isValidPhone(phone) == false){
        showEditFail("你必须输入正确的手机号码！", $("#authorizerphone"));
        return;
    }

    // 创建后台任务

    // 如果创建后台任务成功了，就定时刷新，展示授权结果
}

function getAuthorizedState(){
    console.log("getAuthorizedState...");
    // 先获得状态
    $.when(myAjaxGet(bp + 'Smserver/settings/authorizedState')).done(function (data) {
        var htmlStr = '';
        if (data != null) {
            var authorizedState = data['authorizedState'];
            if(authorizedState.value == 'invalid'){
                var invalidReason = data['invalidReason'];
                $("#invalidReason").html(invalidReason + "<br>");
                $("#authorizedModal").modal("show");
                countDown(5);
            }
        }
    });
}


function countDown(counts) {
    if(counts == 0){
        gotoAuthorizePage();
        return;
    }
    var htmlStr = counts + " 秒后将跳转到设置页面...";
    $("#countdowndesc").html(htmlStr);
    counts = counts - 1;
    setTimeout("countDown("+counts+")", 1000);
}


// 跳转到设置页面
function gotoAuthorizePage(){
    console.log("gotoAuthorizePage..");
    $('#authorize>p').trigger('click');
    $("#authorizedModal").modal("hide");
    // 添加未授权标识
    $("#unauth").show();
}

var settingses = null;

// 获得所有设置并按照规则展示出来
function getSettings() {
    $.when(myAjaxGet(bp + 'Smserver/settings/settings')).done(function (data) {
        var htmlStr = '';
        if (data != null) {
            settingses = data['settingses'];
            if(settingses != null || settingses != undefined){
                initAuthorizationUI(findSettingsesByName("authorizedState"));
                initUserInfoUI();
                initDatabaseUI();
            }
        }
    });
}


// 初始化验证状态UI
function initAuthorizationUI(settings) {
    if(settings.value == 'invalid'){
        $("#authorizestateimg").attr("src","../../resources/bootstrap-3.3.7-dist/img/cry.png");
        $("#authorizestatedes").html(" 您尚未获得授权 ");
        $("#authorizestatedes").attr("style","color: red");
        $("#getAuthorized").attr("style","");
    }else{
        $("#authorizestateimg").attr("src","../../resources/bootstrap-3.3.7-dist/img/smiley.png");
        $("#authorizestatedes").html(" 您已经获得授权 ");
        $("#authorizestatedes").attr("style","color: green");
        var validTime = findSettingsesByName("validTime");
        var time = parseMillsToDate(Number(validTime.value));
        var htmlStr = '<h5>到期时间为:<br>'+time+'</h5>';
        $("#authorizestatedes").after(htmlStr);
        $("#getAuthorized").attr("style","display:none");
    }
}

// 初始化用户基本信息UI
function initUserInfoUI() {
    var username = findSettingsesByName("username");
    $("#username").val(username.value);
    var phone = findSettingsesByName("phone");
    $("#authorizerphone").val(phone.value);
}

// 初始化数据库信息UI
function initDatabaseUI() {
    var startElementId = "db";
    for(var i=0;i<settingses.length;){
        var settings = findSettingsesByName("db_");
        if(settings != null || settings != undefined){
            var htmlStr = initHtmlStrOfSettings(startElementId, settings);
            $("#"+startElementId).after(htmlStr);
            startElementId = settings.name+'OutterDiv';
            i=0;// 找到了从头再次寻找
        }else {
            i++;
        }
    }
}

// 初始化用来展示Settings的控件 htmlstr
function initHtmlStrOfSettings(startElementId, settings){
    var htmlStr = '<div class="row" id="'
        + settings.name+'OutterDiv' +'"><div class="col-md-4 text-right">'+settings.description
        + '</div><div class="col-md-6 text-left"><input id="'
        + settings.name+'" class="form-control" value="'
        + settings.value +'"></div></div>';
    return htmlStr;

}


// 通过settings的name在settingses中查找
function findSettingsesByName(name) {
    var mapper = new RegExp("^("+name+")","gim");// 构造正则表达式
    for(var i=0;i<settingses.length;i++){
        if(mapper.test(settingses[i].name)){
            var settings = settingses[i];
            settingses.splice(i,1);
            return settings;
        }
    }
}