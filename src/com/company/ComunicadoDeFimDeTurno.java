package com.company;

public class ComunicadoDeFimDeTurno extends Comunicado {
  private int grupo;

  public ComunicadoDeFimDeTurno(int grupo) {
    this.grupo = grupo;
  }

  public int getGrupo() {
    return this.grupo;
  }
}