/**
 * Created by Administrator on 2016-11-1.
 */

var selectedIContacts = new Array();// 已经确认选中的人员
var newSelectIContacts = null;// 新操作时选中的人员
var anonymousId = -1;// 匿名者的id


// 展示联系人选择
function showContactsUI() {
    showEditDone();
    hideEditFail();
    // 显示联系人信息表格div
    showContactsDiv();
    // 初始化分组表格
    initTbodyOfGroups(false);
}

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
}

function showContactsDiv() {
    $("#contactsDiv").show(2000);
}

function hideContactsDiv() {
    $("#contactsDiv").hide(2000);
}

// 清空内容
function emptyContents(){
    if (confirm("确认清空内容？")) {
        $("#message").val("");
    } else {
        return;
    }
}


// contacts数组查找
function contactsFind(array, obj){
    for(var i=0;i<array.length; i++){
        if(obj.id == array[i].id){
            return i;
        }
    }
    return -1;
}
// 将添加的联系人的手机号唯一化，因为不同组里可能有同一个人
function uniqueContactPhone(){
    var phoneNumbers = new Array();
    for(var i=0; i<selectedIContacts.length;i++){
        phoneNumbers.push(selectedIContacts[i].phonenumber);
    }
    console.log("length before:" + phoneNumbers.length);
    phoneNumbers = $.unique(phoneNumbers);
    console.log("length after:" + phoneNumbers.length);
    // 整理成字符串
    var phoneStr = "";
    for(var i=0; i<phoneNumbers.length;i++){
        phoneStr = phoneStr + phoneNumbers[i];
        if( (i+1) < phoneNumbers.length){
            phoneStr = phoneStr + ',';
        }
    }
    return phoneStr;
}

function createTask() {
    var contents = $("#message").val();
    var name = "群发消息";
    var description = "主动发送消息给一部分人...";
    var receivers = uniqueContactPhone();
    // 判断不发空消息；发送人员不为空
    if(isEmpty(contents)){
        showEditFail("你必须要输入消息内容哦。",$("#message"));
        return;
    }
    if(isEmpty(receivers)){
        showEditFail("你必须要添加发送的对象啊。",$("#receivers"));
        return;
    }
    description = description + '(消息内容:'+contents+')';
    if (confirm("确认发送吗？")) {
        var jsonStr = "{\"name\":\"" + name
            + "\",\"description\":\"" + description
            + "\",\"contents\":\"" + contents
            + "\",\"receivers\":\"" + receivers + "\"}";
        $.when(myAjaxPost(bp + 'Smserver/task/create/', jsonStr)).done(function (data) {
            showEditDone();
        });
    } else {
        return;
    }


}