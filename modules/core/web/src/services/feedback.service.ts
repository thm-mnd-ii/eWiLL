import Semester from "../model/Semester"
import axios from "axios";

class FeedbackService {

    postFeedback(feedback: string){
        return axios.post("/api/feedback/create", feedback, {headers: {"Content-Type": "text/plain"}})
    }


}

export default new FeedbackService();