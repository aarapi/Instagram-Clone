package com.example.connectionframework.requestframework.json;

import android.util.Log;


import com.example.connectionframework.requestframework.constants.Constants;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class DeserializedJsonToObjectConverter {

	private Deserializer deserializer;
	private String dataPackageName;

	private Map<String, Class<?>> nominalClassMap;

	public DeserializedJsonToObjectConverter() {
		this.deserializer = new Deserializer();
		this.nominalClassMap = new HashMap<String, Class<?>>();
	}

	public void setDataPackageName(String dataPackageName) {
		this.dataPackageName = dataPackageName;
	}

	/**
	 * <p>
	 * This method registers clazz and all classes covered by clazz. Programmer
	 * does not have to register all classes covered by clazz one by one to
	 * converting the class from json string.
	 * </p>
	 * 
	 * @param clazz
	 *            is the class which we want to register its and all its covered
	 *            classes.
	 */
	public void registerAllClasses(Class<?> clazz) {

		this.registerClass(clazz);
		List<Field> fields = getDeclearedFields(clazz);

		for (Field field : fields) {

			Class<?> fieldType = field.getType();

			if ((!fieldType.isPrimitive()) && (!fieldType.isArray())) {
				if (!nominalClassMap.containsKey(fieldType.getCanonicalName())
						&& !fieldType.getCanonicalName().startsWith("java")) {
					this.registerAllClasses(fieldType);
				}
			}
		}
	}

	/**
	 * <p>
	 * This method adds record of only clazz.
	 * </p>
	 * 
	 * @param clazz
	 *            is the class to which we want to register.
	 */

	public void registerClass(Class<?> clazz) {
		this.nominalClassMap.put(clazz.getCanonicalName(), clazz);
	}

	/**
	 * <p>
	 * This method delete record of only the class.
	 * </p>
	 * 
	 * @param clazz
	 *            is the class which we want to delete its record.
	 */
	public void unregisterClass(Class<?> clazz) {
		this.nominalClassMap.remove(clazz.getCanonicalName());
	}

	/**
	 * <p>
	 * This method delete record of all classes covered by clazz.
	 * </p>
	 * <p>
	 * This method doesn't check covered classes are used by any class which are
	 * registered so you must be sure covered classes not necessary for other
	 * registered class.
	 * </p>
	 * 
	 * @param clazz
	 *            is the class which we want to delete its and all its covered
	 *            class.
	 */

	public void unregisterAllClass(Class<?> clazz) {

		this.unregisterClass(clazz);

		Field[] fields = clazz.getDeclaredFields();

		for (Field field : fields) {

			Class<?> fieldType = field.getType();

			if ((!fieldType.isPrimitive()) && (!fieldType.isArray())) {

				if (nominalClassMap.containsKey(fieldType.getCanonicalName())
						&& !fieldType.getCanonicalName().startsWith("java")) {
					this.unregisterClass(fieldType);
				}
			}
		}

		this.unregisterAllClass(clazz);
	}

	/**
	 * <p>
	 * This method registers clazz and convert json to object. Than, it converts
	 * json string to object
	 * </p>
	 * <p>
	 * If a class covered by clazz is not registered, json can not be convert
	 * specific object.
	 * </p>
	 * <p>
	 * If json can not be converted to object, the method returns null.
	 * </p>
	 * 
	 * @param json
	 *            serialized string which we want to convert to object.
	 * @param clazz
	 *            is a class which we want to add record. This class mostly the
	 *            target object class for the json string.
	 * @return converted object from json.
	 */

	public Object registerClassAndConvertJsonToObject(String json,
			Class<?> clazz) {
		this.registerClass(clazz);
		return convertJsonToObject(json);
	}

	/**
	 * <p>
	 * This method registers clazz and all its covered classes.Than convert json
	 * string to object.
	 * </p>
	 * <p>
	 * This method guaranties registering all classes covered by clazz. If json
	 * can not be converted to object, the method returns null.
	 * </p>
	 * 
	 * @param json
	 *            serialized string which we want to convert to object.
	 * @param clazz
	 *            is a class which we want to add record. This class mostly is
	 *            the target objects class for the json string.
	 * @return converted object from json.
	 */
	public Object registerAllClassReferencesAndConvertJsonToObject(String json,
			Class<?> clazz) {
		this.registerAllClasses(clazz);
		return convertJsonToObject(json);
	}

	/**
	 * <p>
	 * This method converts serialized json string to object.
	 * </p>
	 * 
	 * @param json
	 *            is serialized string which we want to convert object.
	 * @return converted object from json.
	 */
	public Object convertJsonToObject(String json) {
		if (Constants.DEBUG == true)
			Log.v("LOGGER", json);
		Object deserializedJson = deserializer.deserialize(json);
		return convertDeserializedJsonToObject(deserializedJson);
	}

	/**
	 * 

	 */
	public Object convertDeserializedJsonToObject(Object deserializedJson)
			throws JsonToObjectConversionException {
		try {
			return this.performConvertDeserializedJsonToObject(
					deserializedJson, null);
		} catch (Exception e) {
			throw new JsonToObjectConversionException(e);
		}
	}

	@SuppressWarnings("unused")
	private String mapToString(Map<String, Object> map) {
		StringBuilder buf = new StringBuilder();

		buf.append("\n");

		for (Entry<String, Object> entry : map.entrySet()) {
			buf.append(entry.getKey() + " : " + entry.getValue().toString());
			buf.append("\n");
		}

		return buf.toString();
	}

	private String removeAndExtractBareClassNameFromMap(Map<String, Object> map) {
		return (String) map.remove("__type");
	}

	private Object performConvertDeserializedJsonToObject(
			Object deserializedJson, Class<?> expectedClass)
			throws ClassNotFoundException, SecurityException,
			IllegalArgumentException, NoSuchMethodException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException {
		if (deserializedJson == null) {
			return null;
		} else if (deserializedJson instanceof Map<?, ?>) {
			@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>) deserializedJson;

			if (expectedClass == null) {

				String bareClassName = this
						.removeAndExtractBareClassNameFromMap(map);

				// Class<?> clazz = this.findBestMatchingClass(map,
				// bareClassName);
				Class<?> clazz = null;
				try {
					clazz = Class
							.forName("com.bkt.messagingframework.messaging."
									+ bareClassName);
				} catch (Exception ex) {
				}
				if (clazz == null) {
					try {
						clazz = Class.forName("com.bkt.mobile.data."
								+ bareClassName);
					} catch (Exception ex) {
					}
				}

				if (clazz == null) {// Means plain dictionary not an object
					LinkedHashMap<String, Object> resultMap = new LinkedHashMap<String, Object>();

					for (Entry<String, Object> entry : map.entrySet()) {
						Object value = performConvertDeserializedJsonToObject(
								entry.getValue(), null);
						resultMap.put(entry.getKey(), value);
					}
					return resultMap;
					// throw new
					// ClassNotFoundException("Can not find class in registered classes to convert given map into."
					// + mapToString(map));
				}
				return convertMapToObject(map, clazz);

			} else {
				return convertMapToObject(map, expectedClass);
			}

		} else if (deserializedJson.getClass().isArray()) {
			Object[] objects = (Object[]) deserializedJson;
			Object[] array = new Object[objects.length];

			for (int i = 0; i < objects.length; i++) {
				array[i] = this.performConvertDeserializedJsonToObject(
						objects[i], null);
			}

			return array;
		} else {
			return deserializedJson;
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Object convertMapToObject(Map<String, Object> map, Class<?> clazz)
			throws SecurityException, NoSuchMethodException,
			IllegalArgumentException, InstantiationException,
			IllegalAccessException, InvocationTargetException,
			ClassNotFoundException {
		Object object = this.instantiateObject(clazz);

		if (object instanceof HashMap) {

			Map objectMap = (Map) object;

			for (Entry entry : map.entrySet()) {
				Object deserializedKey = entry.getKey();
				Object deserializedValue = map.get(deserializedKey);
				Object key = performConvertDeserializedJsonToObject(
						deserializedKey, null);
				Object value = performConvertDeserializedJsonToObject(
						deserializedValue, null);

				objectMap.put(key, value);
			}

			return objectMap;
		} else {

			List<Field> fields = getDeclearedFields(clazz);

			for (Field field : fields) {
				this.assignFieldFromMap(object, field, map);
			}
		}

		return object;
	}

	private void assignFieldFromMap(Object object, Field field,
			Map<String, Object> map) throws ClassNotFoundException,
			IllegalArgumentException, IllegalAccessException,
			SecurityException, NoSuchMethodException, InstantiationException,
			InvocationTargetException {

		Object deserializedJson = map.get(field.getName());

		if (deserializedJson == null) {
			return;
		} else {
			field.setAccessible(true);

			Object value = null;
			/*
			 * Because json conversion of date is problematic, we used different
			 * conversion methodology. First incoming json string is in the form
			 * of string that contains date information. In here, if the type of
			 * expected field is Date, we get the corresponding the formatted
			 * json string. Then by the convertStringToDate method we create a
			 * Date object with incoming value of formatted string and set it to
			 * the related field.
			 */
			if (field.getType() == java.util.Date.class) {
				Object jsonValue = this.performConvertDeserializedJsonToObject(
						deserializedJson, null);
				value = convertStringToDate((String) jsonValue);
			} else {
				value = this.performConvertDeserializedJsonToObject(
						deserializedJson, field.getType());
			}

			// Object value =
			// this.performConvertDeserializedJsonToObject(deserializedJson);

			Class<?> targetClass = field.getType();

			if (targetClass.isPrimitive()
					|| targetClass.isAssignableFrom(value.getClass())) {
				try {
					field.set(object, value);
				} catch (IllegalArgumentException e) {
					// Log.e("F",
					// "Field assignment failed. Field: "
					// + field.getName() + " targetClass: "
					// + targetClass.getCanonicalName()
					// + " value class:"
					// + value.getClass().getCanonicalName()
					// + " value:" + value);
					throw e;
				}
			} else {
				if (value instanceof Object[]) {
					Object[] objects = (Object[]) value;

					if (targetClass.isAssignableFrom(ArrayList.class)) {
						ArrayList<Object> arrayList = new ArrayList<Object>();

						for (Object o : objects) {
							arrayList.add(o);
						}

						field.set(object, arrayList);
					}
				} else {
					throw new JsonToObjectConversionException(
							"Assignment exception: can not assign "
									+ targetClass.getCanonicalName() + " from "
									+ value.getClass().getCanonicalName());
				}
			}

		}
	}

	private static java.util.Date convertStringToDate(String jsonValue) {
		/**
		 * Coming format: /Date(1322819036700)/
		 * 
		 */
		String dateString = jsonValue.substring(6, jsonValue.length() - 2);
		long miliseconds = Long.valueOf(dateString);
		return new java.util.Date(miliseconds);
	}

	/**
	 * <p>
	 * This method calculates the matching score from a Map to a class. The
	 * matching score is always between 0.0 and 1.0. If the map contains any key
	 * that does not match to a field name within the class, the score is
	 * automatically zero. If all keys in the map match field names in the
	 * class, the matching score is the number of keys in the map divided by the
	 * total number of fields in the class; that is, it is a measure of how well
	 * the map fills the fields in the class. Thus, a perfect match will result
	 * in a score of 1.0.
	 * </p>
	 * 
	 * <p>
	 * No type checking is performed here for the types of the fields in the
	 * object. It is entirely possible that the map can not be actually
	 * converted to the given class type when it is attempted.
	 * </p>
	 * 
	 * @param clazz
	 *            the class to which we want to convert the map
	 * @param map
	 *            the map to be converted
	 * @return a matching score between 0.0 and 1.0, 1.0 indicating a perfect
	 *         match.
	 */

	private static double mapToClassMatchScore(Class<?> clazz,
			Map<String, Object> map) {

		List<Field> fieldList = getDeclearedFields(clazz);

		int matches = 0;

		for (Field field : fieldList) {
			if (map.containsKey(field.getName())) {
				matches++;
			}
		}

		// Log.d("F", "mapToClassMatchScore class: " + clazz.getCanonicalName()
		// + " matches: " + matches + " map size: " + map.size());

		if (matches < map.size()) {
			// This means we could not cover the map, thus it is a score of 0.0
			return 0.0;
		} else {
			// This means the map is covered. Return the proper score.
			return (double) map.size() / (double) fieldList.size();
		}

	}

	/**
	 * Finds the best matching class for a given map. Returns null if no match
	 * is found. It is possible to supply a nominal class name, in which case
	 * this method will try to match it first, looking at "certain"
	 * (regrettably, hard-coded) packages. If the nominal class is a match at
	 * all (i.e., the map does not contain impossible-to-insert entries) it will
	 * be returned. Otherwise, the method will look for a best match within
	 * registered classes.
	 * 
	 * @param map
	 *            the map for which we seek a matching class
	 * @param nominalClassName
	 *            a nominal class name
	 * @return the best matching class, or null if not found
	 */
	@SuppressWarnings("unused")
	private Class<?> findBestMatchingClass(Map<String, Object> map,
			String nominalClassName) {

		Class<?> nominalClass;

		nominalClass = findNominalClass(nominalClassName);

		if (nominalClass != null) {
			double score = mapToClassMatchScore(nominalClass, map);

			if (score > 0.0) {
				return nominalClass;
			}
		}

		double maxScore = 0.0;
		Class<?> bestMatch = null;

		for (Class<?> clazz : this.nominalClassMap.values()) {
			double score = mapToClassMatchScore(clazz, map);
			if (score > maxScore) {
				maxScore = score;
				bestMatch = clazz;
			}
		}

		return bestMatch;
	}

	// -----------------------------------

	private static List<Field> getDeclearedFields(Class<?> clazz) {

		Type superClass = clazz.getSuperclass();
		List<Field> fieldList = new ArrayList<Field>();
		addAllFieldsToList(fieldList, clazz.getDeclaredFields());

		if (!superClass.toString().contains("java")) {
			List<Field> superClassField = getDeclearedFields(((Class<?>) superClass));
			fieldList.addAll(superClassField);
		}

		return fieldList;
	}

	private static void addAllFieldsToList(List<Field> list, Field[] array) {
		for (Field element : array) {
			list.add(element);
		}
	}

	private Class<?> findNominalClass(String nominalClassName) {
		String fullyQualifiedClassName = dataPackageName + "."
				+ nominalClassName;

		if (this.nominalClassMap.containsKey(fullyQualifiedClassName)) {
			return this.nominalClassMap.get(fullyQualifiedClassName);
		} else {
			try {
				Class<?> nominalClass = Class.forName(fullyQualifiedClassName);
				this.nominalClassMap.put(fullyQualifiedClassName, nominalClass);
				return nominalClass;
			} catch (ClassNotFoundException e) {
				// Log.w("F", "Nominal class not found: "
				// + fullyQualifiedClassName);
				return null;
			}
		}
	}

	private Object instantiateObject(Class<?> clazz)
			throws NoSuchMethodException, InstantiationException,
			IllegalAccessException, InvocationTargetException {

		Constructor<?> constructor = clazz.getConstructor((Class[]) null);
		constructor.setAccessible(true);
		return constructor.newInstance();
	}

}
