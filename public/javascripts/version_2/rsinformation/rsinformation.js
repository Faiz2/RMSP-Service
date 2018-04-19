(function($, w){
    var overflowTableContainer = $(".inside-div");
    var overflowTableContainerEle = overflowTableContainer[0];
    var tableHeade = $(".overflow-table-head");
    var table = $(".overflow-table");
    var tablelen = table.length;
    var oldTableWidth = 0;
    var scrollBarLength = 0;
    // 判断是哪个有滚动条的表格显示
    function whichTable() {
        try {
            if (overflowTableContainer[0].offsetWidth == 0) {
                if (overflowTableContainer[1].offsetWidth == 0) {
                    if(overflowTableContainer[2].offsetWidth == 0) {
                        if(overflowTableContainer[3].offsetWidth == 0) {
                            overflowTableContainerEle = overflowTableContainer[4];
                        }else {
                            overflowTableContainerEle = overflowTableContainer[3];
                        }
                    }else {
                        overflowTableContainerEle = overflowTableContainer[2];
                    }
                } else {
                    overflowTableContainerEle = overflowTableContainer[1];
                }
            } else {
                overflowTableContainerEle = overflowTableContainer[0];
            }
            scrollBarLength = overflowTableContainerEle.offsetWidth - overflowTableContainerEle.clientWidth;
            overflowTableContainerEle.style.paddingRight = scrollBarLength + "px";
        } catch(err) {
            w.console.info(err)
        }

    }

    // 消除滚动条
    function resetTableWidth() {
        oldTableWidth = 0;
        for (var i = 0; i < tablelen; i++) {
                oldTableWidth = overflowTableContainer[i].clientWidth;
                table[i].style.width = tableHeade[i].clientWidth + "px";
        }
    }

    // 医院、代表、产品信息切换
    var switchHospitalWithProductInfo = function(identify) {
        // download-btn
        var $div = $('div[name="resource-info"]');
        if($div.css("display") === "block") {
            if(identify === "代表信息") {
                $div.find(".person-list").show();
                $div.find(".hospital-list").hide();
                $div.find(".product-list").hide();
                $div.find(".report-list").hide();
                $div.find(".download-btn").hide();
            } else if(identify === "医院信息"){
                $div.find(".hospital-list").show();
                $div.find(".person-list").hide();
                $div.find(".product-list").hide();
                $div.find(".report-list").hide();
                $div.find(".download-btn").show();
            } else if(identify === "产品信息"){
                $div.find(".hospital-list").hide();
                $div.find(".person-list").hide();
                $div.find(".product-list").show();
                $div.find(".report-list").hide();
                $div.find(".download-btn").hide();
            } else {

                $div.find(".hospital-list").hide();
                $div.find(".person-list").hide();
                $div.find(".product-list").hide();
                $div.find(".report-list").show();
                $div.find(".download-btn").hide();
                whichTable();
                resetTableWidth();
            }
        }
    };

    var personRadarMap = function(identify) {
        var radarmap = echarts.init($('.person-list div[name="'+ identify +'"]')[0]);
        var lineStyle = {
            normal: {
                width: 2,
                opacity: 0.9
            }
        };

        var option = {
            tooltip: {
                confine: true
            },
            title: {},
            legend: {
                top: 'top',
                left: 'right',
                orient: 'vertical',
                data: ['当期', '上期'],
                itemGap: 20,
                icon:'circle',
                textStyle: {
                    color: '#fff',
                    fontSize: 10
                },
                // selectedMode: 'single'
            },
            radar: {
                indicator: [
                    {name: '销售技巧', max: 100},
                    {name: '产品知识', max: 100},
                    {name: '工作积极性', max: 100}
                ],
                center: ['48%', '55%'],
                radius: 75,
                shape: 'circle',
                splitNumber: 5,
                name: {
                    textStyle: {
                        fontSize: 10,
                        color: 'rgb(236, 236, 236)'
                    }
                },
                splitLine: {
                    lineStyle: {
                        color: [
                            new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                                "offset": 0,
                                "color": '#2496dc',

                            }, {
                                "offset": 1,
                                "color": '#FE0059'
                            }]),

                            new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                                "offset": 0,
                                "color": '#FE601B',

                            }, {
                                "offset": 1,
                                "color": '#FE0059'
                            }]),

                            new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                                "offset": 0,
                                "color": '#FE601B',

                            }, {
                                "offset": 1,
                                "color": '#00E8F8'
                            }])

                        ]
                        // color: [
                        //     'rgba(238, 197, 102, 0.1)', 'rgba(238, 197, 102, 0.2)',
                        //     'rgba(238, 197, 102, 0.4)', 'rgba(238, 197, 102, 0.6)',
                        //     'rgba(238, 197, 102, 0.8)', 'rgba(238, 197, 102, 1)'
                        // ].reverse()
                    }
                },
                splitArea: {
                    show: false
                },
                axisLine: {
                    lineStyle: {
                        color: 'rgba(238, 197, 102, 0.5)'
                    }
                }
            },
            series: [
                {
                    name: '人物评分',
                    type: 'radar',
                    symbol: 'none',
                    lineStyle: lineStyle,
                    areaStyle: {normal: {}},
                    data : [
                        {
                            value : [40, 90, 50 ],
                            name : '当期',
                            itemStyle: {
                                normal: {
                                    color: '#0E86A4'
                                }
                            }
                        },
                        {
                            value : [80, 50, 12],
                            name : '上期',
                            itemStyle: {
                                normal: {
                                    color: '#A6A921'
                                }
                            }
                        }
                    ]
                }
            ]
        };
        radarmap.setOption(option);
    };

    $(function(){

        init: {
            var person = ["小宋", "小兰", "小木", "小白", "小青"];
            $.each(person, function(i, v){
                personRadarMap(v)
            });
        }
        events: {
            //资源页面 收起按钮
            $('#backup-btn').click(function() {
                $('div[name="message-box"]').show();
                $('div[name="input-box"]').show();
                $('div[name="answer-tab"]').show();
                $('div[name="resource-info"]').hide();
            });

            $('#download-btn').click(function(){
                var jsonObj = JSON.stringify(f.parameterPrefixModule.conditions({
                    uuid: $('input[name="uuid"]').val(),
                    phase: parseInt($('input[name="phase"]').val())
                }));
                f.alert.loading(true);
                f.ajaxModule.baseCall('/hospital/create', jsonObj, 'POST', function(r){
                    if (r.status === 'ok') {
                        f.alert.loading(false);
                        // w.window.open('/download/file/' + r.result.data);
                        w.location.href = '/download/file/' + r.result.data
                    }
                });
            });
            //资源页面 tab切换按钮
            $('div[name="navbar-btn"] button').click(function() {
                $(this).addClass("active");
                $(this).siblings().removeClass("active");
                switchHospitalWithProductInfo($(this).text());
            });


        }
    });
})(jQuery, window);
