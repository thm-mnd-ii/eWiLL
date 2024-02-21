import type StatusLevel from "@/enums/StatusLevel";
import type FeedbackLevel from "@/enums/FeedbackLevel";
import type MessageType from "@/enums/MessageType";

export default interface Comment {
  feedbackLevel: FeedbackLevel;
  message: string;
  resultMessageType: MessageType;
  affectedEntityId: number;
  affectedEntityName: number;
  affectedAttributeName: string;
  connectedToId: number;
  statusLevel: StatusLevel;
}
