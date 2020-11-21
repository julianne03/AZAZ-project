package kr.hs.emirim.seungmin.javaproject_azaz.Model;

public class User {

    public String image, name, intro;

    public User() {}

    public User(String image, String name, String intro) {
        this.image = image;
        this.name = name;
        this.intro = intro;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }
}
