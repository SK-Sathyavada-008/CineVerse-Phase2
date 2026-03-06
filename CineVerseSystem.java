package CineVerse;

import java.util.Scanner;

public class CineVerseSystem {

    // Movie database (up to 100 movies)
    Movie[] movies = new Movie[100];
    int count  = 0;
    int nextId = 6;

    // User database (up to 50 users) 
    User[] users   = new User[50];
    int userCount  = 0;
    User activeUser = null;  // currently logged-in user

    // Constructor – initialise default movies
    public CineVerseSystem() {
    		movies[0] = new Movie(0, "3 Idiots", "Comedy", "Hindi", "U/A", 2009, "Netflix", 4.8, 5000);
    		movies[1] = new Movie(1, "Brahma Yugam", "Mystery", "Telugu", "U/A", 2024, "SonyLIV", 4.3, 5450);
    		movies[2] = new Movie(2, "Dil Bechara", "Drama", "Hindi", "U/A", 2020, "Disney+ Hotstar", 4.5, 3500);
    		movies[3] = new Movie(3, "Chhichhore", "Comedy", "Hindi", "U/A", 2019, "Disney+ Hotstar", 4.6, 2800);
    		movies[4] = new Movie(4, "Hi Nanna", "Romance", "Telugu", "U/A", 2023, "Netflix", 4.4, 1500);
    		movies[5] = new Movie(5, "Krishna Aur Kans", "Mythology", "Hindi", "U", 2012, "Disney+ Hotstar", 4.8, 8100);
    		count = 6;
        // By default sort titles into ascending order while adding 
        mergeSortByTitle(movies, 0, count - 1);
    }

    // Add movie (By default sort titles into ascending order while adding ) 
    public void addMovie(Scanner sc) {
        sc.nextLine(); // consume leftover newline
        System.out.print("Enter Title: ");
        String title = sc.nextLine();
        System.out.print("Enter Genre: ");
        String genre = sc.nextLine();
        System.out.print("Enter Language: ");
        String language = sc.nextLine();
        System.out.print("Enter Age Rating (U / U/A / A / PG-13 / R): ");
        String ageRating = sc.nextLine();
        System.out.print("Enter Year Released: ");
        int year = sc.nextInt(); sc.nextLine();
        System.out.print("Enter OTT Platform: ");
        String ott = sc.nextLine();
        System.out.print("Enter Base Rating (1-5): ");
        double baseRating = sc.nextDouble();

        if (count < movies.length) {
            movies[count++] = new Movie(nextId++, title, genre, language,
                                        ageRating, year, ott, baseRating, 0);
            // Keep array sorted alphabetically so binary search stays valid
            mergeSortByTitle(movies, 0, count - 1);
            System.out.println("Movie added successfully with ID: " + (nextId - 1));
        } else {
            System.out.println("Cannot add more movies. Array is full.");
        }
    }

    // Display all movies
    public void displayMovies() {
        System.out.printf("%-4s | %-40s | %-12s | %-8s | %-5s | %4s | %-15s | %-12s | %s%n",
                "ID", "Title", "Genre", "Language", "Age", "Year", "OTT Platform", "Rating", "Views");
        System.out.println("-".repeat(120));
        for (int i = 0; i < count; i++) {
            movies[i].display();
        }
    }

    // Binary Search by Title (array is always kept sorted alphabetically)
    public void binarySearchByTitle(String title) {
        int si = 0, ei = count - 1;
        boolean found = false;
        while (si <= ei) {
            int mi = (si + ei) / 2;
            int cmp = movies[mi].title.compareToIgnoreCase(title);
            if (cmp == 0) {
                movies[mi].display();
                found = true;
                break;
            } else if (cmp < 0) {
                si = mi + 1;
            } else {
                ei = mi - 1;
            }
        }
        if (!found) System.out.println("Movie not found!");
    }

    // find array index by title (after sort)
    private int findIndexByTitle(String title) {
        for (int i = 0; i < count; i++) {
            if (movies[i].title.equalsIgnoreCase(title)) return i;
        }
        return -1;
    }

    // find movie object by title
    public Movie getMovieByTitle(String title) {
        int idx = findIndexByTitle(title);
        return idx == -1 ? null : movies[idx];
    }

    // Watch Movie
    public void watchMovie(Scanner sc) throws InterruptedException {
        if (activeUser == null) {
            System.out.println("Please log in first.");
            return;
        }
        sc.nextLine();
        System.out.print("Enter movie title to watch: ");
        String title = sc.nextLine();
        Movie m = getMovieByTitle(title);
        if (m == null) {
            System.out.println("Movie not found!");
            return;
        }

        // "Play" the movie
        System.out.println("\n🎬 Now playing: " + m.title);
        Thread.sleep(1000);
        System.out.println("...\nThe End...\n");

        // Ask for rating
        System.out.print("Rate this movie (1-10): ");
        int userRating = sc.nextInt();
        while (userRating < 1 || userRating > 10) {
            System.out.print("Invalid. Enter a rating between 1 and 10: ");
            userRating = sc.nextInt();
        }
        m.addUserRating(userRating);
        System.out.printf("Thanks! Updated movie rating: %.1f%n", m.rating);

        // Update user records
        activeUser.addToHistory(m);
        activeUser.removeFromWatchlist(m.movieId);
    }

    // User management
    public void registerUser(Scanner sc) {
        if (userCount >= users.length) {
            System.out.println("User limit reached.");
            return;
        }
        sc.nextLine();
        System.out.print("Enter username: ");
        String name = sc.nextLine();
        // Check duplicate
        for (int i = 0; i < userCount; i++) {
            if (users[i].username.equalsIgnoreCase(name)) {
                System.out.println("Username already exists.");
                return;
            }
        }
        System.out.print("Enter your preferred genre (Action/Comedy/Fantasy/Animation/Adventure/etc.): ");
        String genre = sc.nextLine();
        users[userCount++] = new User(name, genre);
        System.out.println("User \"" + name + "\" registered successfully!");
    }

    public void loginUser(Scanner sc) {
        sc.nextLine();
        System.out.print("Enter username: ");
        String name = sc.nextLine();
        for (int i = 0; i < userCount; i++) {
            if (users[i].username.equalsIgnoreCase(name)) {
                activeUser = users[i];
                System.out.println("Welcome back, " + activeUser.username + "! 👋");
                return;
            }
        }
        System.out.println("User not found. Please register first.");
    }

    public void logoutUser() {
        if (activeUser != null) {
            System.out.println("Goodbye, " + activeUser.username + "!");
            activeUser = null;
        } else {
            System.out.println("No user is logged in.");
        }
    }

    // Recommendation – genre match 
    public void showRecommendations(Scanner sc) {
        if (activeUser == null) {
            System.out.println("Please log in first.");
            return;
        }
        System.out.println("\nRecommended for you :");
        boolean any = false;
        for (int i = 0; i < count; i++) {
            if (movies[i].genre.equalsIgnoreCase(activeUser.preferredGenre)) {
                movies[i].display();
                any = true;
                System.out.print("Add to watchlist? (y/n): ");
                String ans = sc.next();
                if (ans.equalsIgnoreCase("y")) {
                    activeUser.addToWatchlist(movies[i]);
                }
            }
        }
        if (!any) System.out.println("No new recommendations at the moment.");
    }

    // Sorting methods

    // Quick Sort – descending by rating
    void quickSortByRating(Movie[] a, int si, int ei) {
        if (si < ei) {
            int j = partitionByRating(a, si, ei);
            quickSortByRating(a, si, j - 1);
            quickSortByRating(a, j + 1, ei);
        }
    }
    int partitionByRating(Movie[] a, int si, int ei) {
        double pivot = a[si].rating;
        int i = si, j = ei + 1;
        do {
            do { i++; } while (i <= ei && a[i].rating > pivot);
            do { j--; } while (a[j].rating < pivot);
            if (i < j) swap(a, i, j);
        } while (i < j);
        swap(a, si, j);
        return j;
    }

    // Quick Sort – descending by views
    void quickSortByViews(Movie[] a, int si, int ei) {
        if (si < ei) {
            int j = partitionByViews(a, si, ei);
            quickSortByViews(a, si, j - 1);
            quickSortByViews(a, j + 1, ei);
        }
    }
    int partitionByViews(Movie[] a, int si, int ei) {
        int pivot = a[si].views;
        int i = si, j = ei + 1;
        do {
            do { i++; } while (i <= ei && a[i].views > pivot);
            do { j--; } while (a[j].views < pivot);
            if (i < j) swap(a, i, j);
        } while (i < j);
        swap(a, si, j);
        return j;
    }

    // Merge Sort – ascending by title (alphabetical)
    void mergeSortByTitle(Movie[] arr, int si, int ei) {
        if (si < ei) {
            int mi = (si + ei) / 2;
            mergeSortByTitle(arr, si, mi);
            mergeSortByTitle(arr, mi + 1, ei);
            mergeByTitle(arr, si, mi, ei);
        }
    }
    void mergeByTitle(Movie[] arr, int si, int mi, int ei) {
        Movie[] temp = new Movie[ei - si + 1];
        int i = si, j = mi + 1, k = 0;
        while (i <= mi && j <= ei) {
            if (arr[i].title.compareToIgnoreCase(arr[j].title) <= 0)
                temp[k++] = arr[i++];
            else
                temp[k++] = arr[j++];
        }
        while (i <= mi) temp[k++] = arr[i++];
        while (j <= ei) temp[k++] = arr[j++];
        for (int x = si, y = 0; x <= ei; x++, y++) arr[x] = temp[y];
    }

    // Merge Sort – ascending by language (alphabetical)
    void mergeSortByLanguage(Movie[] arr, int si, int ei) {
        if (si < ei) {
            int mi = (si + ei) / 2;
            mergeSortByLanguage(arr, si, mi);
            mergeSortByLanguage(arr, mi + 1, ei);
            mergeByLanguage(arr, si, mi, ei);
        }
    }
    void mergeByLanguage(Movie[] arr, int si, int mi, int ei) {
        Movie[] temp = new Movie[ei - si + 1];
        int i = si, j = mi + 1, k = 0;
        while (i <= mi && j <= ei) {
            if (arr[i].language.compareToIgnoreCase(arr[j].language) <= 0)
                temp[k++] = arr[i++];
            else
                temp[k++] = arr[j++];
        }
        while (i <= mi) temp[k++] = arr[i++];
        while (j <= ei) temp[k++] = arr[j++];
        for (int x = si, y = 0; x <= ei; x++, y++) arr[x] = temp[y];
    }

 // Merge Sort – descending by year released (most recent first)
    void mergeSortByYear(Movie[] arr, int si, int ei) {
        if (si < ei) {
            int mi = (si + ei) / 2;
            mergeSortByYear(arr, si, mi);
            mergeSortByYear(arr, mi + 1, ei);
            mergeByYear(arr, si, mi, ei);
        }
    }
    void mergeByYear(Movie[] arr, int si, int mi, int ei) {
        Movie[] temp = new Movie[ei - si + 1];
        int i = si, j = mi + 1, k = 0;
        while (i <= mi && j <= ei) {
            if (arr[i].yearReleased >= arr[j].yearReleased) 
                temp[k++] = arr[i++];
            else
                temp[k++] = arr[j++];
        }
        while (i <= mi) temp[k++] = arr[i++];
        while (j <= ei) temp[k++] = arr[j++];
        for (int x = si, y = 0; x <= ei; x++, y++) arr[x] = temp[y];
    }

    public void swap(Movie[] a, int i, int j) {
        Movie temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }
}
