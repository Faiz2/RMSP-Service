(function($, w){
    var f = new Facade();
    $(function () {
        var json = JSON.stringify(f.parameterPrefixModule.conditions({"cycle": "周期1"}));
        load_business_cycle1_info(json);
    });

    function load_business_cycle1_info (json) {
        f.ajaxModule.baseCall("/business_decision", json, "POST", function(r){
            var $content = $('#sum_promotion_budget-cycle1');
            var $content2 = $('#sum_promotion_budget-cycle2');
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
        }, function(e){console.info(e)})
    }
})(jQuery, window);