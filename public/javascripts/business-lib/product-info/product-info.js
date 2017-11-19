(function ($, w) {
    var f = new Facade();
    $(function(){
        var json = JSON.stringify(f.parameterPrefixModule.conditions({"cycle": "周期1"}));
        load_product_info(json);
    });

    function load_product_info(json) {
        f.ajaxModule.baseCall("/product_info", json, "POST", function(r){
            var $product_content = $('#product-info-content');
            $product_content.empty();
            if (r.status === 'ok') {
                $product_content.html(r.result.data)
            } else {
                $product_content.html('<h1 style="color: red">Error.</h1>');
            }
        });
    }
})(jQuery, window);