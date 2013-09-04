package br.com.hackathon.hibernate;


public abstract class Transacao extends ATransacao {

	@Override
	public Boolean precisaDeCommit() {
		return Boolean.valueOf(true);
	}
	

}
