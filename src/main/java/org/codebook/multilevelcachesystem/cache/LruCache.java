package org.codebook.multilevelcachesystem.cache;

import java.util.HashMap;

public class LruCache<K,V> implements Cache<K,V> {
    private final int capacity;

    private final HashMap<K, Node<K,V>> map;

    private final DoublyLinkedList<K,V> list;

    public LruCache(int capacity){
        this.capacity=capacity;
        this.map=new HashMap<>();
        this.list=new DoublyLinkedList<>();
    }

    public synchronized V get(K key){
        if(!map.containsKey(key)){
            return null;
        }
        Node<K,V> node=map.get(key);
        if(isExpired(node)){
            list.removeNode(node);
            map.remove(node.key);
            return null;
        }
        list.modeToTail(node); // lru implementation -> moving the recent used to tail
        return node.value;
    }

    public synchronized void set(K key, V value, long ttlMillis){
        long expirationTime = ttlMillis>0 ? System.currentTimeMillis()+ttlMillis : 0;
        if(map.containsKey(key)){
            Node<K,V> existing = map.get(key);
            existing.value=value;
            existing.expirationTime=expirationTime;
            list.modeToTail(existing);
        } else {
            if(map.size()>=capacity){
                Node<K,V> removed=list.removeHead();
                if(removed!=null){
                    map.remove(removed.key);
                }
            }
            Node<K,V> newNode = new Node<>(key, value, expirationTime);
            list.addToTail(newNode);
            map.put(key,newNode);
        }
    }

    public synchronized void remove(K key){
        if(map.containsKey(key)){
            Node<K,V> node = map.get(key);
            list.removeNode(node);
            map.remove(key);
        }
    }

    public void printOrder(){
        list.printList();
    }

    private boolean isExpired(Node<K, V> node){
        return node.expirationTime>0 && node.expirationTime < System.currentTimeMillis();
    }

    // Doubly Linked List for tracking access order
    private static class DoublyLinkedList<K,V> {
        private Node<K,V> head;
        private Node<K,V> tail;

        void addToTail(Node<K,V> node){
            if(tail==null){
                head=tail=node;
                return;
            }
            tail.next=node;
            node.prev=tail;
            tail=node;
        }

        void modeToTail(Node<K,V> node) {
            if(tail==node) return;
            removeNode(node);
            addToTail(node);
        }

        Node<K,V> removeHead() {
            if(head==null) return null;
            Node<K,V> removed=head;
            removeNode(head);
            return removed;
        }

        void removeNode(Node<K,V> node){
            if(node.prev==null){
                head=node.next;
            }
            else{
                node.prev.next=node.next;
            }
            if(node.next==null){
                tail=node.prev;
            }
            else{
                node.next.prev=node.prev;
            }
            node.next=node.prev=null;
        }

        void printList() {
            Node<K,V> current=head;
            System.out.println("Cache order: ");
            while(current!=null){
                System.out.println(current.key + " " + current.value);
                current=current.next;
            }
        }
    }

    // Node in a cache list
    private static class Node<K,V>{
        K key;
        V value;
        long expirationTime; // 0 means no expiration
        Node<K,V> prev;
        Node<K,V> next;
        Node(K key, V value, long expirationTime){
            this.key = key;
            this.value = value;
            this.expirationTime = expirationTime;
        }
    }
}
