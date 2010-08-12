$(function(){
    $("tbody tr:even").css("background-color", "#ECE9D8");
    var tds = $("tbody td:even");
    tds.click(function() {
        var inputobj = $("<input type='text'>");
        var td = $(this);
        if(td.children("input").length > 0) {
            return false;
        }
        var text = td.html();
        td.html("");
        inputobj.css("border-width", "0").css("font-size", "15px")
        .css("background-color", td.css("background-color")).width(td.width())
        .val(text).appendTo(td).click(function(){
            return false;
        });
        inputobj.trigger("focus").trigger("select");
        inputobj.keyup(function(event) {
            var code = event.which;
            if(code == 13) {
                var newText = $(this).val();
                td.html(newText);
            }
            if(code == 27) {
                td.html(text);
            }
        })
    });
});