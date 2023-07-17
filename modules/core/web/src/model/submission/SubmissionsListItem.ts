import Submission from "./Submission"
import Member from "../course/Member"
import Result from "./Result"

interface SubmissionListItem {
    member: Member;
    submission: Submission;
    result: Result
  }
  
  export default SubmissionListItem;
  