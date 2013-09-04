<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/functions/Utilitarios" prefix="utilitarios"%>
<!DOCTYPE html>
<html>

	<head>
	  <meta charset="UTF-8" />
	  
      	<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<meta http-equiv="Content-Type" content="text/html;charset=utf-8" >
		<title><fmt:message key="title"/></title>
		
		<script src="http://code.jquery.com/jquery.js"></script>
		<script src="<c:url value="/css/bootstrap/js/bootstrap.js"/>"></script>
		<link href="<c:url value="/css/bootstrap/css/bootstrap.css"/>" rel="stylesheet">
		<link href="<c:url value="/css/estilo.css"/>" rel="stylesheet"/>
		<link href="<c:url value="/css/bootstrap/css/bootstrap-responsive.css"/>" rel="stylesheet">
	    <link href="<c:url value="/css/bootstrap/css/docs.css"/>" rel="stylesheet">
        <link href="https://developers.google.com/maps/documentation/javascript/examples/default.css" rel="stylesheet">
     	<link rel="shortcut icon" href="<c:url value="/images/favicon.ico"/>" type="image/ico">	
	
	  <noscript>
		<meta http-equiv="Refresh" content="0;url=<c:url value="/javascriptDesabilitado.jsp"/>">
	  </noscript>
	  
	</head>
	<body style="background:url('<c:url value="/paodeacucar.png"/>');">
	
		<div id="divgeral">
			<div id="conteudo">
			<c:if test="${not empty sucesso}">
				<div class="alert-box success">
					${sucesso}
				</div>
			</c:if>
			
			<c:if test="${not empty errors}">
				<div class="alert-box alert">
					<c:forEach items="${errors }" var="error">
						 ${error.message } <br>
					</c:forEach>
				</div>
			</c:if>