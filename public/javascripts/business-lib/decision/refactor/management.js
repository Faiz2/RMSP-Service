(function($, w){

    $(function(){
        $('#go-submit').click(function(){
            let $inputs = $('input');
            let sum = 0;
            let coach = [];
            $inputs.filter('[pharbers-type="能力辅导"]').map(function(val, input){
                sum += parseInt($(input).val());
                coach.push({
                    "personal": $(input).attr("name"),
                    "days": parseInt($(input).val())
                });
            });
            let assist = [];
            $inputs.filter('[pharbers-type="实地协访"]').map(function(val, input){
                sum += parseInt($(input).val());
                assist.push({
                    "personal": $(input).attr("name"),
                    "days": parseInt($(input).val())
                });
            });
            let construction = [];
            $inputs.filter('[pharbers-type="团队例会和团建"]').map(function(val, input){
                sum += parseInt($(input).val());
                construction.push({
                    "personal": $(input).attr("name"),
                    "days": parseInt($(input).val())
                });
            });
            let report = [];
            $inputs.filter('[pharbers-type="KPI 报告分析"]').map(function(val, input){
                sum += parseInt($(input).val());
                report.push({
                    "personal": $(input).attr("name"),
                    "days": parseInt($(input).val())
                });
            });
            let pr = [];
            $inputs.filter('[pharbers-type="行政工作"]').map(function(val, input){
                sum += parseInt($(input).val());
                pr.push({
                    "personal": $(input).attr("name"),
                    "days": parseInt($(input).val())
                });
            });
            let product = [];
            $inputs.filter('[pharbers-type="产品培训"]').map(function(val, input){
                product.push({
                    "personal": $(input).attr("name"),
                    "days": parseInt($(input).val())
                });
            });

            let json = {
                "user_id": $.cookie("user"),
                "uuid": $("input:hidden[name='uuid']").val(),
                "phase": parseInt($("input:hidden[name='phase']").val()),
                "management": [
                    {
                        "phase": parseInt($("input:hidden[name='phase']").val()),
                        "project_name": "能力辅导",
                        "project_code": parseInt("0"),
                        "apply": coach
                    },
                    {
                        "phase": parseInt($("input:hidden[name='phase']").val()),
                        "project_name": "实地协访",
                        "project_code": parseInt("1"),
                        "apply": assist
                    },
                    {
                        "phase": parseInt($("input:hidden[name='phase']").val()),
                        "project_name": "团队例会和团建",
                        "project_code": parseInt("2"),
                        "apply": construction
                    },
                    {
                        "phase": parseInt($("input:hidden[name='phase']").val()),
                        "project_name": "KPI 报告分析",
                        "project_code": parseInt("3"),
                        "apply": report
                    },
                    {
                        "phase": parseInt($("input:hidden[name='phase']").val()),
                        "project_name": "行政工作",
                        "project_code": parseInt("4"),
                        "apply": pr
                    },
                    {
                        "phase": parseInt($("input:hidden[name='phase']").val()),
                        "project_name": "产品培训",
                        "project_code": parseInt("5"),
                        "apply": product
                    },
                ]
            };
            let user_info = {
                "user_id": $.cookie("user"),
                "uuid": $("input:hidden[name='uuid']").val()
            };

            // console.info(JSON.stringify($.extend(json, f.parameterPrefixModule.conditions(user_info))))

            if(sum > 100) {
                f.alert.alert_warn("经理分配时间", "超出预设时间");
            } else if(sum < 0){
                f.alert.alert_warn("经理分配时间", "低于预设时间");
            } else {
                // TODO: Ajax
                f.ajaxModule.baseCall("/management/proceed", JSON.stringify($.extend(json, f.parameterPrefixModule.conditions(user_info))), 'POST', function (r) {
                    console.info(r);
                    f.alert.alert_success("消息", "模拟成功");
                });
            }
        })
    });

})(jQuery, window);