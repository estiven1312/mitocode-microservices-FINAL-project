CREATE TYPE contract_type AS ENUM ('CONTRACT', 'ADDENDUM');
CREATE TYPE contract_service_type AS ENUM ('SERVICE', 'GOOD', 'PROPERTY', 'OTHER');
CREATE TYPE contract_status AS ENUM (
  'IN_DRAFT',
  'IN_REVIEW',
  'PENDING_APPROVAL_BY_MANAGER',
  'APPROVED_BY_MANAGER',
  'SENT_TO_EXTERNAL',
  'PENDING_VALIDATION_BY_EXTERNAL',
  'APPROVED_BY_EXTERNAL',
  'SENT_TO_LEGAL',
  'PENDING_VALIDATION_BY_LEGAL',
  'APPROVED_BY_LEGAL',
  'ACTIVE',
  'FINISHED'
);

CREATE TYPE deliverable_status AS ENUM (
  'PENDING',
  'IN_PROGRESS',
  'DELIVERED',
  'ACCEPTED',
  'REJECTED',
  'OVERDUE'
);

CREATE TYPE milestone_status AS ENUM ('PENDING', 'COMPLETED', 'OVERDUE');
CREATE TYPE obligation_status AS ENUM ('PENDING', 'FULFILLED', 'BREACHED');
CREATE TYPE payment_currency AS ENUM ('USD', 'EUR', 'PEN');
CREATE TYPE payment_status AS ENUM ('PENDING', 'PAID', 'OVERDUE');

CREATE TABLE contracts (
  contract_id UUID PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  description TEXT,
  type contract_type NOT NULL,
  service_type contract_service_type NOT NULL,
  related_contract_id UUID,
  start_date DATE NOT NULL,
  end_date DATE NOT NULL,
  third_party_id UUID NOT NULL,
  hash_signature VARCHAR(255),
  url_file VARCHAR(255),
  url_signed_file VARCHAR(255),
  created_by UUID NOT NULL,
  created_at DATE NOT NULL,
  requested_area UUID NOT NULL,
  requested_company UUID NOT NULL,
  amount NUMERIC(19,4) NOT NULL,
  status contract_status NOT NULL
);

CREATE TABLE contract_approvals (
  id BIGSERIAL PRIMARY KEY,
  contract_id UUID NOT NULL,
  approver_id UUID,
  approver_email VARCHAR(255),
  approver_name VARCHAR(255),
  comment TEXT,
  approval_date TIMESTAMP,
  status VARCHAR(100) NOT NULL,
  CONSTRAINT fk_contract_approvals_contract
    FOREIGN KEY (contract_id) REFERENCES contracts(contract_id) ON DELETE CASCADE
);

CREATE TABLE deliverables (
  id BIGSERIAL PRIMARY KEY,
  contract_id UUID NOT NULL,
  name VARCHAR(255) NOT NULL,
  description TEXT,
  schedule_date DATE,
  delivered_at TIMESTAMP,
  accepted_at TIMESTAMP,
  status deliverable_status NOT NULL,
  CONSTRAINT fk_deliverables_contract
    FOREIGN KEY (contract_id) REFERENCES contracts(contract_id) ON DELETE CASCADE
);

CREATE TABLE milestones (
  id BIGSERIAL PRIMARY KEY,
  contract_id UUID NOT NULL,
  name VARCHAR(255) NOT NULL,
  description TEXT,
  due_date DATE,
  completed_at TIMESTAMP,
  status milestone_status NOT NULL,
  CONSTRAINT fk_milestones_contract
    FOREIGN KEY (contract_id) REFERENCES contracts(contract_id) ON DELETE CASCADE
);

CREATE TABLE obligations (
  id BIGSERIAL PRIMARY KEY,
  contract_id UUID NOT NULL,
  description TEXT,
  due_date DATE,
  status obligation_status NOT NULL,
  CONSTRAINT fk_obligations_contract
    FOREIGN KEY (contract_id) REFERENCES contracts(contract_id) ON DELETE CASCADE
);

CREATE TABLE payments (
  id BIGSERIAL PRIMARY KEY,
  contract_id UUID NOT NULL,
  amount NUMERIC(19,4) NOT NULL,
  code_payment VARCHAR(255),
  currency payment_currency NOT NULL,
  due_date DATE,
  paid_at DATE,
  status payment_status NOT NULL,
  CONSTRAINT fk_payments_contract
    FOREIGN KEY (contract_id) REFERENCES contracts(contract_id) ON DELETE CASCADE
);

CREATE TABLE deliverables_by_milestone (
  id BIGSERIAL PRIMARY KEY,
  contract_id UUID NOT NULL,
  milestone_id BIGINT NOT NULL,
  deliverable_id BIGINT NOT NULL,
  CONSTRAINT fk_dbm_contract
    FOREIGN KEY (contract_id) REFERENCES contracts(contract_id) ON DELETE CASCADE
);

CREATE INDEX idx_contract_approvals_contract_id ON contract_approvals(contract_id);
CREATE INDEX idx_deliverables_contract_id ON deliverables(contract_id);
CREATE INDEX idx_milestones_contract_id ON milestones(contract_id);
CREATE INDEX idx_obligations_contract_id ON obligations(contract_id);
CREATE INDEX idx_payments_contract_id ON payments(contract_id);
CREATE INDEX idx_dbm_contract_id ON deliverables_by_milestone(contract_id);
CREATE INDEX idx_dbm_milestone_id ON deliverables_by_milestone(milestone_id);
CREATE INDEX idx_dbm_deliverable_id ON deliverables_by_milestone(deliverable_id);


