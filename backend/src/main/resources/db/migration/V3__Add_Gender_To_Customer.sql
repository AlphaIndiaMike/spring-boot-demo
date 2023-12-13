BEGIN;

-- Add the column without NOT NULL constraint
ALTER TABLE customer ADD COLUMN gender VARCHAR(100);

-- Update the column with a default value
UPDATE customer SET gender = 'ndef'; -- Replace 'default_value' with a suitable placeholder

-- Apply the NOT NULL constraint
ALTER TABLE customer ALTER COLUMN gender SET NOT NULL;

COMMIT;