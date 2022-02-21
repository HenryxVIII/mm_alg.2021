import java.util.Arrays;

class UnionFind
{
    public UnionFind (int N)
    {
        m_parent = new int[N];
        m_size = new int[N];
        for (int i = 0; i < N; i++) {
            m_parent[i] = i;
            m_size[i] = 1;
        }
    }

    public int Find (int p)
    {
        int n = m_parent.length;
        if (p < 0 || p >= n)
            return -1;

        int root = p;
        while (root != m_parent[root])
            root = m_parent[root];

        while (p != root)
        {
            int new_p = m_parent[p];
            m_parent[p] = root;
            p = new_p;
        }
        return root;
    }

    public void Union (int p, int q)
    {
        int root_p = Find (p);
        int root_q = Find (q);
        if (root_p == root_q) return;

        // make smaller root point to larger one
        if (m_size[root_p] < m_size[root_q])
        {
            m_parent[root_p] = root_q;
            m_size[root_q] += m_size[root_p];
        }
        else
        {
            m_parent[root_q] = root_p;
            m_size[root_p] += m_size[root_q];
        }
    }
//----------------members---------------------------
    private int[] m_parent;  
    private int[] m_size;  
}

public class Grid
{
    public Grid (int N)
    {
        m_N = N;
        m_cells = new boolean[N * N];
        Arrays.fill (m_cells, false);
        m_opened_count = 0;
        m_uf = new UnionFind (N * N + 2);
    }

    public void OpenCell (int i, int j)
    {
        int idx = i * m_amount + j;

        if (m_cells[idx])
            return;

        m_cells[idx] = true;
        m_opened_count++;

        if (i == 0)
            m_uf.Union (idx, m_amount * m_amount);

        if (i == m_amount - 1)
            m_uf.Union (idx, m_amount * m_amount + 1);

        if ((i > 0) && m_cells[(i - 1) * m_amount + j])
            m_uf.Union (idx, (i - 1) * m_amount + j);

        if ((i < m_amount - 1) && m_cells[(i + 1) * m_amount + j])
            m_uf.Union (idx, (i + 1) * m_amount + j);

        if ((j > 0) && m_cells[i * m_amount + j - 1])
            m_uf.Union (idx, i * m_amount + j - 1);

        if ((j < m_amount - 1) && m_cells[i * m_amount + j + 1])
            m_uf.Union (idx, i * m_amount + j + 1);
    }

    public boolean HasPercolation ()
    {
        return m_uf.Find (m_amount * m_amount) == m_uf.Find (m_amount * m_amount + 1);
    }

    public int OpenedCount ()
    {
        return m_opened_count;
    }

//----------------------members------------------
    private final int m_amount;
    private boolean[] m_cells;
    private UnionFind m_uf;
    private int m_opened_count;
}
