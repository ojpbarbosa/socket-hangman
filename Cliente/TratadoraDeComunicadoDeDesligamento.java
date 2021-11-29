public class TratadoraDeComunicadoDeDesligamento extends Thread {
  private Parceiro servidor;

  public TratadoraDeComunicadoDeDesligamento(Parceiro servidor) throws Exception {
    if (servidor == null)
      throw new Exception("Servidor ausente");

    this.servidor = servidor;
  }

  public void run() {
    for (;;) {
      System.out.println("Tratadora de comunicado de desligamento rodando!");
      try {
        if (this.servidor.espie() instanceof ComunicadoDeDesligamento) {
          System.out.println("\nO servidor vai ser desligado agora;");
          System.err.println("volte mais tarde!\n");
          System.exit(0);
        }
      } catch (Exception erro) {
      }
    }
  }
}
