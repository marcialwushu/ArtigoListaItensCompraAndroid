package com.example.listatarefas;

public class ItemCompra {

	private String nome;
    private float quantidade;
    private boolean comprado;
    
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public float getQuantidade() {
		return quantidade;
	}
	public void setQuantidade(float quantidade) {
		this.quantidade = quantidade;
	}
	public boolean isComprado() {
		return comprado;
	}
	public void setComprado(boolean comprado) {
		this.comprado = comprado;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Nome: "+nome+". Qtd: "+quantidade;
	}
	
}
