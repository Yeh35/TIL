# RxJava 기본

## RxJava란
* 자바에서 **리액티브 프로그래밍** 을 구현하는데 사용하는 라이브러리
* RxJava 2.0 부터는 **Reactive Streams** 사양을 따름

```java
public static void main(String[] args) {
    
    //데이터를 통지하는 생성자를 생성
    Flowable<String> flowable = Flowable.just("Hello", "Word!");
    
    //데이터를 통지받는 소비자를 생성
    Consumer<String> subscriber = str -> {
        System.out.println("통지: " + str);
    };
    
    //구독하기
    flowable.subscribe(subscriber);
}
```

## 리액티브 프로그래밍
데이터가 통지될 때마다 관련 프로그램이 **반응** 해 데이터를 처리하는 프로그래밍 방식이다.
다른 말로 표현하면 프로그램이 필요한 데이터를 직접 가져와 처리하는 것이 아니라 보내온 데이터를 받은 시점에 반응해 이를 처리하는 프로그램을 만드는 것이다.

리액티브 프로그래밍의 핵심은 **책임분리** 이다. 
데이터를 생성하는 측은 데이터를 전달하는 것까지만 책임지면 됩니다. (그뒤는 몰라도 됨)   
소비하는 측에서 어떤 것을 하는지는 신경쓰지 않아도 됨으로 데이터를 처리하는 도중에도 다음 데이터를 처리할 수 있습니다.    
(비동기 처리가 쉬움)

## Reactive Streams
Reactive Streams란 프레임워크에 상관없이 데이터 스트림을 비동기로 다룰 수 있는 공통 메커니즘으로 이 메커니짐을 편리하게 사용할 수 있는 **인터페이스** 를 제공한다.
즉, **인터페이스만 제공함으로 구현은 각 라이브러리 프레임워크에서 해야한다.**

Reactive Streams는 **데이터를 만들어 통지하는 Publisher(생성자)와 통지된 데이터를 받아 처리하는 Subscriber(소비자)로 구성**된다.
Subscriber(소비자)가 Publisher(생성자)를 구독하면 데이터가 통지되는 형식이다.
* Publisher(생성자) : 데이터를 만들어 통지
* Subscriber(소비자) : 통지된 데이터를 받아 처리

기본적으로 4개의 인터페이스가 주축이다.
```java
// 데이터를 만들고 통지하는 생성자
public static interface Publisher<T> {

    // 구독하기
    public void subscribe(Subscriber<? super T> subscriber);
}

// 데이터를 소비하는 소비자
public static interface Subscriber<T> {
    
    // 구독(subscribe)시 callBack되는 메서드
    public void onSubscribe(Subscription subscription);
    
    // 데이터를 통지받는 메서드 
    public void onNext(T item);

    // 통지도중 에러가 발생시 통지됨
    public void onError(Throwable throwable);

    // 완료 통지
    public void onComplete();
}

// 생성자(Publisher)와 소비자(Subscriber)를 연결하는 인터페이스
public static interface Subscription {

    // 데이터 요청
    public void request(long n);

    // 구독 해지
    public void cancel();
}

// 생성자와 소비자 기능 둘다 가지고 있는 인터페이스!
public static interface Processor<T,R> extends Subsc riber<T>, Publisher<R> {}
```

기본동작은 
1. Publisher와 Subscriber를 만들기
2. Subscriber를 담아서 Publisher에 subscribe 메소드 호출
3. callback으로 Subscriber에 onSubscribe 호출
4. Subscription.request를 통해 Subscriber가 받을 준비가 됬음을 (요청받을 데이터 개수와 함께) 알려줌 
5. onNext를 통해 데이터 통지
6. onComplete로 완료 통지

### Reactive Streams 규칙
##### Reactive Streams 기본 규칙은 다음과 같다.
* 구독 시작 통지(onSubscribe)는 해당 구독에서 한 번만 발생
* 통지는 순차적으로 이루어진다.
* null을 통지하지 않음
* Publisher의 처리는 완료(onComplete) 또는 에러(onError)를 통지해 종료

기본 규칙은 아니지만 구독이 종료된 생성자와 소비자 인스턴스를 재활용 하는 것은 좋지 못하다.
내부 상태를 제대로 초기화 하지 않은 경우 의도치 않은 결과가 나올 수 있다.

##### Subscription 규칙
* 데이터 개수 요청에 `Long.MAX_VALUE`를 설정하면 데이터 개수에 의한 통지 제한은 없다.
* Subscription의 메소드는 동기화된 생태로 호출해야한다. 
(스레드 안전하기 위해서)

## RxJava
RxJava는 Reactive Streams를 지원하는 `Flowable`, `Subscriber`와 지원하지 않는 `Observable`, `Observer`로 나뉜다.

|    구분                 |   생성자         |   소비자      |
|-------------------------|-----------------|---------------|
| Reactive Streams 지원    |   Flowable      | Subscriber   |
| Reactive Streams 미지원  |   Observable    | Observer     |

두개의 차이는 **배압기능**이 있는지 없는지 여부로 나뉜다.
`Observable`, `Observer`는 배압기능이 없다.
`Subscription.request`를 통해서 처리 가능한 데이터 양을 알려주는 것을 배압기능이다.

`Observable`, `Observer`는 `Subscription` 인터페이스 대신 `Disposable`를 사용한다.
```java
public interface Disposable {
    // 구독 해제
    void dispose();

    // 구독 해제 여부 확인
    boolean isDisposed();
}
```

### 연산자
RxJava에서 통지된 데이터가 소비자에게 도착하기 전에 불필요한 데이터를 삭제하거나 소비자가 사용하기 쉽게 데이터를 변환하는 하는 등 통지하는 데이터를 변경해야 할 때 사용된다.

```java
Flowable<Integer> flowable = 
        Flowable.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        // 짝수에 해당하는 데이터만 통지
        .filter(data -> data % 2 == 0)
        // 데이터를 100배로 변환
        .map(data -> data * 100);

flowable.subscribe(data -> System.out.println("data : " + data));
```

하지만 연산자를 남발하면 디버깅 하기 힘들어진다.
처리 작업의 외부에 어떤 변화를 주는 것을 **부가 작용**이라고 하는데 연산자를 사용하면 이런 부가 작용이 늘어남으로 **책임범위가 넒어진다.**

### Hot 생성자와 Cold 생성자

### Flowable VS Observable

### 주의 사항
