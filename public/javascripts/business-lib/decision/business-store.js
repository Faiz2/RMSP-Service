/**
 * Created by yym on 12/19/17.
 */
var business_store = (function ($,w) {
    var f = new Facade();

    var bn = function () {
        bnf();
        // setInterval(function () {
        //     bnf();
        //     console.log(1111);
        // }, 20000);
    };


    var bnf = function (){
        var res = cyc_business();
        var cyc =res[1];
        var cyc_content = res[0];
        var count = [1, 2];
        var  tmp = count.map(function (x) {
            return '.cyc'+cyc+'_hospital'+x+'_body'
        });
        console.log(cyc);
        if(cyc !== 0){
            var json = input_json(cyc_content);
            var token = $.cookie("user_token");
            json["user_token"] = token;
            json["phase"] = [cyc];
            store_onidle(tmp, 1000, JSON.stringify(json));
        }
    };

    var cyc_business = function () {
        if($('#p1_go_decision').attr('class').indexOf('disabled') < 0){
            return [$('#sum_promotion_budget-cycle1') ,1];
        }else if($('#p2_go_decision').attr('class').indexOf('disabled') < 0){
            return [$('#sum_promotion_budget-cycle2') ,2];
        }else {
            return ["" ,0];
        }
    };

    var input_json = function (content) {
        var json = {}
        var inputs = content.find('div input,pre');
        var selects = content.find('select');
        $.each(inputs, function (i, v) {
            var input = $(v);
            var key = input.attr("pharbers-type");
            json[key] = [input.val() === "" ? input.text().replace(",", "") : input.val()];
        });
        $.each(selects, function (i, v) {
            var select = $(v);
            var key = select.attr("pharbers-type");
            json[key] = [select.val()];
        });
        return json;
    };

    var store_onidle = function (arr ,time, data) {
        $.each(arr , function (i, v) {
            $(v).idle({
                onIdle: function(){
                    // debugger;
                    idle(data);
                },
                idle: time
            })
        })
    };

    var idle = function(json) {
        // console.info("a");
        setTimeout(function(){
            f.ajaxModule.baseCall('/store/input', json, "POST", function (r) {
                console.log(r);
            })
        }, 2000,2500,3000);
    };

    return {
        "input_json" : input_json,
        "cyc_business" : cyc_business,
        "store_onidle" : store_onidle,
        "bn" : bn
    }
})(jQuery, window);