package com.company;

public class ComunicadoDeNumeroDeJogadores extends Comunicado {
    private int numeroDeJogadores;

    public ComunicadoDeNumeroDeJogadores(int numeroDeJogadores) {
        this.numeroDeJogadores = numeroDeJogadores;
    }

    public int getNumeroDeJogadores() {
        return this.numeroDeJogadores;
    }
}
