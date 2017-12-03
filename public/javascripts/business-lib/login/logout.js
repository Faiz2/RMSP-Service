(function($, w) {
    var f = new Facade();
    $(function(){
        $('#logout').click(function() {
            f.cookieModule.cleanAllCookie();
            w.location.reload();
        });
    });
})(jQuery, window);