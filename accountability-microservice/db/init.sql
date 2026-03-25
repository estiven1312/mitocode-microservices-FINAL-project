-- ============================================================
-- ENUMS
-- ============================================================
CREATE TYPE status AS ENUM ('PENDING','APPROVED','REJECTED','NOT_PROCESSED');

-- ============================================================
-- TABLES
-- ============================================================
CREATE TABLE IF NOT EXISTS currency (
    id        UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    iso_code  VARCHAR(10) NOT NULL,
    iso3_code VARCHAR(10) NOT NULL,
    active    BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS payment (
    id           UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    code_payment VARCHAR(100) NOT NULL UNIQUE,
    description  TEXT NOT NULL,
    amount       NUMERIC(19, 4) NOT NULL,
    currency_id  UUID NOT NULL REFERENCES currency(id),
    timestamp    TIMESTAMP,
    status       status NOT NULL DEFAULT 'NOT_PROCESSED'
);

-- ============================================================
-- SEED DATA
-- ============================================================
INSERT INTO currency (id, iso_code, iso3_code, active) VALUES
    ('a1b2c3d4-0001-0001-0001-000000000001', 'S/', 'PEN', TRUE),
    ('a1b2c3d4-0002-0002-0002-000000000002', '$', 'USD', TRUE),
    ('a1b2c3d4-0003-0003-0003-000000000003', '€', 'EUR', TRUE)
ON CONFLICT (id) DO NOTHING;

INSERT INTO payment (id, code_payment, description, amount, currency_id, status) VALUES
    ('b1b2c3d4-0001-0001-0001-000000000001', 'PAY-001', 'Pago pendiente de validación', 150.00, 'a1b2c3d4-0001-0001-0001-000000000001', 'PENDING'),
    ('b1b2c3d4-0002-0002-0002-000000000002', 'PAY-002', 'Pago aprobado automáticamente', 200.50, 'a1b2c3d4-0002-0002-0002-000000000002', 'APPROVED'),
    ('b1b2c3d4-0003-0003-0003-000000000003', 'PAY-003', 'Pago rechazado por validación', 75.00, 'a1b2c3d4-0003-0003-0003-000000000003', 'REJECTED'),
    ('b1b2c3d4-0004-0004-0004-000000000004', 'PAY-004', 'Pago todavía no procesado', 320.00, 'a1b2c3d4-0001-0001-0001-000000000001', 'NOT_PROCESSED')
ON CONFLICT (id) DO NOTHING;
