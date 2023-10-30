import models.ModeloF;
import models.MyFunko;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import services.FunkoServices;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;


import static org.junit.jupiter.api.Assertions.*;

class FunkoServicesTest {
    private FunkoServices funkoServices;

    private List<MyFunko> funkos;


    @BeforeEach
    void init() {
        funkoServices = FunkoServices.getInstance();
        funkos = new ArrayList<>();


    }

    @Test
    void funkoMasCaro() {
        MyFunko funkoMasCaroO = new MyFunko(UUID.randomUUID(), "FunkoName", ModeloF.ANIME, 100.0, LocalDate.now(), LocalDateTime.now(), LocalDateTime.now());
        funkos.add(funkoMasCaroO);
        funkos.add(new MyFunko(UUID.randomUUID(), "Funko1", ModeloF.DISNEY, 45.0, LocalDate.now(), LocalDateTime.now(), LocalDateTime.now()));
        funkos.add(new MyFunko(UUID.randomUUID(), "Funko2", ModeloF.OTROS, 70.0, LocalDate.now(), LocalDateTime.now(), LocalDateTime.now()));

        FunkoServices funkoServicesMock = spy(funkoServices);
        when(funkoServicesMock.funkoMasCaro()).thenReturn(Mono.just(funkoMasCaroO));

        assertEquals(funkoMasCaroO, funkoServicesMock.funkoMasCaro().block());


    }

    @Test
    void precioMedio() {
        funkos.add(new MyFunko(UUID.randomUUID(), "Funko1", ModeloF.DISNEY, 45.0, LocalDate.now(), null, null));
        funkos.add(new MyFunko(UUID.randomUUID(), "Funko2", ModeloF.OTROS, 70.0, LocalDate.now(), null, null));

        FunkoServices funkoServicesMock = spy(FunkoServices.getInstance());
        when(funkoServicesMock.precioMedio()).thenReturn(Mono.just(57.5));
        double expectedAverage = 57.5;
        assertEquals(expectedAverage, funkoServicesMock.precioMedio().block());

    }

    @Test
    void funkosPorModelo() {
        funkos.add(new MyFunko(UUID.randomUUID(), "Funko1", ModeloF.DISNEY, 45.0, LocalDate.now(), null, null));
        funkos.add(new MyFunko(UUID.randomUUID(), "Funko2", ModeloF.OTROS, 70.0, LocalDate.now(), null, null));
        FunkoServices funkoServicesMock = spy(FunkoServices.getInstance());
        when(funkoServicesMock.funkosPorModelo()).thenReturn(Flux.just(funkos.stream().collect(Collectors.groupingBy(MyFunko::modelo))));
        assertEquals(funkos.stream().collect(Collectors.groupingBy(MyFunko::modelo)), funkoServicesMock.funkosPorModelo().blockFirst());

    }

    @Test
    void numerodeFunkosPorModelo() {
        funkos.add(new MyFunko(UUID.randomUUID(), "Funko1", ModeloF.DISNEY, 45.0, LocalDate.now(), null, null));
        funkos.add(new MyFunko(UUID.randomUUID(), "Funko2", ModeloF.OTROS, 70.0, LocalDate.now(), null, null));
        FunkoServices funkoServicesMock = spy(FunkoServices.getInstance());
        when(funkoServicesMock.numerodeFunkosPorModelo()).thenReturn(Flux.just(funkos.stream().collect(Collectors.groupingBy(MyFunko::modelo, Collectors.counting()))));
        assertEquals(funkos.stream().collect(Collectors.groupingBy(MyFunko::modelo, Collectors.counting())), funkoServicesMock.numerodeFunkosPorModelo().blockFirst());

    }

    @Test
    void funkosLanzados2023() {
        funkos.add(new MyFunko(UUID.randomUUID(), "Funko1", ModeloF.DISNEY, 45.0, LocalDate.now(), null, null));
        funkos.add(new MyFunko(UUID.randomUUID(), "Funko2", ModeloF.OTROS, 70.0, LocalDate.now(), null, null));

        FunkoServices funkoServicesMock = spy(FunkoServices.getInstance());
        when(funkoServicesMock.funkosLanzados2023()).thenReturn(Flux.just(funkos.stream().filter(myFunko -> myFunko.fecha().getYear() == 2023).toList()));
        assertEquals(funkos.stream().filter(myFunko -> myFunko.fecha().getYear() == 2023).toList(), funkoServicesMock.funkosLanzados2023().blockFirst());

    }
    @Test
    void listFunkosStitch(){
        funkos.add(new MyFunko(UUID.randomUUID(), "Stitch", ModeloF.DISNEY, 45.0, LocalDate.now(), LocalDateTime.now(), LocalDateTime.now()));
        FunkoServices funkoServicesMock = spy(FunkoServices.getInstance());
        when(funkoServicesMock.funkosStitch()).thenReturn(Flux.just(funkos.stream().filter(myFunko -> myFunko.nombre().contains("Stitch")).toList()));


    }


    @Test
    void readAllCSV() {
        String rutaArchivoCSV = "src/main/resources/funkos.csv";
        FunkoServices funkoServicesMock = mock(FunkoServices.class);
        when(funkoServicesMock.readAllCSV(rutaArchivoCSV)).thenCallRealMethod();
        Flux<MyFunko> funkos = funkoServicesMock.readAllCSV(rutaArchivoCSV);
        List<MyFunko> funkoList = funkos.collectList().block();
        assertEquals(90, funkoList.size());
    }
}
