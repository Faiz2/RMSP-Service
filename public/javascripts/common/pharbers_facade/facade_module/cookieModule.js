
var CookieHandler = function () {};

CookieHandler.prototype.setCookie = function (key, value) {
    $.cookie(key, value, {path: "/"})
};

CookieHandler.prototype.cleanAllCookie = function (region) {
    var keys = document.cookie.match(/[^ =;]+(?=\=)/g);
    if(keys) {
        $.each(keys, function(i, v) {
            $.cookie(v, "", {"path": region, "expires": -1 });
        });
    };
};