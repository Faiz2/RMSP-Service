(function($, w) {
    var selected_salemman = "";
    // TODO: 从2018年3月12日后，暂时封印ES6的写法, IE11一下不支持
    // Persion Pie
    const getPersionPieData = function(percent) {
        return [{
            value: percent,
            itemStyle: {
                normal: {
                    color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                            "offset": 0,
                            "color": '#4930EF'
                        }, {
                            "offset": 1,
                            "color": '#00FFC7'
                        }]),
                    shadowBlur: 10,
                    shadowColor: '#1195C4'
                }
            }
        }, {
            value: 100 - percent,
            itemStyle: {
                normal: {
                    color: 'transparent'
                }
            }
        }];
    };

    // Total Budget Bar
    const getTotalBudgetData = function(percent) {
        return [{
            value: percent,
            itemStyle: {
                normal: {
                    barBorderRadius: [20, 25, 25, 20],
                    color: {
                        type: 'bar',
                        colorStops: [{
                            offset: 0,
                            color: '#00FFC7' // 0% 处的颜色
                        }, {
                            offset: 1,
                            color: '#4930EF' // 100% 处的颜色
                        }],
                        globalCoord: false // 缺省为 false
                    }
                }
            }
        }]
    };

    // Allot Time Pie
    const getAllotTimePieData = function(percents) {
        return [
            {value:30, name:'协助拜访'},
            {value:31, name:'能力辅助'},
            {value:3, name:'例会/团建'},
            {value:8, name:'KPI分析'},
            {value:12, name:'行政工作'},
            {value:16, name:'未分配'}
        ];
    }

    // salesman分配时间
    const setPersonData = function(id, p) {
        let percent = 70;
        let personPie = echarts.init(document.getElementById(id));
        let option = {
            // backgroundColor: '#474B5A',
            title: {
                text: percent + "天",
                x: 'center',
                y: 'center',
                textStyle: {
                    color: '#98a0c4',
                    // fontWeight: 'bolder',
                    fontSize: 24,
                }
            },
            series: [{
                    type: 'pie',
                    radius: ['85%', '100%'],
                    silent: true,
                    label: {
                        normal: {
                            show: false,
                        }
                    },
                    data: [{
                        value: 1,
                        itemStyle: {
                            normal: {
                                color: '#454752',
                                shadowBlur: 10,
                                shadowColor: '#454752',
                            }
                        }
                    }],
                    animation: false
                },{
                    type: 'pie',
                    radius: ['85%', '100%'],
                    silent: true,
                    label: {
                        normal: {
                            show: false,
                        }
                    },
                    data: [{
                        value: 1,
                        itemStyle: {
                            normal: {
                                color: '#454752',
                                shadowBlur: 10,
                                shadowColor: '#454752'
                            }
                        }
                    }],
                    animation: false
                },{
                    name: 'main',
                    type: 'pie',
                    radius: ['85%', '100%'],
                    label: {
                        normal: {
                            show: false,
                        }
                    },
                    data: getPersionPieData(percent),
                    hoverAnimation: false,
                    animationEasingUpdate: 'cubicInOut',
                    animationDurationUpdate: 500
                }
            ]
        };
        personPie.setOption(option);
    };

    // 总预算
    const setTotalBudget = function(id, p) {
        let percent = p || 0;
        let totalBudgetBar = echarts.init(document.getElementById(id));
        option = {
            xAxis: [{
                axisTick: {show: false},
                axisLine: {show: false},
                axisLabel: {show: false},
                splitLine: {show: false}
            }],
            yAxis: [{
                type: 'category',
                data: [''],
                axisTick: {show: false},
                axisLine: {show: false},
                axisLabel: {
                    textStyle: {
                        color: '#fff',
                    }
                }
            }],
            series: [{
                name: ' ',
                type: 'bar',
                barWidth: 24,
                silent: true,
                itemStyle: {
                    normal: {
                        color: '#393D47',
                        barBorderRadius: [20, 25, 25, 20]
                    }
                },
                barGap: '-100%',
                barCategoryGap: '50%',
                data: [100],
            }, {
                name: ' ',
                type: 'bar',
                barWidth: 24,
                label: {
                    normal: {
                        show: true,
                        color: '#D5D5D7',
                        position: ['100', '30%'],
                        formatter: '{c}%',
                    }
                },
                data: getTotalBudgetData(percent)
            }]
        };
        totalBudgetBar.setOption(option);
    };

    // 经理分配时间
    const setAllotTime = function(id, p) {
        let percent;
        let allotTimePie = echarts.init(document.getElementById(id));
        let option = {
            tooltip: {
                trigger: 'item',
                formatter: "{a} <br/>{b}: {c} ({d}%)"
            },
            color: ['#00ACFF', '#356BFF', '#6BD8FA', '#D6E9F7', '#374066', '#DCDBDC'],
            series: [
                {
                    name:'分配时间',
                    type:'pie',
                    radius: ['50%', '80%'],
                    avoidLabelOverlap: false,
                    label: { //标签的位置
                        normal: {
                            show: true,
                            position: 'inside', //标签的位置
                            formatter: "{d}%",
                            textStyle: {
                                color: '#fff',
                            }
                        },
                        emphasis: {
                            show: true,
                            textStyle: {
                                fontWeight: 'bold'
                            }
                        }
                    },
                    labelLine: {
                        normal: {
                            show: false
                        }
                    },
                    data: getAllotTimePieData()
                }
            ]
        };
        allotTimePie.setOption(option);
    }

    // 查看详情Btn
    const detailsBtn = function() {
        $('div[name="message-box"]').hide();
        $('div[name="input-box"]').hide();
        $('div[name="answer-tab"]').hide();
        $('div[name="resource-info"]').show();
    };

    // 简单切换HospitalInfo 多的情况有性能问题，后续重构再改吧
    const switchHospitalInfo = function(hospital_name) {
        $.each($('div[class*="hosp-input-info"]'), function(i, v) {
            if($(v).attr("name") === hospital_name) {
                $(v).show();
            } else {
                $(v).hide();
            }
        });
    }

    // 销售计划于人员培训切换
    var switchSalesAndPersonel = function(identify) {
        var $div = $('.hosp-input-info').filter(function(index){
            return $(this).css("display") === "block";
        });

        if(identify === "销售计划") {
            $div.find(".sales-planning").show();
            $(".personnel-training").hide();
            $('.home .message-box .hosp-array-masklayer').hide();
        } else {
            $(".personnel-training").show();
            $div.find(".sales-planning").hide();
            $('.home .message-box .hosp-array-masklayer').show();
        }
    }

    // 医院、代表、产品信息切换
    var switchHospitalWithProductInfo = function(identify) {
        var $div = $('div[name="resource-info"]');
        if($div.css("display") === "block") {
            if(identify === "代表信息") {
                $div.find(".person-list").show();
                $div.find(".hospital-list").hide();
                $div.find(".product-list").hide();
            } else if(identify === "医院信息"){
                $div.find(".hospital-list").show();
                $div.find(".person-list").hide();
                $div.find(".product-list").hide();
            } else {
                $div.find(".hospital-list").hide();
                $div.find(".person-list").hide();
                $div.find(".product-list").show();
            }
        }
    }

    $(function(){

        let person = ["xiaosong", "xiaobai", "xiaolan", "xiaomu", "xiaoqing"];

        person.forEach(function(val) {
            setPersonData(val);
        });

        setAllotTime("hospcode-1-allot-time");

        setTotalBudget("total-budget");


        //答题页 查看详情按钮
        $('button[name="details-btn"]').click(function() {
            detailsBtn();
        });


        //资源页面 收起按钮
        $('#backup-btn').click(function() {
            $('div[name="message-box"]').show();
            $('div[name="input-box"]').show();
            $('div[name="answer-tab"]').show();
            $('div[name="resource-info"]').hide();
        });

        //资源页面 tab切换按钮
        $('div[name="navbar-btn"] button').click(function() {
            $(this).addClass("active");
            $(this).siblings().removeClass("active");
            switchHospitalWithProductInfo($(this).text());
        });

        // 医院列表点击事件
        $('ul[name="hosp-list"] li').click(function() {
            $(this).addClass("active");
            $(this).siblings().removeClass("active");
            switchHospitalInfo($(this).attr("name"));
        });

        //答题页 销售计划于人员培训按钮
        $('div[name="answer-tab"] div[name="btn-group"] button').click(function(){
            $(this).addClass("active");
            $(this).siblings().removeClass("active");
            switchSalesAndPersonel($(this).text());
        });

        //提交按钮
        $('button[name="submit-btn"]').click(function(){
            w.location = "/report/7aeddad0-3509-4dd2-8411-2dd4cfc923fc/1"
        });






        // 验证

        //分配推广预算keyup设置图
        $('div[name="bottom"] div[name="hosp-info"] input[name="input-budget"]').keyup(function(){
            var inputs = $('div[name="bottom"] div[name="hosp-info"] input[name="input-budget"]');
            var sum = 0;
            $.each(inputs, function(i, v) {
                sum += parseInt($(v).val() || 0);
            });
            setTotalBudget("total-budget", sum);
        });

        //分配推广预算blur设置检查超出100
        $('div[name="bottom"] div[name="hosp-info"] input[name="input-budget"]').blur(function(){
            var inputs = $('div[name="bottom"] div[name="hosp-info"] input[name="input-budget"]');
            var sum = 0;
            $.each(inputs, function(i, v) {
                sum += parseInt($(v).val() || 0);
            });
            if(sum > 100) {
                alert("超出预算！")
            }
        });

        //SalesMan下拉选择 禁用、不禁用input
        $('.hosp-input-info select').change(function(){
            var that = this;
            var div = $('.hosp-input-info').filter(function(index, evn){
                return $(evn).css("display") === "block"
            });

            var selected = $(div).find('select option:selected');
            if($(selected).val() !== "") {
                selected_salemman = $(selected).val();
                $(div).find('input').attr("disabled", false);
                $(div).find('option').attr("selected", false);
                $(div).find('option[value="小白"]').attr("selected", true);
                w.console.info()
            }

            // if($(this).val() !== "" && $(this).find("option:selected").text() !== "不分配") {
            //     selected_salemman = $(this).val();
            //     $(div).find('input').attr("disabled", false);
            // } else {
            //     f.alert.choose_info("分配情况", ["是", "否"], "*******？", function () {
            //         $(div).find('input').val("").attr("disabled", true);
            //         setTotalBudget("total-budget", 0);
            //     }, function () {
            //
            //         $(that).find('option').attr("selected", false);
            //         setTimeout(function(){
            //                 $('.hosp-input-info select').find('option[value="'+selected_salemman+'"]').attr("selected", true);
            //         }, 1000)
            //
            //
            //         $(div).find('input').attr("disabled", false);
            //     });
            // }
        });

    });


})(jQuery, window);
