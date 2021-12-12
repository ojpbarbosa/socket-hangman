package com.company;

public class ControladorDeLetrasJaDigitadas extends Comunicado implements Cloneable {
  private String letrasJaDigitadas;

  public ControladorDeLetrasJaDigitadas() {
    this.letrasJaDigitadas = "";
  }

  public boolean isJaDigitada(char letra) {
    boolean possuiLetra = false;

    for (int i = 0; i < this.letrasJaDigitadas.length(); i++) {
      if (letrasJaDigitadas.charAt(i) == letra)
        possuiLetra = true;
    }

    return possuiLetra;
  }

  public void registre(char letra) throws Exception {
    if (letra == ' ')
      throw new Exception("Letra Ausente");

    if (this.isJaDigitada(letra))
      throw new Exception("Letra já digitada");

    this.letrasJaDigitadas += letra;
  }

  @Override
  public String toString() {
    String todasAsletras = "";

    for (int i = 0; i < this.letrasJaDigitadas.length(); i++) {
      // Condicional com o objetivo de não colocar "," depois da última letra
      if (i != this.letrasJaDigitadas.length() - 1)
        todasAsletras += Character.toString(this.letrasJaDigitadas.charAt(i)) + ", ";
      else
        todasAsletras += Character.toString(this.letrasJaDigitadas.charAt(i));
    }

    return todasAsletras;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;

    if (obj == null)
      return false;

    if (obj.getClass() != ControladorDeLetrasJaDigitadas.class)
      return false;

    ControladorDeLetrasJaDigitadas cljd = (ControladorDeLetrasJaDigitadas) obj;
    if (!this.letrasJaDigitadas.equals(cljd.letrasJaDigitadas))
      return false;

    return true;
  }

  @Override
  public int hashCode() {
    int ret = 666;

    ret = 11 * ret + new String(this.letrasJaDigitadas).hashCode();

    if (ret < 0)
      ret = -ret;

    return ret;
  }

  public ControladorDeLetrasJaDigitadas(ControladorDeLetrasJaDigitadas controladorDeLetrasJaDigitadas)
      throws Exception {
    if (controladorDeLetrasJaDigitadas == null)
      throw new Exception("Controlador Ausente");

    this.letrasJaDigitadas = controladorDeLetrasJaDigitadas.letrasJaDigitadas;
  }

  @Override
  public Object clone() {
    ControladorDeLetrasJaDigitadas ret = null;
    try {
      ret = new ControladorDeLetrasJaDigitadas(this);
    } catch (Exception erro) {
    } // Não tratamos pois sabemos que não ocorrerá

    return ret;
  }
}
