import java.io.*;
import java.util.*;
import java.net.*;

public class Servidor {
	public static final String PORTA_PADRAO = "3000";

	public static void main(String[] args) {
		if (args.length > 1) {
			System.err.println("Uso esperado: java Servidor.java [PORTA]\n");
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
		} catch (Exception e) {
			System.err.println("Escolha uma porta que possa ser usada para o jogo!");
			return;
		}

		try {
			File logo = new File("../logo.txt");
			Scanner scanner = new Scanner(logo);
			while (scanner.hasNextLine())
				System.out.println(scanner.nextLine());
			scanner.close();
		} catch (Exception e) {
		}

		System.out.println("\nO servidor esta ativo! Para desativa-lo,");
		System.out.println("use o comando \"desativar\".");
		try {
			System.out.println("Endereco IP do jogo: " + InetAddress.getLocalHost().getHostAddress());
		} catch (Exception e) {
		}
		System.out.print("\n> ");

		for (;;) {
			String comando = null;
			try {
				comando = Teclado.getUmString();
			} catch (Exception e) {
			}

			if (comando.toLowerCase().equals("desativar")) {
				synchronized (jogadores) {
					ComunicadoDeDesligamento comunicadoDeDesligamento = new ComunicadoDeDesligamento();

					for (Parceiro jogador : jogadores) {
						try {
							jogador.receba(comunicadoDeDesligamento);
							jogador.adeus();
						} catch (Exception ex) {
						}
					}
				}
				System.out.println("O servidor foi desativado!");
				System.exit(0);
			} else
				System.out.println("Comando inválido");
		}
	}
}
