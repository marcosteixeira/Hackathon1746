<%@ include file="/base.jsp" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<body style="padding-top:0px;">
    <script src="http://maps.googleapis.com/maps/api/js?sensor=false&amp;libraries=places"></script>
    <script src="<c:url value="/js/jquery.geocomplete.js"/>"></script>
    <script src="<c:url value="/js/logger.js"/>"></script>

      <!-- Jumbotron -->
      <div class="jumbotron" style="background:url('../paodeacucar.png');height:120px;padding-top:40px;padding:40px;text-align:center;box-shadow:none;-webkit-box-shadow:none;border-bottom:none;">
      </div>
      
       <div class="row" style="background: #F0F0F0;margin: 0px 10px 0px 10px;border-radius: 10px;padding:10px;">
      <!--<h1 style="text-align: center;color: #50b050;"><fmt:message key="sua_ocorrencia_foi_criada"/></h1>
      <hr/>-->

      <div class="row">
	      <div class="span4" style="text-align:center;border-radius: 10px; border: 1px solid gray;padding:10px;">
		      <img src="../../images/sem-imagem.jpg" id="fotoOcorrencia" alt="" style="width:90%;">
		      <img src="data:image/png;base64,<c:out value='${ocorrencia.qrCode}'/>" id="qrcode" alt="" style="width:90%;display:none;">
		      <hr/>
		      <p style="text-align:center">Compartilhe o QR Code!<br/><strong>${fn:length(ocorrencia.assinaturas)} pessoas</strong> já votaram esta ocorrência!</p>
 	 	  </div>
      
	     <div class="span5" style="font-color:gray;padding-left: 30px; padding-right: 30px;">
	     	<a href="/hackathon" role="button" class="btn btn-large btn-block" data-toggle="modal">Página Inicial</a>
	        <h2>${ocorrencia.servico.nome}</h2>
	        <h3>${ocorrencia.endereco.logradouro} <c:if test="${not empty ocorrencia.endereco.numero}">, altura do número ${ocorrencia.endereco.numero}</c:if></h3>
	        <c:if test="${not empty ocorrencia.endereco.cep}"><h5>Cep: ${ocorrencia.endereco.cep}</h5></c:if>
	        
	        <p>Descrição: ${ocorrencia.descricao }</p>
	        
	        <p>Status: ${ocorrencia.status }</p>
	        
	        <p>Criado por: ${ocorrencia.usuario.nome}</p>
	        
	        <p style="word-wrap: break-word;">Link: ${ocorrencia.linkOcorrencia}</span></p>
	        
	        <br/>&nbsp;&nbsp;<br/>
	        <div class="well well-large">
	        	<h4 style="margin-top:0px;">Compartilhe!</h4>
	        	<h6>Quanto mais pessoas votarem nesta ocorrência, mais rápido ela será resolvida!</h6>
	        	<button class="btn btn-large btn-block" type="button" id="botaoQR" onclick="mostrarDesmotrarQR();">Compartilhar o QR Code</button>
				<a href="https://www.facebook.com/sharer/sharer.php?u=${ocorrencia.linkOcorrencia}"><button class="btn btn-primary btn-large btn-block" type="button">Compartilhar no Facebook</button></a>
				<a href="#modalAssinar" role="button" class="btn btn-danger btn-large btn-block" data-toggle="modal">Eu apoio esta reclamação!</a>
	        </div>
	      </div>
	      
	      <div id="modalAssinar" class="modal hide fade">
  <div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
    <h3>Assinar Esta Reclamação</h3>
  </div>
  <div id="modalLogin" class="modal-body">
    <p>
    
		<form class="form-horizontal" action="<c:url value="/ocorrencia/assinar"/>" method="POST">
		  <div class="control-group">
		    <label class="control-label"for="inputEmail">Email</label>
		    <div class="controls">
		      <input name="usuario.email" type="email" id="inputEmail" placeholder="Email" required="required">
		    </div>
		  </div>
		  <div class="control-group">
		    <div class="controls">
		      <button type="submit" class="btn">Assinar</button>
		    </div>
		  </div>
		</form>
    
    </p>
  </div>
  <div class="modal-footer">
    <a href="#" class="btn" data-dismiss="modal">Fechar</a>
  </div>
</div>
	      
	      
     </div>
	</div>
		<script>
			function mostrarDesmotrarQR(){
				$("#qrcode").toggle();
				$("#fotoOcorrencia").toggle();
				if($("#qrcode").is(":visible")){
					$('#botaoQR').html("Ver a imagem");
				}else{
					$('#botaoQR').html("Compartilhar QR Code");
				}
			}
		</script>
<%@ include file="/rodapeBase.jsp" %>

