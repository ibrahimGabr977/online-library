create table if not exists Book(
    id identity,
    title varchar(50) not null,
    author varchar(50) not null,
    publication_year timestamp not null,
    ISBN varchar(13) not null
);

create table if not exists Patron(
    id identity,
    name varchar(50) not null,
    contact_information varchar(255) not null
);


create table if not exists Borrowing_Record(
    book_id bigint not null,
    patron_id bigint not null,
    borrow_date timestamp default CURRENT_TIMESTAMP,
    return_date timestamp NULL
);



alter table Borrowing_Record 
add foreign key (book_id) references Book(id);

alter table Borrowing_Record 
add foreign key (patron_id) references Patron(id);