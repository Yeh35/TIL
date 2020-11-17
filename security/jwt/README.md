# JSON Web Token(JWT)

## JSON Web Token는 뭘까?
당사자간에 정보를 JSON 객체로 안전하게 전송하기 위한 컴팩트하고 독립적인 방식을 정의한 표준([RFC 7519](https://tools.ietf.org/html/rfc7519))이다.

* 디지털 서명:
    JWT는 비밀 또는 공개/개인 키 쌍을 사용하여 서명할 수 있다.

* 자가 수용적(self-contained) : 
    JWT는 필요한 모든 정보를 자체적으로 지니고 있다.   
    JWT시스템에서 발급된 토큰은 토큰에 기본정보(그인시스템에서는 유저 정보)와 토큰이 검증됐다는 것을 증명해주는 signature 를 포함

* 쉽게 전달 될 수 있습니다
    웹서버의 경우 HTTP의 헤더에 넣어서 전달 할 수도 있고, URL의 파라미터로 전달 할 수도 있다.

## JWT 언제 사용??
* 회원 인증: 
    가장 흔한 케이스, 유저가 로그인 하면, 서버는 유저 정보를 기반한 토큰을 발행후 유저에게 전달
* 정보 교류:    
    두 개체(컴퓨터?) 사이에 안전하게 정보를 교환    
    그 이유는 sign이 되어 있어 정보를 보낸이, 도중에 조작여부를 검증할 수 있다.

## JWT의 구조
![JWT의 구조](./jwt_structure.png)    
압축 형식의 JWT는 점(`.`)으로 세 부분을 구성한다.
세 부분은 `헤더`, `내용`, `서명`으로 나뉜다.

### 헤더(hearder)
```json
{
  "typ": "JWT",
  "alg": "HS256"
}
```
헤더는 일반적으로 타입과 해싱 알고리즘을 정의해놓는다.
* `typ`: 토큰의 타입을 지정한 것으로 바로 `JWT`를 뜻한다
* `alg`: 해싱 알고리즘을 지정, 보통 `HMAC SHA256` 혹은 `RSA`를 사용한다.
    이 알고리즘은, 토큰을 검증 할 때 `서명(signature)`부분에서 사용된다.

    참고: JSON 형태의 객체가 base64 로 인코딩 되는 과정에서 공백 / 엔터들이 사라진다. 
    `{"alg":"HS256","typ":"JWT"}`

### 정보(payload)
정보(payload)는 토큰에 담을 정보를 넣는다.   
여기에 담는 정보의 한 '조각'을 클레임(claim)이라고 부른다.
클레임(claim)은 naem/value의 한 쌍으로 이뤄져있다.
토큰에는 여러개의 클레임을 넣을 수 있다.

클레임(claim)에는 세 분류로 나눠져 있다.
* 등록된 (registered) 클레임
* 공개 (public) 클레임
* 비공개 (private) 클레임

#### 등록된(registered) 클레임,
서비스에서 필요한 정보들이 아닌 토근에 대한 정보를 담기위하여 이름이 이미 정의된 클래임들이다.
정의는 [JWT표준](https://tools.ietf.org/html/rfc7519#section-4.1)에 정의되어 있다.
등록된(registered) 클레임을 사용하는 것은 전부 선택적(optional)이다.

* `iss`: 토큰 발급자(issuer)
* `sub`: 토큰 제목 (subject)
* `aud`: 토큰 대상자 (audience)
* `exp`: 토큰의 만료시간 (expiraton), 시간은 NumericDate 형식으로 되어있어야 하며 언제나 현재 시간보다 이후로 설정 (예: 1480849147370)
* `nbf`:  토큰의 활성 날짜(Not Before), 시간은 NumericDate 형식으로 날짜를 지정하며, 이 날짜가 지나기 전까지는 토큰이 처리되지 않습니다.
* `iat`: 토큰이 발급된 시간 (issued at), 이 값을 사용하여 토큰의 나이가  얼마나 되었는지 판단
* `jti`:  JWT의 고유 식별자(JWT ID), 주로 중복적인 처리를 방지하기 위하여 사용됩니다. 일회용 토큰에 사용하면 유용합니다.

그외는 [JWT표준](https://tools.ietf.org/html/rfc7519#section-4.1)을 확인

#### 공개(public) 클레임
JWT를 사용하는 사람들이 원하는대로 정의할 수 있다.
그러나 이름 충돌을 방지하려면 [IANA JSON Web Toekn Registries](https://www.iana.org/assignments/jwt/jwt.xhtml)에 정의하거나, URL형식으로 이름을 짓는다.

```json
{
    "https://velopert.com/jwt_claims/is_admin": true
}
```
`https://velopert.com/jwt_claims/`는 충돌 방지용 네임 스페이스
`is_admin`는 실질적 토큰 네임이다.

#### 비공개(private) 클레임
비공개(private) 클레임은 등록된 클레임도, 공개된 클레임도 아니다.
즉 사용하는 양 측간(클라이언트 <-> 서버) 합의하에 사용되는 클레임을 뜻한다.
다른 클레임과 달리 이름이 중복 충돌이 발생할 수 있으니 주의 해서 사용.

```json
{
    "username": "velopert"
}
```

### 서명(signature)
서명(signature)은 헤더의 인코딩값과, 정보의 인코딩값을 합친후 주어진 비밀키로 해쉬를 하여 생성한다.

서명부분을 만드는 슈도코드의 구조
```pseudocode
HMACSHA256(
  base64UrlEncode(header) + "." +
  base64UrlEncode(payload),
  secret)
```

이렇게 만든 해쉬를, `base64`형태로 나타내면 된다.
(만들어진 `해쉬코드` -> `base64`로 인코딩)

## 만들어보기
```javascript
const crypto = require('crypto');

const header = {
    "typ": "JWT",
    "alg": "HS256"
  };

const payload = {
    "iss": "velopert.com",
    "exp": "1485270000000",
    "https://velopert.com/jwt_claims/is_admin": true,
    "userId": "11028373727102",
    "username": "velopert"
};


// encode to base64
const encodedHeader = Buffer.from(JSON.stringify(header))
    .toString('base64')
    .replace('=', '');

const encodedPayload = Buffer.from(JSON.stringify(payload))
    .toString('base64')
    .replace('=', '');

const signature = crypto.createHmac('sha256', 'secret')
    .update(encodedHeader + '.' + encodedPayload)
    .digest('base64')
    .replace('=', '');
               

console.log('header: ',encodedHeader);
console.log('payload: ',encodedPayload);
console.log('signature: ',signature);
console.log(encodedHeader + '.' + encodedPayload + '.' + signature);
```

![여기 스크립트가 있다.](./jwt_script.js)

    base64로 인코딩을 할 때 dA== 처럼 뒤에 = 문자가 한두개 붙을 때가 있습니다. 
    이 문자는 base64 인코딩의 padding 문자라고 부릅니다.
    JWT 토큰은 가끔 URL 의 파라미터로 전달 될 때도 있는데요, 이 = 문자는, url-safe 하지 않으므로, 제거되어야 합니다. 패딩이 한개 생길 때도 있고, 두개 생길 때도 있는데, 전부 지워

## HTTP에서 사용할 때 
사용자는 엑세스하기 원할때 마다 일반적으로 `Bearer 스키마를 사용하는 Authorization 헤더`에 JWT를 전송한다.

```
Authorization: Bearer <token>
```

필요한 데이터를 토큰에 다 저장하고 있으면 DB를 거치지 않고도 작업을 할 수 있다. 
(물론 다 그런건 아니다.)

## 보안 문제를 방지
* 일반적으로 필요한 것보다 오래 토큰을 보관하면 안된다.
* 민감한 세션 데이터를 브라우저 저장소에 넣어선 안된다.

## 결론
JWT 구조와 어떤 과정으로 생성이되는지 알았다.
보통은 직접 인코딩하여 JWT를 생성할일은 없다. 다양한 언어의 다양한 라이브러리들을 설정하는 것만으로도 그 작업을 해주니 말이다.
하지만 알고 쓰는거랑 모르고 쓰는 것은 큰 차이가 있다.

## 참고
* [JSON 웹 토큰 소개](https://jwt.io/introduction/)
* [JSON Web Token 소개 및 구조](https://velopert.com/2389)


## 읽어봐야하는 것
* https://vladmihalcea.com/hibernate-identity-sequence-and-table-sequence-generator/
* https://velopert.com/2389
* https://jwt.io/introduction/