import java.io.*;
import java.net.*;
import java.util.*;

public class SupervisoraDeConexao extends Thread {
  private Parceiro jogador;
  private Socket conexao;
  private ArrayList<Parceiro> jogadores;

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
    } catch (Exception e) {
      return;
    }

    ObjectInputStream receptor = null;
    try {
      receptor = new ObjectInputStream(this.conexao.getInputStream());
    } catch (Exception e) {
      try {
        transmissor.close();
      } catch (Exception falha) {
      }

      return;
    }

    try {
      this.jogador = new Parceiro(this.conexao, receptor, transmissor);
    } catch (Exception e) {
    }

    try {
      synchronized (this.jogadores) {
        this.jogadores.add(this.jogador);
      }

      for (;;) {
        Comunicado comunicado = this.jogador.envie();

        if (comunicado == null)
          return;
        else if (comunicado instanceof PedidoDeLetraJaDigitada) {
          // TODO
        } else if (comunicado instanceof PedidoDeMaximoDeErros) {
          // TODO
        } else if (comunicado instanceof PedidoDePalavra) {
          // TODO
        } else if (comunicado instanceof PedidoDeRegistramentoDeLetra) {
          // TODO
        } else if (comunicado instanceof PedidoDeRegistroDeErro) {
          // TODO
        } else if (comunicado instanceof PedidoDeRevelacao) {
          // TODO
        } else if (comunicado instanceof PedidoParaSair) {
          synchronized (this.jogadores) {
            this.jogadores.remove(this.jogador);
          }
          this.jogador.adeus();
        }
      }
    } catch (Exception e) {
      try {
        transmissor.close();
        receptor.close();
      } catch (Exception falha) {
      }

      return;
    }
  }
}