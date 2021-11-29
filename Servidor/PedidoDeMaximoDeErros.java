public class PedidoDeMaximoDeErros extends Comunicado {
  private boolean isAtingidoMaximoDeErros;

  public PedidoDeMaximoDeErros(boolean isAtingidoMaximoDeErros) {
    this.isAtingidoMaximoDeErros = isAtingidoMaximoDeErros;
  }

  public boolean getIsAtingidoMaximoDeErros() {
    return this.isAtingidoMaximoDeErros;
  }
}
