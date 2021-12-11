package com.company;

public class ComunicadoDeDerrotaPorAtingirMaximoDeErros extends Comunicado {
    private int grupo;

    public ComunicadoDeDerrotaPorAtingirMaximoDeErros(int grupo) {
        this.grupo = grupo;
    }

    public int getGrupo() {
        return this.grupo;
    }
}