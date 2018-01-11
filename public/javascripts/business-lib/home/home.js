/**
 * Created by yym on 1/5/18.
 */
(function($, w){
    'use strict';
    $(function () {
        var f = new Facade();
        console.log("ok home");
        var log_status = $.cookie("log_status");
        console.log(log_status);
        if(log_status === "0"){
            w.store_info.store_choose();
            f.cookieModule.setCookie("log_status" ,"1");
        }
    });
})(jQuery, window);
