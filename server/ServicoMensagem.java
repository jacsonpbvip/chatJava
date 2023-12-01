import java.io.OutputStream;
import java.net.Socket;

public class ServicoMensagem implements Runnable {
    private String apelido;
    private String mensagem;
    private Participante destinatario;

    public ServicoMensagem(String apelido, String mensagem, Participante destinatario) {
        this.apelido = apelido;
        this.mensagem = mensagem;
        this.destinatario = destinatario;
    }

    @Override
    public void run() {
        if (apelido != null && mensagem != null) {
            System.out.println(apelido + " : " + mensagem);
        } else {
            System.err.println("Erro: apelido ou mensagem é nulo");
        }
    }

    public void enviarMensagem(OutputStream outputStream, String string) {
    }

    public String receberMensagem(Socket socket) {
        return null;
    }

    public String getApelido() {
        return null;
    }

}
