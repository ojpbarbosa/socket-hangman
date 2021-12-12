package com.company;

import java.io.*;
import java.net.*;
import java.util.*;

public class SupervisoraDeConexao extends Thread {
  private Parceiro jogador;
  private Socket conexao;
  private ArrayList<Parceiro> jogadores;
  private static ArrayList<ArrayList<Parceiro>> grupos = new ArrayList<>();
  private Palavra palavra;
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

        if (this.jogadores.size() % 3 == 0) {
          ArrayList<Parceiro> grupo = new ArrayList<>();
          for (int i = 3; i > 0; i--)
            grupo.add(this.jogadores.get(this.jogadores.size() - i));

          grupos.add(grupo);

          this.palavra = BancoDePalavras.getPalavraSorteada();
          this.tracinhos = new Tracinhos(this.palavra.getTamanho());
          this.controladorDeLetrasJaDigitadas = new ControladorDeLetrasJaDigitadas();
          this.controladorDeErros = new ControladorDeErros((int) (this.palavra.getTamanho() * 0.6));

          for (Parceiro jogador : grupo)
            jogador.receba(new ComunicadoDeInicio(
                    grupos.indexOf(grupo),
                    this.palavra,
                    this.tracinhos,
                    this.controladorDeErros,
                    this.controladorDeLetrasJaDigitadas));

          grupo.get(0).receba(new ComunicadoDeSeuTurno(
              this.palavra,
              this.tracinhos,
              this.controladorDeErros,
              this.controladorDeLetrasJaDigitadas));
        }
      }

      for (;;) {
        Comunicado comunicado = this.jogador.envie();

        if (comunicado == null)
          return;

        else if (comunicado instanceof ComunicadoDeDerrotaPorAtingirMaximoDeErros cdame) {
          ArrayList<Parceiro> grupo = grupos.get(cdame.getGrupo());

          synchronized (grupo) {
            for (Parceiro jogador : grupo)
              jogador.receba(new ComunicadoDeDerrotaPorAtingirMaximoDeErros(cdame.getGrupo()));
          }
        }

        else if (comunicado instanceof ComunicadoDeDerrotaPorErrarPalavra cdep) {
          ArrayList<Parceiro> grupo = grupos.get(cdep.getGrupo());

          synchronized (grupo) {
            for (Parceiro jogador : grupo)
              jogador.receba(new ComunicadoDeDerrotaPorErrarPalavra(cdep.getGrupo()));

            // int jogadorDaVez = grupo.indexOf(jogador);

            if (grupo.size() == 1)
              grupo.get(0).receba(new ComunicadoDeVitoriaPorNaoHaverMaisJogadores());

            /* else if (jogadorDaVez < grupo.size() - 1)
              grupo.get(jogadorDaVez + 1).receba(new ComunicadoDeSeuTurno(
                      this.palavra,
                      this.tracinhos,
                      this.controladorDeErros,
                      this.controladorDeLetrasJaDigitadas));

            else
              grupo.get(0).receba(new ComunicadoDeSeuTurno(
                      this.palavra,
                      this.tracinhos,
                      this.controladorDeErros,
                      this.controladorDeLetrasJaDigitadas)); */
          }
        }

        else if (comunicado instanceof ComunicadoDeFimDeTurno cft) {
          ArrayList<Parceiro> grupo = grupos.get(cft.getGrupo());

          synchronized (grupo) {
            int jogadorDaVez = grupo.indexOf(jogador);

            if (jogadorDaVez < grupo.size() - 1)
              grupo.get(jogadorDaVez + 1).receba(new ComunicadoDeSeuTurno(
                      this.palavra,
                      this.tracinhos,
                      this.controladorDeErros,
                      this.controladorDeLetrasJaDigitadas));

            else
              grupo.get(0).receba(new ComunicadoDeSeuTurno(
                      this.palavra,
                      this.tracinhos,
                      this.controladorDeErros,
                      this.controladorDeLetrasJaDigitadas));
          }
        }

        else if (comunicado instanceof ComunicadoDeLetraJaDigitada cljd) {
          cljd.setJaDigitada(this.controladorDeLetrasJaDigitadas.isJaDigitada(cljd.getLetra()));

          this.jogador.receba(cljd);
        }

        else if (comunicado instanceof ComunicadoDeVitoriaPorAcertarPalavra cvap) {
          ArrayList<Parceiro> grupo = grupos.get(cvap.getGrupo());

          synchronized (grupo) {
            for (Parceiro jogador : grupo)
              jogador.receba(new ComunicadoDeVitoriaPorAcertarPalavra(cvap.getGrupo()));
          }
        }

        else if (comunicado instanceof PedidoDeAtualizarDados pad) {
          this.palavra = pad.getPalavra();
          this.tracinhos = pad.getTracinhos();
          this.controladorDeErros = pad.getControladorDeErros();
          this.controladorDeLetrasJaDigitadas = pad.getControladorDeLetrasJaDigitadas();
        }

        else if (comunicado instanceof PedidoDeMaximoDeErros) {
          this.jogador.receba(new ComunicadoDeMaximoDeErros(this.controladorDeErros.isAtingidoMaximoDeErros()));
        }

        else if (comunicado instanceof PedidoDeNumeroDeJogadores pnj) {
          ArrayList<Parceiro> grupo = grupos.get(pnj.getGrupo());

          this.jogador.receba(new ComunicadoDeNumeroDeJogadores(grupo.size()));
        }

        else if (comunicado instanceof PedidoDeRegistroDeErro) {
          this.controladorDeErros.registreUmErro();
        }

        else if (comunicado instanceof PedidoDeRegistroDeLetra prl) {
          this.controladorDeLetrasJaDigitadas.registre(prl.getLetra());
        }

        else if (comunicado instanceof PedidoDeRevelacao pr) {
          int posicao = pr.getPosicao();
          char letra = pr.getLetra();

          this.tracinhos.revele(posicao, letra);

          this.jogador.receba(new ComunicadoDeRevelacao(this.tracinhos));
        }

        else if (comunicado instanceof PedidoParaSair ps) {
          ArrayList<Parceiro> grupo = grupos.get(ps.getGrupo());
  
          synchronized (grupo) {
            grupo.remove(this.jogador);

            if (grupo.size() == 1)
              grupo.get(0).receba(new ComunicadoDeVitoriaPorNaoHaverMaisJogadores());
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