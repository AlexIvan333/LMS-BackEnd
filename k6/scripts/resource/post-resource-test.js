import http from 'k6/http';
import { check, sleep } from 'k6';
import { RESOURCE_BASE_URL } from '../config/config.js';
import { getAdminToken } from '../config/login-helper.js';

const fileData = open('../config/test-file.txt', 'b');

export function postResourceTest() {
    const token = getAdminToken();

    const payload = {
        file: http.file(fileData, 'test-file.txt', 'text/plain'),
    };

    const params = {
        headers: {
            'Cookie': `token=${token}`,
        },
    };

    const res = http.post(`${RESOURCE_BASE_URL}/resources`, payload, params);

    const passed = check(res, {
        'resource uploaded successfully (200)': (r) => r.status === 200,
    });

    if (!passed) {
        console.error(`Upload failed! Status: ${res.status}`);
        console.error(`Response body: ${res.body}`);
    }

    sleep(1);
}
