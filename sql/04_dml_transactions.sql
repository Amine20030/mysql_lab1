-- =============================================================================
-- Lab 1 : Mise en place et découverte de MySQL
-- Script 04 : Mises à jour, Suppressions et Gestion des Transactions (DML / TCL)
-- =============================================================================

-- 1. Mise à jour avec condition : Revalorisation salariale de 8% pour les employés du département IT
UPDATE employes 
SET salaire = salaire * 1.08 
WHERE departement_id = (SELECT id FROM departements WHERE code = 'DEP-IT');

-- 2. Vérification de la contrainte ON DELETE SET NULL
-- Suppression d'un département et conservation des employés avec departement_id = NULL
DELETE FROM departements WHERE code = 'DEP-MKT';

-- 3. Simulation de Transaction Sécurisée avec COMMIT
-- Modification budgétaire d'un projet dans un bloc transactionnel atomique
-- START TRANSACTION;
UPDATE projets SET budget = budget + 50000.00 WHERE code = 'PRJ-CLD';
-- COMMIT;

-- 4. Nettoyage ciblé d'une affectation
DELETE FROM affectations WHERE employe_id = 1 AND projet_id = 2;
