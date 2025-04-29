import { getStudentTest } from "./auth/get-student-test.js";
import { postStudentTest } from "./auth/post-student-test.js";
import { postInstructorTest } from "./auth/post-instructors-test.js";
import { getInstructorsTest } from "./auth/get-instructors-test.js";
import { getModulesTest } from "./course/get-modules-test.js";
import { getCoursesTest } from "./course/get-courses-test.js";
import { postModuleTest } from "./course/post-module-test.js";
import { postCourseTest } from "./course/post-course-test.js";
import { getAssignmentsTest} from "./assignment/get-assignments-test.js";
import { getAssignmentSubmissonsTest} from "./assignment/get-assignments-submissons-test.js";
import { postAssignmentTest } from "./assignment/post-assignment-test.js";
import {postAssignmentSubmissionTest} from "./assignment/post-assignment-submission-test.js";
import {postResourceTest} from "./resource/post-resource-test.js";
import {getResourceByIdTest} from "./resource/get-resource-by-id-test.js";

export const options = {
    vus: 5,
    duration: '30s',
};

export default function () {
    getStudentTest();
    postStudentTest();
    postInstructorTest();
    getInstructorsTest();
    getModulesTest();
    getCoursesTest();
    postModuleTest();
    postCourseTest();
    getAssignmentsTest();
    getAssignmentSubmissonsTest();
    postAssignmentTest();
    postAssignmentSubmissionTest();
    postResourceTest();
    getResourceByIdTest();
}
