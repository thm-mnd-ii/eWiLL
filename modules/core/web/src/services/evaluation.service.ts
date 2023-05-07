import axios from "axios";
import SubmitPL from "@/model/SubmitPL";

class EvaluationService{
    submitDiagram(submitPL: SubmitPL){
        console.log(submitPL)
        return axios.post("/api/evaluation/v2/submit", submitPL)
    }
}

export default new EvaluationService();