// var overflowTableContainer = document.getElementsByClassName("inside-div");
(function($,w){
    $(function(){
        init: {
        }
        events: {
            //导出Excel
            $('#exportExcel').click(function(){

                f.alert.loading(true);
                setTimeout(function(){
                    f.alert.loading(false);
                    layer.alert('已下载');
                },1000)


            });
            // 显示导出/导入excel区域按钮
            $('div[name = "toggle-export"]').click(function(e) {
                $('div[name="area-export"]').toggle();
                $(document).one("click", function(){
                    $('div[name="area-export"]').hide();
                });
                e.stopPropagation();
            });

            // 导入/导出区域的按钮冒泡阻止
            $('div[name="area-export"]').click(function(e) {
                e.stopPropagation();
            });

            $('button[name="go-phrase"]').click(function(){
                // w.location.href = "/home/" + $('input[name="uuid"]').val() + "/2"
                w.location.href = "/transition/" + $('input[name="uuid"]').val() + "/"+ $('input[name="phrase"]').val()
            });
        }
    });

})(jQuery, window);
