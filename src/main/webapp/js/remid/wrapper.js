var remidExecCallback = undefined;

function remidExec(transactionRequest, options) {
	
	options = $.extend({
	    postAddress : "etrust/dialog",
		recycleAddress : "etrust/dialogContext",
		dialogTarget: "remidDialogTarget",
		createIframe : true,
		recycle : false,
		iframeWidth : "520px",
		iframeHeight : "400px",
		iframeScrolling : "auto",
		iframeBorder : "1px solid silver",
		iframeBorderRadius : "14px",
		iframeBackground : "white",
		iframeZindex : "99",
		overlayBackground : "black",
		overlayOpacity : "0.75",
		overlayZindex : "98",
		callback : undefined,
		errorCallback : undefined,
		matchReaderName : undefined
	}, options || {});
	
	var errorCallback = options.errorCallback;
	
	var existingForm = $("[name=remidDialogForm]");
	if(existingForm.length > 0) {
		existingForm.show();
	}
	else {
		$("body").append(
			$("<form>")
			.attr("name", "remidDialogForm")
			.attr("target", options.dialogTarget)
			.attr("method", "post")
			.attr("action", options.postAddress)
			.hide()
			.append(
					$("<input>")
					.attr("name", "transactionRequest")
					.attr("type", "hidden")
				)
			.append(
					$("<input>")
					.attr("name", "wrapperCallback")
					.attr("type", "hidden")
				)
             .append(
                        $("<input>")
                        .attr("name", "matchReaderName")
                        .attr("type", "hidden")
                        .attr("value", options.matchReaderName)
                )
		);
	}
	
	if(options.createIframe) {
		var existingControls = $("#remidDialogOverlay, #remidDialogIframe");
		if(existingControls.length > 0) {
			existingControls.show();
		}
		
		else {
			$("body").append(
				$("<div>")
				.attr("id", "remidDialogOverlay")
				.css("position", "fixed")
				.css("top", "0")
				.css("left", "0")
				.css("width", "100%")
				.css("height", "100%")
				.css("background", options.overlayBackground)
				.css("opacity", options.overlayOpacity)
				.css("z-index", options.overlayZindex)
			);
			
			$("body").append(
				$("<iframe name=\"" + options.dialogTarget + "\">")
				.attr("id", "remidDialogIframe")
				.attr("width", options.iframeWidth)
				.attr("height", options.iframeHeight)
				.attr("scrolling", options.iframeScrolling)
				.attr("frameborder", "0")
				.css("position", "fixed")
				.css("border", options.iframeBorder)
				.css("border-radius", options.iframeBorderRadius)
				.css("background", options.iframeBackground)
				.css("z-index", options.iframeZindex)
			);
		}

        $(window).bind('resize.modal', remidCenterDialog);
        remidCenterDialog();
	}
	
    remidExecCallback = options.callback;
	if(options.recycle) {
	    // Perform an ajax POST
	    var data = {
	        transactionRequest: transactionRequest
	    };
	    if (options.matchReaderName){
	        data.matchReaderName = options.matchReaderName;
	    }
        if (remidExecCallback) {
            data.wrapperCallback = "remidDialogCallback";
        }
	    $.ajax({
	        type: "POST",
	        url: options.recycleAddress,
	        data: data,
	        dataType: "json"
	    }).done(function(dialogContext) {
	        var iframeElt = $('[name=' + options.dialogTarget + ']');
	        if(!iframeElt.length) {
	            errorCallback && errorCallback('iframe not found: ' + options.dialogTarget);
	            return;
	        }
	        var runRecycle = iframeElt[0].contentWindow.runDialogContext;
	        if(!runRecycle) {
                errorCallback && errorCallback('iframe not initialized: ' + options.dialogTarget);
                return;
	        }
	        runRecycle(dialogContext);
	    }).fail(function(jqXHR) {
	        errorCallback && errorCallback('Ajax error ' + jqXHR.status + ': ' + jqXHR.statusText);
	    });
	}
	else {
	    // Fill a form in the page in order to perform a POST redirection
	    $("[name=remidDialogForm] > [name=transactionRequest]").val(
            transactionRequest);
        if (remidExecCallback) {
            $("[name=remidDialogForm] > [name=wrapperCallback]").val(
                    "remidDialogCallback");
        }
        $("[name=remidDialogForm]").submit();
	}
}

function remidDialogCallback(result) {
	$("#remidDialogOverlay, #remidDialogIframe").hide();
	$(window).unbind('resize.modal');
	remidExecCallback(result);
}


/*var injectedRemIDObject={};
injectedRemIDObject.getXMLData=function(){
	return '<?xml version="1.0" encoding="UTF-8"?> \
	<e:TransactionRequest xmlns:e="http://remid.eservices.morpho.com/etrust" \
		xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" \
		xsi:schemaLocation="http://remid.eservices.morpho.com/etrust etrust.xsd"> \
		<e:ApplicationID>eGovPlugin</e:ApplicationID> \
		<e:TransactionID>01FF02FE03FD04FC</e:TransactionID> \
		<e:RedirectURL>..\
		</e:RedirectURL>\
		<e:Locale>en</e:Locale>\
		<e:AuthMethods>\
			<e:AuthChoice>\
				<e:AuthType>FINGER</e:AuthType>\
			</e:AuthChoice>\
		</e:AuthMethods>\
		<e:OperationGroup>\
			<e:Name>Identification</e:Name>\
			<e:Operation>\
				<e:Name>AUTHENTICATE</e:Name>\
			</e:Operation>\
			<e:Operation>\
				<e:Name>GET_DATA</e:Name>\
				<e:Parameter name="FirstName" />\
				<e:Parameter name="Surname" />\
			</e:Operation>\
		</e:OperationGroup>\
	</e:TransactionRequest>';
};*/


function remidAndroidExec() {
	var inputData=injectedRemIDObject.getXMLData();
	var options = {};

	options.errorCallback = function(error) {
	    alert(error);
	};
	
	options.createIframe = true;
	options.dialogTarget = "_self";
	
	//alert("input Data is: "+inputData);
	$(document).ready(function(){
		remidExec(inputData, options);
	});
		
};


function remidCenterDialog() {
	var top, left;

	top = Math.max($(window).height() - $("#remidDialogIframe").outerHeight(),
			0) / 2;
	left = Math
			.max($(window).width() - $("#remidDialogIframe").outerWidth(), 0) / 2;

	$("#remidDialogIframe").css({
		top : top,
		left : left
	});
};




