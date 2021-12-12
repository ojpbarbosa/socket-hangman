package com.company;

import java.io.*;
import java.net.*;

public class Cliente5 {
  public static final String HOST_PADRAO = "localhost";
  public static final int PORTA_PADRAO = 3000;

  public static void main(String[] args) throws Exception {
    if (args.length > 2) {
      System.err.println("Uso esperado: Java Cliente [HOST [PORTA]]\n");
      return;
    }

    Socket conexao = null;

    try {
      String host = Cliente.HOST_PADRAO;
      int porta = Cliente.PORTA_PADRAO;

      if (args.length > 0)
        host = args[0];

      if (args.length == 2)
        porta = Integer.parseInt(args[1]);

      conexao = new Socket(host, porta);
    } catch (Exception erro) {
      System.err.println("Indique o servidor e a porta corretos!\n");
      return;
    }

    ObjectOutputStream transmissor = null;

    try {
      transmissor = new ObjectOutputStream(conexao.getOutputStream());
    } catch (Exception erro) {
      System.err.println("Indique o servidor e a porta corretos!\n");
      return;
    }

    ObjectInputStream receptor = null;

    try {
      receptor = new ObjectInputStream(conexao.getInputStream());
    } catch (Exception erro) {
      System.err.println("Indique o servidor e a porta corretos!\n");
      return;
    }

    Parceiro servidor = null;

    try {
      servidor = new Parceiro(conexao, receptor, transmissor);
    } catch (Exception erro) {
      System.err.println("Indique o servidor e a porta corretos!\n");
      return;
    }

    TratadoraDeComunicadoDeDesligamento tratadoraDeComunicadoDeDesligamento = null;
    try {
      tratadoraDeComunicadoDeDesligamento = new TratadoraDeComunicadoDeDesligamento(servidor);
    } catch (Exception erro) {
    } // sei que servidor foi instanciado
    tratadoraDeComunicadoDeDesligamento.start();

    System.out.println("\nConectado!\n");
    System.out.println("Aguardando outros 2 adversários...");

    ComunicadoDeInicio dadosDaForca = null;
    try {
      Comunicado comunicado = null;
      do {
        comunicado = (Comunicado) servidor.espie();
      } while (!(comunicado instanceof ComunicadoDeInicio));
      comunicado = servidor.envie();
      dadosDaForca = (ComunicadoDeInicio) comunicado;
    } catch (Exception erro) {
    }
    System.out.println("\nSua partida está sendo iniciada!");

    servidor.receba(new PedidoDeAtualizarDados(dadosDaForca));

    int grupo = dadosDaForca.getGrupo();
    boolean jogando = true;
    Comunicado comunicado = null;
    do {
      comunicado = null;
      do {
        comunicado = servidor.espie();
      } while (
              !(comunicado instanceof ComunicadoDeDerrotaPorAtingirMaximoDeErros) &&
                      !(comunicado instanceof ComunicadoDeDerrotaPorErrarPalavra) &&
                      !(comunicado instanceof ComunicadoDeVitoriaPorAcertarPalavra) &&
                      !(comunicado instanceof ComunicadoDeVitoriaPorNaoHaverMaisJogadores) &&
                      !(comunicado instanceof ComunicadoDeSeuTurno)
      );
      comunicado = servidor.envie();

      if (comunicado instanceof ComunicadoDeVitoriaPorAcertarPalavra) {
        System.out.println("\nOh, nao! Um jogador ganhou por acertar a palavra!");
        System.out.println("Isso quer dizer que infelizmente sua partida acaba aqui :(");
        jogando = false;
      }

      else if (comunicado instanceof ComunicadoDeVitoriaPorNaoHaverMaisJogadores) {
        System.out.println("\nParabens!!! Voce eh o unico jogador restante e consequentemente GANHOU O JOGO!");
        System.out.println("\nPara sua curiosidade, a palavra era " + dadosDaForca.getPalavra() + "!");
        jogando = false;
      }

      else if (comunicado instanceof ComunicadoDeDerrotaPorAtingirMaximoDeErros) {
        System.out.println("\nOh, nao! Um jogador atingiu o maximo de erros!");
        System.out.println("A pessoa a que voces estavam tentando salvar foi enforcada x(");
        System.out.println("Isso quer dizer que infelizmente sua partida acaba aqui :(");
        jogando = false;
      }

      else if (comunicado instanceof ComunicadoDeDerrotaPorErrarPalavra)
        System.out.println("\nOh, nao! Um outro jogador foi eliminado por tentar acertar a palavra e errar!");

      else if (comunicado instanceof ComunicadoDeSeuTurno) {
        System.out.println("\nSua vez de jogar!\n");

        ComunicadoDeSeuTurno cst = (ComunicadoDeSeuTurno) comunicado;
        dadosDaForca.setPalavra(cst.getPalavra());
        dadosDaForca.setTracinhos(cst.getTracinhos());
        dadosDaForca.setControladorDeErros(cst.getControladorDeErros());
        dadosDaForca.setControladorDeLetrasJaDigitadas(cst.getControladorDeLetrasJaDigitadas());

        Palavra palavra = dadosDaForca.getPalavra();
        Tracinhos tracinhos = dadosDaForca.getTracinhos();
        ControladorDeErros controladorDeErros = dadosDaForca.getControladorDeErros();
        ControladorDeLetrasJaDigitadas controladorDeLetrasJaDigitadas = dadosDaForca.getControladorDeLetrasJaDigitadas();

        servidor.receba(new PedidoDeAtualizarDados(dadosDaForca));

        System.out.println("Palavra......: " + tracinhos);
        System.out.println("Digitadas....: " + controladorDeLetrasJaDigitadas);
        System.out.println("Erros........: " + controladorDeErros);

        try {
          String opcao;
          do {
            System.out.println("Sua vez de jogar, o que deseja fazer: adivinhar a [P]alavra do jogo, adivinhar uma [L]etra ou [S]air do jogo?");
            System.out.print("Escolha uma opcao: ");
            opcao = Teclado.getUmString().toUpperCase();
          } while (!opcao.equals("P") && !opcao.equals("L") && !opcao.equals("S"));

          if (opcao.equals("P")) {
            System.out.print("Qual é a palavra ? ");
            Palavra chute = new Palavra(Teclado.getUmString().toUpperCase());

            if (palavra.compareTo(chute) == 0) {
              System.out.println("Parabens!!! Voce acertou a palavra e consequentemente GANHOU O JOGO!");

              servidor.receba(new ComunicadoDeVitoriaPorAcertarPalavra(grupo));
              jogando = false;
            }

            else {
              System.out.println("Que pena, voce errou a palavra\n");
              System.out.println("Isso quer dizer que infelizmente sua partida acaba aqui :(");
              System.out.println("Adeus.......");

              servidor.receba(new PedidoDeNumeroDeJogadores(grupo));
              do {
                comunicado = servidor.espie();
              } while (!(comunicado instanceof ComunicadoDeNumeroDeJogadores));
              comunicado = servidor.envie();
              int numeroDeJogadoresRemanescentes = ((ComunicadoDeNumeroDeJogadores) comunicado).getNumeroDeJogadores() - 1;

              if (numeroDeJogadoresRemanescentes > 1)
                servidor.receba(new ComunicadoDeFimDeTurno(grupo));

              else
                servidor.receba(new ComunicadoDeDerrotaPorErrarPalavra(grupo));

              servidor.receba(new PedidoParaSair(grupo));
              System.out.println("\nObrigado por jogar!");
              System.exit(0);
            }
          }

          else if (opcao.equals("L")) {
            System.out.print("Qual letra ? ");
            char letra = Character.toUpperCase(Teclado.getUmString().charAt(0));

            // verifica se uma letra já foi digitada
            servidor.receba(new ComunicadoDeLetraJaDigitada(letra));
            do {
              comunicado = (Comunicado) servidor.espie();
            } while (!(comunicado instanceof ComunicadoDeLetraJaDigitada));
            comunicado = servidor.envie();
            boolean isJaDigitada = ((ComunicadoDeLetraJaDigitada) comunicado).getIsJaDigitada();

            if (isJaDigitada)
              System.err.println("Essa letra ja foi digitada!\n");

            else {
              servidor.receba(new PedidoDeRegistroDeLetra(letra));

              int qtdDeAparicoes = palavra.getQuantidade(letra);

              if (qtdDeAparicoes == 0) {
                System.err.println("\nA palavra nao tem a letra '" + letra + "'!");

                servidor.receba(new PedidoDeRegistroDeErro());

                servidor.receba(new PedidoDeMaximoDeErros());
                comunicado = null;
                do {
                  comunicado = (Comunicado) servidor.espie();
                } while (!(comunicado instanceof ComunicadoDeMaximoDeErros));
                comunicado = servidor.envie();
                boolean isAtingidoMaximoDeErros = ((ComunicadoDeMaximoDeErros) comunicado).isAtingidoMaximoDeErros();

                if (isAtingidoMaximoDeErros) {
                  System.out.println("\nE com esse erro voce perdeu todas suas chances de acertar uma letra :(\n");
                  System.out.println("A palavra era " + palavra + "!");
                  System.out.println("Adeus.......");

                  servidor.receba(new ComunicadoDeDerrotaPorAtingirMaximoDeErros(grupo));
                  jogando = false;
                }
              }

              else {
                System.out.println("\nA palavra tem a letra '" + letra + "'!");

                ComunicadoDeRevelacao comunicadoDeRevelacao = null;
                for (int i = 0; i < qtdDeAparicoes; i++) {
                  int posicao = palavra.getPosicaoDaIezimaOcorrencia(i, letra);

                  servidor.receba(new PedidoDeRevelacao(posicao, letra));
                  comunicado = null;
                  do {
                    comunicado = (Comunicado) servidor.espie();
                  } while (!(comunicado instanceof ComunicadoDeRevelacao));
                  comunicado = servidor.envie();
                  comunicadoDeRevelacao = ((ComunicadoDeRevelacao) comunicado);
                }

                tracinhos = comunicadoDeRevelacao.getTracinhos();
                if (!tracinhos.isAindaComTracinhos()) {
                  servidor.receba(new ComunicadoDeVitoriaPorAcertarPalavra(grupo));
                  System.out.println("Parabéns!!! Voce acertou a palavra, que era " + palavra
                          + ", e consequentemente GANHOU O JOGO!");
                  jogando = false;
                }
              }
            }
          }

          else
            jogando = false;
        } catch (Exception erro) {
        }
        if (jogando)
          System.out.println("\nOutro jogador ira jogar agora!");

        servidor.receba(new PedidoDeNumeroDeJogadores(grupo));
        do {
          comunicado = servidor.espie();
        } while (!(comunicado instanceof ComunicadoDeNumeroDeJogadores));
        comunicado = servidor.envie();
        int numeroDeJogadoresRemanescentes = ((ComunicadoDeNumeroDeJogadores) comunicado).getNumeroDeJogadores() - 1;

        if (jogando || numeroDeJogadoresRemanescentes > 1)
          servidor.receba(new ComunicadoDeFimDeTurno(grupo));
      }
    } while (jogando);

    try {
      servidor.receba(new PedidoParaSair(grupo));
    } catch (Exception erro) {
    }
    System.out.println("\nObrigado por jogar!");
    System.exit(0);
  }
}
