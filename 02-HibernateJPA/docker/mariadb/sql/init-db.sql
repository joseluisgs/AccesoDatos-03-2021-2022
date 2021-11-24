-- Adminer 4.8.1 MySQL 5.5.5-10.5.11-MariaDB dump

SET NAMES utf8;
SET time_zone = '+00:00';
SET foreign_key_checks = 0;
SET sql_mode = 'NO_AUTO_VALUE_ON_ZERO';

DROP DATABASE IF EXISTS `mydb`;
CREATE DATABASE `mydb` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `mydb`;

DROP TABLE IF EXISTS `Departamento`;
CREATE TABLE `Departamento` (
                                `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
                                `nombre` varchar(255) NOT NULL,
                                PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Base de Datos de Departamento';

INSERT INTO `Departamento` (`id`, `nombre`) VALUES
                                                (1,	'Java Departamento'),
                                                (2,	'TypeScript Departamento');

DROP TABLE IF EXISTS `Empleado`;
CREATE TABLE `Empleado` (
                            `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
                            `nombre` varchar(255) NOT NULL,
                            `apellidos` varchar(255) NOT NULL,
                            `departamento_id` bigint(20) unsigned NOT NULL,
                            PRIMARY KEY (`id`),
                            KEY `empleado_departamento_FK` (`departamento_id`),
                            CONSTRAINT `empleado_departamento_FK` FOREIGN KEY (`departamento_id`) REFERENCES `Departamento` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Tabla de Empleados';

INSERT INTO `Empleado` (`id`, `nombre`, `apellidos`, `departamento_id`) VALUES
                                                                            (1,	'Pepe',	'Perez',	1),
                                                                            (2,	'Luis',	'Lopez',	2),
                                                                            (3,	'Ana',	'Anaya',	1),
                                                                            (4,	'Pedro',	'Perez',	2),
                                                                            (5,	'Elena',	'Fernandez',	2);

-- 2021-10-06 15:53:59