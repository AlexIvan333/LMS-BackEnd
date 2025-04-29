import http from 'k6/http';
import { check, sleep } from 'k6';
import { COURSE_BASE_URL } from '../config/config.js';
import { getAdminToken } from '../config/login-helper.js';


export function postCourseTest() {
    const token = getAdminToken();

    const createCoursePayload = JSON.stringify({
        "title": "string",
        "description": "string",
        "instructorID": 18,
        "maxStudents": 100
    });
    const params = {
        headers: {
            'Cookie': `token=${token}`,
            'Content-Type': 'application/json',
        },
    };

    const res = http.post(`${COURSE_BASE_URL}/courses`, createCoursePayload, params);

    check(res, {
        'course created successfully (201)': (r) => r.status === 201,
    });

    sleep(1);
}