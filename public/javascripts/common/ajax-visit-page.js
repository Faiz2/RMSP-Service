(function($, w) {


    $('ul[class="sidebar-menu"] li a').click(function(){
        replace_style($(this));
        var li_filter = $(this).attr('pharbers-filter');
        switch (li_filter) {
            case 'market-info':
                replace_page("market_info");
                w.rsmp_page = "market_info";
                break;
            case 'brd':
                replace_page("brd_info");
                w.rsmp_page = "brd_info";
                break;
            case 'product-info':
                replace_page("product_info");
                w.rsmp_page = "product_info";
                break;
            case 'business-decision':
                replace_page("business_decision");
                w.rsmp_page = "business_decision";
                break;
            case 'management-decision':
                replace_page('management_decision');
                w.rsmp_page = "management_decision";
                break;
            case 'report':
                replace_page("report");
                w.rsmp_page = "report";
                break;
            case 'help':
                w.rsmp_page = "help";
                break;
            default:
                w.rsmp_page = "unknown";
        }


    });

    var replace_page = function(link) {
        $.get("/" + link, {}, function(r){
            var $content = $('div[pharbers-filter="'+ AjaxVisVit.config.Body_Id +'"]');
            $content.empty();
            $content.html(r);
        });
    };

    var replace_style = function (doc) {
        var cur_li = doc.parent().not('.treeview');
        var li = $('.sidebar-menu').find('li').not('.header').not('.treeview');
        li.attr('class', '');
        cur_li.attr('class', 'active');
    }
})(jQuery, window);