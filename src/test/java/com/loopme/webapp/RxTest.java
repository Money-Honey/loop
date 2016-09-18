package com.loopme.webapp;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Ignore;
import org.junit.Test;
import rx.*;
import rx.Observable;
import rx.Observer;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.lang.System.out;
import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;
import static java.util.concurrent.TimeUnit.*;
import static rx.Observable.just;

/**
 * @author <a href="mailto:dema.luxoft@gmail.com">Volodymyr Dema</a>
 */
@Ignore
public class RxTest {

    @Test
    public void rxShouldWork() {

        Observable<String> just = just("Hello Async");

        Observable<String> helloObservable = Observable.<String>create(o -> {
            o.onNext("Hello1");

            o.onNext("Hello2");
            o.onCompleted();
        });

        Observable.merge(just, helloObservable).subscribe(out::println);
    }

    @Test
    public void single() {

        just("1");
        Observable.from(Lists.newArrayList(1, 2, 3));
        Observable.range(5, 6);
        Observable.empty();
        Observable.never();
        Observable.error(new IllegalFormatCodePointException(1));

    }

    @Test
    public void myself() {
//        Observable.create( s -> {}).subscribe();
        Observable.create(Observer::onCompleted).subscribe();

        Observable.create(s -> {
            int from = 5;
            int count = 2;
            for (int i = from; i < from + count; i++) {
                s.onNext(i);
            }
            s.onCompleted();
        }).subscribe(v -> out.println(v));

    }

    @Test
    public void twice() {
        Observable<Integer> ints =
                Observable.<Integer>create(subscriber -> {
                            log("Create");
                            subscriber.onNext(42);
                            subscriber.onCompleted();
                        }
                ).cache();

        log("Starting");
        ints.subscribe(i -> log("Element A: " + i));
        ints.subscribe(i -> log("Element B: " + i));
        log("Exit");
    }

    @Test
    @Ignore
    public void broken() {
        Observable<BigInteger> naturalNumbers = Observable.create(
                subscriber -> {
                    BigInteger i = ZERO;
                    while (true) {  //don't do this!
                        subscriber.onNext(i);
                        i = i.add(ONE);
                    }
                });
        naturalNumbers.subscribe(x -> log(x));
    }

    @Test
    public void withThread() throws InterruptedException {
        Observable<BigInteger> naturalNumbers = Observable.create(
                subscriber -> {
                    Runnable r = () -> {
                        BigInteger i = ZERO;
                        while (!subscriber.isUnsubscribed()) {
                            subscriber.onNext(i);
                            i = i.add(ONE);
                        }
                    };
                    new Thread(r).start();
                });

        Subscription subscription = naturalNumbers.subscribe(x -> log(x));

        SECONDS.sleep(1);
        subscription.unsubscribe();
    }

    @Test
    public void delayed() {
        Observable.create(
                subscriber -> {
                    Runnable r = () -> {
                        sleep(10, SECONDS);
                        if (!subscriber.isUnsubscribed()) {
                            subscriber.onNext(1);
                            subscriber.onCompleted();
                        }
                    };
                    new Thread(r).start();
                });
    }

    @Test
    public void delayedWithInterruptSubscription() {
        Observable<Object> objectObservable = Observable.create(
                subscriber -> {
                    Runnable r = () -> {
                        sleep(1, SECONDS);
                        if (!subscriber.isUnsubscribed()) {
                            subscriber.onNext(1);
                            subscriber.onCompleted();
                        }
                    };
                    final Thread thread = new Thread(r);
                    thread.start();
                    subscriber.add(Subscriptions.create(new Action0() {
                        @Override
                        public void call() {
                            thread.interrupt();
                        }
                    }));
                });

        objectObservable.subscribe(out::println);
        sleep(2, SECONDS);
        objectObservable.subscribe(out::println);
    }

    static void sleep(int timeout, TimeUnit unit) {
        try {
            unit.sleep(timeout);
        } catch (InterruptedException ignored) {
            //intentionally ignored
        }
    }

    @Test
    public void create() {

        log("Before");
        Observable
                .range(5, 3)
                .subscribe(i -> {
                    log(i);
                });
        log("After");
    }

    @Test
    public void exception() {

        Observable.create(subscriber -> {
            subscriber.onNext(load());
            subscriber.onCompleted();

        }).subscribe(v -> out.println(v), e -> out.println(e.getMessage()));
    }

    @Test
    public void timer() {
        Observable
                .timer(1, SECONDS)
                .subscribe((Long zero) -> log(zero));

        sleep(2, SECONDS);
    }

    @Test
    public void interval() {
        Observable
                .interval(1_000_000 / 60, MICROSECONDS)
                .subscribe((Long i) -> log(i));

        sleep(2, SECONDS);
    }

    private Object load() {
        throw new IllegalAccessError("1");
    }

    @Test
    public void eaaper() {

        Observable<Integer> ints = Observable
                .create(new Observable.OnSubscribe<Integer>() {
                    @Override
                    public void call(Subscriber<? super Integer> subscriber) {
                        log("Create");
                        subscriber.onNext(5);
                        subscriber.onNext(6);
                        subscriber.onNext(7);
                        subscriber.onCompleted();
                        log("Completed");
                    }
                });
        log("Starting");
        ints.subscribe(i -> log("Element: " + i));
        log("Exit");
    }

    @Test
    public void twitterShouldWork() {
        TwitterSubject twitterSubject = new TwitterSubject();
        IntStream.range(0, 10000).forEach(intNumber -> {
            twitterSubject.observe()
                    .subscribe(fromStreamValue -> log(fromStreamValue));
        });

        sleep(15, SECONDS);
    }

    @Test
    public void publishRefCount() {
        Observable<Integer> observable = Observable.create(subscriber -> {
            log("Establishing connection");

            subscriber.add(Subscriptions.create(() -> {
                log("Disconnecting");
            }));
        });

        Observable<Integer> lazy = observable.publish().refCount();

        log("Before subscribers");
        Subscription sub1 = lazy.subscribe();
        log("Subscribed 1");
        Subscription sub2 = lazy.subscribe();
        log("Subscribed 2");
        sub1.unsubscribe();
        log("Unsubscribed 1");
        sub2.unsubscribe();
        log("Unsubscribed 2");
    }

    @Test
    public void timerFlatMap() {
//        Observable<Integer> integerObservable = Observable.empty()
//                .timer(1, TimeUnit.SECONDS)
//                .flatMap(i -> {
//                    log("call flatMap");
//                    return Observable.just(1, 2, 3);
//                });
        just(1, 2, 3)
                .delay(1, TimeUnit.SECONDS)
                .subscribe((v) -> log(v));

        sleep(2, SECONDS);
    }

    @Test
    public void customDelay() throws InterruptedException {
//        Observable
//                .just("Lorem", "ipsum", "dolor", "sit", "amet",
//                        "consectetur", "adipiscing", "elit")
//                .delay(word -> Observable.timer(word.length(), SECONDS))
//                .subscribe(v -> log(v));

        just("Lorem", "ipsum", "dolor", "sit", "amet",
                "consectetur", "adipiscing", "elit")
                .flatMap(word ->
                        Observable.timer(word.length(), SECONDS).map(x -> word))
                .subscribe(v -> log(v));

        TimeUnit.SECONDS.sleep(15);
    }

    @Test
    public void combineLates() {
        Observable.combineLatest(
                Observable.interval(17, MILLISECONDS).map(x -> "S" + x),
                Observable.interval(10, MILLISECONDS).map(x -> "F" + x),
                (s, f) -> f + ":" + s
        ).forEach(out::println);

        sleep(10, SECONDS);
    }

    @Test
    public void withLatestFrom() {
        Observable<String> fast = Observable.interval(10, MILLISECONDS)
                .map(x -> "F" + x)
                .delay(100, MILLISECONDS)
                .startWith("FX");
        Observable<String> slow = Observable.interval(17, MILLISECONDS).map(x -> "S" + x);

        Observable.combineLatest(slow, fast, (s, f) -> s + ":" + f)
                .forEach(out::println);

        sleep(3, SECONDS);
    }

    @Test
    public void amb() {
        Observable.amb(
                stream(100, 17, "S"),
                stream(200, 10, "F")
        ).subscribe(v -> log(v));

        sleep(3, SECONDS);
    }

    Observable<String> stream(int initialDelay, int interval, String name) {
        return Observable.merge(Observable.<String>empty().delay(initialDelay, MILLISECONDS),
                Observable
                        .interval(interval, MILLISECONDS)
                        .map(x -> name + x)
                        .doOnSubscribe(() ->
                                log("Subscribe to " + name))
                        .doOnUnsubscribe(() ->
                                log(("Unsubscribe from " + name))));
    }

    @Test
    public void genericTest() {
        List integers = Lists.newArrayList(1, 2, 3);
        List<Number> numbers = integers;
        numbers.forEach(v -> log(v));
    }

    @Test
    public void scanMethod() {
        Observable<BigInteger> factorials = Observable
                .range(2, 10)
                .scan(BigInteger.ONE, (big, cur) ->
                        big.multiply(BigInteger.valueOf(cur)));

        factorials.subscribe(v -> log(v));

        sleep(1, SECONDS);
    }

    @Test
    public void reduceTest() {
        Observable<Integer> transfers = Observable.range(1, 100);

        transfers.doOnNext((v) -> log(v))
                .reduce(0, (total, cur) -> total += cur)
                .subscribe(v -> log("total = " + v));

        sleep(1, SECONDS);
    }

    @Test
    public void accumulateToList() {
        Observable<ArrayList> reduce = Observable
                .range(10, 20)
                .reduce(new ArrayList<>(), (list, currentValue) -> {
                    list.add(currentValue);
                    return list;
                });
        Observable<ArrayList> collect = Observable
                .range(10, 20)
                .collect(ArrayList::new, (list, cur) -> list.add(cur));

        reduce.subscribe(result -> log(result));
        collect.subscribe(result -> log(result));

        sleep(1, SECONDS);
    }

    @Test
    public void collectStringBuffer() {
        Observable
                .range(1, 10)
                .collect(StringBuffer::new,
                        (buffer, curValue) -> buffer.append(curValue).append(", "))
                .map(StringBuffer::toString)
                .subscribe(v -> log(v));


        sleep(1, SECONDS);
    }

    @Test
    public void distinctTest() {
        Observable<Object> random = Observable.create(subscriber -> {
            Random r = new Random();
            while (!subscriber.isUnsubscribed()) {
                subscriber.onNext(r.nextInt(1000));
            }
        });

        random
                .distinct()
                .take(1000)
                .subscribe(v -> log(v));

        sleep(1, SECONDS);
    }

    @Test
    public void takeTest() {
        Observable
                .range(1, 10)
                .take(5)
                .subscribe(v -> log(v));

        sleep(1, SECONDS);
    }

    @Test
    public void skipTest() {
        Observable
                .range(1, 10)
                .skip(5)
                .subscribe(v -> log(v));

        sleep(1, SECONDS);
    }

    @Test
    public void takeLastTest() {
        Observable
                .range(1, 10)
                .takeWhile(v -> v < 5)
//                .single()
                .subscribe(v -> log(v));

        sleep(1, SECONDS);
    }

    @Test
    public void skipLastTest() {
        Observable
                .range(1, 10)
                .skipLast(2)
                .subscribe(v -> log(v));

        sleep(1, SECONDS);
    }

    @Test
    public void firstTest() {
        Observable
                .range(1, 10)
                .first(v -> v % 2 == 0)
                .single()
                .subscribe(v -> log(v));

        sleep(1, SECONDS);
    }

    @Test
    public void elementAtTest() {
        Observable
                .interval(10, MILLISECONDS)
                .map(v -> "#" + v++)
                .elementAt(5)
                .single()
                .subscribe(v -> log(v));

        sleep(1, SECONDS);
    }

    @Test
    public void orDefaultTest() {
        Observable
                .range(1, 10)
                .map(v -> "#" + v)
                .elementAtOrDefault(9, "123")
                .single()
                .subscribe(v -> log(v));

        sleep(1, SECONDS);
    }

    class MapOperator<T, R> implements Observable.Operator<R, T> {

        final Func1<T, R> transformer;

        MapOperator(Func1<T, R> transformer) {
            this.transformer = transformer;
        }

        @Override
        public Subscriber<? super T> call(Subscriber<? super R> child) {
            return new Subscriber<T>(child) {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(T t) {
                    child.onNext(transformer.call(t));
                }
            };
        }
    }

    Observable<String> loudlySpeak(String speech) {
        String[] words = speech.split(" ");

        Observable<Integer> delays = Observable
                .from(words)
//                .map(String::length)
                .lift(new MapOperator<String, Integer>((w) -> w.length() * 100))
                .scan((total, next) -> total + next);

        return Observable.from(words)
                .zipWith(delays.startWith(0), Pair::of)
                .flatMap(pair -> just(pair.getLeft()).delay(pair.getRight(), MILLISECONDS));
    }


    @Test
    public void speakMyImplementationTest() {
        Observable<String> anton = loudlySpeak(
                "Hello my dear college. I'm very appriciate you as good employee.");
        Observable<String> vova = loudlySpeak(
                "You are very butyfull girl - my Dasha.");
        Observable<String> dima = loudlySpeak(
                "I like gym. I need more food and sleep be more strong");

        Observable
                .concat(
                        anton.map(s -> "Anton: " + s),
                        vova.map(s -> "Vova: " + s),
                        dima.map(s -> "Dima: " + s)
                )
                .subscribe(out::println);

        sleep(10, SECONDS);
    }

    @Test
    public void concatTest() {
        Observable<String> empty = Observable.empty();
        Observable<String> notEmpty = Observable
                .range(1, 3)
                .map(Object::toString);

        Observable.concat(
                empty,
                notEmpty
        )
                .first()
                .subscribe(v -> log(v));

        sleep(1, SECONDS);
    }

    Observable<String> speak(String quote, long millisPerChar) {
        String[] tokens = quote.replaceAll("[:,]", "").split(" ");
        Observable<String> words = Observable.from(tokens);
        Observable<Long> absoluteDelay = words
                .map(String::length)
                .map(len -> len * millisPerChar)
                .scan((total, current) -> total + current);

        return words
                .zipWith(absoluteDelay.startWith(0L), Pair::of)
                .flatMap(pair -> just(pair.getLeft())
                        .delay(pair.getRight(), MILLISECONDS));
    }

    @Test
    public void speakTest() {
        Observable<String> alice = speak(
                "To be, or not to be: that is the question", 110);
        Observable<String> bob = speak(
                "Though this be madness, yet there is method in't", 90);
        Observable<String> jane = speak(
                "There are more things in Heaven and Earth, " +
                        "Horatio, than are dreamt of in your philosophy", 100);

        Observable
                .concat(
                        alice.map(w -> "Alice: " + w),
                        bob.map(w -> "Bob:   " + w),
                        jane.map(w -> "Jane:  " + w)

                )
                .subscribe(RxTest::log);

        sleep(10, SECONDS);
    }

    @Test
    public void switchOnNext() {
        Random rnd = new Random();
        Observable<String> alice = speak(
                "To be, or not to be: that is the question", 110);
        Observable<String> bob = speak(
                "Though this be madness, yet there is method in't", 90);
        Observable<String> jane = speak(
                "There are more things in Heaven and Earth, " +
                        "Horatio, than are dreamt of in your philosophy", 100);

        Observable<Observable<String>> quotes = just(
                alice.map(w -> "Alice: " + w),
                bob.map(w -> "Bob:   " + w),
                jane.map(w -> "Jane:  " + w))
                .flatMap(innerObs -> just(innerObs)
                        .delay(rnd.nextInt(5), SECONDS));

        Observable
                .switchOnNext(quotes)
                .subscribe(out::println);

        sleep(10, SECONDS);

    }

    @Test
    public void toStringOdd() {
        Observable
                .range(1, 9)
                .buffer(1, 2)
                .flatMap(Observable::from)
                .map(Object::toString);
    }

    @Test
    public void ownOperator() {
        Observable<String> odd = Observable
                .range(1, 9)
                .lift(toStringOfOdd());
    }

    private <T> Observable.Operator<String, T> toStringOfOdd() {
        return new Observable.Operator<String, T>() {
            private boolean odd = true;

            @Override
            public Subscriber<? super T> call(Subscriber<? super String> child) {
                return new Subscriber<T>(child) {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(T t) {
                        if (odd) {
                            child.onNext(t.toString());
                        } else {
                            request(1);
                        }
                        odd = !odd;
                    }
                };
            }
        };
    }

    @Test
    public void distinctChanged() {
        Observable
                .interval(10, TimeUnit.MILLISECONDS)
                .map(x -> x % 2 == 0 ? x : x - 1)
                .distinctUntilChanged()
                .take(10)
                .subscribe(v -> log(v));

        sleep(1, SECONDS);
    }

    @Test
    public void cf() throws ExecutionException, InterruptedException {
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> "");
        completableFuture.get();

    }

    List<String> ids = new ArrayList<String>() {{
        add("1");
        add("2");
        add("3");
        add("4");
        add("5");
        add("6");
        add("7");
        add("8");
        add("9");
        add("10");
    }};

    static AtomicBoolean isExecuted = new AtomicBoolean(false);

    @Test
    public void testAtomicBoolean() {
        if (isExecuted.compareAndSet(false, true)) {
            log("Execute something");

            isExecuted.set(false);
        }
    }

    @Test
    public void nonBlockingTest() {
        List<List<String>> batches = Lists.newArrayList(Iterables.partition(ids, 1));


        Scheduler scheduler = createScheduler();

        Observable.from(batches)
                .doOnNext(batch -> log("Before flat map"))
                .flatMap(batch ->
                        loadAndWrap(batch)
                                .doOnNext(wrapped -> log("load and wrap"))
                                .subscribeOn(scheduler))
                .doOnNext(wrapped -> log("after flat map"))
                .reduce((l1, l2) ->
                        Stream.concat(l1.stream(), l2.stream()).collect(Collectors.toList()))
                .single()
                .subscribe(this::writeToHdfs, error -> log("Error: " + error));

        sleep(6, SECONDS);
        log("The End");
    }

    private Scheduler createScheduler() {
        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat("MyPool-%d")
                .build();
        Executor executor = new ThreadPoolExecutor(
                10,  //corePoolSize
                10,  //maximumPoolSize
                0L, TimeUnit.MILLISECONDS, //keepAliveTime, unit
                new LinkedBlockingQueue<>(200),  //workQueue
                threadFactory
        );
        return Schedulers.from(executor);
    }

    private void writeToHdfs(List<Wrapper> collection) {
        log("The Result: " + Arrays.toString(collection.toArray()));
    }

    Observable<List<Wrapper>> loadAndWrap(List<String> ids) {
        return Observable.defer(() ->
                        Observable.create(s -> {
                            int timeout = new Random().nextInt(5);
                            sleep(timeout, SECONDS);

//                            if (timeout == 3) {
//                                s.onError(new RuntimeException("3 seconds"));
//                            }
                            s.onNext(wrap(ids));
                            s.onCompleted();
                        })
//                    sleep(new Random().nextInt(5), SECONDS);
//                    if(true) {
//                        throw new RuntimeException("Error.");
//                    }
//                    return Observable.just(wrap(ids));
        );
    }

    private List<Wrapper> wrap(List<String> ids) {
        return ids.stream().map(Wrapper::new).collect(Collectors.toList());
    }

    class Wrapper {
        String e;

        Wrapper(String e) {
            this.e = e;
        }
    }

    private static void log(Object msg) {
        out.println(
                Thread.currentThread().getName() +
                        ": " + msg);
    }
}
