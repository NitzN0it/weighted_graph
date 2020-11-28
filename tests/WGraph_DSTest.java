package ex1.tests;
import ex1.src.WGraph_DS;
import ex1.src.node_info;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WGraph_DSTest {
    node_info node;
    int [] node_arr;
    WGraph_DS w_graph;

    private void create_graph (int v, int e)
    {
        w_graph = new WGraph_DS();
        create_nodes(v);
        node_connect(v,e);
    }
    private void create_nodes (int n)
    {
        if (n==0)return;
        for (int i = 0; i < n; i++) {
            w_graph.addNode(i);
        }
    }
    private void node_connect (int v,int e)
    {
        int i = 0, j = 1;
        while (e != 0)
        {
            w_graph.connect(i,j,12);
            e--;  if (j==v) {j=0; i++;} j++;
        }
    }

    public int random_number(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    @Test
    void getNode() {
        create_graph(0,0);
        assertFalse(w_graph.getNode(30) != null);
        create_graph(1,0);
        assertTrue(w_graph.getNode(0) != null);
        assertFalse(w_graph.getNode(30) != null);
    }

    @Test
    void hasEdge() {
        create_graph(0,0);
        assertFalse(w_graph.hasEdge(0,3));
        create_graph(1,0);
        assertFalse(w_graph.hasEdge(1,0));
        create_graph(2,1);
        assertTrue(w_graph.hasEdge(0,1));
        assertTrue(w_graph.hasEdge(1,0));
        create_graph(200,200);
        assertTrue(w_graph.hasEdge(0,random_number(0,200)));
        create_graph(350,450);
        assertTrue(w_graph.hasEdge(0,random_number(0,200)));
        create_graph(20,35);
        assertTrue(w_graph.hasEdge(0,random_number(1,35)));
    }


    @Test
    void getV() {
        create_graph(0,0);
        Collection<node_info> v = w_graph.getV();
        assertTrue(v.isEmpty());
        create_graph(100,100);
        v = w_graph.getV();
        assertTrue(v.size() == 100);
        v = w_graph.getV(0);
        assertFalse(v.size() == 0);
    }
    @Test
    void removeNode() {
        create_graph(1,0);
        w_graph.removeNode(0);
        assertTrue(w_graph.nodeSize() == 0);
        create_graph(350,12);
        w_graph.removeNode(3);
        assertTrue(w_graph.nodeSize() == 350-1);
        w_graph.removeNode(0);
        assertTrue(w_graph.nodeSize() == 350-2);
        w_graph.removeNode(0);
        assertTrue(w_graph.nodeSize() == 350-2);
        w_graph.removeNode(349);
        assertTrue(w_graph.nodeSize() == 350-3);
    }

    @Test
    void removeEdge() {
        create_graph(1,0);
        assertTrue(w_graph.edgeSize() == 0);
        w_graph.removeNode(0);
        assertTrue(w_graph.edgeSize() == 0);
        create_graph(350,12);
        assertTrue(w_graph.edgeSize() == 12);
        int temp = w_graph.getV(0).size();
        w_graph.removeNode(0);
        assertTrue(w_graph.edgeSize() == 12-temp);
        create_graph(350,150);
        w_graph.removeEdge(0,1);
        assertTrue(w_graph.edgeSize() == 150-1);
        w_graph.removeEdge(0,100);
        assertTrue(w_graph.edgeSize() == 150-2);
        w_graph.removeEdge(500,100);
        assertTrue(w_graph.edgeSize() == 150-2);
    }

    @Test
    void getMC() {
        create_graph(1,0);
        w_graph.removeNode(0);
        assertTrue(w_graph.getMC() == 1);
        create_graph(350,12);
        assertTrue(w_graph.getMC() == 362);
        w_graph.removeNode(5);
        w_graph.removeNode(5);
        w_graph.removeNode(1);
        w_graph.removeNode(1);
        assertTrue(w_graph.getMC() == 364);
        w_graph.removeEdge(0,2);
        w_graph.removeEdge(0,0);
        assertTrue(w_graph.getMC() == 365);
    }
}