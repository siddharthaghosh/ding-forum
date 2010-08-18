(function($) {

    $.fn.topCategory = function() {

        return this.each(function() {
            $(this).addClass("ui-widget");
            $(this).children(".ding-topCategory-titlebar").addClass("ui-widget-header");
            var content = $(this).children(".ding-topCategory-content").addClass("ui-widget-content");
            if (content.children().size() == 0) {
                content.css({
                   "display": "none"
                });
            } else {
                content.children("dl").addClass("ui-widget-content");
                content.children("dl").last().addClass("lastOne");

                content.children().each(function() {
                    $("<div>").css({
                        "clear": "both"
                    }).appendTo($(this));
                });
            }
            

            $(this).find("a").hover(
                function() {
                    $(this).css({
                        "text-decoration": "underline"
                    })
                },
                function() {
                    $(this).css({
                        "text-decoration": "none"
                    })
                }
                );
        });
    };

})(jQuery);

(function($) {

    $.fn.topCategoryContainer = function() {
        
        return this.each(function() {

            var topCategoryList = $(this).children(".ding-topCategory")
            .topCategory().detach();
            
            var grid0 = $("<div>").addClass("grid_8").appendTo($(this));
            var grid1 = $("<div>").addClass("grid_8").appendTo($(this));

            var pivot = 0;
            topCategoryList.each(function() {
                (pivot % 2 == 0) ? $(this).appendTo(grid0) : $(this).appendTo(grid1);
                pivot++;
            });

        });

    };

})(jQuery);
