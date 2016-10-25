/**
 * Created by Administrator on 2016-9-20.
 * 访问服务器的一些公共方法
 */

function ajaxGet(url, isAsync){
    $.ajax({
        type: 'GET',
        url: bp + 'Smserver/functions',
        async: isAsync,// 是否为异步请求
        success: function (data) {
            return data;
            initTbodyOfFunctions(data['functions']);
        },

    });
}