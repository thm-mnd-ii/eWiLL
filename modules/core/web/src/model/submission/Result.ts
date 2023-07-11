import Comment from "./Comment"

interface Result {
    id: number;
    correct: boolean;
    score: number;
    comments: Comment[];
    submissionId: number;
  }
  
  export default Result;
  