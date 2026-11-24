CREATE INDEX idx_loans_user_return ON loans (user_id, return_date);
CREATE INDEX idx_loans_return_date ON loans (return_date);