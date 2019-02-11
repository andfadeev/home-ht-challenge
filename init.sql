CREATE TABLE contracts (
	id serial PRIMARY KEY,
	title text
);

drop table payments;
CREATE TABLE payments (
	id serial PRIMARY KEY,
	contract_id int not null REFERENCES contracts,
	description text,
	"value" int NOT null,
	"time" date not null,
	is_imported boolean default false,
	created_at timestamp DEFAULT NOW(),
	updated_at timestamp,
	is_deleted boolean DEFAULT false
);

insert into contracts(title) values ('Contract_1');
insert into contracts(title) values ('Contract_2');