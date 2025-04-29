import http from 'k6/http';
import { check, sleep } from 'k6';
import { AUTH_BASE_URL } from '../config/config.js';
import { getAdminToken } from '../config/login-helper.js';


export function getStudentTest() {
  const token = getAdminToken();

  const params = {
    headers: {
      'Cookie': `token=${token}`,              
      'Content-Type': 'application/json',
    },
  };

  const res = http.get(`${AUTH_BASE_URL}/auth/students?page=1&size=10`, params);

  check(res, {
    'get students status is 200': (r) => r.status === 200,
  });

  sleep(1);
}
