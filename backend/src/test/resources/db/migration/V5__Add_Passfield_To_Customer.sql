BEGIN;

-- Add the column without NOT NULL constraint
ALTER TABLE customer ADD COLUMN password VARCHAR(255);

-- Update the column with a default value
UPDATE customer SET password = 'password'; -- Replace 'default_value' with a suitable placeholder

-- Apply the NOT NULL constraint
ALTER TABLE customer ALTER COLUMN password SET NOT NULL;

COMMIT;