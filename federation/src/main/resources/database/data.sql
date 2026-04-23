<<<<<<< HEAD

=======
>>>>>>> 136bb078437ff1826707ea8d31af680d175db84d
-- Mettre à jour collectivity_id pour tous les membres qui sont dans collectivity_members
UPDATE member m
SET collectivity_id = (
    SELECT collectivity_id
    FROM collectivity_members cm
    WHERE cm.member_id = m.id
    LIMIT 1
)
WHERE EXISTS (
    SELECT 1 FROM collectivity_members cm WHERE cm.member_id = m.id
);

-- Vérifier
<<<<<<< HEAD
SELECT id, firstname, collectivity_id FROM member;

INSERT INTO collectivity_transaction (
    id, collectivity_id, member_id, membership_fee_id,
    amount, payment_mode, transaction_date
) VALUES (
             'tr_1000',
             'col_2000',
             'mem_1000',
             'mf_1000',
             25000,
             'CASH',
             '2026-01-15'
         );

}
=======
SELECT id, firstname, collectivity_id FROM member;
>>>>>>> 136bb078437ff1826707ea8d31af680d175db84d
