public class Main {
    public static void main(String[] args) {
        System.out.println("Mayın Tarlası Oyununa Hoş Geldiniz!");

        MineSweeper game = new MineSweeper();

        game.initialState();

        game.run();
    }
}