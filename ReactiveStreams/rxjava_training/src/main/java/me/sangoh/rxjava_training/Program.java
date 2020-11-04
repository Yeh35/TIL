package me.sangoh.rxjava_training;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.schedulers.Schedulers;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public class Program {

    public static void main(String[] args) throws Exception {

        Flowable<String> flowable =
                Flowable.create(new FlowableOnSubscribe<String>() {

                    @Override
                    public void subscribe(FlowableEmitter<String> emitter) throws Exception {
                        String[] datas = {"Hello, world!", "안녕, RxJava!"};

                        for (String data : datas) {
                            if (emitter.isCancelled()) {
                                return;
                            }

                            emitter.onNext(data);
                        }

                        emitter.onComplete();
                    }
                }, BackpressureStrategy.BUFFER);

        Subscriber subscriber = new Subscriber<String>() {

            private Subscription subscription;

            @Override
            public void onSubscribe(Subscription s) {
                this.subscription = s;
                this.subscription.request(1L);
            }

            @Override
            public void onNext(String data) {
                String threadName = Thread.currentThread().getName();
                System.out.println(threadName + ": " + data);
                this.subscription.request(1L);
            }

            @Override
            public void onError(java.lang.Throwable t) {
                t.printStackTrace();
            }

            @Override
            public void onComplete() {
                String threadName =  Thread.currentThread().getName();
                System.out.println(threadName + ": 완료" );
            }
        };

        flowable
                .observeOn(Schedulers.computation())
                .subscribe(subscriber);

        flowable
                .observeOn(Schedulers.computation())
                .subscribe(subscriber);

        Thread.sleep(500L);
    }

}
