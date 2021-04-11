import com.binance.api.client.BinanceApiWebSocketClient;
import com.binance.api.client.domain.event.CandlestickEvent;
import com.binance.api.client.domain.market.CandlestickInterval;

import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class Publisher {

    String cryptoName;
    Queue<CandlestickEvent> cryptoTicks = new LinkedList<>();
    Map<String, Subscriber> subscriberHashMap = new HashMap<>();
    BinanceApiWebSocketClient client;
    int MAX_TICKS_SIZE = 50;
    Closeable stream;

    public Publisher(String cryptoName, BinanceApiWebSocketClient client) {
        this.cryptoName = cryptoName;
        this.client = client;
    }

    public String getCryptoName() {
        return cryptoName;
    }

    public void startStream() {
        System.out.println("Starting " + cryptoName+ " stream");
        stream = client.onCandlestickEvent(cryptoName, CandlestickInterval.ONE_MINUTE, response -> notify(response));

    }

    public void stopStream() throws IOException {
        System.out.println("Stopping " + cryptoName+ " stream");
        stream.close();
    }

    private void notify(CandlestickEvent event) {
//        cryptoTicks.add(event);
//        if (cryptoTicks.size() > MAX_TICKS_SIZE) {
//            cryptoTicks.remove();
//        }
        System.out.println("");
        System.out.println("cryto name " +cryptoName);
        for (Map.Entry<String, Subscriber> subs : subscriberHashMap.entrySet()) {
            subs.getValue().setCurrentSnapshot(event);
        }
    }

    public void subscribe(Subscriber subscriber) {
        subscriberHashMap.put(subscriber.getSubName(), subscriber);
    }

    public void unsubscribe(Subscriber subscriber) {
        subscriberHashMap.remove(subscriber.getSubName());
    }

}
