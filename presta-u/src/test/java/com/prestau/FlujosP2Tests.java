package com.prestau;

import com.prestau.domain.*;
import com.prestau.repository.*;
import com.prestau.service.*;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FlujosP2Tests {
    @Autowired JdbcTemplate jdbc;
    @Autowired SolicitudService solicitudes;
    @Autowired CategoriaService categorias;
    @Autowired PrestamoService prestamos;
    @Autowired SolicitudRepository solicitudesRepo;
    @Value("${local.server.port}") int puerto;
    private final HttpClient http = HttpClient.newHttpClient();

    @BeforeEach
    void datos() {
        for (String tabla : new String[]{"prestamo", "solicitud", "mantenimiento", "equipo", "usuario", "categoria", "rol"})
            jdbc.update("DELETE FROM " + tabla);
        jdbc.update("INSERT INTO rol (id_rol,nombre) VALUES (1,'Solicitante')");
        jdbc.update("INSERT INTO usuario (id_usuario,id_rol,identificacion,nombre,correo,clave_hash,idioma,activo) VALUES (1,1,'U1','Usuario uno','uno@test','pendiente','es',true),(2,1,'U2','Usuario dos','dos@test','pendiente','es',true)");
        jdbc.update("INSERT INTO categoria (id_categoria,nombre,activa) VALUES (1,'Computación',true),(2,'Vacía',true)");
        jdbc.update("INSERT INTO equipo (id_equipo,id_categoria,codigo,nombre,estado_operativo,activo) VALUES (1,1,'LAP-001','Laptop','En servicio',true),(2,1,'MIC-002','Micrófono','Mantenimiento',true),(3,1,'LAP-003','Inactivo','En servicio',false)");
    }
    SolicitudFormulario formulario(int equipo, int dias) {
        var f = new SolicitudFormulario();
        f.setEquipoId(equipo); f.setInicio(LocalDateTime.now().plusDays(dias).withNano(0));
        f.setFin(f.getInicio().plusHours(2)); f.setMotivo("Trabajo de clase");
        return f;
    }
    HttpResponse<String> get(String ruta) throws Exception {
        return http.send(HttpRequest.newBuilder(URI.create("http://localhost:"+puerto+ruta)).GET().build(), HttpResponse.BodyHandlers.ofString());
    }
    HttpResponse<String> post(String ruta, String body) throws Exception {
        return http.send(HttpRequest.newBuilder(URI.create("http://localhost:"+puerto+ruta))
                .header("Content-Type","application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(body)).build(),HttpResponse.BodyHandlers.ofString());
    }
    @Test void crearYEvitarDuplicados() {
        var f=formulario(1,2); var s=solicitudes.crear(f);
        assertEquals(EstadoSolicitud.PENDIENTE,s.getEstado());
        assertThrows(IllegalArgumentException.class,()->solicitudes.crear(f));
        assertEquals(1,solicitudesRepo.count());
    }
    @Test void rechazarFechasEquipoYMotivo() {
        assertThrows(IllegalArgumentException.class,()->solicitudes.crear(formulario(1,-1)));
        var f=formulario(1,2); f.setFin(f.getInicio());
        assertThrows(IllegalArgumentException.class,()->solicitudes.crear(f));
        assertThrows(IllegalArgumentException.class,()->solicitudes.crear(formulario(2,2)));
        assertThrows(IllegalArgumentException.class,()->solicitudes.crear(formulario(3,2)));
        var vacio=formulario(1,2); vacio.setMotivo(" ");
        assertThrows(IllegalArgumentException.class,()->solicitudes.crear(vacio));
        assertEquals(0,solicitudesRepo.count());
    }
    @Test void detectarSuperposicionYPermitirLimitesContiguos() {
        var f=formulario(1,3); var s=solicitudes.crear(f);
        jdbc.update("UPDATE solicitud SET estado='APROBADA' WHERE id_solicitud=?",s.getIdSolicitud());
        assertThrows(IllegalArgumentException.class,()->solicitudes.crear(f));
        var contigua=formulario(1,3); contigua.setInicio(f.getFin()); contigua.setFin(f.getFin().plusHours(1));
        assertNotNull(solicitudes.crear(contigua).getIdSolicitud());
    }
    @Test void protegerSolicitudesAjenasYCancelarSoloPendientes() throws Exception {
        var s=solicitudes.crear(formulario(1,2));
        solicitudes.cancelar(s.getIdSolicitud());
        assertEquals(EstadoSolicitud.CANCELADA,solicitudes.getPropia(s.getIdSolicitud()).getEstado());
        assertThrows(IllegalArgumentException.class,()->solicitudes.cancelar(s.getIdSolicitud()));
        jdbc.update("UPDATE solicitud SET id_usuario=2 WHERE id_solicitud=?",s.getIdSolicitud());
        assertTrue(solicitudes.getPropias().isEmpty());
        assertThrows(ResponseStatusException.class,()->solicitudes.getPropia(s.getIdSolicitud()));
        assertEquals(404,get("/solicitudes/"+s.getIdSolicitud()).statusCode());
        assertEquals(404,post("/solicitudes/"+s.getIdSolicitud()+"/cancelar","").statusCode());
    }
    @Test void rechazarCancelacionAprobada() {
        var s=solicitudes.crear(formulario(1,2));
        jdbc.update("UPDATE solicitud SET estado='APROBADA' WHERE id_solicitud=?",s.getIdSolicitud());
        assertThrows(IllegalArgumentException.class,()->solicitudes.cancelar(s.getIdSolicitud()));
        assertEquals(EstadoSolicitud.APROBADA,solicitudes.getPropia(s.getIdSolicitud()).getEstado());
    }
    @Test void categoriasUnicasYConEquiposActivos() {
        assertThrows(IllegalArgumentException.class,()->categorias.guardar(null,"computación"));
        assertThrows(IllegalArgumentException.class,()->categorias.guardar(null,"  "));
        assertThrows(IllegalArgumentException.class,()->categorias.inactivar(1));
        categorias.guardar(1,"Portátiles");
        assertEquals(1,jdbc.queryForObject("SELECT id_categoria FROM equipo WHERE id_equipo=1",Integer.class));
        categorias.inactivar(2);
        assertFalse(categorias.getActivas().stream().anyMatch(c->c.getIdCategoria()==2));
    }
    @Test void historialEstadosYPrestamoAbiertoBloquea() {
        var s=solicitudes.crear(formulario(1,3));
        jdbc.update("UPDATE solicitud SET estado='APROBADA' WHERE id_solicitud=?",s.getIdSolicitud());
        var ahora=LocalDateTime.now();
        jdbc.update("INSERT INTO prestamo (id_solicitud,entrega,vencimiento) VALUES (?,?,?)",s.getIdSolicitud(),ahora.minusDays(1),ahora.plusDays(1));
        assertEquals("Activo",prestamos.getPropios().get(0).getEstado());
        assertThrows(IllegalArgumentException.class,()->solicitudes.crear(formulario(1,10)));
        jdbc.update("UPDATE prestamo SET vencimiento=?",ahora.minusHours(1));
        assertEquals("Atrasado",prestamos.getPropios().get(0).getEstado());
        jdbc.update("UPDATE prestamo SET devolucion=?",ahora);
        assertEquals("Devuelto",prestamos.getPropios().get(0).getEstado());
        assertNotNull(solicitudes.crear(formulario(1,10)));
        jdbc.update("UPDATE solicitud SET id_usuario=2 WHERE id_solicitud=?",s.getIdSolicitud());
        assertTrue(prestamos.getPropios().isEmpty());
    }
    @Test void pantallasYFormularioHttp() throws Exception {
        for(String ruta:new String[]{"/inicio","/equipos","/equipos/1","/inventario","/categorias","/solicitudes","/solicitudes/nueva?equipoId=1","/prestamos"})
            assertEquals(200,get(ruta).statusCode(),ruta);
        var f=formulario(1,2);
        String body="equipoId=1&inicio="+URLEncoder.encode(f.getInicio().toString(),StandardCharsets.UTF_8)
                +"&fin="+URLEncoder.encode(f.getFin().toString(),StandardCharsets.UTF_8)+"&motivo=Trabajo";
        var respuesta=post("/solicitudes/nueva",body);
        assertEquals(302,respuesta.statusCode());
        String detalle=URI.create(respuesta.headers().firstValue("location").orElseThrow()).getPath().split(";")[0];
        var pagina=get(detalle);
        assertEquals(200,pagina.statusCode()); assertTrue(pagina.body().contains("confirmarCancelacion"));
        assertEquals(302,post(detalle+"/cancelar","").statusCode());
        assertFalse(get(detalle).body().contains("confirmarCancelacion"));
        assertEquals(200,post("/solicitudes/nueva","equipoId=1&inicio=incorrecta&fin=&motivo=").statusCode());
    }
}
