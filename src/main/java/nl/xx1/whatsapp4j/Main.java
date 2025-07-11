package nl.xx1.whatsapp4j;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Client client = new Client();

        client.on(Event.QR_READY, System.out::println);

        client.start();
    }
}
