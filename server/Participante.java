import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.System.Logger.Level;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

public class Participante implements Runnable {
    private Socket clienteSocket;
    private Servidor servidor;
    private PrintWriter out;
    private String apelido;
    private static final Logger logger = Logger.getLogger(ServicoMensagem.class.getName());

    public Participante(Socket clienteSocket, Servidor servidor) {
        this.clienteSocket = clienteSocket;
        this.servidor = servidor;
    }

    @Override
    public void run() {
        try {
            out = new PrintWriter(clienteSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clienteSocket.getInputStream()));

            apelido = in.readLine();

            // Verifica se o apelido é vazio
            if (apelido.trim().isEmpty()) {
                logger.warning("Apelido vazio recebido. Encerrando a conexão.");
                return;
            }

            servidor.enviarMensagemBroadcast("Servidor: " + apelido + " entrou no chat.", apelido);
            logger.info("Participante '" + apelido + "' entrou no chat.");

            String mensagem;
            while ((mensagem = in.readLine()) != null) {
                if ("##sair##".equals(mensagem)) {
                    break;
                }
                servidor.enviarMensagemBroadcast(apelido + " : " + mensagem, mensagem);
            }
        } catch (IOException e) {
            servidor.removerParticipante(this);
            logger.warning("Erro de E/S ao lidar com o participante. Removendo participante.");

        } finally {
            try {
                clienteSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
                logger.info("Erro ao fechar o socket do participante.");
            }
        }
    }

    public void enviarMensagem(String mensagem) {
        out.println(getTimestamp() + " " + mensagem);
    }

    private String getTimestamp() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return dateFormat.format(new Date());
    }

    public String getApelido() {
        return apelido;
    }
}
