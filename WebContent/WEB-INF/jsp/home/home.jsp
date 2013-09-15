<%@ include file="/base.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<body style="background:url('paodeacucar.png')">
    <script src="http://maps.googleapis.com/maps/api/js?sensor=false&amp;libraries=places"></script>
    <script src="<c:url value="/js/jquery.geocomplete.js"/>"></script>
    <script src="<c:url value="/js/logger.js"/>"></script>

      <!-- Jumbotron -->
      <div class="jumbotron masthead" style="background:transparent;text-align:center;">
        
        <div class="container" style="padding-top:297px;">
        
            <form class="form-inline" id="form-cadastro" method="POST" action="<c:url value="/ocorrencia/"/>">
        
            	<p style="color:white;" class="lead"><fmt:message key="mensagem_inicial"/></p>
        
        	<input type="text" class="input-xxlarge" id="geocomplete" placeholder="<fmt:message key="digite_endereco_ocorrencia"/>"/>
        
        	<button type="submit" class="btn"><fmt:message key="comecar"/></button><br/>
        
        </form>
        </div>
      </div>
	  
     <script>
	    
	    
	    function geocomplete(){
	    	jQuery("#form-cadastro").submit();
	    }
    
	    $(function(){
        
        var options = {
		  country: "br",	
		  bounds: true
        };
        
        $("#geocomplete").geocomplete(options).bind("geocode:result", function(event, result){

        	var numero = "";
        	var logradouro = "";
        	var bairro = "";
        	var cidade = "";
        	var cep = "";
        	var latitude = "";
        	var longitude = "";
        	
        	latitude = result.geometry.location.pb;
        	longitude = result.geometry.location.qb;
        	
        	for (var i=0;i < result.address_components.length; i++){
        
        		elemento = jQuery(result.address_components).get(i);

        		for (var j=0;j<elemento.types.length;j++){
        			
        			var tipo = jQuery(elemento.types).get(j);
       				
        			if(tipo == "street_number"){
       					numero = elemento.long_name;		
        			
        			}else if(tipo == "route"){
        				logradouro = elemento.long_name;		
        			
        			}else if (tipo == "sublocality"){
        				bairro = elemento.long_name;
        			
        			}else if (tipo == "locality"){
        				cidade = elemento.long_name;
        			
        			}else if (tipo == "postal_code"){
    					cep = elemento.long_name;
    				}
        		}
        	}
        	
        	if(bairro == ""){
        		bairro = result.vicinity;
        	}
        	
        	jQuery("#form-cadastro").append('<input type="hidden" name="ocorrencia.endereco.numero" value="'+numero +'" />');
        	jQuery("#form-cadastro").append('<input type="hidden" name="ocorrencia.endereco.logradouro" value="'+logradouro+'" />');
        	jQuery("#form-cadastro").append('<input type="hidden" name="ocorrencia.endereco.bairro" value="'+bairro+'" />');
        	jQuery("#form-cadastro").append('<input type="hidden" name="ocorrencia.endereco.cidade" value="'+cidade+'" />');
        	jQuery("#form-cadastro").append('<input type="hidden" name="ocorrencia.endereco.cep" value="'+cep+'" />');
        	jQuery("#form-cadastro").append('<input type="hidden" name="ocorrencia.endereco.latitude" value="'+latitude+'" />');
        	jQuery("#form-cadastro").append('<input type="hidden" name="ocorrencia.endereco.longitude" value="'+longitude+'" />');
        	
        	geocomplete();
        });
        
      });
    </script>
	
    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
	
<%@ include file="/rodapeBase.jsp" %>