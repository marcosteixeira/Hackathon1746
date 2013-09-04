package br.com.hackathon.services;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.LockOptions;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;

import br.com.caelum.vraptor.Validator;
import br.com.hackathon.hibernate.Buscador;
import br.com.hackathon.hibernate.TemplateTransacao;
import br.com.hackathon.hibernate.Transacao;
import br.com.hackathon.hibernate.TransacaoSomenteLeitura;
import br.com.hackathon.modelo.entidade.Entidade;
import br.com.hackathon.util.Util;
import br.com.hackathon.util.UtilReflect;
import br.com.hackathon.util.UtilString;


public class BaseService {
	
	protected Validator validator ;
	
	public static <E extends Entidade> void salvar(final List<E> entidades){
		for(Entidade e: entidades){
			salvar(e);
		}
	}
	
	public static <E extends Entidade> void excluir(final List<E> entidades){
		for(Entidade e: entidades){
			excluir(e);
		}
	}
	
	
	public static void salvar(final Entidade entidade){
		
		TemplateTransacao.executa(new Transacao() {
			
			@Override
			public Object executaTransacao(Session sessaoAberta) {
				
				salvar(entidade, sessaoAberta);
				
				return entidade;
			}
		});
	}
	
	public static void excluir(final Entidade entidade){
		
		TemplateTransacao.executa(new Transacao() {
			
			@Override
			public Object executaTransacao(Session sessaoAberta) {
				
				excluir(entidade, sessaoAberta);
				
				return entidade;
			}
		});
	}
	public static <E extends Entidade> void inicializa(final Entidade entidadeMae, final Object entidadeFilha) {
		
		if(!Hibernate.isInitialized(entidadeFilha)) {
			
			TemplateTransacao.executa(new TransacaoSomenteLeitura() {
				
				@Override
				public Object executaTransacao(Session sessaoAberta) {
					
					sessaoAberta.buildLockRequest(LockOptions.NONE).lock(entidadeMae);
					Hibernate.initialize(entidadeFilha);
					
					return entidadeMae;
				}
				
			});
		}
	}
	
	 @SuppressWarnings("unchecked")
	public static <T extends Entidade> boolean falhaRegraCampoUnico(final T entidade, final int idObjeto,final String ... nomesAtributos) {
	        
	        final List<T> entidadesEncontradas;
	        final Object[] valoresAtributos = new Object[nomesAtributos.length];
	        final Class<?>[] tiposAtributos = new Class[nomesAtributos.length];
	        final T entidadeTestada;
	        
	        for(int i = 0; i < valoresAtributos.length; i++){
	        	valoresAtributos[i] = UtilReflect.retornarValorAtributo(entidade, nomesAtributos[i]);
	        	tiposAtributos[i] = UtilReflect.retornarTipoDeRetornoDoGetter(entidade, nomesAtributos[i]);
	        }
	        
	        // a regra só é testada se o array de valores não estiver nulo
	        if(Util.preenchido(valoresAtributos)) {
	            
	            try {
	                entidadeTestada = (T) entidade.getClass().newInstance();
	            } catch(Exception e) {
	                throw new RuntimeException("Ocorreu algum erro ao tentar criar uma instância");
	            }
	            
	            for(int i = 0; i < nomesAtributos.length; i++){
	            	UtilReflect.setarValorAtributo(entidadeTestada, nomesAtributos[i], valoresAtributos[i], tiposAtributos[i]);
	            }
	            
	            entidadesEncontradas = new Buscador().matchmode(MatchMode.EXACT).listar2(entidadeTestada);
	            
	            // o sistema não pode encontrar dois ou mais objetos, significa que está inconsistente
	            if(entidadesEncontradas.size() >= 2) {
	            	
	            	throw new RuntimeException("O sistema encontrou mais de uma objeto da classe "
	            			+ UtilString.doubleQuote(entidade.getClass().getName()) + " com os atributos passados.");
	            }
	            
	            // a regra falha se encontrar um objeto no banco que não seja o próprio
	            if((entidadesEncontradas.size() == 1) && (entidadesEncontradas.get(0).getId() != idObjeto)) {
	                return true;
	            }
	        }
	        return false;
	    }

	public Validator getValidator() {
		return validator;
	}

	public void setValidator(Validator validator) {
		this.validator = validator;
	}

}
