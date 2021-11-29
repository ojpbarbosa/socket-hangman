public class PedidoDeRegistroDeLetra extends Comunicado {
  private char letra;

  public PedidoDeRegistroDeLetra(char letra) {
    this.letra = letra;
  }

  public char getLetra() {
    return this.letra;
  }

  public String toString() {
    return ("" + this.letra);
  }
}
