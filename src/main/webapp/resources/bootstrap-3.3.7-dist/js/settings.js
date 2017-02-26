/**
 * Created by Administrator on 2016-12-28.
 */

var settingses = null;// 从服务器读取到的配置信息
var otherSettingsUIHtmlStr = ''; // 其他设置的HTMLStr
var beReferedSettingses = null;// 被依赖的设置

// 获得所有设置并按照规则展示出来
function getSettings() {
    $.when(myAjaxGet(bp + 'Smserver/settings/settings')).done(function (data) {
        if (data != null) {
            settingses = data['settingses'];
            if (settingses != null || settingses != undefined) {
                initUIs();
            }
        }
    });
}

// 初始化所有的设置
function initUIs() {
    initAuthorizationUI();
    initUserInfoUI();
    initDatabaseUI();
    initWxMPUI();
    initOtherSettingsUI();
    initAddSettingsUI();
}


// 初始化验证状态UI
function initAuthorizationUI() {
    var authorizedState = findInSettingsesByName("authorizedState", true);
    if (authorizedState.value == 'valid') {
        $("#authorizestateimg").attr("src", "../../resources/bootstrap-3.3.7-dist/img/smiley.png");
        $("#authorizestatedes").html(" 您已经获得授权 ");
        $("#authorizestatedes").attr("style", "color: green");
        var validTime = findInSettingsesByName("validTime", true);
        var time = parseMillsToDate(Number(validTime.value));
        var htmlStr = '<h5>到期时间为:<br>' + time + '</h5>';
        $("#authorizestatedes").after(htmlStr);
        $("#getAuthorized").attr("style", "display:none");
    } else {
        $("#authorizestateimg").attr("src", "../../resources/bootstrap-3.3.7-dist/img/cry.png");
        $("#authorizestatedes").html(" 您尚未获得授权 ");
        $("#authorizestatedes").attr("style", "color: red");
        var invalidReason = findInSettingsesByName("invalidReason", true);
        $("#invalidReason").html(invalidReason.value);
        $("#getAuthorized").attr("style", "");
    }
}

// 初始化用户基本信息UI
function initUserInfoUI() {
    var username = findInSettingsesByName("username", true);
    $("#username").val(username.value);
    var userphone = findInSettingsesByName("userphone", true);
    $("#userphone").val(userphone.value);
    var phone = findInSettingsesByName("phone", true);
    $("#authorizerphone").val(phone.value);
}

// 初始化数据库信息UI
function initDatabaseUI() {
    initHtmlStrOfSettings("db", "db_", null);
}

// 初始化微信公众号信息UI
function initWxMPUI() {
    var settings = findInSettingsesByName("wx_hasmp", true);
    if (settings != null || settings != undefined) {
        initHtmlStrOfSettings("wx", "wx_", settings);
        if (settings.value == true || settings.value == 'true') {
            $("#wx_hasmp").prop("checked", true);
        }
    }
}

// 初始化用来展示Settings的控件 htmlstr
function initHtmlStrOfSettings(startElementId, name_prefix, referedSettings) {
    for (var i = 0; i < settingses.length;) {
        var settings = findInSettingsesByName(name_prefix, true);
        if (settings != null || settings != undefined) {
            var htmlStr = initSettingsHtmlStr(settings, false, referedSettings);
            $("#" + startElementId).after(htmlStr);
            startElementId = 'outterDiv_' + settings.name;
            i = 0;// 找到了从头再次寻找
        } else {
            i++;
        }
    }
}


/**
 * 通过settings的name在settingses中查找
 * @param name
 * @param needRemove 是否找到了就从队列中去除
 * @returns {*}
 */
function findInSettingsesByName(name, needRemove) {
    var mapper = new RegExp("^(" + name + ")", "gim");// 构造正则表达式
    for (var i = 0; i < settingses.length; i++) {
        if (mapper.test(settingses[i].name)) {
            var settings = settingses[i];
            if (needRemove) {
                settingses.splice(i, 1);// 从队列中删除
            }
            return settings;
        }
    }
}

// 通过settings的name在beReferedSettingses中查找
function findInReferedSettingsesByName(name) {
    for (var i = 0; i < beReferedSettingses.length; i++) {
        if ((beReferedSettingses[i].name) == name) {
            return beReferedSettingses[i];
        }
    }
}


// 根据是否勾选使用微信公众号功能来初始化相关控件
function usingWXMP() {
    var isUseWXMP = $("#wx_hasmp").prop("checked");
    console.log("usingWXMP:" + isUseWXMP);
    if (isUseWXMP == false) {
        $("[id^='wx_']").each(function () {
            if ($(this).prop("id") != "wx_hasmp") {
                $(this).attr("disabled", true);
            }
        });
    } else {
        // 重新获取相关属性
        // 先清除原来的
        $("[id^='outterDiv_wx_']").each(function () {
            $(this).remove();
        });
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
function initOtherSettingsUI() {
    // console.log("initOtherSettingsUI...");
    otherSettingsUIHtmlStr = '';
    // 先找出被依赖的设置
    beReferedSettingses = new Array();
    for (var i = 0; i < settingses.length;) {
        if (settingses[i].referto == 'self') {// 被别人依赖
            // console.log("find referto..."+ settingses[i].name);
            initRefertoSettings(settingses[i], i);
        } else {
            i++;
        }
    }
    // 剩下的都是没有依赖别人的了
    for (var i = 0; i < settingses.length; i++) {
        if (settingses[i].description == 'nodes') {// 不参加展示的设置
            continue;
        }
        var htmlStr = initSettingsHtmlStr(settingses[i], true, null);
        otherSettingsUIHtmlStr += htmlStr;
    }
    // 添加
    $("#other").after(otherSettingsUIHtmlStr);
}

// 初始化一组有依赖关系的设置UI
function initRefertoSettings(refered, index) {
    // 先添加到beReferedSettings队列中，在从原来的队列中删除
    beReferedSettingses.push(refered);
    settingses.splice(index, 1);
    var htmlStr = initSettingsHtmlStr(refered, true, null);
    var referedName = refered.name;
    for (var i = 0; i < settingses.length;) {
        if (settingses[i].referto == referedName) {
            var htmlStr1 = initSettingsHtmlStr(settingses[i], true, refered);
            htmlStr += htmlStr1;
            // 删除这个位置的元素,避免多次循环
            settingses.splice(i, 1);
        } else {
            i++;
        }
    }
    // 将构造好的添加到总的字符串中
    otherSettingsUIHtmlStr += htmlStr;
}

function initAddSettingsUI() {
    console.log("initAddSettingsUI...");
    // 先清除原来的
    var addDescription = $("#addDescription").val("");
    var addName = $("#addName").val("");
    var addValue = $("#addValue").val("");
    var addBerefered = $("#addBerefered").prop("checked", false);
    $("#addReferto").html("");
    // 构造新的
    var htmlStr = '<option value="no">无依赖</option>';
    for (var i = 0; i < beReferedSettingses.length; i++) {
        htmlStr += '<option value="' + beReferedSettingses[i].name + '">' + beReferedSettingses[i].description + '</option>';
    }
    $("#addReferto").html(htmlStr);
}

// 显示一个新增的settings
function addOtherSettingsUI(settings) {
    console.log("addOtherSettingsUI : " + settings);
    var htmlStr = '';
    if (settings.referto == 'self') {// 被别人依赖
        beReferedSettingses.push(settings);// 增加到可被依赖列表中
        htmlStr = initSettingsHtmlStr(settings, true, null);
    } else if (settings.referto == 'no') {// 独立
        htmlStr = initSettingsHtmlStr(settings, true, null);
    } else {
        // 找到其依赖的属性的可用性
        var beReferedSettings = findInReferedSettingsesByName(settings.referto);
        htmlStr = initSettingsHtmlStr(settings, true, beReferedSettings);
    }
    initAddSettingsUI();
    if (settings.referto != 'self' && settings.referto != 'no') {// 依赖于别人 就添加在别人下面
        $("#outterDiv_" + settings.referto).after(htmlStr);
    } else {// 添加在父空间中的最后一个子节点位置之后
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

    if (isEmpty(addDescription)) {// 必须要添加描述
        showEditFail("必须要填写描述内容！", $("#addDescription"));
        return;
    }
    if (isEmpty(addName)) {// 必须要添加name
        showEditFail("必须要填写nanme内容！", $("#addName"));
        return;
    }
    // 检查不要有冲突的命名
    var e = $("#" + addName);
    if (e != undefined) {
        showEditFail("填写的name与已有的配置属性冲突！", e);
        return;
    }

    if (isEmpty(addValue) && addBerefered == false) {// 必须要添加name
        showEditFail("必须要填写字段的值内容！", $("#addValue"));
        return;
    }
    if (addReferto != 'no') {

    }
    if (addBerefered == true) {// 选择了作为其他设置的依赖
        addValue = 'false';
        addReferto = 'self';
    }
    // 提交
    var jsonStr = "{\"description\":\"" + addDescription
        + "\",\"name\":\"" + addName
        + "\",\"value\":\"" + addValue
        + "\",\"referto\":\"" + addReferto
        + "\",\"berefered\":\"" + addBerefered + "\"}";
    $.when(myAjaxPost(bp + 'Smserver/settings/add', jsonStr)).done(function (data) {
        if (data != null) {
            var success = data['success'];
            if (success == 'success') {
                addOtherSettingsUI(data['settings']);
            }
        }
    });
}


/**
 * 初始化Settings 的Settings htmlStr
 * @param settings
 * @param deletable 是否可以删除
 * @param beReferedSettings 被依赖的Settings
 * @returns {string}
 */
function initSettingsHtmlStr(settings, deletable, beReferedSettings) {
    var refertoValue = settings.referto;
    var htmlStr = '<div class="row" id="outterDiv_' + settings.name + '">' +
        '<div class="col-md-4 text-right">' + settings.description + '</div>' +
        '<div class="col-md-3 text-left"><input id="' + settings.name + '"';

    if (refertoValue == 'self') {// 被别人依赖
        htmlStr += ' type="checkbox" onchange="changeEnable(\'' + settings.name + '\')"';
        var isUsed = settings.value;
        if (isUsed == true || isUsed == 'true') {
            htmlStr += ' checked> 是否启用';
        } else {
            htmlStr += ' > 是否启用';
        }
    } else if (refertoValue == 'no') {// 不依赖别人
        htmlStr += ' class="form-control" value="' + settings.value + '" >';
    } else {// 依赖别人
        htmlStr += ' class="form-control" value="' + settings.value + '" name="' + refertoValue + '"';// name属性控制其与绑定settings的联系
        // 先找到被依赖的settings是否可用
        if (beReferedSettings.value == 'false' || beReferedSettings.value == false) {// 不可用
            htmlStr += ' disabled';
        }
        htmlStr += ' >';
    }
    htmlStr += '</div>';
    // 添加删除和更新按钮
    htmlStr += '<div class="col-md-3 text-left">';
    htmlStr += '<a href="javascript:void(0)" onclick="editSettings(\'' + settings.name + '\',\'update\');return false;" title="更新"><img src="' + bp + 'resources/bootstrap-3.3.7-dist/img/settings_done.png" ></a> ';
    if (deletable == true) {// 可以删除
        htmlStr += '<a href="javascript:void(0)" onclick="editSettings(\'' + settings.name + '\',\'delete\');return false;" title="删除"><img src="' + bp + 'resources/bootstrap-3.3.7-dist/img/settings_delete.png"></a>';
    } else {
        htmlStr += '<a><img src="' + bp + 'resources/bootstrap-3.3.7-dist/img/settings_transparent.png" ></a>';
    }
    htmlStr += '</div></div>';
    return htmlStr;
}

function editSettings(name, action) {
    // 先根据name找到其自身相关属性
    var settingsElm = $("#" + name);
    var type = settingsElm.prop("type");
    var value = null;
    var isEnabled = false;
    if (type == 'checkbox') {// 是被依赖的属性
        value = settingsElm.prop("checked");
        if (action == 'delete') {// 删除元素
            if (confirm("确认删除？依赖它的所有设置项也将会被删除！")) {
                var jsonStr = "{\"action\":\"" + action
                    + "\",\"name\":\"" + name
                    + "\",\"value\":\"" + value + "\"}";
                doUpdateSettings(jsonStr, name, action, type);
            } else {
                return;
            }
        } else if (action == 'update') {
            var jsonStr = "{\"action\":\"" + action
                + "\",\"name\":\"" + name
                + "\",\"value\":\"" + value + "\"}";
            doUpdateSettings(jsonStr, name, action, type);
        }
    } else {// 是普通设置
        value = settingsElm.val();
        if (action == 'delete') {// 删除元素
            if (confirm("确认删除？")) {
                var jsonStr = "{\"action\":\"" + action
                    + "\",\"name\":\"" + name
                    + "\",\"value\":\"" + value + "\"}";
                doUpdateSettings(jsonStr, name, action, type);
            } else {
                return;
            }
        } else if (action == 'update') {
            var jsonStr = "{\"action\":\"" + action
                + "\",\"name\":\"" + name
                + "\",\"value\":\"" + value + "\"}";
            doUpdateSettings(jsonStr, name, action, type);
        }
    }
}

// 向服务器提交更新 并刷新页面
function doUpdateSettings(jsonStr, name, action, type) {
    $.when(myAjaxPost(bp + 'Smserver/settings/update', jsonStr)).done(function (data) {
        if (data != null) {
            var success = data['success'];
            if (success == 'success') {
                // 提示已经完成
                showEditDone();
                if (action == 'delete') {
                    if (type == 'checkbox') {// 删除与之关联的
                        var aaa = $("input[name=" + name + "]");
                        aaa.each(function () {
                            $(this).parent().parent().remove();
                        });
                    }
                    // 需要删除自身
                    $("#" + name).parent().parent().remove();
                }
            } else {
                showEditFail("删除失败！", $("#" + name));
            }
        }
    });
}

function showEditDone() {
    var a = $("#editDone", window.parent.document);
    a.show();
    setTimeout("hideEditDone()", 2000);
}

function hideEditDone() {
    var a = $("#editDone", window.parent.document);
    a.hide();
}


// 修改被依赖属性的选择状态后对应的控件状态随之改变
function changeEnable(name) {
    // 先根据name找到其自身相关属性
    var isChecked = $("#" + name).prop("checked");
    // 查找所有关联元素
    var aaa = $("input[name=" + name + "]");
    aaa.each(function () {
        if (isChecked) {
            $(this).removeAttr("disabled");
        } else {
            $(this).attr("disabled", "disabled");
        }
    });
}