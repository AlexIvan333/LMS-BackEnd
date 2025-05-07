import http from 'k6/http';
import { check, sleep } from 'k6';
import { AUTH_BASE_URL } from '../config/config.js';
import { getAdminToken } from '../config/login-helper.js';

function generateUniqueEmail() {
    const timestamp = Date.now();
    const vuId = __VU;
    return `instructor${vuId}_${timestamp}@example.com`;
}

export function postInstructorTest() {
    const token = getAdminToken();

    const createInstructorPayload = JSON.stringify({
        firstName: 'Instructor',
        lastName: 'User',
        middleName: 'Super',
        streetName: 'Main Street',
        email: generateUniqueEmail(),
        streetNumber: 1,
        country: 'Netherlands',
        city: 'Eindhoven',
        zipCode: '5600',
    });
    const params = {
        headers: {
            'Cookie': `token=${token}`,
            'Content-Type': 'application/json',
        },
    };

    const res = http.post(`${AUTH_BASE_URL}/auth/instructors`, createInstructorPayload, params);

    check(res, {
        'instructor created successfully (201)': (r) => r.status === 201,
    });

    sleep(1);
}