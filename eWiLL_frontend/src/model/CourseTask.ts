interface CourseTask {
  id: number;
  title: string;
  description: string;
  course: {
    id: number;
    title: string;
    description: string;
    tasks: CourseTask[];
  };
}

export default CourseTask;
