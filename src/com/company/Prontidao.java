package com.company;

public class Prontidao extends Comunicado {
  private boolean pronto = false;

  public Prontidao(boolean pronto) {
    this.pronto = pronto;
  }

  public boolean getProntidao() {
    return this.pronto;
  }
}