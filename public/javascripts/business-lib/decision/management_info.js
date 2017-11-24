(function($, w){
    var f = new Facade();
    var $content = $('#management-cycle1');
    var $content2 = $('#management-cycle2');

    $(function () {

        $('#p1_go_decision').click(function() {
            click_submit_button_cycle1();
        });

        $('#p2_go_decision').click(function() {
            click_submit_button_cycle2();
        });

    });

    var click_submit_button_cycle1 = function() {
        var $inputs = $content.find('div input,pre');
        var rjv = return_cycle1_json($inputs);
        f.ajaxModule.baseCall("submit/submitdata", rjv, "POST", function(r){
            console.info(r)
        });
    };

    var click_submit_button_cycle2 = function() {
        var $inputs = $content2.find('div input,pre');
        var rjv = return_cycle2_json($inputs);
        f.ajaxModule.baseCall("submit/submitdata", rjv, "POST", function(r){
            console.info(r)
        });
    };

    var return_cycle1_json = function(inputsObj) {
        var temp =  next_save_cycle1_business_decision_json_data;
        // TDDO： 暂时写死，测试通过后进行用户token的解析
        var json = {"user_name": ["admin2"]};
        $.each(inputsObj, function(i, v) {
            var input = $(v);
            var key = input.attr("pharbers-type");
            json[key] = [input.val() === "" ? input.text().replace(",", "") : input.val()];
        });
        return JSON.stringify($.extend(temp, json));
    };

    var return_cycle2_json = function(inputsObj) {
        var temp =  next_save_cycle2_business_decision_json_data;
        var json = {"user_name": ["admin2"]};
        $.each(inputsObj, function(i, v) {
            var input = $(v);
            var key = input.attr("pharbers-type");
            json[key] = [input.val() === "" ? input.text().replace(",", "") : input.val()];
        });
        return JSON.stringify($.extend(temp, json));
    };


})(jQuery, window);