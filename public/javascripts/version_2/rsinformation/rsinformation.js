(function($, w){
    var overflowTableContainer = $(".inside-div");
    var overflowTableContainerEle = overflowTableContainer[0];
    var tableHeade = $(".overflow-table-head");
    var table = $(".overflow-table");
    var order = 0;
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
        var $div = $('div[name="resource-info"]');
        if($div.css("display") === "block") {
            if(identify === "代表信息") {
                $div.find(".person-list").show();
                $div.find(".hospital-list").hide();
                $div.find(".product-list").hide();
                $div.find(".report-list").hide();
            } else if(identify === "医院信息"){
                $div.find(".hospital-list").show();
                $div.find(".person-list").hide();
                $div.find(".product-list").hide();
                $div.find(".report-list").hide();
            } else if(identify === "产品信息"){
                $div.find(".hospital-list").hide();
                $div.find(".person-list").hide();
                $div.find(".product-list").show();
                $div.find(".report-list").hide();
            } else {

                $div.find(".hospital-list").hide();
                $div.find(".person-list").hide();
                $div.find(".product-list").hide();
                $div.find(".report-list").show();
                whichTable();
                resetTableWidth();
            }
        }
    };

    $(function(){

        events: {
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


        }
    });
})(jQuery, window);
