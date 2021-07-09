package za.co.vodacom.vodacommft.service.impl;

import org.springframework.stereotype.Service;
import za.co.vodacom.vodacommft.service.IConsumerCustomPropertiesService;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author ncubeh on 2020/08/21
 * @package za.co.vodacom.vodacomMFT.service.impl
 */
@Service
public class ConsumerCustomPropertiesService implements IConsumerCustomPropertiesService {
    private Properties consumer_property;
    private InputStream consumer_inputStream;
    private final static String THREAD_TUNING = "THREAD_TUNING";

    @Override
    public String getConsumerThreadTuningProperties(String consumer_custom_properties) throws IOException {
        consumer_inputStream = new FileInputStream(consumer_custom_properties);
        consumer_property = new Properties();
        consumer_property.load(consumer_inputStream);

        String cons_thread_tuning_value = consumer_property.getProperty(THREAD_TUNING);

        return cons_thread_tuning_value;
    }

}
