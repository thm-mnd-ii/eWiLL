package com.wipdev.eWiLL_backend.eval

import java.text.FieldPosition

data class AnkerPoint(
    var startEntity : Long,
    var endEntity : Long,
    var startEntityPosition: String,
    var endEntityPosition: String,
    var style:Int
)
