/*
 * Copyright (c) 2008-2016 Computer Network Information Center (CNIC), Chinese Academy of Sciences.
 * 
 * This file is part of Duckling project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 *
 */
package net.duckling.falcon.api.orm;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

/**
 * Simulation ORM-Mapping 如果不是遵守约定的bean 请勿用词方法 约定命名规则 javaBean DB
 * -------------------------- someClass some_class(对象名，表名) id id （主键） someField
 * some_field -------------- boolean值不允许参加查询，忽略，如果需要请用extendSQL附加，因为布尔值没有null
 * 如果想用boolean值的话，请用Boolean代替
 * 
 * 
 * 不用的字段应该用@TempField标识
 * 
 * @author lvly
 * @since 2012-08-07
 * */
public class DAOUtils<T> {
    private static final Logger LOG = Logger.getLogger(DAOUtils.class);
    private static final int DELTA_FROM_UPPER_TO_LOWER = 32;
    private static final int UPPER_START_CODE = 64;
    private static final int UPPER_END_CODE = 91;
    public static final String FIELDS = "${field}";
    public static final String TABLE = "${table}";
    public static final String VALUES = "${value}";

    public static final String INSERT_SQL = "insert into " + TABLE + "(" + FIELDS + ") values(" + VALUES + ")";
    public static final String SELECT_SQL = "select * from " + TABLE + " where 1=1";
    public static final String UPDATE_SQL = "update " + TABLE + " set " + FIELDS + " where id=:id";
    public static final String DELETE_SQL = "delete from " + TABLE + " where 1=1" + FIELDS;
    /** 需要用orm-mapping的bean */
    private Class<?> objClass;
    /** 所持有的有效mapping域 */
    private Field[] fields;

    /**
     * 额的神啊，构造方法还要注释
     * 
     * @param objClass
     *            需要使用的.class实例
     * */
    public DAOUtils(Class<?> objClass) {
        this.objClass = objClass;
        Field[] allFields = objClass.getDeclaredFields();
        List<Field> privateFields = new ArrayList<Field>();
        for (int i = 0; i < allFields.length; i++) {
            if (!Modifier.isStatic(allFields[i].getModifiers()) && Modifier.isPrivate(allFields[i].getModifiers())
                    && allFields[i].getAnnotation(TempField.class) == null) {
                privateFields.add(allFields[i]);
            }
        }
        this.fields = new Field[privateFields.size()];
        int index = 0;
        for (Field field : privateFields) {
            this.fields[index++] = field;
        }
    }

    public Class<?> getObjClass() {
        return objClass;
    }

    public void setObjClass(Class<?> objClass) {
        this.objClass = objClass;
    }

    /**
     * 获取基本的更新sql，会根据t值的主键 更新
     * 
     * */
    public String getUpdate(T t) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            Object value = getFieldValue(t, field);
            if (isNullValue(value)) {
                continue;
            }
            sb.append(getDBField(field.getName()));
            sb.append("=:");
            sb.append(field.getName());
            sb.append(",");
        }
        return UPDATE_SQL.replace(TABLE, getDBField(objClass.getSimpleName())).replace(FIELDS, format(sb));
    }

    /**
     * 获取基本的删除sql
     * 
     * @param t
     *            数据对象，会根据t属性值生成sql
     * **/
    public String getDelete(T t) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            Object value = getFieldValue(t, field);
            if (isNullValue(value)) {
                continue;
            }
            sb.append(" and ");
            sb.append(field.getName());
            sb.append("=:");
            sb.append(field.getName());
        }
        return DELETE_SQL.replace(TABLE, getDBField(objClass.getSimpleName())).replace(FIELDS, format(sb));
    }

    private String format(StringBuilder sb) {
        if (sb.indexOf(",") > 0) {
            sb.deleteCharAt(sb.lastIndexOf(","));
        }
        return sb.toString();
    }

    /***
     * 获得rowMapper对象，如果没有按照正常转换规则，用map里面的转换
     * 
     * @param Class
     *            objClass
     * @param map
     *            存的是Map<本地属性,数据库属性>,如果完全匹配，则不需要给出map
     * @return RowMapper
     * */
    public RowMapper<T> getRowMapper(final Map<String, String> map) {
        return new RowMapper<T>() {
            @SuppressWarnings("unchecked")
			@Override
            public T mapRow(ResultSet rs, int index) throws SQLException {
                Object obj = null;
                try {
                    obj = objClass.newInstance();

                    for (int i = 0; i < fields.length; i++) {

                        Field field = fields[i];
                        field.getName();
                        setValueToObj(field, obj, rs);
                    }
                } catch (InstantiationException e) {
                    LOG.debug(e.getMessage(),e);
                } catch (IllegalAccessException e) {
                    LOG.debug(e.getMessage(),e);
                }
                revertMapping(map, obj, rs);
				return (T)obj;
            }
        };
    }

    private void setValueToObj(Field field, Object obj, ResultSet rs) {
        String setName = getSetMethodName(field.getName());
        String exceptionMsg = "对象属性命名不规范";
        try {
            Method method = objClass.getMethod(setName, field.getType());
            if (isString(field)) {
                method.invoke(obj, new Object[] { rs.getString(getDBField(field.getName())) });
            } else if (isInt(field)) {
                method.invoke(obj, new Object[] { rs.getInt(getDBField(field.getName())) });
            } else if (isDate(field)) {
                method.invoke(obj, new Object[] { rs.getTimestamp(getDBField(field.getName())) });
            } else if (isBoolean(field)) {
                method.invoke(obj, new Object[] { rs.getBoolean(getDBField(field.getName())) });
            } else {
                LOG.debug("Unsupported type");
            }
        } catch (ReflectiveOperationException e) {
            LOG.debug(exceptionMsg + obj.getClass() + "@" + field, e);
        } catch (SQLException e) {
            LOG.debug(exceptionMsg + obj.getClass() + "@" + field, e);
        }
    }

    private void revertMapping(Map<String, String> map, Object obj, ResultSet rs) {
        if (map != null) {
            Set<Entry<String, String>> set = map.entrySet();
            String exceptionMsg = "对象属性命名不规范";
            for (Iterator<Entry<String, String>> it = set.iterator(); it.hasNext();) {
                Entry<String, String> entry = it.next();
                try {
                    Field field;
                    field = objClass.getDeclaredField(entry.getKey());
                    Method method = objClass.getMethod(getSetMethodName(field.getName()), field.getType());
                    if (isString(field)) {
                        method.invoke(obj, new Object[] { rs.getString(entry.getValue()) });
                    } else if (isInt(field)) {
                    	
                        method.invoke(obj, new Object[] { rs.getInt(entry.getValue()) });
                    }
                } catch (ReflectiveOperationException e) {
                    LOG.debug(exceptionMsg + entry, e);
                    continue;
                } catch (SQLException e) {
                    LOG.debug(exceptionMsg + entry, e);
                    continue;
                }
            }
        }
    }

    /**
     * 装载对象里面的数据到prepareStatment
     * 
     * @throws SQLException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * 
     * */
    public PreparedStatement setValues(PreparedStatement pst, final T t, String expectField) throws SQLException {
        int index = 0;
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            if (expectField.contains(field.getName())) {
                continue;
            }
            Object obj = null;
            String exceptionMsg = "属性名字和get方法不匹配";
            try {
                Method method = t.getClass().getMethod(getGetMethodName(field.getName()));
                obj = method.invoke(t);
            } catch (InvocationTargetException e) {
                LOG.debug(field.getName() + exceptionMsg, e);
                continue;
            } catch (NoSuchMethodException e) {
                LOG.debug(field.getName() + exceptionMsg, e);
                continue;
            } catch (SecurityException e) {
                LOG.debug(field.getName() + exceptionMsg, e);
                continue;
            } catch (IllegalAccessException e) {
                LOG.debug(field.getName() + exceptionMsg, e);
                continue;
            } catch (IllegalArgumentException e) {
                LOG.debug(field.getName() + exceptionMsg, e);
                continue;
            }
            if (isString(field)) {
                pst.setString(++index, (String) obj);
            } else if (isInt(field)) {
                pst.setInt(++index, (int) obj);
            } else {
                LOG.debug("Unsupported Type");
            }
        }

        return pst;
    }

    /**
     * 获取INSERT 语句
     * 
     * @param Class
     *            class
     * */
    public String getInsert(String expectField) {
        return INSERT_SQL.replace(TABLE, getDBField(objClass.getSimpleName()))
                .replace(FIELDS, getAllFieldStr(expectField)).replace(VALUES, getValueRepeat(expectField));
    }

    /**
     * 获取SELECT 基本语句
     * 
     * @return 返回一个基本查询语句
     * */
    public String getSelect(T t) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            Object value = getFieldValue(t, field);
            if (isNullValue(value)) {
                continue;
            }
            sb.append(" and ");
            sb.append(getDBField(field.getName()));
            sb.append("=:");
            sb.append(field.getName());
            sb.append(" ");
        }
        return SELECT_SQL.replace(TABLE, getDBField(objClass.getSimpleName())) + sb.toString();
    }
    /**
     * 从对象t里面把有值的 属性键值对取出来
     * 
     * @param t
     *            目标对象
     * @return Map<fieldKey,fieldValue>
     * 
     * **/
    public Map<String, Object> getParamMap(T t) {
    	return getParamMap(t,false);
    }

    /**
     * 从对象t里面把有值的 属性键值对取出来,建议插入的时候使用，查询时使用可能导致结果异常
     * 
     * @param t
     *            目标对象
     * @param isAll 是不是需要所有的对象，包括null的对象？
     * @return Map<fieldKey,fieldValue>
     * 
     * **/
    public Map<String, Object> getParamMap(T t,boolean isAll) {
        Map<String, Object> map = new HashMap<String, Object>();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            Object value = getFieldValue(t, field);
            if (!isAll&&isNullValue(value)) {
                continue;
            }
            if (isDate(field)&&value!=null) {
                value = new Timestamp(((Date) value).getTime());
            }else if(isBoolean(field)){
            	if(value==null){
            		value="0";
            	}else{
            		value=(((boolean)value)?"1":"0");
            	}
            	
            }
            map.put(field.getName(), value);
        }
        return map;
    }

    private Object getFieldValue(T t, Field field) {
        Object obj = null;
        String exceptionMsg = "取值异常：";
        try {
            Method method = objClass.getDeclaredMethod(getGetMethodName(field.getName()));
            if (t == null) {
                return null;
            }
            obj = method.invoke(t);
        } catch (ReflectiveOperationException e) {
            LOG.debug(exceptionMsg + field.getName());
        }
        return obj;
    }

    /**
     * 获得所有数据库字段
     * 
     * @param expectField
     *            不想出现的对象，请用","分割并传进来
     * @return String xx_xx,xx_xx
     * */
    private String getAllFieldStr(String expectField) {
        StringBuilder sb = new StringBuilder();
        for (Field field : fields) {
            if (expectField.contains(field.getName()) || isNullValue(field)) {
                continue;
            }
            sb.append(getDBField(field.getName()));
            sb.append(",");
        }
        return format(sb);
    }

    /**
     * 转换成数据库命名规则,可用于表名，或者属性名
     * 
     * @return xx_xx
     * */
    private String getDBField(String objField) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < objField.length(); i++) {
            char c = objField.charAt(i);
            if (isUpperCase(c)) {
                if (i == 0) {
                    sb.append(up2low(c));
                } else {
                    sb.append("_").append(up2low(c));
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 获得重复的":xxXx"
     * 
     * @param objClass
     * @return :xxXx,:xxXx
     * */
    private String getValueRepeat(String expectField) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            if (expectField.contains(field.getName()) || isNullValue(field)) {
                continue;
            }
            sb.append(":").append(field.getName()).append(",");
        }
        return format(sb);
    }

    private boolean isString(Field field) {
        return field.getType().equals(String.class);
    }

    private boolean isInt(Field field) {
        return field.getType().equals(int.class) || field.getType().equals(Integer.class);
    }

    private boolean isBoolean(Field field) {
        return field.getType().equals(boolean.class) || field.getType().equals(Boolean.class);
    }

    private boolean isDate(Field field) {
        return field.getType().equals(Date.class);
    }

    private boolean isUpperCase(char c) {
        return c < UPPER_END_CODE && c > UPPER_START_CODE;
    }

    private String getGetMethodName(String fieldName) {
        return "get" + low2up(fieldName.charAt(0)) + fieldName.substring(1);
    }

    private String getSetMethodName(String fieldName) {
        return "set" + low2up(fieldName.charAt(0)) + fieldName.substring(1);
    }

    private char up2low(char c) {
        return (char) (c + DELTA_FROM_UPPER_TO_LOWER);
    }

    private char low2up(char c) {
        return (char) (c - DELTA_FROM_UPPER_TO_LOWER);
    }

    private boolean isNullValue(Object obj) {
        if (obj == null) {
            return true;
        } else if (obj instanceof Integer) {
            return (int) obj == 0;
        }
        return false;
    }
}
