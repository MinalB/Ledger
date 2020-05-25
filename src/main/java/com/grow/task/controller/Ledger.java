package com.grow.task.controller;

import com.grow.task.model.Node;

import java.util.*;
import java.util.stream.Collectors;

public class Ledger {

    private List<Node> nodes = new ArrayList<>();
    private Map<Integer, String> ledgerTransactions = new LinkedHashMap<>();

    // TODO: Transaction ID should at least be a long with a database preferentially
    public boolean postTransaction(String nodeId, int transactionId) {

        if(!ledgerTransactions.containsKey(transactionId)){
            synchronized (this.ledgerTransactions) {
                if (getNode(nodeId) == null) {
                    nodes.add(new Node(nodeId));
                }
                ledgerTransactions.put(transactionId, nodeId);
            }
            return true;
        }
        return false;

    }

    // Subscribe when the subscribing node is present and add if initiating node absent
    public boolean subscribe(String initiatingNodeId, String subscribingNodeId){
        Node subscribingNode = getNode(subscribingNodeId);
        if(subscribingNode!=null){
            Node initiatingNode = getNode(initiatingNodeId);
            if(initiatingNode==null){
                initiatingNode = new Node(initiatingNodeId);
                nodes.add(initiatingNode);
            }
            initiatingNode.addToSubscriptionList(subscribingNodeId);
            subscribingNode.addToSubscribersList(initiatingNodeId);
            return true;
        }

        return false;
    }

    //Unsubscribe when both the mentioned nodes are present
    public boolean unsubscribe(String initiatingNodeId, String subscribingNodeId){
        Node initiatingNode = getNode(initiatingNodeId);
        Node subscribingNode = getNode(subscribingNodeId);
        //Better handled with referential integrity
        if(subscribingNode!=null && initiatingNode!=null){
           initiatingNode.removeFromSubscriptionList(subscribingNodeId);
           subscribingNode.removeFromSubscribersList(initiatingNodeId);
        }
        return false;
    }

    public List<Integer> getAllTransactions(String nodeId, boolean latestFirst){

        List<Integer> transactions = null;
        Node node = getNode(nodeId);
        if(node!=null) {

            //LIFO
            if (latestFirst) {
                transactions = ledgerTransactions.entrySet().stream()
                        .filter(key -> (key.getValue().equals(nodeId) || node.getSubscriptionList().contains(key.getValue())))
                        .map(Map.Entry::getKey).collect(Collectors.toList());
            } else {
                //FIFO
                transactions = ledgerTransactions.entrySet().stream()
                        .filter(key -> (key.getValue().equals(nodeId) || node.getSubscriptionList().contains(key.getValue())))
                        .map(Map.Entry::getKey).collect(Collectors.toList());
                Collections.reverse(transactions);
            }
        }

        return transactions;
    }

    public Node getNode(String nodeId){
        return nodes.stream().filter(node -> node.getNodeId().equalsIgnoreCase(nodeId)).findFirst().orElse(null);
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public int getNumberOfTransactions(){
        return ledgerTransactions.size();
    }

}
