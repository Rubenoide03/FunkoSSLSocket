package Main;

import controllers.FunkoController;
import database.DatabaseManager;
import models.ModeloF;
import models.MyFunko;
import repositories.FunkoRepository;
import services.FunkoServices;
import controllers.FunkoController;

import java.time.LocalDate;
import java.util.UUID;

public class Main {

    public static void main(String[] args) {
        FunkoController funkoProgram = FunkoController.getInstance();
        funkoProgram.init();
        FunkoRepository funkoServices = FunkoRepository.getInstance(DatabaseManager.getInstance());


        ;
        System.out.println("Obtenemos todos los funkos");
        System.out.println("Funkos: " + FunkoServices.findAll().collectList().block());
        FunkoServices.findAll().collectList().blockOptional().ifPresentOrElse(
                funkos -> System.out.println("Funkos: " + funkos),
                () -> System.out.println("No hay funkos")
        );
        MyFunko funko1 = MyFunko.builder().cod(UUID.randomUUID()).nombre("Funko1").modelo(ModeloF.DISNEY).precio(45.0).fecha(LocalDate.now()).build();
        MyFunko funko2 = MyFunko.builder().cod(UUID.randomUUID()).nombre("Funko2").modelo(ModeloF.OTROS).precio(70.0).fecha(LocalDate.now()).build();
        MyFunko funko3 = MyFunko.builder().cod(UUID.randomUUID()).nombre("Funko3").modelo(ModeloF.DISNEY).precio(45.0).fecha(LocalDate.now()).build();
        System.out.println("Obtenemos todos los alumnos");
        FunkoServices.findAll().collectList().subscribe(
                alumnos -> System.out.println("Alumnos: " + alumnos),
                error -> System.err.println("Error al obtener todos los alumnos: " + error.getMessage()),
                () -> System.out.println("Obtención de funkos completada")
        );

        System.out.println("Obtenemos el funko con nombre Funko1");
        FunkoServices.findByNombre("Funko1").subscribe(
                funko -> System.out.println("Funko: " + funko),
                error -> System.err.println("Error al obtener el funko: " + error.getMessage()),
                () -> System.out.println("Obtención de funko completada")
        );
        //Borramos todos los funkos
        System.out.printf("Borrando todos los funkos");



    }
}