package com.company;

import java.io.*;
import java.net.*;
import java.util.*;

public class SupervisoraDeConexao extends Thread {
  private Parceiro jogador;
  private Socket conexao;
  private ArrayList<Parceiro> jogadores;
  private Palavra palavraSorteada;
  private Tracinhos tracinhos;
  private ControladorDeErros controladorDeErros;
  private ControladorDeLetrasJaDigitadas controladorDeLetrasJaDigitadas;

  public SupervisoraDeConexao(Socket conexao, ArrayList<Parceiro> jogadores) throws Exception {
    if (conexao == null)
      throw new Exception("Conexao ausente5");

    if (jogadores == null)
      throw new Exception("Jogadores ausentes");

    this.conexao = conexao;
    this.jogadores = jogadores;
  }

  public void run() {
    ObjectOutputStream transmissor;
    try {
      transmissor = new ObjectOutputStream(this.conexao.getOutputStream());
    } catch (Exception erro) {
      return;
    }

    ObjectInputStream receptor = null;
    try {
      receptor = new ObjectInputStream(this.conexao.getInputStream());
    } catch (Exception erro) {
      try {
        transmissor.close();
      } catch (Exception falha) {
      }

      return;
    }

    try {
      this.jogador = new Parceiro(this.conexao, receptor, transmissor);
    } catch (Exception erro) {
    }

    try {
      synchronized (this.jogadores) {
        this.jogadores.add(this.jogador);

        if (this.jogadores.size() == 3) {
          this.palavraSorteada = BancoDePalavras.getPalavraSorteada();
          this.tracinhos = new Tracinhos(this.palavraSorteada.getTamanho());
          this.controladorDeLetrasJaDigitadas = new ControladorDeLetrasJaDigitadas();
          this.controladorDeErros = new ControladorDeErros((int) (this.palavraSorteada.getTamanho() * 0.6));

          for (Parceiro jogador : this.jogadores) {
            ComunicadoComecar comunicadoComecar = new ComunicadoComecar(this.palavraSorteada, this.tracinhos,
                this.controladorDeErros,
                this.controladorDeLetrasJaDigitadas);

            jogador.receba(comunicadoComecar);
          }

          jogadores.get(0).receba(new ComunicadoSeuTurno());
        }
      }

      for (;;) {
        Comunicado comunicado = this.jogador.envie();

        if (comunicado == null)
          return;
        else if (comunicado instanceof PedidoDeAtualizarDados pedido) {
          System.out.println(pedido.getPalavra());
          this.palavraSorteada = pedido.getPalavra();
          this.tracinhos = pedido.getTracinhos();
          this.controladorDeErros = pedido.getControladorDeErros();
          this.controladorDeLetrasJaDigitadas = pedido.getControladorDeLetrasJaDigitadas();
        } else if (comunicado instanceof PedidoDeNome) {
          String nome = ((PedidoDeNome) comunicado).getNome();
          this.jogador.setNome(nome);
        } else if (comunicado instanceof PedidoDeLetraJaDigitada) {
          char letraParaConferir = ((PedidoDeLetraJaDigitada) comunicado).getLetra();
          boolean isJaDigitada = controladorDeLetrasJaDigitadas.isJaDigitada(letraParaConferir);
          ((PedidoDeLetraJaDigitada) comunicado).setJaDigitada(isJaDigitada);

          this.jogador.receba((PedidoDeLetraJaDigitada) comunicado);
        } else if (comunicado instanceof PedidoDeMaximoDeErros) {
          boolean isAtingiuMaximoDeErros = controladorDeErros.isAtingidoMaximoDeErros();
          PedidoDeMaximoDeErros pedidoDeMaximoDeErros = new PedidoDeMaximoDeErros(isAtingiuMaximoDeErros);

          this.jogador.receba(pedidoDeMaximoDeErros);
        } else if (comunicado instanceof PedidoDeRegistroDeLetra) {
          char letraParaRegistrar = ((PedidoDeLetraJaDigitada) comunicado).getLetra();

          controladorDeLetrasJaDigitadas.registre(letraParaRegistrar);
        } else if (comunicado instanceof PedidoDeRegistroDeErro) {
          controladorDeErros.registreUmErro();
        } else if (comunicado instanceof PedidoDeRevelacao) {
          int posicaoParaRevelar = ((PedidoDeRevelacao) comunicado).getPosicao();
          char letraParaRevelar = ((PedidoDeRevelacao) comunicado).getLetra();

          tracinhos.revele(posicaoParaRevelar, letraParaRevelar);
        } else if (comunicado instanceof PedidoParaSair) {
          synchronized (this.jogadores) {
            this.jogadores.remove(this.jogador);
          }
          this.jogador.adeus();
        }
      }
    } catch (Exception erro) {
      try {
        transmissor.close();
        receptor.close();
      } catch (Exception falha) {
      }

      return;
    }
  }
}