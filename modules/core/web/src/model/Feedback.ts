import type FeedbackStatus from "@/enums/FeedbackStatus";

export default interface Feedback {
  id: number;
  firstName: string;
  lastName: string;
  text: string;
  status: FeedbackStatus;
  timeStamp: Date;
}
