<%-- =====================================================================
     UF2405 - BLOQUE 4 - EJERCICIO 13
     mascotas.jsp: capa de PRESENTACION y nada mas.
     Ni una sentencia SQL, ni una llamada al DAO, ni una conexion.
     Recibe la lista ya cargada en el atributo "mascotas" de la peticion.

     Vive bajo /WEB-INF/ a proposito: el contenedor no sirve directamente lo
     que hay ahi, asi que nadie puede pedir esta pagina por la URL y verla
     vacia; solo se llega a ella a traves del Servlet.
     ===================================================================== --%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Mascotas registradas</title>
</head>
<body>

<h1>Mascotas registradas</h1>

<table border="1" cellpadding="4">
    <tr><th>Nombre</th><th>Especie</th><th></th></tr>

    <%-- Apartado 4: se recorre la lista recibida. El bucle es de la libreria
         estandar JSTL, no un scriptlet de Java: en un JSP no debe haber codigo
         Java suelto entre <% %>. --%>
    <c:forEach var="m" items="${mascotas}">
        <tr>
            <%-- c:out escapa el contenido, que es la proteccion frente a XSS.
                 Escribir ${m.nombre} directamente pintaria sin escapar lo que
                 hubiera en la base de datos, incluida una etiqueta script. --%>
            <td><c:out value="${m.nombre}"/></td>
            <td><c:out value="${m.especie}"/></td>
            <td><a href="detalle?id=${m.id}">Ver detalle</a></td>
        </tr>
    </c:forEach>

    <c:if test="${empty mascotas}">
        <tr><td colspan="3">No hay mascotas registradas.</td></tr>
    </c:if>
</table>

</body>
</html>
