package database;

import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;
import io.r2dbc.spi.Connection;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.Statement;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.*;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;


public class DatabaseManager {

    private static DatabaseManager instance;
    private final Logger logger = LoggerFactory.getLogger(DatabaseManager.class);
    String url = "";
    boolean initTables = false;

    private final List<String> initScripts = List.of("Data.sql");
    private final ConnectionFactory connectionFactory;
    @Getter
    private final ConnectionPool connectionPool;


    private DatabaseManager() {

        loadProperties();
        connectionFactory = ConnectionFactories.get(url);
        ConnectionPoolConfiguration connectionPoolConfiguration = ConnectionPoolConfiguration.builder(connectionFactory).maxIdleTime(java.time.Duration.ofMinutes(4)).maxSize(10).build();
        connectionPool = new ConnectionPool(connectionPoolConfiguration);
        if (initTables) {
            initTables().block();
        }
    }

    public void loadProperties() {
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream(ClassLoader.getSystemResource("database.properties").getFile()));
            url = properties.getProperty("db.stringDB");
            initTables = Boolean.parseBoolean(properties.getProperty("db.loadTables", "true"));
        } catch (Exception e) {
            logger.error("Error al cargar el fichero de propiedades ", e);
        }

    }

    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }


    public Mono<Void> initTables() {
        return Flux.fromIterable(initScripts)
                .flatMap(sc -> Mono.usingWhen(
                        connectionFactory.create(),
                        connection -> {
                            String content;
                            try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(sc)) {
                                if (inputStream == null) {
                                    return Mono.error(new IOException("No se ha encontrado el fichero de script de inicialización de la base de datos"));
                                }
                                try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                                    content = reader.lines().collect(Collectors.joining("\n"));
                                }
                            } catch (IOException e) {
                                return Mono.error(e);
                            }

                            Statement statement = connection.createStatement(content);
                            return Mono.from(statement.execute());
                        },
                        Connection::close
                ))
                .then()
                .doOnError((res) -> logger.info("Se han ejecutado todos los scripts"));
    }


}
