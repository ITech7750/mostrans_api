package predict;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class Graph implements Iterable<Graph.Node> {

    public static abstract class Node {
        public enum State {
            None,
            Queued,
            Visited
        }

        private ArrayList<Node> next = new ArrayList<>();
        private State state = State.None;

        public Node() {
            // Конструктор без параметров
        }

        public Node(int passengerLoad, int distanceToCenter) {
            setPassengerLoad(passengerLoad);
            setDistanceToCenter(distanceToCenter);
        }

        public Collection<Node> getNext() {
            return next;
        }

        public State getState() {
            return state;
        }

        public void setState(State state) {
            this.state = state;
        }

        public void addConnection(Node node) {
            next.add(node);
        }

        // Абстрактные методы для наследников
        public abstract int getPassengerLoad();
        public abstract void setPassengerLoad(int passengerLoad);

        public abstract int getDistanceToCenter();
        public abstract void setDistanceToCenter(int distanceToCenter);
    }

    private ArrayList<Node> nodes = new ArrayList<>();

    public Node getNode(int i) {
        return nodes.get(i);
    }

    public int addNode(Node node) {
        nodes.add(node);
        return nodes.size();
    }

    public void addDirectedEdge(int a, int b) {
        nodes.get(a).getNext().add(nodes.get(b));
    }

    public void addEdge(int a, int b) {
        addDirectedEdge(a, b);
        addDirectedEdge(b, a);
    }

    public boolean hasEdge(int a, int b) {
        Node nodeA = getNode(a);
        Node nodeB = getNode(b);
        return nodeA.getNext().contains(nodeB) && nodeB.getNext().contains(nodeA);
    }

    public int getNodeCount() {
        return nodes.size();
    }

    @Override
    public Iterator<Node> iterator() {
        return nodes.iterator();
    }
}
