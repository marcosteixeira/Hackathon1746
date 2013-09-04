<%@ include file="/base.jsp" %>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
  <script src="https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=false"></script>
  <script src="<c:url value="/css/bootstrap/js/markerwithlabel_packed.js"/>"></script>
<body style="padding-top:0px;">
      <!-- Jumbotron -->
      <div class="jumbotron" style="background:url('../paodeacucar.png');height:120px;padding-top:40px;padding:40px;text-align:center;box-shadow:none;-webkit-box-shadow:none;border-bottom:none;">
      </div>
    
    <div class="row" style="background: #F0F0F0; margin: 0px 20px 0px 20px; border-radius: 10px;">
		<div class="span3 bs-docs-sidebar" style="float: left; margin-right: 75px;margin-left:0px;">
			<ul class="nav nav-list bs-docs-sidenav" style="margin-left: 25px; margin-right: 25px;">
				<li class="active"><a>Passo 1</a></li>
				<li><a>Passo 2</a></li>
				<li><a>Passo 3</a></li>
			</ul>
		</div>
		
		<div class="span6" style="float:left;text-align: center;">
			<h2 style="margin-bottom: 0px; text-align: center;">Criar ocorrência</h2>
			<hr />
			<form class="form-inline" style="padding: 10px; text-align: left;" action="<c:url value="/ocorrencia/preencherSolicitante"/>" method="post">
        	<div class="control-group">
        		<label class="control-label"><fmt:message key="servico"/> <span class="obrigatorio"><fmt:message key="obrigatorio"/></span></label>
      		 	<div class="controls">
					<select class="span3" name="ocorrencia.servico.id">
					<c:forEach var="servico" items="${listaServicos}">
						<option value="${servico.id}">${servico.nome}</option>
					</c:forEach>
					</select>
				</div>
        	</div>
        	
        	<div class="control-group">
        		<label class="control-label"><fmt:message key="descricaoOcorrencia"/> <span class="obrigatorio"><fmt:message key="obrigatorio"/></span></label>
      			<div class="controls"><textarea rows="3" class="input-xlarge" id="descricao" name="ocorrencia.descricao" style="width: 100%;" placeholder="<fmt:message key="digiteDescricaoOcorrencia"/>" required="required">${ocorrencia.descricao}</textarea></div>
        	</div>
        	
        	<div class="control-group">
        		<label class="control-label"><fmt:message key="logradouroOcorrencia"/> <span class="obrigatorio"><fmt:message key="obrigatorio"/></span></label>
      		 	<div class="controls"><input type="text" id="logradouro" name="ocorrencia.endereco.logradouro" class="input-xlarge" placeholder="<fmt:message key="digiteLogradouroOcorrencia"/>" required="required" value="${ocorrencia.endereco.logradouro}"></div>
        	</div>
        	
        	<div class="control-group">
        		<label class="control-label"><fmt:message key="numero"/> <span class="opcional"><fmt:message key="opcional"/></span></label>
      			<div class="controls"><input type="text" class="input-small" id="numero" name="ocorrencia.endereco.numero" placeholder="<fmt:message key="digiteNumeroOcorrencia"/>" value="${ocorrencia.endereco.numero}"></div>
        	</div>
        	
        	<div class="control-group">
        		<label class="control-label"><fmt:message key="pontoReferencia"/> <span class="opcional"><fmt:message key="opcional"/></span></label>
      			<div class="controls"><textarea rows="3" class="input-xlarge" name="ocorrencia.endereco.pontoReferencia" id="pontoReferencia" style="width: 50%;" placeholder="<fmt:message key="pontoReferencia"/>"></textarea>${ocorrencia.endereco.pontoReferencia}</div>
        	</div>
        	
        	<input type="file" class="input_file" title="Adicione uma imagem">
        	<div class="control-group">
      			<input type="submit" class="btn btn-large btn-primary" style="right:0px;" value="Continuar"/>
      			<a href="/hackathon"><button class="btn btn-medium" type="button" onclick="document.getElementById('form_criar').submit();">Cancelar</button></a>
      		</div>
        </form>
		</div>
    </div>
    
<%@ include file="/rodapeBase.jsp" %>

