import http from 'k6/http';
import { BASE_URL } from './config.js';

let cachedToken = null;

export function getAdminToken() {
  if (cachedToken) {
    return cachedToken;
  }

  const payload = JSON.stringify({
    email: 'admin@gmail.com',
    password: 'password',
  });

  const params = {
    headers: {
      'Content-Type': 'application/json'                    // ✅ Important!
    },
  };

  const response = http.post(`${BASE_URL}/auth/login/admin`, payload, params);

  console.log(`Login Response Status: ${response.status}`);
  console.log(`Login Response Body: ${response.body}`);

  if (response.status === 200) {
    const setCookieHeader = response.headers['Set-Cookie'];
    if (!setCookieHeader) {
      throw new Error('No Set-Cookie header received!');
    }

    const match = setCookieHeader.match(/token=([^;]+)/);
    if (match && match[1]) {
      cachedToken = match[1];
    } else {
      throw new Error('Token not found in Set-Cookie header!');
    }
  } else {
    throw new Error(`Failed to login admin: ${response.status} - ${response.body}`);
  }

  return cachedToken;
}
