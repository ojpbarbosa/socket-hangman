package com.company;

public class PedidoDeNumeroDeJogadores extends Comunicado {
    private int grupo;

    public PedidoDeNumeroDeJogadores(int grupo) {
        this.grupo = grupo;
    }

    public int getGrupo() {
        return this.grupo;
    }
}
