import type Task from "@/model/task/Task";
import axios from "axios";

class TaskService {
  getTask(id: number): Promise<Task> {
    return new Promise<Task>((resolve, reject) => {
      axios
        .get("/api/tasks/" + id)
        .then((response) => {
          resolve(response.data);
        })
        .catch((error) => {
          console.log(error);
          reject;
        });
    });
  }

  getAllTasks(courseId: number): Promise<Task[]> {
    return new Promise<Task[]>((resolve, reject) => {
      axios
        .get("/api/tasks/" + courseId + "/tasks")
        .then((response) => {
          resolve(response.data);
        })
        .catch((error) => {
          console.log(error);
          reject();
        });
    });
  }

  putTask(taskId: number, task: Task) {
    return axios.put("/api/tasks/" + taskId, task);
  }

  postTask(task: Task) {
    return axios.post("/api/tasks/" + task.courseId, task);
  }

  deleteTask(taskId: number) {
    return axios.delete("/api/tasks/" + taskId);
  }
}

export default new TaskService();
