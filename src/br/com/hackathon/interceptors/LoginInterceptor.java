package br.com.hackathon.interceptors;

import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.interceptor.Interceptor;
import br.com.caelum.vraptor.resource.ResourceMethod;
import br.com.hackathon.controllers.HomeController;
import br.com.hackathon.sessao.SessaoGeral;
import br.com.hackathon.util.Administrativa;
import br.com.hackathon.util.Util;
import br.com.hackathon.util.UtilContexto;

@Intercepts
public class LoginInterceptor implements Interceptor {

    private Result result;
    private SessaoGeral sessaoGeral;
    private HttpServletRequest request;

    public LoginInterceptor(Result result, SessaoGeral sessaoGeral, HttpServletRequest request) {
        this.result = result;
        this.sessaoGeral = sessaoGeral;
        this.sessaoGeral.setUserAgent(request.getHeader("User-Agent").toLowerCase());
        this.request = request;
        
        if(Util.vazio(UtilContexto.getBaseUrl())){
        	String url = "http://" + request.getServerName() + ":" + request.getServerPort()+UtilContexto.getNomeContexto();
        	UtilContexto.setBaseUrl(url);
        }
    }

    
    public void intercept(InterceptorStack stack, ResourceMethod method, Object resourceInstance) {
    	if(method.getMethod().isAnnotationPresent(Administrativa.class) || method.getResource().getType().isAnnotationPresent(Administrativa.class)){
    		if(sessaoGeral.isLogged()) {
    			if(sessaoGeral.getUsuario().getEmail().equals("administrador")){
    				stack.next(method, resourceInstance);
    			}else{
    				result.redirectTo(HomeController.class).home();
    			}
    		}else{
    			result.redirectTo(HomeController.class).home();
    		}
    		
    	} else {
    		stack.next(method, resourceInstance);
    	}
    }


	public boolean accepts(ResourceMethod arg0) {
		return false;
	}
}
