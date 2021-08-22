package kotoale.parking.lot.model;

/**
 * Parking lot receipt.
 */
public class Receipt {
    private final int slotNo;
    private final int charge;

    /**
     * @param slotNo slot No
     * @param charge charge
     */
    public Receipt(int slotNo, int charge) {
        this.slotNo = slotNo;
        this.charge = charge;
    }

    /**
     * @return slot No
     */
    public int getSlotNo() {
        return slotNo;
    }

    /**
     * @return charge
     */
    public int getCharge() {
        return charge;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Receipt receipt = (Receipt) o;
        return slotNo == receipt.slotNo && charge == receipt.charge;
    }

}
