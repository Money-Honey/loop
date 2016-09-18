package com.loopme.webapp;

import org.junit.Ignore;
import org.junit.Test;
import rx.Observable;
import rx.subscriptions.Subscriptions;
import twitter4j.*;
import twitter4j.util.function.Consumer;

/**
 * @author <a href="mailto:dema.luxoft@gmail.com">Volodymyr Dema</a>
 */
public class ExConsumeTest {

    @Test
    @Ignore
    public void consume() {
        consume(
                (status) -> log(status),
                (exception) -> log(exception)
        );
    }

    @Test
    @Ignore
    public void consumeViaObserver() {
        observe().subscribe(
                status -> log(status),
                exception -> log(exception)
        );
    }

    Observable<Status> observe(){
        return Observable.create(subscriber -> {
            TwitterStream twitterStream =
                    new TwitterStreamFactory().getInstance();
            twitterStream.addListener(new StatusListener() {
                @Override
                public void onStatus(Status status) {
                    subscriber.onNext(status);
                }

                @Override
                public void onException(Exception ex) {
                    subscriber.onError(ex);
                }

                //other callbacks
                @Override
                public void onStallWarning(StallWarning stallWarning) {
                }

                @Override
                public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
                }

                @Override
                public void onTrackLimitationNotice(int i) {
                }

                @Override
                public void onScrubGeo(long l, long l1) {
                }
            });
            subscriber.add(Subscriptions.create(twitterStream::shutdown));
        });
    }

    void consume(
            Consumer<Status> onStatus,
            Consumer<Exception> onException) {
        TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
        twitterStream.addListener(new StatusListener() {
            @Override
            public void onStatus(Status status) {
                onStatus.accept(status);
            }

            @Override
            public void onException(Exception ex) {
                onException.accept(ex);
            }

            //other callbacks
            @Override
            public void onStallWarning(StallWarning stallWarning) {}
            @Override
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {}
            @Override
            public void onTrackLimitationNotice(int i) {}
            @Override
            public void onScrubGeo(long l, long l1) {}
        });
        twitterStream.sample();
    }

    private static void log(Object msg) {
        System.out.println(
                Thread.currentThread().getName() +
                        ": " + msg);
    }
}
