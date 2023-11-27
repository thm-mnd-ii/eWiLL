import FeedbackLevel from "@/enums/FeedbackLevel";
import TaskLevel from "@/enums/TaskLevel";
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
  taskLevel: TaskLevel;
}

export default Task;
