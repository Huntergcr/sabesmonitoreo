package org.example.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "estado_desechos")
@Getter
@Setter
public class EstadoDesecho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "codigo_tipo", nullable = false, unique = true)
    private String codigoTipo;

    @Column(nullable = false)
    private String nombre;

    @Column(name = "nivel_llenado_porcentaje")
    private Integer nivelLlenadoPorcentaje;

    @Column(name = "conteo_items")
    private Integer conteoItems;

    @Column(name = "ultima_actualizacion")
    private LocalDateTime ultimaActualizacion;
}