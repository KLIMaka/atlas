package types;

import java.util.HashMap;
import java.util.Map;

public class NodeFactory
{

    private Map<Integer, GeomNode> m_nodes = new HashMap<Integer, GeomNode>();
    private int                    last_id = 1;

    public NodeFactory()
    {
    }

    public GeomNode create(GeomNode parent)
    {
        GeomNode node = new GeomNode(parent, last_id);
        m_nodes.put(last_id, node);
        last_id++;

        if (parent != null)
            parent.attach(node);

        return node;
    }

    public GeomNode getById(int id)
    {
        return m_nodes.get(id);
    }
}
