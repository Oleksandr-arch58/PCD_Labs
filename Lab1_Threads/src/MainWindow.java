
import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Random;

public class MainWindow extends JFrame {

    private final JTextArea arrayArea = new JTextArea();
    private final JTextArea resultArea = new JTextArea();
    private final JTextArea studentArea = new JTextArea();

    private final JButton generateButton =
            new JButton("Сгенерировать массив");
    private final JButton startButton =
            new JButton("Запустить потоки");
    private final JButton clearButton =
            new JButton("Очистить");

    private final int[] mas = new int[100];
    private final Random random = new Random();

    public MainWindow() {
        setTitle("Лабораторная №1 — Вариант 4");
        setSize(900, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BorderLayout(10, 10));

        // Верхняя панель с кнопками
        JPanel buttonPanel = new JPanel(new FlowLayout());

        buttonPanel.add(generateButton);
        buttonPanel.add(startButton);
        buttonPanel.add(clearButton);

        add(buttonPanel, BorderLayout.NORTH);

        // Поля вывода
        arrayArea.setEditable(false);
        resultArea.setEditable(false);
        studentArea.setEditable(false);

        arrayArea.setLineWrap(true);
        arrayArea.setWrapStyleWord(true);

        resultArea.setFont(new Font(
                Font.MONOSPACED, Font.PLAIN, 13));

        // Панель с массивом
        JPanel arrayPanel = new JPanel(new BorderLayout());
        arrayPanel.setBorder(BorderFactory.createTitledBorder(
                "Общий массив (100 чисел)"));

        arrayPanel.add(new JScrollPane(arrayArea));

        // Панель результатов
        JPanel resultPanel = new JPanel(new BorderLayout());
        resultPanel.setBorder(BorderFactory.createTitledBorder(
                "Результаты четырёх потоков"));

        resultPanel.add(new JScrollPane(resultArea));

        // Панель информации
        JPanel studentPanel = new JPanel(new BorderLayout());
        studentPanel.setBorder(BorderFactory.createTitledBorder(
                "Информация о студентах"));

        studentPanel.add(new JScrollPane(studentArea));

        // Размещаем панели
        JPanel centerPanel = new JPanel(new GridLayout(3, 1, 5, 5));

        centerPanel.add(arrayPanel);
        centerPanel.add(resultPanel);
        centerPanel.add(studentPanel);

        add(centerPanel, BorderLayout.CENTER);

        // Действия кнопок
        generateButton.addActionListener(e -> generateArray());

        startButton.addActionListener(e -> startThreads());

        clearButton.addActionListener(e -> {
            resultArea.setText("");
            studentArea.setText("");
        });

        generateArray();
    }

    private void generateArray() {
        for (int i = 0; i < mas.length; i++) {
            mas[i] = random.nextInt(100) + 1;
        }

        arrayArea.setText(Arrays.toString(mas));
        resultArea.setText("");
        studentArea.setText("");
    }

    private void startThreads() {
        startButton.setEnabled(false);
        generateButton.setEnabled(false);
        clearButton.setEnabled(false);

        resultArea.setText("");
        studentArea.setText("");

        // Запускаем работу в фоновом потоке
        new Thread(() -> {

            java.util.function.Consumer<String> output = text ->
                    SwingUtilities.invokeLater(() ->
                            resultArea.append(text));

            Thread th1 = new Thread(
                    new Student1(mas, false, output),
                    "Student1-Th1"
            );

            Thread th2 = new Thread(
                    new Student1(mas, true, output),
                    "Student1-Th2"
            );

            Thread th3 = new Thread(
                    new Student2(mas, false, output),
                    "Student2-Th1"
            );

            Thread th4 = new Thread(
                    new Student2(mas, true, output),
                    "Student2-Th2"
            );


            th1.start();
            th2.start();
            th3.start();
            th4.start();

            try {
                th1.join();
                th2.join();
                th3.join();
                th4.join();

                SwingUtilities.invokeLater(() ->
                        resultArea.append(
                                "Все четыре потока завершены!\n"));

                String info =
                        "Лабораторная работа №1\n" +
                                "Вариант: 4\n" +
                                "Студент 1: Имя Фамилия\n" +
                                "Студент 2: Имя Фамилия\n";

                for (char c : info.toCharArray()) {
                    final char letter = c;

                    SwingUtilities.invokeAndWait(() ->
                            studentArea.append(
                                    String.valueOf(letter)));

                    Thread.sleep(100);
                }

            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                SwingUtilities.invokeLater(() -> {
                    startButton.setEnabled(true);
                    generateButton.setEnabled(true);
                    clearButton.setEnabled(true);
                });
            }

        }, "Controller").start();
    }
}
