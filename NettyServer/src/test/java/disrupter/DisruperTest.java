package disrupter;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;

import java.nio.ByteBuffer;

public class DisruperTest {
    public static void handleEvent(PeopleEvent event, long sequence, boolean endOfBatch)
    {
        System.out.println("PeopleEvent:"+event.getAction());
    }

    public static void translate(PeopleEvent event, long sequence, ByteBuffer buffer)
    {
        event.setAction(new String(buffer.array()));
    }
    public static void main(String[] args) throws Exception
    {
        // Specify the size of the ring buffer, must be power of 2.
        int bufferSize = 1024;

        // Construct the Disruptor
        Disruptor<PeopleEvent> disruptor = new Disruptor<>(PeopleEvent::new, bufferSize, DaemonThreadFactory.INSTANCE);

        // Connect the handler
        disruptor.handleEventsWith(DisruperTest::handleEvent);

        // Start the Disruptor, starts all threads running
        disruptor.start();

        // Get the ring buffer from the Disruptor to be used for publishing.
        RingBuffer<PeopleEvent> ringBuffer = disruptor.getRingBuffer();

        ByteBuffer bb = ByteBuffer.allocate(200);
        for (long l = 0; true; l++)
        {
            bb.clear();
            bb.put("hellow ketty".getBytes());
            //bb.put(new Integer(5).);
            ringBuffer.publishEvent(DisruperTest::translate, bb);
            Thread.sleep(1000);
        }
    }
}