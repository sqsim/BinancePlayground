import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiWebSocketClient;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Timer;
import java.util.TimerTask;

public class Main {

    public static void main(String[] args)  {


        BinanceApiWebSocketClient client = BinanceApiClientFactory.newInstance().newWebSocketClient();

        // Obtain 1m candlesticks in real-time for ETH/BTC

        Publisher bnbbtc = new Publisher("bnbbtc", client);
        bnbbtc.startStream();

        Subscriber sub1 = new Subscriber("sub1");
        bnbbtc.subscribe(sub1);
        sub1.addSMA(bnbbtc.getCryptoName(),"sma5", 5);
        sub1.addSMA(bnbbtc.getCryptoName(),"sma10", 10);

        Subscriber sub2 = new Subscriber("sub2");
        bnbbtc.subscribe(sub2);
        sub2.addSMA(bnbbtc.getCryptoName(),"sma15", 15);

        Publisher ethbtc = new Publisher("ethbtc", client);
        ethbtc.startStream();
        ethbtc.subscribe(sub1);
        sub1.addSMA(ethbtc.getCryptoName(),"sma5", 5);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    System.out.println("Stopping "+bnbbtc.getCryptoName()+" after 10 secs");
                    bnbbtc.stopStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        },10000);
    }
}
