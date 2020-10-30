package disrupter;

import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;

import java.nio.ByteBuffer;

public class PeopleEventProducerWithTranslator {
    private final RingBuffer<PeopleEvent> ringBuffer;

    public PeopleEventProducerWithTranslator(RingBuffer<PeopleEvent> ringBuffer)
    {
        this.ringBuffer = ringBuffer;
    }

    private static final EventTranslatorOneArg<PeopleEvent, ByteBuffer> TRANSLATOR =
            new EventTranslatorOneArg<PeopleEvent, ByteBuffer>()
            {
                public void translateTo(PeopleEvent event, long sequence, ByteBuffer bb)
                {
                    event.setAction(new String(bb.array()));
                }
            };

    public void onData(ByteBuffer bb)
    {
        ringBuffer.publishEvent(TRANSLATOR, bb);
    }
    public static void main(String[] args) throws Exception
    {
        // The factory for the event
        PeopleEventFactory factory = new PeopleEventFactory();

        // Specify the size of the ring buffer, must be power of 2.
        int bufferSize = 1024;

        // Construct the Disruptor
        Disruptor<PeopleEvent> disruptor = new Disruptor<>(factory, bufferSize, DaemonThreadFactory.INSTANCE);

        // Connect the handler
        disruptor.handleEventsWith(new PeopleEventHandler());

        // Start the Disruptor, starts all threads running
        disruptor.start();

        // Get the ring buffer from the Disruptor to be used for publishing.
        RingBuffer<PeopleEvent> ringBuffer = disruptor.getRingBuffer();

        PeopleEventProducerWithTranslator producer = new PeopleEventProducerWithTranslator(ringBuffer);

        ByteBuffer bb = ByteBuffer.allocate(200);
        for (long l = 0; true; l++)
        {
            bb.clear();
            bb.put("hellow ketty".getBytes());
            producer.onData(bb);
            Thread.sleep(1000);
        }
    }
}
