ALTER TABLE promotions ADD COLUMN max_usage_count INT;

ALTER TABLE user_promotions ADD COLUMN usage_exhausted BOOLEAN;
