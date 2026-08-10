CREATE TABLE email_verification_tokens(
    id BINARY(16) NOT NULL,
    token_hash BINARY(32) NOT NULL,
    expires_at DATETIME(6) NOT NULL,
    user_id BINARY(16) NOT NULL,

    CONSTRAINT pk_email_verification_tokens_id PRIMARY KEY (id),
    CONSTRAINT uq_email_verification_tokens_token UNIQUE (token_hash),
    CONSTRAINT uq_email_verification_tokens_user_id UNIQUE (user_id),
    CONSTRAINT fk_email_verification_tokens_user_id FOREIGN KEY (user_id) REFERENCES users(id)
);