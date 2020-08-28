package disrupter;

import com.lmax.disruptor.EventFactory;

public class PeopleEventFactory implements EventFactory<PeopleEvent> {
    @Override
    public PeopleEvent newInstance() {
        return  new PeopleEvent();
    }
}
