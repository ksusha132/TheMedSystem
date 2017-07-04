var serverContext = [[@{"/"}]];

function resendToken(){
	var token = [[${param.token}]];
    $.get(serverContext + "users/resendToken?token="+token)
    .fail(function(data) {
    	window.location.href = serverContext + "login?error=" + data.responseJSON.message;
    });
}

$(document).ajaxStart(function() {
    $("title").html("LOADING ...");
});