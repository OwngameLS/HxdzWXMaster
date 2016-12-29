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
    // 先获得状态
    $.when(myAjaxGet(bp + 'Smserver/settings/authorizedState')).done(function (data) {
        var htmlStr = '';
        if (data != null) {
            var authorizedState = data['authorizedState'];
            if(authorizedState.value == 'invalid'){
                var invalidReason = data['invalidReason'];
                $("#invalidReason").html(invalidReason);
                $("#authorizedModal").modal("show");
            }
        }
    });
}
