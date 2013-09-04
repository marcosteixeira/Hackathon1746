package br.com.hackathon.interceptors;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.interceptor.Interceptor;
import br.com.caelum.vraptor.resource.ResourceMethod;
import br.com.hackathon.sessao.SessaoGeral;
import br.com.hackathon.util.GeradorDeMd5;
import br.com.hackathon.util.Util;

@Intercepts
public class ExceptionInterceptor implements Interceptor {

    private SessaoGeral sessaoGeral;
    private static final Logger log = Logger.getLogger(ExceptionInterceptor.class);

    public ExceptionInterceptor(SessaoGeral sessaoGeral, HttpServletRequest request) {
        this.sessaoGeral = sessaoGeral;
        this.sessaoGeral.setUserAgent(request.getHeader("User-Agent").toLowerCase());
    }

    public boolean accepts(ResourceMethod method) {
        return true ; 
    }
    
    public void intercept(InterceptorStack stack, ResourceMethod method, Object resourceInstance) {
    	
    				try{
    					stack.next(method, resourceInstance);
    					
    				}catch (RuntimeException e) {
    					
    					StringWriter writerStack = new StringWriter();
    					PrintWriter printWriterStack = new PrintWriter(writerStack);
    					e.printStackTrace(printWriterStack);
    					String stackTrace = writerStack.toString();
    					String codigoErro = GeradorDeMd5.getStringAleatoria().substring(0, 5).toUpperCase();
    					
						if(Util.preenchido(sessaoGeral) && Util.preenchido(sessaoGeral.getUsuario())){
							log.error("Erro inesperado "+sessaoGeral.getUsuario().getEmail()+ " " + codigoErro+" - " +stackTrace);
							
						}else{
							log.error("Erro inesperado " + codigoErro+" - " +stackTrace);
						}
    					throw e;
    				}
    }
}
