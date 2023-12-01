import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
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

    public static void main(String[] args) {
        new Servidor().iniciar();
    }

    public void iniciar() {
        try (ServerSocket servidorSocket = new ServerSocket(PORTA)) {
            logger.info("Servidor aguardando conexões na porta " + PORTA);
            while (true) {
                Socket clienteSocket = servidorSocket.accept();
                Participante participante = new Participante(clienteSocket, this);
                participantes.add(participante);
                fofoqueiro.execute(participante);
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Erro no servidor", e);
        }
    }

    public void enviarMensagemBroadcast(String mensagem) {
        for (Participante participante : participantes) {
            fofoqueiro.execute(() -> participante.enviarMensagem(mensagem));
        }
    }

    public void removerParticipante(Participante participante) {
        participantes.remove(participante);
        enviarMensagemBroadcast("Servidor: " + participante.getApelido() + " saiu do chat.");
    }
}
