/*
 * Copyright 2014-2018 ieclipse.cn.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package playground.jdk8;

/**
 * 类/接口描述
 * 
 * @author Jamling
 * @date 2019年2月18日
 *       
 */
public interface EnhanceInterface {
    /**
     * static final optional
     */
    /*public static final */int SIZE = 10;
    
    void absFunc();

    /**
     * JDK8 可以在接口中实现方法啦
     */
    default void defaultFunc() {
        System.out.println("interface default function");
    }

    static int staticFun(int a, int b) {
        int c = a + b;
        System.out.println("interface static function "
                + String.format("%d+%d=%d", a, b, c));
        return c;
    }
    
    /*public static */class Impl implements EnhanceInterface {
        
        @Override
        public void absFunc() {
            System.out.println("interface implementation function");
        }
        
    }
    
    /*public */static void main(String[] args) {
        Impl impl = new Impl();
        impl.absFunc();
        impl.defaultFunc();
        EnhanceInterface.staticFun(1, 2);
    }
}
