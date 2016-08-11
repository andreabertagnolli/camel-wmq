package org.apache.camel.component.wmq;

import org.apache.camel.Component;
import org.apache.camel.Endpoint;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.component.jms.JmsConfiguration;
import org.springframework.jms.connection.UserCredentialsConnectionFactoryAdapter;

import java.util.Map;

public class WMQComponent extends JmsComponent {

    private final String queueManager;
    private final String hostname;
    private final String channel;
    private final Integer port;

    public WMQComponent() {
        this(null, null, null, null);
    }

    public WMQComponent(String hostname, Integer port, String queueManager, String channel) {
        super(WMQEndpoint.class);
        this.queueManager = queueManager;
        this.hostname = hostname;
        this.channel = channel;
        this.port = port;
    }

    public static Component newWmqComponent(String hostname, Integer port, String queueManager, String channel) {
        return new WMQComponent(hostname, port, queueManager, channel);
    }

    @Override
    protected JmsConfiguration createConfiguration() {
        WmqConnectionParameters parameters = new WmqConnectionParameters(queueManager, hostname, port, channel);
        JmsConfiguration configuration = new JmsConfiguration();
        configuration.setConnectionFactory(new WmqConnectionFactory(parameters));
        configuration.setDestinationResolver(new WmqDestinationResolver());
        return configuration;
    }

    @Override
    public Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        WMQEndpoint endpoint = new WMQEndpoint(this, uri, remaining);
        JmsConfiguration configuration = endpoint.getConfiguration();
        String username = this.getAndRemoveParameter(parameters, "username", String.class);
        String password = this.getAndRemoveParameter(parameters, "password", String.class);
        configuration.setConnectionFactory(createUserCredentialsConnectionFactoryAdapter(configuration, username, password));
        return endpoint;
    }

    private UserCredentialsConnectionFactoryAdapter createUserCredentialsConnectionFactoryAdapter(JmsConfiguration configuration, String username, String password) {
        UserCredentialsConnectionFactoryAdapter strategyVal = new UserCredentialsConnectionFactoryAdapter();
        if(username != null && password != null) {
            strategyVal.setTargetConnectionFactory(configuration.getConnectionFactory());
            strategyVal.setPassword(password);
            strategyVal.setUsername(username);
        } else if(username != null || password != null) {
            throw new IllegalArgumentException("The JmsComponent\'s username or password is null");
        }
        return strategyVal;
    }
}
