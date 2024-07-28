CREATE TABLE IF NOT EXISTS payment_plan_cost (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    payment_plan_id UUID,
    amount DOUBLE PRECISION,
    valid_from TIMESTAMP NOT NULL,
    valid_until TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (payment_plan_id) REFERENCES payment_plans(id)
);

ALTER TABLE payments ADD COLUMN status VARCHAR(255);
ALTER TABLE payments ADD COLUMN due_date TIMESTAMP;

ALTER TABLE payment_plans DROP COLUMN percent_discount;