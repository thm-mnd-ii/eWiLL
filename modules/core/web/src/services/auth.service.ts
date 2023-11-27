import axios from "axios";

class AuthService {
  
  login(user: { username: string; password: string }) {
    return axios
      .post(
        "/api/auth/signin",
        {
          username: user.username,
          password: user.password,
        }
      )
      .then((response) => {
        if (response.data.token) {
          localStorage.setItem("user", JSON.stringify(response.data));
          localStorage.setItem("role", response.data["roles"]);
          console.log("login successful");
        }
        return response.data;
      })
      .catch((error) => {
        throw error;
      });
  }


  tokenLogin(this_jsessionid: string) {
    return axios
      .post(
        "/api/auth/tokenLogin",
        {
          jsessionid: this_jsessionid,
        }
      )
      .then((response) => {
        if (response.data.token) {
          localStorage.setItem("user", JSON.stringify(response.data));
          localStorage.setItem("role", response.data["roles"]);
          console.log("login successful");
        }
        return response.data;
      })
      .catch((error) => {
        throw error;
      });
  }

  logout() {
    localStorage.removeItem("user");
    console.log("logout successful");
  }

  isValid() {
    return axios
      .get("/api/auth/isValid")
      .then((response) => {
        return response.data;
      })
      .catch((error) => {
        return error.response;
      });
  }
}

export default new AuthService();
