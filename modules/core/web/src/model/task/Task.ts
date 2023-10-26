import FeedbackLevel from "@/enums/FeedbackLevel";

interface Task {
  id: number;
  name: string;
  description: string;
  dueDate: string;
  mediaType: string;
  courseId: number;
  solutionModelId: number;
  rulesetId: number;
  eliability: string;
  maxSubmissions: number;
  showLevel: FeedbackLevel;
  level: string;
}

export default Task;
