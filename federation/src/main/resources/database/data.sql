-- ============================================================
-- data.sql — Données de test
-- Conforme au schéma schema.sql fourni
-- ============================================================

-- ============================================================
-- 1. COLLECTIVITÉS
-- (id géré manuellement pour les tests, number et name UNIQUE)
-- ============================================================
INSERT INTO collectivity (id, number, name, location, speciality)
VALUES
    ('col-1', '1', 'Mpanorina',      'Ambatondrazaka', 'Riziculture'),
    ('col-2', '2', 'Dobo voalohany', 'Ambatondrazaka', 'Pisciculture'),
    ('col-3', '3', 'Tantely mamy',   'Brickaville',    'Apiculture');


-- ============================================================
-- 2. MEMBRES — COLLECTIVITÉ 1
-- phone_number est INT → on supprime le zéro initial
-- occupation : SENIOR = membre confirmé (pas de CONFIRMED dans l'enum)
-- ============================================================
INSERT INTO member (id, lastname, firstname, birthday, gender, address, profession, phone_number, email, occupation, collectivity_id)
VALUES
    ('C1-M1', 'Nom membre 1', 'Prénom membre 1', '1980-02-01', 'MALE',   'Lot II V M Ambato.',  'Riziculteur',  341234567,  'member.1@fed-agri.mg',  'PRESIDENT',      'col-1'),
    ('C1-M2', 'Nom membre 2', 'Prénom membre 2', '1982-03-05', 'MALE',   'Lot II F Ambato.',    'Agriculteur',  321234567,  'member.2@fed-agri.mg',  'VICE_PRESIDENT', 'col-1'),
    ('C1-M3', 'Nom membre 3', 'Prénom membre 3', '1992-03-10', 'MALE',   'Lot II J Ambato.',    'Collecteur',   331234567,  'member.3@fed-agri.mg',  'SECRETARY',      'col-1'),
    ('C1-M4', 'Nom membre 4', 'Prénom membre 4', '1988-05-22', 'FEMALE', 'Lot A K 50 Ambato.',  'Distributeur', 381234567,  'member.4@fed-agri.mg',  'TREASURER',      'col-1'),
    ('C1-M5', 'Nom membre 5', 'Prénom membre 5', '1999-08-21', 'MALE',   'Lot UV 80 Ambato.',   'Riziculteur',  373434567,  'member.5@fed-agri.mg',  'SENIOR',         'col-1'),
    ('C1-M6', 'Nom membre 6', 'Prénom membre 6', '1998-08-22', 'FEMALE', 'Lot UV 6 Ambato.',    'Riziculteur',  372234567,  'member.6@fed-agri.mg',  'SENIOR',         'col-1'),
    ('C1-M7', 'Nom membre 7', 'Prénom membre 7', '1998-01-31', 'MALE',   'Lot UV 7 Ambato.',    'Riziculteur',  374234567,  'member.7@fed-agri.mg',  'SENIOR',         'col-1'),
    ('C1-M8', 'Nom membre 8', 'Prénom membre 8', '1975-08-20', 'MALE',   'Lot UV 8 Ambato.',    'Riziculteur',  370234567,  'member.8@fed-agri.mg',  'SENIOR',         'col-1');

-- Mise à jour des postes dans la collectivité 1
UPDATE collectivity SET
                        president_id      = 'C1-M1',
                        vice_president_id = 'C1-M2',
                        treasurer_id      = 'C1-M4',
                        secretary_id      = 'C1-M3'
WHERE id = 'col-1';

-- Membres dans collectivity_members — col-1
INSERT INTO collectivity_members (collectivity_id, member_id) VALUES
                                                                  ('col-1', 'C1-M1'), ('col-1', 'C1-M2'), ('col-1', 'C1-M3'), ('col-1', 'C1-M4'),
                                                                  ('col-1', 'C1-M5'), ('col-1', 'C1-M6'), ('col-1', 'C1-M7'), ('col-1', 'C1-M8');

-- Parrainages — col-1
INSERT INTO member_referees (member_id, referee_id) VALUES
                                                        ('C1-M3', 'C1-M1'), ('C1-M3', 'C1-M2'),
                                                        ('C1-M4', 'C1-M1'), ('C1-M4', 'C1-M2'),
                                                        ('C1-M5', 'C1-M1'), ('C1-M5', 'C1-M2'),
                                                        ('C1-M6', 'C1-M1'), ('C1-M6', 'C1-M2'),
                                                        ('C1-M7', 'C1-M1'), ('C1-M7', 'C1-M2'),
                                                        ('C1-M8', 'C1-M6'), ('C1-M8', 'C1-M7');


-- ============================================================
-- 3. MEMBRES — COLLECTIVITÉ 2
-- Les mêmes personnes physiques mais enregistrées comme
-- membres distincts (C2-Mx) avec des rôles différents.
-- Email unique → suffixe .c2 pour éviter le conflit UNIQUE
-- ============================================================
INSERT INTO member (id, lastname, firstname, birthday, gender, address, profession, phone_number, email, occupation, collectivity_id)
VALUES
    ('C2-M1', 'Nom membre 1', 'Prénom membre 1', '1980-02-01', 'MALE',   'Lot II V M Ambato.',  'Riziculteur',  341234568,  'member.1.c2@fed-agri.mg',  'SENIOR',         'col-2'),
    ('C2-M2', 'Nom membre 2', 'Prénom membre 2', '1982-03-05', 'MALE',   'Lot II F Ambato.',    'Agriculteur',  321234568,  'member.2.c2@fed-agri.mg',  'SENIOR',         'col-2'),
    ('C2-M3', 'Nom membre 3', 'Prénom membre 3', '1992-03-10', 'MALE',   'Lot II J Ambato.',    'Collecteur',   331234568,  'member.3.c2@fed-agri.mg',  'SENIOR',         'col-2'),
    ('C2-M4', 'Nom membre 4', 'Prénom membre 4', '1988-05-22', 'FEMALE', 'Lot A K 50 Ambato.',  'Distributeur', 381234568,  'member.4.c2@fed-agri.mg',  'SENIOR',         'col-2'),
    ('C2-M5', 'Nom membre 5', 'Prénom membre 5', '1999-08-21', 'MALE',   'Lot UV 80 Ambato.',   'Riziculteur',  373434568,  'member.5.c2@fed-agri.mg',  'PRESIDENT',      'col-2'),
    ('C2-M6', 'Nom membre 6', 'Prénom membre 6', '1998-08-22', 'FEMALE', 'Lot UV 6 Ambato.',    'Riziculteur',  372234568,  'member.6.c2@fed-agri.mg',  'VICE_PRESIDENT', 'col-2'),
    ('C2-M7', 'Nom membre 7', 'Prénom membre 7', '1998-01-31', 'MALE',   'Lot UV 7 Ambato.',    'Riziculteur',  374234568,  'member.7.c2@fed-agri.mg',  'SECRETARY',      'col-2'),
    ('C2-M8', 'Nom membre 8', 'Prénom membre 8', '1975-08-20', 'MALE',   'Lot UV 8 Ambato.',    'Riziculteur',  370234568,  'member.8.c2@fed-agri.mg',  'TREASURER',      'col-2');

-- Mise à jour des postes dans la collectivité 2
UPDATE collectivity SET
                        president_id      = 'C2-M5',
                        vice_president_id = 'C2-M6',
                        secretary_id      = 'C2-M7',
                        treasurer_id      = 'C2-M8'
WHERE id = 'col-2';

-- Membres dans collectivity_members — col-2
INSERT INTO collectivity_members (collectivity_id, member_id) VALUES
                                                                  ('col-2', 'C2-M1'), ('col-2', 'C2-M2'), ('col-2', 'C2-M3'), ('col-2', 'C2-M4'),
                                                                  ('col-2', 'C2-M5'), ('col-2', 'C2-M6'), ('col-2', 'C2-M7'), ('col-2', 'C2-M8');

-- Parrainages — col-2 (parrainés par les membres confirmés de col-1)
INSERT INTO member_referees (member_id, referee_id) VALUES
                                                        ('C2-M3', 'C1-M1'), ('C2-M3', 'C1-M2'),
                                                        ('C2-M4', 'C1-M1'), ('C2-M4', 'C1-M2'),
                                                        ('C2-M5', 'C1-M1'), ('C2-M5', 'C1-M2'),
                                                        ('C2-M6', 'C1-M1'), ('C2-M6', 'C1-M2'),
                                                        ('C2-M7', 'C1-M1'), ('C2-M7', 'C1-M2'),
                                                        ('C2-M8', 'C1-M6'), ('C2-M8', 'C1-M7');


-- ============================================================
-- 4. MEMBRES — COLLECTIVITÉ 3
-- ============================================================
INSERT INTO member (id, lastname, firstname, birthday, gender, address, profession, phone_number, email, occupation, collectivity_id)
VALUES
    ('C3-M1', 'Nom membre 9',  'Prénom membre 9',  '1988-01-02', 'MALE',   'Lot 33 J Antsirabe',   'Apiculteur',   34034567,   'member.9@fed-agri.mg',  'PRESIDENT',      'col-3'),
    ('C3-M2', 'Nom membre 10', 'Prénom membre 10', '1982-03-05', 'MALE',   'Lot 2 J Antsirabe',    'Agriculteur',  338634567,  'member.10@fed-agri.mg', 'VICE_PRESIDENT', 'col-3'),
    ('C3-M3', 'Nom membre 11', 'Prénom membre 11', '1992-03-12', 'MALE',   'Lot 8 KM Antsirabe',   'Collecteur',   338234567,  'member.11@fed-agri.mg', 'SECRETARY',      'col-3'),
    ('C3-M4', 'Nom membre 12', 'Prénom membre 12', '1988-05-10', 'FEMALE', 'Lot A K 50 Antsirabe', 'Distributeur', 382334567,  'member.12@fed-agri.mg', 'TREASURER',      'col-3'),
    ('C3-M5', 'Nom membre 13', 'Prénom membre 13', '1999-08-11', 'MALE',   'Lot UV 80 Antsirabe',  'Apiculteur',   373365567,  'member.13@fed-agri.mg', 'SENIOR',         'col-3'),
    ('C3-M6', 'Nom membre 14', 'Prénom membre 14', '1998-08-09', 'FEMALE', 'Lot UV 6 Antsirabe',   'Apiculteur',   378234567,  'member.14@fed-agri.mg', 'SENIOR',         'col-3'),
    ('C3-M7', 'Nom membre 15', 'Prénom membre 15', '1998-01-13', 'MALE',   'Lot UV 7 Antsirabe',   'Apiculteur',   374914567,  'member.15@fed-agri.mg', 'SENIOR',         'col-3'),
    ('C3-M8', 'Nom membre 16', 'Prénom membre 16', '1975-08-02', 'MALE',   'Lot UV 8 Antsirabe',   'Apiculteur',   370634567,  'member.16@fed-agri.mg', 'SENIOR',         'col-3');

-- Mise à jour des postes dans la collectivité 3
UPDATE collectivity SET
                        president_id      = 'C3-M1',
                        vice_president_id = 'C3-M2',
                        secretary_id      = 'C3-M3',
                        treasurer_id      = 'C3-M4'
WHERE id = 'col-3';

-- Membres dans collectivity_members — col-3
INSERT INTO collectivity_members (collectivity_id, member_id) VALUES
                                                                  ('col-3', 'C3-M1'), ('col-3', 'C3-M2'), ('col-3', 'C3-M3'), ('col-3', 'C3-M4'),
                                                                  ('col-3', 'C3-M5'), ('col-3', 'C3-M6'), ('col-3', 'C3-M7'), ('col-3', 'C3-M8');

-- Parrainages — col-3
INSERT INTO member_referees (member_id, referee_id) VALUES
                                                        ('C3-M3', 'C3-M1'), ('C3-M3', 'C3-M2'),
                                                        ('C3-M4', 'C3-M1'), ('C3-M4', 'C3-M2'),
                                                        ('C3-M5', 'C3-M1'), ('C3-M5', 'C3-M2'),
                                                        ('C3-M6', 'C3-M1'), ('C3-M6', 'C3-M2'),
                                                        ('C3-M7', 'C3-M1'), ('C3-M7', 'C3-M2'),
                                                        ('C3-M8', 'C3-M1'), ('C3-M8', 'C3-M2');


-- ============================================================
-- 5. COTISATIONS
-- ============================================================
INSERT INTO membership_fee (id, label, status, frequency, eligible_from, amount, collectivity_id)
VALUES
    ('cot-1', 'Cotisation annuelle', 'ACTIVE', 'ANNUALLY', '2026-01-01', 100000, 'col-1'),
    ('cot-2', 'Cotisation annuelle', 'ACTIVE', 'ANNUALLY', '2026-01-01', 100000, 'col-2'),
    ('cot-3', 'Cotisation annuelle', 'ACTIVE', 'ANNUALLY', '2026-01-01',  50000, 'col-3');


-- ============================================================
-- 6. COMPTES FINANCIERS
-- Schéma : financial_account (id, amount, created_at, updated_at)
--          puis cash_account / mobile_banking_account selon le type
--          puis collectivity_financial_account pour le lien
-- ============================================================

-- financial_account parent — col-1
INSERT INTO financial_account (id) VALUES ('C1-A-CASH'), ('C1-A-MOBILE-1');
-- financial_account parent — col-2
INSERT INTO financial_account (id) VALUES ('C2-A-CASH'), ('C2-A-MOBILE-1');
-- financial_account parent — col-3
INSERT INTO financial_account (id) VALUES ('C3-A-CASH');

-- Comptes caisse
INSERT INTO cash_account (id) VALUES ('C1-A-CASH'), ('C2-A-CASH'), ('C3-A-CASH');

-- Comptes mobile money
INSERT INTO mobile_banking_account (id, holder_name, mobile_banking_service, mobile_number) VALUES
                                                                                                ('C1-A-MOBILE-1', 'Mpanorina',      'ORANGE_MONEY', '0370489612'),
                                                                                                ('C2-A-MOBILE-1', 'Dobo voalohany', 'ORANGE_MONEY', '0320489612');

-- Liaison collectivité ↔ comptes
INSERT INTO collectivity_financial_account (collectivity_id, financial_account_id) VALUES
                                                                                       ('col-1', 'C1-A-CASH'),
                                                                                       ('col-1', 'C1-A-MOBILE-1'),
                                                                                       ('col-2', 'C2-A-CASH'),
                                                                                       ('col-2', 'C2-A-MOBILE-1'),
                                                                                       ('col-3', 'C3-A-CASH');


-- ============================================================
-- 7. PAIEMENTS — COLLECTIVITÉ 1
-- payment_mode : CASH | MOBILE_BANKING | BANK_TRANSFER (enum)
-- ============================================================
INSERT INTO member_payment (member_id, amount, payment_mode, membership_fee_id, account_credited_id, creation_date)
VALUES
    ('C1-M1', 100000, 'CASH', 'cot-1', 'C1-A-CASH', '2026-01-01'),
    ('C1-M2', 100000, 'CASH', 'cot-1', 'C1-A-CASH', '2026-01-01'),
    ('C1-M3', 100000, 'CASH', 'cot-1', 'C1-A-CASH', '2026-01-01'),
    ('C1-M4', 100000, 'CASH', 'cot-1', 'C1-A-CASH', '2026-01-01'),
    ('C1-M5', 100000, 'CASH', 'cot-1', 'C1-A-CASH', '2026-01-01'),
    ('C1-M6', 100000, 'CASH', 'cot-1', 'C1-A-CASH', '2026-01-01'),
    ('C1-M7',  60000, 'CASH', 'cot-1', 'C1-A-CASH', '2026-01-01'),
    ('C1-M8',  90000, 'CASH', 'cot-1', 'C1-A-CASH', '2026-01-01');


-- ============================================================
-- 8. TRANSACTIONS — COLLECTIVITÉ 1
-- ============================================================
INSERT INTO collectivity_transaction (collectivity_id, member_id, membership_fee_id, account_credited_id, amount, payment_mode, creation_date)
VALUES
    ('col-1', 'C1-M1', 'cot-1', 'C1-A-CASH', 100000, 'CASH', '2026-01-01'),
    ('col-1', 'C1-M2', 'cot-1', 'C1-A-CASH', 100000, 'CASH', '2026-01-01'),
    ('col-1', 'C1-M3', 'cot-1', 'C1-A-CASH', 100000, 'CASH', '2026-01-01'),
    ('col-1', 'C1-M4', 'cot-1', 'C1-A-CASH', 100000, 'CASH', '2026-01-01'),
    ('col-1', 'C1-M5', 'cot-1', 'C1-A-CASH', 100000, 'CASH', '2026-01-01'),
    ('col-1', 'C1-M6', 'cot-1', 'C1-A-CASH', 100000, 'CASH', '2026-01-01'),
    ('col-1', 'C1-M7', 'cot-1', 'C1-A-CASH',  60000, 'CASH', '2026-01-01'),
    ('col-1', 'C1-M8', 'cot-1', 'C1-A-CASH',  90000, 'CASH', '2026-01-01');

-- Mise à jour du solde du compte caisse col-1 (total = 750 000)
UPDATE financial_account SET amount = 750000, updated_at = CURRENT_TIMESTAMP
WHERE id = 'C1-A-CASH';


-- ============================================================
-- 9. PAIEMENTS — COLLECTIVITÉ 2
-- ============================================================
INSERT INTO member_payment (member_id, amount, payment_mode, membership_fee_id, account_credited_id, creation_date)
VALUES
    ('C2-M1',  60000, 'CASH',           'cot-2', 'C2-A-CASH',     '2026-01-01'),
    ('C2-M2',  90000, 'CASH',           'cot-2', 'C2-A-CASH',     '2026-01-01'),
    ('C2-M3', 100000, 'CASH',           'cot-2', 'C2-A-CASH',     '2026-01-01'),
    ('C2-M4', 100000, 'CASH',           'cot-2', 'C2-A-CASH',     '2026-01-01'),
    ('C2-M5', 100000, 'CASH',           'cot-2', 'C2-A-CASH',     '2026-01-01'),
    ('C2-M6', 100000, 'CASH',           'cot-2', 'C2-A-CASH',     '2026-01-01'),
    ('C2-M7',  40000, 'MOBILE_BANKING', 'cot-2', 'C2-A-MOBILE-1', '2026-01-01'),
    ('C2-M8',  60000, 'MOBILE_BANKING', 'cot-2', 'C2-A-MOBILE-1', '2026-01-01');


-- ============================================================
-- 10. TRANSACTIONS — COLLECTIVITÉ 2
-- ============================================================
INSERT INTO collectivity_transaction (collectivity_id, member_id, membership_fee_id, account_credited_id, amount, payment_mode, creation_date)
VALUES
    ('col-2', 'C2-M1', 'cot-2', 'C2-A-CASH',      60000, 'CASH',           '2026-01-01'),
    ('col-2', 'C2-M2', 'cot-2', 'C2-A-CASH',      90000, 'CASH',           '2026-01-01'),
    ('col-2', 'C2-M3', 'cot-2', 'C2-A-CASH',     100000, 'CASH',           '2026-01-01'),
    ('col-2', 'C2-M4', 'cot-2', 'C2-A-CASH',     100000, 'CASH',           '2026-01-01'),
    ('col-2', 'C2-M5', 'cot-2', 'C2-A-CASH',     100000, 'CASH',           '2026-01-01'),
    ('col-2', 'C2-M6', 'cot-2', 'C2-A-CASH',     100000, 'CASH',           '2026-01-01'),
    ('col-2', 'C2-M7', 'cot-2', 'C2-A-MOBILE-1',  40000, 'MOBILE_BANKING', '2026-01-01'),
    ('col-2', 'C2-M8', 'cot-2', 'C2-A-MOBILE-1',  60000, 'MOBILE_BANKING', '2026-01-01');

-- Mise à jour des soldes col-2 (CASH = 550 000, MOBILE = 100 000)
UPDATE financial_account SET amount = 550000, updated_at = CURRENT_TIMESTAMP WHERE id = 'C2-A-CASH';
UPDATE financial_account SET amount = 100000, updated_at = CURRENT_TIMESTAMP WHERE id = 'C2-A-MOBILE-1';

-- ============================================================
-- Collectivité 3 : aucun paiement ni transaction
-- ============================================================