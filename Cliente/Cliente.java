package com.company;

import java.net.*;
import java.util.concurrent.ExecutionException;
import java.io.*;

public class Cliente {
  public static final String HOST_PADRAO = "localhost";
  public static final int PORTA_PADRAO = 3000;

  public static void main(String[] args) {
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

    try
		{
			System.out.println("Conectado! \n");
			System.out.println("Aguardando um oponente...");
			ComunicadoComecar podeIr = (ComunicadoComecar)servidor.envie();
		}
		catch(Exception e)
		{}

    tratadoraDeComunicadoDeDesligamento.start();

    char continuar = ' ';

    do {
    //   Palavra palavraSorteada = BancoDePalavras.getPalavraSorteada();

    //   Tracinhos tracinhos = null;
    //   try {
    //     tracinhos = new Tracinhos(palavraSorteada.getTamanho());
    //   } catch (Exception erro) {
    //   }

    //   ControladorDeLetrasJaDigitadas controladorDeLetrasJaDigitadas = new ControladorDeLetrasJaDigitadas();

    //   ControladorDeErros controladorDeErros = null;
    //   try {
    //     controladorDeErros = new ControladorDeErros((int) (palavraSorteada.getTamanho() * 0.6));
    //   } catch (Exception erro) {
    //   }

    //   while (tracinhos.isAindaComTracinhos() && !controladorDeErros.isAtingidoMaximoDeErros()) {
    //     System.out.println("Palavra...: " + tracinhos);
    //     System.out.println("Digitadas.: " + controladorDeLetrasJaDigitadas);
    //     System.out.println("Erros.....: " + controladorDeErros);

      // Pegar a palavra sorteada do servidor
      servidor.receba(new PedidoDePalavra());
      Comunicado comunicado = null; 
      do {
        comunicado = (Comunicado)servidor.espie();
      }
      while (!(comunicado instanceof Palavra));
      Palavra palavraSorteada = (Palavra)servidor.envie();


      try {
        System.out.println("Sua vez de jogar, o que deseja fazer: adivinhar a palavra do jogo [1] ou digitar uma letra [2]");
        System.out.print("Escolha um número: ");
        int opcao = Teclado.getUmInt();

        if (opcao == 1) {
          System.out.print("Qual é a palavra ? ");
          Palavra palavraAdivinhada = new Palavra(Teclado.getUmString().toUpperCase());

          if (palavraSorteada.compareTo(palavraAdivinhada) == 0){
            System.out.println();
          }
        }
        else if (opcao == 2) {
          System.out.print("Qual letra? ");
          char letra = Character.toUpperCase(Teclado.getUmChar());

          if (controladorDeLetrasJaDigitadas.isJaDigitada(letra))
            System.err.println("Essa letra ja foi digitada!\n");
          else {
            // controladorDeLetrasJaDigitadas.registre(letra);
            servidor.receba(new PedidoDeRegistramentoDeLetra(letra));

            int qtd = palavraSorteada.getQuantidade(letra);

            if (qtd == 0) {
              System.err.println("A palavra nao tem essa letra!\n");
              // controladorDeErros.registreUmErro();
              servidor.receba(new PedidoDeRegistroDeErro());
            } else {
              for (int i = 0; i < qtd; i++) {
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

      if (controladorDeErros.isAtingidoMaximoDeErros())
        System.out.println("Que pena! Voce perdeu! A palavra era " + palavraSorteada + "\n");
      else // !tracinhos.isAindaComTracinhos()
        System.out.println("Parabens! Voce ganhou! A palavra era mesmo " + palavraSorteada + "\n");

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
    }
    while(continuar=='S');

    try {
      servidor.receba(new PedidoParaSair());
    } catch (Exception erro)
    {}
  
    System.out.println("Obrigado por jogar!");
    System.exit(0);
  }
}
