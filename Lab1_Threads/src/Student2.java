
import java.util.function.Consumer;

public class Student2 implements Runnable {

    private final int[] mas;
    private final boolean reverse;
    private final Consumer<String> output;

    public Student2(int[] mas, boolean reverse, Consumer<String> output) {
        this.mas = mas;
        this.reverse = reverse;
        this.output = output;
    }

    @Override
    public void run() {
        long total = 0;
        int first = -1;

        int start = reverse ? mas.length - 1 : 0;
        int end = reverse ? -1 : mas.length;
        int step = reverse ? -1 : 1;

        for (int i = start; i != end; i += step) {

            // Проверяем, является ли число чётным
            if (mas[i] % 2 == 0) {

                if (first == -1) {
                    first = i;
                } else {
                    int sum = mas[first] + mas[i];
                    total += sum;

                    output.accept(
                            Thread.currentThread().getName()
                                    + ": " + mas[first] + " + " + mas[i]
                                    + " = " + sum + "\n"
                    );

                    first = -1;
                }
            }
        }

        output.accept(
                Thread.currentThread().getName()
                        + ": ИТОГОВАЯ СУММА = " + total + "\n\n"
        );
    }
}
