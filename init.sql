CREATE TABLE contracts (
	id serial primary key,
	title text
);

CREATE TABLE payments (
	id serial primary key,
	contract_id int not null references contracts,
	description text,
	"value" int not null,
	"time" timestamp not null,
	is_imported boolean default false,
	created_at timestamp default now(),
	updated_at timestamp,
	is_deleted boolean default false
);

insert into contracts(title) values ('Contract_1');
insert into contracts(title) values ('Contract_2');