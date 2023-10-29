package database;

import io.r2dbc.spi.Connection;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseService {
    private static DatabaseService controller;
    private  final Logger logger = LoggerFactory.getLogger(DatabaseService.class);
    public static DatabaseService getInstance() {
        if (controller == null) {
            controller = new DatabaseService();
        }
        return controller;
    }




    ConnectionFactoryOptions options = ConnectionFactoryOptions.builder()
            .option(ConnectionFactoryOptions.DRIVER, "h2")
            .option(ConnectionFactoryOptions.PROTOCOL, "tcp")
            .option(ConnectionFactoryOptions.HOST, "localhost")
            .option(ConnectionFactoryOptions.PORT, 3306)
            .option(ConnectionFactoryOptions.USER, "ruben")
            .option(ConnectionFactoryOptions.PASSWORD, "1234")
            .option(ConnectionFactoryOptions.DATABASE, "funkos")
            .build();


    ConnectionFactory connectionFactory = ConnectionFactories.get(options);

    public Publisher<? extends Connection> getConnection() {
        return connectionFactory.create();
    }









}




