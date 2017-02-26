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
        showNear(el);
    }
    $("#failCause").html(msg);

}
function hideEditFail() {
    $("#editFailDiv").hide(2000);
}

function showNear(sObj) {
    var sourceObj = $(sObj);
    var offset = sourceObj.offset();
    var ofLeft = offset.left;
    var ofTop = offset.top;
    var targetObj = $("#editFailDiv");
    var cssStr = "padding:5px;display: none;width: 30%;margin:0 auto;text-align:center;z-index:5;position:absolute;left:"
        + (ofLeft + 80) + 'px;top:' + ofTop + 'px;';
    console.log("cssStr:" + cssStr);
    targetObj.attr("style", cssStr);
    targetObj.show(2000);
}

function isEmpty(value) {
    if (value == null || value == '' || value == undefined || value == 'null' || value == 'undefined') {
        return true;
    } else {
        return false;
    }
}

// 判断是不是大于0的整数
function isInteger(obj) {
    var o = Math.floor(obj);
    if (o == obj) { // ==== 就不行
        if (o >= 0) {
            return true;
        } else {
            return false;
        }
    } else {
        return false;
    }
}

function isValidPhone(phone) {
    // 非空 且 符合手机号规则
    return ((isEmpty(phone) == false) && /^1[3|4|5|8]\d{9}$/.test(phone));
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

function parseMillsToDate(timeinMills) {
    var time = getFormatDateByLong(timeinMills, "yyyy-MM-dd HH:mm:ss");
    return time;
}

// 时间转换
Date.prototype.Format = function (fmt) { //author: meizz
    var o = {
        "M+": this.getMonth() + 1,                 //月份
        "d+": this.getDate(),                    //日
        "H+": this.getHours(),                   //小时
        "m+": this.getMinutes(),                 //分
        "s+": this.getSeconds(),                 //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds()             //毫秒
    };
    if (/(y+)/.test(fmt))
        fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt))
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}


/**
 *转换long值为日期字符串
 * @param l long值
 * @param isFull 是否为完整的日期数据,
 * 为true时, 格式如"2000-03-05 01:05:04"
 * 为false时, 格式如 "2000-03-05"
 * @return 符合要求的日期字符串
 */
function getSmpFormatDateByLong(l, isFull) {
    return getSmpFormatDate(new Date(l), isFull);
}
/**
 *转换long值为日期字符串
 * @param l long值
 * @param pattern 格式字符串,例如：yyyy-MM-dd hh:mm:ss
 * @return 符合要求的日期字符串
 */
function getFormatDateByLong(l, pattern) {
    return getFormatDate(new Date(l), pattern);
}

/**
 *转换日期对象为日期字符串
 * @param date 日期对象
 * @param isFull 是否为完整的日期数据,
 * 为true时, 格式如"2000-03-05 01:05:04"
 * 为false时, 格式如 "2000-03-05"
 * @return 符合要求的日期字符串
 */
function getSmpFormatDate(date, isFull) {
    var pattern = "";
    if (isFull == true || isFull == undefined) {
        pattern = "yyyy-MM-dd hh:mm:ss";
    } else {
        pattern = "yyyy-MM-dd";
    }
    return getFormatDate(date, pattern);
}

/**
 *转换日期对象为日期字符串
 * @param l long值
 * @param pattern 格式字符串,例如：yyyy-MM-dd hh:mm:ss
 * @return 符合要求的日期字符串
 */
function getFormatDate(date, pattern) {
    if (date == undefined) {
        date = new Date();
    }
    if (pattern == undefined) {
        pattern = "yyyy-MM-dd hh:mm:ss";
    }
    return date.Format(pattern);
}

function StringToAscii(str) {
    return str.charCodeAt(0).toString(16);
}
function AsciiToString(asccode) {
    return String.fromCharCode(asccode);
}

// 将utf-8形式编码的内容进行解码
function UrlDecode(zipStr) {
    var uzipStr = "";
    for (var i = 0; i < zipStr.length; i++) {
        var chr = zipStr.charAt(i);
        if (chr == "+") {
            uzipStr += " ";
        } else if (chr == "%") {
            var asc = zipStr.substring(i + 1, i + 3);
            if (parseInt("0x" + asc) > 0x7f) {
                uzipStr += decodeURI("%" + asc.toString() + zipStr.substring(i + 3, i + 9).toString());
                i += 8;
            } else {
                uzipStr += AsciiToString(parseInt("0x" + asc));
                i += 2;
            }
        } else {
            uzipStr += chr;
        }
    }
    return uzipStr;
}

// 通过网页上的UI控件状态来判断是否授权了（前提是一开始就设置好啦）
function checkAuthorizationStateByUI() {
    var a = $("#unauth", window.parent.document);
    console.log("a:" + a);
    if (!a.is(':visible')) {//如果node是隐藏的则显示node元素，否则隐藏
        return true;
    } else {
        $('#authorize>p', window.parent.document).trigger('click');
        return false;
    }
}
