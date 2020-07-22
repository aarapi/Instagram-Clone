package com.instacommerce.annoyingprojects.utilities;

import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ClassType<A> extends TypeToken {

    private   Gson gson;
    final Class<? super A> rawType;
    final Type type;

    protected ClassType() {
        this.type = getSuperclassTypeParameter(getClass());
        this.rawType = (Class<? super A>) $Gson$Types.getRawType(type);

    }
    Type getSuperclassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        } else {
            ParameterizedType parameterized = (ParameterizedType)superclass;
            return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
        }
    }

    public <T> T fromJson(Object jsonObject){
        gson = new Gson();

        return gson.fromJson(gson.toJson(jsonObject), type);
    }


}


