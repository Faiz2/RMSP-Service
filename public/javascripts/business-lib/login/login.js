(function($, w){
    var f = new Facade();
    $(function() {
        $('#signin').click(function() {
            var user = JSON.stringify(f.parameterPrefixModule.conditions({"account": $('#user-name').val(), "password": md5($('#user-password').val())}));
            f.ajaxModule.baseCall("/user/authpwd", user, "POST", function(r) {
                if (r.status === 'ok') {
                    f.cookieModule.setCookie("user", r.result.user);
                    w.location = '/index';
                } else {
                    f.alert.alert_warn('登入信息', '登入失败，请检查用户名与密码是否正确！');
                }
            });
        });

        $(document).keyup(function(event){
            if(event.keyCode === 13){
                // $("#signin").trigger("click");
                $('#signin').click();
            }
        });
    });
})(jQuery, window);