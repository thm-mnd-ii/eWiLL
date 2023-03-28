import Diagram from "../diagram/Diagram"

interface Task {
    id: number;
    name: string;
    description: string;
    dueDate: string
    mediaType: string 
    courseId: number;
    solutionModel: Diagram;
    rulsesetId: number;
    liability: string;
    
  }
  
  export default Task;