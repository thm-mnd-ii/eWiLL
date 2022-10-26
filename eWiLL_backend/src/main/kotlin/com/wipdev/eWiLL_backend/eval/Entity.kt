package com.wipdev.eWiLL_backend.eval

data class Entity(
    val id:Long,
    val typ:Int,
    val entityName:String,
    val top:String,
    val left:String,
    val width:String,
    val attributes: Array<EntityAttribute>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Entity

        if (id != other.id) return false
        if (typ != other.typ) return false
        if (entityName != other.entityName) return false
        if (top != other.top) return false
        if (left != other.left) return false
        if (width != other.width) return false
        if (!attributes.contentEquals(other.attributes)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + typ
        result = 31 * result + entityName.hashCode()
        result = 31 * result + top.hashCode()
        result = 31 * result + left.hashCode()
        result = 31 * result + width.hashCode()
        result = 31 * result + attributes.contentHashCode()
        return result
    }
}
