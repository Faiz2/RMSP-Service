(function($, w) {
    var selected_salemman = "";
    var salesmen = ['小宋', '小白', '小兰', '小木', '小青'];
    // TODO: 从2018年3月12日后，暂时封印ES6的写法, IE11一下不支持
    // Persion Pie
    var getPersionPieData = function(percent) {
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
    var getTotalBudgetData = function(percent) {
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
    var getAllotTimePieData = function(percents) {
        var tmp = [];
        $.each(percents, function(i, v){
            tmp.push(v);
        })
        return tmp;
        // return [
        //     {value:30, name:'协助拜访'},
        //     {value:31, name:'能力辅助'},
        //     {value:3, name:'例会/团建'},
        //     {value:8, name:'KPI分析'},
        //     {value:12, name:'行政工作'},
        //     {value:16, name:'未分配'}
        // ];
    }

    // salesman分配时间
    var setPersonData = function(id, p) {
        var percent = p || 0;
        //document.getElementById(id)
        var personPie = echarts.init($('.person-details-info .detail div[name="' + id + '"]')[0]);
        var option = {
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
    var setTotalBudget = function(id, p) {
        var percent = p || 0;
        var totalBudgetBar = echarts.init(document.getElementById(id));
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
    var setAllotTime = function(id, p) {
        var allotTimePie = echarts.init(document.getElementById(id));
        var option = {
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
                    data: getAllotTimePieData(p)
                }
            ]
        };
        allotTimePie.setOption(option);
    }

    // 查看详情Btn
    var detailsBtn = function() {
        $('div[name="message-box"]').hide();
        $('div[name="input-box"]').hide();
        $('div[name="answer-tab"]').hide();
        $('div[name="resource-info"]').show();
    };

    // 简单切换HospitalInfo 多的情况有性能问题，后续重构再改吧
    var switchHospitalInfo = function(hospital_name) {
        $.each($('div[class*="hosp-input-info"]'), function(i, v) {
            $(v).finish();
            if($(v).attr("name") === hospital_name) {
                $(v).show();
                $(v).css("display","block");
                $(v).addClass('slideInRight');
                $(v).removeClass('slideOutRight');
            } else {
                // $(v).hide("slow");
                $(v).addClass('slideOutRight').delay(300).hide(300);
                $(v).removeClass('slideInRight');

            }
        });
    }

    // 销售计划于人员培训切换
    var switchSalesAndPersonel = function(identify) {
        var $div = $('.hosp-input-info').filter(function(index){
            return $(this).css("display") === "block";
        });
        $(".personal-training").finish();
        if(identify === "销售计划") {
            $div.find(".sales-planning").show();
            $div.find(".sales-planning").addClass('slideInRight');
            $(".personal-training").removeClass('zoomIn')
            $(".personal-training").addClass('zoomOut').delay(400).hide(400);
            $('.home .message-box .hosp-array-masklayer').hide();
        } else {
            $(".personal-training").show();
            $(".personal-training").removeClass('zoomOut')
            $(".personal-training").addClass('zoomIn');
            $div.find(".sales-planning").hide();
            $('.home .message-box .hosp-array-masklayer').show();
        }
    }

    // Budget 输入超过100 验证
    var verifyBudgetlg = function() {
        var inputs = $('div[name="bottom"] div[name="hosp-info"] input[name="input-budget"]');
        var sum = 0;
        $.each(inputs, function(i, v) {
            sum += parseInt($(v).val() || 0);
        });
        if(sum > 100) {return false;}else {return true;}
    }

    // 只要选中代表 budget 就必须填写 验证
    var verifyBudgeteq = function() {
        var result = false;
        var selected = $('div[name="hosp-info"] select option:selected').filter(function(i, dom){
            return $(dom).val() !== "" && $(dom).val() !== "不分配";
        });
        $.each(selected, function(i, v){
            var input = $('input[name="input-budget"][hospital-name="'+$(v).attr("hospital-name")+'"]');
            if(input.val() === "") {
                f.alert.alert_warn("警告", $(v).attr("hospital-name") + "下的"
                        + input.attr("pharbers-type") + "未分配");
                result = false;
                return false;
            } else {
                result = true;
                return true;
            }
        });
        return result;
    }

    // 代表时间大于100 验证
    var verifyTimelg = function() {
        var array = getAllInputAllotTime();
        var result = false;
        $.each(salesmen, function(i, name) {
            var sum = 0;
            var allottArray = array.filter(function(obj, index){ return obj.salesmen === name;});
            $.each(allottArray, function(n, time) {sum += time.allotTime});
            if(sum > 100) {
                f.alert.alert_warn("警告", name+"的沟通时间超出预设");
                result = false;
                return false;
            } else {
                result = true;
                return true;
            }
        });
        return result;
    }

    // 代表时间是否等于 0 验证
    var verifyTimeeq = function() {
        var array = getAllInputAllotTime();
        var result = false;
        $.each(salesmen, function(i, name) {
            var sum = 0;
            var allottArray = array.filter(function(obj, index){ return obj.salesmen === name;});
            $.each(allottArray, function(n, time) {sum += time.allotTime});
            if(sum === 0) {
                f.alert.alert_warn("警告", name+"的沟通时间未分配");
                result = false;
                return false;
            } else {
                result = true;
                return true;
            }
        });
        return result;
    }

    // 经理时间大于100 验证
    var verifyManageTimelg = function() {
        var array = getManageAllotTime();
        var sum = 0;
        $.each(array, function(i, v){
            $.each(v.apply, function(i, data){
                    sum += data.days
            });
        });
        if(sum > 100) {
            f.alert.alert_warn("警告", "经理时间分配超出100天");
            return false;
        } else return true;
    }

    // 经理协作拜访时间大于代表 验证
    var verufyManageVisitTimelg = function(array) {
        var tmp = $(array).filter(function(index, dom){
            return dom.type === "实地协访";
        }).map(function(index, dom){
            return dom.apply;
        }).get(0);

        var result = false;
        $.each(salesmen, function(i, name) {
            var sum = 0;
            var allottArray = getAllInputAllotTime().filter(function(obj, index){ return obj.salesmen === name;});
            $.each(allottArray, function(n, time) {sum += time.allotTime});
            var personal = $(tmp).filter(function(index, dom){
                return dom.personal === name
            });
            if(personal.get(0).days > sum) {
                f.alert.alert_warn("警告", name+"fuck");
                result = false;
                return false;
            } else {
                result = true;
                return true;
            }
        });
        return result;
    }

    // 当前医院是否选中代表 验证
    var verifyCurrHospitalSalesMen = function(hosp_identify) {
        var selected = $('div[name="'+hosp_identify+'"] select option:selected');
        if(selected.val() == "") {return false;} else {return true;}
    }

    // 填写Budget但未选择代表 验证
    var verifyBudgetNotSalesMen = function() {
        var result = false;
        $.each(getAllInputBudget(), function(i, v) {
            if(v.salesmen === "" && v.budget > 0) {
                f.alert.alert_warn("警告", v.hospital+"未选择代表");
                result = false;
                return false;
            } else {result = true;}
        });
        return result;
    }

    // 提交检查所有人是否被选中
    var verifyAllSalesMenIsSelected = function() {
        var result = false;
        var allSelectedSalesMen = $('div[name="hosp-info"]').filter(function(index, dom){
            var selected = $(dom).find('select option:selected');
            return selected.val() !== "" && selected.val() !== "不分配"
        }).map(function(index, dom){
            return $(dom).find('select option:selected').val();
        }).toArray();

        $.each(salesmen, function(i, v){
            if ($.inArray(v, allSelectedSalesMen) < 0) {
                result = false;
                f.alert.alert_warn("警告", v + "代表漏选！")
                return false;
            } else {
                result = true;
                return true;
            }
        });
        return result;
    }

    // 验证所有输入框是否为数字
    var verifyAllInputIsNumber = function() {
        var result = false;
        var inputs = $('input').filter(function(index, dom){
            return $(dom).attr("disabled") !== "disabled" && $(dom).val() !== "" && $(dom).attr("type") !== "hidden";
        });
        $.each(inputs, function(i, v){
            if( !regexExce(numberzzs, $(v).val()) ) {
                f.alert.alert_warn("警告", $(v).attr("hospital-name")+"下的"+$(v).attr("pharbers-type")+"不是正整数")
                result = false;
                return false;
            } else {
                result = true;
                return true;
            }
        });
        if(inputs.toArray().length === 0){result = true;}
        return result;
    }


    // 获取所有input Budget输入
    var getAllInputBudget = function() {
        var tmp = [];
        $.each($('.hosp-input-info div[name="hosp-info"]'), function(i, dom){
            var selected = $(dom).find('select option:selected');
            var budgetNum = $(dom).find('input[name="input-budget"]');
            tmp.push({
                salesmen: $(selected).val(),
                budget: $(budgetNum).val(),
                hospital: $(selected).attr("hospital-name")
            });
        });
        var ftmp = tmp.filter(function(obj, index) { return obj.budget >= 0 });
        return ftmp;
    }

    // 计算所有Input Budget输入
    var calcBudget = function() {
        var sum = 0;
        $('ul[name="hosp-list"] li span[name="budget"]').text("——");
        $.each(getAllInputBudget(), function(i, v) {
            sum += parseInt(v.budget) || 0;
            if(v.budget === "") {
                $('ul[name="hosp-list"]').find('li[name="' + v.hospital + '"] span[name="budget"]').text("——");
            }else {
                $('ul[name="hosp-list"]').find('li[name="' + v.hospital + '"] span[name="budget"]').text(v.budget);
            }

        });
        setTotalBudget("total-budget", sum);
    }

    // 获取代表所有的input AllotTime输入
    var getAllInputAllotTime = function() {
        var tmp = [];
        var trainingSum = 0;
        //人员培训下代表产品培训input
        var trainingInput = $('div[name="personal-training"]')
                            .find('div[name="input-training"]')
                            .find('input[name="产品培训"]');
        //人员培训下团建/列会input
        var meetingInput = $('div[name="personal-training"]')
                            .find('div[name="input-training"]')
                            .find('input[name="团队例会和团建"]');
        //人员培训下1对1辅导
        var coachInput = $('div[name="personal-training"]')
                            .find('div[name="input-training"]')
                            .find('input[name="能力辅导"]');


        $.each($('.hosp-input-info'), function(i, dom){
            var selected = $(dom)
                        .find('div[name="hosp-info"] select option:selected');
            //医院下input
            var inputs = $(dom).find('input[name="prod_hours"]');

            var sumInput = 0;
            $.each(inputs, function(n, v){
                sumInput += parseInt($(v).val()) || 0;
            });

            var product = trainingInput.filter(function(index, dom){
                return $(dom).attr("personal") === $(selected).val();
            });

            var coach = coachInput.filter(function(index, dom){
                return $(dom).attr("personal") === $(selected).val();
            });

            tmp.push({
                salesmen: $(selected).val(),
                allotTime: (parseInt($(product).val()) || 0)
                            + (parseInt($(meetingInput).val()) || 0)
                            + (parseInt($(coach).val()) || 0)
                            + sumInput,
                hospital: $(selected).attr("hospital-name")
            });
        });

        return tmp;
    }

    // 计算代表时间
    var calcAllotTime = function(inputObj) {
        if(verifyTimelg()) {
            $.each(salesmen, function(i, name) {
                var sum = 0;
                var allottArray = getAllInputAllotTime().filter(function(obj, index){ return obj.salesmen === name;});
                $.each(allottArray, function(n, time) {
                    sum += time.allotTime
                });
                setPersonData(name, sum);
            });
        } else {
            try {
                inputObj.val("");
            } catch(err) {

            }
        }

    }

    // 显示选择代表
    var showSelectSalesMen = function() {
        var selected = $('.selected-salesman').find('select option:selected');
        $.each(selected, function(i, v){
            var salesmen = "——";
            var salesmenpic = "salesmen-picture";
            if($(v).val() === "") salesmen = "——"; else salesmen = $(v).val();
            if($(v).val() === "不分配" || $(v).val() === "") { salesmenpic = "salesmen-picture"; } else {salesmenpic = $(v).val()}
            $('ul[name="hosp-list"]').find('li[name="' + $(v).attr("hospital-name") + '"]').find('span[name="salesmen"]').text(salesmen);
            $('div[name="' + $(v).attr("hospital-name") + '"] .salesman-picture img').attr("src", asset_resources+"images/version_2/" + salesmenpic + ".png")
        });

    }

    // 获取经理分配时间所有Input 输入
    var getManageAllotTime = function() {
        var tmp = [];
        var personalDiv = $('div[name="personal-training"]');

        //经理协助拜访input
        var visit = $(personalDiv)
                     .find('div[name="input-personal"]')
                     .find('div[name="input-manager"]')
                     .find('input');
         //人员培训下1对1辅导
         var coachInput = $(personalDiv)
                             .find('div[name="input-training"]')
                             .find('input[name="能力辅导"]');
        //人员培训下团建/列会input
        var meetingInput = $(personalDiv)
                            .find('div[name="input-training"]')
                            .find('input[name="团队例会和团建"]');
        // KPIinput
        var kpi = $(personalDiv)
                    .find('div[name="input-display"]')
                    .find('input[name="KPI 报告分析"]');
        // 行政input
        var administrative = $(personalDiv)
                                .find('div[name="input-display"]')
                                .find('input[name="行政工作"]');


        var array = [visit, coachInput, meetingInput, kpi, administrative];
        $.each(array, function(i, o) {
            var obj = o.map(function(index, dom) {
                return {
                    personal: $(dom).attr("personal"),
                    days: parseInt($(dom).val()) || 0
                }
            });
            tmp.push({
                type: $(o).attr('name'),
                apply: obj
            });

        });

        return tmp;
    }

    // 计算经理分配时间
    var calcManageAllotTime = function(inputObj) {
        var result = getManageAllotTime();
        if(!verifyManageTimelg()) {
            inputObj.val("");

        } else if(!verufyManageVisitTimelg(result)) {
            inputObj.val("");
        }

        var reval = $(result).map(function(index, dom){
            var sum = 0;
            $.each(dom.apply, function(i, v){sum += v.days})
            return {
                name: dom.type,
                value: sum
            }
        });
        reval = reval.toArray();
        reval.push({name: "未分配", value: parseInt($('span[name="other-days"]').text() || 0)});
        setAllotTime("hospcode-allot-time", reval);
        setTipsDays(inputObj);
    }

    // 计算人员培训的分配天数并显示
    var setTipsDays = function(inputObj) {
        var total = 100;
        var visitSum = 0;
        var coachSum = 0;
        var kpiSum = 0;
        var administrativeSum = 0;
        var manageResult = getManageAllotTime();

        //人员培训下代表产品培训input
        var trainingInput = $('div[name="personal-training"]')
                            .find('div[name="input-training"]')
                            .find('input[name="产品培训"]');


        var visitArray = $(manageResult).filter(function(index, dom){
            return dom.type === "实地协访";
        });

        var coachArray = $(manageResult).filter(function(index, dom){
            return dom.type === "能力辅导";
        });

        var kpiArray = $(manageResult).filter(function(index, dom){
            return dom.type === "KPI 报告分析";
        });

        var administrativeArray = $(manageResult).filter(function(index, dom){
            return dom.type === "行政工作";
        });

        var meetingSum =  $(manageResult).filter(function(index, dom){
            return dom.type === "团队例会和团建";
        }).get(0).apply.get(0).days;



        $.each(visitArray, function(i, v) {
            $.each(v.apply, function(n, d){
                visitSum += parseInt(d.days)
            });
        });

        $.each(kpiArray, function(i, v) {
            $.each(v.apply, function(n, d){
                kpiSum += parseInt(d.days)
            });
        });

        $.each(administrativeArray, function(i, v) {
            $.each(v.apply, function(n, d){
                administrativeSum += parseInt(d.days)
            });
        });

        $.each(coachArray, function(i, v){
            $.each(v.apply, function(n, d){
                coachSum += parseInt(d.days)
                var product = trainingInput.filter(function(index, dom){
                    return $(dom).attr("personal") === d.personal;
                });
                var coach = coachArray.get(0).apply.filter(function(index, dom){
                    return dom.personal === d.personal
                });

                $('div[name="input-training"] input[name="'+d.personal+'"]')
                .val( parseInt($(product).val() || 0)
                    + parseInt(coach.get(0).days || 0)
                    + parseInt(meetingSum || 0))

            });
        });
        // debugger;
        $('div[name="input-display"] span[name="meeting-days"]').text(meetingSum);
        $('div[name="input-display"] span[name="coach-days"]').text(coachSum);
        $('div[name="input-display"] span[name="visit-days"]').text(visitSum);
        var otherDays = total - meetingSum - coachSum - visitSum - kpiSum - administrativeSum;
        $('div[name="input-display"] span[name="other-days"]').text(otherDays);

    }

    // 删除空的Option
    var removeSelectNoneOption = function() {
        $('.hosp-input-info select').find('option:selected').filter(function(index, dom){
            return $(dom).val() !== ""
        }).parent().find('option').filter(function(index, dom){
            return $(dom).val() === "";
        }).remove();
    }

    $(function(){

        init: {
            showSelectSalesMen();
            calcBudget();
            calcAllotTime();
            setTipsDays();
            calcManageAllotTime();
            removeSelectNoneOption();
        }

        events: {
            //答题页 查看详情按钮
            $('button[name="details-btn"]').click(function() {
                detailsBtn();
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

            // 分配推广预算keyup设置图
            $('div[name="bottom"] div[name="hosp-info"] input[name="input-budget"]')
                .keyup(function() {
                if(verifyBudgetNotSalesMen()) {
                    calcBudget($(this));
                } else {
                    $(this).val("");
                }
            });

            // 分配推广预算blur设置检查超出100
            $('div[name="bottom"] div[name="hosp-info"] input[name="input-budget"]')
                .blur(function() {
                if(!verifyBudgetlg()) {
                    f.alert.alert_warn("警告", "Budget超出预算");
                    $(this).val("");
                    calcBudget();
                }
            });

            // 分配沟通时间keyup
            $('div[name="hosp-budget"] input[name="prod_hours"]' +
            ', div[name="personal-training"] div[name="input-training"] input')
                .keyup(function() {
                var that = this;
                calcAllotTime($(that));
                calcManageAllotTime($(that));
            });

            // 分配沟通时间blur
            $('div[name="hosp-budget"] input[name="prod_hours"]' +
            ', div[name="personal-training"] div[name="input-training"] input')
                .blur(function() {
                var that = this;
                calcAllotTime($(that))
                calcManageAllotTime($(that));
                if(!verifyCurrHospitalSalesMen($(that).attr("hospital-name"))) {
                    f.alert.alert_warn("警告", "未指定代表");
                }

            });

            // salesmen select change
            $('.hosp-input-info select').change(function() {
                var phrase = $('input[name="phase"]').val();
                var that = this;
                removeSelectNoneOption();
                var inputs = $('div[name="'+$(this).find('option:selected').attr("hospital-name")+'"]')
                            .find('input')
                            .not('[pharbers-type="皮肤药"]')
                            .not('[name="hospital-code"]');
                if(phrase == 2) {
                    inputs = $('div[name="'+$(this).find('option:selected').attr("hospital-name")+'"]')
                            .find('input')
                            .not('[hospital-name="铁路医院"]')
                            .not('[hospital-name="海港医院"]')
                            .not('[hospital-name="第六医院"]')
                            .not('[hospital-name="小营医院"]')
                            .not('[hospital-name="光华医院"]')
                            .not('[hospital-name="大学医院"]')
                            .not('[name="hospital-code"]');
                }
                if($(this).val() === "不分配") {
                    f.alert.choose_info("是否清空", ["是", "否"], "即将清空当前填写项，是否操作？", function () {
                        inputs.val("");
                        inputs.prop("disabled", true);
                        calcBudget();
                    }, function () {
                        $('div[name="'+$(that).find('option:selected').attr("hospital-name")+'"] select option[value="' + selected_salemman + '"]').prop("selected", true);
                        showSelectSalesMen();
                    })
                } else {
                    selected_salemman = $(this).val();
                    inputs.prop("disabled", false)
                }
                showSelectSalesMen();
                calcAllotTime();
            });

            // 实地协防keyup
            $('div[name="personal-training"] div[name="input-manager"] input, '+
                'div[name="input-display"] input').keyup(function(){
                var that = this;
                calcManageAllotTime($(that));
            });

            // 实地协防blur
            $('div[name="personal-training"] div[name="input-manager"] input, '+
                'div[name="input-display"] input').blur(function(){
                var that = this;
                calcManageAllotTime($(that));
            });


            //提交按钮
            $('button[name="submit-btn"]').click(function(){

                var uuid = $('input:hidden[name="uuid"]').val();
                var phase = parseInt($('input:hidden[name="phase"]').val());
                var userId = $.cookie("user");

                var decisionDivs = $('.hosp-input-info');
                var decision = decisionDivs.map(function(index, div) {
                    var sales = $(div).find('input[name="prod_value"]').map(function(index, input){
                        return {
                            "prod_name": $(input).attr('pharbers-type'),
                            "prod_value": parseInt($(input).val()|| 0)
                        };
                    });
                    var visitHours = $(div).find('input[name="prod_hours"]').map(function(index, input){
                        return {
                            "prod_name": $(input).attr('pharbers-type'),
                            "prod_hours": parseInt($(input).val()|| 0)
                        };
                    });
                    return {
                        "hosp_code": parseInt($(div).find('input[name="hospital-code"]').val()),
                        "hosp_name": $(div).attr("name"),
                        "phase": phase,
                        "budget": parseInt($(div).find('input[name="input-budget"]').val() || 0),
                        "salesmen": $(div).find('select option:selected').val(),
                        "sales": sales.toArray(),
                        "visit_hours": visitHours.toArray()
                    };
                });

                var projectNames = ["能力辅导", "实地协访", "团队例会和团建", "KPI 报告分析", "行政工作", "产品培训"];
                var managementDiv = $('div[name="personal-training"]')
                var management = $(projectNames).map(function(index, name){
                    var projectCode = parseInt($(managementDiv).find('input[name="'+ name +'"]').eq(0).attr("code"));
                    var apply = $(managementDiv).find('input[name="'+ name +'"]').map(function(index, input){
                        return {
                            "personal": $(input).attr("personal"),
                            "days": parseInt($(input).val() || 0)
                        };
                    });
                    return {
                        "phase": phase,
                        "project_name": name,
                        "project_code": projectCode,
                        "apply": apply.toArray()
                    };
                });

                var userInfo = {
                    "user_id": userId,
                    "uuid": uuid
                };

                var decisionTmp = {
                    "user_id": userId,
                    "uuid": uuid,
                    "phase": phase,
                    "decision": decision.toArray()
                };

                var managerTmp = {
                    "user_id": userId,
                    "uuid": uuid,
                    "phase": phase,
                    "management": management.toArray()
                };


                var decisionJson = JSON.stringify($.extend(decisionTmp, f.parameterPrefixModule.conditions(userInfo)));
                var managementJson = JSON.stringify($.extend(managerTmp, f.parameterPrefixModule.conditions(userInfo)));

                // w.console.info(verifyAllSalesMenIsSelected())
                // w.console.info(verifyAllInputIsNumber())
                // w.console.info(verifyManageTimelg())
                // w.console.info(verifyTimelg())
                // w.console.info(verifyBudgeteq())
                if(verifyAllSalesMenIsSelected() && verifyAllInputIsNumber() && verifyBudgeteq() && verifyManageTimelg() && verifyTimelg() && verifyTimeeq()) {
                    // w.console.info("aa")

                    f.alert.loading();
                    f.ajaxModule.baseCall("/decision/proceed", decisionJson, 'POST', function(r){
                        if(r.status === 'ok' && r.result.input_update === 'success') {
                            f.ajaxModule.baseCall("/management/proceed", managementJson, 'POST', function (rr) {
                                if(rr.status === 'ok' && rr.result.input_update === 'success') {
                                    f.ajaxModule.baseCall('/submit/submitdata', managementJson, 'POST', function(rrr){
                                        if(rrr.status === 'ok' && rrr.result.data === 'success') {
                                            layer.closeAll('loading');
                                            f.alert.alert_success("消息", "模拟成功");
                                            w.location = "/report/" + $('input:hidden[name="uuid"]').val() + "/" + $('input:hidden[name="phase"]').val();
                                        }
                                    });
                                }
                            });
                        }
                    });

                } else {

                }
            });

            // 显示导出/导入excel区域按钮
            $('div[name = "toggle-import-export"]').click(function(e){
                $('div[name="area-import-export"]').toggle();
                $(document).one("click", function(){
                    $('div[name="area-import-export"]').hide();
                });
                e.stopPropagation();
            });
            // 导入/导出区域的按钮冒泡阻止
            $('div[name="area-import-export"]').click(function(e){
                e.stopPropagation();
            });
            // 拖动事件
            $('button[name="btn-right"]').click(function () {
                if($('ul[name="hosp-list"]').position().left == 0) {
                    $('ul[name="hosp-list"]').animate({left:'-64px'},500);
                } else if ($('ul[name="hosp-list"]').position().left == -64){
                    $('ul[name="hosp-list"]').animate({left:'-200px'},1000);
                }
            });
            $('button[name="btn-left"]').click(function () {
                if($('ul[name="hosp-list"]').position().left == -200) {
                    $('ul[name="hosp-list"]').animate({left:'-135px'},500);
                } else if ($('ul[name="hosp-list"]').position().left == -135){
                    $('ul[name="hosp-list"]').animate({left:'0px'},1000);
                }
            })
        }
    });


})(jQuery, window);
