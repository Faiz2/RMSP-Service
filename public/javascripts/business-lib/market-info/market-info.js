(function($, w){
    var f = new Facade();
    $(function () {
        var json = JSON.stringify(f.parameterPrefixModule.conditions({"cycle": "周期1"}));
        load_market_info(json);
    });

    function load_market_info (json) {
        f.ajaxModule.baseCall("/market_info", json, "POST", function(r){
            var $new_content = $('#market-info-new-content');
            var $client_content = $('#market-info-client-info-content');
            $new_content.empty();
            $client_content.empty();
            if (r.status === 'ok') {
                $new_content.html(r.result.data.news);
                $client_content.html(r.result.data.clientInfo);
            } else {
                $new_content.html('<h1 style="color: red">Error.</h1>');
                $client_content.html('<h1 style="color: red">Error.</h1>');
            }
            $.each($('.news tr'), function(i, v) {
                var last_td = $(v).find('td:last');
                var num = f.thousandsModule.formatNum(last_td.text());
                last_td.text(num);
            });
            $.each($('.client tr'), function(i, v) {
                var last_td_3 = $(v).find('td').eq(3);
                var last_td_4 = $(v).find('td').eq(4);
                var last_td_5 = $(v).find('td').eq(5);
                var last_td_6 = $(v).find('td').eq(6);
                var num_td_3 = f.thousandsModule.formatNum(last_td_3.text().split(".")[0]);
                var num_td_4 = f.thousandsModule.formatNum(last_td_4.text().split(".")[0]);
                var num_td_5 = f.thousandsModule.formatNum(last_td_5.text().split(".")[0]);
                var num_td_6 = f.thousandsModule.formatNum(last_td_6.text().split(".")[0]);
                last_td_3.text(num_td_3);
                last_td_4.text(num_td_4);
                last_td_5.text(num_td_5);
                last_td_6.text(num_td_6);
            });
        }, function(e){console.info(e)})
    }
})(jQuery, window);