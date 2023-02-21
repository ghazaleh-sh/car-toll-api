package ir.co.sadad.cartollapi.rabbit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class Publisher {

    private static final String EXCHANGE_NAME = "ex-transaction-queue";
    private static final String ROUTING_KEY = "transaction";

    private final RabbitTemplate rabbitTemplate;

    public void send(Object msg) {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, msg, m -> {
            m.getMessageProperties().getHeaders().remove("__TypeId__");
            return m;
        });
        log.info("Send msg = " + msg);
    }
}
