import type Comment from "./Comment"

export default interface Result {
    id: number;
    correct: boolean;
    score: number;
    comments: Comment[];
    submissionId: number;
  }
  