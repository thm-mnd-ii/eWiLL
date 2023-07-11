import HighlightLevel from "@/enums/HighlightLevel";
import ResultLevel from "@/enums/ResultLevel";

interface Comment {
    resultLevel: ResultLevel;
    message: string;
    affectedEntityId: number;
    affectedAttributeName: string;
    highlightLevel: HighlightLevel;
  }
  
  export default Comment;
  