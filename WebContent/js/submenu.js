/* Variável global que guardará o id do elemento clicado. */
id = 0;
/*
 * Gera o link completo concatenando o link recebido 
 * como parâmetro com o id do elemento selecionado.
 */
function gerarLinkCompleto(link){
	document.location.href= link + "/" + id; 
}

jQuery(document).ready(function( $ ){
	
	$(function(){
	    $('tr').click(function(e){
	    	
	    	/* Pego o valor do id do tr clicado */
	        id = $(this).attr('id');
	        
	       if(id != null){

	    	   /* Pego o id da entidade e também o id do submenu */
	    	   var posicaoUnderline = id.indexOf("_");
	    	   id = id.substring(posicaoUnderline + 1);
	    	   
	       }
	    });
	});	
});