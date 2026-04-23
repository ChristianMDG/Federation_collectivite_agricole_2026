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
SELECT id, firstname, collectivity_id FROM member;