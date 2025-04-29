import http from 'k6/http';
import { check, sleep } from 'k6';
import { COURSE_BASE_URL } from '../config/config.js';
import { getAdminToken } from '../config/login-helper.js';


export function getCoursesTest() {
    const token = getAdminToken();

    const params = {
        headers: {
            'Cookie': `token=${token}`,
            'Content-Type': 'application/json',
        },
    };

    const res = http.get(`${COURSE_BASE_URL}/courses?page=0&size=10`, params);

    check(res, {
        'get courses status is 200': (r) => r.status === 200,
    });

    sleep(1);
}