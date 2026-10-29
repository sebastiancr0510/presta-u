package com.prestau.repository;

import com.prestau.domain.Equipo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipoRepository extends JpaRepository<Equipo, Integer> {
    @org.springframework.data.jpa.repository.Lock(jakarta.persistence.LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT e FROM Equipo e WHERE e.idEquipo = :id")
    java.util.Optional<Equipo> buscarParaSolicitud(@Param("id") Integer id);

    boolean existsByCategoriaIdCategoriaAndActivoTrue(Integer categoria);
    long countByCategoriaIdCategoria(Integer categoria);
    boolean existsByCodigo(String codigo);

    List<Equipo> findByActivoTrueOrderByNombreAsc();
    
    @Query("SELECT e FROM Equipo e ORDER BY e.activo DESC, e.codigo ASC")
    List<Equipo> findTodosOrdenados();

    @Query("SELECT e FROM Equipo e WHERE e.activo = true AND (LOWER(e.nombre) LIKE LOWER(CONCAT('%', :texto, '%')) OR LOWER(e.codigo) LIKE LOWER(CONCAT('%', :texto, '%'))) ORDER BY e.nombre ASC")
    List<Equipo> consultaJPQL(@Param("texto") String texto);

    @Query("SELECT e FROM Equipo e WHERE e.activo = true AND e.categoria.idCategoria = :categoriaId AND (LOWER(e.nombre) LIKE LOWER(CONCAT('%', :texto, '%')) OR LOWER(e.codigo) LIKE LOWER(CONCAT('%', :texto, '%'))) ORDER BY e.nombre ASC")
    List<Equipo> consultaJPQLPorCategoria(@Param("texto") String texto, @Param("categoriaId") Integer categoriaId);
}
