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
      throw new Exception("Conexao ausente");

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

          jogadores.get(0).receba(new ComunicadoSeuTurno(
                  this.palavraSorteada,
                  this.tracinhos,
                  this.controladorDeErros,
                  this.controladorDeLetrasJaDigitadas));
        }
      }

      for (;;) {
        Comunicado comunicado = this.jogador.envie();

        if (comunicado == null)
          return;
        // pedido de atualizar dados
        else if (comunicado instanceof PedidoDeAtualizarDados pedido) {
          this.palavraSorteada = pedido.getPalavra();
          this.tracinhos = pedido.getTracinhos();
          this.controladorDeErros = pedido.getControladorDeErros();
          this.controladorDeLetrasJaDigitadas = pedido.getControladorDeLetrasJaDigitadas();
        }
        else if (comunicado instanceof PedidoDeDados) {
          ComunicadoDeDados comunicadoDeDados = new ComunicadoDeDados(
                  this.palavraSorteada,
                  this.tracinhos,
                  this.controladorDeErros,
                  this.controladorDeLetrasJaDigitadas);

          jogador.receba(comunicadoDeDados);
        }
        // pedido de letra já digitada
        else if (comunicado instanceof ComunicadoDeLetraJaDigitada cljd) {
          cljd.setJaDigitada(this.controladorDeLetrasJaDigitadas.isJaDigitada(cljd.getLetra()));

          this.jogador.receba(cljd);
        }
        // pedido de registro de letra
        else if (comunicado instanceof PedidoDeRegistroDeLetra prl) {
          char letra = prl.getLetra();

          this.controladorDeLetrasJaDigitadas.registre(letra);
        }
        // pedido de máximo de erros
        else if (comunicado instanceof PedidoDeMaximoDeErros) {
          boolean isAtingiuMaximoDeErros = this.controladorDeErros.isAtingidoMaximoDeErros();
          PedidoDeMaximoDeErros pedidoDeMaximoDeErros = new PedidoDeMaximoDeErros(isAtingiuMaximoDeErros);

          this.jogador.receba(pedidoDeMaximoDeErros);
        }
        // pedido de registro de erro
        else if (comunicado instanceof PedidoDeRegistroDeErro) {
          controladorDeErros.registreUmErro();
        }
        // pedido de revelação
        else if (comunicado instanceof PedidoDeRevelacao pr) {
          int posicao = pr.getPosicao();
          char letra = pr.getLetra();

          tracinhos.revele(posicao, letra);
        }
        // comunicado ganhou por acertar a palavra
        else if (comunicado instanceof ComunicadoGanhouPorAcertarPalavra) {
          synchronized (this.jogadores) {
            for (Parceiro jogador : this.jogadores) {
              if (jogador != this.jogador)
                jogador.receba(new ComunicadoGanhouPorAcertarPalavra());
            }
          }
        }
        // comunicado perdeu por errar a palavra
        else if (comunicado instanceof ComunicadoPerdeuPorErrarPalavra cpep) { //////////////
          synchronized (this.jogadores) {
            for (Parceiro jogador : this.jogadores) {
              if (jogador != this.jogador)
                jogador.receba(new ComunicadoPerdeuPorErrarPalavra(null, null, null, null));
            }

            int jogadorDaVez = this.jogadores.indexOf(jogador);

            if (jogadorDaVez < this.jogadores.size() - 1)
              jogadores.get(jogadorDaVez + 1).receba(new ComunicadoSeuTurno(
                      cpep.getPalavra(),
                      cpep.getTracinhos(),
                      cpep.getControladorDeErros(),
                      cpep.getControladorDeLetrasJaDigitadas()));
            else
              jogadores.get(0).receba(new ComunicadoSeuTurno(
                      cpep.getPalavra(),
                      cpep.getTracinhos(),
                      cpep.getControladorDeErros(),
                      cpep.getControladorDeLetrasJaDigitadas()));
          }
        }
        // comunicado fim de turno
        else if (comunicado instanceof ComunicadoFimDeTurno cft) {
          synchronized (this.jogadores) {
            int jogadorDaVez = this.jogadores.indexOf(jogador);

            if (jogadorDaVez < this.jogadores.size() - 1)
              jogadores.get(jogadorDaVez + 1).receba(new ComunicadoSeuTurno(
                      cft.getPalavra(),
                      cft.getTracinhos(),
                      cft.getControladorDeErros(),
                      cft.getControladorDeLetrasJaDigitadas()));
            else
              jogadores.get(0).receba(new ComunicadoSeuTurno(
                      cft.getPalavra(),
                      cft.getTracinhos(),
                      cft.getControladorDeErros(),
                      cft.getControladorDeLetrasJaDigitadas()));
          }
        }
        // pedido para sair
        else if (comunicado instanceof PedidoParaSair) {
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

      System.err.println();

      return;
    }
  }
}