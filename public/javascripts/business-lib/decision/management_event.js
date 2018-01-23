var management_event = (function ($, w) {
    var $content = $('#management-cycle1');
    var $management_tab_li = $('#management_tab li');
    var f = new Facade()
    $(function(){
    });


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
            "input_sum": ["p1_total_sales_training", "p1_total_field_work", "p1_total_team_meeting", "p1_total_kpi_analysis", "p1_total_admin_work"], // 纵列求和
            "time_allot": ["p1_total_management", "p1_flm_management", "p1_arranged_time_of_flm"] // 总求和
        };

    var cycle_2_inputs = {
            "ability_to_coach": { // 能力辅导
                "inputs": ["p2_sr1_sales_training", "p2_sr2_sales_training", "p2_sr3_sales_training", "p2_sr4_sales_training", "p2_sr5_sales_training"],
                "oputs": ["p2_total_sales_training", "p2_flm_sales_training"]
            },
            "field_association_to_visit": { // 实地协访问
                "inputs": ["p2_sr1_field_work", "p2_sr2_field_work", "p2_sr3_field_work", "p2_sr4_field_work", "p2_sr5_field_work"],
                "oputs": ["p2_total_field_work", "p2_flm_field_work"]
            },
            "party_building": { // 团建
                "inputs": ["p2_flm_team_meeting"],
                "oputs": ["p2_total_team_meeting", "p2_sr1_team_meeting", "p2_sr2_team_meeting", "p2_sr3_team_meeting", "p2_sr4_team_meeting", "p2_sr5_team_meeting"]
            },
            "kpi_report": { // KPI报告
                "inputs": ["p2_flm_kpi_analysis"],
                "oputs": ["p2_total_kpi_analysis"]
            },
            "administrative": { // 行政
                "inputs": ["p2_flm_admin_work"],
                "oputs": ["p2_total_admin_work"]
            },
            "input_sum": ["p2_total_sales_training", "p2_total_field_work", "p2_total_team_meeting", "p2_total_kpi_analysis", "p2_total_admin_work"], // 纵列求和
            "time_allot": ["p2_total_management", "p2_flm_management", "p2_arranged_time_of_flm"] // 总求和
        };

    function bind_input_change(region) {
        var $inputs = (region || $content).find('input');
        var $pres = (region || $content).find('pre');
        function bind_input(obj, key) {
            var lst = obj[key];
            $.each(lst.inputs, function(i, v) {
                var input_attr = '[pharbers-type="' + v + '"]';
                var $input = $inputs.filter(input_attr);
                $input.keyup(function() {
                    var num = 0;
                    $.each(lst.inputs, function(i, v2) {
                        var input_attr = '[pharbers-type="' + v2 + '"]';
                        var $input = $inputs.filter(input_attr);
                        if ($input.val() === "") $input.val(0);
                        num += parseInt($input.val());
                    });
                    $.each(lst.oputs, function(i, v2){
                        var pre_attr = '[pharbers-type="'+ v2 +'"]';
                        var $pre = $pres.filter(pre_attr);
                        $pre.empty().text(num);
                    });
                    sum(obj);
                });
            });
        }

        function sum(obj) {
            var lst = obj['input_sum'];
            var time_allot = obj['time_allot'];
            var num = 0;
            $.each(lst, function(i, v){
                var pre_attr = '[pharbers-type="'+ v +'"]';
                var $pre = $pres.filter(pre_attr);
                num += parseInt($pre.text());
            });
            $.each(time_allot, function(i, v){
                var pre_attr = '[pharbers-type="'+ v +'"]';
                var $pre = $pres.filter(pre_attr);
                $pre.empty().text(num);
            });
        }

        var active = $management_tab_li.filter('[class="active"]');
        if (active.index() === 0) {
            bind_input(cycle_1_inputs, 'ability_to_coach');
            bind_input(cycle_1_inputs, 'field_association_to_visit');
            bind_input(cycle_1_inputs, 'party_building');
            bind_input(cycle_1_inputs, 'kpi_report');
            bind_input(cycle_1_inputs, 'administrative');

        } else if (active.index() === 1) {
            bind_input(cycle_2_inputs, 'ability_to_coach');
            bind_input(cycle_2_inputs, 'field_association_to_visit');
            bind_input(cycle_2_inputs, 'party_building');
            bind_input(cycle_2_inputs, 'kpi_report');
            bind_input(cycle_2_inputs, 'administrative');
        } else {
            console.warn("find a lot of 'li'")
        }

    };

    return {
        "bind_input_change" : bind_input_change
    }
})(jQuery, window);