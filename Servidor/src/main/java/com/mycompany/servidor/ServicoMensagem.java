package com.mycompany.servidor;

import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServicoMensagem implements Runnable {
    private String apelido;
    private String mensagem;
    private Participante destinatario;
    private static final Logger logger = Logger.getLogger(ServicoMensagem.class.getName());

    public ServicoMensagem(String apelido, String mensagem, Participante destinatario) {
        this.apelido = apelido;
        this.mensagem = mensagem;
        this.destinatario = destinatario;
    }

    @Override
    public void run() {
        if (apelido != null && mensagem != null) {
            logger.log(Level.FINE, "{0} : {1}", new Object[] { apelido, mensagem });
        } else {
            logger.log(Level.SEVERE, "Erro: apelido ou mensagem é nulo");
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
