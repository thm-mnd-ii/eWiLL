package com.wipdev.eWiLL_backend.database.tables.utils

import com.vladmihalcea.hibernate.type.json.JsonType

class JsonbUserType: JsonType() {

    override fun getName(): String {
        return "jsonb"
    }
}