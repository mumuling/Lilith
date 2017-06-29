package com.youloft.lilith.common.provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.youloft.lilith.AppConfig;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 设置存储器
 * Created by coder on 2017/6/23.
 */
public class SettingProvider extends ContentProvider {
    private static final String TAG = "SPHelperImpl";
    public static final String COMMA_REPLACEMENT = "__COMMA__";
    private static final String MAINSPNAME = "setting_preferences";
    // normal constants
    public static final String CONTENT = "content://";
    public static final String AUTHORITY = AppConfig.Bundle;
    public static final String SEPARATOR = "/";
    public static final String CONTENT_URI = CONTENT + AUTHORITY;
    public static final String TYPE_STRING_SET = "string_set";
    public static final String TYPE_STRING = "string";
    public static final String TYPE_INT = "int";
    public static final String TYPE_LONG = "long";
    public static final String TYPE_FLOAT = "float";
    public static final String TYPE_BOOLEAN = "boolean";
    public static final String VALUE = "value";

    public static final String NULL_STRING = "null";
    public static final String TYPE_CONTAIN = "contain";
    public static final String TYPE_CLEAN = "clean";
    public static final String TYPE_GET_ALL = "get_all";

    public static final String CURSOR_COLUMN_NAME = "cursor_name";
    public static final String CURSOR_COLUMN_TYPE = "cursor_type";
    public static final String CURSOR_COLUMN_VALUE = "cursor_value";


    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        String[] path = uri.getPath().split(SEPARATOR);
        String type = path[1];
        if (type.equals(TYPE_GET_ALL)) {
            Map<String, ?> all = _getAll(getContext());
            if (all != null) {
                MatrixCursor cursor = new MatrixCursor(new String[]{CURSOR_COLUMN_NAME, CURSOR_COLUMN_TYPE, CURSOR_COLUMN_VALUE});
                Set<String> keySet = all.keySet();
                for (String key : keySet) {
                    Object[] rows = new Object[3];
                    rows[0] = key;
                    rows[2] = all.get(key);
                    if (rows[2] instanceof Boolean) {
                        rows[1] = TYPE_BOOLEAN;
                    } else if (rows[2] instanceof String) {
                        rows[1] = TYPE_STRING;
                    } else if (rows[2] instanceof Integer) {
                        rows[1] = TYPE_INT;
                    } else if (rows[2] instanceof Long) {
                        rows[1] = TYPE_LONG;
                    } else if (rows[2] instanceof Float) {
                        rows[1] = TYPE_FLOAT;
                    }
                    cursor.addRow(rows);
                }
                return cursor;
            }
        }
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        // 用这个来取数值
        String[] path = uri.getPath().split(SEPARATOR);
        String type = path[1];
        String key = path[2];
        if (type.equals(TYPE_CONTAIN)) {
            return _contains(getContext(), key) + "";
        }
        return "" + get(getContext(), key, type);
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        String[] path = uri.getPath().split(SEPARATOR);
        String type = path[1];
        String key = path[2];
        Object obj = (Object) values.get(VALUE);
        if (obj != null)
            _save(getContext(), key, obj);
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        String[] path = uri.getPath().split(SEPARATOR);
        String type = path[1];
        if (type.equals(TYPE_CLEAN)) {
            _clear(getContext());
            return 0;
        }
        String key = path[2];
        if (_contains(getContext(), key)) {
            _remove(getContext(), key);
        }
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        insert(uri, values);
        return 0;
    }


    private static SharedPreferences getSP(Context context) {
        if (context == null) {
            return null;
        }
        return context.getSharedPreferences(MAINSPNAME, Context.MODE_PRIVATE);
    }

    private static SoftReference<Map<String, Object>> sCacheMap;

    private static Object _getCachedValue(String name) {
        if (sCacheMap != null) {
            Map<String, Object> map = sCacheMap.get();
            if (map != null) {
                return map.get(name);
            }
        }
        return null;
    }

    private static void _setValueToCached(String name, Object value) {
        Map<String, Object> map;
        if (sCacheMap == null) {
            map = new HashMap<>();
            sCacheMap = new SoftReference<Map<String, Object>>(map);
        } else {
            map = sCacheMap.get();
            if (map == null) {
                map = new HashMap<>();
                sCacheMap = new SoftReference<Map<String, Object>>(map);
            }
        }
        map.put(name, value);
    }

    private static void _cleanCachedValue() {
        Map<String, Object> map;
        if (sCacheMap != null) {
            map = sCacheMap.get();
            if (map != null) {
                map.clear();
            }
        }
    }


    synchronized static <T> void _save(Context context, String name, T t) {
        SharedPreferences sp = getSP(context);
        if (sp == null) return;

        if (t.equals(_getCachedValue(name))) {
            return;
        }
        SharedPreferences.Editor editor = sp.edit();
        if (t instanceof Boolean) {
            editor.putBoolean(name, (Boolean) t);
        }
        if (t instanceof String) {
            editor.putString(name, (String) t);
        }
        if (t instanceof Integer) {
            editor.putInt(name, (Integer) t);
        }
        if (t instanceof Long) {
            editor.putLong(name, (Long) t);
        }
        if (t instanceof Float) {
            editor.putFloat(name, (Float) t);
        }
        editor.commit();
        _setValueToCached(name, t);
    }

    static String get(Context context, String name, String type) {
        Object value = _getCachedValue(name);
        if (value != null) {
            return value + "";
        } else {
            value = get_impl(context, name, type);
            _setValueToCached(name, value);
            return value + "";
        }
    }

    private static Object get_impl(Context context, String name, String type) {
        if (!_contains(context, name)) {
            return null;
        } else {
            if (type.equalsIgnoreCase(TYPE_STRING)) {
                return _getString(context, name, null);
            } else if (type.equalsIgnoreCase(TYPE_BOOLEAN)) {
                return _getBoolean(context, name, false);
            } else if (type.equalsIgnoreCase(TYPE_INT)) {
                return _getInt(context, name, 0);
            } else if (type.equalsIgnoreCase(TYPE_LONG)) {
                return _getLong(context, name, 0L);
            } else if (type.equalsIgnoreCase(TYPE_FLOAT)) {
                return _getFloat(context, name, 0f);
            } else if (type.equalsIgnoreCase(TYPE_STRING_SET)) {
                return _getString(context, name, null);
            }
            return null;
        }
    }

    static String _getString(Context context, String name, String defaultValue) {
        SharedPreferences sp = getSP(context);
        if (sp == null) return defaultValue;
        return sp.getString(name, defaultValue);
    }

    static int _getInt(Context context, String name, int defaultValue) {
        SharedPreferences sp = getSP(context);
        if (sp == null) return defaultValue;
        return sp.getInt(name, defaultValue);
    }

    static float _getFloat(Context context, String name, float defaultValue) {
        SharedPreferences sp = getSP(context);
        if (sp == null) return defaultValue;
        return sp.getFloat(name, defaultValue);
    }

    static boolean _getBoolean(Context context, String name, boolean defaultValue) {
        SharedPreferences sp = getSP(context);
        if (sp == null) return defaultValue;
        return sp.getBoolean(name, defaultValue);
    }

    static long _getLong(Context context, String name, long defaultValue) {
        SharedPreferences sp = getSP(context);
        if (sp == null) return defaultValue;
        return sp.getLong(name, defaultValue);
    }

    static boolean _contains(Context context, String name) {
        SharedPreferences sp = getSP(context);
        if (sp == null) return false;
        return sp.contains(name);
    }

    static void _remove(Context context, String name) {
        SharedPreferences sp = getSP(context);
        if (sp == null) return;
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(name);
        editor.commit();
    }

    static void _clear(Context context) {
        SharedPreferences sp = getSP(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
        _cleanCachedValue();
    }

    static Map<String, ?> _getAll(Context context) {
        SharedPreferences sp = getSP(context);
        return sp.getAll();
    }


    public synchronized static void save(Context context, String name, Boolean t) {
        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.parse(CONTENT_URI + SEPARATOR + TYPE_BOOLEAN + SEPARATOR + name);
        ContentValues cv = new ContentValues();
        cv.put(VALUE, t);
        cr.update(uri, cv, null, null);
    }

    public synchronized static void save(Context context, String name, String t) {
        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.parse(CONTENT_URI + SEPARATOR + TYPE_STRING + SEPARATOR + name);
        ContentValues cv = new ContentValues();
        cv.put(VALUE, t);
        cr.update(uri, cv, null, null);
    }

    public synchronized static void save(Context context, String name, Integer t) {
        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.parse(CONTENT_URI + SEPARATOR + TYPE_INT + SEPARATOR + name);
        ContentValues cv = new ContentValues();
        cv.put(VALUE, t);
        cr.update(uri, cv, null, null);
    }

    public synchronized static void save(Context context, String name, Long t) {
        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.parse(CONTENT_URI + SEPARATOR + TYPE_LONG + SEPARATOR + name);
        ContentValues cv = new ContentValues();
        cv.put(VALUE, t);
        cr.update(uri, cv, null, null);
    }

    public synchronized static void save(Context context, String name, Float t) {
        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.parse(CONTENT_URI + SEPARATOR + TYPE_BOOLEAN + SEPARATOR + name);
        ContentValues cv = new ContentValues();
        cv.put(VALUE, t);
        cr.update(uri, cv, null, null);
    }


    public synchronized static void save(Context context, String name, Set<String> t) {
        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.parse(CONTENT_URI + SEPARATOR + TYPE_STRING_SET + SEPARATOR + name);
        ContentValues cv = new ContentValues();
        Set<String> convert = new HashSet<>();
        for (String string : t) {
            convert.add(string.replace(",", COMMA_REPLACEMENT));
        }
        cv.put(VALUE, convert.toString());
        cr.update(uri, cv, null, null);
    }

    public static String getString(Context context, String name, String defaultValue) {
        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.parse(CONTENT_URI + SEPARATOR + TYPE_STRING + SEPARATOR + name);
        String rtn = cr.getType(uri);
        if (rtn == null || rtn.equals(NULL_STRING)) {
            return defaultValue;
        }
        return rtn;
    }

    public static int getInt(Context context, String name, int defaultValue) {
        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.parse(CONTENT_URI + SEPARATOR + TYPE_INT + SEPARATOR + name);
        String rtn = cr.getType(uri);
        if (rtn == null || rtn.equals(NULL_STRING)) {
            return defaultValue;
        }
        return Integer.parseInt(rtn);
    }

    public static float getFloat(Context context, String name, float defaultValue) {
        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.parse(CONTENT_URI + SEPARATOR + TYPE_FLOAT + SEPARATOR + name);
        String rtn = cr.getType(uri);
        if (rtn == null || rtn.equals(NULL_STRING)) {
            return defaultValue;
        }
        return Float.parseFloat(rtn);
    }

    public static boolean getBoolean(Context context, String name, boolean defaultValue) {
        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.parse(CONTENT_URI + SEPARATOR + TYPE_BOOLEAN + SEPARATOR + name);
        String rtn = cr.getType(uri);
        if (rtn == null || rtn.equals(NULL_STRING)) {
            return defaultValue;
        }
        return Boolean.parseBoolean(rtn);
    }

    public static long getLong(Context context, String name, long defaultValue) {
        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.parse(CONTENT_URI + SEPARATOR + TYPE_LONG + SEPARATOR + name);
        String rtn = cr.getType(uri);
        if (rtn == null || rtn.equals(NULL_STRING)) {
            return defaultValue;
        }
        return Long.parseLong(rtn);
    }

    public static Set<String> getStringSet(Context context, String name, Set<String> defaultValue) {
        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.parse(CONTENT_URI + SEPARATOR + TYPE_STRING_SET + SEPARATOR + name);
        String rtn = cr.getType(uri);
        if (rtn == null || rtn.equals(NULL_STRING)) {
            return defaultValue;
        }
        if (!rtn.matches("\\[.*\\]")) {
            return defaultValue;
        }
        String sub = rtn.substring(1, rtn.length() - 1);
        String[] spl = sub.split(", ");
        Set<String> returns = new HashSet<>();
        for (String t : spl) {
            returns.add(t.replace(COMMA_REPLACEMENT, ", "));
        }
        return returns;
    }

    public static boolean contains(Context context, String name) {
        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.parse(CONTENT_URI + SEPARATOR + TYPE_CONTAIN + SEPARATOR + name);
        String rtn = cr.getType(uri);
        if (rtn == null || rtn.equals(NULL_STRING)) {
            return false;
        } else {
            return Boolean.parseBoolean(rtn);
        }
    }

    public static void remove(Context context, String name) {
        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.parse(CONTENT_URI + SEPARATOR + TYPE_LONG + SEPARATOR + name);
        cr.delete(uri, null, null);
    }

    public static void clear(Context context) {
        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.parse(CONTENT_URI + SEPARATOR + TYPE_CLEAN);
        cr.delete(uri, null, null);
    }

    public static Map<String, ?> getAll(Context context) {
        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.parse(CONTENT_URI + SEPARATOR + TYPE_GET_ALL);
        Cursor cursor = cr.query(uri, null, null, null, null);
        HashMap resultMap = new HashMap();
        if (cursor != null && cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex(CURSOR_COLUMN_NAME);
            int typeIndex = cursor.getColumnIndex(CURSOR_COLUMN_TYPE);
            int valueIndex = cursor.getColumnIndex(CURSOR_COLUMN_VALUE);
            do {
                String key = cursor.getString(nameIndex);
                String type = cursor.getString(typeIndex);
                Object value = null;
                if (type.equalsIgnoreCase(TYPE_STRING)) {
                    value = cursor.getString(valueIndex);
                    if (((String) value).contains(COMMA_REPLACEMENT)) {
                        String str = (String) value;
                        if (str.matches("\\[.*\\]")) {
                            String sub = str.substring(1, str.length() - 1);
                            String[] spl = sub.split(", ");
                            Set<String> returns = new HashSet<>();
                            for (String t : spl) {
                                returns.add(t.replace(COMMA_REPLACEMENT, ", "));
                            }
                            value = returns;
                        }
                    }
                } else if (type.equalsIgnoreCase(TYPE_BOOLEAN)) {
                    value = cursor.getString(valueIndex);
                } else if (type.equalsIgnoreCase(TYPE_INT)) {
                    value = cursor.getInt(valueIndex);
                } else if (type.equalsIgnoreCase(TYPE_LONG)) {
                    value = cursor.getLong(valueIndex);
                } else if (type.equalsIgnoreCase(TYPE_FLOAT)) {
                    value = cursor.getFloat(valueIndex);
                } else if (type.equalsIgnoreCase(TYPE_STRING_SET)) {
                    value = cursor.getString(valueIndex);
                }
                resultMap.put(key, value);
            }
            while (cursor.moveToNext());
            cursor.close();
        }
        return resultMap;
    }
}
