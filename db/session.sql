
CREATE TABLE users
(
	email varchar NOT NULL,
	password varchar NOT NULL,
	name varchar NOT NULL,	
	
	CONSTRAINT users_pkey PRIMARY KEY (email) 
)