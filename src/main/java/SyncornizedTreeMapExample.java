import com.revolut.assignment.datamodel.Account;
import com.revolut.assignment.datamodel.History;

import java.math.BigDecimal;
import java.util.*;

public class SyncornizedTreeMapExample {

    TreeMap<Long, Account> sortedMap = new TreeMap();
    Map <Long, Account> datastore = Collections.synchronizedSortedMap(sortedMap);

    public static void main (String [] args){

        SyncornizedTreeMapExample app = new SyncornizedTreeMapExample();
        app.datastore.put(1234L, new Account(new BigDecimal(1000)));
        app.datastore.put(1234L, new Account(new BigDecimal(1000)));
        app.datastore.put(1234L, new Account(new BigDecimal(1000)));
        System.out.println(app.datastore.get(1234L));

        app.printAccountsAmount();

        System.out.println(new BigDecimal(new Random().nextInt()));
    }

    public void printAccountsAmount(){
        datastore.values().forEach((p) -> System.out.println(p));
    }
}
