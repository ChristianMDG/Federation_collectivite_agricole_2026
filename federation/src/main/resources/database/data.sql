-- ============================================================
-- NETTOYAGE COMPLET DE LA BASE
-- ============================================================

-- Supprimer toutes les données existantes
TRUNCATE TABLE member_attendance CASCADE;
TRUNCATE TABLE collectivity_transaction CASCADE;
TRUNCATE TABLE member_payment CASCADE;
TRUNCATE TABLE collectivity_financial_account CASCADE;
TRUNCATE TABLE bank_account CASCADE;
TRUNCATE TABLE mobile_banking_account CASCADE;
TRUNCATE TABLE cash_account CASCADE;
TRUNCATE TABLE financial_account CASCADE;
TRUNCATE TABLE membership_fee CASCADE;
TRUNCATE TABLE member_referees CASCADE;
TRUNCATE TABLE collectivity_members CASCADE;
TRUNCATE TABLE activity CASCADE;
TRUNCATE TABLE member CASCADE;
TRUNCATE TABLE collectivity CASCADE;

-- Réinitialiser les séquences
ALTER SEQUENCE member_id_seq RESTART WITH 1000;
ALTER SEQUENCE collectivity_id_seq RESTART WITH 1000;
ALTER SEQUENCE membership_fee_id_seq RESTART WITH 3000;
ALTER SEQUENCE transaction_id_seq RESTART WITH 1000;
ALTER SEQUENCE member_payment_id_seq RESTART WITH 1000;
ALTER SEQUENCE financial_account_id_seq RESTART WITH 1000;
ALTER SEQUENCE activity_id_seq RESTART WITH 1000;
ALTER SEQUENCE attendance_id_seq RESTART WITH 1000;

-- ============================================================
-- TABLEAU 1 : COLLECTIVITÉS
-- ============================================================
INSERT INTO collectivity (id, number, name, location, speciality)
VALUES
    ('col-1', '1', 'Mpanorina', 'Ambatondrazaka', 'Riziculture'),
    ('col-2', '2', 'Dobo voalohany', 'Ambatondrazaka', 'Pisciculture'),
    ('col-3', '3', 'Tantely mamy', 'Brickaville', 'Apiculture');


-- ============================================================
-- TABLEAU 2 : MEMBRES COLLECTIVITÉ 1
-- ============================================================
INSERT INTO member (id, firstname, lastname, birthday, gender, address, profession, phone_number, email, registration_date, occupation, collectivity_id)
VALUES
    ('C1-M1', 'Prénom membre 1', 'Nom membre 1', '1980-02-01', 'MALE', 'Lot II V M Ambato.', 'Riziculteur', 341234567, 'member.1@fed-agri.mg', '2026-01-01', 'PRESIDENT', 'col-1'),
    ('C1-M2', 'Prénom membre 2', 'Nom membre 2', '1982-03-05', 'MALE', 'Lot II F Ambato.', 'Agriculteur', 321234567, 'member.2@fed-agri.mg', '2026-01-01', 'VICE_PRESIDENT', 'col-1'),
    ('C1-M3', 'Prénom membre 3', 'Nom membre 3', '1992-03-10', 'MALE', 'Lot II J Ambato.', 'Collecteur', 331234567, 'member.3@fed-agri.mg', '2026-01-01', 'SECRETARY', 'col-1'),
    ('C1-M4', 'Prénom membre 4', 'Nom membre 4', '1988-05-22', 'FEMALE', 'Lot A K 50 Ambato.', 'Distributeur', 381234567, 'member.4@fed-agri.mg', '2026-01-01', 'TREASURER', 'col-1'),
    ('C1-M5', 'Prénom membre 5', 'Nom membre 5', '1999-08-21', 'MALE', 'Lot UV 80 Ambato.', 'Riziculteur', 373434567, 'member.5@fed-agri.mg', '2026-01-01', 'SENIOR', 'col-1'),
    ('C1-M6', 'Prénom membre 6', 'Nom membre 6', '1998-08-22', 'FEMALE', 'Lot UV 6 Ambato.', 'Riziculteur', 372234567, 'member.6@fed-agri.mg', '2026-01-01', 'SENIOR', 'col-1'),
    ('C1-M7', 'Prénom membre 7', 'Nom membre 7', '1998-01-31', 'MALE', 'Lot UV 7 Ambato.', 'Riziculteur', 374234567, 'member.7@fed-agri.mg', '2026-01-01', 'SENIOR', 'col-1'),
    ('C1-M8', 'Prénom membre 8', 'Nom membre 8', '1975-08-20', 'MALE', 'Lot UV 8 Ambato.', 'Riziculteur', 370234567, 'member.8@fed-agri.mg', '2026-01-01', 'SENIOR', 'col-1');

-- Référents collectivité 1
INSERT INTO member_referees (member_id, referee_id) VALUES
                                                        ('C1-M3', 'C1-M1'), ('C1-M3', 'C1-M2'),
                                                        ('C1-M4', 'C1-M1'), ('C1-M4', 'C1-M2'),
                                                        ('C1-M5', 'C1-M1'), ('C1-M5', 'C1-M2'),
                                                        ('C1-M6', 'C1-M1'), ('C1-M6', 'C1-M2'),
                                                        ('C1-M7', 'C1-M1'), ('C1-M7', 'C1-M2'),
                                                        ('C1-M8', 'C1-M6'), ('C1-M8', 'C1-M7');

-- Association membres collectivité 1
INSERT INTO collectivity_members (collectivity_id, member_id) VALUES
                                                                  ('col-1', 'C1-M1'), ('col-1', 'C1-M2'), ('col-1', 'C1-M3'), ('col-1', 'C1-M4'),
                                                                  ('col-1', 'C1-M5'), ('col-1', 'C1-M6'), ('col-1', 'C1-M7'), ('col-1', 'C1-M8');

-- Mise à jour des postes collectivité 1
UPDATE collectivity SET
                        president_id = 'C1-M1',
                        vice_president_id = 'C1-M2',
                        secretary_id = 'C1-M3',
                        treasurer_id = 'C1-M4'
WHERE id = 'col-1';


-- ============================================================
-- TABLEAU 3 : MEMBRES COLLECTIVITÉ 2 (mêmes membres que C1)
-- ============================================================

-- Association membres collectivité 2
INSERT INTO collectivity_members (collectivity_id, member_id) VALUES
                                                                  ('col-2', 'C1-M1'), ('col-2', 'C1-M2'), ('col-2', 'C1-M3'), ('col-2', 'C1-M4'),
                                                                  ('col-2', 'C1-M5'), ('col-2', 'C1-M6'), ('col-2', 'C1-M7'), ('col-2', 'C1-M8');

-- Mise à jour des postes collectivité 2
UPDATE collectivity SET
                        president_id = 'C1-M5',
                        vice_president_id = 'C1-M6',
                        secretary_id = 'C1-M7',
                        treasurer_id = 'C1-M8'
WHERE id = 'col-2';


-- ============================================================
-- TABLEAU 4 : MEMBRES COLLECTIVITÉ 3
-- ============================================================
INSERT INTO member (id, firstname, lastname, birthday, gender, address, profession, phone_number, email, registration_date, occupation, collectivity_id)
VALUES
    ('C3-M1', 'Prénom membre 9', 'Nom membre 9', '1988-01-02', 'MALE', 'Lot 33 J Antsirabe', 'Apiculteur', 34034567, 'member.9@fed-agri.mg', '2026-01-01', 'PRESIDENT', 'col-3'),
    ('C3-M2', 'Prénom membre 10', 'Nom membre 10', '1982-03-05', 'MALE', 'Lot 2 J Antsirabe', 'Agriculteur', 338634567, 'member.10@fed-agri.mg', '2026-01-01', 'VICE_PRESIDENT', 'col-3'),
    ('C3-M3', 'Prénom membre 11', 'Nom membre 11', '1992-03-12', 'MALE', 'Lot 8 KM Antsirabe', 'Collecteur', 338234567, 'member.11@fed-agri.mg', '2026-01-01', 'SECRETARY', 'col-3'),
    ('C3-M4', 'Prénom membre 12', 'Nom membre 12', '1988-05-10', 'FEMALE', 'Lot A K 50 Antsirabe', 'Distributeur', 382334567, 'member.12@fed-agri.mg', '2026-01-01', 'TREASURER', 'col-3'),
    ('C3-M5', 'Prénom membre 13', 'Nom membre 13', '1999-08-11', 'MALE', 'Lot UV 80 Antsirabe.', 'Apiculteur', 373365567, 'member.13@fed-agri.mg', '2026-01-01', 'SENIOR', 'col-3'),
    ('C3-M6', 'Prénom membre 14', 'Nom membre 14', '1998-08-09', 'FEMALE', 'Lot UV 6 Antsirabe.', 'Apiculteur', 378234567, 'member.14@fed-agri.mg', '2026-01-01', 'SENIOR', 'col-3'),
    ('C3-M7', 'Prénom membre 15', 'Nom membre 15', '1998-01-13', 'MALE', 'Lot UV 7 Antsirabe', 'Apiculteur', 374914567, 'member.15@fed-agri.mg', '2026-01-01', 'SENIOR', 'col-3'),
    ('C3-M8', 'Prénom membre 16', 'Nom membre 16', '1975-08-02', 'MALE', 'Lot UV 8 Antsirabe', 'Apiculteur', 370634567, 'member.16@fed-agri.mg', '2026-01-01', 'SENIOR', 'col-3');

-- Référents collectivité 3
INSERT INTO member_referees (member_id, referee_id) VALUES
                                                        ('C3-M3', 'C3-M1'), ('C3-M3', 'C3-M2'),
                                                        ('C3-M4', 'C3-M1'), ('C3-M4', 'C3-M2'),
                                                        ('C3-M5', 'C3-M1'), ('C3-M5', 'C3-M2'),
                                                        ('C3-M6', 'C3-M1'), ('C3-M6', 'C3-M2'),
                                                        ('C3-M7', 'C3-M1'), ('C3-M7', 'C3-M2'),
                                                        ('C3-M8', 'C3-M1'), ('C3-M8', 'C3-M2');

-- Association membres collectivité 3
INSERT INTO collectivity_members (collectivity_id, member_id) VALUES
                                                                  ('col-3', 'C3-M1'), ('col-3', 'C3-M2'), ('col-3', 'C3-M3'), ('col-3', 'C3-M4'),
                                                                  ('col-3', 'C3-M5'), ('col-3', 'C3-M6'), ('col-3', 'C3-M7'), ('col-3', 'C3-M8');

-- Mise à jour des postes collectivité 3
UPDATE collectivity SET
                        president_id = 'C3-M1',
                        vice_president_id = 'C3-M2',
                        secretary_id = 'C3-M3',
                        treasurer_id = 'C3-M4'
WHERE id = 'col-3';


-- ============================================================
-- COMPTES FINANCIERS DES COLLECTIVITÉS
-- ============================================================

-- Collectivité 1 - Comptes
INSERT INTO financial_account (id, amount) VALUES ('C1-A-CASH', 0), ('C1-A-MOBILE-1', 0);
INSERT INTO cash_account (id) VALUES ('C1-A-CASH');
INSERT INTO mobile_banking_account (id, holder_name, mobile_banking_service, mobile_number)
VALUES ('C1-A-MOBILE-1', 'Mpanorina', 'ORANGE_MONEY', '0370489612');
INSERT INTO collectivity_financial_account (collectivity_id, financial_account_id) VALUES
                                                                                       ('col-1', 'C1-A-CASH'), ('col-1', 'C1-A-MOBILE-1');

-- Collectivité 2 - Comptes
INSERT INTO financial_account (id, amount) VALUES ('C2-A-CASH', 0), ('C2-A-MOBILE-1', 0);
INSERT INTO cash_account (id) VALUES ('C2-A-CASH');
INSERT INTO mobile_banking_account (id, holder_name, mobile_banking_service, mobile_number)
VALUES ('C2-A-MOBILE-1', 'Dobo voalohany', 'ORANGE_MONEY', '0320489612');
INSERT INTO collectivity_financial_account (collectivity_id, financial_account_id) VALUES
                                                                                       ('col-2', 'C2-A-CASH'), ('col-2', 'C2-A-MOBILE-1');

-- Collectivité 3 - Comptes (caisse uniquement)
INSERT INTO financial_account (id, amount) VALUES ('C3-A-CASH', 0);
INSERT INTO cash_account (id) VALUES ('C3-A-CASH');
INSERT INTO collectivity_financial_account (collectivity_id, financial_account_id) VALUES
    ('col-3', 'C3-A-CASH');


-- ============================================================
-- NOUVEAUX COMPTES FINANCIERS POUR LA COLLECTIVITÉ 3
-- ============================================================

-- Comptes bancaires
INSERT INTO financial_account (id, amount) VALUES ('C3-A-BANK-1', 0), ('C3-A-BANK-2', 0);

INSERT INTO bank_account (id, holder_name, bank_name, bank_code, bank_branch_code, bank_account_number, bank_account_key)
VALUES
    ('C3-A-BANK-1', 'Koto', 'BMOI', '00004', '00001', '12345678900', '12'),
    ('C3-A-BANK-2', 'Naivo', 'BRED', '00008', '00003', '45678901230', '58');

-- Compte mobile money MVOLA
INSERT INTO financial_account (id, amount) VALUES ('C3-A-MOBILE-1', 0);
INSERT INTO mobile_banking_account (id, holder_name, mobile_banking_service, mobile_number)
VALUES ('C3-A-MOBILE-1', 'Kolo', 'MVOLA', '0341889612');

-- Association des nouveaux comptes avec la collectivité 3
INSERT INTO collectivity_financial_account (collectivity_id, financial_account_id) VALUES
                                                                                       ('col-3', 'C3-A-BANK-1'),
                                                                                       ('col-3', 'C3-A-BANK-2'),
                                                                                       ('col-3', 'C3-A-MOBILE-1');


-- ============================================================
-- COTISATIONS (MEMBERSHIP FEES)
-- ============================================================

-- Collectivité 1
INSERT INTO membership_fee (id, collectivity_id, label, status, frequency, eligible_from, amount) VALUES
                                                                                                      ('cot-1', 'col-1', 'Cotisation annuelle', 'ACTIVE', 'ANNUALLY', '2026-01-01', 200000.00),
                                                                                                      ('cot-2', 'col-1', 'Famangiana', 'ACTIVE', 'PUNCTUALLY', '2026-04-30', 20000.00);

-- Collectivité 2
INSERT INTO membership_fee (id, collectivity_id, label, status, frequency, eligible_from, amount) VALUES
                                                                                                      ('cot-3', 'col-2', 'Cotisation annuelle', 'ACTIVE', 'ANNUALLY', '2026-01-01', 200000.00),
                                                                                                      ('cot-4', 'col-2', 'Cotisation 2025', 'INACTIVE', 'ANNUALLY', '2025-01-01', 100000.00);

-- Collectivité 3
INSERT INTO membership_fee (id, collectivity_id, label, status, frequency, eligible_from, amount) VALUES
    ('cot-5', 'col-3', 'Cotisation mensuelle', 'ACTIVE', 'MONTHLY', '2026-04-01', 25000.00);


-- ============================================================
-- PAIEMENTS ET TRANSACTIONS COLLECTIVITÉ 1
-- ============================================================

INSERT INTO member_payment (id, member_id, membership_fee_id, account_credited_id, amount, payment_mode, creation_date) VALUES
                                                                                                                            ('mp-c1-1', 'C1-M1', 'cot-1', 'C1-A-CASH', 200000.00, 'CASH', '2026-01-01'),
                                                                                                                            ('mp-c1-2', 'C1-M2', 'cot-1', 'C1-A-CASH', 200000.00, 'CASH', '2026-01-01'),
                                                                                                                            ('mp-c1-3', 'C1-M3', 'cot-1', 'C1-A-MOBILE-1', 200000.00, 'MOBILE_BANKING', '2026-01-01'),
                                                                                                                            ('mp-c1-4', 'C1-M4', 'cot-1', 'C1-A-MOBILE-1', 200000.00, 'MOBILE_BANKING', '2026-01-01'),
                                                                                                                            ('mp-c1-5', 'C1-M5', 'cot-1', 'C1-A-MOBILE-1', 150000.00, 'MOBILE_BANKING', '2026-01-01'),
                                                                                                                            ('mp-c1-6', 'C1-M6', 'cot-1', 'C1-A-CASH', 100000.00, 'CASH', '2026-05-01'),
                                                                                                                            ('mp-c1-7', 'C1-M7', 'cot-1', 'C1-A-CASH', 60000.00, 'CASH', '2026-05-01'),
                                                                                                                            ('mp-c1-8', 'C1-M8', 'cot-1', 'C1-A-CASH', 90000.00, 'CASH', '2026-05-01');

INSERT INTO collectivity_transaction (id, collectivity_id, member_id, membership_fee_id, account_credited_id, amount, payment_mode, creation_date) VALUES
                                                                                                                                                       ('tr-c1-1', 'col-1', 'C1-M1', 'cot-1', 'C1-A-CASH', 200000.00, 'CASH', '2026-01-01'),
                                                                                                                                                       ('tr-c1-2', 'col-1', 'C1-M2', 'cot-1', 'C1-A-CASH', 200000.00, 'CASH', '2026-01-01'),
                                                                                                                                                       ('tr-c1-3', 'col-1', 'C1-M3', 'cot-1', 'C1-A-MOBILE-1', 200000.00, 'MOBILE_BANKING', '2026-01-01'),
                                                                                                                                                       ('tr-c1-4', 'col-1', 'C1-M4', 'cot-1', 'C1-A-MOBILE-1', 200000.00, 'MOBILE_BANKING', '2026-01-01'),
                                                                                                                                                       ('tr-c1-5', 'col-1', 'C1-M5', 'cot-1', 'C1-A-MOBILE-1', 150000.00, 'MOBILE_BANKING', '2026-01-01'),
                                                                                                                                                       ('tr-c1-6', 'col-1', 'C1-M6', 'cot-1', 'C1-A-CASH', 100000.00, 'CASH', '2026-05-01'),
                                                                                                                                                       ('tr-c1-7', 'col-1', 'C1-M7', 'cot-1', 'C1-A-CASH', 60000.00, 'CASH', '2026-05-01'),
                                                                                                                                                       ('tr-c1-8', 'col-1', 'C1-M8', 'cot-1', 'C1-A-CASH', 90000.00, 'CASH', '2026-05-01');

-- Mise à jour des soldes collectivité 1
UPDATE financial_account SET amount = 650000.00, updated_at = CURRENT_TIMESTAMP WHERE id = 'C1-A-CASH';
UPDATE financial_account SET amount = 550000.00, updated_at = CURRENT_TIMESTAMP WHERE id = 'C1-A-MOBILE-1';


-- ============================================================
-- PAIEMENTS ET TRANSACTIONS COLLECTIVITÉ 2
-- ============================================================

INSERT INTO member_payment (id, member_id, membership_fee_id, account_credited_id, amount, payment_mode, creation_date) VALUES
                                                                                                                            ('mp-c2-1', 'C1-M1', 'cot-3', 'C2-A-CASH', 120000.00, 'CASH', '2026-01-01'),
                                                                                                                            ('mp-c2-2', 'C1-M2', 'cot-3', 'C2-A-CASH', 180000.00, 'CASH', '2026-01-01'),
                                                                                                                            ('mp-c2-3', 'C1-M3', 'cot-3', 'C2-A-CASH', 200000.00, 'CASH', '2026-01-01'),
                                                                                                                            ('mp-c2-4', 'C1-M4', 'cot-3', 'C2-A-CASH', 200000.00, 'CASH', '2026-01-01'),
                                                                                                                            ('mp-c2-5', 'C1-M5', 'cot-3', 'C2-A-CASH', 200000.00, 'CASH', '2026-01-01'),
                                                                                                                            ('mp-c2-6', 'C1-M6', 'cot-3', 'C2-A-CASH', 200000.00, 'CASH', '2026-01-01'),
                                                                                                                            ('mp-c2-7', 'C1-M7', 'cot-3', 'C2-A-MOBILE-1', 80000.00, 'MOBILE_BANKING', '2026-01-01'),
                                                                                                                            ('mp-c2-8', 'C1-M8', 'cot-3', 'C2-A-MOBILE-1', 120000.00, 'MOBILE_BANKING', '2026-01-01');

INSERT INTO collectivity_transaction (id, collectivity_id, member_id, membership_fee_id, account_credited_id, amount, payment_mode, creation_date) VALUES
                                                                                                                                                       ('tr-c2-1', 'col-2', 'C1-M1', 'cot-3', 'C2-A-CASH', 120000.00, 'CASH', '2026-01-01'),
                                                                                                                                                       ('tr-c2-2', 'col-2', 'C1-M2', 'cot-3', 'C2-A-CASH', 180000.00, 'CASH', '2026-01-01'),
                                                                                                                                                       ('tr-c2-3', 'col-2', 'C1-M3', 'cot-3', 'C2-A-CASH', 200000.00, 'CASH', '2026-01-01'),
                                                                                                                                                       ('tr-c2-4', 'col-2', 'C1-M4', 'cot-3', 'C2-A-CASH', 200000.00, 'CASH', '2026-01-01'),
                                                                                                                                                       ('tr-c2-5', 'col-2', 'C1-M5', 'cot-3', 'C2-A-CASH', 200000.00, 'CASH', '2026-01-01'),
                                                                                                                                                       ('tr-c2-6', 'col-2', 'C1-M6', 'cot-3', 'C2-A-CASH', 200000.00, 'CASH', '2026-01-01'),
                                                                                                                                                       ('tr-c2-7', 'col-2', 'C1-M7', 'cot-3', 'C2-A-MOBILE-1', 80000.00, 'MOBILE_BANKING', '2026-01-01'),
                                                                                                                                                       ('tr-c2-8', 'col-2', 'C1-M8', 'cot-3', 'C2-A-MOBILE-1', 120000.00, 'MOBILE_BANKING', '2026-01-01');

-- Mise à jour des soldes collectivité 2
UPDATE financial_account SET amount = 1100000.00, updated_at = CURRENT_TIMESTAMP WHERE id = 'C2-A-CASH';
UPDATE financial_account SET amount = 200000.00, updated_at = CURRENT_TIMESTAMP WHERE id = 'C2-A-MOBILE-1';


-- ============================================================
-- PAIEMENTS ET TRANSACTIONS COLLECTIVITÉ 3 (AVRIL 2026)
-- ============================================================

INSERT INTO member_payment (id, member_id, membership_fee_id, account_credited_id, amount, payment_mode, creation_date) VALUES
                                                                                                                            ('mp-c3-apr-1', 'C3-M1', 'cot-5', 'C3-A-BANK-1', 25000.00, 'BANK_TRANSFER', '2026-04-01'),
                                                                                                                            ('mp-c3-apr-2', 'C3-M2', 'cot-5', 'C3-A-BANK-1', 25000.00, 'BANK_TRANSFER', '2026-04-01'),
                                                                                                                            ('mp-c3-apr-3', 'C3-M3', 'cot-5', 'C3-A-BANK-1', 25000.00, 'BANK_TRANSFER', '2026-04-01'),
                                                                                                                            ('mp-c3-apr-4', 'C3-M4', 'cot-5', 'C3-A-BANK-1', 25000.00, 'BANK_TRANSFER', '2026-04-01'),
                                                                                                                            ('mp-c3-apr-5', 'C3-M5', 'cot-5', 'C3-A-BANK-2', 25000.00, 'BANK_TRANSFER', '2026-04-01'),
                                                                                                                            ('mp-c3-apr-6', 'C3-M6', 'cot-5', 'C3-A-BANK-2', 25000.00, 'BANK_TRANSFER', '2026-04-01'),
                                                                                                                            ('mp-c3-apr-7', 'C3-M7', 'cot-5', 'C3-A-CASH', 25000.00, 'CASH', '2026-04-01'),
                                                                                                                            ('mp-c3-apr-8', 'C3-M8', 'cot-5', 'C3-A-CASH', 25000.00, 'CASH', '2026-04-01');

INSERT INTO collectivity_transaction (id, collectivity_id, member_id, membership_fee_id, account_credited_id, amount, payment_mode, creation_date) VALUES
                                                                                                                                                       ('tr-c3-apr-1', 'col-3', 'C3-M1', 'cot-5', 'C3-A-BANK-1', 25000.00, 'BANK_TRANSFER', '2026-04-01'),
                                                                                                                                                       ('tr-c3-apr-2', 'col-3', 'C3-M2', 'cot-5', 'C3-A-BANK-1', 25000.00, 'BANK_TRANSFER', '2026-04-01'),
                                                                                                                                                       ('tr-c3-apr-3', 'col-3', 'C3-M3', 'cot-5', 'C3-A-BANK-1', 25000.00, 'BANK_TRANSFER', '2026-04-01'),
                                                                                                                                                       ('tr-c3-apr-4', 'col-3', 'C3-M4', 'cot-5', 'C3-A-BANK-1', 25000.00, 'BANK_TRANSFER', '2026-04-01'),
                                                                                                                                                       ('tr-c3-apr-5', 'col-3', 'C3-M5', 'cot-5', 'C3-A-BANK-2', 25000.00, 'BANK_TRANSFER', '2026-04-01'),
                                                                                                                                                       ('tr-c3-apr-6', 'col-3', 'C3-M6', 'cot-5', 'C3-A-BANK-2', 25000.00, 'BANK_TRANSFER', '2026-04-01'),
                                                                                                                                                       ('tr-c3-apr-7', 'col-3', 'C3-M7', 'cot-5', 'C3-A-CASH', 25000.00, 'CASH', '2026-04-01'),
                                                                                                                                                       ('tr-c3-apr-8', 'col-3', 'C3-M8', 'cot-5', 'C3-A-CASH', 25000.00, 'CASH', '2026-04-01');


-- ============================================================
-- PAIEMENTS ET TRANSACTIONS COLLECTIVITÉ 3 (MAI 2026)
-- ============================================================

INSERT INTO member_payment (id, member_id, membership_fee_id, account_credited_id, amount, payment_mode, creation_date) VALUES
                                                                                                                            ('mp-c3-may-1', 'C3-M1', 'cot-5', 'C3-A-BANK-1', 25000.00, 'BANK_TRANSFER', '2026-05-01'),
                                                                                                                            ('mp-c3-may-2', 'C3-M2', 'cot-5', 'C3-A-BANK-1', 25000.00, 'BANK_TRANSFER', '2026-05-01'),
                                                                                                                            ('mp-c3-may-3', 'C3-M3', 'cot-5', 'C3-A-MOBILE-1', 15000.00, 'MOBILE_BANKING', '2026-05-01'),
                                                                                                                            ('mp-c3-may-4', 'C3-M4', 'cot-5', 'C3-A-MOBILE-1', 15000.00, 'MOBILE_BANKING', '2026-05-01'),
                                                                                                                            ('mp-c3-may-5', 'C3-M5', 'cot-5', 'C3-A-BANK-2', 20000.00, 'BANK_TRANSFER', '2026-05-01'),
                                                                                                                            ('mp-c3-may-6', 'C3-M6', 'cot-5', 'C3-A-BANK-2', 25000.00, 'BANK_TRANSFER', '2026-05-01'),
                                                                                                                            ('mp-c3-may-7', 'C3-M7', 'cot-5', 'C3-A-CASH', 5000.00, 'CASH', '2026-05-01'),
                                                                                                                            ('mp-c3-may-8', 'C3-M8', 'cot-5', 'C3-A-CASH', 5000.00, 'CASH', '2026-05-01');

INSERT INTO collectivity_transaction (id, collectivity_id, member_id, membership_fee_id, account_credited_id, amount, payment_mode, creation_date) VALUES
                                                                                                                                                       ('tr-c3-may-1', 'col-3', 'C3-M1', 'cot-5', 'C3-A-BANK-1', 25000.00, 'BANK_TRANSFER', '2026-05-01'),
                                                                                                                                                       ('tr-c3-may-2', 'col-3', 'C3-M2', 'cot-5', 'C3-A-BANK-1', 25000.00, 'BANK_TRANSFER', '2026-05-01'),
                                                                                                                                                       ('tr-c3-may-3', 'col-3', 'C3-M3', 'cot-5', 'C3-A-MOBILE-1', 15000.00, 'MOBILE_BANKING', '2026-05-01'),
                                                                                                                                                       ('tr-c3-may-4', 'col-3', 'C3-M4', 'cot-5', 'C3-A-MOBILE-1', 15000.00, 'MOBILE_BANKING', '2026-05-01'),
                                                                                                                                                       ('tr-c3-may-5', 'col-3', 'C3-M5', 'cot-5', 'C3-A-BANK-2', 20000.00, 'BANK_TRANSFER', '2026-05-01'),
                                                                                                                                                       ('tr-c3-may-6', 'col-3', 'C3-M6', 'cot-5', 'C3-A-BANK-2', 25000.00, 'BANK_TRANSFER', '2026-05-01'),
                                                                                                                                                       ('tr-c3-may-7', 'col-3', 'C3-M7', 'cot-5', 'C3-A-CASH', 5000.00, 'CASH', '2026-05-01'),
                                                                                                                                                       ('tr-c3-may-8', 'col-3', 'C3-M8', 'cot-5', 'C3-A-CASH', 5000.00, 'CASH', '2026-05-01');

-- Mise à jour des soldes collectivité 3
UPDATE financial_account SET amount = 150000.00, updated_at = CURRENT_TIMESTAMP WHERE id = 'C3-A-BANK-1';
UPDATE financial_account SET amount = 95000.00, updated_at = CURRENT_TIMESTAMP WHERE id = 'C3-A-BANK-2';
UPDATE financial_account SET amount = 30000.00, updated_at = CURRENT_TIMESTAMP WHERE id = 'C3-A-MOBILE-1';
UPDATE financial_account SET amount = 60000.00, updated_at = CURRENT_TIMESTAMP WHERE id = 'C3-A-CASH';


-- ============================================================
-- NOUVEAUX MEMBRES JUNIORS
-- ============================================================

-- Collectivité 1 - 4 nouveaux juniors
INSERT INTO member (id, firstname, lastname, birthday, gender, address, profession, phone_number, email, registration_date, occupation, collectivity_id) VALUES
                                                                                                                                                             ('C1-NEW-1', 'Junior1Col1', 'Nouveau', '2000-01-01', 'MALE', 'Ambatondrazaka', 'Agriculteur', 340000001, 'new.c1.1@fed-agri.mg', '2026-04-01', 'JUNIOR', 'col-1'),
                                                                                                                                                             ('C1-NEW-2', 'Junior2Col1', 'Nouveau', '2000-01-02', 'FEMALE', 'Ambatondrazaka', 'Agriculteur', 340000002, 'new.c1.2@fed-agri.mg', '2026-04-01', 'JUNIOR', 'col-1'),
                                                                                                                                                             ('C1-NEW-3', 'Junior3Col1', 'Nouveau', '2000-01-03', 'MALE', 'Ambatondrazaka', 'Agriculteur', 340000003, 'new.c1.3@fed-agri.mg', '2026-05-01', 'JUNIOR', 'col-1'),
                                                                                                                                                             ('C1-NEW-4', 'Junior4Col1', 'Nouveau', '2000-01-04', 'FEMALE', 'Ambatondrazaka', 'Agriculteur', 340000004, 'new.c1.4@fed-agri.mg', '2026-06-01', 'JUNIOR', 'col-1');

INSERT INTO member_referees (member_id, referee_id) VALUES
                                                        ('C1-NEW-1', 'C1-M1'), ('C1-NEW-1', 'C1-M2'),
                                                        ('C1-NEW-2', 'C1-M1'), ('C1-NEW-2', 'C1-M2'),
                                                        ('C1-NEW-3', 'C1-M1'), ('C1-NEW-3', 'C1-M2'),
                                                        ('C1-NEW-4', 'C1-M1'), ('C1-NEW-4', 'C1-M2');

INSERT INTO collectivity_members (collectivity_id, member_id) VALUES
                                                                  ('col-1', 'C1-NEW-1'), ('col-1', 'C1-NEW-2'), ('col-1', 'C1-NEW-3'), ('col-1', 'C1-NEW-4');

-- Collectivité 2 - 3 nouveaux juniors
INSERT INTO member (id, firstname, lastname, birthday, gender, address, profession, phone_number, email, registration_date, occupation, collectivity_id) VALUES
                                                                                                                                                             ('C2-NEW-1', 'Junior1Col2', 'Nouveau', '2001-01-01', 'MALE', 'Ambatondrazaka', 'Agriculteur', 320000001, 'new.c2.1@fed-agri.mg', '2026-03-01', 'JUNIOR', 'col-2'),
                                                                                                                                                             ('C2-NEW-2', 'Junior2Col2', 'Nouveau', '2001-01-02', 'FEMALE', 'Ambatondrazaka', 'Agriculteur', 320000002, 'new.c2.2@fed-agri.mg', '2026-03-01', 'JUNIOR', 'col-2'),
                                                                                                                                                             ('C2-NEW-3', 'Junior3Col2', 'Nouveau', '2001-01-03', 'MALE', 'Ambatondrazaka', 'Agriculteur', 320000003, 'new.c2.3@fed-agri.mg', '2026-03-01', 'JUNIOR', 'col-2');

INSERT INTO member_referees (member_id, referee_id) VALUES
                                                        ('C2-NEW-1', 'C1-M1'), ('C2-NEW-1', 'C1-M2'),
                                                        ('C2-NEW-2', 'C1-M1'), ('C2-NEW-2', 'C1-M2'),
                                                        ('C2-NEW-3', 'C1-M1'), ('C2-NEW-3', 'C1-M2');

INSERT INTO collectivity_members (collectivity_id, member_id) VALUES
                                                                  ('col-2', 'C2-NEW-1'), ('col-2', 'C2-NEW-2'), ('col-2', 'C2-NEW-3');

-- Collectivité 3 - 6 nouveaux juniors
INSERT INTO member (id, firstname, lastname, birthday, gender, address, profession, phone_number, email, registration_date, occupation, collectivity_id) VALUES
                                                                                                                                                             ('C3-NEW-1', 'Junior1Col3', 'Nouveau', '2002-01-01', 'MALE', 'Brickaville', 'Apiculteur', 340100001, 'new.c3.1@fed-agri.mg', '2026-01-01', 'JUNIOR', 'col-3'),
                                                                                                                                                             ('C3-NEW-2', 'Junior2Col3', 'Nouveau', '2002-01-02', 'FEMALE', 'Brickaville', 'Apiculteur', 340100002, 'new.c3.2@fed-agri.mg', '2026-02-01', 'JUNIOR', 'col-3'),
                                                                                                                                                             ('C3-NEW-3', 'Junior3Col3', 'Nouveau', '2002-01-03', 'MALE', 'Brickaville', 'Apiculteur', 340100003, 'new.c3.3@fed-agri.mg', '2026-02-01', 'JUNIOR', 'col-3'),
                                                                                                                                                             ('C3-NEW-4', 'Junior4Col3', 'Nouveau', '2002-01-04', 'FEMALE', 'Brickaville', 'Apiculteur', 340100004, 'new.c3.4@fed-agri.mg', '2026-03-01', 'JUNIOR', 'col-3'),
                                                                                                                                                             ('C3-NEW-5', 'Junior5Col3', 'Nouveau', '2002-01-05', 'MALE', 'Brickaville', 'Apiculteur', 340100005, 'new.c3.5@fed-agri.mg', '2026-03-01', 'JUNIOR', 'col-3'),
                                                                                                                                                             ('C3-NEW-6', 'Junior6Col3', 'Nouveau', '2002-01-06', 'FEMALE', 'Brickaville', 'Apiculteur', 340100006, 'new.c3.6@fed-agri.mg', '2026-03-01', 'JUNIOR', 'col-3');

INSERT INTO member_referees (member_id, referee_id) VALUES
                                                        ('C3-NEW-1', 'C3-M1'), ('C3-NEW-1', 'C3-M2'),
                                                        ('C3-NEW-2', 'C3-M1'), ('C3-NEW-2', 'C3-M2'),
                                                        ('C3-NEW-3', 'C3-M1'), ('C3-NEW-3', 'C3-M2'),
                                                        ('C3-NEW-4', 'C3-M1'), ('C3-NEW-4', 'C3-M2'),
                                                        ('C3-NEW-5', 'C3-M1'), ('C3-NEW-5', 'C3-M2'),
                                                        ('C3-NEW-6', 'C3-M1'), ('C3-NEW-6', 'C3-M2');

INSERT INTO collectivity_members (collectivity_id, member_id) VALUES
                                                                  ('col-3', 'C3-NEW-1'), ('col-3', 'C3-NEW-2'), ('col-3', 'C3-NEW-3'),
                                                                  ('col-3', 'C3-NEW-4'), ('col-3', 'C3-NEW-5'), ('col-3', 'C3-NEW-6');


-- ============================================================
-- ACTIVITÉS (DONNÉES BONUS)
-- ============================================================

-- ========== COLLECTIVITÉ 1 ==========
-- act-1: AG1 (1er samedi de chaque mois)
INSERT INTO activity (id, collectivity_id, label, activity_type, member_occupation_concerned, recurrence_week_ordinal, recurrence_day_of_week, executive_date)
VALUES (
           'act-1',
           'col-1',
           'AG1',
           'MEETING',
           'JUNIOR,SENIOR,SECRETARY,TREASURER,VICE_PRESIDENT,PRESIDENT',
           1,
           'SA',
           NULL
       );

-- act-2: Formation de base (2ème dimanche de chaque mois)
INSERT INTO activity (id, collectivity_id, label, activity_type, member_occupation_concerned, recurrence_week_ordinal, recurrence_day_of_week, executive_date)
VALUES (
           'act-2',
           'col-1',
           'Formation de base',
           'TRAINING',
           'JUNIOR',
           2,
           'SU',
           NULL
       );


-- ========== COLLECTIVITÉ 2 ==========
-- act-3: AG2 (1er dimanche de chaque mois)
INSERT INTO activity (id, collectivity_id, label, activity_type, member_occupation_concerned, recurrence_week_ordinal, recurrence_day_of_week, executive_date)
VALUES (
           'act-3',
           'col-2',
           'AG2',
           'MEETING',
           'JUNIOR,SENIOR,SECRETARY,TREASURER,VICE_PRESIDENT,PRESIDENT',
           1,
           'SU',
           NULL
       );

-- act-4: Formation de base (3ème dimanche de chaque mois)
INSERT INTO activity (id, collectivity_id, label, activity_type, member_occupation_concerned, recurrence_week_ordinal, recurrence_day_of_week, executive_date)
VALUES (
           'act-4',
           'col-2',
           'Formation de base',
           'TRAINING',
           'JUNIOR',
           3,
           'SU',
           NULL
       );

-- act-5: Perfectionnement (ponctuelle)
INSERT INTO activity (id, collectivity_id, label, activity_type, member_occupation_concerned, recurrence_week_ordinal, recurrence_day_of_week, executive_date)
VALUES (
           'act-5',
           'col-2',
           'Perfectionnement',
           'TRAINING',
           'SENIOR',
           NULL,
           NULL,
           '2026-04-30'
       );


-- ========== COLLECTIVITÉ 3 ==========
-- act-6: AG3 (1er vendredi de chaque mois)
INSERT INTO activity (id, collectivity_id, label, activity_type, member_occupation_concerned, recurrence_week_ordinal, recurrence_day_of_week, executive_date)
VALUES (
           'act-6',
           'col-3',
           'AG3',
           'MEETING',
           'JUNIOR,SENIOR,SECRETARY,TREASURER,VICE_PRESIDENT,PRESIDENT',
           1,
           'FR',
           NULL
       );

-- act-7: Formation de base (4ème mercredi de chaque mois)
INSERT INTO activity (id, collectivity_id, label, activity_type, member_occupation_concerned, recurrence_week_ordinal, recurrence_day_of_week, executive_date)
VALUES (
           'act-7',
           'col-3',
           'Formation de base',
           'TRAINING',
           'JUNIOR',
           4,
           'WE',
           NULL
       );


-- ============================================================
-- PRÉSENCES AUX ACTIVITÉS (DONNÉES BONUS)
-- ============================================================

-- ========== COLLECTIVITÉ 1 - AG1 Mars 2026 (act-1 le 07/03/2026) ==========
-- C1-M1 à C1-M6 présents, C1-M7 et C1-M8 absents
INSERT INTO member_attendance (activity_id, member_id, attendance_status) VALUES
                                                                              ('act-1', 'C1-M1', 'ATTENDED'),
                                                                              ('act-1', 'C1-M2', 'ATTENDED'),
                                                                              ('act-1', 'C1-M3', 'ATTENDED'),
                                                                              ('act-1', 'C1-M4', 'ATTENDED'),
                                                                              ('act-1', 'C1-M5', 'ATTENDED'),
                                                                              ('act-1', 'C1-M6', 'ATTENDED'),
                                                                              ('act-1', 'C1-M7', 'MISSING'),
                                                                              ('act-1', 'C1-M8', 'MISSING');

-- ========== COLLECTIVITÉ 1 - AG1 Avril 2026 (act-1 le 04/04/2026) ==========
-- C1-M1, C1-M2, C1-M5, C1-M6, C1-M7, C1-M8 présents ; C1-M3, C1-M4 absents
INSERT INTO member_attendance (activity_id, member_id, attendance_status) VALUES
                                                                              ('act-1', 'C1-M1', 'ATTENDED'),
                                                                              ('act-1', 'C1-M2', 'ATTENDED'),
                                                                              ('act-1', 'C1-M3', 'MISSING'),
                                                                              ('act-1', 'C1-M4', 'MISSING'),
                                                                              ('act-1', 'C1-M5', 'ATTENDED'),
                                                                              ('act-1', 'C1-M6', 'ATTENDED'),
                                                                              ('act-1', 'C1-M7', 'ATTENDED'),
                                                                              ('act-1', 'C1-M8', 'ATTENDED');


-- ========== COLLECTIVITÉ 2 - AG2 Mars 2026 (act-3 le 08/03/2026) ==========
INSERT INTO member_attendance (activity_id, member_id, attendance_status) VALUES
                                                                              ('act-3', 'C1-M1', 'ATTENDED'),
                                                                              ('act-3', 'C1-M2', 'ATTENDED'),
                                                                              ('act-3', 'C1-M3', 'MISSING'),
                                                                              ('act-3', 'C1-M4', 'MISSING'),
                                                                              ('act-3', 'C1-M5', 'ATTENDED'),
                                                                              ('act-3', 'C1-M6', 'ATTENDED'),
                                                                              ('act-3', 'C1-M7', 'ATTENDED'),
                                                                              ('act-3', 'C1-M8', 'ATTENDED');

-- ========== COLLECTIVITÉ 2 - AG2 Avril 2026 (act-3 le 05/04/2026) ==========
INSERT INTO member_attendance (activity_id, member_id, attendance_status) VALUES
                                                                              ('act-3', 'C1-M1', 'ATTENDED'),
                                                                              ('act-3', 'C1-M2', 'ATTENDED'),
                                                                              ('act-3', 'C1-M3', 'MISSING'),
                                                                              ('act-3', 'C1-M4', 'ATTENDED'),
                                                                              ('act-3', 'C1-M5', 'ATTENDED'),
                                                                              ('act-3', 'C1-M6', 'ATTENDED'),
                                                                              ('act-3', 'C1-M7', 'ATTENDED'),
                                                                              ('act-3', 'C1-M8', 'MISSING');

-- ========== COLLECTIVITÉ 2 - Perfectionnement (act-5 le 30/04/2026) ==========
INSERT INTO member_attendance (activity_id, member_id, attendance_status) VALUES
                                                                              ('act-5', 'C1-M1', 'ATTENDED'),
                                                                              ('act-5', 'C1-M2', 'ATTENDED'),
                                                                              ('act-5', 'C1-M3', 'ATTENDED'),
                                                                              ('act-5', 'C1-M4', 'MISSING');


-- ========== COLLECTIVITÉ 3 - AG3 Mars 2026 (act-6 le 06/03/2026) ==========
INSERT INTO member_attendance (activity_id, member_id, attendance_status) VALUES
                                                                              ('act-6', 'C3-M1', 'ATTENDED'),
                                                                              ('act-6', 'C3-M2', 'ATTENDED'),
                                                                              ('act-6', 'C3-M3', 'ATTENDED'),
                                                                              ('act-6', 'C3-M4', 'ATTENDED'),
                                                                              ('act-6', 'C3-M5', 'ATTENDED'),
                                                                              ('act-6', 'C3-M6', 'ATTENDED'),
                                                                              ('act-6', 'C3-M7', 'MISSING'),
                                                                              ('act-6', 'C3-M8', 'MISSING');

-- ========== COLLECTIVITÉ 3 - AG3 Avril 2026 (act-6 le 03/04/2026) ==========
INSERT INTO member_attendance (activity_id, member_id, attendance_status) VALUES
                                                                              ('act-6', 'C3-M1', 'ATTENDED'),
                                                                              ('act-6', 'C3-M2', 'ATTENDED'),
                                                                              ('act-6', 'C3-M3', 'MISSING'),
                                                                              ('act-6', 'C3-M4', 'MISSING'),
                                                                              ('act-6', 'C3-M5', 'ATTENDED'),
                                                                              ('act-6', 'C3-M6', 'ATTENDED'),
                                                                              ('act-6', 'C3-M7', 'MISSING'),
                                                                              ('act-6', 'C3-M8', 'ATTENDED'),
                                                                              ('act-6', 'C1-M1', 'ATTENDED');  -- Membre d'une autre collectivité présent


-- ============================================================
-- VÉRIFICATION FINALE
-- ============================================================

-- Vérifier les activités
SELECT id, collectivity_id, label FROM activity ORDER BY collectivity_id;

-- Vérifier les présences par collectivité
SELECT
    a.collectivity_id,
    a.label,
    COUNT(ma.id) as nb_presences
FROM member_attendance ma
         JOIN activity a ON a.id = ma.activity_id
GROUP BY a.collectivity_id, a.label;