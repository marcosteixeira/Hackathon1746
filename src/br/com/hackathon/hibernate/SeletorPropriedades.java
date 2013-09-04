package br.com.hackathon.hibernate;

import java.util.GregorianCalendar;

import org.hibernate.criterion.Example.PropertySelector;
import org.hibernate.type.Type;

/**
 * Um seletor "universal" para nossas classes
 */
public class SeletorPropriedades implements PropertySelector {

	private static final long serialVersionUID = 3576589974494252160L;

	public boolean include(Object valor, String atributo, Type tipo) {
		
		if(valor != null) {
			// tratamento para zeros
			if(valor instanceof Number && ((Number) valor).longValue() != 0) {
				return true;
			}
			
			// tratamento para strings
			if(valor instanceof String) {
				return !"".equals(((String) valor).trim());
			}
			
			// tratamento para datas
			if(valor instanceof GregorianCalendar) {
				return true;
			}
			
			// tratamento para boolean
			if(valor instanceof Boolean) {
				return true;
			}
			
			return false;

		} else {
			return false;
		}
	}

}
