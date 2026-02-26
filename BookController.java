package com.example.webbackend.controller;

import com.example.webbackend.entity.Book;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class BookController {

    private List<Book> books = new ArrayList<>();

    private Long nextId = 1L;

    public BookController() {
        // Add 15 books with varied data for testing
        books.add(new Book(nextId++, "Spring Boot in Action", "Craig Walls", 39.99));
        books.add(new Book(nextId++, "Effective Java", "Joshua Bloch", 45.00));
        books.add(new Book(nextId++, "Clean Code", "Robert Martin", 42.50));
        books.add(new Book(nextId++, "Java Concurrency in Practice", "Brian Goetz", 49.99));
        books.add(new Book(nextId++, "Design Patterns", "Gang of Four", 54.99));
        books.add(new Book(nextId++, "Head First Java", "Kathy Sierra", 35.00));
        books.add(new Book(nextId++, "Spring in Action", "Craig Walls", 44.99));
        books.add(new Book(nextId++, "Clean Architecture", "Robert Martin", 39.99));
        books.add(new Book(nextId++, "Refactoring", "Martin Fowler", 47.50));
        books.add(new Book(nextId++, "The Pragmatic Programmer", "Andrew Hunt", 41.99));
        books.add(new Book(nextId++, "You Don't Know JS", "Kyle Simpson", 29.99));
        books.add(new Book(nextId++, "JavaScript: The Good Parts", "Douglas Crockford", 32.50));
        books.add(new Book(nextId++, "Eloquent JavaScript", "Marijn Haverbeke", 27.99));
        books.add(new Book(nextId++, "Python Crash Course", "Eric Matthes", 38.00));
        books.add(new Book(nextId++, "Automate the Boring Stuff", "Al Sweigart", 33.50));
    }

    // get all books - /api/books
    @GetMapping("/books")
    public List<Book> getBooks() {
        return books;
    }

    // get book by id
    @GetMapping("/books/{id}")
    public Book getBook(@PathVariable Long id) {
        return books.stream().filter(book -> book.getId().equals(id))
                .findFirst().orElse(null);
    }

    // create a new book
    @PostMapping("/books")
    public List<Book> createBook(@RequestBody Book book) {
        books.add(book);
        return books;
    }

    // search by title
    @GetMapping("/books/search")
    public List<Book> searchByTitle(
            @RequestParam(required = false, defaultValue = "") String title
    ) {
        if(title.isEmpty()) {
            return books;
        }

        return books.stream()
                .filter(book -> book.getTitle().toLowerCase().contains(title.toLowerCase()))
                .collect(Collectors.toList());

    }

    // price range
    @GetMapping("/books/price-range")
    public List<Book> getBooksByPrice(
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice
    ) {
        return books.stream()
                .filter(book -> {
                    boolean min = minPrice == null || book.getPrice() >= minPrice;
                    boolean max = maxPrice == null || book.getPrice() <= maxPrice;

                    return min && max;
                }).collect(Collectors.toList());
    }

    // sort
    @GetMapping("/books/sorted")
    public List<Book> getSortedBooks(
            @RequestParam(required = false, defaultValue = "title") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String order
    ){
        Comparator<Book> comparator;

        switch(sortBy.toLowerCase()) {
            case "author":
                comparator = Comparator.comparing(Book::getAuthor);
                break;
                case "title":
                comparator = Comparator.comparing(Book::getTitle);
            default:
                comparator = Comparator.comparing(Book::getTitle);
                break;
        }

        if("desc".equalsIgnoreCase(order)) {
            comparator = comparator.reversed();
        }

        return books.stream().sorted(comparator)
                .collect(Collectors.toList());



    }

    // Update book
    @PutMapping("/books/{id}")
    public Book updateBOok(@PathVariable Long id, @RequestBody Book updatedBook) {

        Book book = books.stream()
                .filter(b -> b.getId().equals(id))
                .findFirst()
                .orElse(null);
        if(book != null) {
            book.setTitle(updatedBook.getTitle());
            book.setAuthor(updatedBook.getAuthor());
            book.setPrice(updatedBook.getPrice());
        }

        return book;
    }

    // Patch (Partial Update)
    @PatchMapping("/books/{id}")
    public Book patchBOok(@PathVariable Long id, @RequestBody Book updatedBook) {
        Book book = (books.stream())
                .filter(b -> b.getId().equals(id))
                .findFirst()
                .orElse(null);


        if(book == null) {
            return null;
        }

        if(updatedBook.getTitle() != null) {
            book.setTitle(updatedBook.getTitle());
        }

        if(updatedBook.getAuthor() != null) {
            book.setAuthor(updatedBook.getAuthor());
        }

        if(updatedBook.getPrice() != null) {
            book.setPrice(updatedBook.getPrice());
        }

        return book;
    }

    // Delete
    @DeleteMapping("/books/{id}")
    public void deleteBOok(@PathVariable Long id) {
        books.removeIf(book -> book.getId().equals(id));
    }

    // GET with Pagination
    @GetMapping("/books/paginated")
    public ResponseEntity<List<Book>> getBooksPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "4") int size
    ){
        int start = page * size;
        int end = Math.min(start + size, books.size());

        if (start >= books.size()) {
            return ResponseEntity.ok(new ArrayList<>());
        }

        List<Book> paginatedBooks = books.subList(start, end);

        return ResponseEntity.ok(paginatedBooks);
    }

    // Get w/ Filtering, Sorting, and Pagination

    @GetMapping("/books/advanced")
    public ResponseEntity<Map<String, Object>> getBooksAdvanced(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(defaultValue = "title") String sortby,
            @RequestParam(defaultValue = "asc") String order,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        List<Book> filtered = books.stream()
                .filter(book -> title == null || book.getTitle().toLowerCase().contains(title.toLowerCase()))
                .filter(book -> author == null || book.getAuthor().toLowerCase().contains(author.toLowerCase()))
                .filter(book -> minPrice == null || book.getPrice() >= minPrice)
                .filter(book -> maxPrice == null || book.getPrice() <= maxPrice)
                .collect(Collectors.toList());

        Comparator<Book> comparator;

        switch (sortby.toLowerCase()) {
            case "author":
                comparator = Comparator.comparing(Book::getAuthor);
                break;
            case "price":
                comparator = Comparator.comparing(Book::getPrice);
                break;
            default:
                comparator = Comparator.comparing(Book::getTitle);
        }

        if ("desc".equalsIgnoreCase(order)) {
            comparator = comparator.reversed();
        }

        List<Book> sorted = filtered.stream()
                .sorted(comparator)
                .collect(Collectors.toList());

        int start = page * size;
        int end = Math.min(start + size, sorted.size());

        List<Book> paginated = start >= sorted.size()
                ? new ArrayList<>()
                : sorted.subList(start,end);

        Map<String,Object> response = new HashMap<>();
        response.put("books", paginated);
        response.put("currentPage", page);
        response.put("pageSize", size);
        response.put("totalItems", sorted.size());
        response.put("totalPages", (int)Math.ceil((double) sorted.size() / size));

        return ResponseEntity.ok(response);


    }


}
