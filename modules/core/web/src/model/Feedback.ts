import FeedbackStatus from "@/enums/FeedbackStatus";

interface Feedback {
  id: number;
  firstName: string;
  lastName: string;
  text: string;
  status: FeedbackStatus;
  timeStamp: Date;
}

export default Feedback;
