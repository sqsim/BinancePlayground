import com.binance.api.client.domain.event.CandlestickEvent;

import java.util.*;

public class Subscriber {

    String subName;
    Queue<CandlestickEvent> currentSnapShot = new LinkedList<>();
    Map<String,Map<String,SMA>> sma = new HashMap<>();
    public Subscriber(String subName) {
        this.subName = subName;
    }

    public String getSubName() {
        return subName;
    }

    public void setCurrentSnapshot(CandlestickEvent events) {

        System.out.println("name " + subName + " close value "+events.getClose() + " close time "+new Date(events.getCloseTime()));
        if(sma.containsKey(events.getSymbol())){
            Map<String,SMA> smaMap = sma.get(events.getSymbol());
            for(SMA s: smaMap.values() ){
                s.setAndGetSMA(events);
            }
        }

    }

    public void addSMA(String cryptoName,String name,int period){
        String cryptoNameCaps = cryptoName.toUpperCase();
        Map<String,SMA> smaMap = new HashMap<>();
        if(sma.containsKey(cryptoNameCaps)){
             smaMap = sma.get(cryptoNameCaps);
        }
        smaMap.put(name,new SMA(name,period));
        sma.put(cryptoNameCaps,smaMap);

    }
}
