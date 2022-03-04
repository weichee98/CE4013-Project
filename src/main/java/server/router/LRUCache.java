package main.java.server.router;

import main.java.shared.udp.UDPMessage;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class LRUCache {
    private final LinkedHashMap<UUID, UDPMessage> cache;
    private final int CAPACITY;

    public LRUCache(int capacity) {
        this.CAPACITY = capacity;
        this.cache = new LinkedHashMap<UUID, UDPMessage>(capacity, 0.75f, true){
            protected boolean removeEldestEntry(Map.Entry eldest) {
                return size() > CAPACITY;
            }
        };
    }
    public UDPMessage get(UUID key) {
        return this.cache.getOrDefault(key, null);
    }
    public void set(UUID key, UDPMessage value) {
        this.cache.put(key, value);
    }
}