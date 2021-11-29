package com.company;

public class PedidoDeRevelacao extends Comunicado {
  private int posicao; 
  private char letra; 

  public PedidoDeRevelacao(int posicao, char letra) {
    this.posicao = posicao;
    this.letra = letra;
  }

  public int getPosicao() {
    return this.posicao;
  }

  public char getLetra() {
    return this.letra;
  }

  public String toString() {
    return (this.letra + "/" + this.posicao);
  }
}
