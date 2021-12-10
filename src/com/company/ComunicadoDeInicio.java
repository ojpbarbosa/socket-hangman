package com.company;

public class ComunicadoComecar extends Comunicado {
    private Palavra palavra;
    private Tracinhos tracinhos;
    private ControladorDeErros controladorDeErros;
    private ControladorDeLetrasJaDigitadas controladorDeLetrasJaDigitadas;

    public ComunicadoComecar(Palavra palavra, Tracinhos tracinhos, ControladorDeErros controladorDeErros,
                             ControladorDeLetrasJaDigitadas controladorDeLetrasJaDigitadas) {
        this.palavra = palavra;
        this.tracinhos = tracinhos;
        this.controladorDeErros = controladorDeErros;
        this.controladorDeLetrasJaDigitadas = controladorDeLetrasJaDigitadas;
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

    public void setPalavra(Palavra palavra) {
        this.palavra = palavra;
    }

    public void setTracinhos(Tracinhos tracinhos) {
        this.tracinhos = tracinhos;
    }

    public void setControladorDeErros(ControladorDeErros controladorDeErros) {
        this.controladorDeErros = controladorDeErros;
    }

    public void setControladorDeLetrasJaDigitadas(ControladorDeLetrasJaDigitadas controladorDeLetrasJaDigitadas) {
        this.controladorDeLetrasJaDigitadas = controladorDeLetrasJaDigitadas;
    }
}