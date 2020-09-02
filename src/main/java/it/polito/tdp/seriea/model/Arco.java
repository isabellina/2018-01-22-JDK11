package it.polito.tdp.seriea.model;

public class Arco {

	private int season1;
	private int season2;
	private int peso;
	
	
	
	public Arco(int season1, int season2, int peso) {
		super();
		this.season1 = season1;
		this.season2 = season2;
		this.peso = peso;
	}



	public int getSeason1() {
		return season1;
	}



	public void setSeason1(int season1) {
		this.season1 = season1;
	}



	public int getSeason2() {
		return season2;
	}



	public void setSeason2(int season2) {
		this.season2 = season2;
	}



	public int getPeso() {
		return peso;
	}



	public void setPeso(int peso) {
		this.peso = peso;
	}
	
	
	
	
}
