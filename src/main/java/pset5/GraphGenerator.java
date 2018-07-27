package pset5;

import org.apache.bcel.Repository;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.*;

public class GraphGenerator
{
    private static int JSR_W_OPCODE = 0xc9;
    private static int JSR_OPCODE = 0xa8;
    private static int LOOKUP_SWITCH_OPCODE = 0xab;
    private static int TABLE_SWITCH_OPCODE = 0xaa;
    private static int INVOKE_DYNAMIC_OPCODE = 0xba;
    private static int INVOKE_INTERFACE_OPCODE = 0xb9;
    private static int INVOKE_SPECIAL_OPCODE = 0Xb7;
    private static int INVOKE_STATIC_OPECODE = 0xb8;
    private static int INVOKE_VIRTUAL_OPCODE = 0Xb6;


    public CFG createCFG(String className) throws ClassNotFoundException
    {
        CFG cfg = new CFG();
        JavaClass jc = Repository.lookupClass(className);
        ClassGen cg = new ClassGen(jc);
        ConstantPoolGen cpg = cg.getConstantPool();
        for (Method m : cg.getMethods())
        {
            MethodGen mg = new MethodGen(m, cg.getClassName(), cpg);
            InstructionList il = mg.getInstructionList();
            InstructionHandle[] handles = il.getInstructionHandles();
            for (InstructionHandle ih : handles)
            {
                int position = ih.getPosition();
                cfg.addNode(position, m, jc);
                Instruction inst = ih.getInstruction();
                // your code goes here
                // TODO: Finish
                int opCode = inst.getOpcode();

                if(opCode == JSR_OPCODE || opCode == JSR_W_OPCODE || opCode == LOOKUP_SWITCH_OPCODE || opCode == TABLE_SWITCH_OPCODE
                    || opCode == INVOKE_DYNAMIC_OPCODE || opCode == INVOKE_INTERFACE_OPCODE || opCode == INVOKE_SPECIAL_OPCODE || opCode ==INVOKE_STATIC_OPECODE || opCode == INVOKE_VIRTUAL_OPCODE)
                {
                    // ignore the instruction
                    continue;
                }
            }
        }
        return cfg;
    }

    public CFG createCFGWithMethodInvocation(String className) throws ClassNotFoundException
    {
        // TODO: Finish
        // your code goes here
        return null;
    }

    public static void main(String[] a) throws ClassNotFoundException
    {
        GraphGenerator gg = new GraphGenerator();
        gg.createCFG("pset5.C"); // example invocation of createCFG
        gg.createCFGWithMethodInvocation("pset5.D"); // example invocation of createCFGWithMethodInovcation
    }
}
