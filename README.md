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
