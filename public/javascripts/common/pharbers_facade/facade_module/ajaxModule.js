var AjaxCall = function () {};

AjaxCall.prototype.baseCall = function (url, data, type, successFun, errorFun, beforeFun, completeFun) {
    var errorFun = errorFun || function (e) {
            console.error(e)
    };
    var beforeFun = beforeFun || function () {};
    var completeFun = completeFun || function () {};
    $.ajax({
        type: type,
        url: url,
        dataType: "json",
        cache: false,
        data: data,
        contentType: "application/json,charset=utf-8",
        Accept: "application/json,charset=utf-8",
        success: function (data) {
            successFun(data)
        },
        error: function (e) {
            errorFun(e)
        },
        beforeSend : function () {
            beforeFun();
        },
        complete : function () {
            completeFun();
        }
    });

};