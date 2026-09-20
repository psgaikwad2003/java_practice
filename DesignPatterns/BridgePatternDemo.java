package DesignPatterns;

import java.util.ArrayList;
import java.util.List;

/**
 * Demonstrates the Bridge Design Pattern (Structural Pattern).
 *
 * Intent: Decouple an abstraction from its implementation so that the two
 * can vary independently.
 *
 * Problem Solved:
 * Prevents a combinatorial explosion of subclasses (M x N Cartesian product).
 * Without Bridge, if we had M notification types and N delivery channels,
 * we would need M * N separate subclasses (e.g., UrgentEmailNotification,
 * UrgentSmsNotification, DigestEmailNotification, DigestSmsNotification, etc.).
 * With Bridge, Abstraction and Implementor hierarchies remain decoupled and
 * composable at runtime via object composition.
 *
 * Real-World Demonstrations:
 * 1. Enterprise Multi-Channel Alert and Notification System:
 *    - Implementor: {@link MessageSender} (Email, SMS, Slack Webhook, Mobile Push)
 *    - Abstraction: {@link Notification} (Standard Alert, Urgent Alert with auto-escalation,
 *      Batch Digest Report)
 * 2. Universal Remote Control & Smart Home Device Ecosystem:
 *    - Implementor: {@link EntertainmentDevice} (Smart TV, Surround Sound, Home Projector)
 *    - Abstraction: {@link RemoteControl} (Basic Remote, Advanced Remote with channel/input controls)
 */
public class BridgePatternDemo {

    // =========================================================================
    // Example 1: Multi-Channel Enterprise Notification System
    // =========================================================================

    /**
     * The Implementor interface.
     * Defines the low-level communication protocol for message delivery channels.
     */
    public interface MessageSender {
        void sendMessage(String recipient, String subject, String body);
        String getChannelName();
        boolean isAvailable();
    }

    /**
     * Concrete Implementor A: Email communication channel.
     */
    public static class EmailSender implements MessageSender {
        private final String smtpServer;

        public EmailSender(String smtpServer) {
            this.smtpServer = smtpServer;
        }

        @Override
        public void sendMessage(String recipient, String subject, String body) {
            System.out.printf("  [EMAIL via %s] To: <%s> | Subject: [%s]%n    Body: %s%n",
                    smtpServer, recipient, subject, body);
        }

        @Override
        public String getChannelName() {
            return "Email";
        }

        @Override
        public boolean isAvailable() {
            return true;
        }
    }

    /**
     * Concrete Implementor B: SMS gateway communication channel.
     */
    public static class SmsSender implements MessageSender {
        private final String gatewayEndpoint;

        public SmsSender(String gatewayEndpoint) {
            this.gatewayEndpoint = gatewayEndpoint;
        }

        @Override
        public void sendMessage(String recipient, String subject, String body) {
            // SMS typically truncates or has character limits
            String snippet = body.length() > 60 ? body.substring(0, 57) + "..." : body;
            System.out.printf("  [SMS Gateway: %s] To: %s | [%s] %s%n",
                    gatewayEndpoint, recipient, subject, snippet);
        }

        @Override
        public String getChannelName() {
            return "SMS";
        }

        @Override
        public boolean isAvailable() {
            return true;
        }
    }

    /**
     * Concrete Implementor C: Slack Webhook communication channel.
     */
    public static class SlackSender implements MessageSender {
        private final String channelName;

        public SlackSender(String channelName) {
            this.channelName = channelName;
        }

        @Override
        public void sendMessage(String recipient, String subject, String body) {
            System.out.printf("  [SLACK #%s] Mention: @%s | *%s*%n    >>> %s%n",
                    channelName, recipient, subject, body);
        }

        @Override
        public String getChannelName() {
            return "Slack";
        }

        @Override
        public boolean isAvailable() {
            return true;
        }
    }

    /**
     * The Abstraction.
     * Maintains a reference to an implementor (the "Bridge") and delegates
     * low-level operations while defining higher-level business logic.
     */
    public abstract static class Notification {
        protected MessageSender sender;

        public Notification(MessageSender sender) {
            this.sender = sender;
        }

        /**
         * Allows dynamic switching of the implementation channel at runtime.
         */
        public void setSender(MessageSender sender) {
            this.sender = sender;
        }

        public abstract void notifyUser(String recipient, String message);
    }

    /**
     * Refined Abstraction 1: Standard alert notification.
     */
    public static class StandardAlertNotification extends Notification {
        public StandardAlertNotification(MessageSender sender) {
            super(sender);
        }

        @Override
        public void notifyUser(String recipient, String message) {
            sender.sendMessage(recipient, "System Alert", message);
        }
    }

    /**
     * Refined Abstraction 2: Urgent notification with severity escalation and fallback.
     */
    public static class UrgentAlertNotification extends Notification {
        private final MessageSender fallbackSender;

        public UrgentAlertNotification(MessageSender primarySender, MessageSender fallbackSender) {
            super(primarySender);
            this.fallbackSender = fallbackSender;
        }

        @Override
        public void notifyUser(String recipient, String message) {
            String urgentSubject = "CRITICAL [P0] ALERT";
            String urgentBody = "!!! IMMEDIATE ACTION REQUIRED !!! " + message;

            if (sender != null && sender.isAvailable()) {
                sender.sendMessage(recipient, urgentSubject, urgentBody);
            } else if (fallbackSender != null && fallbackSender.isAvailable()) {
                System.out.println("  [Fallback Escalation] Primary channel unavailable. Routing to fallback...");
                fallbackSender.sendMessage(recipient, urgentSubject + " (FALLBACK)", urgentBody);
            } else {
                System.err.println("  [Error] All notification channels failed for urgent alert!");
            }
        }
    }

    /**
     * Refined Abstraction 3: Aggregated batch digest notification.
     */
    public static class DigestNotification extends Notification {
        private final List<String> events = new ArrayList<>();

        public DigestNotification(MessageSender sender) {
            super(sender);
        }

        public void addEvent(String event) {
            events.add(event);
        }

        @Override
        public void notifyUser(String recipient, String message) {
            StringBuilder aggregated = new StringBuilder();
            aggregated.append(message).append("\n    Items (").append(events.size()).append("):\n");
            for (int i = 0; i < events.size(); i++) {
                aggregated.append("      - ").append(events.get(i));
                if (i < events.size() - 1) {
                    aggregated.append("\n");
                }
            }
            sender.sendMessage(recipient, "Daily Activity Digest", aggregated.toString());
        }
    }

    // =========================================================================
    // Example 2: Universal Remote Control & Smart Entertainment Devices
    // =========================================================================

    /**
     * The Device Implementor interface.
     */
    public interface EntertainmentDevice {
        boolean isPoweredOn();
        void powerOn();
        void powerOff();
        int getVolume();
        void setVolume(int percent);
        int getChannel();
        void setChannel(int channel);
        String getDeviceName();
    }

    /**
     * Concrete Implementor: Smart OLED TV.
     */
    public static class SmartTvDevice implements EntertainmentDevice {
        private boolean on = false;
        private int volume = 20;
        private int channel = 1;

        @Override
        public boolean isPoweredOn() { return on; }

        @Override
        public void powerOn() {
            on = true;
            System.out.println("  [SmartTV] Panel powered ON (4K HDR mode).");
        }

        @Override
        public void powerOff() {
            on = false;
            System.out.println("  [SmartTV] Panel powered OFF (Standby).");
        }

        @Override
        public int getVolume() { return volume; }

        @Override
        public void setVolume(int percent) {
            this.volume = Math.max(0, Math.min(100, percent));
            System.out.println("  [SmartTV] Volume set to: " + this.volume + "%");
        }

        @Override
        public int getChannel() { return channel; }

        @Override
        public void setChannel(int channel) {
            this.channel = channel;
            System.out.println("  [SmartTV] Tuned to Channel: " + this.channel);
        }

        @Override
        public String getDeviceName() { return "LG OLED 65-inch Smart TV"; }
    }

    /**
     * Concrete Implementor: Surround Sound Audio Receiver.
     */
    public static class SurroundSoundDevice implements EntertainmentDevice {
        private boolean on = false;
        private int volume = 35;
        private int channel = 1;

        @Override
        public boolean isPoweredOn() { return on; }

        @Override
        public void powerOn() {
            on = true;
            System.out.println("  [SurroundSound] Dolby Atmos Receiver powered ON.");
        }

        @Override
        public void powerOff() {
            on = false;
            System.out.println("  [SurroundSound] Receiver entering deep sleep mode.");
        }

        @Override
        public int getVolume() { return volume; }

        @Override
        public void setVolume(int percent) {
            this.volume = Math.max(0, Math.min(100, percent));
            System.out.println("  [SurroundSound] Master gain adjusted to: " + this.volume + " dB");
        }

        @Override
        public int getChannel() { return channel; }

        @Override
        public void setChannel(int channel) {
            this.channel = channel;
            System.out.println("  [SurroundSound] Audio input source switched to Preset #" + channel);
        }

        @Override
        public String getDeviceName() { return "Denon AVR Dolby Atmos 7.2"; }
    }

    /**
     * Abstraction for Remote Controls.
     * Bridged with an {@link EntertainmentDevice}.
     */
    public static class BasicRemote {
        protected EntertainmentDevice device;

        public BasicRemote(EntertainmentDevice device) {
            this.device = device;
        }

        public void togglePower() {
            System.out.println("-> BasicRemote: Pressing [Power Button] for " + device.getDeviceName());
            if (device.isPoweredOn()) {
                device.powerOff();
            } else {
                device.powerOn();
            }
        }

        public void volumeUp() {
            System.out.println("-> BasicRemote: [Volume +]");
            device.setVolume(device.getVolume() + 5);
        }

        public void volumeDown() {
            System.out.println("-> BasicRemote: [Volume -]");
            device.setVolume(device.getVolume() - 5);
        }
    }

    /**
     * Refined Abstraction: Advanced Touch Remote with mute and direct channel controls.
     */
    public static class AdvancedRemote extends BasicRemote {
        private int previousVolume;

        public AdvancedRemote(EntertainmentDevice device) {
            super(device);
            this.previousVolume = device.getVolume();
        }

        public void mute() {
            System.out.println("-> AdvancedRemote: [MUTE Toggle]");
            if (device.getVolume() > 0) {
                this.previousVolume = device.getVolume();
                device.setVolume(0);
            } else {
                device.setVolume(this.previousVolume);
            }
        }

        public void nextChannel() {
            System.out.println("-> AdvancedRemote: [Next Channel]");
            device.setChannel(device.getChannel() + 1);
        }

        public void previousChannel() {
            System.out.println("-> AdvancedRemote: [Prev Channel]");
            if (device.getChannel() > 1) {
                device.setChannel(device.getChannel() - 1);
            }
        }
    }

    // =========================================================================
    // Demonstration Main Method
    // =========================================================================

    public static void main(String[] args) {
        System.out.println("======================================================================");
        System.out.println("        BRIDGE DESIGN PATTERN DEMONSTRATION (STRUCTURAL)");
        System.out.println("======================================================================");

        // ---------------------------------------------------------------------
        // Demo 1: Multi-Channel Notifications via Bridge
        // ---------------------------------------------------------------------
        System.out.println("\n--- [Demo 1] Multi-Channel Notifications (Abstraction + Implementor) ---");

        MessageSender emailChannel = new EmailSender("smtp.production.infra.internal:587");
        MessageSender smsChannel = new SmsSender("https://sms-gw.telco.net/v2/dispatch");
        MessageSender slackChannel = new SlackSender("alerts-sre-ops");

        // 1. Standard alert over Email
        Notification stdNotification = new StandardAlertNotification(emailChannel);
        stdNotification.notifyUser("ops-lead@company.com", "Nightly automated backup completed in 42s.");

        // 2. Urgent critical alert over SMS with Email fallback
        Notification urgentNotification = new UrgentAlertNotification(smsChannel, emailChannel);
        urgentNotification.notifyUser("+1-555-0199", "Disk partition /var/log reached 98% utilization!");

        // 3. Dynamic channel swapping at runtime without changing the notification instance!
        System.out.println("\n  [Runtime Bridge Switching] Switching notification channel to Slack...");
        stdNotification.setSender(slackChannel);
        stdNotification.notifyUser("alex_sre", "CPU load average normalized to 1.15.");

        // 4. Digest notification over Slack
        DigestNotification digest = new DigestNotification(slackChannel);
        digest.addEvent("Deploy build #482 to staging passed.");
        digest.addEvent("Database vacuuming freed 14.2 GB.");
        digest.addEvent("SSL certificate renewed for api.domain.com.");
        digest.notifyUser("team-leads", "Automated Engineering Daily Summary");

        // ---------------------------------------------------------------------
        // Demo 2: Universal Remote Controls & Smart Devices
        // ---------------------------------------------------------------------
        System.out.println("\n--- [Demo 2] Universal Remotes & Entertainment Devices ---");

        EntertainmentDevice tv = new SmartTvDevice();
        EntertainmentDevice soundSystem = new SurroundSoundDevice();

        // Controlling the TV with a Basic Remote
        System.out.println("\n[Action] Controlling TV with Basic Remote:");
        BasicRemote basicRemote = new BasicRemote(tv);
        basicRemote.togglePower();
        basicRemote.volumeUp();

        // Controlling the Sound System with an Advanced Remote
        System.out.println("\n[Action] Controlling Sound System with Advanced Remote:");
        AdvancedRemote advancedRemote = new AdvancedRemote(soundSystem);
        advancedRemote.togglePower();
        advancedRemote.volumeUp();
        advancedRemote.volumeUp();
        advancedRemote.mute();
        advancedRemote.mute();
        advancedRemote.nextChannel();

        System.out.println("\n======================================================================");
        System.out.println("  Bridge Pattern Advantages:");
        System.out.println("  - Decouples interface from implementation; independent evolution.");
        System.out.println("  - Eliminates M x N class explosion (combines M abstractions with N implementors).");
        System.out.println("  - Conforms to Open/Closed Principle and Single Responsibility Principle.");
        System.out.println("======================================================================");
    }
}
