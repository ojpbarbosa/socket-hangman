package com.company;

public class ComunicadoDeVitoriaPorAcertarPalavra extends Comunicado {
    private int grupo;

    public ComunicadoDeVitoriaPorAcertarPalavra(int grupo) {
        this.grupo = grupo;
    }

    public int getGrupo() {
        return this.grupo;
    }
 }