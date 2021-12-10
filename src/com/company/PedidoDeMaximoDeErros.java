package com.company;

public class PedidoDeMaximoDeErros extends Comunicado {
  private boolean isAtingidoMaximoDeErros;

  public PedidoDeMaximoDeErros() {
    this.isAtingidoMaximoDeErros = false;
  }

  public boolean isAtingidoMaximoDeErros() {
    return this.isAtingidoMaximoDeErros;
  }

  public void setIsAtingidoMaximoDeErros(boolean isAtingidoMaximoDeErros) {
    this.isAtingidoMaximoDeErros = isAtingidoMaximoDeErros;
  }
}