import type DiagramType from '@/enums/DiagramType'
import type ElementType from '@/model/diagram/ElementType'
import type ConnectionType from '@/model/diagram/ConnectionType'
import type Syntax from '@/model/diagram/DiagramSyntax'
import type UserPL from '../UserPL'

export default interface DiagramConfig {
  id: number
  name: string
  diagramType?: DiagramType
  creationDate: Date
  lastModified: Date
  createdBy: UserPL
  elementTypes: ElementType[]
  connectionTypes: ConnectionType[]
  diagramSyntax?: Syntax
}
