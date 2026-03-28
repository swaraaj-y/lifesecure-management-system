CREATE TABLE customers (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    address TEXT NOT NULL,
    dob DATE NOT NULL,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE agents (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    mobile VARCHAR(20) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE policies (
    policy_no SERIAL PRIMARY KEY,
    customer_id INT NOT NULL REFERENCES customers(id),
    policy_type VARCHAR(255) NOT NULL,
    details TEXT,
    status VARCHAR(50) DEFAULT 'Under Review',
    agent_id INT REFERENCES agents(id)
);
