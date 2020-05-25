package com.grow.task;

import com.grow.task.controller.Ledger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class LedgerOperationsTest {

    Ledger ledger;

    @Before
    public void setUp(){
        ledger = new Ledger();
    }

    //Add transaction
    @Test
    public void addTransaction(){

        //execute
        ledger.postTransaction("A", 100);

        Assert.assertEquals(1, ledger.getNodes().size());
    }

    //Add transaction on existing node
    @Test
    public void addTransactionOnExistingNode(){

        //execute
        ledger.postTransaction("A", 100);
        ledger.postTransaction("A", 101);

        Assert.assertEquals(1, ledger.getNodes().size());
        Assert.assertEquals(2, ledger.getNumberOfTransactions());
    }


    //Add transaction on a node when transaction already exists
    @Test
    public void addTransactionOnExistingNodeWhenAlreadyPresent(){
        //execute
        ledger.postTransaction("B", 101);
        ledger.postTransaction("C", 101); // rejected transaction
        ledger.postTransaction("C", 102);
        ledger.postTransaction("B", 103);
        ledger.postTransaction("C", 104);

        Assert.assertEquals(2, ledger.getNodes().size());
        Assert.assertEquals(4, ledger.getNumberOfTransactions());
    }


    //Subscribe when both InitiatingNode and SubscribingNode are present
    @Test
    public void subscribeWhenBothNodesPresent(){
        //setup
        ledger.postTransaction("A", 101);
        ledger.postTransaction("B", 102);

        //execute
        ledger.subscribe("A", "B");

        Assert.assertEquals(2, ledger.getNodes().size());
        Assert.assertEquals(2, ledger.getNumberOfTransactions());
        Assert.assertEquals(1, ledger.getNode("A").getSubscriptionList().size());
    }

    //Subscribe when InitiatingNode is absent and SubscribingNode is present
    @Test
    public void subscribeWhenInitiatingNodeAbsentAndSubscribingNodePresent(){
        //setup
        ledger.postTransaction("B", 102);

        //execute
        ledger.subscribe("A", "B");

        Assert.assertEquals(2, ledger.getNodes().size());
        Assert.assertEquals(1, ledger.getNumberOfTransactions());
        Assert.assertEquals(1, ledger.getNode("A").getSubscriptionList().size());
    }

    //Subscribe when InitiatingNode is present and SubscribingNode is absent
    @Test
    public void subscribeWhenInitiatingNodePresentAndSubscribingNodeAbsent(){
        //setup
        ledger.postTransaction("A", 102);

        //execute
        ledger.subscribe("A", "B");


        Assert.assertNull(ledger.getNode("B"));
        Assert.assertEquals(1, ledger.getNodes().size());
        Assert.assertEquals(1, ledger.getNumberOfTransactions());
        Assert.assertEquals(0, ledger.getNode("A").getSubscriptionList().size());
    }

    //Subscribe when both InitiatingNode and SubscribingNode are absent
    @Test
    public void subscribeWhenInitiatingNodeAbsentAndSubscribingNodeAbsent(){
        //execute
        ledger.subscribe("A", "B");

        Assert.assertNull(ledger.getNode("A"));
        Assert.assertNull(ledger.getNode("B"));
        Assert.assertEquals(0, ledger.getNodes().size());
        Assert.assertEquals(0, ledger.getNumberOfTransactions());

    }


    //Unsubscribe when both InitiatingNode and SubscribingNode are present
    @Test
    public void unsubscribeWhenBothNodesPresent(){
        //setup
        ledger.postTransaction("A", 101);
        ledger.postTransaction("B", 102);
        ledger.subscribe("A", "B");
        Assert.assertEquals(1, ledger.getNode("A").getSubscriptionList().size());

        //execute
        ledger.unsubscribe("A", "B");
        Assert.assertEquals(0, ledger.getNode("A").getSubscriptionList().size());

        Assert.assertEquals(2, ledger.getNodes().size());
        Assert.assertEquals(2, ledger.getNumberOfTransactions());

    }

    //Unubscribe when InitiatingNode is absent and SubscribingNode is present
    @Test
    public void unsubscribeWhenInitiatingNodeAbsentAndSubscribingNodePresent(){
        //setup
        ledger.postTransaction("B", 102);

        Assert.assertNull(ledger.getNode("A"));
        Assert.assertEquals(1, ledger.getNodes().size());
        Assert.assertEquals(1, ledger.getNumberOfTransactions());

        //execute
        Assert.assertFalse(ledger.unsubscribe("A", "B"));
    }

    //Unubscribe when InitiatingNode is present and SubscribingNode is absent
    @Test
    public void unsubscribeWhenInitiatingNodePresentAndSubscribingNodeAbsent(){
        //setup
        ledger.postTransaction("A", 102);

        //execute
        ledger.unsubscribe("A", "B");

        Assert.assertNull(ledger.getNode("B"));
        Assert.assertEquals(1, ledger.getNodes().size());
        Assert.assertEquals(1, ledger.getNumberOfTransactions());
        Assert.assertFalse(ledger.unsubscribe("A", "B"));
    }

    //Unubscribe when both InitiatingNode and SubscribingNode are absent
    @Test
    public void unsubscribeWhenInitiatingNodeAbsentAndSubscribingNodeAbsent(){
        //execute
        ledger.unsubscribe("A", "B");

        Assert.assertNull(ledger.getNode("A"));
        Assert.assertNull(ledger.getNode("B"));
        Assert.assertEquals(0, ledger.getNodes().size());
        Assert.assertEquals(0, ledger.getNumberOfTransactions());
        Assert.assertFalse(ledger.unsubscribe("A", "B"));

    }


    //Get Transactions on a single node
    @Test
    public void getTransactionOnSingleNode(){
        //setup
        ledger.postTransaction("A", 100);
        ledger.postTransaction("A", 101);

        Assert.assertEquals(1, ledger.getNodes().size());
        Assert.assertEquals(2, ledger.getNumberOfTransactions());

        //execute
        List<Integer> transactions = ledger.getAllTransactions("A", true);

        Assert.assertEquals(2, transactions.size());
        Assert.assertEquals(new Integer(100), transactions.get(0));
        Assert.assertEquals(new Integer(101), transactions.get(1));

    }

    //Get Transactions on node which is not present
    @Test
    public void getTransactionOnAbsentNode(){

        //execute
        List<Integer> transactions = ledger.getAllTransactions("B", true);

        Assert.assertNull(transactions);

    }


    //Get Transactions on node with subscription
    @Test
    public void getTransactionOnNodeWithSubscription(){
        //setup
        ledger.postTransaction("A", 100);
        ledger.postTransaction("B", 101);
        ledger.postTransaction("C", 102);
        ledger.postTransaction("B", 103);
        ledger.postTransaction("C", 104);

        Assert.assertEquals(3, ledger.getNodes().size());
        Assert.assertEquals(5, ledger.getNumberOfTransactions());

        ledger.subscribe("A", "B");

        //execute
        List<Integer> transactions = ledger.getAllTransactions("A", true);

        Assert.assertEquals(3, transactions.size());
        Assert.assertEquals(new Integer(100), transactions.get(0));
        Assert.assertEquals(new Integer(101), transactions.get(1));
        Assert.assertEquals(new Integer(103), transactions.get(2));

    }



    //Get Transactions on node with subscription in Latest added first (LIFO)
    @Test
    public void getTransactionOnNodeWithSubscriptionLatestFirst(){
        //setup
        ledger.postTransaction("A", 100);
        ledger.postTransaction("B", 101);
        ledger.postTransaction("E", 102);
        ledger.postTransaction("B", 103);
        ledger.postTransaction("E", 104);
        ledger.postTransaction("C", 105);
        ledger.postTransaction("B", 106);

        Assert.assertEquals(4, ledger.getNodes().size());
        Assert.assertEquals(7, ledger.getNumberOfTransactions());

        ledger.subscribe("A", "B");
        ledger.subscribe("A", "C");

        //execute
        List<Integer> transactions = ledger.getAllTransactions("A", true);

        Assert.assertEquals(5, transactions.size());
        Assert.assertEquals(new Integer(100), transactions.get(0));
        Assert.assertEquals(new Integer(101), transactions.get(1));
        Assert.assertEquals(new Integer(103), transactions.get(2));
        Assert.assertEquals(new Integer(105), transactions.get(3));
        Assert.assertEquals(new Integer(106), transactions.get(4));

    }

    //Get Transactions on node with subscription in Latest added last (FIFO)
    @Test
    public void getTransactionOnNodeWithSubscriptionLatestLast(){
        //setup

        ledger.postTransaction("A", 100);
        ledger.postTransaction("B", 101);
        ledger.postTransaction("E", 102);
        ledger.postTransaction("B", 103);
        ledger.postTransaction("E", 104);
        ledger.postTransaction("C", 105);
        ledger.postTransaction("B", 106);

        Assert.assertEquals(4, ledger.getNodes().size());
        Assert.assertEquals(7, ledger.getNumberOfTransactions());

        ledger.subscribe("A", "B");
        ledger.subscribe("A", "C");

        //execute
        List<Integer> transactions = ledger.getAllTransactions("A", false);

        Assert.assertEquals(5, transactions.size());
        Assert.assertEquals(new Integer(106), transactions.get(0));
        Assert.assertEquals(new Integer(105), transactions.get(1));
        Assert.assertEquals(new Integer(103), transactions.get(2));
        Assert.assertEquals(new Integer(101), transactions.get(3));
        Assert.assertEquals(new Integer(100), transactions.get(4));
    }
}
