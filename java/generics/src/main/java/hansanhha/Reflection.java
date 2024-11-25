package hansanhha;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class Reflection {

    public static void main(String[] args) throws NoSuchFieldException {
        Won won = new Won(1000);
        Dollar dollar = new Dollar(2000);

        Bank<Won> bankOfKorea = new Bank<>(won);
        Bank<Dollar> fed = new Bank<>(dollar);

        Field field = bankOfKorea.getClass().getDeclaredField("money");

    }

    private static class Bank<Money extends Printable> {
        Money money;

        public Bank(Money money) {
            this.money = money;
        }

        private void print() {
            System.out.println(money.getValue());
        }
    }

    private static class Dollar implements Printable {
        int value;

        public Dollar(int value) {
            this.value = value;
        }

        @Override
        public int getValue() {
            return value;
        }
    }

    private static class Won implements Printable {
        int value;

        public Won(int value) {
            this.value = value;
        }

        @Override
        public int getValue() {
            return value;
        }
    }

    private interface Printable {
        int getValue();
    }
}
