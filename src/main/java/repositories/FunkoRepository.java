package repositories;

import database.DatabaseManager;
import database.DatabaseService;
import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.spi.Connection;
import models.ModeloF;
import models.MyFunko;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.UUID;

public class FunkoRepository {
    private final DatabaseService databaseService = DatabaseService.getInstance();
    private static FunkoRepository instance;
    private static final int MAX_SIZE = 25;
     static Logger logger = LoggerFactory.getLogger(FunkoRepository.class);

    private static ConnectionPool connectionFactory;
    private final LinkedHashMap<Integer, MyFunko> cache = new LinkedHashMap<Integer, MyFunko>(MAX_SIZE, 0.75f, true) {
        protected boolean removeEldestEntry(java.util.Map.Entry<Integer, MyFunko> eldest) {
            return size() > MAX_SIZE;
        }
    };

    public FunkoRepository(DatabaseManager databaseManager) {
        connectionFactory = databaseManager.getConnectionPool();
    }


    public static synchronized FunkoRepository getInstance(DatabaseManager databaseManager) {
        if (instance == null) {
            instance = new FunkoRepository(DatabaseManager.getInstance());
        }
        return instance;
    }


    public static Flux<MyFunko> findAll() {
        logger.debug("Buscando todos los funkos");
        String sql = "SELECT * FROM FUNKOS";
        return Flux.usingWhen(
                connectionFactory.create(),
                connection -> Flux.from(connection.createStatement(sql).execute())
                        .flatMap(result -> result.map((row, rowMetadata) ->
                                MyFunko.builder()
                                        .cod(row.get("id", UUID.class))
                                        .nombre(row.get("nombre", String.class))
                                        .modelo(row.get("modelo", ModeloF.class))
                                        .precio(row.get("modelos", Double.class))
                                        .fecha(row.get("fecha", LocalDate.class))
                                        .created_at(row.get("created_at", LocalDateTime.class))
                                        .updated_at(row.get("updated_at", LocalDateTime.class))
                                        .build()
                        )),
                Connection::close
        );
    }

    public static Flux<MyFunko> findByNombre(String nombre) {
            logger.debug("Buscando todos los alumnos por nombre");
            String sql = "SELECT * FROM FUNKOS WHERE nombre LIKE ?";
            return Flux.usingWhen(
                    connectionFactory.create(),
                    connection -> Flux.from(connection.createStatement(sql)
                            .bind(0, "%" + nombre + "%")
                            .execute()
                    ).flatMap(result -> result.map((row, rowMetadata) ->
        MyFunko.builder()
                .cod(row.get("id", UUID.class))
                .nombre(row.get("nombre", String.class))
                .modelo(row.get("modelo", ModeloF.class))
                .precio(row.get("precio", Double.class))
                .fecha(row.get("fecha", LocalDate.class))
                .created_at(row.get("created_at", LocalDateTime.class))
                .updated_at(row.get("updated_at", LocalDateTime.class))
                .build()
        )),
        Connection::close);
    }

    public Mono<MyFunko> save(MyFunko funko){
        logger.debug("Guardando alumno"+funko);
        String sql="INSERT INTO FUNKOS (cod,nombre,precio,modelo,fecha,created_at,updated_at) VALUES (?,?,?,?,?,?,?)";
        return Mono.usingWhen(
                connectionFactory.create(),
                connection -> Mono.from(connection.createStatement(sql)
                        .bind(0, funko.cod())
                        .bind(1,funko.nombre())
                        .bind (2, funko.modelo())
                        .bind (3, funko.precio())
                        .bind (4, funko.fecha())
                        .bind (5, funko.created_at())
                        .bind (6, funko.updated_at())
                        .execute()
        ).then(Mono.just(funko)),
        Connection::close
    );
    }

    public Mono<Void> deleteAll() {
        logger.debug("Borrando todos los funkos");
        String sql = "DELETE FROM FUNKOS";
        return Mono.usingWhen(
                connectionFactory.create(),
                connection -> Mono.from(connection.createStatement(sql)
                        .execute()
                ).then(),
                Connection::close
        );
    }





}
