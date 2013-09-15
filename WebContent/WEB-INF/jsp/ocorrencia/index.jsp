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
				<h3 style="margin-bottom: 0px; text-align: center;">Talvez já existam solicitações de ocorrências próximas desta área.</h3>
				<p style="padding: 10px;">Veja as solicitações próximas no mapa e verifique se a sua já não foi criada. Basta escolher o ponto no próprio mapa caso a solicitação já tenha sido criada. <b>Caso deseje criar uma nova ocorrência, clique no botão abaixo:</b> </p>
				<hr />
				<form id="form_criar" action="<c:url value="/ocorrencia/criarOcorrencia"/>" method="POST">
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
		function initialize() {
			  var mapOptions = {
			    zoom: 16,
			    center: new google.maps.LatLng("${ocorrencia.endereco.latitude}", "${ocorrencia.endereco.longitude}"),
			    mapTypeId: google.maps.MapTypeId.ROADMAP
			  };
			  var map = new google.maps.Map(document.getElementById('map_div'),
			      mapOptions);
			  
			  
			  var contexto = window.location.pathname.split('/')[1];
			  var link = window.location.protocol + "//" + window.location.host + "/"+ contexto
			  
			  $.getJSON('/hackathon/ocorrencia/${ocorrencia.endereco.bairro}', function(data) {
				  $.each(data, function(key, item) {
					  $.each(item, function(chave, item2) {
						  var pos = new google.maps.LatLng(item2.endereco.latitude,item2.endereco.longitude);
						  var infowindow = new google.maps.InfoWindow({
						        map: map,
						        position: pos,
						        content: item2.servico.nome +"<br/>"+ item2.endereco.logradouro + ", "+ item2.endereco.numero+'<br/> <a href="'+link+'/ocorrencia/visualizarOcorrencia/'+item2.link +'" title="Acessar ocorrência">Acessar ocorrência</a>'
						      });
					  });
				  });
				});
			}
			
			google.maps.event.addDomListener(window, 'load', initialize);
    </script>
    
<%@ include file="/rodapeBase.jsp" %>

