
import java.util.function.Consumer;

public class Student1 implements Runnable {

    private final int[] mas;
    private final boolean reverse;
    private final Consumer<String> output;

    public Student1(int[] mas, boolean reverse,
                    Consumer<String> output) {
        this.mas = mas;
        this.reverse = reverse;
        this.output = output;
    }

    @Override
    public void run() {
        int first = -1;

        int start = reverse ? mas.length - 1 : 0;
        int end = reverse ? -1 : mas.length;
        int step = reverse ? -1 : 1;

        for (int i = start; i != end; i += step) {

            if (mas[i] % 2 != 0) {

                if (first == -1) {
                    first = i;
                } else {
                    int sum = first + i;

                    output.accept(
                            Thread.currentThread().getName()
                                    + ": числа " + mas[first] + " и " + mas[i]
                                    + " | позиции " + first + " + " + i
                                    + " = " + sum + "\n"
                    );

                    first = -1;
                }
            }
        }
    }
}
