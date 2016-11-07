// UI控制的脚本

// 为UI控件实现提示闪烁的方法
function myAnimate(animateEl, animateCount, defaultStyle) {
    if (defaultStyle == undefined) {
        defaultStyle = '';
    }
    if (animateCount % 2 == 0) {
        animateEl.attr("style", "background:#F55");
    } else {
        animateEl.attr("style", defaultStyle);
    }
    animateCount--;
    if (animateCount != 0) {
        setTimeout(_myAnimate(animateEl, animateCount, defaultStyle), 400);
    }
}

// 为方便传参所用的方法
function _myAnimate(el, count, dfs) {
    return function () {
        myAnimate(el, count, dfs);
    }
}

// 对input UI 进行警示的方法
// 参数：el input id;warningInfos 警告消息
function myInputWarning(el, warningInfos) {
    el.attr("style", "background:#F55");
    mui.toast(warningInfos);
}

// 取消某个Input的警示颜色
function cancleWarning(el) {
    el.attr("style", "background:#FFF");
}

/**
 * 将一段内容处理成缩略词格式
 * @param source 源内容
 * @param limit 长度限制
 * @param description 描述
 */
function parseToAbbr(source, limit, description) {
    var abbr = '';// 缩略后
    if (description != null) {// 只需要在源上添加说明
        abbr = source;
        return '<abbr title="' + description + '">' + abbr + '</abbr>';
    }
    if (source.length > limit) {
        abbr = source.substring(0, limit) + '...';
        return '<abbr title="' + source + '">' + abbr + '</abbr>';
    } else {
        return source;
    }

}

function showEditDone() {
    $("#editDoneDiv").show(2000);
    $("#editDoneDiv").hide(2000);
}
function showEditFail(msg, el) {
    console.log("showEditFail " + el);
    if (el != null) {
        myAnimate(el, 8, el.attr("style"));
    }
    $("#failCause").html(msg);
    $("#editFailDiv").show(2000);

}
function hideEditFail() {
    $("#editFailDiv").hide(2000);
}

function isEmpty(value) {
    if (value == null || value == '' || value == undefined || value == 'null' || value == 'undefined') {
        return true;
    } else {
        return false;
    }
}

function getTimeNow() {
    var mydate = new Date();
    // mydate.getYear(); //获取当前年份(2位)
    // mydate.getFullYear(); //获取完整的年份(4位,1970-????)
    // mydate.getMonth(); //获取当前月份(0-11,0代表1月)
    // mydate.getDate(); //获取当前日(1-31)
    // mydate.getDay(); //获取当前星期X(0-6,0代表星期天)
    // mydate.getTime(); //获取当前时间(从1970.1.1开始的毫秒数)
    // mydate.getHours(); //获取当前小时数(0-23)
    // mydate.getMinutes(); //获取当前分钟数(0-59)
    // mydate.getSeconds(); //获取当前秒数(0-59)
    // mydate.getMilliseconds(); //获取当前毫秒数(0-999)
    // mydate.toLocaleDateString(); //获取当前日期
    var mytime = mydate.toLocaleDateString() + ' ' + mydate.toLocaleTimeString(); //获取当前时间
    return mytime;
    // mydate.toLocaleString( ); //获取日期与时间
}