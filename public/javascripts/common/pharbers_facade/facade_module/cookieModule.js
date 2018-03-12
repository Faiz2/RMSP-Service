
let CookieHandler = function () {};

CookieHandler.prototype.cleanAllCookie = function (region) {
    let keys = document.cookie.match(/[^ =;]+(?=\=)/g);
    let that = this;
    $.each(keys, function (i, v) { that.clearCookie(v) });
};

CookieHandler.prototype.setCookie = function (cname, cvalue, expire) {
    let expiredays = expire || 1;
    let exp  = new Date();
    exp.setTime(exp.getTime() + expiredays*24*60*60*1000);
    document.cookie = cname + "="+ escape(cvalue) + ";expires=" + exp.toGMTString()+";path="+"/";
};

CookieHandler.prototype.clearCookie = function (name) {
    let exp = new Date();
    exp.setTime(exp.getTime() - 1);
    document.cookie = name + "=''"+";expires="+exp.toGMTString()+";path="+"/";
};
