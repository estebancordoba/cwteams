function handleLoginRequest(xhr, status, args) {
    if(args.validationFailed || !args.loggedIn) {
        PF('dlg').jq.effect("shake", {times:5}, 100);
    }
    else {
        PF('dlg').hide();
        $('#loginLink').fadeOut();			            
    }
}

function exportCanvas() {
    $("#chart").get(0).toBlob(function(blob) {
		saveAs(blob, "dimensiones.png");
	});
}