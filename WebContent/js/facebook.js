function loginFacebook(){
	FB.getLoginStatus(function(response) {
			FB.login(function(response) {
				$(window.document.location).attr('href',"http://localhost:8080/hackathon/login/autenticar?access_token="+response.authResponse.accessToken+"&signed_request="+response.authResponse.signedRequest);
				 }, {scope: 'email'});
	});
}
