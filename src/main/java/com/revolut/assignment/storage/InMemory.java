package com.revolut.assignment.storage;

import com.revolut.assignment.App;
import com.revolut.assignment.datamodel.Account;

import java.util.*;
import java.util.logging.Logger;

public class InMemory {
    private static TreeMap<UUID, Account> sortedMap = new TreeMap();
    private static Map <UUID, Account> datastore = Collections.synchronizedSortedMap(sortedMap);

    private static final Logger logger = Logger.getLogger(InMemory.class.getName());

    public static UUID crateAccount(){
        UUID uuid = UUID.randomUUID();
        if (!datastore.containsKey(uuid)){ // I think its impossible case, but...
            datastore.put(uuid, new Account());
        } else {
            logger.warning("Impossible, but its happened - the UUID is duplicated in my application!!!");
        }
        return uuid;
    }

    public static Account getAccountByUUID(UUID uuid){
        return datastore.get(uuid);
    }

    public static Set<UUID> getAllAccountsUUIDSet(){ // only for testing purpose
        return datastore.keySet();
    }

    public static Collection<Account> getAllAccounts(){ //only for testing purpose
        return datastore.values();
    }
}