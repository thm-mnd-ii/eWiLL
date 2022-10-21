package com.wipdev.eWiLL_backend.eval

data class AttributeConnection(
    var entityNameLeft: String,
    var attributeNameLeft: String,
    var entityTypeLeft : Int,
    var attributeTypeLeft: Int,
    var entityNameRight: String,
    var attributeNameRight: String,
    var entityTypeRight : Int,
    var attributeTypeRight: Int) {

    fun compare(to:AttributeConnection,flags:Array<ConnectionEvalFlag>):Boolean{
        var score = 0
        if(flags.contains(ConnectionEvalFlag.COMPARE_ENTITY_BY_NAME)){
            if(entityNameLeft == to.entityNameLeft){
                score++
            }
        }
        if(flags.contains(ConnectionEvalFlag.COMPARE_ENTITY_BY_TYPE)){
            if(entityTypeLeft == to.entityTypeLeft){
                score++
            }
        }
        if(flags.contains(ConnectionEvalFlag.COMPARE_ATTRIBUTES_BY_NAME)){
            if(attributeNameLeft == to.attributeNameLeft){
                score++
            }
        }
        if(flags.contains(ConnectionEvalFlag.COMPARE_ATTRIBUTES_BY_TYPE)){
            if(attributeTypeLeft == to.attributeTypeLeft){
                score++
            }
        }
        return score == flags.size
    }

}