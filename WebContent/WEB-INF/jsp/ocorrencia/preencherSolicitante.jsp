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
          <li class="active"><a ><fmt:message key="passo_2"/></a></li>
          <li><a ><fmt:message key="passo_3"/></a></li>
        </ul>
      </div>
      
      <div class="span7" style="font-color:gray;margin-top:40px;">
        <h2 style="margin-bottom:0px;"><fmt:message key="confirme_informacoes_pessoais"/></h2>
        <p style="margin-top:0px;"><fmt:message key="sera_util_para_identificarmos_e_contatarmos_voce_assim_que_uma_atualizacao_do_status_sair"/></p>
        <hr/>
        <p style="padding: 10px; text-align: center;">
  			<button class="btn btn-primary" type="button">Login com Facebook</button>
  			<a href="#modalLogin" role="button" class="btn" data-toggle="modal">Já tenho cadastro</a>
		</p>
        <form class="form-inline" style="padding: 10px;" action="<c:url value="/ocorrencia/confirmarOcorrencia"/>">
        	<div class="control-group">
        		<label class="control-label"><fmt:message key="nome"/> <span class="obrigatorio"><fmt:message key="obrigatorio"/></span></label>
      		 	<div class="controls"><input name="usuario.nome" type="text" class="input-xlarge" id="nome" placeholder="<fmt:message key="digite_seu_nome_completo"/>" required="required"></div>
        	</div>
        	
        	<div class="control-group">
        		<label class="control-label"><fmt:message key="email"/> <span class="obrigatorio"><fmt:message key="obrigatorio"/></span></label>
      			<div class="controls"><input type="email" name="usuario.email" class="input-xlarge" id="email" placeholder="<fmt:message key="digite_seu_email"/>" required="required"></div>
        	</div>
        	
        	<div class="control-group">
        		<label class="control-label"><fmt:message key="data_de_nascimento"/> <span class="opcional"><fmt:message key="opcional"/></span></label>
      		 	<div class="controls"><input type="date" id="date" name="usuario.dataNascimento" class="input-medium" placeholder="DD/MM/AAAA"></div>
        	</div>
        	
        	<div class="control-group">
        		<label class="control-label"><fmt:message key="telefone"/> <span class="opcional"><fmt:message key="opcional"/></span></label>
      			<div class="controls"><input type="text" name="usuario.telefone" class="input-medium" id="telefone" placeholder="<fmt:message key="mascara_telefone"/>"></div>
        	</div>
        	
        	<div class="control-group">
        		<label class="control-label"><fmt:message key="celular"/> <span class="opcional"><fmt:message key="opcional"/></span></label>
      			<div class="controls"><input type="text" name="usuario.celular" class="input-medium" id="celular" placeholder="<fmt:message key="mascara_telefone"/>"></div>
        	</div>
        	 
        	<div class="control-group">
      			<input type="submit" class="btn btn-primary" style="right:0px;" value="Continuar"/>
      			<a href="/hackathon"><button class="btn btn-medium" type="button" onclick="document.getElementById('form_criar').submit();">Cancelar</button></a>
      		</div>
        </form>
      </div>
    </div>

<div id="modalLogin" class="modal hide fade">
  <div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
    <h3>Acessar Conta</h3>
  </div>
  <div id="modalLogin" class="modal-body">
    <p>
    
		<form class="form-horizontal">
		  <div class="control-group">
		    <label class="control-label" for="inputEmail">Email</label>
		    <div class="controls">
		      <input type="text" id="inputEmail" placeholder="Email">
		    </div>
		  </div>
		  <div class="control-group">
		    <label class="control-label" for="inputPassword">Senha</label>
		    <div class="controls">
		      <input type="password" id="inputSenha" placeholder="Senha">
		    </div>
		  </div>
		  <div class="control-group">
		    <div class="controls">
		      <button type="submit" class="btn">Entrar</button>
		    </div>
		  </div>
		</form>
    
    </p>
  </div>
  <div class="modal-footer">
    <a href="#" class="btn" data-dismiss="modal">Fechar</a>
  </div>
</div>

<%@ include file="/rodapeBase.jsp" %>

