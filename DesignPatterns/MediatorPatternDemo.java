package DesignPatterns;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * Demonstrates the Mediator Design Pattern (Behavioral Pattern).
 * 
 * Intent: Define an object that encapsulates how a set of objects interact.
 * Mediator promotes loose coupling by keeping objects from referring to each
 * other explicitly, allowing their interaction to be varied independently.
 * 
 * Key Participants:
 * 1. Mediator: Declares an interface for communicating with Colleague objects.
 * 2. ConcreteMediator: Implements cooperative behavior by coordinating Colleague objects;
 *    knows and maintains its colleagues.
 * 3. Colleague classes: Each colleague communicates with its mediator whenever it
 *    would have otherwise communicated with another colleague directly.
 * 
 * Included Examples:
 * 1. Air Traffic Control (ATC) Tower: Coordinates runway clearances, takeoffs,
 *    landings, and emergency priority handling among diverse aircraft.
 * 2. Enterprise Chatroom & Compliance Mediator: Mediates direct/channel messages,
 *    automates policy compliance/profanity filtering, and user routing.
 */
public class MediatorPatternDemo {

    // =========================================================================
    // Example 1: Air Traffic Control (ATC) System
    // =========================================================================

    /**
     * Mediator Interface for Air Traffic Coordination.
     */
    public interface AirTrafficControlTower {
        void registerAircraft(Aircraft aircraft);
        boolean requestTakeoff(Aircraft aircraft);
        boolean requestLanding(Aircraft aircraft);
        void reportRunwayVacated(Aircraft aircraft);
        void broadcastHazardAlert(String alert, Aircraft sender);
    }

    /**
     * Abstract Colleague representing any aircraft communicating with the tower.
     */
    public abstract static class Aircraft {
        protected final String callsign;
        protected AirTrafficControlTower tower;
        protected boolean isAirborne;
        protected boolean hasEmergency;

        public Aircraft(String callsign, boolean isAirborne) {
            this.callsign = callsign;
            this.isAirborne = isAirborne;
            this.hasEmergency = false;
        }

        public void setTower(AirTrafficControlTower tower) {
            this.tower = tower;
        }

        public String getCallsign() {
            return callsign;
        }

        public boolean isAirborne() {
            return isAirborne;
        }

        public boolean hasEmergency() {
            return hasEmergency;
        }

        public void declareEmergency(String details) {
            this.hasEmergency = true;
            System.out.printf("  [MAYDAY] %s declares EMERGENCY: %s%n", callsign, details);
            if (tower != null) {
                tower.broadcastHazardAlert("EMERGENCY DECLARED: " + details, this);
            }
        }

        public void requestTakeoff() {
            if (tower == null) {
                System.out.printf("  [%s] No tower assigned for takeoff.%n", callsign);
                return;
            }
            if (isAirborne) {
                System.out.printf("  [%s] Already airborne.%n", callsign);
                return;
            }
            System.out.printf("  [%s] Requesting takeoff clearance from tower...%n", callsign);
            boolean cleared = tower.requestTakeoff(this);
            if (cleared) {
                this.isAirborne = true;
                System.out.printf("  [%s] Wheels up! Now airborne.%n", callsign);
                tower.reportRunwayVacated(this);
            }
        }

        public void requestLanding() {
            if (tower == null) {
                System.out.printf("  [%s] No tower assigned for landing.%n", callsign);
                return;
            }
            if (!isAirborne) {
                System.out.printf("  [%s] Already on the ground.%n", callsign);
                return;
            }
            System.out.printf("  [%s] Requesting landing clearance from tower...%n", callsign);
            boolean cleared = tower.requestLanding(this);
            if (cleared) {
                this.isAirborne = false;
                this.hasEmergency = false;
                System.out.printf("  [%s] Touchdown successful! On the runway.%n", callsign);
                tower.reportRunwayVacated(this);
            }
        }

        public abstract void receiveAlert(String message);
    }

    /**
     * Concrete Colleague: Commercial Passenger Jet.
     */
    public static class CommercialAirliner extends Aircraft {
        private final int passengerCount;

        public CommercialAirliner(String callsign, boolean isAirborne, int passengerCount) {
            super(callsign, isAirborne);
            this.passengerCount = passengerCount;
        }

        @Override
        public void receiveAlert(String message) {
            System.out.printf("  [%s (Passengers: %d)] Alert received: \"%s\"%n", callsign, passengerCount, message);
        }
    }

    /**
     * Concrete Colleague: Heavy Cargo Plane.
     */
    public static class CargoPlane extends Aircraft {
        private final double payloadTons;

        public CargoPlane(String callsign, boolean isAirborne, double payloadTons) {
            super(callsign, isAirborne);
            this.payloadTons = payloadTons;
        }

        @Override
        public void receiveAlert(String message) {
            System.out.printf("  [%s (Cargo: %.1f tons)] Alert received: \"%s\"%n", callsign, payloadTons, message);
        }
    }

    /**
     * Concrete Colleague: Medical Rescue Helicopter.
     */
    public static class RescueHelicopter extends Aircraft {
        public RescueHelicopter(String callsign, boolean isAirborne) {
            super(callsign, isAirborne);
        }

        @Override
        public void receiveAlert(String message) {
            System.out.printf("  [%s (Rescue Unit)] Alert acknowledged: \"%s\"%n", callsign, message);
        }
    }

    /**
     * Concrete Mediator: Airport Tower coordinating flight queues and runway access.
     */
    public static class RegionalAirportTower implements AirTrafficControlTower {
        private final String airportCode;
        private final List<Aircraft> registeredAircraft = new ArrayList<>();
        private final Queue<Aircraft> landingQueue = new LinkedList<>();
        private final Queue<Aircraft> takeoffQueue = new LinkedList<>();
        private Aircraft currentRunwayOccupant = null;

        public RegionalAirportTower(String airportCode) {
            this.airportCode = airportCode;
        }

        @Override
        public void registerAircraft(Aircraft aircraft) {
            if (!registeredAircraft.contains(aircraft)) {
                registeredAircraft.add(aircraft);
                aircraft.setTower(this);
                System.out.printf("[Tower %s] Registered aircraft %s into airspace radar.%n",
                        airportCode, aircraft.getCallsign());
            }
        }

        @Override
        public synchronized boolean requestTakeoff(Aircraft aircraft) {
            if (currentRunwayOccupant == null && landingQueue.isEmpty()) {
                currentRunwayOccupant = aircraft;
                System.out.printf("[Tower %s] CLEARED FOR TAKEOFF: Runway 27L granted to %s.%n",
                        airportCode, aircraft.getCallsign());
                return true;
            } else {
                takeoffQueue.offer(aircraft);
                System.out.printf("[Tower %s] HOLD POSITION: Runway occupied or landing priority active. %s placed in takeoff queue (#%d).%n",
                        airportCode, aircraft.getCallsign(), takeoffQueue.size());
                return false;
            }
        }

        @Override
        public synchronized boolean requestLanding(Aircraft aircraft) {
            // Emergency flights preempt all other traffic immediately
            if (aircraft.hasEmergency()) {
                System.out.printf("[Tower %s] *** EMERGENCY OVERRIDE *** Clear runway for distress flight %s!%n",
                        airportCode, aircraft.getCallsign());
                currentRunwayOccupant = aircraft;
                return true;
            }

            if (currentRunwayOccupant == null) {
                currentRunwayOccupant = aircraft;
                System.out.printf("[Tower %s] CLEARED TO LAND: Runway 27L granted to %s.%n",
                        airportCode, aircraft.getCallsign());
                return true;
            } else {
                landingQueue.offer(aircraft);
                System.out.printf("[Tower %s] ENTER HOLDING PATTERN: %s queued for landing (#%d).%n",
                        airportCode, aircraft.getCallsign(), landingQueue.size());
                return false;
            }
        }

        @Override
        public synchronized void reportRunwayVacated(Aircraft aircraft) {
            if (currentRunwayOccupant == aircraft) {
                System.out.printf("[Tower %s] Runway 27L vacated by %s.%n", airportCode, aircraft.getCallsign());
                currentRunwayOccupant = null;
                processPendingQueues();
            }
        }

        private void processPendingQueues() {
            // Landing takes priority over departure for fuel safety
            if (!landingQueue.isEmpty()) {
                Aircraft nextLanding = landingQueue.poll();
                System.out.printf("[Tower %s] Calling next from landing queue: %s.%n", airportCode, nextLanding.getCallsign());
                nextLanding.requestLanding();
            } else if (!takeoffQueue.isEmpty()) {
                Aircraft nextDeparture = takeoffQueue.poll();
                System.out.printf("[Tower %s] Calling next from takeoff queue: %s.%n", airportCode, nextDeparture.getCallsign());
                nextDeparture.requestTakeoff();
            } else {
                System.out.printf("[Tower %s] Runway 27L is currently idle and ready.%n", airportCode);
            }
        }

        @Override
        public void broadcastHazardAlert(String alert, Aircraft sender) {
            System.out.printf("[Tower %s] BROADCASTING to all active aircraft on frequency: \"%s\" (from %s)%n",
                    airportCode, alert, sender.getCallsign());
            for (Aircraft a : registeredAircraft) {
                if (a != sender) {
                    a.receiveAlert(alert);
                }
            }
        }
    }

    // =========================================================================
    // Example 2: Enterprise Workspace & Moderation Chat Mediator
    // =========================================================================

    /**
     * Mediator Interface for Chat & Collaboration.
     */
    public interface ChatMediator {
        void registerMember(ChatMember member);
        void sendChannelMessage(String message, ChatMember sender);
        void sendDirectMessage(String message, ChatMember sender, String recipientName);
    }

    /**
     * Abstract Colleague for workspace collaboration.
     */
    public abstract static class ChatMember {
        protected final String username;
        protected ChatMediator mediator;

        public ChatMember(String username) {
            this.username = username;
        }

        public void setMediator(ChatMediator mediator) {
            this.mediator = mediator;
        }

        public String getUsername() {
            return username;
        }

        public void postToChannel(String message) {
            System.out.printf("[%s] -> #general: %s%n", username, message);
            if (mediator != null) {
                mediator.sendChannelMessage(message, this);
            }
        }

        public void postDirect(String recipient, String message) {
            System.out.printf("[%s] -> @%s (DM): %s%n", username, recipient, message);
            if (mediator != null) {
                mediator.sendDirectMessage(message, this, recipient);
            }
        }

        public abstract void receiveMessage(String from, String channel, String content);
    }

    /**
     * Concrete Colleague: Software Engineer Member.
     */
    public static class DeveloperMember extends ChatMember {
        public DeveloperMember(String username) {
            super(username);
        }

        @Override
        public void receiveMessage(String from, String channel, String content) {
            System.out.printf("  (@%s inbox [%s]) from @%s: %s%n", username, channel, from, content);
        }
    }

    /**
     * Concrete Colleague: System Automated Bot.
     */
    public static class BotMember extends ChatMember {
        public BotMember(String username) {
            super(username);
        }

        @Override
        public void receiveMessage(String from, String channel, String content) {
            // Bots process notifications silently unless explicitly triggered
            if (content.contains("build") || content.contains("deploy")) {
                System.out.printf("  [CI-BOT @%s] Trigger acknowledged for '%s' from @%s%n",
                        username, content, from);
            }
        }
    }

    /**
     * Concrete Mediator: Enterprise Slack-like workspace with automated compliance filter.
     */
    public static class WorkspaceMediator implements ChatMediator {
        private final String workspaceName;
        private final Map<String, ChatMember> members = new HashMap<>();
        private final List<String> prohibitedWords = List.of("confidential_token", "drop table", "password123");

        public WorkspaceMediator(String workspaceName) {
            this.workspaceName = workspaceName;
        }

        @Override
        public void registerMember(ChatMember member) {
            members.put(member.getUsername().toLowerCase(), member);
            member.setMediator(this);
            System.out.printf("[Workspace %s] Added member @%s to the directory.%n",
                    workspaceName, member.getUsername());
        }

        @Override
        public void sendChannelMessage(String message, ChatMember sender) {
            if (!passesComplianceCheck(message, sender)) {
                return;
            }

            for (ChatMember member : members.values()) {
                if (!member.getUsername().equalsIgnoreCase(sender.getUsername())) {
                    member.receiveMessage(sender.getUsername(), "#general", message);
                }
            }
        }

        @Override
        public void sendDirectMessage(String message, ChatMember sender, String recipientName) {
            if (!passesComplianceCheck(message, sender)) {
                return;
            }

            ChatMember recipient = members.get(recipientName.toLowerCase());
            if (recipient != null) {
                recipient.receiveMessage(sender.getUsername(), "Direct Message", message);
            } else {
                System.out.printf("[System] Delivery Failed: User @%s not found in workspace %s.%n",
                        recipientName, workspaceName);
            }
        }

        private boolean passesComplianceCheck(String message, ChatMember sender) {
            for (String prohibited : prohibitedWords) {
                if (message.toLowerCase().contains(prohibited)) {
                    System.out.printf("[COMPLIANCE VIOLATION] Blocked message from @%s! Contains prohibited term '%s'.%n",
                            sender.getUsername(), prohibited);
                    return false;
                }
            }
            return true;
        }
    }

    // =========================================================================
    // Demonstration & Test Runner
    // =========================================================================

    public static void main(String[] args) {
        System.out.println("=================================================================");
        System.out.println("      MEDIATOR PATTERN DEMO - DECOUPLING OBJECT INTERACTIONS     ");
        System.out.println("=================================================================");

        // --- Demo 1: Air Traffic Control (ATC) System ---
        System.out.println("\n--- SCENARIO 1: AIR TRAFFIC CONTROL TOWER MEDIATION ---");
        RegionalAirportTower tower = new RegionalAirportTower("SFO");

        Aircraft flight101 = new CommercialAirliner("UA-101", false, 180);
        Aircraft flight202 = new CommercialAirliner("DL-202", false, 210);
        Aircraft cargo999 = new CargoPlane("FDX-999", true, 45.5);
        Aircraft rescue1 = new RescueHelicopter("MED-EVAC-1", true);

        tower.registerAircraft(flight101);
        tower.registerAircraft(flight202);
        tower.registerAircraft(cargo999);
        tower.registerAircraft(rescue1);

        System.out.println("\n[Action] Flight UA-101 requests departure:");
        flight101.requestTakeoff();

        System.out.println("\n[Action] Cargo FDX-999 requests landing while runway was occupied / queue processing:");
        cargo999.requestLanding();

        System.out.println("\n[Action] Flight DL-202 requests departure simultaneously:");
        flight202.requestTakeoff();

        System.out.println("\n[Action] Medical Helicopter MED-EVAC-1 declares critical emergency mid-flight:");
        rescue1.declareEmergency("Critical organ transport on board with low reserve fuel");
        rescue1.requestLanding();

        // --- Demo 2: Enterprise Workspace & Moderation Chatroom ---
        System.out.println("\n--- SCENARIO 2: ENTERPRISE CHATROOM & COMPLIANCE MEDIATOR ---");
        WorkspaceMediator slack = new WorkspaceMediator("TechCorp-HQ");

        ChatMember alice = new DeveloperMember("Alice");
        ChatMember bob = new DeveloperMember("Bob");
        ChatMember charlie = new DeveloperMember("Charlie");
        ChatMember ciBot = new BotMember("BuildBot");

        slack.registerMember(alice);
        slack.registerMember(bob);
        slack.registerMember(charlie);
        slack.registerMember(ciBot);

        System.out.println("\n[Action] Alice broadcasts clean message to #general:");
        alice.postToChannel("Team, the new microservice build is ready for review!");

        System.out.println("\n[Action] Bob sends direct message to Charlie:");
        bob.postDirect("Charlie", "Can you review PR #42 before standup?");

        System.out.println("\n[Action] Charlie accidentally posts sensitive token into channel:");
        charlie.postToChannel("Here is the key: confidential_token for testing");

        System.out.println("\n[Action] Bob sends direct message to non-existent user:");
        bob.postDirect("GhostUser", "Are you available?");

        System.out.println("\n=================================================================");
        System.out.println("                 MEDIATOR PATTERN COMPLETED                     ");
        System.out.println("=================================================================");
    }
}
