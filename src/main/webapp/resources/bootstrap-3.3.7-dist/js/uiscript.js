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
function parseToAbbr(source, limit, description){
    var abbr = '';// 缩略后
    if(description != null){// 只需要在源上添加说明
        abbr = source;
        return '<abbr title="'+description+'">'+abbr+'</abbr>';
    }
    if(source.length > limit){
        abbr = source.substring(0, limit) + '...';
        return '<abbr title="'+source+'">'+abbr+'</abbr>';
    }else{
        return source;
    }

}
