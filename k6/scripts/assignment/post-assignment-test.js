import http from 'k6/http';
import { check, sleep } from 'k6';
import {ASSIGNMENT_BASE_URL} from '../config/config.js';
import { getAdminToken } from '../config/login-helper.js';

export function postAssignmentTest() {
    const token = getAdminToken();

    const createAssignmentPayload = JSON.stringify({
        "title": "string",
        "description": "string",
        "deadline": "2025-04-29T18:19:39.863Z",
        "courseId": 9,
        "moduleId": 9,
        "resourceIds": [2,4,6]
    });
    const params = {
        headers: {
            'Cookie': `token=${token}`,
            'Content-Type': 'application/json',
        },
    };

    const res = http.post(`${ASSIGNMENT_BASE_URL}/assignments`, createAssignmentPayload, params);

    check(res, {
        'assignment created successfully (201)': (r) => r.status === 201,
    });

    sleep(1);
}

