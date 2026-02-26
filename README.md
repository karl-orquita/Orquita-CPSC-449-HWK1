# Orquita-CPSC-449-HWK1

# POST

```
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
```
<img width="942" height="687" alt="image" src="https://github.com/user-attachments/assets/ad31dbac-3dc8-41c4-b62c-cbf6a41f10e7" />

# PATCH

    // Patch (Partial Update)
    @PatchMapping("/books/{id}")
    public Book patchBOok(@PathVariable Long id, @RequestBody Book updatedBook) {
        Book book = (books.stream())
                .filter(b -> b.getId().equals(id))
                .findFirst()
                .orElse(null);

        System.out.println("Before update: " + book.getAuthor());

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

        System.out.println("Before update: " + book.getAuthor());
        return book;
    }

<img width="842" height="556" alt="image" src="https://github.com/user-attachments/assets/6e8af3bd-5e20-4ea6-8fe3-0114c1487175" />

# DELETE

    // Delete
    @DeleteMapping("/books/{id}")
    public void deleteBOok(@PathVariable Long id) {
        books.removeIf(book -> book.getId().equals(id));
    }

<img width="1213" height="606" alt="image" src="https://github.com/user-attachments/assets/fa9078a6-2d91-403d-b5e7-cc4ba2e6adf6" />
<img width="836" height="585" alt="image" src="https://github.com/user-attachments/assets/acebd002-4052-4164-b6c9-9a5060cc7426" />

# GET w/ Pagination

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

<img width="712" height="718" alt="image" src="https://github.com/user-attachments/assets/ea94a443-bafa-4350-99e5-cec6081756d1" />

# GET Advanced
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

<img width="962" height="701" alt="image" src="https://github.com/user-attachments/assets/07803365-b090-4d98-bd75-e83ff476ae89" />



