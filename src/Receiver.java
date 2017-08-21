import com.rabbitmq.client.*;
import domain.entity.Offence;
import org.exolab.castor.xml.Unmarshaller;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

/**
 * Created by jorden on 13-8-2017.
 */
public class Receiver {
    private final static String QUEUE_NAME = "nogistest";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                String message = new String(body, "UTF-8");


                System.out.println(" [x] Received '" + message + "'");

                Reader reader = new StringReader(message);
                Offence offence = null;
                try {
                    offence = (Offence) Unmarshaller.unmarshal(Offence.class, reader);
                    System.out.println(offence.toString());
                } catch (Exception e) {
                    throw new IOException("Error during conversion to DTO", e);
                }
            }
        };
        channel.basicConsume(QUEUE_NAME, true, consumer);
    }
}
