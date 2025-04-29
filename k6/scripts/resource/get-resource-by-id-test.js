import http from 'k6/http';
import { check, sleep } from 'k6';
import { RESOURCE_BASE_URL } from '../config/config.js';
import { getAdminToken } from '../config/login-helper.js';

export function getResourceByIdTest() {
    const token = getAdminToken();
    const resourceId = 1;

    const params = {
        headers: {
            'Cookie': `token=${token}`,
            'Content-Type': 'application/json',
        },
    };

    const res = http.get(`${RESOURCE_BASE_URL}/resources/${resourceId}`, params);

    const passed = check(res, {
        'resource fetched successfully (200)': (r) => r.status === 200,
    });

    if (!passed) {
        console.error(`❌ Failed to fetch resource with ID ${resourceId}`);
        console.error(`Status: ${res.status}`);
        console.error(`Response: ${res.body}`);
    }

    sleep(1);
}