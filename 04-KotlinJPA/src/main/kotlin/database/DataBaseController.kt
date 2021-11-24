package database

import org.apache.ibatis.jdbc.ScriptRunner
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.FileReader
import java.io.Reader
import java.sql.*


object DataBaseController{
    private var serverUrl: String? = null
    private var serverPort: String? = null
    private var dataBaseName: String? = null
    private var user: String? = null
    private var password: String? = null

    /*
    Tipos de Driver
    SQLite: "org.sqlite.JDBC";
    MySQL: "com.mysql.jdbc.Driver"
    MariaDB: com.mysql.cj.jdbc.Driver
     */
    private var jdbcDriver: String? = null

    // Para manejar las conexiones y respuestas de las mismas
    private var connection: Connection? = null
    private var preparedStatement: PreparedStatement? = null

    /**
     * Carga la configuración de acceso al servidor de Base de Datos
     * Puede ser directa "hardcodeada" o asignada dinámicamente a traves de ficheros .env o properties
     */
    private fun initConfig() {
        // Leemos los datos de la base de datos que pueden estar en
        // porperties o en .env
        // imaginemos que el usuario y pasword estaán en .env y el resto en application.properties
        // si no los rellenamos aquí.
        serverUrl = "localhost";
        serverPort = "3306"
        dataBaseName = "mydb"
        jdbcDriver = "com.mysql.cj.jdbc.Driver"
        user = "mydb"
        password = "mydb1234"
    }

    /**
     * Abre la conexión con el servidor  de base de datos
     *
     * @throws SQLException Servidor no accesible por problemas de conexión o datos de acceso incorrectos
     */
    @Throws(SQLException::class)
    fun open() {
        //String url = "jdbc:sqlite:"+this.ruta+this.bbdd; //MySQL jdbc:mysql://localhost/prueba", "root", "1daw"
        val url = "jdbc:mariadb://" + serverUrl + ":" + serverPort + "/" + dataBaseName
        // System.out.println(url);
        // Obtenemos la conexión
        connection = DriverManager.getConnection(url, user, password)
    }

    /**
     * Cierra la conexión con el servidor de base de datos
     *
     * @throws SQLException Servidor no responde o no puede realizar la operación de cierre
     */
    @Throws(SQLException::class)
    fun close() {
        preparedStatement?.close() // así me ahorro el try catch o el if preparedStatement != null, si no es null lo hace, si no ignora la linea
        connection?.close()
    }

    /**
     * Realiza una consulta a la base de datos de manera "preparada" obteniendo los parametros opcionales si son necesarios
     *
     * @param querySQL consulta SQL de tipo select
     * @param params   parámetros de la consulta parametrizada
     * @return ResultSet de la consulta
     * @throws SQLException No se ha podido realizar la consulta o la tabla no existe
     */
    @Throws(SQLException::class)
    private fun executeQuery(querySQL: String, vararg params: Any): ResultSet {
        preparedStatement = connection?.prepareStatement(querySQL)
        // Vamos a pasarle los parametros usando preparedStatement
        for (i in 0 until params.size) {
            preparedStatement!!.setObject(i + 1, params[i])
        }
        return preparedStatement!!.executeQuery()
    }

    /**
     * Realiza una consulta select a la base de datos de manera "preparada" obteniendo los parametros opcionales si son necesarios
     *
     * @param querySQL consulta SQL de tipo select
     * @param params   parámetros de la consulta parametrizada
     * @return ResultSet de la consulta
     * @throws SQLException No se ha podido realizar la consulta o la tabla no existe
     */
    @Throws(SQLException::class)
    fun select(querySQL: String, vararg params: Any?): ResultSet? {
        return executeQuery(querySQL, *params as Array<out Any>)
    }

    /**
     * Realiza una consulta select a la base de datos de manera "preparada" obteniendo los parametros opcionales si son necesarios
     *
     * @param querySQL consulta SQL de tipo select
     * @param limit    número de registros de la página
     * @param offset   desplazamiento de registros o número de registros ignorados para comenzar la devolución
     * @param params   parámetros de la consulta parametrizada
     * @return ResultSet de la consulta
     * @throws SQLException No se ha podido realizar la consulta o la tabla no existe o el desplazamiento es mayor que el número de registros
     */
    @Throws(SQLException::class)
    fun select(querySQL: String, limit: Int, offset: Int, vararg params: Any?): ResultSet? {
        val query = "$querySQL LIMIT $limit OFFSET $offset"
        return executeQuery(query, *params as Array<out Any>)
    }

    /**
     * Realiza una consulta de tipo insert de manera "preparada" con los parametros opcionales si son encesarios
     *
     * @param insertSQL consulta SQL de tipo insert
     * @param params    parámetros de la consulta parametrizada
     * @return Clave del registro insertado
     * @throws SQLException tabla no existe o no se ha podido realizar la operación
     */
    @Throws(SQLException::class)
    fun insert(insertSQL: String?, vararg params: Any?): ResultSet? {
        // Con return generated keys obtenemos las claves generadas is las claves es autonumerica por ejemplo
        preparedStatement = connection?.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)
        // Vamos a pasarle los parametros usando preparedStatement
        for (i in 0 until params.size) {
            preparedStatement!!.setObject(i + 1, params[i])
        }
        preparedStatement!!.executeUpdate()
        return preparedStatement!!.generatedKeys
    }

    /**
     * Realiza una consulta de tipo update de manera "preparada" con los parametros opcionales si son encesarios
     *
     * @param updateSQL consulta SQL de tipo update
     * @param params    parámetros de la consulta parametrizada
     * @return número de registros actualizados
     * @throws SQLException tabla no existe o no se ha podido realizar la operación
     */
    @Throws(SQLException::class)
    fun update(updateSQL: String, vararg params: Any?): Int {
        return updateQuery(updateSQL, *params as Array<out Any>)
    }

    /**
     * Realiza una consulta de tipo delete de manera "preparada" con los parametros opcionales si son encesarios
     *
     * @param deleteSQL consulta SQL de tipo delete
     * @param params    parámetros de la consulta parametrizada
     * @return número de registros eliminados
     * @throws SQLException tabla no existe o no se ha podido realizar la operación
     */
    @Throws(SQLException::class)
    fun delete(deleteSQL: String, vararg params: Any?): Int {
        return updateQuery(deleteSQL, *params as Array<out Any>)
    }

    /**
     * Realiza una consulta de tipo update, es decir que modifca los datos, de manera "preparada" con los parametros opcionales si son encesarios
     *
     * @param genericSQL consulta SQL de tipo update, delete, creted, etc.. que modifica los datos
     * @param params     parámetros de la consulta parametrizada
     * @return número de registros eliminados
     * @throws SQLException tabla no existe o no se ha podido realizar la operación
     */
    @Throws(SQLException::class)
    private fun updateQuery(genericSQL: String, vararg params: Any): Int {
        // Con return generated keys obtenemos las claves generadas
        preparedStatement = connection?.prepareStatement(genericSQL)
        // Vamos a pasarle los parametros usando preparedStatement
        for (i in 0 until params.size) {
            preparedStatement!!.setObject(i + 1, params[i])
        }
        return preparedStatement!!.executeUpdate()
    }

    @Throws(FileNotFoundException::class)
    fun initData(sqlFile: String?) {
        val sr = ScriptRunner(connection)
        val reader: Reader = BufferedReader(FileReader(sqlFile))
        sr.runScript(reader)
    }


    /**
     * Constructor privado para Singleton
     */
    init {
        // System.out.println("Mi nombre es: " + this.nombre);
        initConfig()
    }
}
