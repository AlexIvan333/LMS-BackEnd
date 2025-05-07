import http from 'k6/http';
import { check } from 'k6';
import { AUTH_BASE_URL } from './config.js';

let cachedToken = null;

export function getAdminToken() {
  if (cachedToken) {
    return cachedToken;
  }

  const loginPayload = JSON.stringify({
    email: 'admin@gmail.com',
    password: 'password',
  });

  const headers = {
    'Content-Type': 'application/json',
  };

  const loginResponse = http.post(`${AUTH_BASE_URL}/auth/login/admin`, loginPayload, { headers });

  console.log(`Login Response Status: ${loginResponse.status}`);

  if (loginResponse.status === 200) {
    cachedToken = loginResponse.body;
    return cachedToken;
  }

  console.warn('Admin login failed. Trying to create admin...');

  const createAdminPayload = JSON.stringify({
    firstName: 'Admin',
    lastName: 'User',
    middleName: 'Super',
    streetName: 'Main Street',
    email: 'admin@gmail.com',
    streetNumber: 1,
    country: 'Netherlands',
    city: 'Eindhoven',
    zipCode: '5600',
  });

  const createResponse = http.post(`${AUTH_BASE_URL}/admins`, createAdminPayload, { headers });

  check(createResponse, {
    'There already is an account with this email address.': (r) => r.status === 201 || r.status === 400 || r.status === 409,
  });

  console.log(`Create Admin Response Status: ${createResponse.status}`);
  console.log(`Create Admin Response Body: ${createResponse.body}`);

  const retryLoginResponse = http.post(`${AUTH_BASE_URL}/auth/login/admin`, loginPayload, { headers });

  if (retryLoginResponse.status === 200) {
    cachedToken = retryLoginResponse.body;
    return cachedToken;
  } else {
    throw new Error(`Failed to login admin even after creation: ${retryLoginResponse.status} - ${retryLoginResponse.body}`);
  }
}
