package cache;
import reactor.core.publisher.Mono;

public interface CacheInterface {



    public interface CacheFunkos<UUID, Funko> {
        Mono<Void> put(UUID key, Funko value);

        Mono<Funko> get(UUID key);

        Mono<Void> remove(UUID key);

        void clear();

        void close();
    }
}
