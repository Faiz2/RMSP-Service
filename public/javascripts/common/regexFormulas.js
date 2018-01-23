/**
 * Created by yym on 12/11/17.
 */
var arranged_time=/^.*_arranged_time_of_.*$/;

var arranged_promotional_budget = /^.*arranged_promotional_budget*$/;

var arranged_person_hospital = /^.*_sr_hosp.*$/;

var pro_budget_hosp = /^.*promotional_budget_hosp.*$/;

var hosp_sales_target = /^.*hosp\d{1,2}_sales_target.*$/;

var hosp_worktime = /^.*hosp\d{1,2}_worktime.*$/;

var hospR = /^hosp\d{1,2}$/;

var regexTest = function (rgx , str) {
    var res = rgx.test(str);
    return res;
};

var regexTestSome = function (rgxArr, str) {
    return rgxArr.some(function (rgx, index, arr) {
        return rgx.test(str);
    })
}