package predict;

import java.util.ArrayDeque;

public class Predictor {
    private static class BfsNodeInfo {
        private Graph.Node node;
        private int load;

        BfsNodeInfo(Graph.Node node, int load) {
            setNode(node);
            setLoad(load);
        }

        public Graph.Node getNode() {
            return node;
        }

        public void setNode(Graph.Node node) {
            this.node = node;
        }

        public int getLoad() {
            return load;
        }

        public void setLoad(int load) {
            this.load = load;
        }
    }

    private Graph graph;
    private int startNodeIndex;
    private int additionalLoad;
    private int centerRadius;

    public Predictor setGraph(Graph graph) {
        this.graph = graph;
        return this;
    }

    public Predictor setStartNodeIndex(int startNodeIndex) {
        this.startNodeIndex = startNodeIndex;
        return this;
    }

    public Predictor setAdditionalLoad(int additionalLoad) {
        this.additionalLoad = additionalLoad;
        return this;
    }

    public Predictor setCenterRadius(int centerRadius) {
        this.centerRadius = centerRadius;
        return this;
    }

    public void calculateLoad() {
        clearStates();
        bfs();
    }

    private void clearStates() {
        for (Graph.Node node : graph) {
            node.setState(Graph.Node.State.None);
        }
    }

    private void bfs() {
        ArrayDeque<BfsNodeInfo> front = new ArrayDeque<>();
        front.addLast(new BfsNodeInfo(graph.getNode(startNodeIndex), additionalLoad));

        while (!front.isEmpty()) {
            BfsNodeInfo curr = front.pollFirst();
            curr.getNode().setState(Graph.Node.State.Visited);

            int count = 0;
            int countToCenter = 0;
            for (Graph.Node next : curr.getNode().getNext()) {
                if (next.getState() != Graph.Node.State.None) {
                    continue;
                }

                count++;
                if (next.getDistanceToCenter() <= curr.getNode().getDistanceToCenter()) {
                    countToCenter++;
                }
            }
            count++;

            int leave = (curr.getLoad() + (count * 5) - 1) / (count * 5);
            curr.setLoad(curr.getLoad() - leave);
            count--;

            for (Graph.Node next : curr.getNode().getNext()) {
                if (next.getState() == Graph.Node.State.None) {
                    int nextLoad = (curr.getLoad() + count - 1) / count;

                    if (curr.getNode().getDistanceToCenter() > centerRadius && count != countToCenter) {
                        if (next.getDistanceToCenter() <= curr.getNode().getDistanceToCenter()) {
                            nextLoad = (curr.getLoad() * 4 + (5 * countToCenter) - 1) / (5 * countToCenter);
                            countToCenter--;
                        } else {
                            nextLoad = (curr.getLoad() + (count - countToCenter) * 5 - 1) / ((count - countToCenter) * 5);
                        }
                    }
                    curr.setLoad(curr.getLoad() - nextLoad);
                    count--;

                    front.addLast(new BfsNodeInfo(next, nextLoad));
                    next.setState(Graph.Node.State.Queued);
                }
            }

            leave += curr.getLoad();
            curr.getNode().setPassengerLoad(curr.getNode().getPassengerLoad() + leave);
        }
    }
}
