import type ConnectionType from './ConnectionType'
import type ElementType from './ElementType'

export default interface Syntax {
  id: number
  rules: ConnectionRule[]
}

interface ConnectionRule {
  sourceElement: ElementType
  targetElement: ElementType
  allowedConnectionTypes: ConnectionType[]
}
