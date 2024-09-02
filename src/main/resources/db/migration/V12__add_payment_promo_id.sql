ALTER TABLE payments 
ADD COLUMN user_promo_id UUID;

ALTER TABLE payments
ADD CONSTRAINT fk_promotion_id
FOREIGN KEY (user_promo_id) REFERENCES user_promotions(id);