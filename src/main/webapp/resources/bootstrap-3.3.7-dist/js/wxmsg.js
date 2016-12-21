/**
 * Created by Administrator on 2016-12-21.
 */

function getUsableFunctionsAndGroups(){
    $.when(myAjaxGet(bp + 'Smserver/tasks/askServer/usableFunctions')).done(function (data) {
        var functions = eval(data['functions']);
        var groups = eval(data['groups']);
        initFunctions(functions);
        initGroups(groups);

    }).fail(function () {
        $("#message").val("刚刚的查询失败了。");
        $("#myModal").modal("hide");
    });
}

function initFunctions(functions){
    var htmlStr = "";
    for(var i=0;i<functions.length;i++){// 三个一行
        if(i==0 || i%3 == 0){
            htmlStr += '<tr>';
        }
        htmlStr += '<td> <input type="checkbox" name="functions" value="'+functions[i].id+'">'+functions[i].name+'</td>';
        if(i != 0 && i%2 == 0){
            htmlStr += '</tr>';
        }
    }
    $("#functionsBody").html(htmlStr);
}

function initGroups(groups){
    var htmlStr = "";
    for(var i=0;i<groups.length;i++){// 三个一行
        if(i==0 || i%3 == 0){
            htmlStr += '<tr>';
        }
        htmlStr += '<td> <input type="checkbox" name="groups" value="'+groups[i]+'">'+groups[i]+'</td>';
        if(i != 0 && i%2 == 0){
            htmlStr += '</tr>';
        }
    }
    $("#groupsBody").html(htmlStr);
}

var currentSelectFunctionsIds;

function getSelectedFunctions(){
    currentSelectFunctionsIds = new Array();
    var aaa = $("input[name='functions']");
    aaa.each(function () {
        if ($(this).prop("checked")) {
            currentSelectFunctionsIds.push($(this).val());
        }
    });
}

var currentSelectGroupNames;

function getSelectedGroups(){
    currentSelectGroupNames = new Array();
    var aaa = $("input[name='groups']");
    aaa.each(function () {
        if ($(this).prop("checked")) {
            currentSelectGroupNames.push($(this).val());
        }
    });
}

function arrayToString(myArray) {
    var str = "";
    for(var i=0;i<myArray.length;i++){
        str += myArray[i];
        if(i+i < myArray.length){
            str += ",";
        }
    }
    return str;
}

// 清空内容
function emptyContents() {
    if (confirm("确认清空内容？")) {
        $("#message").val("");
    } else {
        return;
    }
}

function createTask() {
    // 1.先检查是否选择了功能和群组
    getSelectedFunctions();
    getSelectedGroups();
    var msg = $("#message").val();// 自定义消息
    var hasMsg = !isEmpty(msg);
    if(hasMsg == false && currentSelectFunctionsIds.length == 0){
        alert("你必须选择查询\'功能\'或 添加\'自定义消息\'。");
        return;
    }

    if(currentSelectGroupNames.length == 0){
        alert("你必须选择需要发送的\'群组\'。");
        return;
    }

    var sendTypeSms = $("#sendTypeSms").prop("checked");
    var sendTypeWx = $("#sendTypeWx").prop("checked");
    if(sendTypeSms == false && sendTypeWx == false){
        alert("你必须选择至少一种发送方式：微信、短信");
        return;
    }
    var sendtype = 0;
    if(sendTypeSms){
        if(sendTypeWx){
            sendtype = 2;
        }
    }else{
        if(sendTypeWx){
            sendtype = 1;
        }
    }

    var strFunctionids = arrayToString(currentSelectFunctionsIds);
    if(isEmpty(strFunctionids)){
        strFunctionids = "nofunctions";
    }

    if (confirm("确认发送吗？")) {
        var jsonStr = "{\"sendtype\":\"" + sendtype
            + "\",\"msg\":\"" + msg
            + "\",\"functionIds\":\"" + strFunctionids
            + "\",\"groupnames\":\"" + arrayToString(currentSelectGroupNames)
            + "\"}";
        $.when(myAjaxPost(bp + 'Smserver/tasks/clientqunfa/', jsonStr)).done(function (data) {
            showEditDone();
            hideEditFail();
            setTimeout(function () {
                location.reload();
            }, 2500);
        });
    } else {
        return;
    }


}