import http from 'k6/http';
import { check, sleep } from 'k6';
import {ASSIGNMENT_BASE_URL} from '../config/config.js';
import { getAdminToken } from '../config/login-helper.js';

export function postAssignmentSubmissionTest() {
    const token = getAdminToken();

    const createAssignmentSubmissionPayload = JSON.stringify({
        "studentId": 17,
        "assigmentId": 1,
        "resourceIds": [1,2,4,6]
    });
    const params = {
        headers: {
            'Cookie': `token=${token}`,
            'Content-Type': 'application/json',
        },
    };

    const res = http.post(`${ASSIGNMENT_BASE_URL}/assignments/submissions`, createAssignmentSubmissionPayload, params);

    check(res, {
        'submission created successfully (201)': (r) => r.status === 201,
    });

    sleep(1);
}