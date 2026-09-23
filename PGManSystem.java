import java.util.Scanner;

public class PGManSystem {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int slotCount = readPositiveInt(input, "Number of slots: ");
        ParkingSlot[] slots = new ParkingSlot[slotCount];
        for (int i = 0; i < slotCount; i++) {
            System.out.print("Slot number: "); String number = input.nextLine().trim();
            String type;
            do { System.out.print("Slot type M(otorcylce) or C(ar): "); type = input.nextLine().trim().toUpperCase(); } while (!type.equals("M") && !type.equals("C"));
            slots[i] = new ParkingSlot(number, type);
        }
        Garage garage = new Garage(slots);
        int operations = readNonNegativeInt(input, "Number of operations: ");
        for (int i = 0; i < operations; i++) {
            System.out.print("Operation (P = park, E = exit): "); String operation = input.nextLine().trim().toUpperCase();
            if (operation.equals("P")) {
                System.out.print("Plate number: "); String plate = input.nextLine().trim();
                System.out.print("Owner name: "); String owner = input.nextLine().trim();
                System.out.print("Vehicle type (MOTORCYCLE or CAR): "); String type = input.nextLine().trim().toUpperCase();
                if (!type.equals("MOTORCYCLE") && !type.equals("CAR")) { System.out.println("Rejected: invalid vehicle type."); continue; }
                String slot = garage.park(new Vehicle(plate, owner, type));
                System.out.println(slot == null ? "Parking rejected: duplicate plate or no compatible slot." : "Parked in slot " + slot + ".");
            } else if (operation.equals("E")) {
                System.out.print("Plate number: "); String plate = input.nextLine().trim();
                int hours = readPositiveInt(input, "Hours parked: ");
                double fee = garage.exit(plate, hours);
                System.out.println(fee < 0 ? "Exit rejected: vehicle not found." : String.format("Exit successful. Fee: PHP %.2f", fee));
            } else System.out.println("Rejected: invalid operation.");
        }
        System.out.println("\nFinal occupancy: " + Garage.getCurrentlyParked());
        garage.displayOccupiedSlots();
        input.close();
    }
    private static int readPositiveInt(Scanner s, String prompt) { int v; do { v = readNonNegativeInt(s, prompt); } while (v == 0); return v; }
    private static int readNonNegativeInt(Scanner s, String prompt) {
        System.out.print(prompt); while (!s.hasNextInt()) { s.nextLine(); System.out.print(prompt); }
        int value = s.nextInt(); s.nextLine(); return value < 0 ? readNonNegativeInt(s, prompt) : value;
    }
}

class Vehicle {
    private String plateNumber, ownerName, type;
    public Vehicle(String plateNumber, String ownerName, String type) { this.plateNumber = plateNumber; this.ownerName = ownerName; this.type = type; }
    public String getPlateNumber() { return plateNumber; }
    public String getOwnerName() { return ownerName; }
    public String getType() { return type; }
}

class ParkingSlot {
    private String slotNumber, slotType;
    private boolean occupied;
    private Vehicle parkedVehicle;
    public ParkingSlot(String slotNumber, String slotType) { this.slotNumber = slotNumber; this.slotType = slotType; }
    public boolean isCompatible(Vehicle vehicle) { return vehicle.getType().equals("MOTORCYCLE") || slotType.equals("C"); }
    public boolean isOccupied() { return occupied; }
    public void park(Vehicle vehicle) { this.parkedVehicle = vehicle; this.occupied = true; }
    public Vehicle removeVehicle() { Vehicle vehicle = parkedVehicle; parkedVehicle = null; occupied = false; return vehicle; }
    public String getSlotNumber() { return slotNumber; }
    public Vehicle getParkedVehicle() { return parkedVehicle; }
}

class Garage {
    private ParkingSlot[] slots;
    private static int currentlyParked;
    public Garage(ParkingSlot[] slots) { this.slots = slots; }
    public String park(Vehicle vehicle) {
        if (findSlotByPlate(vehicle.getPlateNumber()) != null) return null;
        for (ParkingSlot slot : slots) if (!slot.isOccupied() && slot.isCompatible(vehicle)) { slot.park(vehicle); currentlyParked++; return slot.getSlotNumber(); }
        return null;
    }
    public double exit(String plate, int hours) {
        if (hours < 1) return -1;
        ParkingSlot slot = findSlotByPlate(plate);
        if (slot == null) return -1;
        Vehicle vehicle = slot.removeVehicle(); currentlyParked--;
        return vehicle.getType().equals("MOTORCYCLE") ? 20 + (hours - 1) * 10 : 40 + (hours - 1) * 20;
    }
    private ParkingSlot findSlotByPlate(String plate) {
        for (ParkingSlot slot : slots) if (slot.isOccupied() && slot.getParkedVehicle().getPlateNumber().equals(plate)) return slot;
        return null;
    }
    public void displayOccupiedSlots() {
        System.out.println("Occupied slots:");
        for (ParkingSlot slot : slots) if (slot.isOccupied()) System.out.println(slot.getSlotNumber() + " - " + slot.getParkedVehicle().getPlateNumber());
    }
    public static int getCurrentlyParked() { return currentlyParked; }
}

/* Problem 9 answers:
1. Garage owns ParkingSlot[] because slots are the spaces it manages and assigns.
2. A car is compatible only when slotType.equals("C").
3. Duplicate plates would represent the same vehicle parked more than once and make exits ambiguous.
4. Make Vehicle an abstract parent class, then create Motorcycle and Car subclasses with their own fee rules.
*/
