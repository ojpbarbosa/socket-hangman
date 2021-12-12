package com.company;

public class ControladorDeErros extends Comunicado implements Cloneable {
  private int qtdMax, qtdErr = 0;

  public ControladorDeErros(int qtdMax) throws Exception {
    if (qtdMax < 1)
      throw new Exception("A quantidade máxima de erros precisa ser positiva");

    this.qtdMax = qtdMax;
  }

  public void registreUmErro() throws Exception {
    if (this.qtdErr == this.qtdMax)
      throw new Exception("A quantidade de erros já atingiu o limite");

    this.qtdErr++;
  }

  public boolean isAtingidoMaximoDeErros() {
    if (this.qtdErr == this.qtdMax)
      return true;

    return false;
  }

  @Override
  public String toString() {
    return this.qtdErr + "/" + this.qtdMax;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;

    if (obj == null)
      return false;

    if (obj.getClass() != ControladorDeErros.class)
      return false;

    ControladorDeErros cntrlErros = (ControladorDeErros) obj;
    if (this.qtdMax != cntrlErros.qtdMax)
      return false;
    if (this.qtdErr != cntrlErros.qtdErr)
      return false;

    return true;
  }

  @Override
  public int hashCode() {
    int ret = 666;

    ret = 11 * ret + new Integer(this.qtdErr).hashCode();
    ret = 11 * ret + new Integer(this.qtdMax).hashCode();

    if (ret < 0)
      ret = -ret;

    return ret;
  }

  public ControladorDeErros(ControladorDeErros c) throws Exception {
    if (c == null)
      throw new Exception("Modelo ausente");
    this.qtdErr = c.qtdErr;
    this.qtdMax = c.qtdMax;
  }

  @Override
  public Object clone() {
    ControladorDeErros ret = null;

    try {
      ret = new ControladorDeErros(this);
    } catch (Exception erro) {
    } // Ignorando Exception

    return ret;
  }
}
