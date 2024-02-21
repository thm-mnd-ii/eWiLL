export default interface CourseTask {
  id: number;
  name: string;
  description: string;
  dueDate: string;
  mediaType: string;
  courseId: number;
  solutionModelId: number;
  rulesetId: number;
}
