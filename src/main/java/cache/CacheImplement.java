package cache;

import lombok.Getter;
import models.ModeloF;
import models.MyFunko;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.util.LinkedHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CacheImplement implements CacheInterface {

    private final Logger logger = LoggerFactory.getLogger(CacheImplement.class);

    private final int secondsToExpire = 60;
    private final int maxSize = 25;
    @Getter
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private final LinkedHashMap<Integer, MyFunko> cache = new LinkedHashMap<Integer, MyFunko>(maxSize, 0.75f, true) {
        protected boolean removeEldestEntry(java.util.Map.Entry<Integer, MyFunko> eldest) {
            return size() > maxSize;
        }
    };

    public CacheImplement(int secondsToExpire, int maxSize) {
        executorService.scheduleAtFixedRate(this::clear, 2, 50, TimeUnit.SECONDS);

    }

    public Mono<Void> put(Integer key, MyFunko value) {
        logger.debug("Poniendo en el cache funko con key:", key + "Funko", value);
        cache.put(key, value);
        return Mono.fromRunnable(() -> cache.put(key, value));
    }

    public Mono<MyFunko> get(Integer key) {
        logger.debug("Buscando key en el cache", key);
        return Mono.justOrEmpty(cache.get(key));


    }

    public Mono<Void> remove(Integer key) {
        logger.debug("Eliminando key del cache", key);
        return Mono.fromRunnable(() -> cache.remove(key));
    }

    public void clear() {
        logger.debug("Limpiando el cache");
        cache.clear();
    }
}

