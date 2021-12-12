package com.company;

import java.io.*;
import java.util.*;

public class Servidor {
  public static final String PORTA_PADRAO = "3000";

  public static void main(String[] args) {
    if (args.length > 1) {
      System.err.println("Uso esperado: java Servidor [PORTA]\n");
      return;
    }

    String porta = Servidor.PORTA_PADRAO;

    if (args.length == 1)
      porta = args[0];

    ArrayList<Parceiro> jogadores = new ArrayList<Parceiro>();

    AceitadoraDeConexao aceitadoraDeConexao = null;
    try {
      aceitadoraDeConexao = new AceitadoraDeConexao(porta, jogadores);
      aceitadoraDeConexao.start();
    } catch (Exception erro) {
      System.err.println("Escolha uma porta que possa ser usada para o jogo!");
      return;
    }

    System.out.println("\nO servidor esta ativo! Para desativa-lo,");
    System.out.println("use o comando \"desativar\".");

    for (;;) {
      System.out.print("\n> ");

      String comando = null;
      try {
        comando = Teclado.getUmString();
      } catch (Exception erro) {
      }

      if (comando.toLowerCase().equals("desativar")) {
        synchronized (jogadores) {
          for (Parceiro jogador : jogadores) {
            ComunicadoDeDesligamento comunicadoDeDesligamento = new ComunicadoDeDesligamento();

            try {
              jogador.receba(comunicadoDeDesligamento);
              jogador.adeus();
            } catch (Exception erro) {
            }
          }
        }
        System.out.println("O servidor foi desativado!");
        System.exit(0);
      } else
        System.out.print("Comando invalido!");
    }
  }
}
