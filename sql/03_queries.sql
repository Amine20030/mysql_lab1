-- =============================================================================
-- Lab 1 : Mise en place et découverte de MySQL
-- Script 03 : Requêtes de sélection et d'interrogation (DQL)
-- =============================================================================

-- 1. Consultation globale des départements
SELECT id, code, nom, lieu FROM departements ORDER BY id ASC;

-- 2. Employés ayant un salaire supérieur ou égal à 15000 DH
SELECT matricule, nom, prenom, salaire 
FROM employes 
WHERE salaire >= 15000.00 
ORDER BY salaire DESC;

-- 3. Jointure INNER JOIN : Liste des employés avec le nom de leur département
SELECT e.matricule, CONCAT(e.prenom, ' ', e.nom) AS nom_complet, e.salaire, d.nom AS departement
FROM employes e
INNER JOIN departements d ON e.departement_id = d.id
ORDER BY d.nom, e.nom;

-- 4. Jointure LEFT JOIN : Tous les départements et le décompte de leurs collaborateurs
SELECT d.code, d.nom AS departement, COUNT(e.id) AS effectif, COALESCE(AVG(e.salaire), 0.0) AS salaire_moyen
FROM departements d
LEFT JOIN employes e ON d.id = e.departement_id
GROUP BY d.id, d.code, d.nom
ORDER BY effectif DESC;

-- 5. Multi-Jointures : Répartition des employés sur les projets et volume d'heures
SELECT p.code AS projet_code, p.titre AS projet_titre, CONCAT(e.prenom, ' ', e.nom) AS employe, a.role, a.heures
FROM affectations a
JOIN employes e ON a.employe_id = e.id
JOIN projets p ON a.projet_id = p.id
ORDER BY p.code, a.heures DESC;

-- 6. Agrégation avec HAVING : Projets mobilisant plus de 200 heures cumulées
SELECT p.titre, SUM(a.heures) AS total_heures, COUNT(a.employe_id) AS nombre_intervenants
FROM projets p
JOIN affectations a ON p.id = a.projet_id
GROUP BY p.id, p.titre
HAVING SUM(a.heures) >= 200;
