(function ($, w) {
    var $content = $('#management-cycle1');

    $(function(){

    });


    var sum_oput = ["", "", ""];

    var cycle_1_inputs = [
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
            },
            "input_sum": ["", "", "", "", ""]
        }
    ];

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
        var $management_tab_li = $('#management_tab li.active');
        var $inputs = (region || $content).find('input');
        var $pres = (region || $content).find('pre');

        function bind_input(lst) {
            $.each(lst, function(i, v) {

            });
        }

        if ($management_tab_li.index() === 0) {
            bind_input(cycle_1_inputs);
        } else if ($management_tab_li.index() === 1) {
            bind_input(cycle_2_inputs)
        } else {
            console.warn("find a lot of 'li'")
        }

    };

    return {
        "bind_input_change" : bind_input_change
    }

})(jQuery, window);