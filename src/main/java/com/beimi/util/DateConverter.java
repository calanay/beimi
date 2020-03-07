package com.beimi.util;

import java.util.Date;  

import org.apache.commons.beanutils.converters.DateTimeConverter;  
  
public class DateConverter extends DateTimeConverter {  
  
    public DateConverter() {  
    }  
  
    public DateConverter(Object defaultValue) {  
        super(defaultValue);  
    }  
  
    /* (non-Javadoc) 
     * @see org.apache.commons.beanutils.converters.AbstractConverter#getDefaultType() 
     */  
    @SuppressWarnings("rawtypes")  
    protected Class getDefaultType() {  
        return Date.class;  
    }  
  
    /* 
     * (non-Javadoc) 
     * @see org.apache.commons.beanutils.converters.DateTimeConverter#convertToType(java.lang.Class, java.lang.Object) 
     */  
    @SuppressWarnings("rawtypes")  
    @Override  
    protected Object convertToType(Class arg0, Object arg1) throws Exception {  
        if (arg1 == null) {  
            return null;  
        }  
        String value = arg1.toString().trim();  
        if (value.length() == 0) {  
            return null;  
        }  
        return super.convertToType(arg0, arg1);  
    }  
}  