-- ============================================================
-- DONNÉES DE TEST - Fédération de collectivités agricoles
-- TD Final PROG3 - 23 Avril 2026
-- ============================================================

-- ============================================================
-- 1. COLLECTIVITÉS
-- ============================================================
INSERT INTO collectivity (id, number, name, locality, specialization, created_at)
VALUES
    ('col-1', 1, 'Mpanorina',       'Ambatondrazaka', 'Riziculture',  NOW()),
    ('col-2', 2, 'Dobo voalohany',  'Ambatondrazaka', 'Pisciculture', NOW()),
    ('col-3', 3, 'Tantely mamy',    'Brickaville',    'Apiculture',   NOW());


-- ============================================================
-- 2. MEMBRES - Collectivité 1 (col-1)
-- ============================================================
INSERT INTO member (id, last_name, first_name, birth_date, gender, address, profession, phone, email, membership_date)
VALUES
    ('C1-M1', 'Nom membre 1',  'Prénom membre 1',  '1980-02-01', 'M', 'Lot II V M Ambato.',  'Riziculteur', '0341234567', 'member.1@fed-agri.mg',  NOW()),
    ('C1-M2', 'Nom membre 2',  'Prénom membre 2',  '1982-03-05', 'M', 'Lot II F Ambato.',    'Agriculteur', '0321234567', 'member.2@fed-agri.mg',  NOW()),
    ('C1-M3', 'Nom membre 3',  'Prénom membre 3',  '1992-03-10', 'M', 'Lot II J Ambato.',    'Collecteur',  '0331234567', 'member.3@fed-agri.mg',  NOW()),
    ('C1-M4', 'Nom membre 4',  'Prénom membre 4',  '1988-05-22', 'F', 'Lot A K 50 Ambato.',  'Distributeur','0381234567', 'member.4@fed-agri.mg',  NOW()),
    ('C1-M5', 'Nom membre 5',  'Prénom membre 5',  '1999-08-21', 'M', 'Lot UV 80 Ambato.',   'Riziculteur', '0373434567', 'member.5@fed-agri.mg',  NOW()),
    ('C1-M6', 'Nom membre 6',  'Prénom membre 6',  '1998-08-22', 'F', 'Lot UV 6 Ambato.',    'Riziculteur', '0372234567', 'member.6@fed-agri.mg',  NOW()),
    ('C1-M7', 'Nom membre 7',  'Prénom membre 7',  '1998-01-31', 'M', 'Lot UV 7 Ambato.',    'Riziculteur', '0374234567', 'member.7@fed-agri.mg',  NOW()),
    ('C1-M8', 'Nom membre 8',  'Prénom membre 6',  '1975-08-20', 'M', 'Lot UV 8 Ambato.',    'Riziculteur', '0370234567', 'member.8@fed-agri.mg',  NOW());

-- Postes dans la collectivité 1
INSERT INTO collectivity_member (collectivity_id, member_id, role)
VALUES
    ('col-1', 'C1-M1', 'PRESIDENT'),
    ('col-1', 'C1-M2', 'VICE_PRESIDENT'),
    ('col-1', 'C1-M3', 'SECRETARY'),
    ('col-1', 'C1-M4', 'TREASURER'),
    ('col-1', 'C1-M5', 'CONFIRMED_MEMBER'),
    ('col-1', 'C1-M6', 'CONFIRMED_MEMBER'),
    ('col-1', 'C1-M7', 'CONFIRMED_MEMBER'),
    ('col-1', 'C1-M8', 'CONFIRMED_MEMBER');

-- Parrainages collectivité 1
INSERT INTO member_sponsor (member_id, sponsor_id)
VALUES
    ('C1-M3', 'C1-M1'), ('C1-M3', 'C1-M2'),
    ('C1-M4', 'C1-M1'), ('C1-M4', 'C1-M2'),
    ('C1-M5', 'C1-M1'), ('C1-M5', 'C1-M2'),
    ('C1-M6', 'C1-M1'), ('C1-M6', 'C1-M2'),
    ('C1-M7', 'C1-M1'), ('C1-M7', 'C1-M2'),
    ('C1-M8', 'C1-M6'), ('C1-M8', 'C1-M7');


-- ============================================================
-- 3. MEMBRES - Collectivité 2 (col-2)
-- Note : les membres C1-M1 à C1-M8 sont les MÊMES personnes
--        (mêmes IDs), seuls les postes changent.
-- ============================================================

-- Postes dans la collectivité 2
INSERT INTO collectivity_member (collectivity_id, member_id, role)
VALUES
    ('col-2', 'C1-M1', 'CONFIRMED_MEMBER'),
    ('col-2', 'C1-M2', 'CONFIRMED_MEMBER'),
    ('col-2', 'C1-M3', 'CONFIRMED_MEMBER'),
    ('col-2', 'C1-M4', 'CONFIRMED_MEMBER'),
    ('col-2', 'C1-M5', 'PRESIDENT'),
    ('col-2', 'C1-M6', 'VICE_PRESIDENT'),
    ('col-2', 'C1-M7', 'SECRETARY'),
    ('col-2', 'C1-M8', 'TREASURER');


-- ============================================================
-- 4. MEMBRES - Collectivité 3 (col-3)
-- ============================================================
INSERT INTO member (id, last_name, first_name, birth_date, gender, address, profession, phone, email, membership_date)
VALUES
    ('C3-M1', 'Nom membre 9',  'Prénom membre 9',  '1988-01-02', 'M', 'Lot 33 J Antsirabe',   'Apiculteur',  '034034567',  'member.9@fed-agri.mg',  NOW()),
    ('C3-M2', 'Nom membre 10', 'Prénom membre 10', '1982-03-05', 'M', 'Lot 2 J Antsirabe',    'Agriculteur', '0338634567', 'member.10@fed-agri.mg', NOW()),
    ('C3-M3', 'Nom membre 11', 'Prénom membre 11', '1992-03-12', 'M', 'Lot 8 KM Antsirabe',   'Collecteur',  '0338234567', 'member.11@fed-agri.mg', NOW()),
    ('C3-M4', 'Nom membre 12', 'Prénom membre 12', '1988-05-10', 'F', 'Lot A K 50 Antsirabe', 'Distributeur','0382334567', 'member.12@fed-agri.mg', NOW()),
    ('C3-M5', 'Nom membre 13', 'Prénom membre 13', '1999-08-11', 'M', 'Lot UV 80 Antsirabe',  'Apiculteur',  '0373365567', 'member.13@fed-agri.mg', NOW()),
    ('C3-M6', 'Nom membre 14', 'Prénom membre 14', '1998-08-09', 'F', 'Lot UV 6 Antsirabe',   'Apiculteur',  '0378234567', 'member.14@fed-agri.mg', NOW()),
    ('C3-M7', 'Nom membre 15', 'Prénom membre 15', '1998-01-13', 'M', 'Lot UV 7 Antsirabe',   'Apiculteur',  '0374914567', 'member.15@fed-agri.mg', NOW()),
    ('C3-M8', 'Nom membre 16', 'Prénom membre 16', '1975-08-02', 'M', 'Lot UV 8 Antsirabe',   'Apiculteur',  '0370634567', 'member.16@fed-agri.mg', NOW());

-- Postes dans la collectivité 3
INSERT INTO collectivity_member (collectivity_id, member_id, role)
VALUES
    ('col-3', 'C3-M1', 'PRESIDENT'),
    ('col-3', 'C3-M2', 'VICE_PRESIDENT'),
    ('col-3', 'C3-M3', 'SECRETARY'),
    ('col-3', 'C3-M4', 'TREASURER'),
    ('col-3', 'C3-M5', 'CONFIRMED_MEMBER'),
    ('col-3', 'C3-M6', 'CONFIRMED_MEMBER'),
    ('col-3', 'C3-M7', 'CONFIRMED_MEMBER'),
    ('col-3', 'C3-M8', 'CONFIRMED_MEMBER');

-- Parrainages collectivité 3
INSERT INTO member_sponsor (member_id, sponsor_id)
VALUES
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
-- ============================================================

-- Collectivité 1
INSERT INTO financial_account (id, collectivity_id, type, initial_balance, holder_name, phone_number)
VALUES
    ('C1-A-CASH',     'col-1', 'CASH',         0, NULL,           NULL),
    ('C1-A-MOBILE-1', 'col-1', 'ORANGE_MONEY',  0, 'Mpanorina',    '0370489612');

-- Collectivité 2
INSERT INTO financial_account (id, collectivity_id, type, initial_balance, holder_name, phone_number)
VALUES
    ('C2-A-CASH',     'col-2', 'CASH',         0, NULL,            NULL),
    ('C2-A-MOBILE-1', 'col-2', 'ORANGE_MONEY',  0, 'Dobo voalohany','0320489612');

-- Collectivité 3
INSERT INTO financial_account (id, collectivity_id, type, initial_balance, holder_name, phone_number)
VALUES
    ('C3-A-CASH', 'col-3', 'CASH', 0, NULL, NULL);


-- ============================================================
-- 7. PAIEMENTS - Collectivité 1
-- ============================================================
INSERT INTO payment (collectivity_id, member_id, amount, account_id, payment_method, payment_date)
VALUES
    ('col-1', 'C1-M1', 100000, 'C1-A-CASH', 'CASH', '2026-01-01'),
    ('col-1', 'C1-M2', 100000, 'C1-A-CASH', 'CASH', '2026-01-01'),
    ('col-1', 'C1-M3', 100000, 'C1-A-CASH', 'CASH', '2026-01-01'),
    ('col-1', 'C1-M4', 100000, 'C1-A-CASH', 'CASH', '2026-01-01'),
    ('col-1', 'C1-M5', 100000, 'C1-A-CASH', 'CASH', '2026-01-01'),
    ('col-1', 'C1-M6', 100000, 'C1-A-CASH', 'CASH', '2026-01-01'),
    ('col-1', 'C1-M7',  60000, 'C1-A-CASH', 'CASH', '2026-01-01'),
    ('col-1', 'C1-M8',  90000, 'C1-A-CASH', 'CASH', '2026-01-01');


-- ============================================================
-- 8. TRANSACTIONS - Collectivité 1
-- ============================================================
INSERT INTO transaction (collectivity_id, member_id, amount, account_id, payment_method, created_at)
VALUES
    ('col-1', 'C1-M1', 100000, 'C1-A-CASH', 'CASH', '2026-01-01'),
    ('col-1', 'C1-M2', 100000, 'C1-A-CASH', 'CASH', '2026-01-01'),
    ('col-1', 'C1-M3', 100000, 'C1-A-CASH', 'CASH', '2026-01-01'),
    ('col-1', 'C1-M4', 100000, 'C1-A-CASH', 'CASH', '2026-01-01'),
    ('col-1', 'C1-M5', 100000, 'C1-A-CASH', 'CASH', '2026-01-01'),
    ('col-1', 'C1-M6', 100000, 'C1-A-CASH', 'CASH', '2026-01-01'),
    ('col-1', 'C1-M7',  60000, 'C1-A-CASH', 'CASH', '2026-01-01'),
    ('col-1', 'C1-M8',  90000, 'C1-A-CASH', 'CASH', '2026-01-01');


-- ============================================================
-- 9. PAIEMENTS - Collectivité 2
-- ============================================================
INSERT INTO payment (collectivity_id, member_id, amount, account_id, payment_method, payment_date)
VALUES
    ('col-2', 'C1-M1',  60000, 'C2-A-CASH',     'CASH',         '2026-01-01'),
    ('col-2', 'C1-M2',  90000, 'C2-A-CASH',     'CASH',         '2026-01-01'),
    ('col-2', 'C1-M3', 100000, 'C2-A-CASH',     'CASH',         '2026-01-01'),
    ('col-2', 'C1-M4', 100000, 'C2-A-CASH',     'CASH',         '2026-01-01'),
    ('col-2', 'C1-M5', 100000, 'C2-A-CASH',     'CASH',         '2026-01-01'),
    ('col-2', 'C1-M6', 100000, 'C2-A-CASH',     'CASH',         '2026-01-01'),
    ('col-2', 'C1-M7',  40000, 'C2-A-MOBILE-1', 'MOBILE_MONEY', '2026-01-01'),
    ('col-2', 'C1-M8',  60000, 'C2-A-MOBILE-1', 'MOBILE_MONEY', '2026-01-01');


-- ============================================================
-- 10. TRANSACTIONS - Collectivité 2
-- ============================================================
INSERT INTO transaction (collectivity_id, member_id, amount, account_id, payment_method, created_at)
VALUES
    ('col-2', 'C1-M1',  60000, 'C2-A-CASH',     'CASH',         '2026-01-01'),
    ('col-2', 'C1-M2',  90000, 'C2-A-CASH',     'CASH',         '2026-01-01'),
    ('col-2', 'C1-M3', 100000, 'C2-A-CASH',     'CASH',         '2026-01-01'),
    ('col-2', 'C1-M4', 100000, 'C2-A-CASH',     'CASH',         '2026-01-01'),
    ('col-2', 'C1-M5', 100000, 'C2-A-CASH',     'CASH',         '2026-01-01'),
    ('col-2', 'C1-M6', 100000, 'C2-A-CASH',     'CASH',         '2026-01-01'),
    ('col-2', 'C1-M7',  40000, 'C2-A-MOBILE-1', 'MOBILE_MONEY', '2026-01-01'),
    ('col-2', 'C1-M8',  60000, 'C2-A-MOBILE-1', 'MOBILE_MONEY', '2026-01-01');

-- Collectivité 3 : aucun paiement ni transaction pour le moment.