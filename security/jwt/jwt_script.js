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
