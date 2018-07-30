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
                int opCode = inst.getOpcode();

                // Skip JSR[_w} and *switch operations
                //TODO: Check if these are the correct statements to skip
                if(opCode == JSR_OPCODE || opCode == JSR_W_OPCODE || opCode == LOOKUP_SWITCH_OPCODE || opCode == TABLE_SWITCH_OPCODE
                    || opCode == INVOKE_DYNAMIC_OPCODE || opCode == INVOKE_INTERFACE_OPCODE || opCode == INVOKE_SPECIAL_OPCODE || opCode ==INVOKE_STATIC_OPECODE || opCode == INVOKE_VIRTUAL_OPCODE)
                {
                    // ignore the instruction
                    continue;
                }

                InstructionHandle next  = ih.getNext();
                int nextPos = -1;
                if(next != null)
                {
                    nextPos = next.getPosition();
                }
                cfg.addEdge(position,nextPos,m,jc);
                // TODO: Check if this is correct. Also check for ReturnInstructions
                if(inst instanceof  BranchInstruction)
                {
                    int targetPosition = ((BranchInstruction) inst).getTarget().getPosition();
                    cfg.addEdge(position,targetPosition,m,jc);
                }
            }
        }
        return cfg;
    }

    public CFG createCFGWithMethodInvocation(String className) throws ClassNotFoundException
    {
        CFG cfg = new CFG();
        JavaClass jc = Repository.lookupClass(className);
        ClassGen cg = new ClassGen(jc);
        ConstantPoolGen cpg = cg.getConstantPool();

        for (Method m: cg.getMethods())
        {
            MethodGen mg = new MethodGen(m, cg.getClassName(), cpg);
            InstructionList il = mg.getInstructionList();
            InstructionHandle[] handles = il.getInstructionHandles();
            for (InstructionHandle ih : handles)
            {
                int position = ih.getPosition();
                cfg.addNode(position, m, jc);
                Instruction inst = ih.getInstruction();
                int opCode = inst.getOpcode();

                // Skip JSR[_w} and *switch operations.
                // Don't skip INVOKE_STATIC this time
                if(opCode == JSR_OPCODE || opCode == JSR_W_OPCODE || opCode == LOOKUP_SWITCH_OPCODE || opCode == TABLE_SWITCH_OPCODE
                        || opCode == INVOKE_DYNAMIC_OPCODE || opCode == INVOKE_INTERFACE_OPCODE || opCode == INVOKE_SPECIAL_OPCODE || opCode == INVOKE_VIRTUAL_OPCODE)
                {
                    // ignore the instruction
                    continue;
                }
                if(inst instanceof INVOKESTATIC)
                {
                    //TODO: Finish this
                }
                else
                {
                    InstructionHandle next  = ih.getNext();
                    int nextPos = -1;
                    if(next != null)
                    {
                        nextPos = next.getPosition();
                    }
                    cfg.addEdge(position,nextPos,m,jc);
                    // TODO: Check if this is correct. Also check for ReturnInstructions
                    if(inst instanceof  BranchInstruction)
                    {
                        int targetPosition = ((BranchInstruction) inst).getTarget().getPosition();
                        cfg.addEdge(position,targetPosition,m,jc);
                    }
                }

            }

        }
        return cfg;
    }

    public static void main(String[] a) throws ClassNotFoundException
    {
        GraphGenerator gg = new GraphGenerator();
        CFG cCFG = gg.createCFG("pset5.C"); // example invocation of createCFG
        System.out.println(cCFG.toString());
        CFG dCFG = gg.createCFGWithMethodInvocation("pset5.D"); // example invocation of createCFGWithMethodInovcation
    }
}
