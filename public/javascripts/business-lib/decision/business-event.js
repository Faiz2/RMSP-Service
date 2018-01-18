var business_event = (function ($, w) {
    var $content = $('#sum_promotion_budget-cycle1');
    // 输入输出链表
    // 应修改为json配置文件
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
            "select": "p2_sr_hosp1",
            "inputs": ["p2_hosp1_worktime_1", "p2_hosp1_worktime_2", "p2_hosp1_worktime_3", "p2_hosp1_worktime_4"]
        },
        {
            "select": "p2_sr_hosp2",
            "inputs": ["p2_hosp2_worktime_1", "p2_hosp2_worktime_2", "p2_hosp2_worktime_3", "p2_hosp2_worktime_4"]
        },
        {
            "select": "p2_sr_hosp3",
            "inputs": ["p2_hosp3_worktime_1", "p2_hosp3_worktime_2", "p2_hosp3_worktime_3", "p2_hosp3_worktime_4"]
        },
        {
            "select": "p2_sr_hosp4",
            "inputs": ["p2_hosp4_worktime_1", "p2_hosp4_worktime_2", "p2_hosp4_worktime_3", "p2_hosp4_worktime_4"]
        },
        {
            "select": "p2_sr_hosp5",
            "inputs": ["p2_hosp5_worktime_1", "p2_hosp5_worktime_2", "p2_hosp5_worktime_3", "p2_hosp5_worktime_4"]
        },
        {
            "select": "p2_sr_hosp6",
            "inputs": ["p2_hosp6_worktime_1", "p2_hosp6_worktime_2", "p2_hosp6_worktime_3", "p2_hosp6_worktime_4"]
        },
        {
            "select": "p2_sr_hosp7",
            "inputs": ["p2_hosp7_worktime_1", "p2_hosp7_worktime_2", "p2_hosp7_worktime_3", "p2_hosp7_worktime_4"]
        },{
            "select": "p2_sr_hosp8",
            "inputs": ["p2_hosp8_worktime_1", "p2_hosp8_worktime_2", "p2_hosp8_worktime_3", "p2_hosp8_worktime_4"]
        },
        {
            "select": "p2_sr_hosp9",
            "inputs": ["p2_hosp9_worktime_1", "p2_hosp9_worktime_2", "p2_hosp9_worktime_3", "p2_hosp9_worktime_4"]
        },
        {
            "select": "p2_sr_hosp10",
            "inputs": ["p2_hosp10_worktime_1", "p2_hosp10_worktime_2", "p2_hosp10_worktime_3", "p2_hosp10_worktime_4"]
        }
    ];

    var f = new Facade();

    // 未封装
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
                        // clean_sum_input(region);
                        if ($input.val() === "") $input.val(0);
                        if ($(this).attr('pharbers-type').indexOf(v.type) !== -1) {
                            num = 0;
                            $.each(v.inputs, function(n, w){
                                var input_attr = '[pharbers-type="'+ w +'"]';
                                var $input = $inputs.filter(input_attr);
                                num += parseFloat($input.val());

                            });
                        }
                        $pre.empty().text(num);
                    });
                });
            });
        }

        function select_change(lst) {
            // 未封装
            $.each(lst, function(i, v){
                var select_attr = '[pharbers-type="'+ v.select +'"]';
                var $select = $selects.filter(select_attr);

                // 下拉框时间绑定
                $select.change(function() {
                    clean_sum_input(region, lst);
                    var that = this;
                    var num = 0;
                    var pre_attr = '[pharbers-pepole="'+ $(that).val() +'"]';
                    var $pre = $pres.filter(pre_attr);

                    $.each(lst, function(i, v2) {
                        var select_attr = '[pharbers-type="'+ v2.select +'"]';
                        var $select = $selects.filter(select_attr);
                        if($(that).attr('pharbers-type') !== $select.attr('pharbers-type')) {
                            if($(that).val() === $select.val()) {
                                $.each(v2.inputs, function(i, v3) {
                                    // console.info('---'+v3);
                                    var input_attr = '[pharbers-type="'+ v3+'"]';
                                    var $input = $inputs.filter(input_attr);
                                    num += parseFloat($input.val());
                                });
                            }
                            // else {
                            //     $.each(v2.inputs, function(i, v4) {
                            //         console.info(v4)
                            //         // num += parseInt(v4.val());
                            //     });
                            // }
                            // console.info("select = " + $select.val() + "->" + num)
                        } else {
                            $.each(v2.inputs, function(i, v5) {
                                // console.info('***'+v5);
                                var input_attr = '[pharbers-type="'+ v5+'"]';
                                var $input = $inputs.filter(input_attr);
                                num += parseFloat($input.val());
                            });
                        }
                    });

                    $pre.empty().text(num);
                });

                // input与下拉框绑定
                $.each(v.inputs, function (i, n) {
                    var input_attr = '[pharbers-type="'+ n +'"]';
                    var $input = $inputs.filter(input_attr);
                    $input.keyup(function() {
                        // clean_sum_input(region);
                        var pre_attr = '[pharbers-pepole="'+ $select.val() +'"]';
                        var $pre = $pres.filter(pre_attr);
                        var num = 0;
                        if ($input.val() === "") $input.val(0);
                        $.each(lst, function(i, n2){
                            var select_attr2 = '[pharbers-type="'+ n2.select +'"]';
                            var $select2 = $selects.filter(select_attr2);
                            if($select.attr('pharbers-type') !== $select2.attr('pharbers-type')) {
                                if ($select.val() === $select2.val()) {
                                    $.each(n2.inputs, function(i, n3) {
                                        var input_attr = '[pharbers-type="'+ n3+'"]';
                                        var $input = $inputs.filter(input_attr);
                                        num += parseFloat($input.val());
                                    });
                                    // console.info('---'+n2.inputs[0]);
                                }
                            }else {
                                $.each(n2.inputs, function(i, n4) {
                                    var input_attr = '[pharbers-type="'+ n4+'"]';
                                    var $input = $inputs.filter(input_attr);
                                    num += parseFloat($input.val());
                                });
                                // console.info('***'+n2.inputs[0]);
                            }
                        });
                        $pre.empty().text(num);
                    });
                });

            });

        }

        if ($business_tab_li.index() === 0) {
            var history = $.cookie("history");
            var c1_d = $.cookie("c1_decision");
            if(history === "0"&& c1_d === "0"){
                $.cookie("c1_decision", "1");
            }else {
                console.log("c1d");
                $.cookie("c1_decision", "1");
                setHistory(1, "#sum_promotion_budget-cycle1" );
            }
            input_change(cycle_1_table_input);
            select_change(cycle_1_table_aggregate_sum_input);
        } else if ($business_tab_li.index() === 1) {
            var history = $.cookie("history");
            var c2_d = $.cookie("c2_decision");
            if(history === "0" && c2_d ==="0"){
                console.log("noc2d");
                if(cycle1_status){
                    console.log("okc2d");
                    $.cookie("c2_decision", "1");
                }
            }else {
                console.log("c2d");
                $.cookie("c2_decision", "1");
                setHistory(2, "#sum_promotion_budget-cycle2" );
            }
            input_change(cycle_2_table_input);
            select_change(cycle_2_table_aggregate_sum_input);
        } else {
            console.warn("find a lot of 'li'")
        }

    }

    var setHistory = function (cyc, id) {
        var user = $.cookie("user");
        var json = {
            "phase" : [cyc+"_decision"],
            "user" : user
        };
        // console.log(json);
        f.ajaxModule.baseCall("/fetch/input", JSON.stringify(json), "POST", function (r) {
            if(r.status === 'ok'){
                var content = $(id);
                var res = r.result.input;
                var inputs = content.find('input');
                var selects = content.find('select');
                var pres = content.find('pre');
                $.each(inputs, function(i ,v){
                    var key = $(v).attr("pharbers-type");
                    var r = res[key];
                    if(r === undefined){
                        console.log(key)
                    }else {
                        $(v).val(r[0]);
                    }


                });
                $.each(selects, function (i, v) {
                    var key = $(v).attr("pharbers-type");
                    var r = res[key];
                    if(r === undefined){
                        console.log(key)
                    }else {
                        $(v).val(r[0]);
                    }


                });
                $.each(pres, function (i, v) {
                    var key = $(v).attr("pharbers-type");
                    var r = res[key];
                    if(r===undefined){
                        // console.log(key);
                        $(v).text(res["unknownError"])
                    }else {
                        $(v).text(r[0]);
                    }

                })
            }
        })
    };

    // 未封装
    var clean_sum_input = function(region, lst) {
        var $pres = (region || $content).find('pre');
        var $inputs = (region || $content).find('input');
        var $selects = (region || $content).find('select');
        var people = ['小宋', '小兰', '小木', '小白', '小青'];

        $.each(people, function(i, v) {
            var num = 0;
            var pre_attr = '[pharbers-pepole="'+ v +'"]';
            var $pre = $pres.filter(pre_attr);
            $.each(lst, function(i, v2){
                var select_attr = '[pharbers-type="'+ v2.select +'"]';
                var $select = $selects.filter(select_attr);
                // debugger;
                if($select.val() === v) {
                    $.each(v2.inputs, function(i, v3) {
                        var input_attr = '[pharbers-type="'+ v3 +'"]';
                        var $input = $inputs.filter(input_attr);
                        num += parseFloat($input.val());
                    });
                    $pre.empty().text(num);
                }else {
                    $pre.empty().text(num);
                }
            });
        });


    };

    return {
        "bind_input_change": bind_input_change

    }

})(jQuery, window);