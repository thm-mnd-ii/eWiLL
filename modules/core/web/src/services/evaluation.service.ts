import axios, { AxiosResponse } from "axios";
import SubmitPL from "@/model/SubmitPL";
import ResultLevel from "@/enums/ResultLevel";
import Result from "@/model/submission/Result";

class EvaluationService {
  submitDiagram(submitPL: SubmitPL): Promise<AxiosResponse<number>> {
    return axios.post("/api/evaluation/v2/submit", submitPL);
  }

  getSubmissionIdsByUserAndTask(userId: number, taskId: number) {
    return axios.get(
      "/api/evaluation/v2/submissionIds/" + userId + "/" + taskId
    );
  }

  getSubmissionById(
    submissionId: number,
    level: ResultLevel
  ): Promise<AxiosResponse<Result>> {
    return axios.get("/api/evaluation/v2/result/" + submissionId + "/" + level);
  }
}

export default new EvaluationService();
