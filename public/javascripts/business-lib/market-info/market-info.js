(function($, w){
    var f = new Facade();
    $(function () {


        var $cycle1_news = $('#cycle1-news');
        var $cycle2_news = $('#cycle2-news');
        var $cycle1_client = $('#cycle1-client');
        var $cycle2_client = $('#cycle2-client');

        $cycle1_news.click(function() {
            var json = JSON.stringify(f.parameterPrefixModule.conditions({"cycle": "周期1"}));
            load_market_cycle1_info(json);
        });

        $cycle1_client.click(function() {
            var json = JSON.stringify(f.parameterPrefixModule.conditions({"cycle": "周期1"}));
            load_market_cycle1_info(json);
        });

        $cycle2_news.click(function() {
            var json = JSON.stringify(f.parameterPrefixModule.conditions({"cycle": "周期2"}));
            load_market_cycle2_info(json);
        });

        $cycle2_client.click(function() {
            var json = JSON.stringify(f.parameterPrefixModule.conditions({"cycle": "周期2"}));
            load_market_cycle2_info(json);
        });

        if(cycle1_status) {
            $cycle2_news.attr('class', 'btn btn-default');
            $cycle2_client.attr('class', 'btn btn-default');
        }
        $cycle1_news.click();
    });

    function load_market_cycle1_info (json) {
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
                var $tds = $(v).find('td:gt(2)');
                $.each($tds, function(j, k) {
                    var $td = $(k);
                    $td.text(f.thousandsModule.formatNum($td.text()))
                });
            });
            $.each($('.client tr'), function(i, v) {
                var $tds = $(v).find('td:gt(2)');
                $.each($tds, function (j, k) {
                    var $td = $(k);
                    $td.text(f.thousandsModule.formatNum($td.text().split(".")[0]))
                });
            });
        })
    }

    function load_market_cycle2_info (json) {
        f.ajaxModule.baseCall("/market_info", json, "POST", function(r){
            var $new_content = $('#market-info-new-content');
            var $client_content = $('#market-info-client-info-content');
            $new_content.empty();
            $client_content.empty();
            if (r.status === 'ok') {
                $new_content.html(r.result.data.news);
                $client_content.html(r.result.data.clientInfo);
                $new_content.find('th[name="show-switch"]').hide();
                $new_content.find('td[name="show-switch"]').hide();
                $client_content.find('th[name="show-switch"]').hide();
                $client_content.find('td[name="show-switch"]').hide();
            } else {
                $new_content.html('<h1 style="color: red">Error.</h1>');
                $client_content.html('<h1 style="color: red">Error.</h1>');
            }
            $.each($('.news tr'), function(i, v) {
                var $tds = $(v).find('td:gt(2)');
                $.each($tds, function(j, k) {
                    var $td = $(k);
                    $td.text(f.thousandsModule.formatNum($td.text()))
                });
            });
            $.each($('.client tr'), function(i, v) {
                var $tds = $(v).find('td:gt(2)');
                $.each($tds, function (j, k) {
                    var $td = $(k);
                    $td.text(f.thousandsModule.formatNum($td.text().split(".")[0]))
                });
            });
        })
    }
})(jQuery, window);