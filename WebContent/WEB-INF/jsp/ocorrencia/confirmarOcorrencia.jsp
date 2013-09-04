<%@ include file="/base.jsp" %>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<body style="padding-top:0px;">
    <script src="http://maps.googleapis.com/maps/api/js?sensor=false&amp;libraries=places"></script>
    <script src="<c:url value="/js/jquery.geocomplete.js"/>"></script>
    <script src="<c:url value="/js/logger.js"/>"></script>

      <!-- Jumbotron -->
      <div class="jumbotron" style="background:url('../paodeacucar.png');height:120px;padding-top:40px;padding:40px;text-align:center;box-shadow:none;-webkit-box-shadow:none;border-bottom:none;">
      </div>
    
    <div class="row" style="background: #F0F0F0;margin: 0px 10px 0px 10px;border-radius: 10px;">
      <div class="span3 bs-docs-sidebar" style="float: left; margin-right: 75px;">
			<ul class="nav nav-list bs-docs-sidenav" style="margin-left: 25px; margin-right: 25px;">
          <li><a ><fmt:message key="passo_1"/></a></li>
          <li><a ><fmt:message key="passo_2"/></a></li>
          <li class="active"><a ><fmt:message key="passo_3"/></a></li>
        </ul>
      </div>
      
      <div class="span7" style="font-color:gray;margin-top:40px; padding-left: 10px; padding-right: 10px;">
        <h2 style="margin-bottom:0px;"><fmt:message key="confirme_as_informacoes"/></h2>
        <p style="margin-top:0px;"><fmt:message key="esta_quase_tudo_pronto"/> <fmt:message key="confira_as_informacoes_abaixo_e_confirme"/></p>
        <hr/>
        <table class="table table-striped">
        <thead></thead>
        <tbody>
        	<tr>
        		<td><fmt:message key="nome"/></td>
        		<td>${ocorrencia.usuario.nome}</td>
        	</tr>
        	<tr>
        		<td><fmt:message key="email"/></td>
        		<td>${ocorrencia.usuario.email}</td>
        	</tr>
        	
        	<c:if test="${not empty ocorrencia.usuario.telefone}">
	        	<tr>
	        		<td><fmt:message key="telefone"/></td>
	        		<td>${ocorrencia.usuario.telefone}</td>
	        	</tr>
        	</c:if>
        	<c:if test="${not empty ocorrencia.usuario.celular}">
	        	<tr>
	        		<td><fmt:message key="celular"/></td>
	        		<td>${ocorrencia.usuario.celular}</td>
	        	</tr>
        	</c:if>
        	
        	<tr>
        		<td><fmt:message key="servicoOcorrencia"/></td>
        		<td>${ocorrencia.servico.nome}</td>
        	</tr>
        	<tr>
	        		<td><fmt:message key="logradouroOcorrencia"/></td>
	        		<td>${ocorrencia.endereco.logradouro}</td>
        	</tr>
        	
        	<c:if test="${not empty ocorrencia.endereco.numero}">
	        	<tr>
		        		<td><fmt:message key="numeroOcorrencia"/></td>
		        		<td>${ocorrencia.endereco.numero}</td>
	        	</tr>
        	</c:if>
        	<tr>
	        		<td><fmt:message key="descricaoOcorrencia"/></td>
	        		<td>${ocorrencia.descricao}</td>
        	</tr>
	        	
        	</tbody>
		</table>
		<p style="text-align: center;">
  			<a href="<c:url value="/ocorrencia/salvar"/>"><button class="btn btn-primary" type="button"><fmt:message key="confirmarOcorrencia"/></button></a>
  			<a href="/hackathon"><button class="btn btn-danger" type="button">Cancelar</button></a>
  		</p>
      </div>
    </div>

<%@ include file="/rodapeBase.jsp" %>