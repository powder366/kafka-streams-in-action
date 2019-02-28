package bbejeck.chapter_2.producer;

import bbejeck.chapter_2.partitioner.PurchaseKeyPartitioner;
import bbejeck.model.PurchaseKey;
import org.apache.kafka.clients.producer.*;

import java.util.Date;
import java.util.Properties;
import java.util.concurrent.Future;

/**
 * Example of a simple producer, not meant to run as a stand alone example.
 *
 * If desired to run this example change the ProducerRecord below to
 * use a real topic name and comment out line #33 below.
 */
public class SimpleProducer {



    public static void main(String[] args) {

        Properties properties = new Properties();
        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("key.serializer", "bbejeck.chapter_2.producer.PurchaseKeySerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("acks", "1");
        properties.put("retries", "3");
        properties.put("compression.type", "snappy");
        //This line in for demonstration purposes
        properties.put("partitioner.class", PurchaseKeyPartitioner.class.getName());

        PurchaseKey key = new PurchaseKey("12334568", new Date());

        try(Producer<PurchaseKey, String> producer = new KafkaProducer<>(properties)) {
            ProducerRecord<PurchaseKey, String> record = new ProducerRecord<>("test-topic", key, "hej");

            Callback callback = (metadata, exception) -> {
                if (exception != null) {
                    exception.printStackTrace();
                }
            };

            Future<RecordMetadata> sendFuture = producer.send(record, callback);
        }

    }



}
