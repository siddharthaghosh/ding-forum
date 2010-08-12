$(function(){
    $(".mainMenu > a").click(function() {
        var ulNode = $(this).next("ul");
    //       if(ulNode.css("display") == "none") {
    //           ulNode.css("display", "block");
    //       }else {
    //           ulNode.css("display", "none");
    //       }
       ulNode.slideToggle();
       changeIcon($(this));
    });

    $(".hmainMenu").hover(function(){
        changeIcon($(this).children("a"));
        $(this).children("ul").slideDown();
    }, function(){
        changeIcon($(this).children("a"));
        $(this).children("ul").slideUp();
    });
});

function changeIcon(node) {
    if(node) {
        if(node.css("background-image").indexOf("collapsed.gif") >= 0) {
            node.css("background-image", "url('images/expanded.gif')");
        } else {
            node.css("background-image", "url('images/collapsed.gif')");
        }
    }
}


