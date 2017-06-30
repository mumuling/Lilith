package retrofit2.converter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * 处理转换
 * <p>
 * Created by coder on 2017/6/30.
 */
public class FastJsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {

    private Type mType;

    FastJsonResponseBodyConverter(Type type) {
        this.mType = type;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String body = value.string();
        if (mType == JSONObject.class) {
            return (T) JSON.parseObject(body);
        } else if (mType == JSONArray.class) {
            return (T) JSON.parseArray(body);
        } else if (mType == String.class) {
            return (T) body;
        } else {
            return JSON.parseObject(body, mType);
        }
    }
}
