package com.cttic.liugw.ordinary.javaAgent;

import java.io.IOException;
import java.util.List;

import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;

public class AttachToolMain {

    public static void attachedToJVM(String jarName, String JVMDescriptorName)
            throws AttachNotSupportedException, IOException, AgentLoadException, AgentInitializationException {
        List<VirtualMachineDescriptor> list = VirtualMachine.list();
        for (VirtualMachineDescriptor virtualMachineDescriptor : list) {
            System.out.println(virtualMachineDescriptor.displayName());
            if (virtualMachineDescriptor.displayName().endsWith(JVMDescriptorName)) {
                VirtualMachine vm = VirtualMachine.attach(virtualMachineDescriptor.id());
                vm.loadAgent(jarName, "argument for agent");
                System.out.println("Attached OK");
                vm.detach();
            }
        }
    }

    public static void main(String[] args)
            throws AttachNotSupportedException, IOException, AgentLoadException, AgentInitializationException {
        attachedToJVM("d:/jagent.jar", "RunLoopAccountMain");
    }
}
