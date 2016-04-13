package io.ridesafe.backend.parser.json

import org.springframework.boot.json.JacksonJsonParser
import org.springframework.boot.json.JsonParser

/**
 * Created by evoxmusic on 13/04/16.
 */
class MyJsonParser : JsonParser {

    val jsonParser = JacksonJsonParser()

    override fun parseList(json: String?): MutableList<Any>? = jsonParser.parseList(json)

    override fun parseMap(json: String?): MutableMap<String, Any>? = jsonParser.parseMap(json)

}