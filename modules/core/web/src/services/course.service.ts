import axios, { AxiosResponse } from "axios";
import Course from "../model/course/Course";
import CoursePL from "../model/course/CoursePL";
import CourseAndParticipationPL from "../model/course/CourseAndParticipationPL";

class CourseService{

  getCourse(id: number): Promise<AxiosResponse<CoursePL>>{
    return axios.get("/api/course/" + id) 
  }

  getAllCourses(userId: number): Promise<CourseAndParticipationPL[]>{
    return new Promise<CourseAndParticipationPL[]>((resolve, reject) => {
      axios.get("/api/course/all/" + userId)
      .then((response) => {
        const courses: CourseAndParticipationPL[] = response.data
        resolve(courses)
      })
      .catch((error) => {
        console.log(error);
        reject()
      });
    })
    
  }

  postCourse(course: CoursePL){
    course.id = 0;
    return axios.post("/api/course", course)
  }

  putCourse(course: CoursePL){
    return axios.put("/api/course/" + course.id, course)
  }

  joinCourse(courseId: number, key: string, userId: number){
    return axios.post("/api/course/" + courseId + "/join", { keyPass: key, userId: userId })
  }

  leaveCourse(courseId: number, userId?: number){
    return axios.post("/api/course/" + courseId + "/leave?userId=" + userId)
  }

  getUserRoleInCourse(userId: number, courseId: number): Promise<string>{
    return new Promise<string>((resolve, reject) => {
      axios.get("/api/course/" + courseId + "/user/" + userId + "/role")
      .then((response) => {
        resolve(response.data)
      }).catch((error) => {
        console.log(error);
        reject();
      })
    })
  }

  deleteCourse(courseId: number){
    return axios.delete("/api/course/" + courseId)
  }

}

export default new CourseService();