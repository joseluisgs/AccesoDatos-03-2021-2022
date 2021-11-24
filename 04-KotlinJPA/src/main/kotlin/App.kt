import hibernate.Departamento
import hibernate.Empleado
import java.time.LocalDateTime
import java.util.function.Consumer
import javax.persistence.Persistence
import javax.persistence.Query


fun main(args: Array<String>) {
    println("Hello, JPA Kotlin")

    // Creamos las EntityManagerFactory para manejar las entidades y transacciones
    val entityManagerFactory = Persistence.createEntityManagerFactory("default")
    val entityManager = entityManagerFactory.createEntityManager()
    val transaction = entityManager.transaction
    try {

        // OPERACIONES CRUD

        // Listados
        println("Listar todos los Departamentos con consulta sobre la marcha")
        val queryDepartamento: Query = entityManager.createQuery("SELECT d FROM Departamento d")
        val departamentos: List<Departamento> = queryDepartamento.getResultList() as List<Departamento>
        departamentos.forEach(Consumer { x: Departamento? -> println(x) })

        //System.out.println("Listar todos los Departamentos con consulta NamedQuery");
        //TypedQuery<Departamento> namedQueryDepartamento = entityManager.createNamedQuery("Departamento.finAll", Departamento.class);
        //departamentos = namedQueryDepartamento.getResultList();
        //departamentos.forEach(System.out::println);
        println("Buscar el Departamento 1")
        val departamento = entityManager.find(Departamento::class.java, 1L)
        println(departamento.toString())
        println("Listar empleados del Departamento uno 1 usando la relación inversa")
        println("Cuidado con la navegabilidad")
        departamento.empleados!!.forEach(Consumer { x: Empleado? -> println(x) })
        println("Listar empleados del Departamento uno 1 usando NamedQuery parametrizada ")
        var empleados = entityManager.createNamedQuery(
            "Empleado.porDepartamentoNamed",
            Empleado::class.java
        )
            .setParameter(1, "TypeScript Departamento")
            .resultList
        empleados.forEach(Consumer { x: Empleado? -> println(x) })
        println("Listar empleados del Departamento uno 1 usando NativeQuery parametrizada ")
        empleados = entityManager.createNamedQuery("Empleado.porDepartamentoNative", Empleado::class.java)
            .setParameter(1, "TypeScript Departamento")
            .resultList
        empleados.forEach(Consumer { x: Empleado? -> println(x) })
        transaction.begin()
        val dep = Departamento()
        dep.nombre = "Prueba " + LocalDateTime.now().toString()
        entityManager.persist(dep)
        transaction.commit()
        println(dep)

        // Insertando
        println("Insertando Empleado")
        transaction.begin()
        val insert = Empleado()
        // pedro.setId(6);
        insert.nombre = "Insert " + LocalDateTime.now().toString()
        insert.apellidos = LocalDateTime.now().toString()
        insert.departamento = departamento
        entityManager.persist(insert)
        transaction.commit()
        println(insert)

        // Actualizando
        println("Actualizando Empleado 6")
        // Puedo buscarlo o usar inser
        val update = entityManager.find(Empleado::class.java, 5L)
        update.nombre = "Update " + LocalDateTime.now().toString()
        update.apellidos = "Update " + LocalDateTime.now().toString()
        transaction.begin()
        entityManager.merge(update)
        transaction.commit()
        println(update)

        // Eliminando
        println("Eliminado Empleado 6")
        // Puedo buscarlo o usar inser
        transaction.begin()
        entityManager.remove(insert)
        transaction.commit()
        println(insert)
    } finally {
        if (transaction.isActive) {
            transaction.rollback()
        }
        entityManager.close()
        entityManagerFactory.close()
    }
}