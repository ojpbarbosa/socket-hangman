import java.io.*;
import java.util.*;
import java.net.*;

public class Cliente {
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
      System.err.println("Indique o servidor e a porta corretor!\n");
      return;
    }

    TratadoraDeComunicadoDeDesligamento tratadoraDeComunicadoDeDesligamento = null;
    try {
      tratadoraDeComunicadoDeDesligamento = new TratadoraDeComunicadoDeDesligamento(servidor);
    } catch (Exception erro) {
    } // sei que servidor foi instanciado

    
    char continuar = ' ';
    
    do {
      try {
        try {
          File logo = new File("../logo.txt");
          Scanner scanner = new Scanner(logo);
          while (scanner.hasNextLine())
            System.out.println(scanner.nextLine());
          scanner.close();
        } catch (Exception erro) {
        }
  
        System.out.println("\nConectado!\n");
        System.out.println("Aguardando um oponente...");
        ComunicadoComecar podeIr = (ComunicadoComecar) servidor.envie();
      } catch (Exception e) {
      }
  
      tratadoraDeComunicadoDeDesligamento.run();

      // Pegar a palavra sorteada do servidor
      servidor.receba(new PedidoDePalavra());
      Comunicado comunicado = null;
      do {
        comunicado = (Comunicado) servidor.espie();
      } while (!(comunicado instanceof Palavra));
      Palavra palavraSorteada = (Palavra) servidor.envie();

      try {
        System.out
            .println("Sua vez de jogar, o que deseja fazer: adivinhar a palavra do jogo [1] ou adivinhar uma letra [2]");
        System.out.print("Escolha um número: ");
        int opcao = Teclado.getUmInt();

        if (opcao == 1) {
          System.out.print("Qual é a palavra ? ");
          Palavra palavraAdivinhada = new Palavra(Teclado.getUmString().toUpperCase());

          if (palavraSorteada.compareTo(palavraAdivinhada) == 0) {
            System.out.println("Parabéns!!! Você acertou a palavra e consequentemente GANHOU O JOGO ");
            servidor.receba(new ComunicadoGanhoPorAcertarPalavra());
            return;
          } else {
            System.out.println("Que pena, você errou a palavra\n");
            System.out.println("Isso quer dizer que infelizmente sua partida acaba aqui :(");
            System.out.println("Adeus.......");

            servidor.receba(new ComunicadoPercaPorErrarPalavra());
            servidor.receba(new PedidoParaSair());
          }
        } else if (opcao == 2) {
          System.out.print("Qual letra? ");
          char letra = Character.toUpperCase(Teclado.getUmChar());

          // Verifica se uma letra já foi digitada
          servidor.receba(new PedidoDeLetraJaDigitada(letra));
          comunicado = null;
          do {
            comunicado = (Comunicado) servidor.espie();
          } while (!(comunicado instanceof PedidoDeLetraJaDigitada));
          PedidoDeLetraJaDigitada pdjd = (PedidoDeLetraJaDigitada) servidor.envie();
          boolean isJaDigitada = pdjd.getIsJaDigitada();

          if (isJaDigitada)
            System.out.println("Essa letra ja foi digitada!\n");
          else {
            servidor.receba(new PedidoDeRegistramentoDeLetra(letra));

            int qtdDeAparicoes = palavraSorteada.getQuantidade(letra);

            if (qtdDeAparicoes == 0) {
              System.err.println("A palavra nao tem essa letra!\n");
              
              servidor.receba(new PedidoDeMaximoDeErros());
              comunicado = null;
              do {
                comunicado = (Comunicado) servidor.espie();
              } while (!(comunicado instanceof PedidoDeMaximoDeErros));
              PedidoDeMaximoDeErros pme = (PedidoDeMaximoDeErros) servidor.envie();
              boolean isAtingidoMaximoDeErros = pme.getIsAtingidoMaximoDeErross();

              if (isAtingidoMaximoDeErros) {
                System.out.println("E com esse erro você perdeu todas suas chances de acertar uma letra :(\n");
                System.out.println("Adeus.......");

                servidor.receba(new PedidoParaSair());
              } 
              else {}
                servidor.receba(new PedidoDeRegistroDeErro());
            } else {
              for (int i = 0; i < qtdDeAparicoes; i++) {
                int posicao = palavraSorteada.getPosicaoDaIezimaOcorrencia(i, letra);
                // tracinhos.revele(posicao, letra);
                servidor.receba(new PedidoDeRevelacao(posicao, letra));
              }
              System.out.println();
            }
          }
        }

      } catch (Exception erro) {
        System.err.println(erro.getMessage());
      }

      // if (controladorDeErros.isAtingidoMaximoDeErros())
      // servidor.receba(new PedidoDeMaximoDeErros());
      // comunicado = null;
      // do {
      //   comunicado = (Comunicado) servidor.espie();
      // } while (!(comunicado instanceof PedidoDeMaximoDeErros));
      // PedidoDeMaximoDeErros pme = (PedidoDeMaximoDeErros) servidor.envie();
      // boolean isAtingidoMaximoDeErros = pme.getIsAtingidoMaximoDeErross();

      // if (isAtingidoMaximoDeErros)
      //   System.out.println("Que pena! Voce perdeu! A palavra era " + palavraSorteada + "\n");
      // else 
      //   

      for (;;) {
        try {
          System.out.print("Deseja jogar de novo (S/N)? ");
          continuar = Character.toUpperCase(Teclado.getUmChar());
          if (continuar != 'S' && continuar != 'N')
            System.err.println("Opcao invalida! Tente novamente...");
          else
            break;
        } catch (Exception erro) {
          System.err.println("Opcao invalida! Tente novamente...");
        }
      }
    } while (continuar == 'S');

    try {
      servidor.receba(new PedidoParaSair());
    } catch (Exception erro) {
    }

    System.out.println("Obrigado por jogar!");
    System.exit(0);
  }
}
