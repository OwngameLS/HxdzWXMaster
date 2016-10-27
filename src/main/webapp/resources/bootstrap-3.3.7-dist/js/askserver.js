/**
 * Created by Administrator on 2016-9-20.
 * 访问服务器的一些公共方法
 */

// ajaxGET 包装 通过 异步队列 deferred
function myAjaxGet(url){
    console.log("myAjaxGet here...");
    var defer = $.Deferred();
    $.ajax({
        type: 'GET',
        url: url
    }).done(function(data){
        defer.resolve(data);
    }).fail(function(data){
        defer.resolve(data);
    });
    return defer.promise();
}

function myAjaxPost(url, jsonStr){
    console.log("myAjaxPost here...");
    var defer = $.Deferred();
    $.ajax({
        type: 'POST',
        url: url,
        data: jsonStr,
        dataType: "json",
        contentType: "application/json",
    }).done(function(data){
        defer.resolve(data);
    }).fail(function(data){
        defer.resolve(data);
    });
    return defer.promise();
}