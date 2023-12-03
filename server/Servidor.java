import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Servidor {
    private static final Logger logger = Logger.getLogger(Servidor.class.getName());
    private static final int PORTA = 50123;
    private List<Participante> participantes = new ArrayList<>();
    private ExecutorService fofoqueiro = Executors.newFixedThreadPool(10);
    private boolean servidorRodando = true;

    public static void main(String[] args) {
        new Servidor().iniciar();
    }

    public void iniciar() {
        try (ServerSocket servidorSocket = new ServerSocket(PORTA)) {
            logger.info("Servidor aguardando conexões na porta " + PORTA);
            while (servidorRodando) {
                Socket clienteSocket = servidorSocket.accept();
                Participante participante = new Participante(clienteSocket, this);

                // Verifica se o participante já existe na lista
                if (!participantes.contains(participante)) {
                    participantes.add(participante);
                    fofoqueiro.execute(participante);
                }
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Erro no servidor", e);
        }
    }

    public void enviarMensagemBroadcast(String mensagem, String apelido) {
        for (Participante participante : participantes) {
            try {
                participante.enviarMensagem(mensagem);
            } catch (Exception e) {
                // Lide com exceções de envio de mensagem
                logger.log(Level.WARNING, "Erro ao enviar mensagem para " + participante.getApelido(), e);
            }
        }

        // log do servidor com o nome do participante
        registrarMensagemNoLog(getTimestamp() + " " + apelido + ": " + mensagem);
    }

    public void removerParticipante(Participante participante) {
        participantes.remove(participante);
        enviarMensagemBroadcast("Servidor: " + participante.getApelido() + " saiu do chat.", null);
    }

    public void pararServidor() {
        servidorRodando = false;
        fofoqueiro.shutdownNow();
    }

    private void registrarMensagemNoLog(String mensagem) {
        // log do servidor
        logger.info(mensagem);
    }

    private String getTimestamp() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return dateFormat.format(new Date());
    }
}
