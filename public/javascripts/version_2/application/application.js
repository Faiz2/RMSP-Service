(function($,w){

    $(function(){
       // 点击account 退出
       $('p[name="account"]').click(function(e){
           $('.exit-area').toggle();
           $(document).one("click", function(){
               $(".exit-area").hide();
           });
           e.stopPropagation();
       });

        $('.exit-area').click(function(e){
            e.stopPropagation();
        })
    });
})(jQuery,window);