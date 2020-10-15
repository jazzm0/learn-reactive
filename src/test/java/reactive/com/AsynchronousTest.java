package reactive.com;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Instant;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class AsynchronousTest {

    public static class TimeStampStream implements Iterator<Instant>, Supplier<Stream<? extends Instant>> {

        private int count = 0;
        private final int maxCount;

        public TimeStampStream(int maxCount) {
            this.maxCount = maxCount;
        }

        @Override
        public boolean hasNext() {
            return count < maxCount;
        }

        @Override
        public Instant next() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            count++;
            return Instant.now();
        }

        @Override
        public Stream<? extends Instant> get() {
            return StreamSupport.stream(
                    Spliterators.spliteratorUnknownSize(this, Spliterator.ORDERED), true);
        }
    }

    @Test
    public void flatMapTest() {
        Flux.fromStream(() -> new TimeStampStream(10).get())
//                .log()
                .flatMap(
                        instant -> Mono.just(instant).subscribeOn(Schedulers.parallel()), 10)
                .parallel(10)
                .subscribe(System.out::println);
    }

    @Test
    public void flatMapTestOther() {
        Flux.fromStream(() -> new TimeStampStream(10).get())

//                .log()
                .flatMap(value ->
                        Mono.just(value)
                                .subscribeOn(Schedulers.parallel()), 2)
                .parallel(2)
                .subscribe(System.out::println);
    }

    @Test
    public void mapTest() {
        Flux.just("red", "white", "blue")
                .parallel(3)
                .runOn(Schedulers.parallel())
                .map(value -> {
                    String result = value.toUpperCase();
                    System.out.println(Thread.currentThread() + " - " + result);
                    return result;
                })
                .subscribe();
    }

    @Test
    public void paralellTest(){
        Flux.range(1, 10)
                .parallel(2)
                .runOn(Schedulers.parallel())
                .map(i -> i + 1)
                .map(i -> i * 2)
                .map(i -> i + 1)
                .subscribe(System.out::println);
    }
}
