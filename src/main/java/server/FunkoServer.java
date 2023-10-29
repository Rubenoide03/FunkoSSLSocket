package server;
import javax.net.ssl.*;
import java.io.*;
import java.security.*;
import java.security.cert.CertificateException;

public class FunkoServer {

    public static void main(String[] args) {
        int port = 3000;

        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            char[] keystorePassword = "your_keystore_password".toCharArray();
            try (InputStream keystoreStream = new FileInputStream("your_keystore.jks")) {
                keyStore.load(keystoreStream, keystorePassword);
            }
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, keystorePassword);
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());

            SSLServerSocketFactory sslServerSocketFactory = sslContext.getServerSocketFactory();
            SSLServerSocket serverSocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket(port);

            while (true) {
                try (SSLSocket clientSocket = (SSLSocket) serverSocket.accept();
                     BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                     PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)) {




                    String request = reader.readLine();
                    if (request != null) {

                        String response = processRequest(request);
                        writer.println(response);
                    }
                }
            }
        } catch (IOException | NoSuchAlgorithmException | KeyStoreException | CertificateException |
                 UnrecoverableKeyException | KeyManagementException e) {
            e.printStackTrace();
        }
    }

    private static String processRequest(String request) {
        // Implementa la lógica para procesar la solicitud y devolver una respuesta.
        return "Response to the request: " + request;
    }
}
