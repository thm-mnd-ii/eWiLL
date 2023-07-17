import axios from "axios";
import SubmitPL from "@/model/SubmitPL";

class SubmissionService{
    submitDiagram(submitPL: SubmitPL){
        return axios.post("/api/evaluation/v2/submit", submitPL)
    }

    getSubmissionIdsByUserAndTask(userId: number, taskId: number){
        return axios.get("/api/evaluation/v2/submissionIds/" + userId + "/" + taskId)
    }

    getSubmissionsByTask(taskId: number){
        return axios.get("/api/evaluation/v2/submission/" + taskId)
    }

    getLatestSubmissionsByTask(taskId: number){
        return axios.get("/api/evaluation/v2/submission/newest/" + taskId)
    }

    getResultBySubmissionIdAndLevel(submissionId: number, level: string){
        return axios.get("/api/evaluation/v2/result/" + submissionId + "/" + level)
    }

    getPassedByTaskAndUser(taskId: number, userId: number){
        return axios.get("/api/evaluation/v2/hasPassed/" + userId + "/" + taskId)
    }
}

export default new SubmissionService();