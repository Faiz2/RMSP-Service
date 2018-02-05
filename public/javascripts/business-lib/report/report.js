(function($, w){
    $(function(){
        $('#download-report').click(function(){
            let json = JSON.stringify(f.parameterPrefixModule.conditions({
                "uuid": $('input[name="uuid"]').val(),
                "phase": parseInt($('input[name="phase"]').val())
            }));
            f.alert.loading();
            f.ajaxModule.baseCall('/submit/create', json, 'POST', function(r) {
                if(r.status === 'ok' && r.result.data.flag === 'ok') {
                    layer.closeAll('loading');
                    w.window.open('/download/report/' + r.result.data.path);
                    // w.location = "/report/download/" + r.result.data.path
                }
            });
        });
    });

})(jQuery, window);