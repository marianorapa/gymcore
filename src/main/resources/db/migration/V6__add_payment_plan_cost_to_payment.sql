ALTER TABLE payments ADD COLUMN payment_plan_cost_id UUID;

ALTER TABLE payments ADD CONSTRAINT fk_payment_plan_cost FOREIGN KEY (payment_plan_cost_id) REFERENCES payment_plan_cost(id);
