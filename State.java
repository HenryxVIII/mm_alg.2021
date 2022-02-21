import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class State
{
//-------------------------methods-------------------------

    public State (Integer[] positions)
    {
        m_positions = positions;
        m_size = (int) Math.sqrt(positions.length);
        for (int i = 0; i < m_positions.length; i++)
        {
            if (m_positions[i] == 0)
            {
                m_empty = i;
                break;
            }
        }
    }

    public boolean Equals (Object other)
    {
        if (other == this)
        {
            return true;
        }

        if (other == null)
        {
            return false;
        }

        if (other.getClass() != this.getClass())
        {
            return false;
        }

        State other_field = (State) other;
        return Arrays.equals(this.m_positions, other_field.m_positions);
    }

    public Integer At (int row, int col)
    {
        return m_positions[row * m_size + col];
    }

    public static State Target (int size)
    {
        Integer[] positions = new Integer[size * size];
        for (int i = 0; i < size * size - 1; i++)
            positions[i] = i + 1;
        positions[size * size - 1] = 0;
        return new State(positions);
    }

    private int TargetRowFor (int value)
    {
        if (value == 0)
            return m_size - 1;
        return (value - 1) / m_size;
    }

    private int TargetColumnFor (int value)
    {
        if (value % m_size == 0)
            return m_size - 1;
        return (value % m_size) - 1;
    }

    public int Distance ()
    {
        int res = 0;
        for (int i = 0; i < m_size; i++)
        {
            for (int j = 0; j < m_size; j++)
            {
                Integer value = At(i, j);
                if (value == 0)
                    continue;

                int diff_row = Math.abs(TargetRowFor(value) - i);
                int diff_col = Math.abs(TargetColumnFor(value) - j);
                res += diff_row + diff_col;
            }
        }
        return res;
    }

//--------------------Moves and their getters---------------------------

    public ArrayList<State> Neighbors ()
    {
        ArrayList<State> list = new ArrayList<>();

        State up = Up();
        if (up != this)
            list.add(up);

        State down = Down();
        if (down != this)
            list.add(down);

        State left = Left();
        if (left != this)
            list.add(left);

        State right = Right();
        if (right != this)
            list.add(right);

        return list;
    }

    public State Left ()
    {
        if (m_empty % m_size == 0)
            return this;
        Integer[] new_positions = m_positions.clone();
        new_positions[m_empty] = new_positions[m_empty - 1];
        new_positions[m_empty - 1] = 0;
        return new State (new_positions);
    }

    public State Right ()
    {
        if (m_empty % m_size == m_size - 1)
            return this;
        Integer[] new_positions = m_positions.clone();
        new_positions[m_empty] = new_positions[m_empty + 1];
        new_positions[m_empty + 1] = 0;
        return new State (new_positions);
    }

    public State Up ()
    {
        if (m_empty < m_size)
            return this;
        Integer[] new_positions = m_positions.clone();
        new_positions[m_empty] = new_positions[m_empty - m_size];
        new_positions[m_empty - m_size] = 0;
        return new State(new_positions);
    }

    public State Down ()
    {
        if (m_empty > m_size * (m_size - 1) - 1)
            return this;
        Integer[] new_positions = m_positions.clone();
        new_positions[m_empty] = new_positions[m_empty + m_size];
        new_positions[m_empty + m_size] = 0;
        return new State(new_positions);
    }

    public State RandomShift ()
    {
        Random rnd = new Random();
        int direction = rnd.nextInt(4);
        return switch (direction)
        {
            case 0 -> this.Left();
            case 1 -> this.Right();
            case 2 -> this.Up();
            case 3 -> this.Down();
            default -> this;
        };
    }

//---------------------------------------------

    public void Print()
    {
        for (int i = 0; i < m_size; i++)
        {
            System.out.print("|");
            for (int j = 0; j < m_size; j++)
            {
                if (At(i, j) == 0)
                    System.out.print("   ");
                else
                    System.out.printf("%d ", At(i, j));
            }
            System.out.println("|");
        }
        System.out.println();
    }
    //------------------members--------------------
    private final Integer m_size;
    private final Integer[] m_positions;
    private Integer m_empty;
}
