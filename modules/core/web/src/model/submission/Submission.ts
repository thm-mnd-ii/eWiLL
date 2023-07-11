import Diagram from "../diagram/Diagram"

interface Submission {
    id: number;
    userId: number;
    taskId: number;
    date: string;
    diagram: Diagram
    attempt: number;
  }
  
  export default Submission;
  