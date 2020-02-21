package com.example.connectionframework.requestframework.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * This is a class which is meant to be used to deserialize JSON strings. It
 * does a generic deserialization; it essentially maps primitives to primitives,
 * lists to arrays, and objects to maps.
 * </p>
 * 
 * <p>
 * For the case of primitives, numeric values are mapped to Double objects if
 * they contain a decimal point. If not, they are mapped to Integer objects if
 * the value fits in an int, else they are mapped to a Long object. If the value
 * is boolean, it is mapped to a boolean value. Otherwise it is mapped to a
 * string, unless it is a null value, in which case it mapped to null.
 * </p>
 * 
 * <p>
 * The actual job of parsing from JSon text is done by using Google's GSon
 * library.
 * </p>
 * 
 * @author Dr. Yasar Safkan <safkan@gmail.com>
 * 
 */

public class Deserializer {

	private JsonParser jsonParser;

	/**
	 * Create a new Deserializer object.
	 */
	public Deserializer() {
		this.jsonParser = new JsonParser();
	}

	/**
	 * <p>
	 * This method parses the given JSON string to primitives, arrays and maps,
	 * recursively. No mapping to other objects is done. This is the main
	 * business method of this class.
	 * </p>
	 * 
	 * @param json
	 *            the JSON string to be deserialized.
	 * @return the object tree corresponding to the JSON string.
	 */
	public Object deserialize(String json) {
		JsonElement jsonElement = this.jsonParser.parse(json);
		return this.jsonElementToObject(jsonElement);
	}



	/**
	 * <p>
	 * This method takes a JsonElement (as generated by GSon's JsonParser) and
	 * converts that to an Object, suitable for being returned by the
	 * deserialize() method.
	 * </p>
	 * <p>
	 * This method may return a primitive wrapper object, an array, or a Map, or
	 * null, depending on the contents of the JsonElement.
	 * </p>
	 * 
	 * @param jsonElement
	 *            the JsonElement to be converted to an Object.
	 * @return the converted Object
	 */

	private Object jsonElementToObject(JsonElement jsonElement) {
		if (jsonElement.isJsonPrimitive()) {
			return jsonElementAsPrimitive(jsonElement);
		} else if (jsonElement.isJsonArray()) {
			return jsonElementAsArray(jsonElement);
		} else if (jsonElement.isJsonObject()) {
			return jsonElementAsMap(jsonElement);
		} else {
			return null;
		}
	}

	/**
	 * <p>
	 * This method converts a given JsonElement to a Map. The given JsonElement
	 * should be in the format of a Json object, or this method will have
	 * undefined behavior.
	 * </p>
	 * <p>
	 * This method calls the jsonElementToObject() method recursively to perform
	 * the conversion of items found within the Json object.
	 * </p>
	 * 
	 * @param jsonElement
	 *            the JsonElement to be converted
	 * @return the converted Object, in fact a Map.
	 */
	private Object jsonElementAsMap(JsonElement jsonElement) {
		JsonObject jsonObject = jsonElement.getAsJsonObject();

		Set<Map.Entry<String, JsonElement>> entrySet = jsonObject.entrySet();

		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();

		for (Map.Entry<String, JsonElement> entry : entrySet) {
			map.put(entry.getKey(), this.jsonElementToObject(entry.getValue()));
		}

		return map;
	}

	/**
	 * <p>
	 * This method converts a given JsonElement to an array. The given element
	 * should be in the format of a Json array, or this method will have
	 * undefined behavior.
	 * </p>
	 * <p>
	 * This method calls the jsonElementToObject() method recursively to perform
	 * the conversion of each element in the Json array.
	 * 
	 * @param jsonElement
	 *            the JsonElement to be converted
	 * @return the converted object, in fact an Object[]
	 */
	private Object jsonElementAsArray(JsonElement jsonElement) {
		JsonArray jsonArray = jsonElement.getAsJsonArray();

		int size = jsonArray.size();

		Object[] objects = new Object[size];

		for (int i = 0; i < size; i++) {
			objects[i] = this.jsonElementToObject(jsonArray.get(i));
		}

		return objects;
	}

	/**
	 * <p>
	 * This method converts a given JsonElement to a primitive type. The given
	 * element should be a Json primitive, or this method will have undefined
	 * behavior.
	 * </p>
	 * <p>
	 * This method does not perform any recursive calls; the buck stops here.
	 * </p>
	 * <p>
	 * This method is somewhat error-prone. Conversion of primitives is not an
	 * exact science, since types between Javascript and Java do not exactly
	 * match.
	 * </p>
	 * 
	 * @param jsonElement
	 *            the JsonElement to be converted.
	 * @return the converted Object, in fact a primitive wrapper type.
	 * @throws RuntimeException
	 *             if somehow the type of the primitive can not be determined.
	 *             This is not expected to happen.
	 */

	private Object jsonElementAsPrimitive(JsonElement jsonElement) {
		JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();

		if (jsonPrimitive.isString()) {
			return jsonPrimitive.getAsString();
		} else if (jsonPrimitive.isNumber()) {
			if (jsonElement.getAsString().contains(".")) {
				return jsonPrimitive.getAsDouble();
			} else {
				long number = jsonPrimitive.getAsLong();

				if (number <= (long) Integer.MAX_VALUE
						&& number >= (long) Integer.MIN_VALUE) {
					return (int) number;
				} else {
					return number;
				}
			}
		} else if (jsonPrimitive.isBoolean()) {
			return jsonPrimitive.getAsBoolean();
		} else {
			throw new RuntimeException(
					"This must not have happened. Json string is primitive, but what??? "
							+ jsonElement.getAsString());
		}
	}

}
