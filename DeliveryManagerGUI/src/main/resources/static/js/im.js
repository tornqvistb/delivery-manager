/* globals gbg, gbgPhone_parseNumber, gbgPhone_isValidNumber, gbgPhone_isMobileNumber, gbgPhone_isFixedNumber */

$(function() { // Makes sure the code contained doesn't run until
	// all the DOM elements have loaded

	$('#orderLineId').change(function() {
		$('#with-serial-no').hide();
		$('#no-serial-no').hide();
		$('#' + $(this).find(':selected').data('art-type')).show();
	});

	$('#orderLineId').each(function() {
		$('#with-serial-no').hide();
		$('#no-serial-no').hide();
		$('#' + $(this).find(':selected').data('art-type')).show();
	});

	$(function() {
		var includes = $('.include');
		jQuery.each(includes, function() {
			var file = $(this).data('include') + '.html';
			$(this).load(file);
		});
	});
	if ($("#serienr").length) {
		$("#serienr").focus();
		 $('html, body').animate({
             scrollTop: $("#registration-area").offset().top
         }, 500);
	}
});
