delete from Book;
delete from Patron;
delete from Borrowing_Record;


insert into Book (title, author, publication_year, ISBN) values
('To Kill a Mockingbird', 'Harper Lee', '1960-07-11 00:00:00', '9780060935467'),
('1984', 'George Orwell', '1949-06-08 00:00:00', '9780451524935'),
('Moby Dick', 'Herman Melville', '1851-10-18 00:00:00', '9781503280786'),
('The Great Gatsby', 'F. Scott Fitzgerald', '1925-04-10 00:00:00', '9780743273565'),
('Pride and Prejudice', 'Jane Austen', '1813-01-28 00:00:00', '9781503290563'),
('The Catcher in the Rye', 'J.D. Salinger', '1951-07-16 00:00:00', '9780316769488'),
('Brave New World', 'Aldous Huxley', '1932-08-30 00:00:00', '9780060850524');

insert into Patron (name, contact_information) values
('John Doe', 'johndoe@example.com'),
('Jane Smith', 'janesmith@example.com'),
('Alice Johnson', 'alicejohnson@example.com'),
('Bob Brown', 'bobbrown@example.com'),
('Charlie Davis', 'charliedavis@example.com'),
('Daisy Evans', 'daisyevans@example.com'),
('Eve White', 'evewhite@example.com');

-- insert into Borrowing_Record (borrow_date, return_date) values
-- ('2024-08-01 10:00:00', '2024-08-15 10:00:00'),
-- ('2024-08-05 12:00:00', '2024-08-20 12:00:00'),
-- ('2024-08-10 09:00:00', '2024-08-25 09:00:00'),
-- ('2024-08-12 14:00:00', '2024-08-27 14:00:00'),
-- ('2024-08-15 11:00:00', '2024-08-30 11:00:00'),
-- ('2024-08-18 08:00:00', '2024-09-02 08:00:00'),
-- ('2024-08-20 13:00:00', '2024-09-05 13:00:00');
