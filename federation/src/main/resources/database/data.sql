
INSERT INTO collectivity (id, number, name, location, speciality)
VALUES
    ('col-1', '1', 'Mpanorina',      'Ambatondrazaka', 'Riziculture'),
    ('col-2', '2', 'Dobo voalohany', 'Ambatondrazaka', 'Pisciculture'),
    ('col-3', '3', 'Tantely mamy',   'Brickaville',    'Apiculture');


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

UPDATE collectivity SET
                        president_id      = 'C1-M1',
                        vice_president_id = 'C1-M2',
                        treasurer_id      = 'C1-M4',
                        secretary_id      = 'C1-M3'
WHERE id = 'col-1';

INSERT INTO collectivity_members (collectivity_id, member_id) VALUES
                                                                  ('col-1','C1-M1'),('col-1','C1-M2'),('col-1','C1-M3'),('col-1','C1-M4'),
                                                                  ('col-1','C1-M5'),('col-1','C1-M6'),('col-1','C1-M7'),('col-1','C1-M8');

INSERT INTO member_referees (member_id, referee_id) VALUES
                                                        ('C1-M3','C1-M1'),('C1-M3','C1-M2'),
                                                        ('C1-M4','C1-M1'),('C1-M4','C1-M2'),
                                                        ('C1-M5','C1-M1'),('C1-M5','C1-M2'),
                                                        ('C1-M6','C1-M1'),('C1-M6','C1-M2'),
                                                        ('C1-M7','C1-M1'),('C1-M7','C1-M2'),
                                                        ('C1-M8','C1-M6'),('C1-M8','C1-M7');


INSERT INTO member (id, lastname, firstname, birthday, gender, address, profession, phone_number, email, occupation, collectivity_id)
VALUES
    ('C2-M1','Nom membre 1','Prénom membre 1','1980-02-01','MALE','Lot II V M Ambato.','Riziculteur',341234568,'member.1.c2@fed-agri.mg','SENIOR','col-2'),
    ('C2-M2','Nom membre 2','Prénom membre 2','1982-03-05','MALE','Lot II F Ambato.','Agriculteur',321234568,'member.2.c2@fed-agri.mg','SENIOR','col-2'),
    ('C2-M3','Nom membre 3','Prénom membre 3','1992-03-10','MALE','Lot II J Ambato.','Collecteur',331234568,'member.3.c2@fed-agri.mg','SENIOR','col-2'),
    ('C2-M4','Nom membre 4','Prénom membre 4','1988-05-22','FEMALE','Lot A K 50 Ambato.','Distributeur',381234568,'member.4.c2@fed-agri.mg','SENIOR','col-2'),
    ('C2-M5','Nom membre 5','Prénom membre 5','1999-08-21','MALE','Lot UV 80 Ambato.','Riziculteur',373434568,'member.5.c2@fed-agri.mg','PRESIDENT','col-2'),
    ('C2-M6','Nom membre 6','Prénom membre 6','1998-08-22','FEMALE','Lot UV 6 Ambato.','Riziculteur',372234568,'member.6.c2@fed-agri.mg','VICE_PRESIDENT','col-2'),
    ('C2-M7','Nom membre 7','Prénom membre 7','1998-01-31','MALE','Lot UV 7 Ambato.','Riziculteur',374234568,'member.7.c2@fed-agri.mg','SECRETARY','col-2'),
    ('C2-M8','Nom membre 8','Prénom membre 8','1975-08-20','MALE','Lot UV 8 Ambato.','Riziculteur',370234568,'member.8.c2@fed-agri.mg','TREASURER','col-2');

UPDATE collectivity SET
                        president_id='C2-M5',
                        vice_president_id='C2-M6',
                        secretary_id='C2-M7',
                        treasurer_id='C2-M8'
WHERE id='col-2';

INSERT INTO collectivity_members (collectivity_id, member_id) VALUES
                                                                  ('col-2','C2-M1'),('col-2','C2-M2'),('col-2','C2-M3'),('col-2','C2-M4'),
                                                                  ('col-2','C2-M5'),('col-2','C2-M6'),('col-2','C2-M7'),('col-2','C2-M8');


INSERT INTO member (id, lastname, firstname, birthday, gender, address, profession, phone_number, email, occupation, collectivity_id)
VALUES
    ('C3-M1','Nom membre 9','Prénom membre 9','1988-01-02','MALE','Lot 33 J Antsirabe','Apiculteur',34034567,'member.9@fed-agri.mg','PRESIDENT','col-3'),
    ('C3-M2','Nom membre 10','Prénom membre 10','1982-03-05','MALE','Lot 2 J Antsirabe','Agriculteur',338634567,'member.10@fed-agri.mg','VICE_PRESIDENT','col-3'),
    ('C3-M3','Nom membre 11','Prénom membre 11','1992-03-12','MALE','Lot 8 KM Antsirabe','Collecteur',338234567,'member.11@fed-agri.mg','SECRETARY','col-3'),
    ('C3-M4','Nom membre 12','Prénom membre 12','1988-05-10','FEMALE','Lot A K 50 Antsirabe','Distributeur',382334567,'member.12@fed-agri.mg','TREASURER','col-3');

UPDATE collectivity SET
                        president_id='C3-M1',
                        vice_president_id='C3-M2',
                        secretary_id='C3-M3',
                        treasurer_id='C3-M4'
WHERE id='col-3';



UPDATE member m
SET collectivity_id = (
    SELECT cm.collectivity_id
    FROM collectivity_members cm
    WHERE cm.member_id = m.id
    LIMIT 1
    )
WHERE EXISTS (
    SELECT 1 FROM collectivity_members cm WHERE cm.member_id = m.id
    );

SELECT id, firstname, collectivity_id FROM member;