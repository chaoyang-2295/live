package com.huashengke.com.tools;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.*;

public class ResultSetObjectMapper<T> {

	private static Map<Class<?>, List<Field>> CLASS_FIELDS_CACHE = new HashMap<>();

	private String columnPrefix;
	private JoinOn joinOn;
	private ResultSet resultSet;
	private Class<? extends T> resultType;
	private Set<String> objectFieldSet;

	public ResultSetObjectMapper(ResultSet aResultSet, Class<T> aResultType,
                                 JoinOn aJoinOn, Set<String> objectFieldSet) {

		this(aResultSet, aResultType, aJoinOn);
		this.objectFieldSet = objectFieldSet;
	}

	public ResultSetObjectMapper(ResultSet aResultSet, Class<T> aResultType,
			JoinOn aJoinOn) {

		super();

		this.joinOn = aJoinOn;
		this.resultSet = aResultSet;
		this.resultType = aResultType;
	}

	public ResultSetObjectMapper(ResultSet aResultSet,
			Class<? extends T> aResultType, String aColumnPrefix, JoinOn aJoinOn) {

		super();

		this.columnPrefix = aColumnPrefix;
		this.joinOn = aJoinOn;
		this.resultSet = aResultSet;
		this.resultType = aResultType;
	}

	public T mapResultToType() {
		T object = this.createFrom(this.resultType());

		Set<String> associationsToMap = new HashSet<String>();

		List<Field> fields = getFields(this.resultType());

		for (Field field : fields) {
			String columnName = this.fieldNameToColumnName(field.getName());

			if (this.hasColumn(this.resultSet(), columnName)) {
				Object columnValue = this.columnValueFrom(columnName,
						field.getType());

				this.joinOn().saveCurrentLeftQualifier(columnName, columnValue);

				try {
					field.setAccessible(true);
					field.set(object, columnValue);
				} catch (Exception e) {
					throw new IllegalStateException("Cannot map to: "
							+ this.resultType.getSimpleName() + "#"
							+ columnName);
				}
			} else {
				String objectPrefix = this.toObjectPrefix(columnName);

				if (!associationsToMap.contains(objectPrefix)
						&& this.hasAssociation(this.resultSet(), objectPrefix)) {

					associationsToMap.add(field.getName());
				}
			}
		}

		if (objectFieldSet() != null) {
			for (String field : objectFieldSet()) {
				mapObjectField(object, this.resultSet(), field);
			}
		}

		if (!associationsToMap.isEmpty()) {
			this.mapAssociations(object, this.resultSet(), associationsToMap);
		}

		return object;
	}

	private Field getField(Class<?> clazz, String fieldName) {
		List<Field> fields = getFields(clazz);

		if (fields == null || fields.size() == 0)
			return null;

		for (Field field : fields) {
			if (field.getName().equals(fieldName))
				return field;
		}

		return null;
	}

	private List<Field> getFields(Class<?> clazz) {
		List<Field> cacheFields = CLASS_FIELDS_CACHE.get(clazz);
		if (cacheFields != null)
			return cacheFields;

		List<Field> fields = recursiveGetFields(this.resultType());

		CLASS_FIELDS_CACHE.put(this.resultType(), fields);
		return fields;
	}

	private List<Field> recursiveGetFields(Class<?> type) {
		Field[] fields = type.getDeclaredFields();

		Class<?> superclass = type.getSuperclass();

		if (superclass.equals(Object.class))
			return Arrays.asList(fields);

		List<Field> superFields = recursiveGetFields(superclass);

		if (superFields.size() == 0)
			return Arrays.asList(fields);

		List<Field> recFields = new ArrayList<>();
		recFields.addAll(Arrays.asList(fields));
		recFields.addAll(superFields);
		return recFields;
	}

	private void mapObjectField(T anObject, ResultSet aResultSet,
			String objectField) {
		try {
			// Field associationField = anObject.getClass().getDeclaredField(
			// objectField);

			Field associationField = getField(anObject.getClass(), objectField);

			if (associationField == null) {
				throw new NoSuchFieldException("No such field with:"
						+ objectField);
			}

			Class<?> objectFieldType = associationField.getType();

			ResultSetObjectMapper<Object> mapper = new ResultSetObjectMapper<Object>(
					aResultSet, objectFieldType,
					this.toObjectPrefix(objectField), this.joinOn());

			Object associationObject = mapper.mapResultToType();

			associationField.setAccessible(true);
			associationField.set(anObject, associationObject);
		} catch (NoSuchFieldException | SecurityException
				| IllegalArgumentException | IllegalAccessException e) {
			throw new IllegalArgumentException("Cannot map object field for "
					+ objectField + " because: " + e.getMessage(), e);
		}
	}

	private String columnPrefix() {
		return this.columnPrefix;
	}

	private boolean hasColumnPrefix() {
		return this.columnPrefix != null;
	}

	private Object columnValueFrom(String aColumnName, Class<?> aType) {

		Object value = null;
		String tempStr = null;

		try {
			String typeName = aType.getName();

			if (aType.isPrimitive()) {

				// boolean, byte, char, short, int, long, float, double

				if (typeName.equals("int")) {
					value = new Integer(this.resultSet().getInt(aColumnName));
				} else if (typeName.equals("long")) {
					value = new Long(this.resultSet().getLong(aColumnName));
				} else if (typeName.equals("boolean")) {
					int oneOrZero = this.resultSet().getInt(aColumnName);
					value = oneOrZero == 1 ? Boolean.TRUE : Boolean.FALSE;
				} else if (typeName.equals("short")) {
					value = new Short(this.resultSet().getShort(aColumnName));
				} else if (typeName.equals("float")) {
					value = new Float(this.resultSet().getFloat(aColumnName));
				} else if (typeName.equals("double")) {
					value = new Double(this.resultSet().getDouble(aColumnName));
				} else if (typeName.equals("byte")) {
					value = new Byte(this.resultSet().getByte(aColumnName));
				} else if (typeName.equals("char")) {
					String charStr = this.resultSet().getString(aColumnName);
					if (charStr == null) {
						value = new Character((char) 0);
					} else {
						value = charStr.charAt(0);
					}
				}

			} else {
				if (typeName.equals("java.lang.String")) {
					value = this.resultSet().getString(aColumnName);
				} else if (typeName.equals("java.lang.Integer")) {
					tempStr = this.resultSet().getString(aColumnName);
					value = tempStr == null ? null : Integer.parseInt(tempStr);
				} else if (typeName.equals("java.lang.Long")) {
					tempStr = this.resultSet().getString(aColumnName);
					value = tempStr == null ? null : Long.parseLong(tempStr);
				} else if (typeName.equals("java.lang.Boolean")) {
					int oneOrZero = this.resultSet().getInt(aColumnName);
					value = oneOrZero == 1 ? Boolean.TRUE : Boolean.FALSE;
				} else if (typeName.equals("java.util.Date")) {
					java.sql.Timestamp timestamp = this.resultSet()
							.getTimestamp(aColumnName);
					if (timestamp != null) {
						value = new Date(timestamp.getTime()
								+ timestamp.getNanos());
					}
				} else if (typeName.equals("java.lang.Short")) {
					tempStr = this.resultSet().getString(aColumnName);
					value = tempStr == null ? null : Short.parseShort(tempStr);
				} else if (typeName.equals("java.lang.Float")) {
					tempStr = this.resultSet().getString(aColumnName);
					value = tempStr == null ? null : Float.parseFloat(tempStr);
				} else if (typeName.equals("java.lang.Double")) {
					tempStr = this.resultSet().getString(aColumnName);
					value = tempStr == null ? null : Double
							.parseDouble(tempStr);
				} else if (aType.isEnum()) {
					Object enumObj = this.resultSet().getObject(aColumnName);
					value = enumObj == null ? null : EnumUtil.safeEnum(aType,
							enumObj);
				}
			}

		} catch (Exception e) {
			throw new IllegalArgumentException("Cannot map " + aColumnName
					+ " because: " + e.getMessage(), e);
		}

		return value;
	}

	private Collection<Object> createCollectionFrom(Class<?> aType) {

		Collection<Object> newCollection = null;

		if (List.class.isAssignableFrom(aType)) {
			newCollection = new ArrayList<Object>();
		} else if (Set.class.isAssignableFrom(aType)) {
			newCollection = new HashSet<Object>();
		}

		return newCollection;
	}

	private T createFrom(Class<? extends T> aClass) {
		T object = null;

		try {
			Constructor<? extends T> ctor = aClass.getDeclaredConstructor();

			object = ctor.newInstance();

		} catch (Exception e) {
			throw new IllegalArgumentException("Cannot create instance of: "
					+ aClass.getName());
		}

		return object;
	}

	private String fieldNameToColumnName(String aFieldName) {
		StringBuffer buf = new StringBuffer();

		if (this.hasColumnPrefix()) {
			buf.append(this.columnPrefix());
		}

		for (char ch : aFieldName.toCharArray()) {
			if (Character.isAlphabetic(ch) && Character.isUpperCase(ch)) {
				buf.append('_').append(Character.toLowerCase(ch));
			} else {
				buf.append(ch);
			}
		}

		return buf.toString();
	}

	private boolean hasAssociation(ResultSet aResultSet, String anObjectPrefix) {

		try {
			ResultSetMetaData metaData = aResultSet.getMetaData();
			int totalColumns = metaData.getColumnCount();

			for (int idx = 1; idx <= totalColumns; ++idx) {
				String columnName = metaData.getColumnLabel(idx);

				if (columnName.startsWith(anObjectPrefix)
						&& this.joinOn().isJoinedOn(aResultSet)) {

					return true;
				}
			}

		} catch (Exception e) {
			throw new IllegalStateException(
					"Cannot read result metadata because: " + e.getMessage(), e);
		}

		return false;
	}

	private boolean hasColumn(ResultSet aResultSet, String aColumnName) {
		try {
			aResultSet.findColumn(aColumnName);

			return true;

		} catch (Exception e) {
			return false;
		}
	}

	private JoinOn joinOn() {
		return this.joinOn;
	}

	private void mapAssociations(T anObject, ResultSet aResultSet,
			Set<String> anAssociationsToMap) {

		Map<String, Collection<Object>> mappedCollections = new HashMap<String, Collection<Object>>();

		String currentAssociationName = null;

		try {
			for (boolean hasResult = true; hasResult; hasResult = aResultSet
					.next()) {

				if (!this.joinOn().hasCurrentLeftQualifier(aResultSet)) {
					aResultSet.relative(-1);

					return;
				}

				for (String fieldName : anAssociationsToMap) {

					currentAssociationName = fieldName;

//					Field associationField = anObject.getClass()
//							.getDeclaredField(fieldName);
					Field associationField = getField(anObject.getClass(), fieldName);

					associationField.setAccessible(true);

					Class<?> associationFieldType = null;

					Collection<Object> collection = null;

					if (Collection.class.isAssignableFrom(associationField
							.getType())) {
						collection = mappedCollections.get(fieldName);

						if (collection == null) {
							collection = this
									.createCollectionFrom(associationField
											.getType());
							mappedCollections.put(fieldName, collection);
							associationField.set(anObject, collection);
						}

						ParameterizedType parameterizeType = (ParameterizedType) associationField
								.getGenericType();
						associationFieldType = (Class<?>) parameterizeType
								.getActualTypeArguments()[0];

					} else {
						associationFieldType = associationField.getType();
					}

					String columnName = this.fieldNameToColumnName(fieldName);

					ResultSetObjectMapper<Object> mapper = new ResultSetObjectMapper<Object>(
							aResultSet, associationFieldType,
							this.toObjectPrefix(columnName), this.joinOn());

					Object associationObject = mapper.mapResultToType();

					if (collection != null) {
						collection.add(associationObject);
					} else {
						associationField.set(anObject, associationObject);
					}
				}
			}

		} catch (Exception e) {
			throw new IllegalArgumentException(
					"Cannot map object association for "
							+ currentAssociationName + " because: "
							+ e.getMessage(), e);
		}
	}

	private ResultSet resultSet() {
		return this.resultSet;
	}

	private Class<? extends T> resultType() {
		return this.resultType;
	}

	private Set<String> objectFieldSet() {
		return this.objectFieldSet;
	}

	private String toObjectPrefix(String aColumnName) {
		String objectPrefix = "o_" + aColumnName + "_";

		return objectPrefix;
	}
}
