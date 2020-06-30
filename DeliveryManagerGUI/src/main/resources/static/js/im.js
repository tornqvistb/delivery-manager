/* globals gbg, gbgPhone_parseNumber, gbgPhone_isValidNumber, gbgPhone_isMobileNumber, gbgPhone_isFixedNumber */

$(function() { // Makes sure the code contained doesn't run until
	// all the DOM elements have loaded

	$('#orderLineId').change(function() {
		$('#with-serial-no').hide();
		$('#no-serial-no').hide();
		$('#input-stealing-id').show();
		$('#' + $(this).find(':selected').data('art-type')).show();
		if ($(this).find(':selected').data('restriction-code') == '4') {
			$('#input-stealing-id').hide();
		}
	});

	$('#orderLineId').each(function() {
		$('#with-serial-no').hide();
		$('#no-serial-no').hide();
		$('#input-stealing-id').show();
		$('#' + $(this).find(':selected').data('art-type')).show();
		if ($(this).find(':selected').data('restriction-code') == '4') {
			$('#input-stealing-id').hide();
		}
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
	
	$("#change-route-plan").click(function(){
		$("#route-plan-form").toggle();
	});

	$(".hidden-form").hide();
	

	$("#serienr").blur(function() {
		// Get input string into var
		var serialNr = document.getElementById("serienr").value;
		var array = serialNr.split(',');
		if (array.length > 1) {
			$('#serienr').val(array[0]);
		}
	});

	
});

function PrintLabels()
{
     var WinPrint = window.open('', '', 'letf=100,top=100,width=600,height=600');
     var intOrderNo = retrieve("int-order-no");
     var extOrderNo =  retrieve("ext-sales-no");     
     WinPrint.document.write("<html><head></head><body>");
     WinPrint.document.write("<div>");
     WinPrint.document.write("Visolit order:<br />");
     WinPrint.document.write("<span style='font-weight:bold;font-size:larger'>" + intOrderNo + "</span>");
     WinPrint.document.write("<br /><br />");
     WinPrint.document.write("Kundens order:<br />");
     WinPrint.document.write("<span style='font-weight:bold;font-size:larger'>" + extOrderNo + "</span>");
     WinPrint.document.write("</div></body>");
     WinPrint.document.close();
     WinPrint.focus();
     WinPrint.print();
}

function retrieve(id) {
	var tdElem = document.getElementById ( id );
    return tdElem.textContent;
}
$(function() {
	$("#deliveryAreaId").change(function() {
		$.ajax({
			type: "POST",
			url: "/DeliveryManagerGUI/order-list/view/getdaybyarea/" + $(this).find(":selected").val(), 
			dataType: "html",
			data: { parent: $(this).find(":selected").val() },
			success: function(result) {
				$("#deliveryDayId").html(result);
				$("#deliveryDayId").trigger("change");
			},
			error: function(result) {
				alert("Ett fel uppstod på servern när dagar skulle hämtas för aktuellt område: " + result.responseText);
			}
		});
	});
});

$(function() {
	$("#deliveryDayId").change(function() {
		$('#dateToDeliver').val($(this).find(":selected").val());
	});
});

$(function () {
    $("#customerGroupId").change(function () {
    	//if ($(this).val() > 0){
    		location.href = "/DeliveryManagerGUI/reports/delivery/changecustomer/" + $(this).val();
    	//}
    })
});

$( function() {
    $( "#sortable1, #sortable2" ).sortable({
    	connectWith: ".connectedSortable"
    }).disableSelection();
});

$(function() {
    $("#sortable1, #sortable2").fixedsortable({
        fixed: "> .static"
    });

});

function getListValuesAndSubmit() {
	const listItems = document.querySelectorAll('#sortable2 li');
	var itemValues = "";
	for (let i = 0; i < listItems.length; i++) {
		if (i > 0) {
			itemValues = itemValues.concat(";");
		}		
		itemValues = itemValues.concat(listItems[i].getAttribute('data-field-name'));
	}
	var listValuesInput = document.getElementById("listValues"); 
	listValuesInput.value = itemValues;
	document.getElementById("reportFieldsForm").submit();
}

