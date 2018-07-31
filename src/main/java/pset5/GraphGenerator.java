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
    // dummy integer to represent our single exit point
    private static int DUMMY_EXIT_NODE = -1;

    private void addNextInstruction(CFG cfg, InstructionHandle ih, Instruction inst, Method m, JavaClass jc, int position)
    {
        // Check if the instruction is a return instruction
        if (inst instanceof ReturnInstruction)
        {
            // Add an edge from the current method to the exit dummy node
            cfg.addEdge(position,DUMMY_EXIT_NODE,m,jc);
            // Check if the instruction in a branch instruction
        }else if(inst instanceof BranchInstruction)
        {
            // Add an edge from the current node to the target position
            InstructionHandle target = ((BranchInstruction) inst).getTarget();
            int targetPosition = DUMMY_EXIT_NODE;
            if(target != null)
            {
                targetPosition = target.getPosition();
            }
            cfg.addEdge(position,targetPosition,m,jc);
        }

        // Add an edge from the current position to the target
        InstructionHandle next  = ih.getNext();
        int nextPos = DUMMY_EXIT_NODE;
        if(next != null)
        {
            nextPos = next.getPosition();
        }
        cfg.addEdge(position,nextPos,m,jc);

    }

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
                if(opCode == JSR_OPCODE || opCode == JSR_W_OPCODE || opCode == LOOKUP_SWITCH_OPCODE || opCode == TABLE_SWITCH_OPCODE
                    || opCode == INVOKE_DYNAMIC_OPCODE || opCode == INVOKE_INTERFACE_OPCODE || opCode == INVOKE_SPECIAL_OPCODE || opCode ==INVOKE_STATIC_OPECODE || opCode == INVOKE_VIRTUAL_OPCODE)
                {
                    // ignore the instruction
                    continue;
                }
                addNextInstruction(cfg,ih,inst,m,jc,position);
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
                    // Get the invoked class and method names
                    // TODO: Pretty sure this breaks with overloading methods
                    String invokeMethodName = ((INVOKESTATIC) inst).getMethodName(cpg);
                    String invokeClassName =  ((INVOKESTATIC) inst).getClassName(cpg);
                    JavaClass invokeClass = Repository.lookupClass(invokeClassName);
                    Method invokeMethod = null; //TODO: Figure out how to get this method
                    // Add an edge from the current position to position 0 of the invoked method
                    cfg.addEdge(position,m,jc,0,invokeMethod,invokeClass);

                    // Get the next position
                    InstructionHandle next  = ih.getNext();
                    int nextPos = DUMMY_EXIT_NODE;
                    if(next != null)
                    {
                        nextPos = next.getPosition();
                    }
                    // Add an edge from the invoked method's exit to the nex position of the current method
                    cfg.addEdge(DUMMY_EXIT_NODE,invokeMethod,invokeClass,nextPos,m,jc);
                }else
                {
                    addNextInstruction(cfg, ih, inst, m, jc, position);
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
