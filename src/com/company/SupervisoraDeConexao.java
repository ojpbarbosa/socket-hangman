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
            ComunicadoDeInicio ComunicadoDeInicio = new ComunicadoDeInicio(
                this.palavraSorteada,
                this.tracinhos,
                this.controladorDeErros,
                this.controladorDeLetrasJaDigitadas);

            jogador.receba(ComunicadoDeInicio);
          }

          jogadores.get(0).receba(new ComunicadoSeuTurno(
              this.palavraSorteada,
              this.tracinhos,
              this.controladorDeErros,
              this.controladorDeLetrasJaDigitadas));
        }

        else if (this.jogadores.size() > 3)
          this.jogador.receba(new ComunicadoDeServidorCheio());
      }

      for (;;) {
        Comunicado comunicado = this.jogador.envie();

        if (comunicado == null)
          return;
        // pedido de atualizar dados
        else if (comunicado instanceof PedidoDeAtualizarDados pad) {
          this.palavraSorteada = pad.getPalavra();
          this.tracinhos = pad.getTracinhos();
          this.controladorDeErros = pad.getControladorDeErros();
          this.controladorDeLetrasJaDigitadas = pad.getControladorDeLetrasJaDigitadas();
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
          this.jogador.receba(new ComunicadoDeMaximoDeErros(this.controladorDeErros.isAtingidoMaximoDeErros()));
        }
        // pedido de registro de erro
        else if (comunicado instanceof PedidoDeRegistroDeErro) {
          this.controladorDeErros.registreUmErro();
        }
        // pedido de revelação
        else if (comunicado instanceof PedidoDeRevelacao pr) {
          int posicao = pr.getPosicao();
          char letra = pr.getLetra();

          this.tracinhos.revele(posicao, letra);

          this.jogador.receba(new ComunicadoDeRevelacao(this.tracinhos));
        }
        // comunicado ganhou por acertar a palavra
        else if (comunicado instanceof ComunicadoDeVitoriaPorAcertarPalavra) {
          synchronized (this.jogadores) {
            for (Parceiro jogador : this.jogadores)
              jogador.receba(new ComunicadoDeVitoriaPorAcertarPalavra());
          }
        } else if (comunicado instanceof ComunicadoDeDerrotaPorAtingirMaximoDeErros) {
          synchronized (this.jogadores) {
            for (Parceiro jogador : this.jogadores)
              jogador.receba(new ComunicadoDeDerrotaPorAtingirMaximoDeErros());
          }
        }
        // comunicado perdeu por errar a palavra
        else if (comunicado instanceof ComunicadoDeDerrotaPorErrarPalavra) {
          synchronized (this.jogadores) {
            for (Parceiro jogador : this.jogadores)
              jogador.receba(new ComunicadoDeDerrotaPorErrarPalavra());

            if (this.jogadores.size() == 1)
              jogadores.get(0).receba(new ComunicadoDeVitoriaPorNaoHaverJogadores());

            else {
              int jogadorDaVez = this.jogadores.indexOf(jogador);

              if (jogadorDaVez < this.jogadores.size() - 1)
                jogadores.get(jogadorDaVez + 1).receba(new ComunicadoSeuTurno(
                    this.palavraSorteada,
                    this.tracinhos,
                    this.controladorDeErros,
                    this.controladorDeLetrasJaDigitadas));

              else
                jogadores.get(0).receba(new ComunicadoSeuTurno(
                    this.palavraSorteada,
                    this.tracinhos,
                    this.controladorDeErros,
                    this.controladorDeLetrasJaDigitadas));
            }
          }
        }
        // comunicado fim de turno
        else if (comunicado instanceof ComunicadoFimDeTurno) {
          synchronized (this.jogadores) {
            int jogadorDaVez = this.jogadores.indexOf(jogador);

            if (jogadorDaVez < this.jogadores.size() - 1)
              jogadores.get(jogadorDaVez + 1).receba(new ComunicadoSeuTurno(
                  this.palavraSorteada,
                  this.tracinhos,
                  this.controladorDeErros,
                  this.controladorDeLetrasJaDigitadas));

            else
              jogadores.get(0).receba(new ComunicadoSeuTurno(
                  this.palavraSorteada,
                  this.tracinhos,
                  this.controladorDeErros,
                  this.controladorDeLetrasJaDigitadas));
          }
        }
        // pedido para sair
        else if (comunicado instanceof PedidoParaSair) {
          synchronized (this.jogadores) {
            this.jogadores.remove(this.jogador);

            if (this.jogadores.size() == 1)
              jogadores.get(0).receba(new ComunicadoDeVitoriaPorNaoHaverJogadores());
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