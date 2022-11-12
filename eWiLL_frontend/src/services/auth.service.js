import axios from "axios";

const API_URL = "http://localhost:8080/api/auth/";

class AuthService {
  login(user) {
    return axios
      .post(API_URL + "signin", {
        username: user.username,
        password: user.password,
      })
      .then((response) => {
        if (response.data.token) {
          localStorage.setItem("user", JSON.stringify(response.data));
          console.log("login successful");
        }
        return response.data;
      });
  }

  logout() {
    localStorage.removeItem("user");
    console.log("logout successful");
  }
}

export default new AuthService();
