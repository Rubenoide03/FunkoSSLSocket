package controllers;
import models.ModeloF;
import models.MyFunko;
import reactor.core.publisher.Flux;
import services.FunkoServices;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FunkoController {
    private static FunkoController instance;

    private FunkoController() {
        FunkoServices funkoServices = FunkoServices.getInstance();
    }

    public static FunkoController getInstance() {
        if (instance == null) {
            instance = new FunkoController();
        }
        return instance;
    }

    public void init(){
        printConsoleData();

    }


    public void printConsoleData(){
        FunkoServices funkoServices=FunkoServices.getInstance();
        System.out.println("Funko mas caro: ");
        funkoServices.funkoMasCaro().subscribe(System.out::println);
        System.out.println("Precio medio: ");
        funkoServices.precioMedio().subscribe(System.out::println);
        System.out.println("Funkos por modelo: ");
        funkoServices.funkosPorModelo().subscribe(System.out::println);
        System.out.println("Numero de funkos por modelo: ");
        funkoServices.numerodeFunkosPorModelo().subscribe(System.out::println);
        System.out.println("Funkos lanzados en 2023: ");
        funkoServices.funkosLanzados2023().subscribe(System.out::println);
        System.out.println("Lista de Stitches: ");
        funkoServices.funkosStitch().subscribe(System.out::println);
        System.out.println("Numero de Stitches ");
        funkoServices.numeroFunkosStitch().subscribe(System.out::println);

    }



}
