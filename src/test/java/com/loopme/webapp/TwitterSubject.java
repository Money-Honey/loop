package com.loopme.webapp;

import rx.Observable;
import rx.subjects.PublishSubject;
import twitter4j.*;

import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:dema.luxoft@gmail.com">Volodymyr Dema</a>
 */
public class TwitterSubject {

    private final PublishSubject<Long> subject = PublishSubject.create();

    public TwitterSubject() {
//        TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
//        twitterStream.addListener(new StatusListener() {
//            @Override
//            public void onStatus(Status status) {
//                subject.onNext(status);
//            }
//
//            @Override
//            public void onException(Exception ex) {
//                subject.onError(ex);
//            }
//
//            //other callbacks
//            @Override
//            public void onStallWarning(StallWarning stallWarning) {}
//            @Override
//            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {}
//            @Override
//            public void onTrackLimitationNotice(int i) {}
//            @Override
//            public void onScrubGeo(long l, long l1) {}
//        });
//        twitterStream.sample();

        Observable.interval(1, TimeUnit.SECONDS).subscribe(v -> subject.onNext(v));
    }

    public Observable<Long> observe() {
        return subject.serialize();
    }

}
