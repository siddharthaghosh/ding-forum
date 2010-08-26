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
 * $.fn.hoverUnderline
 */
(function($) {

    $.fn.hoverUnderline = function() {

        return this.each(function() {
            $(this).hover(
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
        });

    };

})(jQuery);


/*
 * $.fn.topCategoryContainer
 */
(function($) {

    /*
     * $(".topcategory")
     */
    function topCategory() {

        function titlebar() {
            return this.each(function() {
                $(this).addClass("ui-widget-header");
                $("<span>").addClass("ui-icon ui-icon-home").prependTo($(this));
            });
        };

        function content() {
            return this.each(function() {
                $(this).addClass("ui-widget-content");
                if ($(this).children().size() == 0) {
                    $(this).css({
                        "display": "none"
                    });
                } else {
                    $(this).children("dl").addClass("ui-widget-content");
                    $(this).children("dl").last().css({
                        "border-bottom-style": "none"
                    });

                    $(this).children().each(function() {
                        $("<div>").addClass("clear").appendTo($(this));
                    });
                }
            });
        };
        
        return this.each(function() {

            $(this).addClass("ui-widget");

            titlebar.call($(this).children(".titlebar"));

            content.call($(this).children(".content"));

            $(this).find("a").hoverUnderline();

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

            var topCategoryList = topCategory.call($(this).children(".topcategory")).detach();

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

    // $(."top-toolbar")
    function topToolbar() {

        // $(".pager")
        function pager() {
            return this.each(function() {

                $(this).children(".prev").button({
                    icons: {
                        primary: "ui-icon-triangle-1-w"
                    }
                }).removeClass("ui-corner-all");

                $(this).children(".next").button({
                    icons: {
                        secondary: "ui-icon-triangle-1-e"
                    }
                }).removeClass("ui-corner-all");
            });
        };

        function sort() {
            return this.each(function() {

                $(this).children(".price-desc").button({
                    icons: {
                        secondary: "ui-icon-arrowthick-1-s"
                    }
                }).removeClass("ui-corner-all");

                $(this).children(".price-asc").button({
                    icons: {
                        secondary: "ui-icon-arrowthick-1-n"
                    }
                }).removeClass("ui-corner-all");
                
                $(this).children(".addtime").button().removeClass("ui-corner-all");

                $(this).children("a[href='#']").button("disable");
            });
        };


        return this.each(function() {
            $(this).addClass("ui-widget ui-widget-header");

            pager.call($(this).children(".pager"));
            sort.call($(this).children(".sort"));

            $("<div>").addClass("clear").appendTo($(this));
        });
    };

    // $(".thumbcontainer")
    function thumbContainer() {

        // $(".productthumb")
        function productThumb() {

            function brief() {
                return this.each(function() {
                    $(this).find("a").hoverUnderline();
                });
            };

            function price() {
                return this.each(function() {
                    $("<div>").addClass("clear").appendTo($(this).children(".marketprice"));

                    $("<div>").addClass("clear").appendTo($(this).children(".vipprice"));
                });
            };

            return this.each(function() {

                $(this).addClass("ui-widget ui-widget-content");

                brief.call($(this).children(".brief"));

                price.call($(this).children(".price"));

                $(this).bind("fillSpace", function() {
                    $(this).height($(this).parent().height());
                });
            });
        };

        return this.each(function() {

            var thumbList = $(this).children(".productthumb").detach();

            productThumb.call(thumbList);

            var row = Math.ceil(thumbList.size() / 3);
            for(var i=0;i<row;i++) {
                var innerGrid12 = $("<div>").addClass("grid_12 alpha omega").appendTo($(this)).css({
                    "margin-bottom": "10px"
                });
                $("<div>").addClass("grid_4 alpha").appendTo(innerGrid12);
                $("<div>").addClass("grid_4").appendTo(innerGrid12);
                $("<div>").addClass("grid_4 omega").appendTo(innerGrid12);
            }

            var pivot = 0;
            var thumbContainer = $(this);
            thumbList.each(function() {
                var row = Math.floor(pivot / 3);
                var column = pivot % 3;

                thumbContainer.children().eq(row).children().eq(column).append($(this));
                pivot++;
            });

            $(this).children().equalHeights();
            $(this).find(".productthumb").trigger("fillSpace");
            $("<div>").addClass("clear").appendTo($(this));
        });
    };

    $.fn.productShowcase = function() {
        
        return this.each(function() {
            $(this).addClass("grid_12");

            topToolbar.call($(this).children(".top-toolbar"));
        
            thumbContainer.call($(this).children(".thumbcontainer"));
        });

    };
    
})(jQuery);