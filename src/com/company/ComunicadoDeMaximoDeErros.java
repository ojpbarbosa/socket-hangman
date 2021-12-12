package com.company;

public class ComunicadoDeMaximoDeErros extends Comunicado {
  private boolean isAtingidoMaximoDeErros;

  public ComunicadoDeMaximoDeErros(boolean isAtingidoMaximoDeErros) {
    this.isAtingidoMaximoDeErros = isAtingidoMaximoDeErros;
  }

  public boolean isAtingidoMaximoDeErros() {
    return this.isAtingidoMaximoDeErros;
  }
}
