# 포커스 처리

## requestFocus로 포커스 처리 삽질의 결과
사실 editText 포커스 처리에서 엄청 애먹었어서 기록을 하려고 남긴다.
포커스를 얻는 방법을 검색해보면 `requestFocus()`를 하라고 추천한다.
하지만 막상 사용해보면 내가 원하는 포커스로 안가때가 있다. ㅠㅠ
입력이 **아래에서 위로 향하는 경우에는 작동을 잘 안한다.** 

꼼수로 찾은 방법이 있다.

```xml
<EditText
    android:id="@+id/edit_first"
    android:inputType="number"
    android:importantForAutofill="no"
    android:nextFocusDown="@id/edit_first"
    tools:ignore="LabelFor"/>
```
`android:nextFocusDown`설정을 자기 자신으로 줘야지 `requestFocus()`를 해서도 포커스가 다음 포커스로 이동하지 않는다.

## 참고 문언
* [공식문서 포커스처리](https://developer.android.com/guide/topics/ui/ui-events.html#HandlingFocus)
