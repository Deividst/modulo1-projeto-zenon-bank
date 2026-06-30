use
zenon_frauds;

CREATE table TRANSACTIONS
(
    id                 bigint auto_increment primary key,
    step               int            not null,
    type               varchar(20)    not null,
    amount             decimal(20, 2) not null,
    name_origin        varchar(50)    not null,
    old_balance_origin decimal(20, 2) not null,
    new_balance_origin decimal(20, 2) not null,
    name_dest          varchar(50)    not null,
    old_balance_dest   decimal(20, 2) not null,
    new_balance_dest   decimal(20, 2) not null,
    is_fraud           tinyint(1) default 0,
    is_flagged_fraud   tinyint(1) default 0
);

INSERT INTO zenon_frauds.TRANSACTIONS
(step, `type`, amount, name_origin, old_balance_origin, new_balance_origin, name_dest, old_balance_dest,
 new_balance_dest, is_fraud, is_flagged_fraud)
VALUES (1, 'PAYMENT', 5000.00, 'C100000', 5000.00, 0.00, 'M1000001', 0.00, 0.00, 0, 0);


SELECT *
FROM TRANSACTIONS;