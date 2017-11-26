(function ($, w) {
    var $content = $('#management-cycle1');
    var $content2 = $('#management-cycle2');
    var $management_tab_li = $('#management_tab li.active');

    $(function(){
        // if ($management_tab_li.index() === 0) {
        //     bind_input_change($content);
        // } else if ($management_tab_li.index() === 1) {
        //     bind_input_change($content2);
        // } else {
        //     console.warn("find a lot of 'li'")
        // }

    });


    var sum_oput = ["", "", ""];

    var cycle_1_inputs = {
            "ability_to_coach": { // 能力辅导
                "inputs": ["p1_sr1_sales_training", "p1_sr2_sales_training", "p1_sr3_sales_training", "p1_sr4_sales_training", "p1_sr5_sales_training"],
                "oputs": ["p1_total_sales_training", "p1_flm_sales_training"]
            },
            "field_association_to_visit": { // 实地协访问
                "inputs": ["p1_sr1_field_work", "p1_sr2_field_work", "p1_sr3_field_work", "p1_sr4_field_work", "p1_sr5_field_work"],
                "oputs": ["p1_total_field_work", "p1_flm_field_work"]
            },
            "party_building": { // 团建
                "inputs": ["p1_flm_team_meeting"],
                "oputs": ["p1_total_team_meeting", "p1_sr1_team_meeting", "p1_sr2_team_meeting", "p1_sr3_team_meeting", "p1_sr4_team_meeting", "p1_sr5_team_meeting"]
            },
            "kpi_report": { // KPI报告
                "inputs": ["p1_flm_kpi_analysis"],
                "oputs": ["p1_total_kpi_analysis"]
            },
            "administrative": { // 行政
                "inputs": ["p1_flm_admin_work"],
                "oputs": ["p1_total_admin_work"]
            },
            "input_sum": ["p1_total_sales_training", "p1_total_field_work", "p1_total_team_meeting", "p1_total_kpi_analysis", "p1_total_admin_work"]
        };

    var cycle_2_inputs = [
        {
            "ability_to_coach": { // 能力辅导
                "inputs": ["", "", "", "", ""],
                "oputs": ["", ""]
            },
            "field_association_to_visit": { // 实地协访问
                "inputs": ["", "", "", "", ""],
                "oputs": ["", ""]
            },
            "party_building": { // 团建
                "inputs": [""],
                "oputs": ["", "", "", "", "", ""]
            },
            "kpi_report": { // KPI报告
                "inputs": [""],
                "oputs": [""]
            },
            "administrative": { // 行政
                "inputs": [""],
                "oputs": [""]
            }
        }
    ];

    var bind_input_change = function(region) {

        var $inputs = (region || $content).find('input');
        var $pres = (region || $content).find('pre');
        function bind_input(lst) {
            $.each(lst.inputs, function(i, v) {
                var input_attr = '[pharbers-type="' + v + '"]';
                var $input = $inputs.filter(input_attr);
                $input.keyup(function() {
                    var num = 0;
                    $.each(lst.inputs, function(i, v2) {
                        var input_attr = '[pharbers-type="' + v2 + '"]';
                        var $input = $inputs.filter(input_attr);
                        num += parseInt($input.val());
                    });
                    console.info(num)
                });
            });
        }

        if ($management_tab_li.index() === 0) {
            bind_input(cycle_1_inputs.ability_to_coach);
            console.info("fff")
            // bind_input(cycle_1_inputs.field_association_to_visit);
            // bind_input(cycle_1_inputs.party_building);
            // bind_input(cycle_1_inputs.kpi_report);
            // bind_input(cycle_1_inputs.administrative);
        } else if ($management_tab_li.index() === 1) {
            bind_input(cycle_2_inputs)
        } else {
            console.warn("find a lot of 'li'")
        }

    };

})(jQuery, window);