(function($, w){
    var f = new Facade();
    $(function () {
        var json = JSON.stringify(f.parameterPrefixModule.conditions({"cycle": "周期1"}));
        // load_management_cycle1_info(json);
    });

    // function load_management_cycle1_info (json) {
    //     f.ajaxModule.baseCall("/management_decision", json, "POST", function(r){
    //         var $content = $('#sum_promotion_budget-cycle1');
    //         // $content.empty();
    //         if (r.status === 'ok') {
    //             console.info(r.data)
    //             // $content.append(r.result.data.reValSumPrompBudgetHtml);
    //             // $content.append(r.result.data.reValHospitalHtml);
    //         } else {
    //             $content.html('<h1 style="color: red">Error.</h1>');
    //         }
    //         // var $hospitals =  $content.find('.hospital-num div:gt(0)');
    //         // var $sum = $('#p1_total_promotional_budget');
    //         // $sum.text(f.thousandsModule.formatNum($sum.text().split(".")[0]));
    //         // $.each($hospitals, function(i, v){
    //         //     var $hnum = $(v).find('.noplaceholder').eq(0);
    //         //     var num = f.thousandsModule.formatNum($hnum.text().split(".")[0]);
    //         //     $hnum.text(num);
    //         // });
    //     }, function(e){console.info(e)})
    // }

})(jQuery, window);