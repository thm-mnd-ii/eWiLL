import axios from "axios";

class AuthService {
  private getUserIpAdress() {
    return axios
      .get("https://api.ipify.org?format=json")
      .then((response) => response.data.ip)
      .catch((error) => {
        console.error("Fehler beim Abrufen der IP-Adresse:", error);
        throw error;
      });
  }

  login(user: { username: string; password: string }) {
    return this.getUserIpAdress().then((ip) => {
      const config = {
        headers: {
          "X-Forwarded-For": ip,
        },
      };

      return axios
        .post(
          "/api/auth/signin",
          {
            username: user.username,
            password: user.password,
          },
          config
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
    });
  }

  tokenLogin(this_jsessionid: string) {
    return this.getUserIpAdress().then((ip) => {
      const config = {
        headers: {
          "X-Forwarded-For": ip,
        },
      };

      return axios
        .post(
          "/api/auth/tokenLogin",
          {
            jsessionid: this_jsessionid,
          },
          config
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
