# FunkoSSLSocket

Este proyecto es una implementación de un socket seguro utilizando el protocolo SSL/TLS y un certificado con sus claves correspondientes en JWT.
Gran parte del proyecto ya esta explicado en el repositorio de FunkoReactiv, por lo que aquí solo se explicará lo que es nuevo.

## ¿Cómo funciona?

El socket funciona de la siguiente manera:

1. Se crea un Secure Socket.
2. Se crea un contexto SSL.
3. Se extraen los datos de un archivo de configuración.
4. Se implementa en el protocolo SSL.
5. Se crea un socket SSL a partir del socket normal y el contexto SSL.
6. Se realiza el handshake.
7. Se envían y reciben datos.
8. Se cierra el socket SSL. 
```
package server;

import database.DatabaseManager;
import lombok.extern.log4j.Log4j;
import models.MyFunko;
import org.h2.tools.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import repositories.FunkoRepository;
import services.FunkoServices;
import utils.PropertiesReader;

import javax.net.ssl.*;
import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class FunkoServer {
    public static final String TOKEN_SECRET = "EstoEsElTokenSecret";
    public static final long TOKEN_EXPIRATION = 10000;
    private static final AtomicLong clientNumber = new AtomicLong(0);
    private static final Logger logger = LoggerFactory.getLogger(Server.class);
    private static final int PUERTO = 3000;
    private static final FunkoRepository funkoServices = FunkoServices.getInstance(DatabaseManager.getInstance());

    public static void main(String[] args) {
        try {
            var myConfig = readConfigFile();

            logger.debug("Configurando TSL");



            SSLServerSocketFactory serverFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
            SSLServerSocket serverSocket = (SSLServerSocket) serverFactory.createServerSocket(PUERTO);

            logger.debug("Protocolos soportados: " + Arrays.toString(serverSocket.getSupportedProtocols()));
            serverSocket.setEnabledCipherSuites(new String[]{"TLS_AES_128_GCM_SHA256"});
            serverSocket.setEnabledProtocols(new String[]{"TLSv1.3"});


            System.out.println("🚀 Servidor escuchando en el puerto 3000");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                logger.debug("Cliente conectado: " + clientSocket.getInetAddress().getHostAddress());
                ClientHandler clientHandler = new ClientHandler(clientSocket, (FunkoServices) funkoServices);
                clientHandler.start();
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    private   static Map<String, String> readConfigFile() {
     try {
        logger.debug("Leyendo el fichero de propiedades");
        PropertiesReader properties = new PropertiesReader("server.properties");

        String keyFile = properties.getProperty("keyFile");
        String keyPassword = properties.getProperty("keyPassword");
        String tokenSecret = properties.getProperty("tokenSecret");
        String tokenExpiration = properties.getProperty("tokenExpiration");

        if (keyFile.isEmpty() || keyPassword.isEmpty()) {
            throw new IllegalStateException("Hay errores al procesar el fichero de propiedades o una de ellas está vacía");
        }

        if (!Files.exists(Path.of(keyFile))) {
            throw new FileNotFoundException("No se encuentra el fichero de la clave");
        }

        Map<String, String> configMap = new HashMap<>();
        configMap.put("keyFile", keyFile);
        configMap.put("keyPassword", keyPassword);
        configMap.put("tokenSecret", tokenSecret);
        configMap.put("tokenExpiration", tokenExpiration);

        return configMap;
    } catch (FileNotFoundException e) {
        logger.error("Error en clave: " + e.getLocalizedMessage());

        return Collections.emptyMap();
    } catch (IOException e) {
        logger.error("Error al leer el fichero de propiedades: " + e.getLocalizedMessage());
        return null;
    }
}


    private static String processRequest(String request) {
        switch (request) {
            case "funkoMasCaro":
                return "Funko mas caro: " + FunkoServices.getInstance().funkoMasCaro().block();
            case "precioMedio":
                return "Precio medio: " + FunkoServices.getInstance().precioMedio().block();
            case "funkosPorModelo":
                return "Funkos por modelo: " + FunkoServices.getInstance().funkosPorModelo();
            case "numeroFunkosPorModelo":
                return "Numero de funkos por modelo: " + FunkoServices.getInstance().numerodeFunkosPorModelo();
            case "funkosLanzados2023":
                return "Funkos lanzados en 2023: " + FunkoServices.getInstance().funkosLanzados2023();
            case "funkosStitch":
                return "Lista de Stitches: " + FunkoServices.getInstance().funkosStitch();
            case "numeroFunkosStitch":
                return "Numero de Stitches: " + FunkoServices.getInstance().numeroFunkosStitch();
        }



        return "Response to the request: " + request;
    }
}

```
