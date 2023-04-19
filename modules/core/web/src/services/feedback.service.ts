import Feedback from "@/model/Feedback";
import axios, { AxiosResponse } from "axios";
import authHeader from "./auth-header";
import FeedbackStatus from "@/enums/FeedbackStatus";

class FeedbackService {
  updateFeedback(feedback: Feedback): Promise<AxiosResponse<Feedback>> {
    return axios.post("/api/feedback/update/" + feedback.id, feedback, {
      headers: authHeader(),
    });
  }

  getFeedbacks(): Promise<AxiosResponse<Feedback[]>> {
    return axios.post("/api/feedback/get", { headers: authHeader() });
  }

  createFeedback(feedback: Feedback): Promise<AxiosResponse<Feedback>> {
    return axios.post("/api/feedback/create", feedback, {
      headers: authHeader(),
    });
  }

  getStatuses(): Promise<AxiosResponse<FeedbackStatus[]>> {
    return axios.get("/api/feedback/statuses", { headers: authHeader() });
  }

  deleteFeedback(id: number) {
    return axios.post("/api/feedback/delete/" + id, { headers: authHeader() });
  }
}

export default new FeedbackService();
