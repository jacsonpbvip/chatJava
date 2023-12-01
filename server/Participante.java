import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Participante implements Runnable {
    private Socket clienteSocket;
    private Servidor servidor;
    private PrintWriter out;
    private String apelido;

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
            servidor.enviarMensagemBroadcast("Servidor: " + apelido + " entrou no chat.");

            String mensagem;
            while ((mensagem = in.readLine()) != null) {
                if ("##sair##".equals(mensagem)) {
                    break;
                }
                servidor.enviarMensagemBroadcast(apelido + " : " + mensagem);
            }
        } catch (IOException e) {
            servidor.removerParticipante(this);
        } finally {
            try {
                clienteSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
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
