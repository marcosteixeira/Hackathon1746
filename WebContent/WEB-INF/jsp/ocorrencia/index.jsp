<%@ include file="/base.jsp" %>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
	  <script src="https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=false"></script>
	  <script src="<c:url value="/css/bootstrap/js/markerwithlabel_packed.js"/>"></script>
<body style="padding-top:0px;">
      <!-- Jumbotron -->
      <div class="jumbotron" style="background:url('../paodeacucar.png');height:120px;padding-top:40px;padding:40px;text-align:center;box-shadow:none;-webkit-box-shadow:none;border-bottom:none;">
      </div>
    
    <div class="row" style="background: #F0F0F0; margin: 0px 20px 0px 20px; border-radius: 10px;">
		<div class="span3 bs-docs-sidebar" style="float: left; margin-right: 75px;">
			<ul class="nav nav-list bs-docs-sidenav" style="margin-left: 25px; margin-right: 25px;">
				<li class="active"><a>Passo 1</a></li>
				<li><a>Passo 2</a></li>
				<li><a>Passo 3</a></li>
			</ul>
			
			<div class="span3" style="font-color: gray; margin-top: 30px;">
				<h3 style="margin-bottom: 0px; text-align: center;">Talvez já existam solicitações de ocorrências próximo desta área.</h3>
				<p style="padding: 10px;">Veja as solicitações próximas no mapa e verifique se a sua já não foi criada. Basta escolher o ponto no próprio mapa caso a solicitação já tenha sido criada. <b>Caso deseje criar uma nova ocorrência, clique no botão abaixo:</b> </p>
				<hr />
				<form id="form_criar" action="<c:url value="/ocorrencia/criarOcorrencia"/>">
					<p style="text-align: center;">
						<button class="btn btn-large btn-warning" type="button" onclick="document.getElementById('form_criar').submit();">Entendi! Criar nova solicitação mesmo assim</button><br/><br/>
						<a href="/hackathon"><button class="btn btn-medium" type="button" onclick="document.getElementById('form_criar').submit();">Escolher Novo Endereço</button></a>
					</p>
				</form>
			</div>
		</div>
		
		<div class="span6" style="float:left;text-align: center;">
			<center><div id="map_div" style="width: 95%; height: 500px; float: left; margin: 10px;">&nbsp;</div></center>
		</div>
    </div>
    
	<script>
		var ocorrenciasJavaScript = "";
		// Creating a LatLng object containing the coordinate for the center of the map
		var latlng = new google.maps.LatLng("${ocorrencia.endereco.latitude}", "${ocorrencia.endereco.longitude}");
		  
		// Creating an object literal containing the properties we want to pass to the map  
		var options = {  
			zoom: 16, // This number can be set to define the initial zoom level of the map
			center: latlng,
			mapTypeId: google.maps.MapTypeId.ROADMAP // This value can be set to define the map type ROADMAP/SATELLITE/HYBRID/TERRAIN
		};  
		// Calling the constructor, thereby initializing the map  
		var map = new google.maps.Map(document.getElementById('map_div'), options);  
		ocorrenciasJavaScript = ${ocorrencias} ;
	  
		var markers = [];
		var image = "";
   	  for (var i = 0; i < ocorrenciasJavaScript.length; i++) {
   		  
   		  if(ocorrenciasJavaScript[i].servico.nome == "Poda de árvores"){
	   	   	image = new google.maps.MarkerImage('../images/arvore.png',
	   			        new google.maps.Size(129, 42),
	   			        new google.maps.Point(0,0),
	   			        new google.maps.Point(18, 42)
	 			    );
   		  }else if (ocorrenciasJavaScript[i].servico.nome == "Iluminação pública"){
  	   	   	image = new google.maps.MarkerImage('../images/lampada.png',
   			        new google.maps.Size(129, 42),
   			        new google.maps.Point(0,0),
   			        new google.maps.Point(18, 42)
 			    );

   		  }else if(ocorrenciasJavaScript[i].servico.nome == "Conservação de vias"){
  	   	   	image = new google.maps.MarkerImage('../images/buraco.png',
   			        new google.maps.Size(129, 42),
   			        new google.maps.Point(0,0),
   			        new google.maps.Point(18, 42)
 			    );
   		  }else if(ocorrenciasJavaScript[i].servico.nome == "Estacionamento irregular"){
  	   	   	image = new google.maps.MarkerImage('../images/estacionamento.png',
   			        new google.maps.Size(129, 42),
   			        new google.maps.Point(0,0),
   			        new google.maps.Point(18, 42)
 			    );

   		  }
   		  var marker = new google.maps.Marker({
	 		  position: new google.maps.LatLng(ocorrenciasJavaScript[i].endereco.latitude, ocorrenciasJavaScript[i].endereco.longitude),
   		      map: map,
   		      icon: image
   	      });
		
   		var link = window.location.protocol + "//" + window.location.host + "/" + window.location.pathname.split('/')[1];
   		var infowindow = new google.maps.InfoWindow({  
   			content:  createInfo(ocorrenciasJavaScript[i].servico.nome, ocorrenciasJavaScript[i].endereco.bairro+',<br/>'+ocorrenciasJavaScript[i].endereco.logradouro+ ','+ ocorrenciasJavaScript[i].endereco.numero+' <br /><a href="'+link+'/ocorrencia/visualizarOcorrencia/'+ocorrenciasJavaScript[i].link +'" title="Acessar ocorrência">Acessar ocorrência</a>')
   		});

   	    makeInfoWindowEvent(map, infowindow, marker);
   	    
   	    markers.push(marker);

	  }
   	
   	  function makeInfoWindowEvent(map, infowindow, marker) {
   	  google.maps.event.addListener(marker, 'click', function() {
   	    infowindow.open(map, marker);
   	  });
   	}
	// Create information window
	function createInfo(title, content) {
		return '<div class="infowindow"><strong>'+ title +'</strong><br />'+content+'</div>';
	} 
	
    </script>
    
<%@ include file="/rodapeBase.jsp" %>

