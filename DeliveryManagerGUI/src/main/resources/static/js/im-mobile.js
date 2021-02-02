$(document).ready(
function() {
	var signaturePad;
	if (document.querySelector("#signature-pad")) {
		signaturePad = new SignaturePad(document
				.getElementById('signature-pad'));
		$('#signature-data').val(signaturePad.toDataURL('image/png'));
	}
	$('#click').click(function() {
		var data = signaturePad.toDataURL('image/png');
		$('#output').val(data);

		$("#sign_prev").show();
		$("#sign_prev").attr("src", data);
	});

	$('#clear-signature').click(function() {
		signaturePad.clear();
	});
/*
	$('#clear-upload').click(function() {
		document.getElementById('file-input-label').innerHTML = 'your tip has been submitted!';
		console.log("hej 1q");
		bsCustomFileInput.destroy();
		console.log("hej 2q");
		bsCustomFileInput.init();
		console.log("hej 3q");
		var input = document.getElementById('file-input');
	    var label = input.parentNode.querySelector('.custom-file-input');
	    console.log("hej 4q");
	    if (label) {
	      var element = findFirstChildNode(label);
	      element.textContent = "Hej och hå";
		  console.log("hej 5q");
	    }

	});
	
	  var findFirstChildNode = function findFirstChildNode(element) {
		    if (element.childNodes.length > 0) {
		      var childNodes = [].slice.call(element.childNodes);

		      for (var i = 0; i < childNodes.length; i++) {
		        var node = childNodes[i];

		        if (node.nodeType !== 3) {
		          return node;
		        }
		      }
		    }

		    return element;
		  };
*/
	
	function showSectionsByStatus(status) {
		if (status == 'delivered') {
			$('#receiver-section').show();
			$('#signature-section').show();
			$('#clearify-section').hide();
			$('#comment-section').hide();
			$('#submit-section').show();
		} else if (status != '') {
			$('#receiver-section').hide();
			$('#signature-section').hide();
			$('#clearify-section').hide();
			$('#comment-section').show();
			$('#submit-section').show();
		}
		signaturePad.clear();
		if ($('#receiver').val() == 'Annan' && $('#receiver').is(":visible")) {
			$('#clearify-section').show();
		}		
	}
	
	$('#confirm-btn').click(function() {
		if ($('#receiver').val() == 'Annan') {
			if ($('#name-clarification').val() == '') {
				$('#error-client').show();
				$('#error-client').text("Namnförtydligande saknas");
				return false;
			}
		}
		if ($('#status').val() != 'delivered') {
			if ($('#comment').val() == '') {
				$('#error-client').show();
				$('#error-client').text("Kommentar saknas");
				return false;
			}
		} else {
			var currSign = signaturePad.toDataURL('image/png');
			var blankSign = $('#signature-data').val();
			console.log("curr: " + currSign);
			console.log("blank: " + blankSign);
			if (currSign.localeCompare(blankSign) == 0) {
				$('#error-client').show();
				$('#error-client').text("Signatur saknas");
				return false;				
			}
						
		}
		var data = signaturePad.toDataURL('image/png');
		$('#signature-data').val(data);		
		document.getElementById("confirmation-form").submit();
	});

	$('#status').change(function() {
		showSectionsByStatus($(this).val());
		/*
		if ($(this).val() == 'delivered') {
			$('#receiver-section').show();
			$('#signature-section').show();
			$('#clearify-section').hide();
			$('#comment-section').hide();
			$('#submit-section').show();
		} else if ($(this).val() != '') {
			$('#receiver-section').hide();
			$('#signature-section').hide();
			$('#clearify-section').hide();
			$('#comment-section').show();
			$('#submit-section').show();
		}
		signaturePad.clear();
		if ($('#receiver').val() == 'Annan' && $('#receiver').is(":visible")) {
			$('#clearify-section').show();
		}
		*/
	});

	$('#receiver').change(function() {
		if ($(this).val() == 'Annan') {
			$('#clearify-section').show();
		} else {
			$('#clearify-section').hide();
		}
		signaturePad.clear();
	});

	$('#receiver-section').show();
	$('#signature-section').show();
	if ($('#receiver').val() == 'Annan') {
		$('#clearify-section').show();
	} else {
		$('#clearify-section').hide();
	}
	$('#comment-section').hide();
	$('#submit-section').show();
	
	showSectionsByStatus($('#status').val())
})
