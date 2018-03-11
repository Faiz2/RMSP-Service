(function($, w) {

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

    // 未做基础验证
    const setPersonData = function(id, percent = 70) {
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
    const setTotalBudget = function(id, percent = 100) {
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
                        position: ['185', '30%'],
                        formatter: '{c}%',
                    }
                },
                data: getTotalBudgetData(percent)
            }]
        };
        totalBudgetBar.setOption(option);
    };

    // 查看详情Btn
    const detailsBtn = function() {
        $('div[name="message-box"]').hide();
        $('div[name="input-box"]').hide();
        $('div[name="resource-info"]').show();
    };

    $(function(){

        $('button[name="details-btn"]').click(function() {
            detailsBtn();
        });

        $('#backup-btn').click(function() {
            $('div[name="message-box"]').show();
            $('div[name="input-box"]').show();
            $('div[name="resource-info"]').hide();
        });

        let person = ["xiaosong", "xiaobai", "xiaolan", "xiaomu", "xiaoqing"];
        person.forEach(val => {
            setPersonData(val);
        });
        setTotalBudget("total-budget");

        // 医院列表点击事件
        $('ul[name="hosp-list"] li').click(function() {
            $.each($('ul[name="hosp-list"] li'), function(i, v){
                $(v).attr("class", "box")
            });
            $(this).attr("class", "box active")
        });
    });


})(jQuery, window)
