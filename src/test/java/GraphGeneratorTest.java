import org.junit.Test;
import pset5.CFG;
import pset5.GraphGenerator;

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
}
