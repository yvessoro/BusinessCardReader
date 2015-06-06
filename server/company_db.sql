-- phpMyAdmin SQL Dump
-- version 2.11.0
-- http://www.phpmyadmin.net
--
-- Serveur: localhost
-- Généré le : Ven 05 Juin 2015 à 14:54
-- Version du serveur: 5.0.45
-- Version de PHP: 5.2.4

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";

--
-- Base de données: `company_db`
--

-- --------------------------------------------------------

--
-- Structure de la table `employee`
--

CREATE TABLE `employee` (
  `number` int(11) NOT NULL auto_increment,
  `id` varchar(20) NOT NULL,
  `lastname` varchar(250) NOT NULL,
  `firstname` varchar(250) NOT NULL,
  PRIMARY KEY  (`number`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Contenu de la table `employee`
--

