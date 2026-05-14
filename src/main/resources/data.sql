MERGE INTO USERS(USERNAME, PASSWORD, ENABLED) KEY(USERNAME) VALUES
('admin#10', '$2a$12$n.yrvz2s9lMV2mVq0fDb8uaP7PPBn5kVCIcsNs2JJWn2COHl4IMaC', 1),    -- Password: adm_id_10
('basic#10', '$2a$12$JolEJV.JaLjGAOHMaZm/x.o4AE06DNiTLNKOOhgDxVZyBMVWAWBma', 1),    -- Password: bsc_id_10
('pro#10', '$2a$12$iuphg8mw1fFAiP7PzJqtiOBDTqSCz6V1wFFBHFgvdIfbRy4BjxzyW', 1),      -- Password: pro_id_10
('prova#1#10', '$2a$12$LnzRh6GC2vSS3SlBIJf8..4O4DK77kw9QPps2bOA7rlHI5iUSS9aS', 1),  -- Password: prv_id_10
('prova#2#10', '$2a$12$LnzRh6GC2vSS3SlBIJf8..4O4DK77kw9QPps2bOA7rlHI5iUSS9aS', 1),  -- Password: prv_id_10
('prova#3#10', '$2a$12$LnzRh6GC2vSS3SlBIJf8..4O4DK77kw9QPps2bOA7rlHI5iUSS9aS', 1);  -- Password: prv_id_10

MERGE INTO AUTHORITIES(USERNAME, AUTHORITY) KEY(USERNAME) VALUES
('admin#10', 'ROLE_ADMIN'),
('basic#10', 'ROLE_USER_BASIC'),
('pro#10', 'ROLE_USER_PRO'),
('prova#1#10', 'ROLE_USER_PROVA'),
('prova#2#10', 'ROLE_USER_PROVA'),
('prova#3#10', 'ROLE_USER_PROVA');


MERGE INTO USERDATA (USERNAME, NAME, SURNAME, DATE_OF_BIRTH, EMAIL, REG_DATE) KEY(USERNAME) VALUES
('admin#10',    'Mario',   'Rossi',    DATE '1985-03-12', 'mario.rossi@example.com',    CURRENT_DATE),
('basic#10',    'Luca',    'Bianchi',  DATE '1992-07-25', 'luca.bianchi@example.com',   CURRENT_DATE),
('pro#10',      'Giulia',  'Verdi',    DATE '1988-11-08', 'giulia.verdi@example.com',   CURRENT_DATE),
('prova#1#10',  'Anna',    'Ferrari',  DATE '1999-01-15', 'anna.ferrari@example.com',   CURRENT_DATE),
('prova#2#10',  'Marco',   'Esposito', DATE '1995-09-30', 'marco.esposito@example.com', CURRENT_DATE),
('prova#3#10',  'Sara',    'Romano',   DATE '2001-05-21', 'sara.romano@example.com',    CURRENT_DATE);