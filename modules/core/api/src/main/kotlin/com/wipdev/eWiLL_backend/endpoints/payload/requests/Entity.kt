package com.wipdev.eWiLL_backend.endpoints.payload.requests
/*
val id: Long?,
                   val type: String?,
                   val entityName: String?,
                   val top: Int?,
                   val left: Int?,
                   val width: Int?,
                   val attributes: Array<Attribute>?
 */

class Entity {

    var id: Long? = null
    var type: Int? = null
    var entityName: String? = null
    var top: Int? = null
    var left: Int? = null
    var width: Int? = null
    var attributes: Array<Attribute>? = null


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Entity

        if (id != other.id) return false
        if (type != other.type) return false
        if (entityName != other.entityName) return false
        if (top != other.top) return false
        if (left != other.left) return false
        if (width != other.width) return false
        if (attributes != null) {
            if (other.attributes == null) return false
            if (!attributes.contentEquals(other.attributes)) return false
        } else if (other.attributes != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (type ?: 0)
        result = 31 * result + (entityName?.hashCode() ?: 0)
        result = 31 * result + (top ?: 0)
        result = 31 * result + (left ?: 0)
        result = 31 * result + (width ?: 0)
        result = 31 * result + (attributes?.contentHashCode() ?: 0)
        return result
    }
}