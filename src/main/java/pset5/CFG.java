package pset5;

import java.util.*;

import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;

public class CFG
{
    Set<Node> nodes = new HashSet<Node>();
    Map<Node, Set<Node>> edges = new HashMap<Node, Set<Node>>();

    public static class Node
    {
        int position;
        Method method;
        JavaClass clazz;

        Node(int p, Method m, JavaClass c)
        {
            position = p;
            method = m;
            clazz = c;
        }

        public Method getMethod()
        {
            return method;
        }

        public JavaClass getClazz()
        {
            return clazz;
        }

        public boolean equals(Object o)
        {
            if (!(o instanceof Node)) return false;
            Node n = (Node) o;
            return (position == n.position) && method.equals(n.method) && clazz.equals(n.clazz);
        }

        public int hashCode()
        {
            return position + method.hashCode() + clazz.hashCode();
        }

        public String toString()
        {
            return clazz.getClassName() + "." + method.getName() + method.getSignature() + ": " + position;
        }
    }

    public void addNode(int p, Method m, JavaClass c)
    {
        addNode(new Node(p, m, c));
    }

    private void addNode(Node n)
    {
        nodes.add(n);
        Set<Node> nbrs = edges.get(n);
        if (nbrs == null)
        {
            nbrs = new HashSet<Node>();
            edges.put(n, nbrs);
        }
    }

    public void addEdge(int p1, Method m1, JavaClass c1, int p2, Method m2, JavaClass c2)
    {
        Node n1 = new Node(p1, m1, c1);
        Node n2 = new Node(p2, m2, c2);
        addNode(n1);
        addNode(n2);
        Set<Node> nbrs = edges.get(n1);
        nbrs.add(n2);
        edges.put(n1, nbrs);
    }

    public void addEdge(int p1, int p2, Method m, JavaClass c)
    {
        addEdge(p1, m, c, p2, m, c);
    }

    public String toString()
    {
        return nodes.size() + " nodes\n" + "nodes: " + nodes + "\n" + "edges: " + edges;
    }

    public boolean isReachable(String methodFrom, String clazzFrom,
                               String methodTo, String clazzTo)
    {
        if(methodFrom == null || clazzFrom == null || methodTo == null || clazzTo == null)
        {
            return false;
        }
        Node start = null;
        // Find the start node
        for (Node n: nodes)
        {
            String methodName = n.getMethod().getName();
            String className = n.getClazz().getClassName();
            int pos = n.position;

            // Start node has position 0 and matches the method and classnames
            boolean isStartNode = pos == 0;
            boolean isStartClass = clazzFrom.matches(className);
            boolean inStartMethod = methodFrom.matches(methodName);
            if (isStartNode && isStartClass && inStartMethod)
            {
                start = n;
                break;
            }
        }
        if(start == null)
        {
            // Did not find a start node
            return false;
        }
        Set<Node> visitedNodes = new HashSet<Node>();

        // Iterate over the CFG using DFS
        Stack<Node> s  = new Stack<Node>();

        // Use a stack to keep track of method invocations
        // If a method A invokes method B, then we must return to method A,
        // and not other methods
        Stack<Node> methodInvocations = new Stack<Node>();
        s.push(start);
        while(s.empty() == false)
        {
            Node curr = s.pop();
            if(visitedNodes.contains(curr))
            {
                continue;
            }
            visitedNodes.add(curr);
            String currMethodName = curr.getMethod().getName();
            String currClazzName = curr.getClazz().getClassName();

            // Check if the current node is the destination node
            if(methodTo.equals(currMethodName) && clazzTo.equals(currClazzName))
            {
                return true;
            }
            Set<Node> neighbors = edges.get(curr);

            int currPosition = curr.position;
            // Check if the current node is an EXIT node
            boolean isExitNode = currPosition == GraphGenerator.DUMMY_EXIT_NODE;
            // This condition is correct.
            if(isExitNode)
            {
                if(methodInvocations.isEmpty() == false)
                {
                    // Get the method that invoked this current method
                    Node invokee = methodInvocations.pop();
                    String methodName = invokee.getMethod().getName();
                    // Add only the nodes going back to the invokee
                    for(Node n : neighbors)
                    {
                        String str = n.getMethod().getName();
                        if(methodName.equals(str))
                        {
                            s.push(n);
                        }
                    }
                }
            }
            else
            {
                // Get the nodes reachable from the current node
                for(Node n : neighbors)
                {
                    int position = n.position;
                    boolean isMethodInvocation = position == 0;
                    // check if the neighbor is a method invocation
                    if(isMethodInvocation)
                    {
                        // Add the currentNode to the stack
                        methodInvocations.push(curr);

                    }
                    s.push(n);
                }
            }
        }
        return false;
    }
}
