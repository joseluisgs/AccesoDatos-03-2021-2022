package hibernate

import javax.persistence.*


@Entity
@NamedQuery(name = "Departamento.finAll", query = "SELECT d FROM Departamento d")
class Departamento {
    @get:Column(name = "id")
    @get:GeneratedValue(strategy = GenerationType.IDENTITY)
    @get:Id
    var id: Long = 0

    @get:Column(name = "nombre")
    @get:Basic
    var nombre: String? = null

    @get:OneToMany(mappedBy = "departamento")
    var empleados: Collection<Empleado>? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Departamento

        if (id != other.id) return false
        if (nombre != other.nombre) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (nombre?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "Departamento(id=$id, nombre=$nombre, empleados=$empleados)"
    }


}
