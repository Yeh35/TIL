package me.sangoh.rxjava.training;

import io.reactivex.*;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.schedulers.Schedulers;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.w3c.dom.CDATASection;

public class Rxjava {

    @Test
    public void flowableUseCase() throws Exception {
        Flowable<String> flowable =
                Flowable.create(new FlowableOnSubscribe<String>() {

                    @Override
                    public void subscribe(FlowableEmitter<String> emitter) {
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
                this.subscription.request(Long.MAX_VALUE);
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
                String threadName = Thread.currentThread().getName();
                System.out.println(threadName + ": 완료");
            }
        };

        flowable
                .observeOn(Schedulers.computation())
                .subscribe(subscriber);

        Thread.sleep(500L);
    }

    /**
     * Flowable과 비슷하지만 배압기능이 없다.
     */
    @Test
    public void observableUseCase() throws Exception {

        // 인사말 통지하는 생성자
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> emitter) throws Exception {
                String[] datas = {"Hello World!", "안녕, RxJava"};

                for (String data : datas) {
                    if (emitter.isDisposed()) {
                        return;
                    }

                    String threadName = Thread.currentThread().getName();
                    System.out.println(threadName + ".onNext : " + data);

                    emitter.onNext(data);
                }

                emitter.onComplete();
            }
        });


        observable
                .observeOn(Schedulers.computation()) // 소비하는 측의 처리를 개별 스레드로 실행
                .subscribe(new Observer<String>() { // 소비자 생성

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull String s) {
                        String threadName = Thread.currentThread().getName();
                        System.out.println(threadName + ": " + s);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        String threadName = Thread.currentThread().getName();
                        System.out.println(threadName + ": 완료");
                    }
                });

        observable
                .observeOn(Schedulers.computation()) // 소비하는 측의 처리를 개별 스레드로 실행
                .subscribe(new Observer<String>() { // 소비자 생성

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull String s) {
                        String threadName = Thread.currentThread().getName();
                        System.out.println(threadName + ": " + s);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        String threadName = Thread.currentThread().getName();
                        System.out.println(threadName + ": 완료");
                    }
                });

    }

    @Test
    public void compositeDisposable() throws InterruptedException {
        //Disposable
        CompositeDisposable compositeDisposable = new CompositeDisposable();

        compositeDisposable.add(Flowable.range(1, 3)
                .doOnCancel(() -> System.out.println(" No.1 cancled"))
                .observeOn(Schedulers.newThread())
                .subscribe(data -> {
                    System.out.print(Thread.currentThread().getName());
                    Thread.sleep(100L);
                    System.out.println(" No.1: " + data);
                })
        );

        Thread.sleep(150L);

        compositeDisposable.dispose();
    }
    
    @Test
    public void 연산자(){
        Flowable<Integer> flowable = 
                Flowable.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                // 짝수에 해당하는 데이터만 통지
                .filter(data -> data % 2 == 0)
                // 데이터를 100배로 변환
                .map(data -> data * 100);

        flowable.subscribe(data -> System.out.println("data : " + data));
    }
}
