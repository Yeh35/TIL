# 함수형 인터페이스
함수형 인터페이스(람다식)는 구현해야할 메서드가 1개만 있어서 `인자 유무`, `타입 및 반환값 유무`, `타입에 대한 정보`가 필요하다.

하지만 **JAVA8 부터는 범용적으로 사용할 수 있는 몇가지 함수형 인터페이스가 추가 되었다.**
* `java.util.function` 패키지 안에 많은 인터페이스가 추가되어 있다.
* 함수형 인터페이스는 일급 객체를 사용할 수 없는 자바 언어의 단점을 보안하고자 도입

## 사용방법 
어떤 유형이 있는지는 [여기](https://javaplant.tistory.com/34)를 클랙해서 보면 될거 같다. 
하지만 굳이 어느 것이 있는지 한번만 살펴보고 안봐도 된다.
사용 유형만 대충 보면 된다.
아래는 람다식을 사용한 Funtional Interface의 사용 예이다.
```java
() -> {}                // No parameters; result is void
() -> 42                // No parameters, expression body
() -> null              // No parameters, expression body
() -> { return 42; }    // No parameters, block body with return
() -> { System.gc(); }  // No parameters, void block body
 
() -> {                 // Complex block body with returns
  if (true) return 12;
  else {
    int result = 15;
    for (int i = 1; i < 10; i++)
      result *= i;
    return result;
  }
}                         
 
(int x) -> x+1              // Single declared-type parameter
(int x) -> { return x+1; }  // Single declared-type parameter
(x) -> x+1                  // Single inferred-type parameter
x -> x+1                    // Parentheses optional for single inferred-type parameter
 
(String s) -> s.length()      // Single declared-type parameter
(Thread t) -> { t.start(); }  // Single declared-type parameter
s -> s.length()               // Single inferred-type parameter
t -> { t.start(); }           // Single inferred-type parameter
 
(int x, int y) -> x+y  // Multiple declared-type parameters
(x, y) -> x+y          // Multiple inferred-type parameters
(x, int y) -> x+y    // Illegal: can't mix inferred and declared types
(x, final y) -> x+y  // Illegal: no modifiers with inferred types 
```

## 참고
* [Java 8 - Function Interface](https://beomseok95.tistory.com/277)
* https://javaplant.tistory.com/34
