(function($,w){

    $(function(){
       // 点击account 退出
       $('.navbar-exit').click(function(e){
           $('.exit-area').toggle();
           $(document).one("click", function(){
               $(".exit-area").hide();
           });
           e.stopPropagation();
       });

        $('.exit-area').click(function(e){
            e.stopPropagation();
            w.location.href = "/"
        });

        $('p[name="account"]').text($.cookie("user"));
    });
})(jQuery,window);
