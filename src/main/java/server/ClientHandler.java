package server;

import lombok.extern.log4j.Log4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import services.FunkoServices;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicLong;


public class ClientHandler extends Thread {
    private final Logger logger = LoggerFactory.getLogger(ClientHandler.class);
    private final Socket clientSocket;
    private final AtomicLong counter = new AtomicLong(0);
    private final FunkoServices funkoServices;
    BufferedReader reader;
    BufferedWriter writer;

    public ClientHandler(Socket clientSocket, FunkoServices funkoServices) {
        this.clientSocket = clientSocket;
        this.funkoServices = funkoServices;
    }

    public void run() {
        try {
            openConnection();

            String clienteMessage;

            while (true) {
                clienteMessage = reader.readLine();
                logger.debug("Mensaje recibido del cliente: " + clienteMessage);
            }
        } catch (IOException e) {
            logger.error("Error de I/O en el cliente", e);
        }


    }
    private void openConnection() throws IOException {
        logger.debug("Abriendo conexión con el cliente");
        reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        writer = new BufferedWriter(new java.io.OutputStreamWriter(clientSocket.getOutputStream()));
    }
    private void closeConnection() throws IOException {
        logger.debug("Cerrando conexión con el cliente");
        reader.close();
        writer.close();
        clientSocket.close();
    }
}
