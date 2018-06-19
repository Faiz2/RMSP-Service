// var overflowTableContainer = document.getElementsByClassName("inside-div");


(function($,w){
    var dom = document.getElementById("container");
    var myChart = echarts.init(dom);

    var option = {
        tooltip: {
            trigger: 'axis'
        },
        radar: {
            indicator:
                [
                    {text: '报表分析与决策', max: 5},
                    {text: '市场洞察', max: 5},
                    {text: '目标分级', max: 5},
                    {text: '公司战略执行力', max: 5},
                    {text: '屏幕', max: 5},
                    {text: '系统', max: 5},
                    {text: '性能', max: 5},
                    {text: '屏幕', max: 5},
                    {text: '屏幕', max: 5}
                ],
            center: ['50%','45%'],
            radius: 200
        },
        series: [
            {
                name: "能力",
                type: 'radar',
                tooltip: {
                    trigger: 'item'
                },
                itemStyle: {normal: {areaStyle: {type: 'default'}}},
                data: [
                    {
                        value: [1,1,1,1,1,1,1,1,1]
                    }
                ]
            }
        ]
    };
    myChart.setOption(option, true);

    $(function(){
        init: {
            var condition = f.parameterPrefixModule.conditions(
                {
                    "uuid": "219776aa-6d5a-4bcf-bc99-74857e86ec7a",
                    "phase_key": "final"
                });
            var c = JSON.stringify(condition);

            f.ajaxModule.baseCall("/summary/query", c, "POST", function(rd) {
                if ( rd.status === 'ok') {
                    $('#total-sales').text(f.thousandsModule.formatNum(rd.result.data.total_sales.total));
                    $('#sales-ratio').text(parseFloat(f.thousandsModule.formatNum(rd.result.data.total_sales.uplift_ratio)).toFixed(2)+"%");
                    $('#team-ability').text(f.thousandsModule.formatNum(rd.result.data.team_ability.team_ability));
                    $('#ability-ratio').text(parseFloat(f.thousandsModule.formatNum(rd.result.data.team_ability.uplift_ratio)).toFixed(2)+"%");
                    // $('#total-sales').text(f.thousandsModule.formatNum(rd.result.data.total_sales)) =
                    // document.getElementById('totoaa-salex').innerHTML(rd.result.data.total_sales)
                    $.each(rd.result.data.team_achievement, function(i, v){
                        $('#team-achievement').append('<li>'+v.product_name+'</li>');
                        $('#achievement-radio').append('<li>'+parseFloat(f.thousandsModule.formatNum(v.achievement_ratio)).toFixed(2)+"%"+'</li>');
                    });
                    $.each(rd.result.data.market_share, function (i, v) {
                        $('#product-name').append('<li>'+v.product_name+'</li>');
                        $('#market-share').append('<li>'+parseFloat(f.thousandsModule.formatNum(v.market_share)).toFixed(2)+"%"+'</li>');
                        $('#uplift-ratio').append('<li>'+parseFloat(f.thousandsModule.formatNum(v.uplift_ratio)).toFixed(2)+"%"+'</li>');
                    });

                    // div attr description

                    // indicator：[] Array
                    // data：[] Array


                    var data = [];
                    var value = [];
                    var indicator = [];
                    $.each(rd.result.data.radar_map, function(i, v) {
                        var $div = $('div [description='+ v.name +']');
                        var grade = '/assets/images/version_2/' + v.comments.tips + ".png";
                        var title = '/assets/images/version_2/' + v.name + "-" + v.comments.tips + ".png";
                        $div.find('img[name="grade"]').attr('src', grade);
                        $div.find('p[name="explain"]').text(v.comments.describe);
                        if (v.comments.score > 2 ) {
                            $div.find('div[name="title"]').append('<img src="' + title + '" alt="">');
                        }
                        indicator.push({
                           text: v.name,
                           max: 5
                        });
                        value.push(v.comments.score);
                    });
                    data.push({
                        "value": value
                    });

                    // console.info(indicator)
                    // console.info(data)
                    myChart.setOption({
                        radar: {
                            indicator: indicator,
                            center: ['50%','45%'],
                            radius: 200
                        },
                        series: [
                            {
                                type: 'radar',
                                name: "能力",
                                tooltip: {
                                    trigger: 'item'
                                },
                                itemStyle: {normal: {areaStyle: {type: 'default'}}},
                                data: data
                            }
                        ]
                    }, true);
                } else {
                    console.error("Error")
                }
            });
            // console.info(f.thousandsModule.formatNum("200122555222"))
            // var total_sale = $('.top-p');


            // $.each(total_sale, function (ele) {
            //     total_sales.push(ele);
            // });
        }

        events: {
            //导出Excel
            // $('#exportExcel').click(function(){
            //
            //     f.alert.loading(true);
            //     setTimeout(function(){
            //         f.alert.loading(false);
            //         layer.alert('已下载');
            //     },1000)
            //
            //
            // });
            // // 显示导出/导入excel区域按钮
            // $('div[name = "toggle-export"]').click(function(e) {
            //     $('div[name="area-export"]').toggle();
            //     $(document).one("click", function(){
            //         $('div[name="area-export"]').hide();
            //     });
            //     e.stopPropagation();
            // });
            //
            // // 导入/导出区域的按钮冒泡阻止
            // $('div[name="area-export"]').click(function(e) {
            //     e.stopPropagation();
            // });
            //
            // $('button[name="go-phrase"]').click(function(){
            //     // w.location.href = "/home/" + $('input[name="uuid"]').val() + "/2"
            //     w.location.href = "/transition/" + $('input[name="uuid"]').val() + "/"+ $('input[name="phrase"]').val()
            // });

        }
    });

})(jQuery, window);



