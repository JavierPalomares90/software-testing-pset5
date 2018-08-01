import org.junit.Test;
import pset5.CFG;
import pset5.GraphGenerator;

import static org.junit.Assert.assertTrue;

public class GraphGeneratorTest
{
    @Test
    public void t0() throws ClassNotFoundException
    {
        GraphGenerator gg = new GraphGenerator();
        CFG cfg = gg.createCFG("pset5.C"); // example invocation of createCFG
        System.out.println(cfg.toString());
        assertTrue(true);
    }

    @Test
    public void t1() throws ClassNotFoundException
    {
        GraphGenerator gg = new GraphGenerator();
        CFG cfg = gg.createCFGWithMethodInvocation("pset5.D"); // example invocation of createCFGWithMethodInovcation
        System.out.println(cfg.toString());

    }
}
