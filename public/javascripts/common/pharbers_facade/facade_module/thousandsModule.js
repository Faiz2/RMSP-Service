function Thousands() {}

(function() {
    this.formatNum = function(input) {
        var num = (input || 0).toString(), result = '';
        while (num.length > 3) {
            result = ',' + num.slice(-3) + result;
            num = num.slice(0, num.length - 3);
        }
        if (num) { result = num + result; }
        return result;
    };
}).call(Thousands.prototype);
