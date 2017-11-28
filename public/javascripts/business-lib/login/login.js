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