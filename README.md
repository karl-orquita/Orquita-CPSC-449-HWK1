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


