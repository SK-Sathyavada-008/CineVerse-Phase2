package CineVerse;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Scanner sc = new Scanner(System.in);
        CineVerseSystem cv = new CineVerseSystem();
        int choice;

        do {
            System.out.println("\n🎬  CineVerse  🎬");
            System.out.println("================================");

            // Show active user
            if (cv.activeUser != null)
                System.out.println("👤 Logged in as: " + cv.activeUser.username);

            System.out.println();
            System.out.println(" 1. Display All Movies");
            System.out.println(" 2. Add Movie");
            System.out.println(" 3. Search Movie by Title");
            System.out.println(" 4. Sort by Rating");
            System.out.println(" 5. Sort by Views");
            System.out.println(" 6. Sort by Title");
            System.out.println(" 7. Sort by Language");
            System.out.println(" 8. Sort by Year Released");
            System.out.println();
            System.out.println("User Menu");
            System.out.println("9. Register");
            System.out.println("10. Login");
            System.out.println("11. Logout");
            System.out.println("12. Watch a Movie");
            System.out.println("13. View My Watchlist");
            System.out.println("14. View My Watch History");
            System.out.println("15. Get Recommendations");
            System.out.println();
            System.out.println("16. Exit");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    cv.displayMovies();
                    break;
                case 2:
                    cv.addMovie(sc);
                    break;
                case 3:
                    sc.nextLine();
                    System.out.print("Enter Title to Search: ");
                    String searchTitle = sc.nextLine();
                    cv.binarySearchByTitle(searchTitle);
                    break;
                case 4:
                    cv.quickSortByRating(cv.movies, 0, cv.count - 1);
                    System.out.println("\nMovies sorted by Rating :");
                    cv.displayMovies();
                    break;
                case 5:
                    cv.quickSortByViews(cv.movies, 0, cv.count - 1);
                    System.out.println("\nMovies sorted by Views :");
                    cv.displayMovies();
                    break;
                case 6:
                    cv.mergeSortByTitle(cv.movies, 0, cv.count - 1);
                    System.out.println("\nMovies sorted by Title :");
                    cv.displayMovies();
                    break;
                case 7:
                    cv.mergeSortByLanguage(cv.movies, 0, cv.count - 1);
                    System.out.println("\nMovies sorted by Language :");
                    cv.displayMovies();
                    cv.mergeSortByTitle(cv.movies, 0, cv.count - 1);
                    break;
                case 8:
                    cv.mergeSortByYear(cv.movies, 0, cv.count - 1);
                    System.out.println("\nMovies sorted by Year Released :");
                    cv.displayMovies();
                    cv.mergeSortByTitle(cv.movies, 0, cv.count - 1);
                    break;
                    
                case 9:
                    cv.registerUser(sc);
                    break;
                case 10:
                    cv.loginUser(sc);
                    break;
                case 11:
                    cv.logoutUser();
                    break;
                case 12:
                    cv.watchMovie(sc);
                    break;
                case 13:
                    if (cv.activeUser == null)
                        System.out.println("Please log in first.");
                    else
                        cv.activeUser.displayWatchlist();
                    break;
                case 14:
                    if (cv.activeUser == null)
                        System.out.println("Please log in first.");
                    else
                        cv.activeUser.displayHistory();
                    break;
                case 15:
                    cv.showRecommendations(sc);
                    break;

                case 16:
                    System.out.println("🎬 Bye!");
                    break;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }

        } while (choice != 16);
        sc.close();
    }
}
