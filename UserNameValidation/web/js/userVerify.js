$(function() {
    var unode = $("#userName");
    var vbtn = $("#verifyButton");
    vbtn.click(function() {
        var uname = unode.val();
        if(uname == "") {
            alert("must input user name");
        } else {
            $.get
        }
        alert("");
    });
    unode.keyup(function() {
        var value = unode.val();
        if(value == "") {
            unode.addClass("userText");
        } else {
            unode.removeClass("userText");
        }
    });
});