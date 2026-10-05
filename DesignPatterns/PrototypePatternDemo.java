package DesignPatterns;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrototypePatternDemo {

    public interface Prototype<T> {
        T deepCopy();
    }

    public static class StorageVolume implements Prototype<StorageVolume> {
        private String mountPoint;
        private int capacityGb;
        private String volumeType;

        public StorageVolume(String mountPoint, int capacityGb, String volumeType) {
            this.mountPoint = mountPoint;
            this.capacityGb = capacityGb;
            this.volumeType = volumeType;
        }

        public void setCapacityGb(int capacityGb) { this.capacityGb = capacityGb; }
        public String getMountPoint() { return mountPoint; }
        public int getCapacityGb() { return capacityGb; }

        @Override
        public StorageVolume deepCopy() {
            return new StorageVolume(this.mountPoint, this.capacityGb, this.volumeType);
        }

        @Override
        public String toString() {
            return String.format("[%s: %dGB (%s)]", mountPoint, capacityGb, volumeType);
        }
    }

    public static class SecurityRule implements Prototype<SecurityRule> {
        private String protocol;
        private int port;
        private String cidrBlock;

        public SecurityRule(String protocol, int port, String cidrBlock) {
            this.protocol = protocol;
            this.port = port;
            this.cidrBlock = cidrBlock;
        }

        @Override
        public SecurityRule deepCopy() {
            return new SecurityRule(this.protocol, this.port, this.cidrBlock);
        }

        @Override
        public String toString() {
            return String.format("%s/%d from %s", protocol, port, cidrBlock);
        }
    }

    public static class VirtualMachineConfiguration implements Prototype<VirtualMachineConfiguration> {
        private String instanceName;
        private int cpuCores;
        private int ramGb;
        private String osImage;
        private List<StorageVolume> attachedDisks = new ArrayList<>();
        private List<SecurityRule> firewallRules = new ArrayList<>();

        public VirtualMachineConfiguration(String instanceName, int cpuCores, int ramGb, String osImage) {
            this.instanceName = instanceName;
            this.cpuCores = cpuCores;
            this.ramGb = ramGb;
            this.osImage = osImage;
        }

        public void addDisk(StorageVolume disk) { attachedDisks.add(disk); }
        public void addSecurityRule(SecurityRule rule) { firewallRules.add(rule); }
        public void setInstanceName(String instanceName) { this.instanceName = instanceName; }
        public void setRamGb(int ramGb) { this.ramGb = ramGb; }
        public List<StorageVolume> getAttachedDisks() { return attachedDisks; }
        public List<SecurityRule> getFirewallRules() { return firewallRules; }

        @Override
        public VirtualMachineConfiguration deepCopy() {
            VirtualMachineConfiguration clone = new VirtualMachineConfiguration(
                    this.instanceName, this.cpuCores, this.ramGb, this.osImage
            );

            for (StorageVolume disk : this.attachedDisks) {
                clone.addDisk(disk.deepCopy());
            }

            for (SecurityRule rule : this.firewallRules) {
                clone.addSecurityRule(rule.deepCopy());
            }

            return clone;
        }

        public void printSummary() {
            System.out.printf("  [VM: %s] OS: %s | %d vCPUs | %d GB RAM%n", instanceName, osImage, cpuCores, ramGb);
            System.out.printf("    - Attached Disks: %s%n", attachedDisks);
            System.out.printf("    - Security Rules: %s%n", firewallRules);
        }
    }

    public static class CloudTemplateRegistry {
        private final Map<String, VirtualMachineConfiguration> templates = new HashMap<>();

        public void registerTemplate(String key, VirtualMachineConfiguration config) {
            templates.put(key, config);
        }

        public VirtualMachineConfiguration createInstanceFromTemplate(String key, String newInstanceName) {
            VirtualMachineConfiguration template = templates.get(key);
            if (template == null) {
                throw new IllegalArgumentException("Unknown VM template: " + key);
            }
            VirtualMachineConfiguration clone = template.deepCopy();
            clone.setInstanceName(newInstanceName);
            return clone;
        }
    }

    public abstract static class GameUnit implements Prototype<GameUnit> {
        protected String unitType;
        protected int maxHealth;
        protected int currentHealth;
        protected int posX;
        protected int posY;

        public GameUnit(String unitType, int maxHealth) {
            this.unitType = unitType;
            this.maxHealth = maxHealth;
            this.currentHealth = maxHealth;
            this.posX = 0;
            this.posY = 0;
        }

        public void setPosition(int x, int y) {
            this.posX = x;
            this.posY = y;
        }

        public void takeDamage(int amount) {
            this.currentHealth = Math.max(0, this.currentHealth - amount);
        }

        public abstract void printDetails();
    }

    public static class WarriorUnit extends GameUnit {
        private String mainWeapon;
        private List<String> inventory = new ArrayList<>();

        public WarriorUnit(String unitType, int maxHealth, String mainWeapon) {
            super(unitType, maxHealth);
            this.mainWeapon = mainWeapon;
        }

        public void addInventoryItem(String item) { inventory.add(item); }
        public void setMainWeapon(String weapon) { this.mainWeapon = weapon; }
        public List<String> getInventory() { return inventory; }

        @Override
        public WarriorUnit deepCopy() {
            WarriorUnit clone = new WarriorUnit(this.unitType, this.maxHealth, this.mainWeapon);
            clone.posX = this.posX;
            clone.posY = this.posY;
            clone.currentHealth = this.currentHealth;
            clone.inventory = new ArrayList<>(this.inventory);
            return clone;
        }

        @Override
        public void printDetails() {
            System.out.printf("  [Warrior: %s] HP: %d/%d at (%d, %d) | Weapon: %s | Bag: %s%n",
                    unitType, currentHealth, maxHealth, posX, posY, mainWeapon, inventory);
        }
    }

    public static class SorcererUnit extends GameUnit {
        private int manaPoints;
        private List<String> learnedSpells = new ArrayList<>();

        public SorcererUnit(String unitType, int maxHealth, int manaPoints) {
            super(unitType, maxHealth);
            this.manaPoints = manaPoints;
        }

        public void learnSpell(String spell) { learnedSpells.add(spell); }
        public List<String> getLearnedSpells() { return learnedSpells; }

        @Override
        public SorcererUnit deepCopy() {
            SorcererUnit clone = new SorcererUnit(this.unitType, this.maxHealth, this.manaPoints);
            clone.posX = this.posX;
            clone.posY = this.posY;
            clone.currentHealth = this.currentHealth;
            clone.learnedSpells = new ArrayList<>(this.learnedSpells);
            return clone;
        }

        @Override
        public void printDetails() {
            System.out.printf("  [Sorcerer: %s] HP: %d/%d | MP: %d at (%d, %d) | Spellbook: %s%n",
                    unitType, currentHealth, maxHealth, manaPoints, posX, posY, learnedSpells);
        }
    }

    public static class UnitSpawnerRegistry {
        private final Map<String, GameUnit> unitPrototypes = new HashMap<>();

        public void registerPrototype(String key, GameUnit prototype) {
            unitPrototypes.put(key, prototype);
        }

        public GameUnit spawn(String key, int x, int y) {
            GameUnit prototype = unitPrototypes.get(key);
            if (prototype == null) {
                throw new IllegalArgumentException("Unknown unit prototype: " + key);
            }
            GameUnit clone = prototype.deepCopy();
            clone.setPosition(x, y);
            return clone;
        }
    }

    public static void main(String[] args) {
        System.out.println("=================================================================");
        System.out.println("       PROTOTYPE PATTERN DEMO - ROBUST DEEP CLONING & REGISTRY   ");
        System.out.println("=================================================================");

        System.out.println("\n--- SCENARIO 1: CLOUD VM PROVISIONING FROM BASELINE TEMPLATES ---");
        CloudTemplateRegistry cloudRegistry = new CloudTemplateRegistry();

        VirtualMachineConfiguration microserviceBaseline = new VirtualMachineConfiguration(
                "template-microservice", 4, 16, "Ubuntu 24.04 LTS"
        );
        microserviceBaseline.addDisk(new StorageVolume("/dev/sda1 (OS)", 50, "gp3"));
        microserviceBaseline.addDisk(new StorageVolume("/dev/sdb (Logs)", 100, "gp3"));
        microserviceBaseline.addSecurityRule(new SecurityRule("TCP", 8080, "0.0.0.0/0"));
        microserviceBaseline.addSecurityRule(new SecurityRule("TCP", 22, "10.0.0.0/16"));

        cloudRegistry.registerTemplate("microservice-standard", microserviceBaseline);
        System.out.println("[Template Registered] Baseline Template:");
        microserviceBaseline.printSummary();

        System.out.println("\n[Action] Provisioning 'order-service-prod-01' from template:");
        VirtualMachineConfiguration orderVm = cloudRegistry.createInstanceFromTemplate(
                "microservice-standard", "order-service-prod-01"
        );
        orderVm.printSummary();

        System.out.println("\n[Action] Provisioning 'analytics-worker-01' with extended RAM & additional disk:");
        VirtualMachineConfiguration analyticsVm = cloudRegistry.createInstanceFromTemplate(
                "microservice-standard", "analytics-worker-01"
        );
        analyticsVm.setRamGb(32);
        analyticsVm.addDisk(new StorageVolume("/dev/sdc (DataWarehouse)", 500, "io2"));

        analyticsVm.getAttachedDisks().get(1).setCapacityGb(250);

        analyticsVm.printSummary();

        System.out.println("\n[Verification] Checking original baseline template to confirm deep-copy integrity:");
        microserviceBaseline.printSummary();

        System.out.println("\n--- SCENARIO 2: GAME UNIT SPAWNER REGISTRY & DEEP CLONES ---");
        UnitSpawnerRegistry spawner = new UnitSpawnerRegistry();

        WarriorUnit goblinArchetype = new WarriorUnit("Goblin Marauder", 80, "Rusty Dagger");
        goblinArchetype.addInventoryItem("Health Salve");
        goblinArchetype.addInventoryItem("Torch");

        SorcererUnit archmageArchetype = new SorcererUnit("High Elf Archmage", 150, 300);
        archmageArchetype.learnSpell("Chain Lightning");
        archmageArchetype.learnSpell("Blizzard Storm");
        archmageArchetype.learnSpell("Teleport");

        spawner.registerPrototype("goblin", goblinArchetype);
        spawner.registerPrototype("archmage", archmageArchetype);

        System.out.println("[Action] Spawning Goblin Ambush Squad:");
        WarriorUnit goblin1 = (WarriorUnit) spawner.spawn("goblin", 15, 30);
        WarriorUnit goblin2 = (WarriorUnit) spawner.spawn("goblin", 18, 32);

        goblin2.setMainWeapon("Poisoned Shortsword");
        goblin2.addInventoryItem("Smoke Bomb");

        goblin1.printDetails();
        goblin2.printDetails();

        System.out.println("\n[Action] Spawning Archmage Defender:");
        SorcererUnit defender = (SorcererUnit) spawner.spawn("archmage", 50, 50);
        defender.learnSpell("Arcane Barrier");
        defender.printDetails();

        System.out.println("\n[Verification] Checking Archetype Prototypes (unaffected by spawned clones):");
        goblinArchetype.printDetails();
        archmageArchetype.printDetails();

        System.out.println("\n=================================================================");
        System.out.println("                 PROTOTYPE PATTERN COMPLETED                     ");
        System.out.println("=================================================================");
    }
}
