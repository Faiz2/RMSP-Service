// var overflowTableContainer = document.getElementsByClassName("inside-div");
(function($,w){
    $(function(){
        init: {
        }
        events: {
            //导出Excel
            $('#exportExcel').click(function(){

                f.alert.loading(true);
                setTimeout(function(){
                    f.alert.loading(false);
                    layer.alert('已下载');
                },1000)


            });
            // 显示导出/导入excel区域按钮
            $('div[name = "toggle-export"]').click(function(e) {
                $('div[name="area-export"]').toggle();
                $(document).one("click", function(){
                    $('div[name="area-export"]').hide();
                });
                e.stopPropagation();
            });

            // 导入/导出区域的按钮冒泡阻止
            $('div[name="area-export"]').click(function(e) {
                e.stopPropagation();
            });

            $('button[name="go-phrase"]').click(function(){
                // w.location.href = "/home/" + $('input[name="uuid"]').val() + "/2"
                w.location.href = "/transition/" + $('input[name="uuid"]').val() + "/"+ $('input[name="phrase"]').val()
            });
        }
    });

})(jQuery, window);

var dom = document.getElementById("container");
var myChart = echarts.init(dom);
var app = {};
option = null;
option = {
    // title: {
    //     text: '多雷达图'
    // },
    tooltip: {
        trigger: 'axis'
    },
    // legend: {
    //     x: 'center',
    //     data:['某软件','某主食手机','某水果手机','降水量','蒸发量']
    // },
    radar: [

        {
            indicator:
                [
                    {text: '<div>报表分析与决策</div>', max: 100},
                    {text: '市场洞察', max: 100},
                    {text: '目标分级', max: 100},
                    {text: '公司战略执行力', max: 100},
                    {text: '屏幕', max: 100},
                    {text: '系统', max: 100},
                    {text: '性能', max: 100},
                    {text: '屏幕', max: 100},
                    {text: '屏幕', max: 100}
                ],
            center: ['50%','45%'],
            radius: 200
        }
    ],
    series: [
        {
            type: 'radar',
            tooltip: {
                trigger: 'item'
            },
            itemStyle: {normal: {areaStyle: {type: 'default'}}},
            data: [
                {
                    value: [30,53,65,40,40,50,50,50,50],
                    name: '某软件'
                }
            ]
        },
        {
            type: 'radar',
            radarIndex: 1,
            data: [
                {
                    value: [85, 90, 90, 95, 95],
                    name: '某主食手机'
                },
                {
                    value: [95, 80, 95, 90, 93],
                    name: '某水果手机'
                }
            ]
        },
        {
            type: 'radar',
            radarIndex: 2,
            itemStyle: {normal: {areaStyle: {type: 'default'}}},
            data: [
                {
                    name: '降水量',
                    value: [2.6, 5.9, 9.0, 26.4, 28.7, 70.7, 75.6, 82.2, 48.7, 18.8, 6.0, 2.3],
                },
                {
                    name:'蒸发量',
                    value:[2.0, 4.9, 7.0, 23.2, 25.6, 76.7, 35.6, 62.2, 32.6, 20.0, 6.4, 3.3]
                }
            ]
        }
    ]
};
;
if (option && typeof option === "object") {
    myChart.setOption(option, true);
}
