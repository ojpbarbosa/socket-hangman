package com.company;

import java.io.*;
import java.net.*;

public class Cliente3 {
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

    ComunicadoComecar dadosDaForca = null;
    try {
      System.out.println("\nConectado!\n");
      System.out.println("Aguardando outros 2 adversários...");
      Comunicado comunicado = null;
      do {
        comunicado = (Comunicado) servidor.espie();
      } while (!(comunicado instanceof ComunicadoComecar));
      comunicado = servidor.envie();
      dadosDaForca = (ComunicadoComecar) comunicado;
      // Junto com o comunicadoComecar vem os dados do jogo atual
    } catch (Exception erro) {
    }

    System.out.println("\nSua partida está sendo iniciada!\n");

    System.out.println(((Palavra)dadosDaForca.getPalavra()).toString()); ///////////////////////

    boolean jogando = true;

    Comunicado comunicado = null;
    do {
      comunicado = (Comunicado) servidor.espie();

      if (comunicado instanceof ComunicadoGanhouPorAcertarPalavra) {
        System.out.println("Oh, nao! Um outro jogador ganhou por acertar a palavra!");
        System.out.println("Isso quer dizer que infelizmente sua partida acaba aqui :(");
        jogando = false;
      }

      else if (comunicado instanceof ComunicadoPerdeuPorErrarPalavra) {
        System.out.println("Oh, nao! Um outro jogador foi eliminado por tentar acertar a palavra e errar!");
        jogando = false;
      }

      else if (comunicado instanceof ComunicadoSeuTurno) {
        comunicado = (ComunicadoSeuTurno) servidor.envie();

        Palavra palavraSorteada = (Palavra) dadosDaForca.getPalavra();
        Tracinhos tracinhos = dadosDaForca.getTracinhos();
        ControladorDeErros erros = dadosDaForca.getControladorDeErros();
        ControladorDeLetrasJaDigitadas letrasJaDigitadas = dadosDaForca.getControladorDeLetrasJaDigitadas();

        System.out.println("Palavra......: " + tracinhos.toString());
        System.out.println("Digitadas....: " + letrasJaDigitadas.toString());
        System.out.println("Erros........: " + erros.toString());

        try {
          String opcao;
          do {
            System.out
                    .println(
                            "Sua vez de jogar, o que deseja fazer: adivinhar a palavra do jogo [1] ou adivinhar uma letra [2]");
            System.out.print("Escolha um número: ");
            opcao = Teclado.getUmString();
          } while (!opcao.equals("1") && !opcao.equals("2"));

          if (opcao.equals("1")) {
            System.out.print("Qual é a palavra ? ");
            Palavra palavraAdivinhada = new Palavra(Teclado.getUmString().toUpperCase());

            if (palavraSorteada.compareTo(palavraAdivinhada) == 0) {
              System.out.println("Parabéns!!! Você acertou a palavra e consequentemente GANHOU O JOGO!");
              servidor.receba(new ComunicadoGanhouPorAcertarPalavra());
              break;
            } else {
              System.out.println("Que pena, você errou a palavra\n");
              System.out.println("Isso quer dizer que infelizmente sua partida acaba aqui :(");
              System.out.println("Adeus.......");

              servidor.receba(new ComunicadoPerdeuPorErrarPalavra());
              break;
            }
          } else {
            System.out.print("Qual letra? ");
            char letra = Character.toUpperCase(Teclado.getUmChar());

            // Verifica se uma letra já foi digitada
            boolean isJaDigitada = false;
            servidor.receba(new PedidoDeLetraJaDigitada(letra));
            try {
              Comunicado comunicadoLetras = null;
              do {
                comunicadoLetras = (Comunicado) servidor.espie();
              } while (!(comunicadoLetras instanceof PedidoDeLetraJaDigitada));
              comunicadoLetras = servidor.envie();
              PedidoDeLetraJaDigitada pdjd = (PedidoDeLetraJaDigitada) comunicadoLetras;
              isJaDigitada = pdjd.getIsJaDigitada();
            } catch (Exception erro) {
            }

            if (isJaDigitada)
              System.out.println("Essa letra ja foi digitada!\n");
            else {
              servidor.receba(new PedidoDeRegistroDeLetra(letra));

              int qtdDeAparicoes = palavraSorteada.getQuantidade(letra);

              if (qtdDeAparicoes == 0) {
                System.err.println("A palavra nao tem essa letra!\n");

                servidor.receba(new PedidoDeMaximoDeErros(false));
                comunicado = null;
                do {
                  comunicado = (Comunicado) servidor.espie();
                } while (!(comunicado instanceof PedidoDeMaximoDeErros));
                PedidoDeMaximoDeErros pme = (PedidoDeMaximoDeErros) servidor.envie();
                boolean isAtingidoMaximoDeErros = pme.isAtingidoMaximoDeErros();

                if (isAtingidoMaximoDeErros) {
                  System.out.println("E com esse erro você perdeu todas suas chances de acertar uma letra :(\n");
                  System.out.println("Adeus.......");

                  servidor.receba(new ComunicadoPercaPorAtingirMaximoDeErros());
                  break;
                } else
                  servidor.receba(new PedidoDeRegistroDeErro());
              } else {
                for (int i = 0; i < qtdDeAparicoes; i++) {
                  int posicao = palavraSorteada.getPosicaoDaIezimaOcorrencia(i, letra);

                  servidor.receba(new PedidoDeRevelacao(posicao, letra));
                }
              }
            }
          }
        } catch (Exception erro) {
        }
      }
    } while (jogando);

    try {
      servidor.receba(new PedidoParaSair());
    } catch (Exception erro) {
    }

    System.out.println("Obrigado por jogar!");
    System.exit(0);
  }
}
