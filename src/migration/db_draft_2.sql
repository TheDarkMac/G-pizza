-- Suppression des tables et types avec CASCADE pour éviter les conflits
DROP TABLE IF EXISTS dish_ingredient CASCADE;
DROP TABLE IF EXISTS dish CASCADE;
DROP TABLE IF EXISTS ingredient_price_history CASCADE;
DROP TABLE IF EXISTS stock CASCADE;
DROP TABLE IF EXISTS ingredient CASCADE;
DROP TABLE IF EXISTS available_quantity CASCADE;
DROP TYPE IF EXISTS movement_type CASCADE;
DROP TYPE IF EXISTS unit CASCADE;

-- Création du type ENUM pour l'unité des ingrédients
CREATE TYPE unit AS ENUM('G','L','U');

-- Table des ingrédients
CREATE TABLE ingredient(
    id_ingredient BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    unit unit NOT NULL
);

-- Historique des prix des ingrédients
CREATE TABLE ingredient_price_history(
    id_price BIGSERIAL PRIMARY KEY,
    id_ingredient BIGINT NOT NULL,
    date_price DATE NOT NULL DEFAULT CURRENT_DATE,
    unit_price DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_ingredient FOREIGN KEY (id_ingredient) REFERENCES ingredient(id_ingredient) ON DELETE CASCADE,
    CONSTRAINT unique_price_date UNIQUE (id_ingredient, date_price) -- Correction ici
);

-- Table des plats
CREATE TABLE dish(
    id_dish BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL
);

-- Association des ingrédients aux plats
CREATE TABLE dish_ingredient(
    id_dish BIGINT,
    id_ingredient BIGINT,
    quantity DECIMAL(10,2) NOT NULL,
    unit unit NOT NULL,
    PRIMARY KEY (id_dish, id_ingredient),
    CONSTRAINT fk_ingredient_id FOREIGN KEY (id_ingredient) REFERENCES ingredient(id_ingredient) ON DELETE CASCADE,
    CONSTRAINT fk_dish_id FOREIGN KEY (id_dish) REFERENCES dish(id_dish) ON DELETE CASCADE
);

-- Type ENUM pour les mouvements de stock
CREATE TYPE movement_type AS ENUM('IN','OUT');

-- Table de stock
CREATE TABLE stock(
    id_stock BIGSERIAL PRIMARY KEY,
    id_ingredient BIGINT NOT NULL,
    quantity DECIMAL(10,2) NOT NULL,
    date_of_movement TIMESTAMP NOT NULL DEFAULT NOW(),
    movement_type movement_type NOT NULL,
    CONSTRAINT fk_stock_ingredient FOREIGN KEY (id_ingredient) REFERENCES ingredient(id_ingredient) ON DELETE CASCADE
);

-- Table des quantités disponibles
CREATE TABLE available_quantity(
    id_ingredient BIGINT PRIMARY KEY,
    available_quantity DECIMAL(10,2) NOT NULL DEFAULT 0,
    date_of_last_movement TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_available_quantity FOREIGN KEY (id_ingredient) REFERENCES ingredient(id_ingredient) ON DELETE CASCADE
);

-- Fonction pour mettre à jour les quantités disponibles
CREATE OR REPLACE FUNCTION update_available_quantity()
RETURNS TRIGGER AS $$
DECLARE
    current_quantity DECIMAL(10,2);
BEGIN
    -- Récupération de la quantité disponible actuelle
    SELECT aq.available_quantity INTO current_quantity
    FROM available_quantity aq
    WHERE aq.id_ingredient = NEW.id_ingredient
    FOR UPDATE;

    -- Vérification du stock disponible en cas de sortie
    IF NEW.movement_type = 'out' AND (current_quantity IS NULL OR current_quantity < NEW.quantity) THEN
        RAISE EXCEPTION 'Stock insuffisant pour l''ingrédient %', NEW.id_ingredient;
    END IF;

    -- Mise à jour de la quantité disponible
    INSERT INTO available_quantity (id_ingredient, available_quantity, date_of_last_movement)
    VALUES (NEW.id_ingredient,
            COALESCE(current_quantity, 0) + CASE WHEN NEW.movement_type = 'in' THEN NEW.quantity ELSE -NEW.quantity END,
            NEW.date_of_movement)
    ON CONFLICT (id_ingredient)
    DO UPDATE SET
        available_quantity = available_quantity.available_quantity + -- ✅ Correction ici
                             CASE WHEN NEW.movement_type = 'in' THEN NEW.quantity ELSE -NEW.quantity END,
        date_of_last_movement = NEW.date_of_movement;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;


-- Trigger pour mettre à jour la table available_quantity après un mouvement de stock
CREATE TRIGGER trg_update_available_quantity
AFTER INSERT ON stock
FOR EACH ROW EXECUTE FUNCTION update_available_quantity();

-- Insertion d'exemples (évite les erreurs en utilisant ON CONFLICT DO NOTHING)
INSERT INTO ingredient (name, unit) VALUES
    ('Saucisse', 'G'),
    ('Tomate', 'G'),
    ('Oignon', 'G'),
    ('Fromage', 'G')
ON CONFLICT DO NOTHING;

INSERT INTO stock (id_ingredient, quantity, date_of_movement, movement_type) VALUES
    (1, 20, TO_TIMESTAMP('2025-02-01 08:00:00', 'YYYY-MM-DD HH24:MI:SS'), 'IN'), -- ✅ Correction : 'IN' devient 'in'
    (2, 15, TO_TIMESTAMP('2025-02-02 09:00:00', 'YYYY-MM-DD HH24:MI:SS'), 'IN'),
    (3, 30, TO_TIMESTAMP('2025-02-03 10:00:00', 'YYYY-MM-DD HH24:MI:SS'), 'IN'),
    (4, 25, TO_TIMESTAMP('2025-02-04 11:00:00', 'YYYY-MM-DD HH24:MI:SS'), 'IN')
ON CONFLICT DO NOTHING;

INSERT INTO ingredient_price_history(id_ingredient, date_price, unit_price)
VALUES (1, '2025-01-01', 20),
       (2, '2025-01-01', 10000),
       (3, '2025-01-01', 1000),
       (4, '2025-01-01', 1000);

INSERT INTO dish(name, unit_price)
VALUES ('HOT dog', 15000);

INSERT INTO dish_ingredient(id_dish, id_ingredient, quantity, unit)
VALUES (1, 1, 100, 'G'),
       (1, 2, 0.15, 'L'),
       (1, 3, 1, 'U'),
       (1, 4, 1, 'U');

INSERT INTO stock(id_ingredient, quantity, date_of_movement, movement_type)
    VALUES(3,100,TO_TIMESTAMP('2025-2-1 08:00:00','YYYY-MM-DD HH24:MI:SS'),'IN'),
       (4,10,TO_TIMESTAMP('2025-2-1 08:00:00','YYYY-MM-DD HH24:MI:SS'),'IN'),
       (1,10000,TO_TIMESTAMP('2025-2-1 08:00:00','YYYY-MM-DD HH24:MI:SS'),'IN'),
       (2,20,TO_TIMESTAMP('2025-2-1 08:00:00','YYYY-MM-DD HH24:MI:SS'),'IN');