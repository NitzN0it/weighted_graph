package ex1.src;

import java.io.*;
import java.util.*;

public class WGraph_Algo implements weighted_graph_algorithms, Serializable{
    weighted_graph graph;
    @Override
    public void init(weighted_graph g) {
        graph = g;
    }

    @Override
    public weighted_graph getGraph() {
        return graph;
    }

    @Override
    public weighted_graph copy() {
        weighted_graph new_graph = new WGraph_DS();
        for (node_info node: graph.getV()) {
            node_info new_node = new NodeInfo(node.getKey());
            new_node.setInfo(node.getInfo());
            new_node.setTag(node.getTag());
        }
        for (node_info node: new_graph.getV()) {
            for (node_info neighbor : graph.getV(node.getKey())) {
                double w = graph.getEdge(node.getKey(),neighbor.getKey());
                new_graph.connect(node.getKey(),neighbor.getKey(),w);
            }
        }
        return new_graph;
    }

    @Override
    public boolean isConnected() {

        if (graph.edgeSize() + 1 < graph.nodeSize()) { // checks if there is minimal number of edges.
            return false;
        }
        if ((graph.nodeSize() == 1) && (graph.edgeSize() == 0))
            return true;
        if ((graph.edgeSize() == 0) && (graph.nodeSize() == 0))
        {
            return true;
        }
        if ((graph.edgeSize() == 0) && (graph.nodeSize() > 1))
        {
            return false;
        }
        tag_reset();
        // BFS algorithm without adding parent, counting the nodes visited and comparing to number of total nodes.
        Collection<node_info> v = graph.getV();
        Iterator<node_info> it = v.iterator();
        node_info first_node = it.next();
        first_node.setTag(2);
        if (v.size() == 0) return true;
        LinkedList<node_info> queue = new LinkedList<>();
        queue.add(first_node);
        int i = 1;
        while (!(queue.isEmpty())) {
            for (node_info node : graph.getV(queue.remove().getKey())) {
                if (node.getTag() != 2) {
                    i++; // count number of nodes visited.
                    node.setTag(2); // tag node as visited.
                    queue.add(node);
                }
            }
        }
        if (i == v.size()) // if visited same number of nodes as the nodes in the ex0.graph.
        {
            tag_reset();
            return true;
        }
        return false;

    }
    private void tag_reset ()
    {
        for (node_info node:graph.getV()) {
            node.setTag(0);
        }
    }
    /*
    ** Sets all the node's tags to infinity - for dijkstra algorithm.
     */
    private void tag_inf()
    {
        for (node_info node:graph.getV())
        {
            node.setTag(Double.MAX_VALUE);
            node.setInfo("");
        }
    }
    @Override
    public double shortestPathDist(int src, int dest) {
        PriorityQueue<node_info> queue = new PriorityQueue<>(new node_infoComperator());
        double path = 0;
        tag_inf();
        node_info current_node = graph.getNode(src);
        queue.add(current_node);
        current_node.setTag(0);
        while (!(queue.isEmpty()))
        {
            current_node = queue.poll();
            if (current_node.getInfo().isBlank())
            {
                current_node.setInfo(' '+current_node.getInfo());
                if (current_node.getKey() == dest)
                    return current_node.getTag();
                for (node_info node: graph.getV(current_node.getKey()))
                {
                    path = current_node.getTag() + graph.getEdge(current_node.getKey(),node.getKey());
                    if (path < node.getTag())
                    {
                        node.setTag(path);
                        queue.add(node);
                    }
                }
            }
        }
        return -1;
    }

    @Override
    public List<node_info> shortestPath(int src, int dest) {
        PriorityQueue<node_info> queue = new PriorityQueue<>(new node_infoComperator()); // priority queue for traveling the shortest path nodes.
        HashMap<node_info,node_info> parents = new HashMap<>(); // HashMap collects the traveled nodes and their "parents" - for creating the path itself.
        LinkedList<node_info> lst = new LinkedList<>(); // list contains the shortest path - for return.
        if (src == dest)
        {
            lst.add(graph.getNode(src));
            return lst;
        }
        if ( (graph.getNode(src) == null) || (graph.getNode(dest) == null) )
            return lst;
        double path = 0;
        tag_inf(); // set all tags of the nodes to infinity (as the path from the current node).
        node_info current_node = graph.getNode(src); // set the current node as the source node
        queue.add(current_node);
        current_node.setTag(0); // set the source node distance 0.
        parents.put(current_node,current_node); // add the source node to the path collector, with "no parent".
        while (!(queue.isEmpty()))
        {
            current_node = queue.poll();
            if (current_node.getInfo().isBlank())
            {
                current_node.setInfo(' '+current_node.getInfo()); // node.info empty == unvisited node, node.info starts with ' ' == visited node.
                if (current_node.getKey() == dest)
                    break;
                for (node_info node: graph.getV(current_node.getKey()))
                {
                    path = current_node.getTag() + graph.getEdge(current_node.getKey(),node.getKey());
                    if (path <= node.getTag())
                    {
                        node.setTag(path);
                        queue.add(node);
                        if (parents.containsKey(node))
                        {
                            parents.remove(node);
                        }
                        parents.put(node,current_node);
                    }
                }
            }
        }
        if (graph.getNode(dest).getInfo().isBlank()) //if i haven't got to the destination - return empty list.
        {
            return lst;
        }
        // turn the hashmap to linked list with the shortest path.
        node_info temp = graph.getNode(dest);
        lst = new LinkedList<>();
        while (parents.get(temp) != temp)
        {
            lst.add(temp);
            temp = parents.get(temp);
        }
        lst.add(graph.getNode(src));
        Collections.reverse(lst);
        return lst;
    }

    @Override
    public boolean save(String file) {
        try {
            FileOutputStream fout = new FileOutputStream(file);
            ObjectOutputStream  oos = new ObjectOutputStream(fout);
            oos.writeObject(graph);
            fout.close();
            oos.close();
            return true;
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean load(String file) {
        try {
            FileInputStream streamIn = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(streamIn);
            weighted_graph read = (weighted_graph) ois.readObject();
            this.graph = read;
            streamIn.close();
            ois.close();
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }
}
