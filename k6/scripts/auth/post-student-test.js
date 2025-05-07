import http from 'k6/http';
import { check, sleep } from 'k6';
import { AUTH_BASE_URL } from '../config/config.js';
import { getAdminToken } from '../config/login-helper.js';


function generateUniqueEmail() {
    const timestamp = Date.now();
    const vuId = __VU;
    return `student${vuId}_${timestamp}@example.com`;
}


export function postStudentTest() {
    const token = getAdminToken();

    const createStudentPayload = JSON.stringify({
        firstName: 'Student',
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

    const res = http.post(`${AUTH_BASE_URL}/auth/students`, createStudentPayload, params);

    check(res, {
        'student created successfully (201)': (r) => r.status === 201,
    });

    sleep(1);
}
