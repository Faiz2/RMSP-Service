/**
 * Created by yym on 12/11/17.
 */
var arranged_time=/^.*_arranged_time_of_.*$/;
var arranged_promotional_budget = /^.*arranged_promotional_budget*$/;
var regexTest = function (rgx , str) {
    var res = rgx.test(str);
    return res;
};