package it.unimib.aiprint.model.shop;

public class ShopResponse {
    private String image;
    private String url;

    public ShopResponse(String image, String url) {
        this.image = image;
        this.setUrl(url);
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
