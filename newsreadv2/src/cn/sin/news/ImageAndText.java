package cn.sin.news;

public class ImageAndText {
	    private String imageUrl;
	    private String text;
	    private String identinum;
	    public ImageAndText(String imageUrl, String text, String identinum) {
	        this.imageUrl = imageUrl;
	        this.text = text;
	        this.identinum=identinum;
	    }
	    public String getImageUrl() {
	        return imageUrl;
	    }
	    public String getText() {
	        return text;
	    }
	    public String getidentinum() {
	        return identinum;
	    }
}
