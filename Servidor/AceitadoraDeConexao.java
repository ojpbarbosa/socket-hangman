import java.net.*;
import java.util.*;

public class AceitadoraDeConexao extends Thread {
  private ServerSocket pedido;
  private ArrayList<Parceiro> jogadores;

  public AceitadoraDeConexao(String porta, ArrayList<Parceiro> jogadores) throws Exception {
    if (porta == null)
      throw new Exception("Porta ausente!");

    try {
      this.pedido = new ServerSocket(Integer.parseInt(porta));
    } catch (Exception erro) {
      throw new Exception("Porta invalida");
    }

    if (jogadores == null)
      throw new Exception("Jogadores ausentes");

    this.jogadores = jogadores;
  }

  public void run() {
    for (;;) {
      Socket conexao = null;
      try {
        conexao = this.pedido.accept();
        System.out.print("\n- Jogador conectado!\n> ");
      } catch (Exception erro) {
        continue;
      }

      SupervisoraDeConexao supervisoraDeConexao = null;
      try {
        supervisoraDeConexao = new SupervisoraDeConexao(conexao, jogadores);
      } catch (Exception erro) {
      }
      supervisoraDeConexao.start();
    }
  }
}