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


}```

```
<img width="842" height="556" alt="image" src="https://github.com/user-attachments/assets/6e8af3bd-5e20-4ea6-8fe3-0114c1487175" />
