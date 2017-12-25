/**
 * Created by yym on 12/19/17.
 */
var web_store = (function ($, w) {
    // debugger
    var f = new Facade();

    var cyc_business = function () {
        if ($('#p1_go_decision').attr('class').indexOf('disabled') < 0) {
            return [$('#sum_promotion_budget-cycle1'), 1];
        } else if ($('#p2_go_decision').attr('class').indexOf('disabled') < 0) {
            return [$('#sum_promotion_budget-cycle2'), 2];
        } else {
            return ["", 0];
        }
    };
    var cyc_manage = function () {
        if ($('#p1_go_decision').attr('class').indexOf('action-button ') > 0) {
            return [$('#management-cycle1'), 1];
        } else if ($('p2_go_decision').attr('class').indexOf('disabled') > 0) {
            return [$('#management-cycle2'), 2];
        } else {
            return ["", 0];
        }
    };


    var input_json = function (content) {
        var json = {};
        var inputs = content.find('input');
        var pres = content.find('pre');
        // console.log(pres);
        var selects = content.find('select');
        $.each(inputs, function (i, v) {
            var input = $(v);
            var key = input.attr("pharbers-type");
            // json[key] = [input.val() === "" ? input.val().replace(",", "") : input.val()];
            json[key] = [input.val()];
        });
        $.each(pres, function (i, v) {
            var pre = $(v);
            var key = pre.attr("pharbers-type");
            if(key ==="p1_potential_sales_hosp6_2"){
                json["unknownError"] = [pre.text()];
            }else {
                json[key] = [pre.text()];
            }
            // console.log(key +":"+ pre.text());
            // json[key] = [pre.text() === "" ? pre.text().replace(",", "") : pre.text()];
        });
        $.each(selects, function (i, v) {
            var select = $(v);
            var key = select.attr("pharbers-type");
            json[key] = [select.val()];
        });
        return json;
    };

    var manage_json = function (content) {
        var json  = {};
        var inputs= $(content).find('input');
        var pres= $(content).find('pre');
        $.each(inputs, function (i, v) {
            var key = $(v).attr('pharbers-type');
            json[key] = $(v).val();
        });
        $.each(pres, function (i, v) {
            var key = $(v).attr('pharbers-type');
            // console.log(key +":"+ $(v).text());
            json[key] = $(v).text();
        });
        return json;
    };

    var manage_idle = function () {
        $('body').idle({
            onIdle: function () {
                var res = cyc_manage();
                var cyc = res[1];
                var cyc_content = res[0];
                // console.log(cyc);
                if (cyc !== 0) {
                    var json = manage_json(cyc_content);
                    var token = $.cookie("user_token");
                    json["user_token"] = token;
                    json["phase"] = [cyc+"_manage"];
                    // console.log(JSON.stringify(json));
                    idle_request(JSON.stringify(json), 2000);
                }
            },
            idle: 3000
        })
    }

    var business_idle = function () {
        $('body').idle({
            onIdle: function () {
                var res = cyc_business();
                var cyc = res[1];
                var cyc_content = res[0];
                // console.log(cyc);
                if (cyc !== 0) {
                    var json = input_json(cyc_content);
                    var token = $.cookie("user_token");
                    json["user_token"] = token;
                    json["phase"] = [cyc];
                    // console.log(JSON.stringify(json));
                    idle_request(JSON.stringify(json), 2000);
                }
            },
            idle: 3000
        })

    };
    var idle_request = function (json, time) {
        setTimeout(function () {
            f.ajaxModule.baseCall('/store/input', json, "POST", function (r) {
                console.log(r);
            })
        }, time);
    };
    return {
        "input_json": input_json,
        "cyc_business": cyc_business,
        "business_idle": business_idle,
        "manage_idle" : manage_idle
    }
})(jQuery, window);