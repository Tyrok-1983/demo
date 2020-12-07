CREATE TABLE test_table (id uuid PRIMARY KEY DEFAULT uuid_generate_v4(), field integer);

do $$

    BEGIN
        FOR i IN 0..1000 LOOP
                INSERT INTO test_table (field)
                VALUES (i + 1);
            END LOOP;
    END
$$;
