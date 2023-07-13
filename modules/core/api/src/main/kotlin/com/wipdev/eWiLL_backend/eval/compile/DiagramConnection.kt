package com.wipdev.eWiLL_backend.eval.compile


class DiagramConnection(

    var from: DiagramNode,
    var to: DiagramNode,
    var type: Int
) {


    init {
        from.connections += this
        to.connections += this
    }
}

