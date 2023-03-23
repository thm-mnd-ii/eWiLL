import axios, { AxiosResponse } from "axios";
import Course from "../model/course/Course";
import CoursePL from "../model/course/CoursePL";
import CourseAndParticipationPL from "../model/course/CourseAndParticipationPL";

class CourseService{

  getCourse(id: number): Promise<AxiosResponse<CoursePL>>{
    return axios.get("/api/course/" + id) 
  }

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

  postCourse(course: CoursePL){
    return axios.post("/api/course", course)
  }

  joinCourse(courseId: number, key: string, userId: number){
    return axios.post("/api/course/" + courseId + "/join", { keyPass: key, userId: userId })
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