import axios from "axios";
import authHeader from "./auth-header";


class UserService {
  getPublicContent() {
    return axios.get("/api/test/all");
  }

  getUserBoard() {
    return axios.get("/api/test/user", { headers: authHeader() });
  }
}

export default new UserService();
