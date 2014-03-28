CREATE TABLE users(
    id int(11) primary key auto_increment,
    name varchar(100) not null,
    password varchar(100) not null,
    permission varchar(100) not null
    )