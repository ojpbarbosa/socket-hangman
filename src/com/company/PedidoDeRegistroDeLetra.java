package com.company;

public class PedidoDeRegistroDeLetra extends Comunicado {
  private char letra;

  public PedidoDeRegistroDeLetra(char letra) {
    this.letra = letra;
  }

  public char getLetra() {
    return this.letra;
  }

  @Override
  public String toString() {
    return ("" + this.letra);
  }
}
