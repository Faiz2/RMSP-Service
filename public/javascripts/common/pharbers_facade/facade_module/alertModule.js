function Alert() {}

(function() {

    this.alert_success = function (title, content) {
        var t = title || "信息";
        var c = content || "成功" ;
        layer.alert(c, {
            skin: 'layui-layer-lan',
            title: t,
            icon: 1,
            closeBtn: 0,
            anim: 4
        });
    }

    this.alert_error = function (title, content) {
        var t = title || "信息";
        var c = content || "错误" ;
        layer.alert(c, {
            skin: 'layui-layer-lan',
            title: t,
            icon: 2,
            closeBtn: 0,
            anim: 4
        });
    }

    this.alert_warn = function (title, content) {
        var t = title || "信息";
        var c = content || "警告";
        layer.alert(c, {
            skin: 'layui-layer-lan',
            title: t,
            icon: 3,
            closeBtn: 0,
            anim: 4
        });
    }
}).call(Alert.prototype);