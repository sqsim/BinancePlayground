import com.binance.api.client.domain.event.CandlestickEvent;

import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;


public class SMA {

    Queue<Double> smaQueue = new LinkedList<>();
    int period;
    String name;
    Long smaTime;

    public SMA(String name, int period) {
        this.name = name;
        this.period = period;
    }

    public void setAndGetSMA(CandlestickEvent event) {
        storeAndShiftEvent(event);
        calculateSMA();
    }

    private void storeAndShiftEvent(CandlestickEvent event) {
//        System.out.println("close" + event.getClose());
        if (smaQueue.size() <= period) {
            Double close = Double.valueOf(event.getClose());
            smaQueue.add(close);
            smaTime = event.getCloseTime();
            if (smaQueue.size() > period) {
                smaQueue.remove();
            }
        }
    }

    private void calculateSMA() {
        String smaValue = "";
        if (smaQueue.size() < period) {
            return;
        }
        Double sum = 0.0;
        for (Double close : smaQueue) {
            sum += close;
        }
//        System.out.println("sum "+ sum+" period "+period);
        smaValue = String.valueOf(sum / period);
        System.out.println(name+" time "+ new Date(smaTime)+" "+smaValue);
    }
}
