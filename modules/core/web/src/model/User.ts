import type GlobalRoles from "@/enums/GlobalRoles";

export default interface User {
  token: string;
  id: number;
  username: string;
  email: string;
  roles: GlobalRoles[];
  type: string;
}
