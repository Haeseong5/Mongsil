package com.cashproject.mongsil.extension
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.serializer
import java.lang.reflect.Type

@OptIn(ExperimentalSerializationApi::class)
val json = Json {
    ignoreUnknownKeys = true
    isLenient = true
    explicitNulls = false
    coerceInputValues = true
    encodeDefaults = true
    allowStructuredMapKeys = true
    classDiscriminator = "classDiscriminator"
}

fun String.toJsonElement(): JsonElement {
    return json.parseToJsonElement(this)
}

inline fun <reified T : Any> T.toJson(): String {
    return json.encodeToString(this)
}

@OptIn(ExperimentalSerializationApi::class)
inline fun <reified T : Any> String.fromJson(): T {
    return json.decodeFromString(this)
}

@OptIn(ExperimentalSerializationApi::class)
inline fun <reified T : Any> JsonElement.fromJson(): T {
    return json.decodeFromJsonElement(this)
}

@Suppress("UNCHECKED_CAST")
@OptIn(ExperimentalSerializationApi::class)
fun <T : Any> String.fromJson(classType: Class<T>): T? {
    val deserializer = serializer(classType as Type)
    return json.decodeFromString(deserializer, this) as? T
}

@Suppress("UNCHECKED_CAST")
@OptIn(ExperimentalSerializationApi::class)
fun <T : Any> String.fromJson(type: Type): T? {
    val deserializer = serializer(type)
    return json.decodeFromString(deserializer, this) as? T
}

@Suppress("UNCHECKED_CAST")
@OptIn(ExperimentalSerializationApi::class)
fun <T : Any> JsonElement.fromJson(classType: Class<T>): T? {
    val deserializer = serializer(classType)
    return json.decodeFromJsonElement(deserializer, this) as? T
}

@Suppress("UNCHECKED_CAST")
@OptIn(ExperimentalSerializationApi::class)
fun <T : Any> JsonElement.fromJson(type: Type): T? {
    val deserializer = serializer(type)
    return json.decodeFromJsonElement(deserializer, this) as? T
}