(function($, w){
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
