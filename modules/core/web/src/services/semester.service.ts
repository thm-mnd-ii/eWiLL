import type Semester from "@/model/Semester"
import axios from "axios";

class SemesterService {

    getAllSemesters(){
        let semesters: Semester[] = [];
        return new Promise<Semester[]>((resolve, reject) => {
            axios.get("/api/semester")
            .then((response) => {
              semesters = response.data     
              // console.log(semesters)      
              resolve(semesters)
            })
            .catch((error) => {
              console.log(error);
              reject()
            });
          })
    }


}

export default new SemesterService();