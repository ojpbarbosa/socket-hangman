package com.company;

public class PedidoParaSair extends Comunicado {
    private int grupo;

    public PedidoParaSair(int grupo) {
        this.grupo = grupo;
    }

    public int getGrupo() {
        return this.grupo;
    }
}
