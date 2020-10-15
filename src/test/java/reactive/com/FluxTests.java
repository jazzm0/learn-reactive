package reactive.com;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

import static java.util.Arrays.asList;

public class FluxTests {

    @Test
    public void testFlux() {
        Flux<String> flux = Flux.just("A");
        flux.map(s -> "foo" + s)
                .subscribe(System.out::println);
    }

    @Test
    public void emptyFlux() {
        Flux<String> empty = Flux.empty();
        StepVerifier.create(empty).expectComplete()
                .verify();
    }

    @Test
    public void fluxWithTwoValues() {
        Flux<String> flux = Flux.just("foo", "bar");
        StepVerifier.create(flux)
                .expectNext("foo")
                .expectNext("bar")
                .expectComplete()
                .verify();
    }

    @Test
    public void fluxWithTwoValuesMap() {
        Flux<String> flux = Flux.just("foo", "bar").map(String::toUpperCase);
        StepVerifier.create(flux)
                .expectNext("FOO")
                .expectNext("BAR")
                .expectComplete()
                .verify();
    }

    @Test
    public void fluxFromIterable() {
        Set<String> s = new HashSet<>(asList("foo", "bar"));
        Flux<String> flux = Flux.fromIterable(s);
        StepVerifier.create(flux)
                .expectNext("bar")
                .expectNext("foo")
                .expectComplete()
                .verify();
    }

    @Test
    public void fluxWithDelay() {
        Flux<Integer> flux = Flux
                .fromStream(IntStream.iterate(0, i -> i + 1).limit(10).boxed())
                .delayElements(Duration.ofMillis(10));

        StepVerifier.create(flux)
                .expectNext(0)
                .expectNext(1)
                .expectNext(2)
                .expectNext(3)
                .expectNext(4)
                .expectNext(5)
                .expectNext(6)
                .expectNext(7)
                .expectNext(8)
                .expectNext(9)
                .expectComplete()
                .verify();
    }
}
