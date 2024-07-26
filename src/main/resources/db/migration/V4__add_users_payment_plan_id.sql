ALTER TABLE users ADD COLUMN payment_plan_id UUID;
ALTER TABLE users ADD CONSTRAINT fk_payment_plan FOREIGN KEY (payment_plan_id) REFERENCES payment_plans(id);
