package org.lubrimax.msvc_comprobante.msvc_comprobante.models.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import org.lubrimax.msvc_comprobante.msvc_comprobante.models.EstadoDelComprobante;
import org.lubrimax.msvc_comprobante.msvc_comprobante.models.TipoDeComprobante;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "comprobantes",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = "ordenId"),
            @UniqueConstraint(columnNames = "numero")
        })
public class Comprobante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version private int version;

    @NotNull
    @Column(nullable = false, unique = true)
    private Long ordenId;

    @NotNull private Long clienteId;

    @NotNull private Long serieId;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TipoDeComprobante tipo;

    @Column(nullable = false, unique = true)
    private String numero;

    @NotBlank private String receptorNombre;

    private String receptorDocumento;

    @Column(precision = 5, scale = 4)
    private BigDecimal tasaIgv;

    @Column(precision = 12, scale = 2)
    private BigDecimal subtotal;

    @Column(precision = 12, scale = 2)
    private BigDecimal igv;

    @Column(precision = 12, scale = 2)
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    private EstadoDelComprobante estado;

    private LocalDateTime emitidoEn;

    private String moneda = "PEN";

    @Valid
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "comprobante_id")
    private List<LineaDeComprobante> lineas = new ArrayList<>();

    @Valid
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "comprobante_id")
    private List<Pago> pagos = new ArrayList<>();

    public void emitir(String numero, BigDecimal tasa, BigDecimal totalRevalidado) {
        if (tipo == TipoDeComprobante.FACTURA
                && (receptorDocumento == null || !receptorDocumento.matches("\\d{11}"))) {
            throw new IllegalArgumentException("La factura exige RUC de 11 digitos");
        }
        if (lineas.isEmpty()) {
            throw new IllegalArgumentException("El comprobante requiere lineas");
        }
        subtotal =
                lineas.stream()
                        .map(LineaDeComprobante::subtotal)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
                        .setScale(2, RoundingMode.HALF_UP);
        tasaIgv = tasa;
        igv = subtotal.multiply(tasa).setScale(2, RoundingMode.HALF_UP);
        total = subtotal.add(igv);
        if (total.compareTo(totalRevalidado) != 0) {
            throw new IllegalStateException(
                    "El total facturado difiere de la liquidacion revalidada");
        }
        this.numero = numero;
        estado = EstadoDelComprobante.EMITIDO;
        emitidoEn = LocalDateTime.now();
    }

    public void registrarPago(Pago pago) {
        if (estado == EstadoDelComprobante.ANULADO) {
            throw new IllegalStateException("El comprobante esta anulado");
        }
        BigDecimal pagado =
                pagos.stream().map(Pago::getMonto).reduce(BigDecimal.ZERO, BigDecimal::add);
        if (pagado.add(pago.getMonto()).compareTo(total) > 0) {
            throw new IllegalArgumentException("Los pagos no pueden superar el total");
        }
        pago.setMomento(LocalDateTime.now());
        pagos.add(pago);
        if (pagado.add(pago.getMonto()).compareTo(total) == 0) {
            estado = EstadoDelComprobante.PAGADO;
        }
    }

    public void anular() {
        estado = EstadoDelComprobante.ANULADO;
    }

    public Long getId() {
        return id;
    }

    public int getVersion() {
        return version;
    }

    public Long getOrdenId() {
        return ordenId;
    }

    public void setOrdenId(Long ordenId) {
        this.ordenId = ordenId;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public Long getSerieId() {
        return serieId;
    }

    public void setSerieId(Long serieId) {
        this.serieId = serieId;
    }

    public TipoDeComprobante getTipo() {
        return tipo;
    }

    public void setTipo(TipoDeComprobante tipo) {
        this.tipo = tipo;
    }

    public String getNumero() {
        return numero;
    }

    public String getReceptorNombre() {
        return receptorNombre;
    }

    public void setReceptorNombre(String receptorNombre) {
        this.receptorNombre = receptorNombre;
    }

    public String getReceptorDocumento() {
        return receptorDocumento;
    }

    public void setReceptorDocumento(String receptorDocumento) {
        this.receptorDocumento = receptorDocumento;
    }

    public BigDecimal getTasaIgv() {
        return tasaIgv;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public BigDecimal getIgv() {
        return igv;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public EstadoDelComprobante getEstado() {
        return estado;
    }

    public LocalDateTime getEmitidoEn() {
        return emitidoEn;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public List<LineaDeComprobante> getLineas() {
        return lineas;
    }

    public void setLineas(List<LineaDeComprobante> lineas) {
        this.lineas = lineas == null ? new ArrayList<>() : lineas;
    }

    public List<Pago> getPagos() {
        return pagos;
    }
}
