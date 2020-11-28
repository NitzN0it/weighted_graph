package ex1.src;

import java.io.Serializable;
import java.util.*;

class NodeInfo implements node_info, Serializable {
    private static int keyCounter = 0;
    private int key = 0;
    private String info = "";
    double tag = 0;

    public NodeInfo() {
        key = keyCounter;
        keyCounter++;
    }

    public NodeInfo(int k) {
        key = k;
    }

    @Override
    public int getKey() {
        return key;
    }
    @Override
    public String getInfo() {
        return info;
    }

    @Override
    public void setInfo(String s) {
        info = s;
    }

    @Override
    public double getTag() {
        return tag;
    }

    @Override
    public void setTag(double t) {
        tag=t;
    }
}

class node_infoComperator implements Comparator<node_info>
{
    @Override
    public int compare(node_info o1, node_info o2) {
        if (o1.getTag() < o2.getTag())
            return -1;
        if (o1.getTag() > o2.getTag())
            return 1;
        return 0;
    }
}

public class WGraph_DS implements weighted_graph, Serializable {
    private HashMap<Integer, HashMap<Integer, Double>> vertecies = new HashMap<>();
    private HashMap<Integer, node_info> node_map = new HashMap<>();
    private int v_counter = 0;
    private int MC = 0;
    public WGraph_DS()
    {

    }
    @Override
    public node_info getNode(int key) {
        return node_map.get(key);
    }

    @Override
    public boolean hasEdge(int node1, int node2) {
        if (node1 == node2) return true;
        if ( !(vertecies.containsKey(node1)) || !(vertecies.containsKey(node2)) ) return false;
        return vertecies.get(node1).containsKey(node2);
    }

    @Override
    public double getEdge(int node1, int node2) {
        if ((vertecies.containsKey(node1)) && (vertecies.get(node1).containsKey(node2)))
        {
            return vertecies.get(node1).get(node2);
        }
        return -1;
    }

    @Override
    public void addNode(int key) {
        if (!(node_map.containsValue(key))) {
            MC++;
            node_info n = new NodeInfo(key);
            node_map.put(key, n);
        }
    }

    @Override
    public void connect(int node1, int node2, double w) {
        if ( !(node_map.containsKey(node1) || !(node_map.containsKey(node2))) )
            return;
        if (node1 == node2)
            return;
        if ( (vertecies.containsKey(node1)) && (vertecies.get(node1).containsKey(node2)) )
            return;
        if (vertecies.containsKey(node1))
        {
            vertecies.get(node1).put(node2,w);
        }
        else
        {
            HashMap<Integer,Double> tempVertex = new HashMap<>();
            tempVertex.put(node2,w);
            vertecies.put(node1,tempVertex);
        }

        if (vertecies.containsKey(node2))
        {
            vertecies.get(node2).put(node1,w);
        }
        else
        {
            HashMap<Integer,Double> tempVertex = new HashMap<>();
            tempVertex.put(node1,w);
            vertecies.put(node2,tempVertex);
        }
        MC++;
        v_counter++;
    }

    @Override
    public Collection<node_info> getV() {
        return node_map.values();
    }

    @Override
    public Collection<node_info> getV(int node_id) {
        LinkedList<node_info> lst = new LinkedList<>();

        if (vertecies.containsKey(node_id))
        {
            for (int i : vertecies.get(node_id).keySet()) {
                lst.add(getNode(i));
            }
        }
        return lst;
    }

    @Override
    public node_info removeNode(int key) {
        if (node_map.containsKey(key)) {
            if (vertecies.containsKey(key))
            {
                for (int node:vertecies.get(key).keySet()) {
                    vertecies.get(node).remove(key);
                }
                v_counter -= vertecies.remove(key).values().size();
                MC++;
                return node_map.remove(key);
            }

        }
        return null;
    }

    @Override
    public void removeEdge(int node1, int node2) {
        if (!(vertecies.containsKey(node1)) || !(vertecies.containsKey(node2))) return;
        if (vertecies.get(node1).containsKey(node2))
        {
            vertecies.get(node1).remove(node2);
            vertecies.get(node2).remove(node1);
            v_counter--;
            MC++;
        }
    }

    @Override
    public int nodeSize() {
        return node_map.size();
    }

    @Override
    public int edgeSize() {
        return v_counter;
    }

    @Override
    public int getMC() {
        return MC;
    }

    public boolean equals (Object o)
    {
        weighted_graph g = (weighted_graph) o;
      if ((g.nodeSize() != this.nodeSize()) && (g.edgeSize() != this.edgeSize())) return false;
      for (node_info node:this.getV())
      {
          Iterator<node_info> it = g.getV().iterator();
          boolean foundNode = false;
          while (it.hasNext())
          {
              node_info temp = it.next();
                if (this.getV(node.getKey()).size() == g.getV(temp.getKey()).size())
                    foundNode = true;
                if ((!(it.hasNext())) && (foundNode == false))
                    return false;
          }
      }
        return true;

        /*
        if (this == o)
            return true;
        if (o.getClass().equals(this))
        {
            weighted_graph g = (weighted_graph) o;
            if (g.edgeSize() == this.edgeSize())
                if (g.nodeSize() == this.nodeSize()) {
                    for (node_info node : g.getV()) {
                        for (node_info neighbors : g.getV(node.getKey())) {
                            if (!(vertecies.get(node.getKey()).containsKey(neighbors)))
                                return false;
                        }
                    }
                }
        }
        else
            return false;
        return true;

         */
    }
}