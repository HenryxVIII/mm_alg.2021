import java.lang.reflect.Field;
import java.util.PriorityQueue;
import java.util.Stack;
import java.util.Arrays;

class Solver {
    private class SolverState implements Comparable<SolverState>
    {
        public SolverState (State field, int moves, SolverState previousState)
        {
            m_field = field;
            m_moves = moves;
            m_prev_state = previousState;
            m_priority = m_a * m_field.Distance() + m_b * moves;
        }

        public int compareTo(SolverState other)
        {
            return (m_priority - other.m_priority);
        }
    //---------------members-------------------------------------
        private final State m_field;
        private final int m_moves;
        private final SolverState m_prev_state;
        private final int m_priority;
    }

    public Solver(int size, int a, int b)
    {
        m_a = a;
        m_b = b;
        m_pq = new PriorityQueue<>();
        m_ts = State.Target(size);
    }

    public boolean Solve(State initial)
    {
        m_is = initial;
        m_pq.clear();
        m_pq.add(new SolverState(m_is, 0, null));
        while (!m_pq.isEmpty() && !m_pq.peek().m_board.Equals(m_ts))
        {
            SolverState min_state = m_pq.poll();
            var neighbors = min_state.m_board.Neighbors();
            for (State neighbor : neighbors)
            {
                if (min_state.m_moves == 0)
                {
                    m_pq.add(new SolverState(neighbor, min_state.m_moves + 1, min_state));
                }
                else if (!neighbor.Equals(min_state.m_prev_state.m_board))
                {
                    m_pq.add(new SolverState(neighbor, min_state.m_moves + 1, min_state));
                }
            }
        }
        return IsSolved();
    }

    public boolean IsSolved()
    {
        return !m_pq.isEmpty() && m_pq.peek().m_board.Equals(m_ts);
    }

    public int MovesCount()
    {
        if (!IsSolved ())
            return -1;
        assert m_pq.peek() != null;
        return m_pq.peek().m_moves;
    }

    public void Print()
    {
        if (IsSolved())
        {
            Stack<State> solutionStack = PathToSolution();
            while (!solutionStack.empty())
            {
                solutionStack.pop().Print();
            }
            System.out.println("Number of steps: " + MovesCount());
        }
        else
        {
            System.out.println("No solution!");
        }
    }

    public Stack<State> PathToSolution()
    {
        if (!IsSolved() || m_pq.isEmpty())
            return null;

        Stack<State> stackSolution = new Stack<>();
        SolverState current = m_pq.peek();
        while (current.m_prev_state != null)
        {
            stackSolution.push(current.m_board);
            current = current.m_prev_state;
        }
        stackSolution.push(m_is);
        return stackSolution;
    }

//--------------------members---------------------
    static private int m_a;
    static private int m_b;
    private final PriorityQueue<SolverState> m_pq;
    private State m_is;
    private final State m_ts;
}

public class Solver15 {

    public static void main(String[] args)
    {
        test (false);
        test (true);
    }


    private static void test (Boolean print_time)
    {
        System.out.println("Field size:" + m_field_size + "x" + m_field_size);
        if (print_time)
            System.out.println("Average time for solving the puzzle depending on 'a' and 'b' coeffitients:");
        else
            System.out.println("Average moves for solving the puzzle depending on 'a' and 'b' coeffitients:");
        System.out.print("a\\b| ");

        for (int b = 1; b <= m_test_b_max; b++)
        {
            System.out.printf("   %2d  ", b);
        }

        System.out.println();

        for (int a = 1; a <= m_test_a_max; a++)
        {
            System.out.printf("%2d | ", a);
            for (int b = 1; b <= m_test_b_max; b++)
            {
                boolean testPassed = test(m_field_size, a, b, m_test_size, print_time);
                if (!testPassed)
                    System.out.println("FAILED");
            }
            System.out.println();
        }
    }

    private static boolean test (int puzzleSize, int a, int b, int test_size, boolean printTime)
    {
        Solver solver = new Solver(puzzleSize, a, b);
        double[] times = new double[test_size];
        int[] moves = new int[test_size];

        for (int test_i = 0; test_i < test_size; test_i++)
        {
            long start_time = System.currentTimeMillis();
            solver.Solve(GenerateField(puzzleSize));
            long finish_time = System.currentTimeMillis();
            times[test_i] = (finish_time - start_time) / 1000.0;
            if (!solver.IsSolved())
                return false;
            moves[test_i] = solver.MovesCount();
        }
        if (printTime)
            System.out.printf("%.3e  ", Arrays.stream(times).average().getAsDouble());
        else
            System.out.printf("%3.3f  ", Arrays.stream(moves).average().getAsDouble());
        return true;
    }

    private static State GenerateField(int size)
    {
        State field = State.Target (size);
        for (int i = 0; i < m_shuffle_num; i++)
        {
            field = field.RandomShift ();
        }
        return field;
    }

//---------------------members--------------------

    static private int m_field_size = 3;
    static private int m_a = 3;
    static private int m_b = 1;

    static private int m_shuffle_num = 1000;

    static private int m_test_size = 20;
    static private int m_test_a_max = 10;
    static private int m_test_b_max = 10;
}
