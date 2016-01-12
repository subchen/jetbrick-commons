package jetbrick.files;

@javax.xml.bind.annotation.XmlRootElement
public class Jdk8Lambda {

    public static void main(String[] args) {
        new Thread(() -> System.out.println("In Java8, Lambda expression rocks !!")).start(); 
    }

}
