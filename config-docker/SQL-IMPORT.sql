
create schema spring_shop;

set search_path = "spring_shop";

-- user-service

DROP TABLE IF EXISTS Users CASCADE;
DROP TABLE IF EXISTS Roles CASCADE;
DROP TABLE IF EXISTS Users_roles CASCADE;
DROP TABLE IF EXISTS Users_details CASCADE;
DROP TABLE IF EXISTS Users_billings CASCADE;

CREATE TABLE IF NOT EXISTS Users (
	id 				bigserial primary key,
	username 		varchar(255) not null unique,
	password 		varchar(255) not null
);

CREATE TABLE IF NOT EXISTS Roles (
	id 		bigserial PRIMARY KEY,
	name 	varchar(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS Users_Roles (
	user_id 	bigint NOT NULL,
	role_id 	int NOT NULL,

	PRIMARY KEY (user_id, role_id),
	CONSTRAINT fk_users FOREIGN KEY (user_id) REFERENCES Users(id) ON DELETE CASCADE,
	CONSTRAINT fk_roles FOREIGN KEY (role_id) REFERENCES Roles(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Users_Details (
    id bigserial PRIMARY KEY,
    user_id bigint NOT NULL, --NEW not null
    first_name varchar(255),
    last_name varchar(255),
    phone_number varchar(30),
    email varchar(255) NOT NULL,

    CONSTRAINT fk_users FOREIGN KEY (user_id) REFERENCES Users(id) ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS Users_Billings (
    id bigserial PRIMARY KEY,
    user_id bigint NOT NULL,
    card_number varchar(20) NOT NULL UNIQUE,
    holder_name varchar(100) NOT NULL,
    expiration_date varchar(10) NOT NULL,
    cvv_code varchar(3) NOT NULL,

    CONSTRAINT fk_users FOREIGN KEY (user_id) REFERENCES Users(id) ON DELETE CASCADE
);

insert into Users (username, password)
values 	('log1', '$2a$12$D4yxD6.nsKzupQOsRMzQn.9TBe69a10mqSkOVI2yf3db5NF9jLjFy'),
		('log2', '$2a$12$D4yxD6.nsKzupQOsRMzQn.9TBe69a10mqSkOVI2yf3db5NF9jLjFy'),
		('log3', '$2a$12$D4yxD6.nsKzupQOsRMzQn.9TBe69a10mqSkOVI2yf3db5NF9jLjFy');

insert into Roles (name)
values 	('ROLE_USER'),
		('ROLE_ADMIN');

insert into users_roles (user_id, role_id)
VALUES 	(1, 2),
		(2, 1),
		(3, 1);

insert into users_details (user_id, first_name, last_name, phone_number, email)
VALUES 	(1, 'first_name_1', 'last_name_1', '8 (900) 000-00-11', 'test@mail.com');

insert into users_billings (user_id, card_number, holder_name, expiration_date, cvv_code)
VALUES 	(1, '1111 2222 3333 4444', 'holder name jr.', '02/2200', '333');


-- product-service

DROP TABLE IF EXISTS Products CASCADE;

CREATE TABLE IF NOT EXISTS Products (
	id bigserial PRIMARY KEY,
	title varchar(255) NOT NULL UNIQUE,
	price numeric(10, 2) NOT null,
	created_at timestamp default current_timestamp
);

insert into Products
		(title, price, created_at)
values
		('Sugar', 90.0, now()),
		('Milk', 70.0, now()),
		('Eggs', 80.0, now()),
		('Bread', 30.0, now()),
		('Sugar11', 40.0, now()),
		('Sugar2', 90.0, now()),
		('Milk2', 70.0, now()),
		('Eggs2', 80.0, now()),
		('Bread2', 30.0, now()),
		('Sugar22', 40.0, now()),
		('Sugar3', 90.0, now()),
		('Milk3', 70.0, now()),
		('Eggs3', 80.0, now()),
		('Bread3', 30.0, now()),
		('Sugar33', 40.0, now()),
		('Sugar4', 90.0, now());

-- order-service

DROP TABLE IF EXISTS Orders CASCADE;
DROP TABLE IF EXISTS Orders_Content CASCADE;

CREATE TABLE IF NOT EXISTS Orders (
	id bigserial PRIMARY KEY,
	username varchar(255) NOT NULL,
	address_id bigint not null,
	billing_id bigint not null,
	order_total numeric(10, 2) not null,
	created_at timestamp default current_timestamp
);

CREATE TABLE IF NOT EXISTS Orders_Content (
	id bigserial PRIMARY KEY,
	order_id bigint NOT NULL,
	product_id bigint NOT NULL,
	price numeric(10, 2) NOT NULL,
	amount integer NOT NULL,
	sum numeric(10, 2) NOT NULL,

	CONSTRAINT fk_orders FOREIGN KEY (order_id) REFERENCES Orders(id) ON DELETE CASCADE
);

-- address-service

DROP TABLE IF EXISTS Countries CASCADE;
DROP TABLE IF EXISTS Regions CASCADE;
DROP TABLE IF EXISTS Cities CASCADE;
DROP TABLE IF EXISTS Streets CASCADE;
DROP TABLE IF EXISTS Addresses CASCADE;

CREATE TABLE IF NOT EXISTS Countries (
	id serial PRIMARY KEY,
	name varchar(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS Regions (
	id bigserial NOT NULL PRIMARY KEY,
	name varchar(255) NOT NULL,
	country_id integer NOT null,

	CONSTRAINT fk_countries FOREIGN KEY (country_id) REFERENCES Countries(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Cities (
	id bigserial PRIMARY KEY,
	name varchar(255) NOT NULL,
	region_id bigint NOT NULL,

	CONSTRAINT fk_regions FOREIGN KEY (region_id) REFERENCES Regions(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Streets (
	id bigserial PRIMARY KEY,
	name varchar(255) NOT NULL,
	city_id bigint NOT NULL,

	CONSTRAINT fk_cities FOREIGN KEY (city_id) REFERENCES Cities(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Addresses (
    id bigserial primary key,
    username varchar(255),
    street_id bigint not null,
    house_number varchar(255) not null,
    apartment_number varchar(255),

    constraint fk_streets FOREIGN KEY (street_id) REFERENCES Streets(id) ON DELETE CASCADE

);

INSERT INTO Countries (name) VALUES ('Russia');
INSERT INTO Regions (name, country_id) VALUES ('Moscow area', 1);
INSERT INTO Cities (name, region_id) VALUES ('Moscow', 1);
INSERT INTO Streets (name, city_id) VALUES ('Generala Razina', 1);
INSERT INTO Streets (name, city_id) VALUES ('Lenina', 1);
INSERT INTO Cities (name, region_id) VALUES ('Podolsk', 1);
INSERT INTO Streets (name, city_id) VALUES ('Hrabrih pionerov', 2);
INSERT INTO Streets (name, city_id) VALUES ('Lenina', 2);

INSERT INTO Addresses
    (username, street_id, house_number, apartment_number)
VALUES
    ('log', 2, '3A', '109'),
    ('log', 4, '5 str. 6', '12'),
    ('log1', 3, 'log 1 address', '777');

