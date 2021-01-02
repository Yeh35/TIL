### 람다식(Lamdaba Expressions)?
* 처음 람다라는 이야기를 들었을 떄 '람보'가 생각나는.... 나만 그런건가?...   
* 람다식은 `Java8`부터 도입되었다.   
* 람다식은 익명함수(anonymous function)을 생성하기 위한 식으로 함수 지향 프로그래밍에 사실 더 가깝다.  
* 매개변수를 가진 코드블록에 불가하지만, 런티임 시 익명 구현 객체를 생성한다.   

### 람다식를 사용하기 위해서는 규칙이 있다.   
**람다식이 구현할 인터페이스에 추상메소드가 한개만 존재햐아한다.** 

`Java8`이전에 코드에 익숙하다면 간결해 보이지만 조금 낫선감은 없지 않다.   

정리하면 **람다식은 로컬 익명 구현객체를 생성을 하며 주로 Interface의 메소드를 간편하게 구현하는 목적으로 사용한다**   

### 이제 코드로 보자
아래와 같은 인터페이스가 있다고 가정해보자
```
@FunctionalInterface
public interface LamdabaInterface {
    String hello();
}
```
* `@FunctionalInterface`는 함수형 인터페이스라고 선언하는 것으로 람다식을 사용할 수 있다고 명시해주는 어노테이션이다.   
* 위에서 람다식을 사용하기 위해서는 추상화 메소드가 하나만 있어야 한다고 했는데 그 규칙을 어기면 컴파일 에러를 내보낸다.  

이제 위의 인터페이스를 구현해야 한다고 하면 처음에는 상속을 이용할 것이다.
```
public class LamdabaClass implements LamdabaInterface {

    @Override
    public String hello() {
        return "Hello";
    }

    public static void main(String[] args) {
        LamdabaClass lamdaba = new LamdabaClass();
        print(lamdaba);
    }

    public static void print(LamdabaInterface lamdaba) {
        System.out.println(lamdaba.hello());
    }
}
```
재사용도 가능하고 익숙하다 하지만 다른 곳에서 사용하지 않는 것이라면 이렇게 만드는거 자체가 유지보수(어디서 몰래 가져다 쓸지 모르니까..)에도 코드를 작성하는 입장에서도 좋지 않다..

그 대안으로 `익명함수`를 사용하는 방법이다.

```
public class LamdabaClass {

    public static void main(String[] args) {
        print(new LamdabaInterface() {

            @Override
            public String hello() {
                return "Hello";
            }
        });
    }

    public static void print(LamdabaInterface lamdaba) {
        System.out.println(lamdaba.hello());
    }
}
```
처음에 비해 많이 간결하기도 하고 쓸대 없는 재사용도 하지 않을 수 있다.   
하지만 그래도 뭔가 길다... 이걸 더 줄일 수 있는 것이 `람다식`이다.

```
public class LamdabaClass {

    public static void main(String[] args) {
        print(() -> "Hello");
    }

    public static void print(LamdabaInterface lamdaba) {
        System.out.println(lamdaba.hello());
    }
}
```
와우 놀라울 정도로 간결해졌다.   
물론 Interface에 무슨 메소드가 추상화 되었는지는 따라가봐야 알 수 있지만 이 간결함에 그정도는 감수 할 수 있다.  
예제에서는 파라미터가 없는 추상 메소드를 사용하였지만 파라미터가 있어도 된다.

### 람다식 문법..
람디식의 간결함에 대해서 봤으니 이제 재미없는 문법을 볼 차례이다.
람다식의 기본 문법은 `(타입 매개변수, ....) -> {실행문;};` 이렇다.
하지만 여기서도 다음과 같은 사항을 더 생략할 수 있다.
* 매개변수의 자료형은 생략 가능
* 매개변수가 하나이면 소괄호'()' 생략 가능
* 실행문이 한개라면 중괄호'{}' 생략 가능
* 실행문이 한개고 그 한개의 실행문이 return문일 경우 중괄호'{}'와 return문 생략 가능
