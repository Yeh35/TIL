# [SLF4J] 로깅 프레임 워크 선택에 대한 고민
Java에서는 많은 로깅 시스템이 존재하는데 그런 것들의 총합이다.

## [common logging](https://commons.apache.org/proper/commons-logging/)
서로 다른 로깅 구현 사이에 작은 다리 역할을 하는 로그 **인터페이스**이다.
Spring Boot 내부 로깅에 기본으로 사용되긴 한다.(하지만 다른걸로 설정가능)
실질적인 인터페이스도 `interface Log`와 `class LogFactory`만 사용하면 된다.
구현체로는 `Log4J`같은 것이 있다.

## [SLF4J](http://www.slf4j.org/)
`common logging`와 비슷하게 로깅 구현체(시스템)의 추상화한 **인터페이스**를 제공한다.
홈페이지에서는 다음과 같이 정의하고 있다.

    SLF4J (Simple Logging Facade for Java)는 다양한 로깅 프레임 워크 (예 : java.util.logging, logback, log4j)에 대한 단순한 파사드 또는 추상화 역할을하여 최종 사용자가 배포 시 원하는 로깅 프레임 워크에 연결할 수 있도록합니다.

### common logging vs SLF4J
`SLF4J`의 출발 자체가 `common logging`의 몇가지 문제점을 해결하면서 등장한 API이다.
예시를 보자면..
```java
Object entry = new SomeObject(); 
commons-loggin: logger.debug("The new entry is "+entry+"."); 
SLF4J: logger.debug("The new entry is {}.", entry);
```
`commons-loggin` 코드에는 두가지 문제점이 있다.
debug 단계에서 로그를 남긴다면 상관이 없지만 운영단계에서 남가지도 않는 로그를 위해서 비싼 String 계산을 하게 된다. (성능저하)
그걸 방지하려면 다음과 같이 코드를 작성해야하는데
```java
if(logger.isDebugEnabled()) { 
    logger.debug("Entry number: " + i + " is " + String.valueOf(entry[i])); 
}
```
코드가 난잡해지고 실수할 여지도 생긴다. 그 뿐만 아니라 `isDebugEnabled`라는 메소드를 호출하는 것 자체도 작지만 리소스 소모가 일어납니다. (스택에 올랐다 내렸다)

이런 문제를 해결하기 위해 **SLF4J는 별도의 파라미터 포매팅 개념을 도입**했습니다.
```java
logger.debug("The new entry is {}.", entry);
```
위에 소스와 같이 로깅을 남겨야하는 여부를 결정하는 행위의 주체가 SLF4J로 넘어 갔고 
\String 계산도 SLF4J가 합니다.
로그를 남겨야 겠다고 판단되는 시점에 `{}`와 파라미터로 받은 값을 교체합니다.

```java
Object entry = new SomeObject(); 
commons-loggin: logger.debug("The new entry is "+entry+"."); 
SLF4J: logger.debug("The new entry is {}.", entry);
```
다시 이 예제에서는 둘이 같은 일을 하지만 성능 차이는 30배 정도 차이난다.
로그가 한두개라면 모를까 수많은 로그에서 `commons-loggin`처럼 작동하면 시스템에 부하를 먹게 된다.

### SLF4J 사용 설명
* [SLF4J 사용 설명](http://www.slf4j.org/manual.html)를 기반으로 작성하였습니다.

일단 SLF4J는 API용 인터페이스로 구현체가 없다. 
`slf4j-api.jar`를 클래스 패스에 추가하는 것 만으로도 동작하지 않는다.
가장 간단하게 구현된 구현체는 `slf4j-simple.jar`가 있다.
그리고 가장 많이 사용하는 구현체는 `logback`이다.

가장 유명하고 간단한 예시
```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloWorld {
  public static void main(String[] args) {
    Logger logger = LoggerFactory.getLogger(HelloWorld.class);
    logger.info("Hello World");
  }
}
```

일반적으로 많이 사용하는 패턴
```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Wombat {
 
  final Logger logger = LoggerFactory.getLogger(Wombat.class);
  Integer t;
  Integer oldT;

  public void setTemperature(Integer temperature) {
   
    oldT = t;        
    t = temperature;

    logger.debug("Temperature set to {}. Old temperature was {}.", t, oldT);

    if(temperature.intValue() > 50) {
      logger.info("Temperature has risen above 50 degrees.");
    }
  }
}
```

### Fluent Logging API
버전 `2.0.0`이후에 사용가능하고 `Java8`이상의 의존성을 가지고 있다.
기존하고의 차이점은 [`LoggingEventBuilder`](http://www.slf4j.org/apidocs/org/slf4j/spi/LoggingEventBuilder.html)라는 개념이 추가되었다.
말보다는 코드를 보는게 이해가 빠를거 같다. 아래 두 코드는 같은 동작을 한다.
```java
logger.info("Hello world."); //기존
logger.atInfo().log("Hello world"); //Fluent Logging API
```   
Build 패턴처럼 줄줄이 연결해서 최종 `.log`라는 함수가 나오면 로그를 출력한다.
개인적인 이런 유형의 API는 쓰다보면 잘 써지는거 같다.
그러니 예제를 많이 많이...
```java
int newT = 15;
int oldT = 16;

// using traditional API
logger.debug("Temperature set to {}. Old temperature was {}.", newT, oldT);

// using fluent API, add arguments one by one and then log message
logger.atDebug().addArgument(newT).addArgument(oldT).log("Temperature set to {}. Old temperature was {}.");

// using fluent API, log message with arguments
logger.atDebug().log("Temperature set to {}. Old temperature was {}.", newT, oldT);

// using fluent API, add one argument and then log message providing one more argument
logger.atDebug().addArgument(newT).log("Temperature set to {}. Old temperature was {}.", oldT);

// using fluent API, add one argument with a Supplier and then log message with one more argument.
// Assume the method t16() returns 16.
logger.atDebug().addArgument(() -> t16()).log(msg, "Temperature set to {}. Old temperature was {}.", oldT);
```

값을 표현하는것도 명확하게 할 수 있다.
```java
int newT = 15;
int oldT = 16;

// using classical API
logger.debug("oldT={} newT={} Temperature changed.", newT, oldT);

// using fluent API
logger.atDebug().addKeyValue("oldT", oldT).addKeyValue("newT", newT).log("Temperature changed."); 
```   
    사견으로는 어쩌피 변수명 그대로 출력하는 경우가 많은데 `.addKeyValue("oldT", oldT)`이렇게 말고 리플렉션을 사용해서 `addKeyValue(oldT)`이렇게 해주면 안돼나... 음.. 리플렉션을 하면 속도가 느려지겠구나.. 

## 마치며..
`SLF4J`는 참 이름으 안 외워지는 API이다. (쓸때마다 S...뭐였지 찾게되는..)
로깅 시스템?을 학습하면서 설계가 얼마나 어렵고 중요한지 그리고 그걸 이해하고 사용하는게 얼마나 교훈을 얻은거 같다.
사실 `common logging`을 사용하면 가독성 자체는 그렇게 나쁘지 않다.
```java
commons-loggin: logger.debug("The new entry is "+entry+".");
```
하지만 성능측면과 (일단 java String 표현방식이 구리니 할말이 없지만)생산성 측면에서 좋지는 못하다.
코드를 작성하는데 있어서 이런 사소한(로그가 사소해?)부분들에서 차이를 만들어 내는거 같다.

## 자료
* [sprint boot logging 공식문서](https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-logging)
* [commons-logging 공식문서](https://commons.apache.org/proper/commons-logging/)
* [SLF4J](http://www.slf4j.org/index.html)
* [Common-Logging의 문제점](https://hermeslog.tistory.com/186)
* [slf4j 사용 설명](http://www.slf4j.org/manual.html)
