package kotoale.parking.lot.model;

public class Receipt {
    private final int slotNo;
    private final int charge;

    public Receipt(int slotNo, int charge) {
        this.slotNo = slotNo;
        this.charge = charge;
    }

    public int getSlotNo() {
        return slotNo;
    }

    public int getCharge() {
        return charge;
    }
}
