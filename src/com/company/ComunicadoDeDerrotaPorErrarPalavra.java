package com.company;

public class ComunicadoDeDerrotaPorErrarPalavra extends Comunicado {
  private int grupo;

  public ComunicadoDeDerrotaPorErrarPalavra(int grupo) {
    this.grupo = grupo;
  }

  public int getGrupo() {
    return this.grupo;
  }
}