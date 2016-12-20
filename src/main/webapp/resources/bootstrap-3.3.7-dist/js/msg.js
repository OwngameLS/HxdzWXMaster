/**
 * Created by Administrator on 2016-11-1.
 */

var selectedIContacts = new Array();// 已经确认选中的人员
var newSelectIContacts = null;// 新操作时选中的人员
var anonymousId = -1;// 匿名者的id

// 联系人类
function selectedContact() {
    this.id;
    this.name;
    this.title;
    this.phonenumber;
}
// 从联系人选择控件上得到新选择的人员
function getNewSelectedContacts() {
    // 当前选中的联系人
    newSelectIContacts = new Array();
    var aaa = $("input[name='contactsCheckbox']");
    aaa.each(function () {
        if ($(this).prop("checked")) {
            var scontact = new selectedContact();
            // 获得父元素
            var p = $(this).parent();
            scontact.id = $(this).val();
            scontact.name = p.next().next().text();
            scontact.title = p.next().next().next().text();
            scontact.phonenumber = p.next().next().next().next().text();
            newSelectIContacts.push(scontact);
        }
    });
}

// 单独点击删除已经选中的联系人
function deleteSelectedContacts(id) {
    var tempArray = new Array();
    for (var j = 0; j < selectedIContacts.length; j++) {
        if (selectedIContacts[j].id != id) {
            tempArray.push(selectedIContacts[j]);
        }
    }
    selectedIContacts = tempArray;
    showSelectedContactsHtml();
}

function updateSelectedContacts(action) {
    console.log("updateSelectedContacts");
    // 在之前选中的Contact上操作
    for (var i = 0; i < newSelectIContacts.length; i++) {
        console.log("newSelectIContacts " + i);
        // 在原来的ids中查找
        var index = contactsFind(selectedIContacts, newSelectIContacts[i]);
        if (index != -1) {// 找到了
            console.log("we found one!");
            if (action == 'add') {
                // 不用处理
            } else if (action == 'remove') {
                // 删除找到的元素
                selectedIContacts.splice(index, 1);
            }
        } else {// 没找到
            if (action == 'add') {
                // 添加元素
                selectedIContacts.push(newSelectIContacts[i]);
            } else if (action == 'remove') {
                // 不用处理
            }
        }
    }
}

// 添加联系人ids action : add ;remove
function editContacts(action) {
    getNewSelectedContacts();
    updateSelectedContacts(action);
    // 刷新显示
    showSelectedContactsHtml();

}

// 清空已选
function removeAllContacts() {
    if (confirm("确认清空当前已选的人员？")) {
        selectedIContacts = new Array();
        showSelectedContactsHtml();
    } else {
        return;
    }
}
// 新增一名匿名人员
function addAnonymous() {
    // 先判断输入的手机号合法性
    var phone = $("#newphone").val();
    if (false == (phone && /^1[3|4|5|8]\d{9}$/.test(phone))) {
        alert("输入的手机号不合法，请检查！");
    } else {
        var t = new selectedContact();
        t.id == anonymousId--;
        t.name = "匿名";
        t.title = "未知";
        t.phonenumber = phone;
        selectedIContacts.push(t);
        $("#newphone").val("");
        showSelectedContactsHtml();
        alert("添加完成！");
    }
}


// 展示选中的人员
function showSelectedContactsHtml() {
    var htmlStr = '';
    for (var j = 0; j < selectedIContacts.length; j++) {
        if (j == 0 || j % 6 == 0) {
            htmlStr = htmlStr + '<div class="row">';
        }
        htmlStr = htmlStr + '<div class="col-md-2 text-center';
        if (j % 2 == 0) {
            htmlStr = htmlStr + ' bg-warning ';
        } else {
            htmlStr = htmlStr + ' bg-success ';
        }
        htmlStr = htmlStr + 'style="width: auto;display:inline">'
            + parseToAbbr(selectedIContacts[j].name, 0, selectedIContacts[j].title + "," + selectedIContacts[j].phonenumber)
            + '<img src="../../resources/bootstrap-3.3.7-dist/img/cross.png" onclick="deleteSelectedContacts(' + selectedIContacts[j].id + ')" /></div>';

        if (j % 6 == 5) {
            htmlStr = htmlStr + '</div><br>';
        }

    }
    $("#receivers").html(htmlStr);
    showEditDone();
}


// 清空内容
function emptyContents() {
    if (confirm("确认清空内容？")) {
        $("#message").val("");
    } else {
        return;
    }
}


// contacts数组查找
function contactsFind(array, obj) {
    for (var i = 0; i < array.length; i++) {
        if (obj.id == array[i].id) {
            return i;
        }
    }
    return -1;
}
// 将添加的联系人的手机号唯一化，因为不同组里可能有同一个人
function uniqueContactPhone() {
    var phoneNumbers = new Array();
    for (var i = 0; i < selectedIContacts.length; i++) {
        phoneNumbers.push(selectedIContacts[i].phonenumber);
    }
    console.log("length before:" + phoneNumbers.length);
    phoneNumbers = $.unique(phoneNumbers);
    console.log("length after:" + phoneNumbers.length);
    // 整理成字符串
    var phoneStr = "";
    for (var i = 0; i < phoneNumbers.length; i++) {
        phoneStr = phoneStr + phoneNumbers[i];
        if ((i + 1) < phoneNumbers.length) {
            phoneStr = phoneStr + ',';
        }
    }
    return phoneStr;
}

function createTask() {
    var contents = $("#message").val();
    var name = "群发消息";
    var description = "群发消息...";
    var receivers = uniqueContactPhone();
    // 判断不发空消息；发送人员不为空
    if (isEmpty(contents)) {
        showEditFail("你必须要输入消息内容哦。", $("#titleOfcontents"));
        return;
    }
    if (isEmpty(receivers)) {
        showEditFail("你必须要添加发送的对象啊。", $("#titleOfReceivers"));
        return;
    }
    var sendTypeSms = $("#sendTypeSms").prop("checked");
    var sendTypeWx = $("#sendTypeWx").prop("checked");
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
    description = description + '(消息内容:' + contents + ')';
    if (confirm("确认发送吗？")) {
        var jsonStr = "{\"name\":\"" + name
            + "\",\"description\":\"" + description
            + "\",\"contents\":\"" + contents
            + "\",\"sendtype\":\"" + sendtype
            + "\",\"receivers\":\"" + receivers + "\"}";
        $.when(myAjaxPost(bp + 'Smserver/tasks/create/', jsonStr)).done(function (data) {
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
// 获取所有方法
function useFunction() {
    $("#myModal").modal("show");
    $("#mbody").html('<img src="/resources/bootstrap-3.3.7-dist/img/loading.gif" style="width: 100px;height: 100px"/> 请稍后...');
    $.when(myAjaxGet(bp + 'Smserver/functions/getall')).done(function (data) {
        if (data != null) {
            var htmlStr = '';
            var functions = data['functions'];
            for (var j = 0; j < functions.length; j++) {
                if (j == 0 || j % 4 == 0) {
                    htmlStr = htmlStr + '<div class="row">';
                }
                htmlStr = htmlStr + '<div class="col-md-3 text-center" style="width: auto;display:inline">'
                    + '<input type="checkbox" id="func' + j + '" value="' + functions[j].id + '" ';
                if (functions[j].usable == 'no') {
                    htmlStr = htmlStr + ' disabled';
                }
                htmlStr = htmlStr + '>' + parseToAbbr(functions[j].name, 0, functions[j].keywords + "," + functions[j].description)
                    + '</div>';

                if (j % 4 == 3) {
                    htmlStr = htmlStr + '</div><br>';
                }
            }
            $("#mbody").html(htmlStr);
        }
    });
}

// 获得选中方法的结果
function getResults() {
    var funcIds = new Array();// 被选中的方法id
    var funcCheck = $("input[id*='func']");

    for (var i = 0; i < funcCheck.length; i++) {
        var isSelected = $("#func" + i).prop("checked");
        if (isSelected) {
            funcIds.push($("#func" + i).val());
        }
    }
    if (funcIds.length == 0) {
        $("#myModal").modal("hide");
        return;
    }
    $("#mbody").html('<img src="/resources/bootstrap-3.3.7-dist/img/loading.gif" style="width: 100px;height: 100px"/> 查询中，请稍后...');
    if (funcIds.length > 0) {
        // 提交给服务器
        var ids = "";
        for (var i = 0; i < funcIds.length; i++) {
            ids = ids + funcIds[i];
            if ((i + 1) < funcIds.length) {
                ids = ids + ",";
            }
        }
        var jsonStr = "{\"ids\":\"" + ids + "\"}";
        $.when(myAjaxPost(bp + 'Smserver/functions/getresults/', jsonStr)).done(function (data) {
            var contents = $("#message").val();
            if (isEmpty(contents)) {
                contents = data['results'];
            } else {
                contents = ";" + data['results'];
            }
            contents = contents + getTimeNow();
            $("#message").val(contents);
            $("#myModal").modal("hide");
        }).fail(function () {
            $("#message").val("刚刚的查询失败了。");
            $("#myModal").modal("hide");
        });
    }

}
