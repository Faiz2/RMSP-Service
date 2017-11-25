var business_event = (function ($, w) {
    var $content = $('#sum_promotion_budget-cycle1');
    var $content2 = $('#sum_promotion_budget-cycle2');


    //输入输出链表周期1
    var cycle_1_table_input = [
        {"inputs":
            [
                "p1_promotional_budget_hosp1",
                "p1_promotional_budget_hosp2",
                "p1_promotional_budget_hosp3",
                "p1_promotional_budget_hosp4",
                "p1_promotional_budget_hosp5",
                "p1_promotional_budget_hosp6",
                "p1_promotional_budget_hosp7",
                "p1_promotional_budget_hosp8",
                "p1_promotional_budget_hosp9",
                "p1_promotional_budget_hosp10"
            ],
            "oput": "p1_arranged_promotional_budget",
            "type": "p1_promotional_budget"
        },
        {"inputs":
            [
                "p1_hosp1_sales_target_1",
                "p1_hosp2_sales_target_1",
                "p1_hosp3_sales_target_1",
                "p1_hosp4_sales_target_1",
                "p1_hosp5_sales_target_1",
                "p1_hosp6_sales_target_1",
                "p1_hosp7_sales_target_1",
                "p1_hosp8_sales_target_1",
                "p1_hosp9_sales_target_1",
                "p1_hosp10_sales_target_1"
            ],
            "oput": "p1_product1",
            "type": "sales_target_1"
        },
        {"inputs":
            [
                "p1_hosp1_sales_target_2",
                "p1_hosp2_sales_target_2",
                "p1_hosp3_sales_target_2",
                "p1_hosp4_sales_target_2",
                "p1_hosp5_sales_target_2",
                "p1_hosp6_sales_target_2",
                "p1_hosp7_sales_target_2",
                "p1_hosp8_sales_target_2",
                "p1_hosp9_sales_target_2",
                "p1_hosp10_sales_target_2"
            ],
            "oput": "p1_product2",
            "type": "sales_target_2"
        },
        {"inputs":
            [
                "p1_hosp1_sales_target_3",
                "p1_hosp2_sales_target_3",
                "p1_hosp3_sales_target_3",
                "p1_hosp4_sales_target_3",
                "p1_hosp5_sales_target_3",
                "p1_hosp6_sales_target_3",
                "p1_hosp7_sales_target_3",
                "p1_hosp8_sales_target_3",
                "p1_hosp9_sales_target_3",
                "p1_hosp10_sales_target_3"
            ],
            "oput": "p1_product3",
            "type": "sales_target_3"
        },
        {"inputs":
            [
                "p1_hosp1_sales_target_4",
                "p1_hosp2_sales_target_4",
                "p1_hosp3_sales_target_4",
                "p1_hosp4_sales_target_4",
                "p1_hosp5_sales_target_4",
                "p1_hosp6_sales_target_4",
                "p1_hosp7_sales_target_4",
                "p1_hosp8_sales_target_4",
                "p1_hosp9_sales_target_4",
                "p1_hosp10_sales_target_4"
            ],
            "oput": "p1_product4",
            "type": "sales_target_4"
        }
    ];
    var cycle_1_table_aggregate_sum_input = [
        {
            "select": "p1_sr_hosp1",
            "inputs": ["p1_hosp1_worktime_1", "p1_hosp1_worktime_2", "p1_hosp1_worktime_3", "p1_hosp1_worktime_4"]
        },
        {
            "select": "p1_sr_hosp2",
            "inputs": ["p1_hosp2_worktime_1", "p1_hosp2_worktime_2", "p1_hosp2_worktime_3", "p1_hosp2_worktime_4"]
        },
        {
            "select": "p1_sr_hosp3",
            "inputs": ["p1_hosp3_worktime_1", "p1_hosp3_worktime_2", "p1_hosp3_worktime_3", "p1_hosp3_worktime_4"]
        },
        {
            "select": "p1_sr_hosp4",
            "inputs": ["p1_hosp4_worktime_1", "p1_hosp4_worktime_2", "p1_hosp4_worktime_3", "p1_hosp4_worktime_4"]
        },
        {
            "select": "p1_sr_hosp5",
            "inputs": ["p1_hosp5_worktime_1", "p1_hosp5_worktime_2", "p1_hosp5_worktime_3", "p1_hosp5_worktime_4"]
        },
        {
            "select": "p1_sr_hosp6",
            "inputs": ["p1_hosp6_worktime_1", "p1_hosp6_worktime_2", "p1_hosp6_worktime_3", "p1_hosp6_worktime_4"]
        },
        {
            "select": "p1_sr_hosp7",
            "inputs": ["p1_hosp7_worktime_1", "p1_hosp7_worktime_2", "p1_hosp7_worktime_3", "p1_hosp7_worktime_4"]
        },{
            "select": "p1_sr_hosp8",
            "inputs": ["p1_hosp8_worktime_1", "p1_hosp8_worktime_2", "p1_hosp8_worktime_3", "p1_hosp8_worktime_4"]
        },
        {
            "select": "p1_sr_hosp9",
            "inputs": ["p1_hosp9_worktime_1", "p1_hosp9_worktime_2", "p1_hosp9_worktime_3", "p1_hosp9_worktime_4"]
        },
        {
            "select": "p1_sr_hosp10",
            "inputs": ["p1_hosp10_worktime_1", "p1_hosp10_worktime_2", "p1_hosp10_worktime_3", "p1_hosp10_worktime_4"]
        }
    ];

    var cycle_2_table_input = [
        {"inputs":
            [
                "p2_promotional_budget_hosp1",
                "p2_promotional_budget_hosp2",
                "p2_promotional_budget_hosp3",
                "p2_promotional_budget_hosp4",
                "p2_promotional_budget_hosp5",
                "p2_promotional_budget_hosp6",
                "p2_promotional_budget_hosp7",
                "p2_promotional_budget_hosp8",
                "p2_promotional_budget_hosp9",
                "p2_promotional_budget_hosp10"
            ],
            "oput": "p2_arranged_promotional_budget",
            "type": "p2_promotional_budget"
        },
        {"inputs":
            [
                "p2_hosp1_sales_target_1",
                "p2_hosp2_sales_target_1",
                "p2_hosp3_sales_target_1",
                "p2_hosp4_sales_target_1",
                "p2_hosp5_sales_target_1",
                "p2_hosp6_sales_target_1",
                "p2_hosp7_sales_target_1",
                "p2_hosp8_sales_target_1",
                "p2_hosp9_sales_target_1",
                "p2_hosp10_sales_target_1"
            ],
            "oput": "p2_product1",
            "type": "sales_target_1"
        },
        {"inputs":
            [
                "p2_hosp1_sales_target_2",
                "p2_hosp2_sales_target_2",
                "p2_hosp3_sales_target_2",
                "p2_hosp4_sales_target_2",
                "p2_hosp5_sales_target_2",
                "p2_hosp6_sales_target_2",
                "p2_hosp7_sales_target_2",
                "p2_hosp8_sales_target_2",
                "p2_hosp9_sales_target_2",
                "p2_hosp10_sales_target_2"
            ],
            "oput": "p2_product2",
            "type": "sales_target_2"
        },
        {"inputs":
            [
                "p2_hosp1_sales_target_3",
                "p2_hosp2_sales_target_3",
                "p2_hosp3_sales_target_3",
                "p2_hosp4_sales_target_3",
                "p2_hosp5_sales_target_3",
                "p2_hosp6_sales_target_3",
                "p2_hosp7_sales_target_3",
                "p2_hosp8_sales_target_3",
                "p2_hosp9_sales_target_3",
                "p2_hosp10_sales_target_3"
            ],
            "oput": "p2_product3",
            "type": "sales_target_3"
        },
        {"inputs":
            [
                "p2_hosp1_sales_target_4",
                "p2_hosp2_sales_target_4",
                "p2_hosp3_sales_target_4",
                "p2_hosp4_sales_target_4",
                "p2_hosp5_sales_target_4",
                "p2_hosp6_sales_target_4",
                "p2_hosp7_sales_target_4",
                "p2_hosp8_sales_target_4",
                "p2_hosp9_sales_target_4",
                "p2_hosp10_sales_target_4"
            ],
            "oput": "p2_product4",
            "type": "sales_target_4"
        }
    ];
    var cycle_2_table_aggregate_sum_input = [
        {
            "select": "p1_sr_hosp1",
            "inputs": ["p1_hosp1_worktime_1", "p1_hosp1_worktime_2", "p1_hosp1_worktime_3", "p1_hosp1_worktime_4"]
        },
        {
            "select": "p1_sr_hosp2",
            "inputs": ["p1_hosp2_worktime_1", "p1_hosp2_worktime_2", "p1_hosp2_worktime_3", "p1_hosp2_worktime_4"]
        },
        {
            "select": "p1_sr_hosp3",
            "inputs": ["p1_hosp3_worktime_1", "p1_hosp3_worktime_2", "p1_hosp3_worktime_3", "p1_hosp3_worktime_4"]
        },
        {
            "select": "p1_sr_hosp4",
            "inputs": ["p1_hosp4_worktime_1", "p1_hosp4_worktime_2", "p1_hosp4_worktime_3", "p1_hosp4_worktime_4"]
        },
        {
            "select": "p1_sr_hosp5",
            "inputs": ["p1_hosp5_worktime_1", "p1_hosp5_worktime_2", "p1_hosp5_worktime_3", "p1_hosp5_worktime_4"]
        },
        {
            "select": "p1_sr_hosp6",
            "inputs": ["p1_hosp6_worktime_1", "p1_hosp6_worktime_2", "p1_hosp6_worktime_3", "p1_hosp6_worktime_4"]
        },
        {
            "select": "p1_sr_hosp7",
            "inputs": ["p1_hosp7_worktime_1", "p1_hosp7_worktime_2", "p1_hosp7_worktime_3", "p1_hosp7_worktime_4"]
        },{
            "select": "p1_sr_hosp8",
            "inputs": ["p1_hosp8_worktime_1", "p1_hosp8_worktime_2", "p1_hosp8_worktime_3", "p1_hosp8_worktime_4"]
        },
        {
            "select": "p1_sr_hosp9",
            "inputs": ["p1_hosp9_worktime_1", "p1_hosp9_worktime_2", "p1_hosp9_worktime_3", "p1_hosp9_worktime_4"]
        },
        {
            "select": "p1_sr_hosp10",
            "inputs": ["p1_hosp10_worktime_1", "p1_hosp10_worktime_2", "p1_hosp10_worktime_3", "p1_hosp10_worktime_4"]
        }
    ];

    function bind_input_change(region) {
        var $business_tab_li = $('#business_tab li.active');
        var $inputs = (region || $content).find('input');
        var $selects = (region || $content).find('select');
        var $pres = (region || $content).find('pre');

        function input_change(lst) {
            $.each(lst, function(i, v) {
                var pre_attr = '[pharbers-type="' + v.oput + '"]';
                var $pre = $pres.filter(pre_attr);
                var num = 0;
                $.each(v.inputs, function(j ,k){
                    var input_attr = '[pharbers-type="'+ k +'"]';
                    var $input = $inputs.filter(input_attr);
                    $input.keyup(function(){
                        if ($input.val() === "") $input.val(0);
                        if ($(this).attr('pharbers-type').indexOf(v.type) !== -1) {
                            num = 0;
                            $.each(v.inputs, function(n, w){
                                var input_attr = '[pharbers-type="'+ w +'"]';
                                var $input = $inputs.filter(input_attr);
                                num += parseInt($input.val());
                            });
                        }
                        $pre.empty().text(num);
                    });
                });
            });
        }

        function select_change(lst) {

            $.each(lst, function(i, v){
                var select_attr = '[pharbers-type="'+ v.select +'"]';
                var $select = $selects.filter(select_attr);
                $select.change(function() {
                    var that = this;
                    var pre_attr = '[pharbers-pepole="'+ $(that).val() +'"]';
                    var $pre = $pres.filter(pre_attr);
                    var num = 0;

                    $.each(lst, function(j, k) {
                        var select_attr = '[pharbers-type="'+ k.select +'"]';
                        var $select = $selects.filter(select_attr).not('[pharbers-type="'+ $(that).attr("pharbers-type") +'"]');
                        if ($select.val() === $(that).val()) {
                            $.each(lst, function(l, m){
                                if ($(that).attr('pharbers-type') === m.select || $select.attr('pharbers-type') === m.select) {
                                    $.each(m.inputs, function(g, c) {
                                        var input_attr = '[pharbers-type="'+ c +'"]';
                                        var $input = $inputs.filter(input_attr);
                                        num += parseInt($input.val());
                                    });
                                }
                            });
                        } else {
                            console.info(k.inputs)
                            // console.info(k.inputs)
                            // $.each(k.inputs, function(l, m){
                            //     var input_attr = '[pharbers-type="'+ m +'"]';
                            //     var $input = $inputs.filter(input_attr);
                            //     num += parseInt($input.val());
                            // });
                        }
                    });
                    $pre.empty().text(num);
                });
            });
        }

        if ($business_tab_li.index() === 0) {
            input_change(cycle_1_table_input);
            select_change(cycle_1_table_aggregate_sum_input);
        } else if ($business_tab_li.index() === 1) {
            input_change(cycle_2_table_input);
        } else {
            console.warn("find a lot of 'li'")
        }
    }

    return {
        "bind_input_change": bind_input_change
    }

})(jQuery, window);