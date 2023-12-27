import java.util.Random;
import java.util.Scanner;

public class MineSweeper {
    private char[][] map;
    private char[][] mineMap;
    private int numberRow;
    private int numberCol;
    private int numberMine;
    private int leftNumberMine;
    private long startTime;
    private long endTime;

    public MineSweeper() {
        initializeGame();
    }

    private void initializeGame() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Satır sayısını giriniz: ");
        numberRow = scanner.nextInt();

        System.out.print("Sütun sayısını giriniz : ");
        numberCol = scanner.nextInt();

        if (numberRow < 2 || numberCol < 2) {
            System.out.println("Satır ve sütun sayıları en az 2 olmalıdır. Tekrar deneyin.");
            initializeGame();
            return;
        }

        map = new char[numberRow][numberCol];
        mineMap = new char[numberRow][numberCol];


        int totalElements = numberRow * numberCol;
        numberMine = totalElements / 4;

        if (numberMine < 1 || numberMine >= totalElements) {
            System.out.println("Geçersiz mayın sayısı. Tekrar deneyiniz.");
            initializeGame();
            return;
        }

        leftNumberMine = numberMine;

        initializeMap();
        placeMines();
    }

    private void initializeMap() {
        for (int i = 0; i < numberRow; i++) {
            for (int j = 0; j < numberCol; j++) {
                map[i][j] = '-';
                mineMap[i][j] = '-';
            }
        }
    }

    private void placeMines() {
        int placedMines = 0;
        Random random = new Random();

        while (placedMines < numberMine) {
            int randomRow = random.nextInt(numberRow);
            int randomCol = random.nextInt(numberCol);

            if (mineMap[randomRow][randomCol] != '*') {
                mineMap[randomRow][randomCol] = '*';
                placedMines++;
            }
        }
    }

    private void printMap() {
        System.out.println("  ");
        for (int i = 0; i < numberCol; i++) {
            System.out.print(" " + (i + 1));
        }
        System.out.println();

        for (int i = 0; i < numberRow; i++) {
            System.out.print((i + 1) + " ");
            for (int j = 0; j < numberCol; j++) {
                System.out.print(map[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    private void printHint() {
        System.out.println("  ");
        for (int i = 0; i < numberCol; i++) {
            System.out.print(" " + (i + 1));
        }
        System.out.println();

        for (int i = 0; i < numberRow; i++) {
            System.out.print((i + 1) + " ");
            for (int j = 0; j < numberCol; j++) {
                char cellValue = map[i][j];
                if (cellValue == ' ' || cellValue == '-') {
                    System.out.print(mineMap[i][j] + " ");
                } else {
                    System.out.print(cellValue + " ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    private void revealCell(int row, int col) {
        if (isValidCoordinate(row, col) && map[row][col] == '-') {
            int adjacentMines = countAdjacentMines(row, col);

            if (adjacentMines > 0) {
                map[row][col] = (char) (adjacentMines + '0');
            } else {
                map[row][col] = ' ';

                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        revealCell(row + i, col + j);
                    }
                }
            }
        }
    }

    private int countAdjacentMines(int row, int col) {
        int count = 0;

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (isValidCoordinate(row + i, col + j) && mineMap[row + i][col + j] == '*') {
                    count++;
                }
            }
        }

        return count;
    }

    private boolean isValidCoordinate(int row, int col) {
        return row >= 0 && row < numberRow && col >= 0 && col < numberCol;
    }

    private boolean isMine(int row, int col) {
        return mineMap[row][col] == '*';
    }

    public void run() {
        startTime = System.currentTimeMillis();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            printMap();

            System.out.print("Satır giriniz : ");
            int row = scanner.nextInt() - 1;

            System.out.print("Sütun giriniz : ");
            int col = scanner.nextInt() - 1;

            if (!isValidCoordinate(row, col)) {
                System.out.println("Hatalı giriş yaptınız. Lütfen tekrar giriniz.");
                continue;
            }

            if (map[row][col] != '-') {
                System.out.println("Daha önce aynı seçimi yaptınız. Başka bir koordinat giriniz.");
                continue;
            }

            if (isMine(row, col)) {
                printMap();
                System.out.println("Mayına bastınız! Oyunu kaybettiniz.");
                break;
            } else {
                revealCell(row, col);
                leftNumberMine--;

                if (leftNumberMine == 0) {
                    long timeMillis = endTime - startTime;
                    long timeSeconds = timeMillis / 1000;

                    int point = calculateScore(timeSeconds);

                    printMap();
                    System.out.println("Tebrikler! Tüm mayınları buldunuz. Oyunu kazandınız!");
                    System.out.println("Geçen süre: " + timeSeconds + " saniye");
                    System.out.println("Toplam puanınız: " + point);
                    break;
                }
            }

            printHint();

        }

        System.out.print("Yeniden oynamak istiyor musunuz? (Evet için 'E' / Hayır için 'H'): ");
        char answer = scanner.next().charAt(0);

        if (answer == 'E' || answer == 'e') {
            restartGame();
        } else {
            System.out.println("Oyun sonlandırıldı. İyi günler!");
        }

        scanner.close();
    }

    private int calculateScore(long sure) {
        return (int) (sure * 10);
    }

    private void restartGame() {
        initializeGame();
    }

    public void initialState() {
        for (int i = 0; i < numberRow; i++) {
            for (int j = 0; j < numberCol; j++) {
                System.out.print(mineMap[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void main(String[] args) {
        MineSweeper game = new MineSweeper();
        game.initialState();
        game.run();
    }
}