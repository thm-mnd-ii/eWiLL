import type Submission from "./Submission"
import type Member from "../course/Member"
import type Result from "./Result"

export default interface SubmissionListItem {
    member: Member;
    submission: Submission;
    result: Result
  }

  