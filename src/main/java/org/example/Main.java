package main.java.org.example;

import java.io.IOException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws InterruptedException, IOException {

        System.out.println("    Вариант 1   ");
        System.out.println("----1 задание----");
        FirstTask.main();
        System.out.println("----2 задание----");
        SecondTask.main();
        System.out.println("----3 задание----");
        ThirdTask.main();
        System.out.println("----4 задание----");
        FourthTask.main();
        System.out.println("----5 задание. " +
                " Для старта таймера нажмите Enter. Таймер будет остановлен после 3 секунд от начала----"
        );

        System.in.read();

        FifthTask timer = new FifthTask(5);

        timer.start();

        Thread.sleep(3000);

        timer.stop();

        System.out.println("Таймер остановлен.");

    }
}