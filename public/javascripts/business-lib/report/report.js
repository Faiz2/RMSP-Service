(function($, w){
    var f = new Facade();
    var $report_tab_li = $('#report-tab li');

    var phase_switch = function(zone, text) {
        switch (text) {
            case "周期1":
                return zone + "1";
                break;
            case "周期2":
                return zone + "2";
                break;
            default:
                console.warn("tab is more")
        }
    };

    $(function() {

        $report_tab_li.click(function() {
            market_sales($(this).find('a').text());
            deputy_report($(this).find('a').text());
            manager_report($(this).find('a').text());
            allot_report($(this).find('a').text());
            sales_customer($(this).find('a').text());
            sales_deputy($(this).find('a').text());
            sales_product($(this).find('a').text());
        });

        if (cycle1_status) {
            $('div[name="cycle1-style-controller"]').show();
            $report_tab_li.eq(0).click();
        }
        if (cycle2_status) {
            $('div[name="cycle2-style-controller"]').show();
            $report_tab_li.eq(1).click();
        }


        $('#cycle1-down').click(function() {
            var name = $.cookie('reportname1');
            window.open('/report/download/' + name);
        });

        $('#cycle2-down').click(function() {
            var name = $.cookie('reportname2');
            window.open('/report/download/' + name);
        })
    });

    function allot_select_change(hk, dk) {
        var li_text = $report_tab_li.filter('.active').find('a').text();
        var $zone = $('#' + phase_switch('allot-report-cycle', li_text));
        var $hospital_select = $zone.find('select[name="hospital"]');
        var $dimension_select = $zone.find('select[name="dimension"]');
        $hospital_select.find('option[value="'+ hk +'"]').attr('selected', true);
        $dimension_select.find('option[value="'+ dk +'"]').attr('selected', true);

        $hospital_select.change(function () {
            allot_report(li_text, $(this).val(), $dimension_select.val());

        });

        $dimension_select.change(function () {
            allot_report(li_text, $hospital_select.val(), $(this).val());
        });
    }

    function sales_customer_select_change(hk, pk) {
        var li_text = $report_tab_li.filter('.active').find('a').text();
        var $zone = $('#' + phase_switch('sales-details-cycle', li_text));
        var $hospital_select = $zone.find('select[name="hospital"]');
        var $product_select = $zone.find('div[name="customer"] select[name="product"]');
        $hospital_select.find('option[value="'+ hk +'"]').attr('selected', true);
        $product_select.find('option[value="'+ pk +'"]').attr('selected', true);

        $hospital_select.change(function () {
            sales_customer(li_text, $(this).val(), $product_select.val());

        });

        $product_select.change(function () {
            sales_customer(li_text, $hospital_select.val(), $(this).val());
        });
    }

    function sales_deputy_select_change(dk, pk) {
        var li_text = $report_tab_li.filter('.active').find('a').text();
        var $zone = $('#' + phase_switch('sales-details-cycle', li_text));
        var $people_select = $zone.find('select[name="people"]');
        var $product_select = $zone.find('div[name="deputy"] select[name="product"]');
        $people_select.find('option[value="'+ dk +'"]').attr('selected', true);
        $product_select.find('option[value="'+ pk +'"]').attr('selected', true);

        $people_select.change(function () {
            sales_deputy(li_text, $(this).val(), $product_select.val());

        });

        $product_select.change(function () {
            sales_deputy(li_text, $people_select.val(), $(this).val());
        });
    }

    function market_sales(text) {
       // var li_text = $report_tab_li.filter('.active').find('a').text();
       var json = JSON.stringify(f.parameterPrefixModule.conditions({'cycle': text, 'user': $.cookie('user')}));
       var $zone = $('#' + phase_switch('market-sales-report-cycle', text));
       f.ajaxModule.baseCall('report/querymarketsales', json, 'POST', function(r) {
           if(r.status === 'ok') {
               var html = r.result.data.marketsalesreport;
               $zone.find('div[name="market-sales"]').empty().html(html);
               var $tds = $zone.find('.num-format');
               $.each($tds, function(i, v) {
                   var $td = $(v);
                   $td.text(f.thousandsModule.formatNum($td.text().split('.')[0]));
               });
           }else {
               $zone.find('div[name="market-sales"]').empty().html('<h4 style="">暂无数据！</h4>')
           }
       });
   }
   
    function deputy_report(text) {
       // var li_text = $report_tab_li.filter('.active').find('a').text();
       var json = JSON.stringify(f.parameterPrefixModule.conditions({'cycle': text, 'user': $.cookie('user')}));
       var $zone = $('#' + phase_switch('delegate-report-cycle', text));

       f.ajaxModule.baseCall('report/querydelegate', json, 'POST', function(r) {
           if(r.status === 'ok') {
               var html = r.result.data.deputyreport;
               $zone.find('div[name="deputy"]').empty().html(html);
           }else {
               $zone.find('div[name="deputy"]').empty().html('<h4 style="">暂无数据！</h4>')
           }
       });
   }

    function manager_report(text) {
       // var li_text = $report_tab_li.filter('.active').find('a').text();
       var json = JSON.stringify(f.parameterPrefixModule.conditions({'cycle': text, 'user': $.cookie('user')}));
       var $zone = $('#' + phase_switch('manager-report-cycle', text));

       f.ajaxModule.baseCall('report/querymanager', json, 'POST', function(r) {
           if(r.status === 'ok') {
               var html = r.result.data.managerreport;
               $zone.find('div[name="manager"]').empty().html(html);
               var $tds = $zone.find('.num-format');
               $.each($tds, function(i, v) {
                   var $td = $(v);
                   $td.text(f.thousandsModule.formatNum($td.text().split('.')[0]));
               });
           }else {
               $zone.find('div[name="manager"]').empty().html('<h4 style="">暂无数据！</h4>')
           }
       });
   }

    function allot_report(text, hospital, dimension) {
       // var li_text = $report_tab_li.filter('.active').find('a').text();
       var json = JSON.stringify(f.parameterPrefixModule.conditions({'cycle': text,
           'user': $.cookie('user'),
           'hospital': hospital === '' ? undefined : hospital,
           'dimension': dimension === '' ? undefined : dimension
       }));
       var $zone = $('#' + phase_switch('allot-report-cycle', text));

       f.ajaxModule.baseCall('report/queryaloot', json, 'POST', function(r) {
           if(r.status === 'ok') {
               var html = r.result.data.allotreport;
               $zone.find('div[name="allot"]').empty().html(html);
               var $tds = $zone.find('.num-format');
               $.each($tds, function(i, v) {
                   var $td = $(v);
                   $td.text(f.thousandsModule.formatNum($td.text()));
               });
               allot_select_change(hospital, dimension);
           }else {
               $zone.find('div[name="allot"]').empty().html('<h4 style="">暂无数据！</h4>')
           }
       });

   }
    
    function sales_customer(text, hospital, product) {
        // var li_text = $report_tab_li.filter('.active').find('a').text();
        var json = JSON.stringify(f.parameterPrefixModule.conditions({'cycle': text,
            'user': $.cookie('user'),
            'hospital': hospital === '' ? undefined : hospital,
            'product': product === '' ? undefined : product
        }));
        var $zone = $('#' + phase_switch('sales-details-cycle', text));

        f.ajaxModule.baseCall('report/querysalsecustomer', json, 'POST', function(r) {
            if(r.status === 'ok') {
                var html = r.result.data.salescustomerreport;
                $zone.find('div[name="sales-customer"]').empty().html(html);
                var $tds = $zone.find('.num-format');
                $.each($tds, function(i, v) {
                    var $td = $(v);
                    $td.text(f.thousandsModule.formatNum($td.text().split('%')[0]));
                });
                sales_customer_select_change(hospital, product);
            }else {
                $zone.find('div[name="allot"]').empty().html('<h4 style="">暂无数据！</h4>')
            }
        });
    }

    function sales_deputy(text, people, product) {
        // var li_text = $report_tab_li.filter('.active').find('a').text();
        var json = JSON.stringify(f.parameterPrefixModule.conditions({'cycle': text,
            'user': $.cookie('user'),
            'people': people === '' ? undefined : people,
            'product': product === '' ? undefined : product
        }));
        var $zone = $('#' + phase_switch('sales-details-cycle', text));

        f.ajaxModule.baseCall('report/querysalsedeputy', json, 'POST', function(r) {
            if(r.status === 'ok') {
                var html = r.result.data.salesdeputyreport;
                $zone.find('div[name="sales-deputy"]').empty().html(html);
                var $tds = $zone.find('.num-format');
                $.each($tds, function(i, v) {
                    var $td = $(v);
                    $td.text(f.thousandsModule.formatNum($td.text().split('%')[0]));
                });
                sales_deputy_select_change(people, product);
            }else {
                $zone.find('div[name="sales-deputy"]').find("table").empty();
            }
        });
    }

    function sales_product(text) {
        // var li_text = $report_tab_li.filter('.active').find('a').text();
        var json = JSON.stringify(f.parameterPrefixModule.conditions({'cycle': text, 'user': $.cookie('user')}));
        var $zone = $('#' + phase_switch('sales-details-cycle', text));

        f.ajaxModule.baseCall('report/querysalseproduct', json, 'POST', function(r) {
            if(r.status === 'ok') {
                var html = r.result.data.salesproductreport;
                $zone.find('div[name="sales-porduct"]').empty().html(html);
                var $tds = $zone.find('.num-format');
                $.each($tds, function(i, v) {
                    var $td = $(v);
                    $td.text(f.thousandsModule.formatNum($td.text().split('%')[0]));
                });
            }else {
                $zone.find('div[name="sales-porduct"]').empty().html('<h4 style="">暂无数据！</h4>')
            }
        });
    }


})(jQuery, window);