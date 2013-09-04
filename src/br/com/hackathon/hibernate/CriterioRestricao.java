package br.com.hackathon.hibernate;

public enum CriterioRestricao {
	
	/*
	 * Igual
	 */
	EQ,
	/*
	 * Igualdade entre duas colunas
	 */
	EQ_PROPERTY,
	/*
	 * Maior entre duas colunas
	 */
	GT_PROPERTY,
	/*
	 * Menor ou igual entre duas colunas
	 */
	LE_PROPERTY,
	/*
	 * Diferente
	 */
	NE,
	/*
	 * Desigualdade entre duas colunas
	 */
	NE_PROPERTY,
	/*
	 * Menor ou igual
	 */
	LE,
	/*
	 * Maior ou igual
	 */
	GE,
	/*
	 * Menor
	 */
	LT,
	/*
	 * Maior
	 */
	GT,
	/*
	 * Vazio
	 */
	IS_EMPTY,
	/*
	 * N�o-Vazio
	 */
	IS_NOT_EMPTY,
	/*
	 * Nulo
	 */
	IS_NULL,
	/*
	 * N�o-Nulo
	 */
	IS_NOT_NULL,
	/*
	 * Like
	 */
	LIKE,
	/*
	 * In
	 */
	IN,
	/*
	 * Not In
	 */
	NOT_IN, 
	/*
	 * Like para n�o string
	 */
	LIKE_NOT_STRING,
	/*
	 * Between
	 * 
	 */
	BETWEEN
	;
}