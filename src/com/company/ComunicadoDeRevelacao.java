package com.company;

public class ComunicadoDeRevelacao extends Comunicado {
  private Tracinhos tracinhos;

  public ComunicadoDeRevelacao(Tracinhos tracinhos) {
    this.tracinhos = tracinhos;
  }

  public Tracinhos getTracinhos() {
    return this.tracinhos;
  }
}
