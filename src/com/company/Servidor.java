package com.company;

public class Servidor {
  public static String PORTA_PADRAO = "3000";
  public static void main(String[] args) {
    if (args.length > 1) {
      System.err.println("Uso esperado: Java Servidor [PORTA]\n");
      return;
    }

    String porta = Servidor.PORTA_PADRAO;

    if (args.length == 1)
      porta = args[0];
  } 
}
