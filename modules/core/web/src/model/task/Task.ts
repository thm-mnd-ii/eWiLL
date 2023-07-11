import ResultLevel from "@/enums/ResultLevel";

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
    showLevel: ResultLevel;
    
  }
  
  export default Task;