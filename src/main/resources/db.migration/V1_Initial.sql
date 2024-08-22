CREATE TABLE transactions (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              account_from BIGINT NOT NULL,
                              account_to BIGINT NOT NULL,
                              currency_shortname VARCHAR(3) NOT NULL,
                              sum DECIMAL(19, 4) NOT NULL,
                              expense_category VARCHAR(255),
                              datetime TIMESTAMP,
                              limit_sum DECIMAL(19, 4) NOT NULL,
                              limit_datetime TIMESTAMP,
                              limit_currency_shortname VARCHAR(3),
                              limit_exceeded BOOLEAN
);

CREATE TABLE exchange_rates (
                                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                currency VARCHAR(3) NOT NULL,
                                rate DECIMAL(19, 6) NOT NULL,
                                timestamp TIMESTAMP
);

CREATE TABLE monthly_limit (
                               id BIGINT AUTO_INCREMENT PRIMARY KEY,
                               limit_amount DECIMAL(19, 4) NOT NULL,
                               limit_set_date TIMESTAMP NOT NULL,
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
