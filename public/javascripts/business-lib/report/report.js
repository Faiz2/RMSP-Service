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
        market_sales();
        deputy_report();
        manager_report();
        allot_report();
        sales_customer();
        sales_deputy();
        sales_product();
    });

    function allot_select_change(hk, dk) {
        var li_text = $report_tab_li.filter('.active').find('a').text();
        var $zone = $('#' + phase_switch('allot-report-cycle', li_text));
        var $hospital_select = $zone.find('select[name="hospital"]');
        var $dimension_select = $zone.find('select[name="dimension"]');
        $hospital_select.find('option[value="'+ hk +'"]').attr('selected', true);
        $dimension_select.find('option[value="'+ dk +'"]').attr('selected', true);

        $hospital_select.change(function () {
            allot_report($(this).val(), $dimension_select.val());

        });

        $dimension_select.change(function () {
            allot_report($hospital_select.val(), $(this).val());
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
            sales_customer($(this).val(), $product_select.val());

        });

        $product_select.change(function () {
            sales_customer($hospital_select.val(), $(this).val());
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
            sales_deputy($(this).val(), $product_select.val());

        });

        $product_select.change(function () {
            sales_deputy($people_select.val(), $(this).val());
        });
    }

    function market_sales() {
       var li_text = $report_tab_li.filter('.active').find('a').text();
       var json = JSON.stringify(f.parameterPrefixModule.conditions({'cycle': li_text, 'user': $.cookie('user')}));
       var $zone = $('#' + phase_switch('market-sales-report-cycle', li_text));
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
               $zone.find('div[name="market-sales"]').empty().html('<h1 style="color: red">Error.</h1>')
           }
       });
   }
   
    function deputy_report() {
       var li_text = $report_tab_li.filter('.active').find('a').text();
       var json = JSON.stringify(f.parameterPrefixModule.conditions({'cycle': li_text, 'user': $.cookie('user')}));
       var $zone = $('#' + phase_switch('delegate-report-cycle', li_text));

       f.ajaxModule.baseCall('report/querydelegate', json, 'POST', function(r) {
           if(r.status === 'ok') {
               var html = r.result.data.deputyreport;
               $zone.find('div[name="deputy"]').empty().html(html);
           }else {
               $zone.find('div[name="deputy"]').empty().html('<h1 style="color: red">Error.</h1>')
           }
       });
   }

    function manager_report() {
       var li_text = $report_tab_li.filter('.active').find('a').text();
       var json = JSON.stringify(f.parameterPrefixModule.conditions({'cycle': li_text, 'user': $.cookie('user')}));
       var $zone = $('#' + phase_switch('manager-report-cycle', li_text));

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
               $zone.find('div[name="manager"]').empty().html('<h1 style="color: red">Error.</h1>')
           }
       });
   }

    function allot_report(hospital, dimension) {
       var li_text = $report_tab_li.filter('.active').find('a').text();
       var json = JSON.stringify(f.parameterPrefixModule.conditions({'cycle': li_text,
           'user': $.cookie('user'),
           'hospital': hospital === '' ? undefined : hospital,
           'dimension': dimension === '' ? undefined : dimension
       }));
       var $zone = $('#' + phase_switch('allot-report-cycle', li_text));

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
               $zone.find('div[name="allot"]').empty().html('<h1 style="color: red">Error.</h1>')
           }
       });

   }
    
    function sales_customer(hospital, product) {
        var li_text = $report_tab_li.filter('.active').find('a').text();
        var json = JSON.stringify(f.parameterPrefixModule.conditions({'cycle': li_text,
            'user': $.cookie('user'),
            'hospital': hospital === '' ? undefined : hospital,
            'product': product === '' ? undefined : product
        }));
        var $zone = $('#' + phase_switch('sales-details-cycle', li_text));

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
                $zone.find('div[name="allot"]').empty().html('<h1 style="color: red">Error.</h1>')
            }
        });
    }

    function sales_deputy(people, product) {
        var li_text = $report_tab_li.filter('.active').find('a').text();
        var json = JSON.stringify(f.parameterPrefixModule.conditions({'cycle': li_text,
            'user': $.cookie('user'),
            'people': people === '' ? undefined : people,
            'product': product === '' ? undefined : product
        }));
        var $zone = $('#' + phase_switch('sales-details-cycle', li_text));

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
                $zone.find('div[name="allot"]').empty().html('<h1 style="color: red">Error.</h1>')
            }
        });
    }

    function sales_product() {
        var li_text = $report_tab_li.filter('.active').find('a').text();
        var json = JSON.stringify(f.parameterPrefixModule.conditions({'cycle': li_text, 'user': $.cookie('user')}));
        var $zone = $('#' + phase_switch('sales-details-cycle', li_text));

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
                $zone.find('div[name="sales-porduct"]').empty().html('<h1 style="color: red">Error.</h1>')
            }
        });
    }


})(jQuery, window);