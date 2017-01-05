/**
 * Created by Administrator on 2016-12-28.
 */

var settingses = null;// 从服务器读取到的配置信息
var otherSettingsUIHtmlStr = ''; // 其他设置的HTMLStr
var beReferedSettings = null;// 被依赖的设置

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
    // console.log("getAuthorizedState...");
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
    // console.log("gotoAuthorizePage..");
    $('#authorize>p').trigger('click');
    $("#authorizedModal").modal("hide");
    // 添加未授权标识
    $("#unauth").show();
}



// 获得所有设置并按照规则展示出来
function getSettings() {
    $.when(myAjaxGet(bp + 'Smserver/settings/settings')).done(function (data) {
        var htmlStr = '';
        if (data != null) {
            settingses = data['settingses'];
            // console.log("settingses:" + settingses);
            if(settingses != null || settingses != undefined){
                initUIs();
            }
        }
    });
}

// 初始化所有的设置
function initUIs(){
    initAuthorizationUI(findSettingsesByName("authorizedState"));
    initUserInfoUI();
    initDatabaseUI();
    initWxMPUI();
    initOtherSettingsUI();
    initAddSettingsUI();
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
    initHtmlStrOfSettings("db", "db_");
}

// 初始化微信公众号信息UI
function initWxMPUI() {
    var settings = findSettingsesByName("wx_hasmp");
    if(settings != null || settings != undefined){
        if(settings.value == true || settings.value == 'true'){
            $("#isUseWXMP").prop("checked", true);
            initHtmlStrOfSettings("wx", "wx_");
        }
        // else{
        //     $("#isUseWXMP").prop("checked", false);
        //     $("[id^='wx_']").each(function () {
        //         $(this).attr("disabled", true);
        //     })
        // }
    }
}

// 初始化用来展示Settings的控件 htmlstr
function initHtmlStrOfSettings(startElementId, name_prefix){
    for(var i=0;i<settingses.length;){
        var settings = findSettingsesByName(name_prefix);
        if(settings != null || settings != undefined){
            var htmlStr = initSettingsHtmlStr(settings, true, true);
            $("#"+startElementId).after(htmlStr);
            startElementId = 'outterDiv_' + settings.name;
            i=0;// 找到了从头再次寻找
        }else {
            i++;
        }
    }
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

// 根据是否勾选使用微信公众号功能来初始化相关控件
function usingWXMP() {
    var isUseWXMP = $("#isUseWXMP").prop("checked");
    // console.log("usingWXMP:" + isUseWXMP);
    if(isUseWXMP == false){
        $("[id^='wx_']").each(function () {
            $(this).attr("disabled",true);
        });
    }else{
        // 重新获取相关属性
        // 先清除原来的
        var htmlStr = '<div id="wx" class="row bg-success"><div class="col-md-4 text-left"><img src="../../resources/bootstrap-3.3.7-dist/img/wechat.png">服务器配置</div>'
            +'<div class="col-md-6 text-left"></div></div>';
        $("#wxSettings").html(htmlStr);
        var jsonStr = "{\"name\":\"wx_\"}";
        $.when(myAjaxPost(bp + 'Smserver/settings/settingslikename', jsonStr)).done(function (data) {
            if (data != null) {
                settingses = data['settingses'];
                initWxMPUI();
            }
        });
    }
}


// 设置其他Settings的UI
function initOtherSettingsUI(){
    // console.log("initOtherSettingsUI...");
    otherSettingsUIHtmlStr = '';
    // 先找出被依赖的设置
    beReferedSettings = new Array();
    for(var i=0;i<settingses.length;){
        if(settingses[i].referto == 'self'){// 被别人依赖
            // console.log("find referto..."+ settingses[i].name);
            initRefertoSettings(settingses[i], i);
        }else{
            i++;
        }
    }
    // 剩下的都是没有依赖别人的了
    for(var i=0;i<settingses.length;i++){
        if(settingses[i].description == 'nodes'){// 不参加展示的设置
            continue;
        }
        var htmlStr = initSettingsHtmlStr(settingses[i], true, null);
        otherSettingsUIHtmlStr += htmlStr;
    }
    // 添加
    $("#other").after(otherSettingsUIHtmlStr);
}

// 初始化一组有依赖关系的设置UI
function initRefertoSettings(refered, index){
    // console.log("initRefertoSettings...");
    // 先添加到beReferedSettings队列中，在从原来的队列中删除
    beReferedSettings.push(refered);
    settingses.splice(index,1);
    var isUsed = refered.value;
    var htmlStr = initBeReferdSettingsUIHtmlStr(refered);
    var referedName = refered.name;
    
    for(var i=0;i<settingses.length;){
        if(settingses[i].referto == referedName){
            var htmlStr1 = initSettingsHtmlStr(settingses[i], false, isUsed);
            htmlStr += htmlStr1;
            // console.log("htmlStr1..."+ htmlStr1);
            // 删除这个位置的元素,避免多次循环
            settingses.splice(i,1);
        }else{
            i++;
        }
    }
    // 将构造好的添加到总的字符串中
    otherSettingsUIHtmlStr += htmlStr;
}

function initAddSettingsUI(){
    console.log("initAddSettingsUI...");
    // 先清除原来的
    var addDescription = $("#addDescription").val("");
    var addName = $("#addName").val("");
    var addValue = $("#addValue").val("");
    var addBerefered = $("#addBerefered").prop("checked", false);
    $("#addReferto").html("");
    // 构造新的
    var htmlStr = '<option value="no">无依赖</option>';
    for(var i=0;i<beReferedSettings.length;i++){
        htmlStr += '<option value="'+ beReferedSettings[i].name+'">'+beReferedSettings[i].description+'</option>';
    }
    $("#addReferto").html(htmlStr);
}

// 显示一个新增的settings
function addOtherSettingsUI(settings){
    console.log("addOtherSettingsUI : " + settings);
    var htmlStr = '';
    if(settings.referto == 'self'){// 被别人依赖
        beReferedSettings.push(settings);// 增加到可被依赖列表中
        htmlStr = initBeReferdSettingsUIHtmlStr(settings);
    }else if(settings.referto == 'no'){// 独立
        htmlStr = initSettingsHtmlStr(settings, true, null);
    }else{
        // 找到其依赖的属性的可用性
        var isUsed = $("#"+ settings.referto).prop("checked");
        htmlStr = initSettingsHtmlStr(settings, false, isUsed);
    }
    initAddSettingsUI();
    if(settings.referto != 'self' && settings.referto != 'no'){// 依赖于别人 就添加在别人下面
        $("#outterDiv_"+ settings.referto).after(htmlStr);
    }else{// 添加在父空间中的最后一个子节点位置之后
        $("#otherSettings").append(htmlStr);
    }

}

// 添加新属性
function addSettings() {
// 先读取值，判断合理性
    var addDescription = $("#addDescription").val();
    var addName = $("#addName").val();
    var addValue = $("#addValue").val();
    var addReferto = $("#addReferto option:selected").val();
    var addBerefered = $("#addBerefered").prop("checked");

    if(isEmpty(addDescription)){// 必须要添加描述
        showEditFail("必须要填写描述内容！", $("#addDescription"));
        return ;
    }
    if(isEmpty(addName)){// 必须要添加name
        showEditFail("必须要填写nanme内容！", $("#addName"));
        return ;
    }
    if(isEmpty(addValue) && addBerefered == false){// 必须要添加name
        showEditFail("必须要填写字段的值内容！", $("#addValue"));
        return ;
    }
    if(addReferto != 'no'){

    }
    if(addBerefered == true){// 选择了作为其他设置的依赖
        addValue = 'false';
        addReferto = 'self';
    }
    // 提交
    var jsonStr =  "{\"description\":\"" + addDescription
        + "\",\"name\":\"" + addName
        + "\",\"value\":\"" + addValue
        + "\",\"referto\":\"" + addReferto
        + "\",\"berefered\":\"" + addBerefered + "\"}";
    $.when(myAjaxPost(bp + 'Smserver/settings/add', jsonStr)).done(function (data) {
        if (data != null) {
            var success = data['success'];
            if(success == 'success'){
                addOtherSettingsUI(data['settings']);
            }
        }
    });
}

// 初始化被依赖的Settings htmlStr
function initBeReferdSettingsUIHtmlStr(beReferedSettings){
    var isUsed = beReferedSettings.value;
    var htmlStr = '<div class="row" id="outterDiv_'+ beReferedSettings.name +'">' +
        '<div class="col-md-4 text-right">'+beReferedSettings.description + '</div>' +
        '<div class="col-md-6 text-left"><input id="' + beReferedSettings.name+'" type="checkbox"';
    if(isUsed == true || isUsed == 'true'){
        htmlStr += ' checked';
    }
    htmlStr += '>是否启用</div>' + '</div>';
    return htmlStr;
}

// 初始化依赖与其他Settings 的Settings htmlStr
/**
 *
 * @param settings
 * @param isIndependent 是否独立
 * @param refertoValue 不独立，被依赖的设置是否启用
 * @returns {string}
 */
function initSettingsHtmlStr(settings, isIndependent, refertoValue) {
    var htmlStr = '<div class="row" id="outterDiv_'+ settings.name +'">' +
        '<div class="col-md-4 text-right">'+settings.description + '</div>' +
        '<div class="col-md-6 text-left"><input id="' + settings.name+'" class="form-control" value="' + settings.value +'"';
    if(isIndependent == false){// 不独立
        if(refertoValue == false || refertoValue == 'false'){
            htmlStr += ' disabled';
        }
    }
    htmlStr +='></div></div>';
    return htmlStr;
}