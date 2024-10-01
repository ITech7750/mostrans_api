package predict;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class Graph implements Iterable<Graph.Node> {
    public static class Node {
        public enum State {
            None,
            Queued,
            Visited
        }

        private ArrayList<Node> next = new ArrayList<>();
        private int passengerLoad = 0;
        private int distanceToCenter = 0;
        private State state = State.None;

        public Node() {
            this.passengerLoad = 0;
            this.distanceToCenter = 0;
        }

        public Node(int passengerLoad, int distanceToCenter) {
            setPassengerLoad(passengerLoad);
            this.distanceToCenter = distanceToCenter;
        }

        public Collection<Node> getNext() {
            return next;
        }

        public int getPassengerLoad() {
            return passengerLoad;
        }

        public void setPassengerLoad(int passengerLoad) {
            this.passengerLoad = passengerLoad;
        }

        public State getState() {
            return state;
        }

        public void setState(State state) {
            this.state = state;
        }

        public int getDistanceToCenter() {
            return distanceToCenter;
        }
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
