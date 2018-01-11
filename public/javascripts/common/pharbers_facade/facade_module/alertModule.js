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
    this.choose_info = function (title , options, message ,func_one, func_two) {
        var t = title || "选择";
        var arr = options || ["确定", "取消"];
        var msg = message || "请选择";
        //询问框
        var num = layer.confirm( msg , {
            title: t,
            closeBtn: 0,//关闭按钮
            btn: arr //按钮
        }, function(){
            func_one();
            layer.close(num);
        }, function(){
            func_two();
            layer.close(num);
        });

    }


}).call(Alert.prototype);