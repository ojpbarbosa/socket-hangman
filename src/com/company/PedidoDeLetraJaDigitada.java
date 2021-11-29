package com.company;

public class PedidoDeLetraJaDigitada extends Comunicado {
  private char letra;
  private boolean isJaDigitada;

  public PedidoDeLetraJaDigitada(char letra) {
    this.letra = letra;
  }

  public void setJaDigitada(boolean isJaDigitada) {
    this.isJaDigitada = isJaDigitada;
  }

  public char getLetra() {
    return this.letra;
  }

  public boolean getIsJaDigitada() {
    return this.isJaDigitada;
  }
}