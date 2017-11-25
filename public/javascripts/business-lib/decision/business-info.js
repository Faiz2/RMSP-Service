(function($, w){
    var f = new Facade();
    var $content = $('#sum_promotion_budget-cycle1');
    var $content2 = $('#sum_promotion_budget-cycle2');
    var $business_tab_li = $('#business_tab li');

    $(function () {
        $business_tab_li.click(function() {
            if($(this).find("a").attr("pharbers-status") === "false") {
                cycle_tab($(this).find("a").text());
            }
        });
        if(cycle1_status) {
            $business_tab_li.eq(1).find('a').click();
        } else {
            $business_tab_li.eq(0).find('a').click();
        }
        reload_cycle_status();
    });

    //当做完一次计算操作时，需要禁用一些操作与展示一些历史数据
    function reload_cycle_status() {
        disabled_button();
        $('#p1_go_decision:not(.disabled)').click(function() {
            click_next_button_cycle1();
        });
        $('#p2_go_decision:not(.disabled)').click(function() {
            click_next_button_cycle2();
        });
    }

    function disabled_button() {
        var $p1_btn = $('#p1_go_decision');
        var $p2_btn = $('#p2_go_decision');
        if(cycle1_status && $p1_btn.attr("class").indexOf('disabled') < 0) {
            $p1_btn.attr('class', $p1_btn.attr('class')+'disabled')
        }
        if(cycle2_status && $p2_btn.attr("class").indexOf('disabled') < 0) {
            $p2_btn.attr('class', $p2_btn.attr('class')+'disabled')
        }
    }

    function cycle_tab(cycle) {
        var json = JSON.stringify(f.parameterPrefixModule.conditions({"cycle": cycle}));
        switch (cycle) {
            case "周期1":
                load_business_cycle1_info(json);
                break;
            case "周期2":
                load_business_cycle2_info(json);
                break;
        }
    }

    function load_business_cycle1_info (json) {
        f.ajaxModule.baseCall("/business_decision", json, "POST", function(r){
            cycle1_append_html(r);
            w.business_event.bind_input_change($content);
        }, function(e){console.info(e)})
    }

    function load_business_cycle2_info (json) {
        f.ajaxModule.baseCall("/business_decision", json, "POST", function(r){
            cycle2_aapend_html(r);
            w.business_event.bind_input_change($content2);
        }, function(e){console.info(e)})
    }

    var click_next_button_cycle1 = function() {
        var $inputs = $content.find('div input,pre');
        var $select = $content.find('div select');
        return_cycle1_json($inputs, $select);
    };

    var return_cycle1_json = function(inputsObj, selectsObj) {
        console.info(inputsObj);
        var json = {"phase":[1]};
        $.each(inputsObj, function(i, v) {
            var input = $(v);
            var key = input.attr("pharbers-type");
            json[key] = [input.val() === "" ? input.text().replace(",", "") : input.val()];
        });
        $.each(selectsObj, function(i, v){
            var select  = $(v);
            var key = select.attr("pharbers-type");
            json[key] = [select.val()];
        });
        next_save_cycle1_business_decision_json_data = json;

        switch_left_page('management-decision');

        // switch_left_page('business-decision');

    };

    var click_next_button_cycle2 = function() {
        var $inputs = $content2.find('div input,pre');
        var $select = $content2.find('div select');
        return_cycle2_json($inputs, $select);
    };

    var return_cycle2_json = function(inputsObj, selectsObj) {
        var json = {"phase":[2]};
        $.each(inputsObj, function(i, v) {
            var input = $(v);
            var key = input.attr("pharbers-type");
            json[key] = [input.val() === "" ? input.text().replace(",", "") : input.val()];
        });
        $.each(selectsObj, function(i, v){
            var select  = $(v);
            var key = select.attr("pharbers-type");
            json[key] = [select.val()];
        });
        next_save_cycle2_business_decision_json_data = json;
        switch_left_page('management-decision')
    };

    // 判断切换周期
    // var switch_cycle_page = function() {
    //
    // };

    var switch_left_page = function(page) {
        disabled_button();
        var $li = $('ul[class="treeview-menu"] li a[pharbers-filter=' + page + ']');
        $li.click();
    };

    var cycle1_append_html = function(r) {
        $content.empty();
        if (r.status === 'ok') {
            $content.append(r.result.data.reValSumPrompBudgetHtml);
            $content.append(r.result.data.reValHospitalHtml);
        } else {
            $content.html('<h1 style="color: red">Error.</h1>');
        }
        var $hospitals =  $content.find('.hospital-num div:gt(0)');
        var $sum = $content.find('pre[pharbers-type="p1_total_promotional_budget"]');
        $sum.text(f.thousandsModule.formatNum($sum.text().split(".")[0]));
        $.each($hospitals, function(i, v){
            var $hnum = $(v).find('.noplaceholder').eq(0);
            var num = f.thousandsModule.formatNum($hnum.text().split(".")[0]);
            $hnum.text(num);
        });
        $business_tab_li.eq(0).find("a").attr("pharbers-status", true);

    };

    var cycle2_aapend_html = function(r) {
        if(cycle1_status)
            $('div').find('.cycle-style-controller').css('display', 'block')

        $content2.empty();
        if (r.status === 'ok') {
            $content2.append(r.result.data.reValSumPrompBudgetHtml);
            $content2.append(r.result.data.reValHospitalHtml);
        } else {
            $content2.html('<h1 style="color: red">Error.</h1>');
        }
        var $hospitals =  $content2.find('.hospital-num div:gt(0)');
        var $sum = $content2.find('pre[pharbers-type="p2_total_promotional_budget"]');
        $sum.text(f.thousandsModule.formatNum($sum.text().split(".")[0]));

        $.each($hospitals, function(i, v){
            var $hnum = $(v).find('.noplaceholder').eq(0);
            var num = f.thousandsModule.formatNum($hnum.text().split(".")[0]);
            $hnum.text(num);
        });
        $business_tab_li.eq(1).find("a").attr("pharbers-status", true);

    };

})(jQuery, window);