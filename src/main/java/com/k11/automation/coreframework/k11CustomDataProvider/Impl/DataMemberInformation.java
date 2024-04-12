package com.k11.automation.coreframework.k11CustomDataProvider.Impl;

import java.lang.reflect.Field;

/**
 * A Simple POJO class that represents some basic information with respect to a data member. This is internally used by
 * {@link AbstractExcelDataProvider#prepareObject(Object, Field[], java.util.List)}.
 * 
 */
class DataMemberInformation {
    private final Field field;
    private final Object userProvidedObject;
    private final Object objectToSetDataInto;
    private final String dataToUse;

    public DataMemberInformation(Field eachField, Object userObj, Object objectToReturn, String data) {
        this.field = eachField;
        this.userProvidedObject = userObj;
        this.objectToSetDataInto = objectToReturn;
        this.dataToUse = data;
    }

    /**
     * @return the field
     */
    public Field getField() {
        return field;
    }

    /**
     * @return the userProvidedObject
     */
    public Object getUserProvidedObject() {
        return userProvidedObject;
    }

    /**
     * @return the objectToSetDataInto
     */
    public Object getObjectToSetDataInto() {
        return objectToSetDataInto;
    }

    /**
     * @return the dataToUse
     */
    public String getDataToUse() {
        return dataToUse;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("DataMemberInformation [");
        if (field != null) {
            builder.append("field=");
            builder.append(field);
            builder.append(", ");
        }
        if (userProvidedObject != null) {
            builder.append("userProvidedObject=");
            builder.append(userProvidedObject);
            builder.append(", ");
        }
        if (objectToSetDataInto != null) {
            builder.append("objectToSetDataInto=");
            builder.append(objectToSetDataInto);
            builder.append(", ");
        }
        if (dataToUse != null) {
            builder.append("dataToUse=");
            builder.append(dataToUse);
        }
        builder.append("]");
        return builder.toString();
    }

}