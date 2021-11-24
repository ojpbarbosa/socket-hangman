public class PedidoDeRegistramentoDeLetra extends Comunicado {
  private char letra;
  
  public PedidoDeRegistramentoDeLetra(char letra) {
    this.letra = letra;
  }

  public char getLetra() {
    return this.letra;
  }

  public String toString() {
    return ("" + this.letra);
  }
}
