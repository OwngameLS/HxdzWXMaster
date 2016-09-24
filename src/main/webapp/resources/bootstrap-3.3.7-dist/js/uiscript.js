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

