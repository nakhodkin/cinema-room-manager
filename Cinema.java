package cinema;

import java.util.Arrays;
import java.util.Scanner;

public class Cinema {
    private static final int FRONT_TICKET_PRICE = 10;
    private static final int BACK_TICKET_PRICE = 8;

    private static final int MENU_SHOW_SEATS = 1;
    private static final int MENU_BUY_TICKET = 2;
    private static final int MENU_SHOW_STATISTICS = 3;
    private static final int MENU_EXIT = 0;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the number of rows:");
        final int rows = scanner.nextInt();

        System.out.println("Enter the number of seats in each row:");
        final int seats = scanner.nextInt();

        String[][] cinemaHall = buildCinemaLayout(rows, seats);

        int choice;

        do {
            displayMenu();

            choice = scanner.nextInt();

            switch (choice) {
                case MENU_SHOW_SEATS:
                    displayCinemaLayout(cinemaHall);
                    break;
                case MENU_BUY_TICKET:
                    buyTicket(cinemaHall, rows, seats);
                    break;
                case MENU_SHOW_STATISTICS:
                    showStatistics(cinemaHall, rows, seats);
                    break;
                case MENU_EXIT:
                    break;
                default:
                    System.out.println("Illegal option");
                    break;
            }
        } while (choice != MENU_EXIT);
    }

    private static String[][] buildCinemaLayout(int rows, int seats) {
        String[][] cinemaHall = new String[rows][seats];

        for (String[] row: cinemaHall) {
            Arrays.fill(row, "S");
        }

        return cinemaHall;
    }

    private static void displayCinemaLayout(String[][] cinemaHall) {
        System.out.println("Cinema:");

        for (int i = 0; i < cinemaHall.length + 1; i++) {
            for (int j = 0; j < cinemaHall[0].length + 1; j++) {
                if (i == 0 && j == 0) {
                    System.out.print("  ");
                } else if (i == 0) {
                    System.out.print(j + " ");
                } else if (j == 0) {
                    System.out.print(i + " ");
                } else {
                    System.out.print(cinemaHall[i - 1][j - 1] + " ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    private static int calculateTotalIncome(int rows, int seats) {
        int cinemaSize = rows * seats;

        return cinemaSize <= 60
            ? FRONT_TICKET_PRICE * cinemaSize
            : (rows / 2 * seats * FRONT_TICKET_PRICE) + (rows - (rows / 2)) * seats * BACK_TICKET_PRICE;
    }

    private static int calculateCurrentIncome(String[][] cinemaHall, int rows, int seats) {
        int currentIncome = 0;

        for (int i = 0; i < rows; i++) {
            for (String seat: cinemaHall[i]) {
                if (seat.equalsIgnoreCase("B")) {
                    currentIncome += calculateTicketPrice(rows, seats, i + 1);
                }
            }
        }

        return currentIncome;
    }

    private static int getNumberOfPurchasedTickets(String[][] cinemaHall) {
        int numberOfPurchasedTickets = 0;

        for (String[] row: cinemaHall) {
            for (String seat: row) {
                if (seat.equalsIgnoreCase("B")) {
                    numberOfPurchasedTickets += 1;
                }
            }
        }

        return numberOfPurchasedTickets;
    }

    private static void bookTicket(String[][] cinemaHall, int row, int seat) {
        cinemaHall[row - 1][seat - 1] = "B";
    }

    private static int calculateTicketPrice(int rows, int seats, int row) {
        int cinemaSize = rows * seats;

        boolean frontSeat = row <= rows / 2;
        boolean smallCinema = cinemaSize <= 60;

        return smallCinema || frontSeat ? FRONT_TICKET_PRICE : BACK_TICKET_PRICE;
    }

    private static void displayMenu() {
        for (String item: new String[]{ "1. Show the seats", "2. Buy a ticket", "3. Statistics", "0. Exit" }) {
            System.out.println(item);
        }
    }

    private static void buyTicket(String[][] cinemaHall, int rows, int seats) {
        Scanner scanner = new Scanner(System.in);

        do {
            System.out.println("Enter a row number:");
            final int row = scanner.nextInt();

            System.out.println("Enter a seat number in that row:");
            final int seat = scanner.nextInt();

            if (!isValidSeat(rows, seats, row, seat)) {
                System.out.println("Wrong input!");
                continue;
            }

            if (isSeatBooked(cinemaHall, row, seat)) {
                System.out.println("That ticket has already been purchased!");
                continue;
            }

            bookTicket(cinemaHall, row, seat);
            System.out.println("Ticket price: $" + calculateTicketPrice(rows, seats, row));
            break;
        } while (true);
    }

    private static boolean isValidSeat(int rows, int seats, int row, int seat) {
        return row >= 1 && row <= rows && seat >= 1 && seat <= seats;
    }

    private static boolean isSeatBooked(String[][] cinemaHall, int row, int seat) {
        return cinemaHall[row - 1][seat - 1].equalsIgnoreCase("B");
    }

    private static void showStatistics(String[][] cinemaHall, int rows, int seats) {
        int numberOfPurchasedTickets = getNumberOfPurchasedTickets(cinemaHall);
        double percentageOfPurchasedTickets = (double) numberOfPurchasedTickets / (rows * seats) * 100;

        System.out.printf("Number of purchased tickets: %d%n", numberOfPurchasedTickets);
        System.out.printf("Percentage: %.2f%%%n", percentageOfPurchasedTickets);
        System.out.printf("Current income: $%d%n", calculateCurrentIncome(cinemaHall, rows, seats));
        System.out.printf("Total income: $%d%n%n", calculateTotalIncome(rows, seats));
    }
}