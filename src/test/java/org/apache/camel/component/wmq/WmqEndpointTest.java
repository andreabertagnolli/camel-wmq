package org.apache.camel.component.wmq;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class WmqEndpointTest {

    private static final String ANY_URI = "any";

    @Mock WmqComponent component;

    @Test
    public void when_define_endpoint_without_destinationType_use_queue_default_type() throws Exception {
        WmqEndpoint endpoint = new WmqEndpoint(component, ANY_URI, "A_QUEUE");

        assertEquals("A_QUEUE", endpoint.getDestinationName());
        assertEquals("queue", endpoint.getDestinationType());
    }

    @Test
    public void when_define_endpoint_with_queue_destination_type_use_it() throws Exception {
        WmqEndpoint endpoint = new WmqEndpoint(component, ANY_URI, "queue:A_QUEUE");

        assertEquals("A_QUEUE", endpoint.getDestinationName());
        assertEquals("queue", endpoint.getDestinationType());
    }

    @Test
    public void when_define_endpoint_with_topic_destination_type_use_it() throws Exception {
        WmqEndpoint endpoint = new WmqEndpoint(component, ANY_URI, "topic:A_TOPIC");

        assertEquals("A_TOPIC", endpoint.getDestinationName());
        assertEquals("topic", endpoint.getDestinationType());
    }

}