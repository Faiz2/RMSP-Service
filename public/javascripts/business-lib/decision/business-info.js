(function($, w){
    var f = new Facade();
    var $content = $('#sum_promotion_budget-cycle1');
    var $content2 = $('#sum_promotion_budget-cycle2');
    $(function () {
        $('#p1_go_decision').click(function() {
            click_next_button_cycle1();
        });
        $('#p2_go_decision').click(function() {
            click_next_button_cycle2();
        });
        var json = JSON.stringify(f.parameterPrefixModule.conditions({"cycle": "周期1"}));
        load_business_cycle1_info(json);
    });

    function load_business_cycle1_info (json) {
        f.ajaxModule.baseCall("/business_decision", json, "POST", function(r){
            append_html(r);
        }, function(e){console.info(e)})
    }

    var click_next_button_cycle1 = function() {
        var $inputs = $content.find('div input:not([disabled="true"]):not(.disabled)');
        var $select = $content.find('div select');
        return_cycle1_json($inputs, $select);
    }

    var return_cycle1_json = function(inputsObj, selectsObj) {
        var json = {"phase":[1]};
        $.each(inputsObj, function(i, v) {
            var input = $(v);
            var key = input.attr("pharbers-type");
            json[key] = [input.val()];
        });
        $.each(selectsObj, function(i, v){
            var select  = $(v);
            var key = select.attr("pharbers-type");
            json[key] = [select.val()];
        });
        next_save_cycle1_business_decision_json_data = json;
    }

    var click_next_button_cycle2 = function() {
        var $inputs = $content2.find('div input:not([disabled="true"]):not(.disabled)');
        var $select = $content2.find('div select');
        return_cycle2_json($inputs, $select);
    }

    var return_cycle2_json = function(inputsObj, selectsObj) {
        var json = {"phase":[2]};
        $.each(inputsObj, function(i, v) {
            var input = $(v);
            var key = input.attr("pharbers-type");
            json[key] = [input.val()];
        });
        $.each(selectsObj, function(i, v){
            var select  = $(v);
            var key = select.attr("pharbers-type");
            json[key] = [select.val()];
        });
        next_save_cycle2_business_decision_json_data = json;
    }

    // 判断切换周期
    var switch_cycle_page = function() {

    }

    var append_html = function(r) {
        $content.empty();
        $content2.empty();
        if (r.status === 'ok') {
            $content.append(r.result.data.reValSumPrompBudgetHtml);
            $content2.append(r.result.data.reValSumPrompBudgetHtml);
            $content.append(r.result.data.reValHospitalHtml);
            $content2.append(r.result.data.reValHospitalHtml);
        } else {
            $content.html('<h1 style="color: red">Error.</h1>');
            $content2.html('<h1 style="color: red">Error.</h1>');
        }
        var $hospitals =  $content.find('.hospital-num div:gt(0)');
        var $hospitals2 =  $content2.find('.hospital-num div:gt(0)');
        var $sum = $content.find('pre[pharbers-type="p1_total_promotional_budget"]');
        var $sum2 = $content2.find('pre[pharbers-type="p1_total_promotional_budget"]');
        $sum.text(f.thousandsModule.formatNum($sum.text().split(".")[0]));
        $sum2.text(f.thousandsModule.formatNum($sum2.text().split(".")[0]));
        $.each($hospitals, function(i, v){
            var $hnum = $(v).find('.noplaceholder').eq(0);
            var num = f.thousandsModule.formatNum($hnum.text().split(".")[0]);
            $hnum.text(num);
        });
        $.each($hospitals2, function(i, v){
            var $hnum = $(v).find('.noplaceholder').eq(0);
            var num = f.thousandsModule.formatNum($hnum.text().split(".")[0]);
            $hnum.text(num);
        });
    }
})(jQuery, window);