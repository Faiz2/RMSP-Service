/**
 * Created by yym on 1/5/18.
 */
(function($, w){
    'use strict';
    $(function () {
        var f = new Facade();
        var log_status = $.cookie("log_status");
        if(log_status === "0"){
            w.store_info.store_choose();
            f.cookieModule.setCookie("log_status" ,"1");
        }
        window.onbeforeunload = function (event) {
            console.log(event);
            f.cookieModule.cleanAllCookie();
            window.location.href("/login");
        }
    });
})(jQuery, window);
