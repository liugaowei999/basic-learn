package ordinary;

import java.io.IOException;
import java.lang.reflect.Method;

class H {

    private int a;

    public H() {
        System.out.println("H default con");
    }

    static {
        System.out.println("static code");
    }

    private int compute(Integer a, Integer b) {
        return a + b;
    }
}

class K {

    private int a;

    public int compute(Integer a, Integer b) {
        return a * b;
    }
}

public class RetureTest {

    volatile static int x = 0;

    public static int inc() throws Exception, IOException {
        try {
            x = 2;

            return x;
        } catch (Exception e) {
            // TODO: handle exception
            x = 3;
            throw new Exception("xxxxxxxxxxxxxxxx");
            //            return x;
        } finally {
            x = 4;
            //            return x;
        }
    }

    public static void main(String args[]) throws Exception {
        //        System.out.println(inc());

        // H h = new H();
        //        h.add(1, 3);

        /**
         * # Busiy_1_class=com.xx.H Busiy_2_class=com.xx.K
         * Busiy_invoke_method=compute
         */

        // 读配置
        String busyClassName = "ordinary.H";
        String methodName = "compute";

        Class clazz = Class.forName(busyClassName, true, Thread.currentThread().getContextClassLoader());
        //Class clazz= Class.forName(busyClassName);
        System.out.println("=================");

        Class[] parameterTypes = { Integer.class, Integer.class };
        int[] aaa = { 2, 3 };

        Method declaredMethods = clazz.getDeclaredMethod(methodName, parameterTypes);
        declaredMethods.setAccessible(true);

        Object result = declaredMethods.invoke(clazz.newInstance(), new Object[] { 2, 3 });
        System.out.println("result=" + result);

        //ClassForName("busyClassName");
    }
}
