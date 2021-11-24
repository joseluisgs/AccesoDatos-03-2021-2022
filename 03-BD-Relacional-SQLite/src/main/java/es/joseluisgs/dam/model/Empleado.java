package es.joseluisgs.dam.model;

import lombok.Data;

@Data
public class Empleado {
    private long id;
    private String nombre;
    private String apellidos;
    private long departamento_id;
}
