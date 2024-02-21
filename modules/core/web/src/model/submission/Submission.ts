import type Diagram from "../diagram/Diagram"

export default interface Submission {
    id: number;
    userId: number;
    taskId: number;
    date: string;
    diagram: Diagram
    attempt: number;
  }

  