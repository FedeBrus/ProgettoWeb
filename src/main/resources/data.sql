INSERT INTO USERS (USERNAME, PASSWORD, ENABLED)
SELECT *
FROM (
         VALUES
             ('admin#10', '$2a$12$TmrJhOPgKB.Rq5rtBa.Qv.hQ6nNV362WB.edZHi4HBZCPZJC3wzQO', 1),    -- Password: ad_id_10
             ('basic#10', '$2a$12$Qb5VCc5R5bYDB/jIAYoayud9ABaIuHLG2osUGkaXtpDFa1WONm.9y', 1),    -- Password: bs_id_10
             ('pro#10', '$2a$12$oiiegyG.Lgw2KikU81LAHemmexAOo8KruVtlzU974utEJcRJExlzq', 1),      -- Password: pr_id_10
             ('prova#1#10', '$2a$12$vFY9D2ei429EoPiffxalde3I0NySBiPXIS0uFTUYmI7KlibjzRuZm', 1),  -- Password: pv_id_10
             ('prova#2#10', '$2a$12$iwLBSfaV4zxwfoy3hiQiwOmlfFHz9iFkWUDB3kFniy4PBuNFjLcAm', 1),  -- Password: pv_id_10
             ('prova#3#10', '$2a$12$HVPlvpPTxfT/tQBJ4XvVFeRQv3muOrx2emJKwAdT4IyuYint0yL4a', 1)   -- Password: pv_id_10
) AS v(USERNAME, PASSWORD, ENABLED)
WHERE NOT EXISTS (
    SELECT 1
    FROM USERS u
    WHERE u.USERNAME = v.USERNAME
);

INSERT INTO AUTHORITIES(USERNAME, AUTHORITY)
    SELECT *
    FROM (
        VALUES
        ('admin#10', 'ROLE_ADMIN'),
        ('basic#10', 'ROLE_USER_BASIC'),
        ('pro#10', 'ROLE_USER_PRO'),
        ('prova#1#10', 'ROLE_USER_PROVA'),
        ('prova#2#10', 'ROLE_USER_PROVA'),
        ('prova#3#10', 'ROLE_USER_PROVA')
    ) AS v(USERNAME, AUTHORITY)
    WHERE NOT EXISTS (
        SELECT 1
        FROM AUTHORITIES a
        WHERE a.USERNAME = v.USERNAME
    );

MERGE INTO USERDATA (USERNAME, NAME, SURNAME, DATE_OF_BIRTH, EMAIL, REG_DATE) KEY(USERNAME) VALUES
('admin#10',    'Mario',   'Rossi',    DATE '1985-03-12', 'mario.rossi@example.com',    CURRENT_DATE),
('basic#10',    'Luca',    'Bianchi',  DATE '1992-07-25', 'luca.bianchi@example.com',   CURRENT_DATE),
('pro#10',      'Giulia',  'Verdi',    DATE '1988-11-08', 'giulia.verdi@example.com',   CURRENT_DATE),
('prova#1#10',  'Anna',    'Ferrari',  DATE '1999-01-15', 'anna.ferrari@example.com',   CURRENT_DATE),
('prova#2#10',  'Marco',   'Esposito', DATE '1995-09-30', 'marco.esposito@example.com', CURRENT_DATE),
('prova#3#10',  'Sara',    'Romano',   DATE '2001-05-21', 'sara.romano@example.com',    CURRENT_DATE);