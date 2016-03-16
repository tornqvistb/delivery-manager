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
	
	$('input.trig-enter').keydown(function(e) {
	    var code = e.keyCode || e.which;

	    if (code === 9) {  
	        e.preventDefault();
	        $('form#reg-equipment').submit();
	    }
	});
	
});

function PrintLabels()
{
     var WinPrint = window.open('', '', 'letf=100,top=100,width=600,height=600');
     var intOrderNo = retrieve("int-order-no");
     var extOrderNo =  retrieve("ext-order-no");
     WinPrint.document.write("<html><head></head><body>");
     WinPrint.document.write("<div>");
     WinPrint.document.write("Lanteam order:<br />");
     WinPrint.document.write("<span style='font-weight:bold;font-size:larger'>" + intOrderNo + "</span>");
     WinPrint.document.write("<br /><br />");
     WinPrint.document.write("Intraservice order:<br />");
     WinPrint.document.write("<span style='font-weight:bold;font-size:larger'>" + extOrderNo + "</span>");
     WinPrint.document.write("</div></body>");
     WinPrint.document.close();
     WinPrint.focus();
     WinPrint.print();
     //WinPrint.close()   
}

function retrieve(id) {
	var tdElem = document.getElementById ( id );
	var tdText = tdElem.innerText | tdElem.textContent;
    return tdText;
}
