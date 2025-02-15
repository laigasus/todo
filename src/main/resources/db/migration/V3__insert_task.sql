DELIMITER //

CREATE PROCEDURE IF NOT EXISTS insert_tasks(IN num_tasks INT)
BEGIN
    DECLARE i INT DEFAULT 1;

    WHILE i <= num_tasks
        DO
            INSERT INTO todo.task (id, title, description, state, created_at, updated_at)
            VALUES (i, CONCAT('Task ', i), CONCAT('Description ', i), 'TODO', CURRENT_TIMESTAMP,
                    CURRENT_TIMESTAMP);
            SET i = i + 1;
        END WHILE;
END //

DELIMITER ;

CALL insert_tasks(40);