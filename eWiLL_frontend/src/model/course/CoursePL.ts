interface CoursePL {
  id: number;
  name: string;
  description: string;
  active: boolean;
  creationDate: string;
  semesterId: number;
  owner: number;
  keyPassword: string;
  location: string;
}
  
export default CoursePL;
  