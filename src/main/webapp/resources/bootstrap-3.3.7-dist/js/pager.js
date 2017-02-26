/**
 * Created by Administrator on 2017-2-24.
 */
class Pager {
    constructor(pager, queryCallback, dataDisplay) {
        if (pager == null) {
            this.pageSize = 20; // 每页显示多少条记录
            this.targetPage = 1; //目标第几页数据
            this.totalRecords = 0; // 一共多少条记录
            this.totalPages = 0; // 一共多少页记录
            this.dataList = null; //要显示的数据
            this.currentPage = 1;
        } else {
            this.pageSize = pager.pageSize; // 每页显示多少条记录
            this.targetPage = pager.targetPage; //目标第几页数据
            this.totalRecords = pager.totalRecords; // 一共多少条记录
            this.totalPages = pager.totalPages; // 一共多少页记录
            this.dataList = pager.dataList; //要显示的数据
            this.currentPage = pager.targetPage;
        }
        this.queryCallback = queryCallback;
        this.dataDisplay = dataDisplay;
        this.queryCallbackParam = null;
        this.dataDisplayParam = null;
    }

    uiDisplay(){
        this.initPageDiv();
        if(this.dataDisplayParam == null){
            console.log("if...");
            this.dataDisplay(this.dataList);
        }else{
            console.log("else...");
            this.dataDisplay(this.dataList, this.dataDisplayParam);
        }
    }

    initPageDiv() {
        console.log("initPageDiv");
        if (this == null || this.dataList == null || this.dataList.length == 0) {
            $("#pageSelectDiv").html('共查询到0条记录');
            return;
        }
        var htmlStr = '<nav onselectstart="return false;" aria-label="Page navigation">共<label id="totalRecord">' + this.totalRecords + '</label>条信息，' +
            '每页显示<input style="width: 50px;" id="pageSize" placeholder="' + this.pageSize + '" onblur="changePageSize()">条信息， ' +
            '<ul class="pagination" style="margin-bottom: -10px;">' +
            '<li> <a href="javascript:void(0)" onclick="gotoPage(-1)" aria-label="Previous"> <span aria-hidden="true">&laquo;</span> </a></li>';
        if (this.totalRecords != 0) {
            for (var i = 0; i < this.totalPages; i++) {
                if (i == (this.currentPage - 1)) {
                    htmlStr += '<li class="active">';
                } else {
                    htmlStr += '<li>';
                }
                htmlStr += '<a href="javascript:void(0)" onclick="gotoPage(' + (i + 1) + ')">' + (i + 1) + '</a></li>';
            }

        }
        htmlStr += '<li><a href="javascript:void(0)" onclick="gotoPage(-2)" aria-label="Next"><span aria-hidden="true">&raquo;</span></a></li></ul>';
        htmlStr += ' 共<label id="totalPage">' + this.totalPages + '</label>页，跳转到第<input style="width: 50px;" id="changePage" placeholder="' + this.targetPage + '">页，<button type="button" class="btn btn-primary btn-sm" onclick="gotoPage(\'changePage\')">跳转</button></nav>';
        $("#pageSelectDiv").html(htmlStr);
        console.log("initPageDiv end");
    }

    changePageSize() {
        // 先验证输入是否合理
        var ps = $("#pageSize").val();
        if (isInteger(ps) == false) {
            showEditFail("请输入正整数", $("#pageSize"));
            return;
        }
        if (ps > this.totalRecords) {
            ps = this.totalRecords;
        }
        this.pageSize = ps;
        $('.navbar-brand', window.parent.document).focus();
        this.queryCallback(this.queryCallbackParam);
    }

    // 根据页码操作得到目标页码
    gotoPage(page) {
        var targetPageTemp = 0;
        if (page == -1) {
            targetPageTemp = this.currentPage - 1;
            if (targetPageTemp <= 0) {
                targetPageTemp = 1;
            }
        } else if (page == -2) {
            targetPageTemp = this.currentPage + 1;
            if (targetPageTemp > this.totalPages) {
                targetPageTemp = this.totalPages;
            }
        } else if (page == "changePage") {
            targetPageTemp = $("#changePage").val();
            if (isEmpty(targetPageTemp) == true) {
                return -1;
            } else if (isInteger(targetPageTemp) == false) {
                showEditFail("请输入正整数", $("#changePage"));
                return -1;
            }
        } else {
            targetPageTemp = page;
        }
        this.targetPage = targetPageTemp;
        this.queryCallback(this.queryCallbackParam);
    }

    get pageSize() {
        return this._pageSize;
    }

    set pageSize(value) {
        this._pageSize = value;
    }

    get targetPage() {
        return this._targetPage;
    }

    set targetPage(value) {
        this._targetPage = value;
    }

    get totalRecords() {
        return this._totalRecords;
    }

    set totalRecords(value) {
        this._totalRecords = value;
    }

    get totalPages() {
        return this._totalPages;
    }

    set totalPages(value) {
        this._totalPages = value;
    }

    get dataList() {
        return this._dataList;
    }

    set dataList(value) {
        this._dataList = value;
    }

    get currentPage() {
        return this._currentPage;
    }

    set currentPage(value) {
        this._currentPage = value;
    }

    get queryCallbackParam() {
        return this._queryCallbackParam;
    }

    set queryCallbackParam(value) {
        this._queryCallbackParam = value;
    }

    get dataDisplayParam() {
        return this._dataDisplayParam;
    }

    set dataDisplayParam(value) {
        this._dataDisplayParam = value;
    }

}
