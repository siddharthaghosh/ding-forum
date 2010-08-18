function documentReadyFunction() {
	$(document).ready(function() {    			
		$('.fg-button').hover(
			function(){ $(this).removeClass('ui-state-default').addClass('ui-state-focus'); },
			function(){ $(this).removeClass('ui-state-focus').addClass('ui-state-default'); }
		);
		
		$("#categoryMenu").children().eq(1).addClass("fg-menu-data-hidden");
		$("#categoryMenu").children().eq(0).addClass("fg-button fg-button-icon-right ui-widget ui-state-default ui-corner-all");
		$("#categoryMenu").children().eq(0).children().eq(0).addClass("ui-icon ui-icon-triangle-1-s");
		
		$('#categoryMenu').children().eq(0).menu({ content: $('#categoryMenu').children().eq(1).html(), flyOut: true });
    });
}