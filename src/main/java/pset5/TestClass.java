package pset5;

public class TestClass
{
    public static void main(String[] a)
    {
        recursiveMethod(3);
        foo(a);
        bar(a);
        da();
    }

    static void foo(String[] a)
    {
        if (a == null) return;
        bar(a);
    }
    static void da()
    {
    }

    static void recursiveMethod(int i)
    {
        if( i <= 0)
        {
            return;
        }
        recursiveMethod(i-1);
    }

    static void bar(String[] a)
    {
        foo(a);
    }

}
