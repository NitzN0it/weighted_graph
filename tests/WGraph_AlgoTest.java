package ex1.tests;
import ex1.src.WGraph_Algo;
import ex1.src.WGraph_DS;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WGraph_AlgoTest {
    WGraph_Algo g_algo = new WGraph_Algo();
    WGraph_DS g = new WGraph_DS();
    double random_weight=0;
    @Test
    void init() {
        create_connected_graph(30);
        g_algo.init(g);
        assertTrue(g_algo.getGraph().nodeSize() == 30);
        create_connected_graph(500);
        g_algo.init(g);
        assertTrue(g_algo.getGraph().nodeSize() == 500);
        create_connected_graph(12000);
        g_algo.init(g);
        assertTrue(g_algo.getGraph().nodeSize() == 12000);
    }

    @Test
    void copy() {
    }

    @Test
    void isConnected() {
        create_connected_graph(12000);
        g_algo.init(g);
        assertTrue(g_algo.isConnected());
        g.removeEdge(1,2);
        assertFalse(g_algo.isConnected());
        g.removeEdge(2,3);
        g.removeEdge(3,4);
        assertFalse(g_algo.isConnected());
        g.connect(1,2,1);
        g.connect(2,3,1);
        assertFalse(g_algo.isConnected());
        g.connect(3,4,1);
        assertTrue(g_algo.isConnected());
    }

    @Test
    void shortestPath() {
        create_connected_graph(12000);
        g_algo.init(g);
        assertEquals(random_weight,g_algo.shortestPathDist(0,11999));
    }
    @Test
    void loav_save() {
        create_connected_graph(100);
        g_algo.init(g);
        g_algo.save("test.obj");
        g_algo.load("test.obj");
        assertTrue(g.equals(g_algo.getGraph()));
        g.removeNode(10);
        assertFalse(g.equals(g_algo.getGraph()));
    }
    private void create_connected_graph (int v)
    {
        for (int i = 0; i < v; i++) {
            g.addNode(i);
        }
        v--;
        int i =0;
        int temp = 0;
        while (v!=0)
        {
            temp = random_number(1,100);
            random_weight += temp;
            g.connect(i,i+1,temp);
            v--;
            i++;
        }
    }
    public int random_number(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}