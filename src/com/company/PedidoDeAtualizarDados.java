package com.company;

public class PedidoDeAtualizarDados extends Comunicado {
  private Palavra palavra;
  private Tracinhos tracinhos;
  private ControladorDeErros controladorDeErros;
  private ControladorDeLetrasJaDigitadas controladorDeLetrasJaDigitadas;

  public PedidoDeAtualizarDados(ComunicadoDeInicio dadosDaForca) {
    this.palavra = dadosDaForca.getPalavra();
    this.tracinhos = dadosDaForca.getTracinhos();
    this.controladorDeErros = dadosDaForca.getControladorDeErros();
    this.controladorDeLetrasJaDigitadas = dadosDaForca.getControladorDeLetrasJaDigitadas();
  }

  public Palavra getPalavra() {
    return this.palavra;
  }

  public Tracinhos getTracinhos() {
    return this.tracinhos;
  }

  public ControladorDeErros getControladorDeErros() {
    return this.controladorDeErros;
  }

  public ControladorDeLetrasJaDigitadas getControladorDeLetrasJaDigitadas() {
    return this.controladorDeLetrasJaDigitadas;
  }
}