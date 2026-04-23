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

SELECT id, firstname, collectivity_id FROM member;