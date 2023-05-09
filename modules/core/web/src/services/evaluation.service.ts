import axios from "axios";
import SubmitPL from "@/model/SubmitPL";

class EvaluationService{
    submitDiagram(submitPL: SubmitPL){
        return axios.post("/api/evaluation/v2/submit", submitPL)
    }

    getSubmissionIdsByUserAndTask(userId: number, taskId: number){
        return axios.get("/api/evaluation/v2/submissionIds/" + userId + "/" + taskId)
    }
}

export default new EvaluationService();