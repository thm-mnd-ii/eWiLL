interface CoursePL {
  id: number;
  name: string;
  description: string;
  active: boolean;
  creationDate: string;
  startDate: string;
  endDate: string;
  owner: number;
  keyPassword: string;
  location: string;
}
  
export default CoursePL;
  