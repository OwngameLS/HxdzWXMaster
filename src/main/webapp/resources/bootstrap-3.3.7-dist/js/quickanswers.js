/**
 * Created by Administrator on 2016-10-27.
 */

var keynames = new Array();

function queryQuickAnswers() {
    var jsonStr = "{\"pageSize\":\"" + pagerKeywords.pageSize
        + "\",\"targetPage\":\"" + pagerKeywords.targetPage
        + "\"}";
    $.when(myAjaxPost(bp + 'Smserver/quickanswers/getall', jsonStr)).done(function (data) {
        if (data != null) {
            pagerKeywords = new Pager(data['quickanswers'], queryQuickAnswers, initUIOfQuickanswers, "pageSelectDivKeywords");
            pagerKeywords.uiDisplay();
        }
    });
}

// 初始化表格内容
function initUIOfQuickanswers(quickanswers) {
    var htmlStr = '';
    if (quickanswers != null || quickanswers != undefined) {
        for (var i = 0; i < quickanswers.length; i++) {
            htmlStr += '<div class="row">';
            htmlStr += '<div class="col-md-1 text-left">启用:<input type="checkbox" id="enable_' + quickanswers[i].id + '" ';
            htmlStr += 'onchange="changeEnable(' + quickanswers[i].id + ')"';
            if (quickanswers[i].enable == '1' || quickanswers[i].enable == 1) {
                htmlStr += ' checked';
            }
            htmlStr += '></div>';
            htmlStr += '<div class="col-md-3 text-left">关键字:<input id="keyname_' + quickanswers[i].id + '"'
                + 'value="' + quickanswers[i].keyname + '" ';
            if (quickanswers[i].enable == 0) {
                htmlStr += ' disabled'
            }
            htmlStr += '></div>';
            htmlStr += '<div class="col-md-3 text-left">返回结果:<input id="result_' + quickanswers[i].id + '"'
                + 'value="' + quickanswers[i].result + '" ';
            if (quickanswers[i].enable == 0) {
                htmlStr += 'disabled'
            }
            htmlStr += '></div>';
            htmlStr += '<div class="col-md-3 text-left">描述:<input id="description_' + quickanswers[i].id + '"'
                + 'value="' + quickanswers[i].description + '" ';
            if (quickanswers[i].enable == 0) {
                htmlStr += 'disabled'
            }
            htmlStr += '></div>';
            htmlStr += '<div class="col-md-1 text-left">' +
                '<a href="javascript:void(0)" onclick="editQuickanswers(' + quickanswers[i].id + ',\'update\');return false;" title="更新"><img src="' + bp + 'resources/bootstrap-3.3.7-dist/img/settings_done.png"></a>' +
                '<a href="javascript:void(0)" onclick="editQuickanswers(' + quickanswers[i].id + ',\'delete\');return false;" title="删除"><img src="' + bp + 'resources/bootstrap-3.3.7-dist/img/settings_delete.png"></a>' +
                '</div>';
            htmlStr += '</div>';
        }
    }
    $("#keywordsSettings").html(htmlStr);
    $("#myModal").modal("show");
    setTimeout(hideModal, 2000);
}

// 隐藏Modal
function hideModal() {
    $("#myModal").modal("hide");
}

function editQuickanswers(id, action) {
    if (action == 'delete') {
        if (confirm("确认删除？")) {
            doEdit(id, null, null, null, 0, 'delete');
        } else {
            return;
        }
    } else {
        // 先根据id找到其自身相关属性
        // 是否处于可编辑状态
        // var isEnable = $("#enable_" + id).prop("checked");
        // if (isEnable == false) {
        //     if (action == 'update') {
        //         showEditFail("请先确保它处于可编辑状态。", $("#enable_" + id));
        //         return;
        //     }
        // }
        var keyname = $("#keyname_" + id).val().trim();
        if (isEmpty(keyname)) {
            showEditFail("请输入内容.", $("#keyname_" + id));
            return;
        }
        var result = $("#result_" + id).val().trim();
        if (isEmpty(result)) {
            showEditFail("请输入内容.", $("#result_" + id));
            return;
        }
        var description = $("#description_" + id).val().trim();
        if (isEmpty(description)) {
            showEditFail("请输入内容.", $("#description_" + id));
            return;
        }
        // 检查关键字别重复
        $.when(validateDuplicateQuickanswers(id, keyname)).done(function () {
            // 可以更新
            var c = $("#enable_" + id).prop("checked");
            var enable = 0;
            if (c) {
                enable = 1;
            }
            doEdit(id, keyname, result, description, enable, 'update');
        }).fail(function (data) {
            showEditFail(data, $("#keyname_" + id));
        });
    }
}


function changeEnable(id) {
    var isChecked = $("#enable_" + id).prop("checked");
    if (isChecked) {
        $("#keyname_" + id).removeAttr("disabled");
        $("#result_" + id).removeAttr("disabled");
    } else {
        $("#keyname_" + id).attr("disabled", "disabled");
        $("#result_" + id).attr("disabled", "disabled");
    }
}

// 检查某个关键字是否已存在
function validateDuplicateQuickanswers(id, keyname) {
    // 要访问服务器
    var defer = $.Deferred();
    var jsonStr = "{\"id\":\"" + id
        + "\",\"keyname\":\"" + keyname + "\"}";
    $.when(myAjaxPost(bp + 'Smserver/quickanswers/duplicate', jsonStr)).done(function (data) {
        if (data != null) {
            var result = data['isDuplicate'];
            if (result == true) {
                defer.reject(data['similarKeywords']);
            } else {
                defer.resolve();
            }
        }
    }).fail(function () {
        var el;
        if (id < 0) {
            el = $("#addKeyname");
        } else {
            el = $("#keyname_" + id);
        }
        showEditFail("与服务器通讯失败，请稍后再试...", el);
        defer.reject("与服务器通讯失败，请稍后再试...");
    });
    return defer.promise();
}

// 新增关键字
function addQuickanswer() {
    // 先读取关键字名
    var keyname = $("#addKeyname").val();
    if (isEmpty(keyname)) {
        showEditFail("请输入内容！", $("#addKeyname"));
        return;
    }
    var result = $("#addResult").val();
    if (isEmpty(result)) {
        showEditFail("请输入内容！", $("#addResult"));
        return;
    }
    var decription = $("#addDescription").val();
    if (isEmpty(decription)) {
        showEditFail("请输入内容！", $("#addDescription"));
        return;
    }
    // 检查关键字重复
    $.when(validateDuplicateQuickanswers(-1, keyname)).done(function () {
        // 可以添加
        doEdit(-1, keyname, result, decription, 1, 'update');
    }).fail(function (data) {
        showEditFail(data, $("#addKeyname"));
    });
}


// 向服务器提交更新 并刷新页面
function doEdit(id, keyname, result, description, enable, action) {
    var jsonStr = "{\"id\":\"" + id
        + "\",\"keyname\":\"" + keyname
        + "\",\"result\":\"" + result
        + "\",\"description\":\"" + description
        + "\",\"enable\":\"" + enable
        + "\",\"action\":\"" + action + "\"}";
    console.log("jsonStr:" + jsonStr);
    $.when(myAjaxPost(bp + 'Smserver/quickanswers/update', jsonStr)).done(function (data) {
        if (data != null) {
            var success = data['success'];
            if (success == 'success') {
                // 提示已经完成
                if (id < 0) {
                    $("#addKeyname").val("");
                    $("#addResult").val("");
                    $("#addDescription").val("");
                }
                showEditDone();
                queryQuickAnswers();
            } else {
                var el;
                if (id < 0) {
                    el = $("#addKeyname");
                } else {
                    el = $("#keyname_" + id);
                }
                showEditFail("更新失败，请再次尝试...", el);
            }
        }
    });
}


