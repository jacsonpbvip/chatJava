import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Cliente {
    private static final String IP_SERVIDOR = "localhost"; // ou o IP do seu servidor
    private static final int PORTA_SERVIDOR = 50123;

    public static void main(String[] args) {
        new Cliente().iniciar();
    }

    public void iniciar() {
        try (Socket socket = new Socket(IP_SERVIDOR, PORTA_SERVIDOR)) {
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            System.out.println("Digite seu apelido:");
            String apelido = in.readLine();

            // Verifica se o apelido é vazio
            if (apelido.trim().isEmpty()) {
                System.out.println("Apelido não pode ser vazio. Encerrando.");
                return;
            }

            out.println(apelido);

            Thread threadReceberMensagens = new Thread(new RecebedorMensagens(socket));
            threadReceberMensagens.start();

            String mensagem;
            while ((mensagem = in.readLine()) != null) {
                out.println(mensagem);
                if ("##sair##".equals(mensagem)) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
