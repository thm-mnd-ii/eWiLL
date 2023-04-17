import GlobalRoles from "../enums/GlobalRoles";

interface User {
  token: string;
  id: number;
  username: string;
  email: string;
  roles: GlobalRoles[];
  type: string;
}

export default User;
