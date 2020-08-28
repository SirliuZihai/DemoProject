package disrupter;

import com.lmax.disruptor.EventHandler;

public class PeopleEventHandler implements EventHandler<PeopleEvent> {
    @Override
    public void onEvent(PeopleEvent peopleEvent, long sequence, boolean endOfBatch) throws Exception {
        System.out.println(peopleEvent.getAction());
    }
}
