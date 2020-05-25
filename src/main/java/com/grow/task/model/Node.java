package com.grow.task.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Node {

    private String NodeId;

    private Set<String> subscriptionList = new HashSet<>();

    private Set<String> subscribers = new HashSet<>();

    public String getNodeId() {
        return NodeId;
    }

    public Node(String nodeId) {
        NodeId = nodeId;
    }

    public Set<String> getSubscriptionList() {
        return subscriptionList;
    }

    public void addToSubscriptionList(String subscribingNodeId) {
        this.subscriptionList.add(subscribingNodeId);
    }

    public void removeFromSubscriptionList(String subscribingNodeId) {
        this.subscriptionList.remove(subscribingNodeId);
    }

    public void addToSubscribersList(String initiatingNodeId) {
        this.subscribers.add(initiatingNodeId);
    }

    public void removeFromSubscribersList(String initiatingNodeId) {
        this.subscribers.remove(initiatingNodeId);
    }
}
