import type GlobalRoles from '@/enums/GlobalRoles'

export default interface UserPL {
  id: number
  username?: string
  email?: string
  firstName?: string
  lastName?: string
  roles?: GlobalRoles[]
}
