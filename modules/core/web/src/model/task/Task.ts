import type FeedbackLevel from "@/enums/FeedbackLevel";
import type TaskLevel from "@/enums/TaskLevel";

export default interface Task {
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
