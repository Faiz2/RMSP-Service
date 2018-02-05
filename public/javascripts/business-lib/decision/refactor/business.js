(function ($, w) {


     const next_validation = function(inputs_array) {
        let filter_empty = function(element) { return element.salesmen !== ""; };
        let filter_exist = function(element, value) { return element.salesmen === value; };
        let filter_inputs_array = inputs_array.filter(filter_empty);
        let personals = ["小青", "小白", "小木", "小兰", "小宋"];

        let result = personals.map(function(v, i){
            if(filter_inputs_array.find( elem => filter_exist(elem, v)) === undefined) {
                f.alert.alert_warn("缺少业务代表", "未曾选中：<span style='color: red;'>" + v + "</span> 业务代表。");
                return false;
            } else {
                return true;
            }
        });

        if (result.find(elem => elem === false) === false) {
            return false;
        } else {
            let flag = true;
            $.each(personals, function(n, p){
                let sum = 0;
                $.each(filter_inputs_array, function(i, v){
                    if(v.salesmen === p) {
                        sum += v.visit_hours.map(function(val, ind){ return val.prod_hours }).reduce(function(p, c, i, a){ return p + c });
                        if(sum > 100) {
                            f.alert.alert_warn("安排时间", "业务代表：<span style='color: red;'>" + v.salesmen + "</span> 超出预定时间。");
                            flag = false;
                            return flag;
                        }else if(sum <= 0) {
                            f.alert.alert_warn("安排时间", "业务代表：<span style='color: red;'>" + v.salesmen + "</span> 低于预定时间。");
                            flag = false;
                            return flag;
                        }
                    }
                });
            });
            return flag;
        }
     };

     const check_phase = function() {
         let json = JSON.stringify(f.parameterPrefixModule.conditions({
             "uuid": $('input:hidden[name="uuid"]').val(),
             "phase": parseInt($('input:hidden[name="phase"]').val())
         }));
         f.ajaxModule.baseCall('/phase/status', json, 'POST', function (r) {
             if(r.status === 'ok' && r.result.data.flag === 'ok') {
                 let $lis = $('#business_tab');
                 //parseInt($('input:hidden[name="phase"]').val())
                 if(r.result.data.inputs > 1) {
                     $lis.find('li').eq(1).show();
                 }
             }
         })
     };

    $(function() {

        $('#go-decision').click(function() {

            let $inputs = $('input');
            let $select = $('select');
            let obj = [];

            $("input:hidden[name='input']").map(function(val, input){
                let vv = $(input).val();
                let hospital_name = $inputs.filter('[hospital-code="'+ vv +'"]').filter('[name="budget"]').attr("hospital-name");
                let sales = $.map($inputs.filter('[hospital-code="'+ vv +'"]').filter('[name="prod_value"]'), function(sales, i){
                    return {
                        "prod_name": $(sales).attr("pharbers-type"),
                        "prod_value": parseFloat($(sales).val())
                    }
                });

                let visit = $.map($inputs.filter('[hospital-code="'+ vv +'"]').filter('[name="prod_hours"]'), function(sales, i){
                    return {
                        "prod_name": $(sales).attr("pharbers-type"),
                        "prod_hours": parseFloat($(sales).val())
                    }
                });

                obj.push({
                    "hosp_code": parseInt(vv),
                    "hosp_name": hospital_name,
                    "phase": parseInt($("input:hidden[name='phase']").val()),
                    "budget": parseFloat($inputs.filter('[hospital-code="'+ vv +'"]').filter('[name="budget"]').val()),
                    "sales": sales,
                    "salesmen": $select.filter('[hospital-code="'+ vv +'"]').filter('[name="salesmen"]').val(),
                    "visit_hours": visit
                })
            });
            if(next_validation(obj)) {
                let uuid = $("input:hidden[name='uuid']").val();
                let json = {
                    "user_id": $.cookie("user"),
                    "uuid": uuid,
                    "phase": parseInt($("input:hidden[name='phase']").val()),
                    "decision": obj
                };
                let user_info = {
                    "user_id": $.cookie("user"),
                    "uuid": uuid
                };

                f.ajaxModule.baseCall("/decision/proceed", JSON.stringify($.extend(json, f.parameterPrefixModule.conditions(user_info))), 'POST', function(r){
                    if(r.status === 'ok' && r.result.input_update === 'success') {
                        w.location = "/management/" + $('input:hidden[name="uuid"]').val() + "/" + $('input:hidden[name="phase"]').val();
                    }
                });
            }
        });

        // check_phase();
    });

})(jQuery, window);