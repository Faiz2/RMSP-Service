var Facade = function () {
    this.validationModule = new ValidateHandler();
    this.ajaxModule =  new AjaxCall();
    this.cookieModule = new CookieHandler();
    this.parameterPrefix = new ParameterPrefix();
};
