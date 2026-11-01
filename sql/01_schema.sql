-- =============================================================================
-- Lab 1 : Mise en place et découverte de MySQL
-- Script 01 : Création du schéma relationnel (DDL)
-- =============================================================================

CREATE TABLE IF NOT EXISTS departements (
    id INT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(20) NOT NULL UNIQUE,
    nom VARCHAR(100) NOT NULL,
    lieu VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS employes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    matricule VARCHAR(20) NOT NULL UNIQUE,
    nom VARCHAR(50) NOT NULL,
    prenom VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    salaire DECIMAL(10,2) NOT NULL CHECK (salaire > 0),
    date_embauche DATE NOT NULL,
    departement_id INT,
    CONSTRAINT fk_emp_dept FOREIGN KEY (departement_id) 
        REFERENCES departements(id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS projets (
    id INT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(20) NOT NULL UNIQUE,
    titre VARCHAR(150) NOT NULL,
    budget DECIMAL(12,2) NOT NULL,
    date_debut DATE NOT NULL,
    date_fin DATE
);

CREATE TABLE IF NOT EXISTS affectations (
    employe_id INT NOT NULL,
    projet_id INT NOT NULL,
    heures INT NOT NULL DEFAULT 0,
    role VARCHAR(50) NOT NULL,
    PRIMARY KEY (employe_id, projet_id),
    CONSTRAINT fk_aff_emp FOREIGN KEY (employe_id) 
        REFERENCES employes(id) ON DELETE CASCADE,
    CONSTRAINT fk_aff_proj FOREIGN KEY (projet_id) 
        REFERENCES projets(id) ON DELETE CASCADE
);
