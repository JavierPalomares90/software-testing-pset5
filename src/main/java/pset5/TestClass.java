package pset5;

public class TestClass
{
    public static void main(String[] a)
    {
        recursiveMethod(3);
        foo(a);
        bar(a);
    }

    static void foo(String[] a)
    {
        if (a == null) return;
        bar(a);
    }
    static void da()
    {
    }
    static void du()
    {

    }


    static void recursiveMethod(int i)
    {
        if( i <= 0)
        {
            return;
        }
        da();
        recursiveMethod(i-1);
    }

    static void bar(String[] a)
    {
        foo(a);
    }


    static void ifffs(int i)
    {
        if(i < 0)
        {
            if(i < 10 )
            {
                if(i < 100)
                {
                    da();
                    return;
                }

            }
            bar(null);
            return;
        }
        foo(null);

    }

}
