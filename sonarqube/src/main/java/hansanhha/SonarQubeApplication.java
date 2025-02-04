package hansanhha;

public class SonarQubeApplication {

    public static void main(String[] args) {
        Calculator calculator = new Calculator();
        int sum = calculator.add(5, 3);
        System.out.println("Sum: " + sum);
    }
}
