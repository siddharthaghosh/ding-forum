/*
 * $.fn.equalHeights
 *
 * by filamentgroup.com
 */

(function($) {

    $.fn.equalHeights = function(px) {
        $(this).each(function(){
            var currentTallest = 0;
            $(this).children().each(function(i){
                if ($(this).height() > currentTallest) {
                    currentTallest = $(this).height();
                }
            });
            if (!px || !Number.prototype.pxToEm) currentTallest = currentTallest.pxToEm(); //use ems unless px is specified
            // for ie6, set height since min-height isn't supported
            if ($.browser.msie && $.browser.version == 6.0) {
                $(this).children().css({
                    'height': currentTallest
                });
            }
            $(this).children().css({
                'min-height': currentTallest
            });
        });
        return this;
    };

    Number.prototype.pxToEm = String.prototype.pxToEm = function(settings){
        //set defaults
        settings = jQuery.extend({
            scope: 'body',
            reverse: false
        }, settings);

        var pxVal = (this == '') ? 0 : parseFloat(this);
        var scopeVal;
        var getWindowWidth = function(){
            var de = document.documentElement;
            return self.innerWidth || (de && de.clientWidth) || document.body.clientWidth;
        };

        if (settings.scope == 'body' && $.browser.msie && (parseFloat($('body').css('font-size')) / getWindowWidth()).toFixed(1) > 0.0) {
            var calcFontSize = function(){
                return (parseFloat($('body').css('font-size'))/getWindowWidth()).toFixed(3) * 16;
            };
            scopeVal = calcFontSize();
        }
        else {
            scopeVal = parseFloat(jQuery(settings.scope).css("font-size"));
        };

        var result = (settings.reverse == true) ? (pxVal * scopeVal).toFixed(2) + 'px' : (pxVal / scopeVal).toFixed(2) + 'em';
        return result;
    };

})(jQuery);


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

/*
 * $.fn.productShowcase
 */
(function($) {

    function productCase() {
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
            }).appendTo($(this).find(".marketPrice"));

            $("<div>").css({
                "clear": "both"
            }).appendTo($(this).find(".vipPrice"));

            $("<div>").css({
                "clear": "both"
            }).appendTo($(this).children(".toolbar"));

            $(this).find(".toolbar a").button().removeClass("ui-corner-all");

            $(this).bind("resize", function() {
                $(this).height($(this).parent().height());
            });
        });
    };

    $.fn.productShowcase = function() {
        
        return this.each(function() {
            $(this).addClass("grid_12");

            var productCaseList = productCase.call($(this).children(".productCase")).detach();

            var row = Math.ceil(productCaseList.size() / 3);
            for(var i=0;i<row;i++) {
                var innerGrid12 = $("<div>").addClass("grid_12 alpha omega").appendTo($(this)).css({
                    "margin-bottom": "10px"
                });
                $("<div>").addClass("grid_4 alpha").appendTo(innerGrid12);
                $("<div>").addClass("grid_4").appendTo(innerGrid12);
                $("<div>").addClass("grid_4 omega").appendTo(innerGrid12);
            }

            var pivot = 0;
            var showcase = $(this);
            productCaseList.each(function() {
                var row = Math.floor(pivot / 3);
                var column = pivot % 3;

                showcase.children().eq(row).children().eq(column).append($(this));
                pivot++;
            });

            $(this).children().equalHeights();
            $(this).find(".productCase").trigger("resize");
            //alert($(this).children().eq(0).children().eq(0).height());
        });

    };
    
})(jQuery);
