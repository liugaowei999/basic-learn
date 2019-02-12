package com.cttic.liugw.ordinary.classjudge;

import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * isAssignableFrom()方法与instanceof关键字的区别总结为以下两个点：
 *    1. isAssignableFrom()方法是从类继承的角度去判断，instanceof关键字是从实例继承的角度去判断。
 *    2. isAssignableFrom()方法是判断是否为某个类的父类，instanceof关键字是判断是否某个类的子类。
 * 使用方法：
 *    父类.class.isAssignableFrom(子类.class)
 *    子类实例 instanceof 父类类型
 * isAssignableFrom()方法的调用者和参数都是Class对象，调用者为父类，参数为本身或者其子类。
 * instanceof关键字两个参数，前一个为类的实例，后一个为其本身或者父类的类型。
 *
 * 我们有时候需要为对象字段设置默认值，即在别的处理中生成对象并对对象进行赋值后，有些值需要有默认值，但是又不方便通过构造方法设置的时候，
 * 我们可以通过反射配合注解来为其设置默认值而不用调用一堆set方法。
 *
 * 下面这个例子即反射配置注解为对象字段设置默认值（包括父类），仅支持String和本类型的包装部分包装类（Number的子类）。
 *
 */

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////         一个使用注解 设置默认值的 例子                                                   ////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * description: 用于设置默认值的注解类
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@interface ParamDefaultValue {
    String value();
}

public class ClassAssignableFromTest {

    // isAssignableFrom 应用于 注解设置默认值
    public static void applyDefaultValue(Object o) {
        Class sourceClass = o.getClass();
        //获取对象所有字段 包括父类
        ArrayList<Field> fields = new ArrayList<>();
        while (sourceClass != null){
            fields.addAll(Arrays.asList(sourceClass.getDeclaredFields()));
            sourceClass = sourceClass.getSuperclass();
        }

        for (Field field : fields) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(ParamDefaultValue.class)) {
                try {
                    Object val = field.get(o);
                    if (val != null) {
                        continue;
                    }
                    Class type = field.getType();
                    if (type.isPrimitive()) {
                        continue;
                    }
                    String defVal = field.getAnnotation(ParamDefaultValue.class).value();

                    if (String.class.isAssignableFrom(type)) {
                        field.set(o, defVal);
                    } else if (Number.class.isAssignableFrom(type)) {
                        if (Byte.class.isAssignableFrom(type)) {
                            field.set(o, Byte.valueOf(defVal));
                        } else if (Float.class.isAssignableFrom(type)) {
                            field.set(o, Float.valueOf(defVal));
                        } else if (Short.class.isAssignableFrom(type)) {
                            field.set(o, Short.valueOf(defVal));
                        } else if (Integer.class.isAssignableFrom(type)) {
                            field.set(o, Integer.valueOf(defVal));
                        } else if (Double.class.isAssignableFrom(type)) {
                            field.set(o, Double.valueOf(defVal));
                        } else if (Long.class.isAssignableFrom(type)) {
                            field.set(o, Long.valueOf(defVal));
                        }
                    }

                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        A a = new A();
        B b = new B();
        A ba = new B();
        System.out.println("1-------------");
        System.out.println(A.class.isAssignableFrom(a.getClass()));
        System.out.println(B.class.isAssignableFrom(b.getClass()));
        System.out.println(A.class.isAssignableFrom(b.getClass()));
        System.out.println(B.class.isAssignableFrom(a.getClass()));
        System.out.println(A.class.isAssignableFrom(ba.getClass()));
        System.out.println(B.class.isAssignableFrom(ba.getClass()));

        System.out.println("2-------------");
        System.out.println(a.getClass().isAssignableFrom(A.class));
        System.out.println(b.getClass().isAssignableFrom(B.class));
        System.out.println(a.getClass().isAssignableFrom(B.class));
        System.out.println(b.getClass().isAssignableFrom(A.class));
        System.out.println(ba.getClass().isAssignableFrom(A.class));
        System.out.println(ba.getClass().isAssignableFrom(B.class));

        System.out.println("3-------------");
        System.out.println(Object.class.isAssignableFrom(b.getClass()));
        System.out.println(Object.class.isAssignableFrom("abc".getClass()));

        System.out.println("4-------------");
        System.out.println("a".getClass().isAssignableFrom(Object.class));
        System.out.println("abc".getClass().isAssignableFrom(Object.class));
    }


}

class A {
}

class B extends A {
}
