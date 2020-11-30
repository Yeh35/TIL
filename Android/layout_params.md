# 레이아웃 파라미터
Xml을 작성하다보면 다음과 같이 크기를 정해주는 설정을 했을 것이다.
```xml

android:layout_width="wrap_content"
android:layout_height="wrap_content"
```

이런 속성은 일반 속성하고 다르게 **레이아웃 파라미터** 라고 불린다.
일반 속성은 text, gravity, src와 같이 뷰 자체의 속성을 뜻한다.
TextView에서 text을 set할 수 있기 때문이다. (setText, setSrc 등 setter가 존재)

하지만 **레이아웃 파라미터** 는 뷰가 배치되는 부모에게 제시하는 속성으로 즉, 부모 레이아웃이 소속된 소속되어 있다고 할 수 있다.
View는 부모 레이아웃에 따라 파라미터 종류가 변함으로 set을 제공하지 않는다.
이유는 자신이 어떤 레이아웃에 놓일지 모르기 때문이다.

addView를 보면 `LayoutParams`를 파라미터로 받는다.
```java
public void addView(View child)
public void addView(View child, int index)
public void addView(View child, int width, int height)
public void addView(View child, LayoutParams params)
public void addView(View child, int index, LayoutParams params)
```
`addView(View child)`를 사용하게 될 경우 해당 View에 기본값이 세팅된다.

## 사용방법
기본적으로 `LayoutParams`객체를 생성해서 `addView`에 파라미터로 넣어주면 된다.

* [여기](https://whatisthenext.tistory.com/20)
* [여기](https://developer.android.com/reference/android/view/ViewGroup.LayoutParams)
를 보면 좋을거 같다.
