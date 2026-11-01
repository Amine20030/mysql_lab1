-- =============================================================================
-- Lab 1 : Mise en place et découverte de MySQL
-- Script 02 : Peuplement des données initiales (DML - INSERT)
-- =============================================================================

-- Insertion des Départements
INSERT INTO departements (code, nom, lieu) VALUES
('DEP-IT', 'Technologies de l''Information', 'Casablanca - Maarif'),
('DEP-RH', 'Ressources Humaines', 'Rabat - Agdal'),
('DEP-FIN', 'Finance et Comptabilite', 'Casablanca - Marina'),
('DEP-MKT', 'Marketing et Communication', 'Marrakech - Gueliz');

-- Insertion des Employés
INSERT INTO employes (matricule, nom, prenom, email, salaire, date_embauche, departement_id) VALUES
('EMP-001', 'El Hezzam', 'Mohamed Amine', 'amine.hezzam@emsi.ma', 18500.00, '2022-01-15', 1),
('EMP-002', 'Alami', 'Sara', 'sara.alami@emsi.ma', 14500.00, '2022-04-01', 1),
('EMP-003', 'Benjelloun', 'Omar', 'omar.benjelloun@emsi.ma', 12000.00, '2023-02-10', 2),
('EMP-004', 'Tazi', 'Khadija', 'khadija.tazi@emsi.ma', 16000.00, '2021-09-01', 3),
('EMP-005', 'Idrissi', 'Youssef', 'youssef.idrissi@emsi.ma', 11500.00, '2023-06-15', 4),
('EMP-006', 'Chraibi', 'Mehdi', 'mehdi.chraibi@emsi.ma', 19500.00, '2020-11-20', 1);

-- Insertion des Projets
INSERT INTO projets (code, titre, budget, date_debut, date_fin) VALUES
('PRJ-CLD', 'Migration Cloud & Microservices', 450000.00, '2023-01-01', '2023-12-31'),
('PRJ-ERP', 'Refonte Système ERP Finance', 320000.00, '2023-03-01', '2024-02-28'),
('PRJ-MOB', 'Application Mobile RH & Collaborateurs', 180000.00, '2023-05-15', '2023-11-30');

-- Insertion des Affectations
INSERT INTO affectations (employe_id, projet_id, heures, role) VALUES
(1, 1, 120, 'Lead Architect'),
(2, 1, 160, 'Dev Backend'),
(6, 1, 100, 'DevOps Engineer'),
(4, 2, 140, 'Financial Analyst'),
(1, 2, 80,  'Technical Advisor'),
(3, 3, 110, 'HR Coordinator'),
(2, 3, 90,  'FullStack Dev');
