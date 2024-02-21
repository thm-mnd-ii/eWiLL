import type User from "@/model/User";

export default function authHeader() {
  const userStorage = localStorage.getItem("user");
  let user: User | null = null;
  if (userStorage != null) {
    user = JSON.parse(userStorage);
  }

  if (user && user.token) {
    return { Authorization: "Bearer " + user.token };
  } else {
    return {};
  }
}
