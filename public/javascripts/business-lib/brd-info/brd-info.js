(function($, w){
    var f = new Facade();
    $(function () {
        var json = JSON.stringify(f.parameterPrefixModule.conditions({"cycle": "周期1"}));
        load_brd_info(json);
    });

    function load_brd_info (json) {
        f.ajaxModule.baseCall("/brd_info", json, "POST", function(r){
            var $brd_content = $('#brd-info-content');
            $brd_content.empty();
            if (r.status === 'ok') {
                $brd_content.html(r.result.data);
            } else {
                $brd_content.html('<h1 style="color: red">Error.</h1>');
            }
        }, function(e){console.info(e)})
    }
})(jQuery, window);