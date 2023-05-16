import StatusLevel from "@/enums/StatusLevel";
import FeedbackLevel from "@/enums/FeedbackLevel";
import MessageType from "@/enums/MessageType";

interface Comment {
  feedbackLevel: FeedbackLevel;
  message: string;
  resultMessageType: MessageType;
  affectedEntityId: number;
  affectedEntityName: number;
  affectedAttributeName: string;
  connectedToId: number;
  statusLevel: StatusLevel;
}

export default Comment;
