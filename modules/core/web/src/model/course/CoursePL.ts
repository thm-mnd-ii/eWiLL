import type Semester from "../Semester"

interface CoursePL {
  id: number;
  name: string;
  description: string;
  active: boolean;
  creationDate: string;
  semester: Semester;
  owner: number;
  keyPassword: string;
  location: string;
}
  
export default CoursePL;
  