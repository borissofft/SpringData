package softuni.exam.models.dto;

import com.google.gson.annotations.Expose;

import javax.validation.constraints.*;

public class PartSeedDto {
    @Expose
    private String partName;
    @Expose
    private double price;
    @Expose
    private int quantity;

    public PartSeedDto() {

    }

    @NotBlank
    @Size(min = 2, max = 19)
    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    @NotNull
    @DecimalMax(value = "2000")
    @DecimalMin(value = "10")
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @NotNull
    @Min(0)
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
