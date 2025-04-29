import http from 'k6/http';
import { check, sleep } from 'k6';
import { COURSE_BASE_URL } from '../config/config.js';
import { getAdminToken } from '../config/login-helper.js';


export function postModuleTest() {
    const token = getAdminToken();

    const createModulePayload = JSON.stringify({
        "title": "string",
        "description": "string",
        "courseId": 1,
        "resourceIds": [3,4,5,6]
    });
    const params = {
        headers: {
            'Cookie': `token=${token}`,
            'Content-Type': 'application/json',
        },
    };

    const res = http.post(`${COURSE_BASE_URL}/courses/modules`, createModulePayload, params);

    check(res, {
        'module created successfully (201)': (r) => r.status === 201,
    });

    sleep(1);
}