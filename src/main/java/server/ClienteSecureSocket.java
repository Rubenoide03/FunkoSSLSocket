package server;
import javax.net.ssl.*;
import java.io.*;
import java.security.KeyStore;

public class ClienteSecureSocket {
    public static void main(String[] args) {
        String host = "localhost";
        int port = 13579;
        String clienteKeystorePath ="src/cert/clienteKeys.jks";
        String keystorePassword ="123456";

        try{
            SSLContext sslContext= SSLContext.getInstance("TSL");
            TrustManagerFactory trustManagerFactory= TrustManagerFactory.getInstance("SunX509");
            KeyStore keyStoreCli=KeyStore.getInstance("JKS");

            FileInputStream fileInputStream=new FileInputStream(clienteKeystorePath);
            keyStoreCli.load(fileInputStream,keystorePassword.toCharArray());
            fileInputStream.close();

            trustManagerFactory.init(keyStoreCli);
            sslContext.init(null,trustManagerFactory.getTrustManagers(),null);

            SSLSocketFactory sslSocketFactory= sslContext.getSocketFactory();
            SSLSocket sslSocket= (SSLSocket) sslSocketFactory.createSocket(host,port);

            PrintWriter pw=new PrintWriter(sslSocket.getOutputStream(),true);
            pw.println("Hola, si aparece esto, la conexion funciona");

            BufferedReader reader=new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));
            String response= reader.readLine();
            System.out.println("Hola mi mensaje es el siguiente"+response);

            pw.close();
            reader.close();
            sslSocket.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
