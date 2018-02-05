/**
 * Created by yym on 2/5/18.
 */

(function ($, w) {
    var f = new Facade();
    $(function () {
        f.alert.choose_info("继续上一次操作", ["是", "否"], "是否需要继续上一次操作？", function () {
            var uuid = $("#pharbers_uuid").attr("pharbers-uuid");
            w.location.href = "/takelast/" + uuid;
        }, function () {
            w.location.href = '/takenew';
        })
    });
})(jQuery, window);