import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;

public class Main {
    static int lengthOfString = 100000;// длина строки
    static int countOfStrings = 10000;// кол-во элементов
    static int countInQueue = 100;//длина очереди


    public static void main(String[] args) {


        // write your code here
        ArrayBlockingQueue<String> aQueue = new ArrayBlockingQueue<>(countInQueue);
        ArrayBlockingQueue<String> bQueue = new ArrayBlockingQueue<>(countInQueue);
        ArrayBlockingQueue<String> cQueue = new ArrayBlockingQueue<>(countInQueue);

        Runnable filling = () -> {//должен наполнять очереди генерируемыми текстами
            for (int i = 1; i <= countOfStrings; i++) {//должно быть 10000
                String text = generateText("abc", lengthOfString);//должно быть 100000
                try {
                    aQueue.put(text);
                    bQueue.put(text);
                    cQueue.put(text);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread fillingThread = new Thread(filling);
        fillingThread.start();
        Thread threadA = new Thread() {
            @Override
            public void run() {
                char actualSymbol = 'a';
                //вот здесь хорошо бы вставить отдельный метод, чтоб не повторяться
                counterOfSymbol(actualSymbol, aQueue);


            }
        };
        Thread threadB = new Thread() {
            @Override
            public void run() {
                char actualSymbol = 'b';
                //вот здесь хорошо бы вставить отдельный метод, чтоб не повторяться
                counterOfSymbol(actualSymbol, bQueue);


            }
        };
        Thread threadC = new Thread() {
            @Override
            public void run() {
                char actualSymbol = 'c';
                //вот здесь хорошо бы вставить отдельный метод, чтоб не повторяться
                counterOfSymbol(actualSymbol, cQueue);


            }
        };
        threadA.start();
        threadB.start();
        threadC.start();

        try {
            threadA.join();
            threadB.join();
            threadC.join();
            fillingThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    private static void counterOfSymbol(char actualSymbol, ArrayBlockingQueue<String> symbolQueue) {
        String element;//текущая строка из списка
        String actualElement = "";//
        String resultElement = "";//строка с наибольшим кол-вом искомых букв
        int maxCountSymbol = 0; //кол-во букв в строке c максимальным кол-вом искомых букв
        int countSymbol = 0;//кол-во искомых букв в искомой строке
        for (int i = 1; i <= countOfStrings; i++) {//должно быть 10000
            try {
                element = symbolQueue.take();
                countSymbol = counter(actualSymbol, element);
                if (maxCountSymbol < countSymbol) {
                    resultElement = element;
                    maxCountSymbol = countSymbol;
                }


            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
//        System.out.println(resultElement);

        System.out.println("максимальное кол-во символов " + "\'" + actualSymbol + "\' есть " + maxCountSymbol);
    }

    private static int counter(char symbol, String element) {

        int count = 0;//подсчет указанных символов в  строке

        synchronized (element) {
            for (int i = 0; i < element.length(); i++) {
                if (element.charAt(i) == symbol) {
                    count++;
                }
            }
            return count;
        }

    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

}
