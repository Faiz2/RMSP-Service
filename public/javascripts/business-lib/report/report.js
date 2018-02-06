(function($, w){

    const check_phase = function() {
        let json = JSON.stringify(f.parameterPrefixModule.conditions({
            "uuid": $('input:hidden[name="uuid"]').val(),
            "phase": parseInt($('input:hidden[name="phase"]').val())
        }));
        f.ajaxModule.baseCall('/phase/status', json, 'POST', function (r) {
            if(r.status === 'ok' && r.result.data.flag === 'ok') {
                let $lis = $('#report-tab');
                // parseInt($('input:hidden[name="phase"]').val())
                if(r.result.data.phase > 1) {
                    $lis.find('li').eq(1).show();
                }
            }
        })
    };

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

        // check_phase();
    });

})(jQuery, window);