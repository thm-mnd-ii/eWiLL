import axios, { AxiosResponse } from "axios";
import Course from "../model/course/Course";
import CoursePL from "../model/course/CoursePL";
import CourseAndParticipationPL from "../model/course/CourseAndParticipationPL";

class CourseService{

  getAllCourses(userId: number): Promise<Course[]>{
    const courses: Course[] = [];
    return new Promise<Course[]>((resolve, reject) => {
      axios.get("/api/course/all/" + userId)
      .then((response) => {
        const data: CourseAndParticipationPL[] = response.data
        data.forEach((element) => {
          const tmpCourse: Course = this.convertToCourse(element.course, element.member)
          courses.push(tmpCourse)
        })
        resolve(courses)
      })
      .catch((error) => {
        console.log(error);
        reject()
      });
    })
    
  }

  convertToCourse(payload: CoursePL, member: boolean): Course{
    const course = {} as Course;
    course.id = payload.id;
    course.active = payload.active;
    course.location = payload.location;
    course.name = payload.name;
    course.participation = member;
    course.semester = "Not yet implemented"

    return course;
  }
}

export default new CourseService();