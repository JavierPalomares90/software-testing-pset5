import org.junit.Test;
import pset5.CFG;
import pset5.GraphGenerator;

import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GraphGeneratorTest
{
    @Test
    public void testCFCForC() throws ClassNotFoundException
    {
        GraphGenerator gg = new GraphGenerator();
        CFG cfg = gg.createCFG("pset5.C"); // example invocation of createCFG
        System.out.println(cfg.toString());
        assertTrue(true);
    }

    @Test
    public void testCFGForD() throws ClassNotFoundException
    {
        GraphGenerator gg = new GraphGenerator();
        CFG cfg = gg.createCFGWithMethodInvocation("pset5.D"); // example invocation of createCFGWithMethodInovcation
        System.out.println(cfg.toString());
        assertTrue(true);
    }

    @Test
    public void testReachable() throws ClassNotFoundException
    {
        GraphGenerator gg = new GraphGenerator();
        CFG cfg = gg.createCFGWithMethodInvocation("pset5.D"); // example invocation of createCFGWithMethodInovcation
        boolean reachable = cfg.isReachable("main", "pset5.D", "foo", "pset5.D");
        assertTrue(reachable);
    }

    @Test
    public void testReachable2() throws ClassNotFoundException
    {
        GraphGenerator gg = new GraphGenerator();
        CFG cfg = gg.createCFGWithMethodInvocation("pset5.D"); // example invocation of createCFGWithMethodInovcation
        boolean reachable = cfg.isReachable("main", "pset5.D", "bar", "pset5.D");
        assertTrue(reachable);
    }

    @Test
    public void testReachable3() throws ClassNotFoundException
    {
        GraphGenerator gg = new GraphGenerator();
        CFG cfg = gg.createCFGWithMethodInvocation("pset5.C"); // example invocation of createCFG
        boolean reachable = cfg.isReachable("max","pset5.C","init","pset5.C");
        assertFalse(reachable);

    }

    @Test
    public void testReachable4() throws ClassNotFoundException
    {
        GraphGenerator gg = new GraphGenerator();
        CFG cfg = gg.createCFGWithMethodInvocation("pset5.TestClass"); // example invocation of createCFG
        boolean reachable = cfg.isReachable("main","pset5.TestClass","recursiveMethod","pset5.TestClass");
        assertTrue(reachable);
        reachable = cfg.isReachable("main","pset5.TestClass","da","pset5.TestClass");
        assertTrue(reachable);
        reachable = cfg.isReachable("recursiveMethod","pset5.TestClass","recursiveMethod","pset5.TestClass");
        assertTrue(reachable);
        reachable = cfg.isReachable("recursiveMethod","pset5.TestClass","da","pset5.TestClass");
        assertTrue(reachable);
        reachable = cfg.isReachable("foo","pset5.TestClass","bar","pset5.TestClass");
        assertTrue(reachable);
        reachable = cfg.isReachable("bar","pset5.TestClass","foo","pset5.TestClass");
        assertTrue(reachable);
        reachable = cfg.isReachable("bar","pset5.TestClass","da","pset5.TestClass");
        assertFalse(reachable);
        reachable = cfg.isReachable("recursiveMethod","pset5.TestClass","main","pset5.TestClass");
        assertFalse(reachable);
        reachable = cfg.isReachable("recursiveMethod","pset5.TestClass","bar","pset5.TestClass");
        assertFalse(reachable);
        reachable = cfg.isReachable("recursiveMethod","pset5.TestClass","foo","pset5.TestClass");
        assertFalse(reachable);
    }

    @Test
    public void testReachable5() throws ClassNotFoundException
    {
        GraphGenerator gg = new GraphGenerator();
        CFG cfg = gg.createCFGWithMethodInvocation("pset5.TestClass"); // example invocation of createCFG
        boolean reachable = cfg.isReachable("recursiveMethod","pset5.TestClass","da","pset5.TestClass");
        assertTrue(reachable);

    }

}
