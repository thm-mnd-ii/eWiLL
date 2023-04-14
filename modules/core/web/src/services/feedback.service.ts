import Semester from "../model/Semester"
import axios from "axios";

class FeedbackService {

    postFeedback(feedback: string){
        return axios.post("/api/feedback/create", feedback)
    }


}

export default new FeedbackService();