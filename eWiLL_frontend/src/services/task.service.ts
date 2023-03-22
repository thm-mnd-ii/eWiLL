import Task from "../model/task/Task";
import axios from "axios";

class TaskService{

    getAllTasks(courseId: number): Promise<Task[]>{
        return new Promise<Task[]>((resolve, reject) => {
            axios.get("/api/tasks/" + courseId + "/tasks")
            .then((response) => {
                resolve(response.data)
            }).catch((error) => {
                reject()
            })
        })
    }

}

export default new TaskService();