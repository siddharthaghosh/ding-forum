/*
 * $.fn.topCategoryContainer
 */
(function($) {

    /*
     * $(".ding-topCategory")
     */
    function topCategory() {
        return this.each(function() {

            $(this).addClass("ui-widget");

            var titlebar = $(this).children(".ding-topCategory-titlebar").addClass("ui-widget-header");
            $("<span>").addClass("ui-icon ui-icon-home").prependTo(titlebar);

            var content = $(this).children(".ding-topCategory-content").addClass("ui-widget-content");
            if (content.children().size() == 0) {
                content.css({
                    "display": "none"
                });
            } else {
                content.children("dl").addClass("ui-widget-content");
                content.children("dl").last().css({
                   "border-bottom-style": "none"
                });

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
                    });
                },
                function() {
                    $(this).css({
                        "text-decoration": "none"
                    });
                }
                );

            $(this).hover(
                function(){
                    $(this).find(".ui-widget-header").addClass("ui-state-active");
                //   $(this).find(".ui-widget-content").addClass("ui-state-default");
                },
                function() {
                    $(this).find(".ui-widget-header").removeClass("ui-state-active");
                //   $(this).find(".ui-widget-content").removeClass("ui-state-default");
                });
        });
    };

    $.fn.topCategoryContainer = function() {

        return this.each(function() {

            var topCategoryList = topCategory.call($(this).children(".ding-topCategory")).detach();

            var column = [
            $("<div>").addClass("grid_8").appendTo($(this)),
            $("<div>").addClass("grid_8").appendTo($(this))
            ];

            var pivot = 0;
            topCategoryList.each(function() {
                $(this).appendTo(column[pivot++ % 2]);
            });

            $("<div>").addClass("clear").appendTo($(this));

        });

    };

})(jQuery);

(function($) {

    $.fn.productCase = function() {

        return this.each(function() {

            $(this).addClass("ui-widget ui-widget-content");

            $(this).find(".brief a").hover(
                function() {
                    $(this).css({
                        "text-decoration": "underline"
                    });
                },
                function() {
                    $(this).css({
                        "text-decoration": "none"
                    });
                }
                );

            $("<div>").css({
                "clear": "both"
            }).appendTo($(this).children(".price"));

            $("<div>").css({
                "clear": "both"
            }).appendTo($(this).children(".toolbar"));

            $(this).find(".toolbar a").button().removeClass("ui-corner-all");
            
        });
    };

})(jQuery);
