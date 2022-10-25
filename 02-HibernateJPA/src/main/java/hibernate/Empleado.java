package hibernate;

import javax.persistence.*;

@Entity
@Table (name="Empleado") // Solo es necexario si cambia la tabla respecto al modelo
@NamedQuery(name = "Empleado.porDepartamentoNamed", query = "SELECT e FROM Empleado e WHERE e.departamento.nombre = ?1")
@NamedNativeQuery(
        name = "Empleado.porDepartamentoNative",
        query = "SELECT * FROM Empleado e, Departamento d WHERE d.nombre = ? and e.departamento_id = d.id",
        resultClass=Empleado.class)

public class Empleado {
    private long id;
    private String nombre;
    private String apellidos;
    private Departamento departamento;

    @Override
    public String toString() {
        return "Empleado{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", departamento=" + departamento +
                '}';
    }

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "nombre")
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Basic
    @Column(name = "apellidos")
    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Empleado empleado = (Empleado) o;

        if (id != empleado.id) return false;
        if (nombre != null ? !nombre.equals(empleado.nombre) : empleado.nombre != null) return false;
        if (apellidos != null ? !apellidos.equals(empleado.apellidos) : empleado.apellidos != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (nombre != null ? nombre.hashCode() : 0);
        result = 31 * result + (apellidos != null ? apellidos.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "departamento_id", referencedColumnName = "id", nullable = false)
    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }


}
