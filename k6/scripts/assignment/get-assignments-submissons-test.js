import http from 'k6/http';
import { check, sleep } from 'k6';
import {ASSIGNMENT_BASE_URL} from '../config/config.js';
import { getAdminToken } from '../config/login-helper.js';



export function getAssignmentSubmissonsTest() {
    const token = getAdminToken();

    const params = {
        headers: {
            'Cookie': `token=${token}`,
            'Content-Type': 'application/json',
        },
    };

    const res = http.get(`${ASSIGNMENT_BASE_URL}/assignments/submissions?page=0&size=10`, params);

    check(res, {
        'get submissions status is 200': (r) => r.status === 200,
    });

    sleep(1);
}