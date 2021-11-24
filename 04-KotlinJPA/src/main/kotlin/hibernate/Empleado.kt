package hibernate

import javax.persistence.*


@Entity
@Table(name = "Empleado") // Solo es necexario si cambia la tabla respecto al modelo
@NamedQueries(
        NamedQuery(name = "Empleado.findAll", query = "SELECT e FROM Empleado e"),
        NamedQuery(name = "Empleado.porDepartamentoNamed", query = "SELECT e FROM Empleado e WHERE e.departamento.nombre = ?1"),
        NamedQuery(name = "Empleado.findByNombre", query = "SELECT e FROM Empleado e WHERE e.nombre = :nombre")
)
@NamedNativeQuery(
    name = "Empleado.porDepartamentoNative",
    query = "SELECT * FROM Empleado e, Departamento d WHERE d.nombre = ? and e.departamento_id = d.id",
    resultClass = Empleado::class
)
class Empleado {
    @get:Column(name = "id")
    @get:GeneratedValue(strategy = GenerationType.IDENTITY)
    @get:Id
    var id: Long = 0

    @get:Column(name = "nombre")
    @get:Basic
    var nombre: String? = null

    @get:Column(name = "apellidos")
    @get:Basic
    var apellidos: String? = null

    @get:JoinColumn(name = "departamento_id", referencedColumnName = "id", nullable = false)
    @get:ManyToOne
    var departamento: Departamento? = null





    override fun toString(): String {
        return "Empleado(id=$id, nombre=$nombre, apellidos=$apellidos, departamento=${departamento?.id})"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Empleado

        if (id != other.id) return false
        if (nombre != other.nombre) return false
        if (apellidos != other.apellidos) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (nombre?.hashCode() ?: 0)
        result = 31 * result + (apellidos?.hashCode() ?: 0)
        return result
    }
}
