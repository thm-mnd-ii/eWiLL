import axios, { AxiosResponse } from "axios";
import Course from "../model/course/Course";
import authHeader from "./auth-header";

class CourseService{

    getAllCourses(userId: number): Course[]{
      const courses: Course[] = [
          {
            id: 0,  
            semester: "SS21asdf",
            active: true,
            name: "MIB13 Datenbanken",
            location: "Friedberg",
            participation: true,
          },
          {
            id: 1,
            semester: "SS21",
            active: false,
            name: "Datenbanksysteme",
            location: "Gießen",
            participation: false,
          },
          {
            id: 2,
            semester: "WS21",
            active: true,
            name: "Relationale Datenbanken",
            location: "Friedberg",
            participation: true,
          },
          {
            id: 3,
            semester: "SS22",
            active: false,
            name: "SQL",
            location: "Gießen",
            participation: false,
          },
          {
            id: 4,
            semester: "SS22",
            active: false,
            name: "Datenbanksysteme 2",
            location: "Gießen",
            participation: false,
          },
          {
            id: 5,
            semester: "WS23",
            active: true,
            name: "Relationale Datenbanken 2",
            location: "Friedberg",
            participation: true,
          },
          {
            id: 6,
            semester: "SS23",
            active: false,
            name: "SQL for Fortgeschrittene",
            location: "Gießen",
            participation: false,
          },
          {
            id: 7,
            semester: "SS23",
            active: true,
            name: "SQL for Fortgeschrittene",
            location: "Gießen",
            participation: false,
          },
          {
            id: 8,
            semester: "SS23",
            active: true,
            name: "SQL for Fortgeschrittene",
            location: "Gießen",
            participation: true,
          },
          {
            id: 9,
            semester: "SS23",
            active: false,
            name: "SQL for Fortgeschrittene",
            location: "Friedberg",
            participation: false,
          },
          {
            id: 9,
            semester: "SS23",
            active: false,
            name: "SQL for Fortgeschrittene",
            location: "Friedberg",
            participation: true,
          },
        ];
        return courses;
  }
}

export default new CourseService();