package playground.jdk5;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class HowToGetGeneralType {

    public static void main(String[] args) {

        ApiResult<String> object = new ApiResult<>();
        object.code = 0;
        object.msg = "success";
        object.data = "login success";

        Task task = new Task<ApiResult<String>, String>(new DataListener<String>() {
            @Override
            public void onResponse(String s) {
                System.out.println("response: " + s);
            }
        }) {
            @Override
            public void load(ApiResult<String> o) {
                super.load(o);
            }
        };
        task.load(object);
    }

    public static class ApiResult<Output> {
        private int code;
        private String msg;
        private Output data;
    }

    public static class Request {

    }

    interface DataListener<Output> {
        void onResponse(Output output);
    }

    public abstract static class Task<Input, Output> {
        private DataListener<Output> listener;
        private Class<Output> outputClass;

        public Task(DataListener<Output> listener) {
            this.listener = listener;
            // get DataListener type
            ParameterizedType type = (ParameterizedType) this.listener.getClass().getGenericInterfaces()[0];
            Type atype = type.getActualTypeArguments()[0];
            if (atype instanceof Class) {
                outputClass = (Class<Output>) atype;
            } // other type
            System.out.println("output class: " + outputClass);

            type = (ParameterizedType) getClass().getGenericSuperclass();
            Type inputType = type.getActualTypeArguments()[0];
            Type outputType = type.getActualTypeArguments()[1];

            System.out.println("input type: " + inputType);
            System.out.println("output type: " + outputType);
        }

        public void load(Input input) {
            String json = new Gson().toJson(input);
            Type type = getApiResultType();
            type = TypeToken.getParameterized(ApiResult.class, outputClass).getType();

            ApiResult<Output> apiResult = new Gson().fromJson(json, type);
            this.listener.onResponse(apiResult.data);
        }

        private Type getApiResultType() {
            Type type = new ParameterizedType() {
                @Override
                public Type[] getActualTypeArguments() {
                    return new Type[]{outputClass};
                }

                @Override
                public Type getRawType() {
                    return HowToGetGeneralType.ApiResult.class;
                }

                @Override
                public Type getOwnerType() {
                    return null;
                }
            };
            return type;
        }
    }
}
