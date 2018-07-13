package com.cttic.liugw.ordinary.jdi;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jdi.AbsentInformationException;
import com.sun.jdi.ArrayReference;
import com.sun.jdi.BooleanValue;
import com.sun.jdi.Bootstrap;
import com.sun.jdi.ByteValue;
import com.sun.jdi.CharValue;
import com.sun.jdi.DoubleValue;
import com.sun.jdi.Field;
import com.sun.jdi.FloatValue;
import com.sun.jdi.IncompatibleThreadStateException;
import com.sun.jdi.IntegerValue;
import com.sun.jdi.LocalVariable;
import com.sun.jdi.Location;
import com.sun.jdi.LongValue;
import com.sun.jdi.ObjectReference;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.ShortValue;
import com.sun.jdi.StackFrame;
import com.sun.jdi.StringReference;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.Value;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.VirtualMachineManager;
import com.sun.jdi.connect.AttachingConnector;
import com.sun.jdi.connect.Connector.Argument;
import com.sun.jdi.event.BreakpointEvent;
import com.sun.jdi.event.Event;
import com.sun.jdi.event.EventIterator;
import com.sun.jdi.event.EventQueue;
import com.sun.jdi.event.EventSet;
import com.sun.jdi.event.VMDisconnectEvent;
import com.sun.jdi.request.BreakpointRequest;
import com.sun.jdi.request.EventRequest;
import com.sun.jdi.request.EventRequestManager;
import com.sun.tools.jdi.ClassTypeImpl;
import com.sun.tools.jdi.SocketAttachingConnector;

/**
 * 使用JDI编写代码进行远程调式
 * 1. 被调式程序设置调式端口
 *    被调式程序启动时添加允许远程调式的启动参数：
 *               -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005
 * 2. 引用类库
 *    每个Java JDK都有自己的JDI接口实现，标准的Oracle JDK的JDI接口放在jdk/lib/tools.jar中，因此写代码时首先需要引入这个jar。
 * 3. 连接远程JVM
 * 4. 标记断点
 * 5. 跟踪获取断点信息
 * 
 * 注意： 会影响程序运行的性能， 生产环境不可长时间使用。
 * 
 * @author liugaowei
 *
 */
public class JDIDebugTool {
    private static final Logger LOGGER = LoggerFactory.getLogger(JDIDebugTool.class);

    private VirtualMachine vm;
    private String hostname;
    private Integer port;

    public JDIDebugTool(String hostname, Integer port) throws Exception {
        this.hostname = hostname;
        this.port = port;
        vm = connectVirtualMachine(hostname, port);
    }

    /**
     * 连接远程JVM 
     * @param hostname
     * @param port
     * @return
     * @throws Exception
     */
    private VirtualMachine connectVirtualMachine(String hostname, Integer port) throws Exception {
        VirtualMachineManager virtualMachineManager = Bootstrap.virtualMachineManager();
        List<AttachingConnector> attachingConnectors = virtualMachineManager.attachingConnectors();
        SocketAttachingConnector sac = null;
        for (AttachingConnector attachingConnector : attachingConnectors) {
            if (attachingConnector instanceof SocketAttachingConnector) {
                sac = (SocketAttachingConnector) attachingConnector;
                break;
            }
        }
        if (sac == null) {
            throw new Exception("未找到可用的SocketAttachingConnector连接器");
        }

        Map<String, Argument> arguments = sac.defaultArguments();
        arguments.get("hostname").setValue(hostname);
        arguments.get("port").setValue(String.valueOf(port));
        return (VirtualMachine) sac.attach(arguments);
    }

    /**
     * 标记断点
     * 注册了一个Breakpoint类型的事件请求
     * 
     * 需指定要标记断点的类和行数。注意一个类或者行可能会有多个线程来调用，
     * 但是示例代码中为了简单考虑只获取了第一个调用线程的结果，即get(0)。
     * 此外，给行打断点时，所设置的行数必须是有效代码的行。
     * 例如如果所设置的行是回车没有Java代码，则会抛出异常。
     * 
     * @param clazz
     * @param line
     * @throws Exception
     */
    public void makeBreakPoint(Class clazz, int line) throws Exception {
        EventRequestManager eventRequestManager = vm.eventRequestManager();
        List<ReferenceType> rtypeList = vm.classesByName(clazz.getName());
        if (CollectionUtils.isEmpty(rtypeList)) {
            throw new Exception("无法获取有效的debug类");
        }

        ClassTypeImpl classType = (ClassTypeImpl) rtypeList.get(0);
        List<Location> locations = classType.locationsOfLine(line);
        if (CollectionUtils.isEmpty(locations)) {
            throw new Exception("无法获取有效的debug行");
        }

        Location location = locations.get(0);
        BreakpointRequest breakpointRequest = eventRequestManager.createBreakpointRequest(location);
        breakpointRequest.setSuspendPolicy(EventRequest.SUSPEND_EVENT_THREAD);
        breakpointRequest.enable();
    }

    /**
     * 跟踪断点信息
     */
    public void traceBreakpoint() {
        EventQueue eventQueue = vm.eventQueue();
        try {
            boolean runable = true;
            while (runable) {
                EventSet eventSet = eventQueue.remove();
                EventIterator eventIterator = eventSet.eventIterator();
                while (eventIterator.hasNext()) {
                    Event event = eventIterator.next();
                    // 如果是 设置断点所引起的事件
                    if (event instanceof BreakpointEvent) {
                        logBreadkpoint((BreakpointEvent) event);

                        /**
                         * VMDisconnectEvent : 目标虚拟机与调试器断开链接所引发的事件
                         * 断开链接肯定是最后一个事件，所以自动跳出最内层循环
                         */
                        if (event instanceof VMDisconnectEvent) {
                            runable = false;
                        }
                    }
                }
                eventSet.resume();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取断点信息
     * 
     * @param event
     * @throws IncompatibleThreadStateException 
     * @throws AbsentInformationException 
     */
    private void logBreadkpoint(BreakpointEvent event) throws IncompatibleThreadStateException {
        ThreadReference threadReference = event.thread();
        System.out.println("threadReference.frameCount()=" + threadReference.frameCount());

        StackFrame stackFrame = threadReference.frame(0);
        LOGGER.debug("<<================<< " + stackFrame.location()
                + " / " + stackFrame.location().method().name()
                + " >>================>>");
        System.out.println("<<================<< " + stackFrame.location()
                + " / " + stackFrame.location().method().name()
                + " >>================>>");

        LOGGER.debug("[方法参数]");
        System.out.println("[方法参数]");
        List<Value> argsList = stackFrame.getArgumentValues();
        for (Value arg : argsList) {
            LOGGER.debug(arg.type().name() + "=" + parseValue(arg));
        }

        LOGGER.debug("[变量信息]");
        System.out.println("[变量信息]");
        List<LocalVariable> varList;
        try {
            varList = stackFrame.visibleVariables();
            for (LocalVariable var : varList) {
                logLocalVariable(stackFrame, var);
            }
        } catch (AbsentInformationException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取变量信息
     * 
     * @param stackFrame
     * @param var
     */
    private void logLocalVariable(StackFrame stackFrame, LocalVariable var) {
        StringBuilder out = new StringBuilder(var.name());
        if (var.isArgument()) {
            out.append("[*]");
        }
        out.append(" = ");
        out.append(parseValue(stackFrame.getValue(var)));
        LOGGER.debug(out.toString());
        System.out.println(out.toString());
    }

    private String parseValue(Value value) {
        StringBuilder out = new StringBuilder();

        // 如果是基本数据类型 或者是 字符串类型
        if (value instanceof StringReference || value instanceof IntegerValue || value instanceof BooleanValue
                || value instanceof ByteValue || value instanceof CharValue || value instanceof ShortValue
                || value instanceof LongValue || value instanceof FloatValue || value instanceof DoubleValue) {
            out.append(value);
        } else if (value instanceof ObjectReference) {
            // 如果是对象引用类型
            ObjectReference obj = (ObjectReference) value;
            String type = obj.referenceType().name();
            if ("java.lang.Integer".equals(type) || "java.lang.Boolean".equals(type) ||
                    "java.lang.Float".equals(type) || "java.lang.Double".equals(type) ||
                    "java.lang.Long".equals(type) || "java.lang.Byte".equals(type) ||
                    "java.lang.Character".equals(type)) {
                // 基本数据类型的自动装箱类型
                Field field = obj.referenceType().fieldByName("value");
                out.append(obj.getValue(field));
            } else if ("java.util.Date".equals(type)) {
                // 日期类型
                Field field = obj.referenceType().fieldByName("fastTime");
                Date date = new Date(Long.parseLong("" + obj.getValue(field)));
                out.append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));
            } else if (value instanceof ArrayReference) {
                // 数组类型
                ArrayReference ar = (ArrayReference) value;
                List<Value> values = ar.getValues();
                out.append("[");
                for (int i = 0; i < values.size(); i++) {
                    if (i != 0)
                        out.append(" ,");
                    out.append(parseValue(values.get(i)));
                }
                out.append("]");
            } else {
                // 其他
                out.append(type);
            }
        }

        return out.toString();
    }

    public static void main(String[] args) throws Exception {
        JDIDebugTool debugTool = new JDIDebugTool("127.0.0.1", 5005);
        debugTool.makeBreakPoint(MyAppProcess.class, 36);
        //JDIDebugTool debugTool = new JDIDebugTool("192.168.1.22", 5005);
        //debugTool.makeBreakPoint(DataConvertTask.class, 328);
        debugTool.traceBreakpoint();
    }

}
