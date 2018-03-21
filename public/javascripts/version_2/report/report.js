// var overflowTableContainer = document.getElementsByClassName("inside-div");
var overflowTableContainer = $(".inside-div");
var overflowTableContainerEle = overflowTableContainer[0];
var tableHeade = $(".overflow-table-head");
// var table = document.getElementsByClassName("overflow-table");
var table = $(".overflow-table");
var order = 0;
var tablelen = table.length;
var oldTableWidth = 0;
var scrollBarLength = 0;
// 判断是哪个有滚动条的表格显示
function whichTable() {
    if (overflowTableContainer[0].offsetWidth == 0) {
        if (overflowTableContainer[1].offsetWidth == 0) {
            overflowTableContainerEle = overflowTableContainer[2];
        } else {
            overflowTableContainerEle = overflowTableContainer[1];
        }
    } else {
        overflowTableContainerEle = overflowTableContainer[0];
    }
    scrollBarLength = overflowTableContainerEle.offsetWidth - overflowTableContainerEle.clientWidth;
    overflowTableContainerEle.style.paddingRight = scrollBarLength + "px";
};
whichTable();
// 消除滚动条
function resetTableWidth() {
    oldTableWidth = 0;
    console.log("resetTableWidth function is running");
    for (var i = 0; i < tablelen; i++) {
        oldTableWidth = overflowTableContainer[i].clientWidth;
        table[i].style.width = tableHeade[i].clientWidth + "px";
        // table[i].style.width = oldTableWidth + scrollBarLength + "px";
    }
};

$(window).resize(function() {
    resetTableWidth();
});
// 点击li时切换右边对应内容，并隐藏滚动条
$(".menu-tab li").click(function() {
    $(".menu-tab li").removeClass("active")
    $(this).addClass("active");
    order = $(this).index();
    $(".content .content-container").addClass("unvisible");
    $(".container_" + order).removeClass("unvisible");
    resetTableWidth();
    whichTable();
})
resetTableWidth();
