import http from 'k6/http';
import { check, sleep } from 'k6';
import { BASE_URL } from '../config/config.js';
import { getAdminToken } from '../config/login-helper.js';

export const options = {
  vus: 10,
  duration: '30s',
};

export default function () {
  const token = getAdminToken();

  const params = {
    headers: {
      'Cookie': `token=${token}`,              
      'Content-Type': 'application/json',
    },
  };

  const res = http.get(`${BASE_URL}/auth/students?page=1&size=10`, params);

  check(res, {
    'status is 200': (r) => r.status === 200,
  });

  sleep(1);
}
