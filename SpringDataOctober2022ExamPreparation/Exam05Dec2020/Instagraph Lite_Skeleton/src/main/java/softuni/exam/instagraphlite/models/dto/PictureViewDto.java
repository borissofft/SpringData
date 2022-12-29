package softuni.exam.instagraphlite.models.dto;

public class PictureViewDto {
    private String path;
    private Double size;

    public PictureViewDto() {

    }

    public PictureViewDto(String path, Double size) {
        this.path = path;
        this.size = size;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Double getSize() {
        return size;
    }

    public void setSize(Double size) {
        this.size = size;
    }
}
