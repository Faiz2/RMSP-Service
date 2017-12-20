/**
 * Created by yym on 12/19/17.
 */
(function ($,w) {
    var f = new Facade();
    var phase = 0;
    $(function () {
        var content = cyc_content();
        input_json(content);
    });

    var cyc_content = function () {
        if($('#p1_go_decision').attr('class').indexOf('disabled') < 0){
            phase = 1;
            return $('#sum_promotion_budget-cycle1');
        }else if($('#p2_go_decision').attr('class').indexOf('disabled') < 0){
            phase = 2;
            return $('#sum_promotion_budget-cycle2');
        }
        console.log(phase);
    };

    var input_json = function (content) {
        var json = {"phase": [phase]};
        var inputs = content.find('div input,pre');
        var selects = content.find('select');
        $.each(inputs, function (i, v) {
            var input = $(v);
            var key = input.attr("pharbers-type");
            console.log(key);
            json[key] = [input.val() === "" ? input.text().replace(",", "") : input.val()];
        });
        $.each(selects, function (i, v) {
            var select = $(v);
            var key = select.attr("pharbers-type");
            json[key] = [select.val()];
        })
    }
})(jQuery, window)