/**
 * Created by Administrator on 2016-11-1.
 */

var selectedIContacts = new Array();// 已经确认选中的人员
var newSelectIContacts = null;// 新操作时选中的人员


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
function selectedContact(){
    this.id;
    this.name;
    this.title;
    this.phonenumber;
}
// 从联系人选择控件上得到新选择的人员
function getNewSelectedContacts(){
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
function deleteSelectedContacts(id){
    var tempArray = new Array();
    for (var j = 0; j < selectedIContacts.length; j++) {
        if(selectedIContacts[j].id != id){
            tempArray.push(selectedIContacts[j]);
        }
    }
    selectedIContacts = tempArray;
    initSelectedContactsHtml();
}

function updateSelectedContacts(action){
    // 在之前选中的Contact上操作
    for (var i = 0; i < newSelectIContacts.length; i++) {
        // 在原来的ids中查找
        var index = selectedIContacts.indexOf(newSelectIContacts[i]);
        if (index != -1) {// 找到了
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
    if (newSelectIContacts.length == 0) {// 没有选择任何联系人
        return;
    }
    updateSelectedContacts(action);
    // 刷新显示
    initSelectedContactsHtml();
}

function initSelectedContactsHtml(){
    var htmlStr = '';
    for (var j = 0; j < selectedIContacts.length; j++) {
            htmlStr = htmlStr + '<div id="receiver'+ j + '" class="alert alert-success" style="width: auto;display:inline" role="alert">'
            + parseToAbbr(selectedIContacts[j].name,0, selectedIContacts[j].title+","+selectedIContacts[j].phonenumber)
            + '<img src="../../resources/bootstrap-3.3.7-dist/img/cross.png" onclick="deleteSelectedContacts('+selectedIContacts[j].id+')" /></div>';
        if ((j % 8) != 0) {
            htmlStr = htmlStr + '<br>';
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