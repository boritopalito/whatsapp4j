package nl.xx1.whatsapp4j;

import nl.xx1.whatsapp4j.auth.LocalAuth;
import nl.xx1.whatsapp4j.model.Message;

public class Main {
    public static void main(String[] args) {
        Client client = new Client(
                ClientLaunchOptions.builder().authStrategy(new LocalAuth()).build());

        client.on(Event.QR_READY, System.out::println);

        client.on(Event.READY, (obj) -> {
            System.out.println("Client is ready!");
            client.getChats().forEach(System.out::println);
        });

        client.on(Event.MESSAGE_RECEIVED, (Message message) -> {
            System.out.println(message);
        });

        client.start();
    }
}
