import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import domain.entity.OutputMessage;
import domain.entity.Offence;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.ValidationException;

import java.io.IOException;
import java.io.StringWriter;
import java.util.concurrent.TimeoutException;


public class RabbitMQOutput implements OutputService {

    private String queName;
    private String host;

    public RabbitMQOutput(String queName, String host) {
        this.queName = queName;
        this.host = host;
    }

    @Override
    public void publish(OutputMessage outputMessage ) {
        //OutputMessage outputMessage = new OutputMessage(offence, offenceAmount);
        Marshaller marshaller = null;
        StringWriter strWriter = new StringWriter();

        try {
            marshaller = new Marshaller(strWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            marshaller.marshal(outputMessage);
        } catch (MarshalException e) {
            e.printStackTrace();
        } catch (ValidationException e) {
            e.printStackTrace();
        }

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        Connection connection;
        Channel channel = null;
        try {
            connection = factory.newConnection();
          channel   = connection.createChannel();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }


        try {
            channel.queueDeclare(queName, false, false, false, null);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            channel.basicPublish("", queName, null, strWriter.toString().getBytes("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(" [x] Sent '" + strWriter.toString() + "'");

    }


}
