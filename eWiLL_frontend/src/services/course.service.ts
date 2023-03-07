import axios, { AxiosResponse } from "axios";
import Course from "../model/course/Course";
import authHeader from "./auth-header";

class CourseService{


    getAllCourses(userId: number): Course[]{
        const courses: Course[] = [
            {
              id: 0,  
              semester: "SS21",
              active: 1,
              name: "MIB13 Datenbanken",
              location: "Friedberg",
              participation: 1,
            },
            {
              id: 1,
              semester: "SS21",
              active: 0,
              name: "Datenbanksysteme",
              location: "Gießen",
              participation: 0,
            },
            {
              id: 2,
              semester: "WS21",
              active: 1,
              name: "Relationale Datenbanken",
              location: "Friedberg",
              participation: 1,
            },
            {
              id: 3,
              semester: "SS22",
              active: 0,
              name: "SQL",
              location: "Gießen",
              participation: 0,
            },
            {
              id: 4,
              semester: "SS22",
              active: 0,
              name: "Datenbanksysteme 2",
              location: "Gießen",
              participation: 0,
            },
            {
              id: 5,
              semester: "WS23",
              active: 1,
              name: "Relationale Datenbanken 2",
              location: "Friedberg",
              participation: 1,
            },
            {
              id: 6,
              semester: "SS23",
              active: 0,
              name: "SQL for Fortgeschrittene",
              location: "Gießen",
              participation: 0,
            },
          ];
          return courses;
    }
}

export default new CourseService();