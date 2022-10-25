DROP TABLE IF EXISTS  "Departamento";
CREATE TABLE IF NOT EXISTS "Departamento" (
                                              "id"	INTEGER NOT NULL,
                                              "nombre"	TEXT NOT NULL,
                                              PRIMARY KEY("id" AUTOINCREMENT)
    );
DROP TABLE IF EXISTS  "Empleado";
CREATE TABLE IF NOT EXISTS "Empleado" (
                                          "id"	INTEGER NOT NULL,
                                          "nombre"	TEXT NOT NULL,
                                          "apellidos"	TEXT NOT NULL,
                                          "departamento_id"	INTEGER NOT NULL,
                                          PRIMARY KEY("id" AUTOINCREMENT)
    );
INSERT INTO "Departamento" VALUES (1,'Java Departamento');
INSERT INTO "Departamento" VALUES (2,'TypeScript Departamento');
INSERT INTO "Empleado" VALUES (1,'Pepe','Perez',1);
INSERT INTO "Empleado" VALUES (2,'Luis','Lopez',2);
INSERT INTO "Empleado" VALUES (3,'Ana','Anaya',1);
INSERT INTO "Empleado" VALUES (4,'Pedro','Perez',2);
INSERT INTO "Empleado" VALUES (5,'Elena','Fernandez',2);
