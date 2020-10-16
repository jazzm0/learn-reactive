package reactive.com;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public class BatchSubscriber implements Subscriber<String> {

    private int count = 0;
    private Subscription subscription;

    @Override
    public void onSubscribe(Subscription subscription) {
        this.subscription = subscription;
        subscription.request(2);
    }

    @Override
    public void onNext(String s) {
        count++;
        if (count > 2) {
            count = 0;
            subscription.request(2);
        }
    }

    @Override
    public void onError(Throwable throwable) {
    }

    @Override
    public void onComplete() {
    }
}
